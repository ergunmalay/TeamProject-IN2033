package com.novasolutions.ipospu.service;

import com.novasolutions.ipospu.db.CommercialApplicationDAO;
import com.novasolutions.ipospu.db.MemberDAO;
import com.novasolutions.ipospu.model.Member;
import org.mindrot.jbcrypt.BCrypt;

import java.security.SecureRandom;
import java.util.UUID;

// I put all membership business logic here, keeping the controllers and DAO thin.
public class MembershipService {

    private final MemberDAO memberDAO = new MemberDAO();
    private final CommercialApplicationDAO commercialApplicationDAO = new CommercialApplicationDAO();

    // I define these character pools to satisfy the password complexity requirements in UC-01a.
    private static final String LETTERS = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String NUMBERS = "0123456789";
    private static final String SPECIAL = "!@#$%^&*";
    private static final String ALL = LETTERS + NUMBERS + SPECIAL;

    // I generate a random 10-character password that always contains at least
    // one letter, one number, and one special character, as required by UC-01a.
    private String generatePassword() {
        SecureRandom random = new SecureRandom();
        char[] password = new char[10];

        // I guarantee one of each required type in the first three slots.
        password[0] = LETTERS.charAt(random.nextInt(LETTERS.length()));
        password[1] = NUMBERS.charAt(random.nextInt(NUMBERS.length()));
        password[2] = SPECIAL.charAt(random.nextInt(SPECIAL.length()));

        // I fill the remaining slots from the full character pool.
        for (int i = 3; i < 10; i++) {
            password[i] = ALL.charAt(random.nextInt(ALL.length()));
        }

        // I shuffle the array so the required characters don't always appear at the front.
        for (int i = 9; i > 0; i--) {
            int j = random.nextInt(i + 1);
            char temp = password[i];
            password[i] = password[j];
            password[j] = temp;
        }

        return new String(password);
    }

    /**
     * UC-01a: Registers a new non-commercial member.
     * Generates a random password, hashes it with BCrypt, and stores the account.
     * The is_first_login flag defaults to true in the database so the member can
     * be prompted to change their password on first login.
     *
     * @param name  the member's full name
     * @param email the member's email address (used as their username)
     * @return a RegistrationResult indicating success or failure; contains the
     * generated password on success or an error message on failure
     */
    public RegistrationResult registerNonCommercial(String name, String email) {
        // I reject null or blank inputs early to avoid unnecessary DB calls.
        if (name == null || email == null) {
            System.out.println("❌ Name and email cannot be null");
            return RegistrationResult.failure("Name and email cannot be empty");
        }

        if (!email.contains("@")) {
            System.out.println("❌ Invalid email format: " + email);
            return RegistrationResult.failure("Invalid email format");
        }

        if (name.isBlank() || email.isBlank()) {
            System.out.println("❌ Name and email cannot be blank");
            return RegistrationResult.failure("Name and email cannot be blank");
        }

        // I check for duplicate emails before attempting to insert.
        if (memberDAO.emailExists(email)) {
            System.out.println("❌ Email already exists: " + email);
            return RegistrationResult.failure("Email already in use");
        }

        String plainPassword = generatePassword();
        String hashedPassword = BCrypt.hashpw(plainPassword, BCrypt.gensalt());

        // I pass the full_name value and set the member type and status for non-commercial registration.
        boolean created = memberDAO.createMember(name, email, hashedPassword, "NON_COMMERCIAL", "APPROVED");
        if (created) {
            System.out.println("✅ Registration successful for " + name);
            return RegistrationResult.success(plainPassword);
        } else {
            System.out.println("❌ Registration failed for " + name);
            return RegistrationResult.failure("Registration failed due to a database error");
        }
    }

    /**
     * UC-01b: Submits a commercial membership application for SA review.
     *
     * A PENDING COMMERCIAL member row is created immediately (so full_name and
     * company_name are stored) alongside the commercial_applications row.
     * Both inserts happen in a single transaction inside the DAO.
     *
     * The member is given a placeholder BCrypt hash of a random UUID — nobody
     * knows the plaintext so the account cannot be logged into until an SA
     * approves the application and sets a real password.
     *
     * @param applicantName      full name of the applicant (→ members.full_name)
     * @param companyName        trading name (→ members.company_name)
     * @param companiesHouseNumber registered company number (→ commercial_applications)
     * @param directorNames      director names (→ commercial_applications)
     * @param businessType       type of business (→ commercial_applications)
     * @param businessAddress    registered address (→ commercial_applications)
     * @param email              contact email (→ both tables)
     * @return a CommercialApplicationResult indicating success or failure
     */
    public CommercialApplicationResult submitCommercialApplication(
            String applicantName, String companyName,
            String companiesHouseNumber, String directorNames,
            String businessType, String businessAddress, String email) {

        // I reject null or blank inputs early to avoid unnecessary DB calls.
        if (applicantName == null || companyName == null || companiesHouseNumber == null
                || directorNames == null || businessType == null || businessAddress == null
                || email == null) {
            return CommercialApplicationResult.failure("All fields are required");
        }

        if (applicantName.isBlank() || companyName.isBlank() || companiesHouseNumber.isBlank()
                || directorNames.isBlank() || businessType.isBlank() || businessAddress.isBlank()
                || email.isBlank()) {
            return CommercialApplicationResult.failure("All fields are required");
        }

        if (!email.contains("@")) {
            return CommercialApplicationResult.failure("Invalid email format");
        }

        // I check for a PENDING application first to give the most specific message.
        // A previously rejected applicant has no PENDING application, so they are not blocked here.
        if (commercialApplicationDAO.pendingApplicationExistsForEmail(email)) {
            System.out.println("❌ Duplicate pending commercial application: " + email);
            return CommercialApplicationResult.failure(
                    "A pending application already exists for this email address");
        }

        // I then check the members table — catches existing approved/rejected members
        // whose email was already claimed, and also catches any edge-case duplicate pending members.
        if (memberDAO.emailExists(email)) {
            System.out.println("❌ Email already claimed for commercial application: " + email);
            return CommercialApplicationResult.failure(
                    "An account already exists for this email address");
        }

        // I generate a BCrypt hash of a random UUID as a placeholder.
        // The plaintext is discarded immediately so the account is effectively locked
        // until an SA approves the application and sets a real password.
        String placeholderHash = BCrypt.hashpw(UUID.randomUUID().toString(), BCrypt.gensalt());

        boolean submitted = commercialApplicationDAO.submitApplication(
                applicantName, companyName, placeholderHash,
                companiesHouseNumber, directorNames, businessType, businessAddress, email);

        if (submitted) {
            System.out.println("✅ Commercial application submitted for: " + email);
            return CommercialApplicationResult.success(
                    "Your application has been submitted for review. " +
                    "A System Administrator will contact you at " + email + " once it has been processed.");
        } else {
            System.out.println("❌ Commercial application DB insert failed for: " + email);
            return CommercialApplicationResult.failure(
                    "Failed to submit application due to a database error. Please try again.");
        }
    }

    public LoginResult authenticate(String email, String password) {

        if (email == null || password == null || email.isBlank() || password.isBlank()) {
            return LoginResult.failure("Email and password cannot be empty");
        }

        Member member = memberDAO.findByEmail(email);

        if (member == null) {
            return LoginResult.failure("No member found with that email");
        }

        if (BCrypt.checkpw(password, member.passwordHash())) {
            return LoginResult.success("Login successful", member);
        } else {
            return LoginResult.failure("Invalid email or password");
        }
    }
}