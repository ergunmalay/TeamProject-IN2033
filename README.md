# IPOS-PU — InfoPharma Online Purchasing System (Public Portal)

**Team 24/C — Nova Solutions**  
IN2033 Team Project | City, St George's University of London

## Quick Start

### Prerequisites
- **JDK 17** (not newer — JavaFX 17.0.2 compatibility)
- **Maven 3.8+** (IntelliJ bundles this)
- **MySQL 8.0**
- **IntelliJ IDEA**

### Setup
1. Clone the repo
2. Open as Maven project in IntelliJ (File → Open → select `pom.xml`)
3. Copy `src/main/resources/db.properties.example` → `src/main/resources/db.properties`
4. Edit `db.properties` with your local MySQL credentials
5. Run the SQL scripts in order:
   ```
   mysql -u root -p < sql/ipos_pu_schema.sql
   mysql -u root -p < sql/ipos_pu_seed_data.sql
   ```
6. Run `MainApp.java`

### Running
- **IntelliJ:** Right-click `MainApp.java` → Run
- **Maven:** `mvn javafx:run`
- **Tests:** `mvn test`

## Project Structure

```
src/main/java/com/novasolutions/ipospu/
├── MainApp.java              JavaFX entry point
├── model/                    Domain objects (Member, Product, Order, etc.)
├── interfaces/
│   ├── provided/             APIs we expose to Teams 22/23
│   └── required/             APIs we consume from Teams 22/23
├── controller/               Implements provided interfaces
├── service/                  Business logic (OrderService, MembershipService, etc.)
├── impl/                     Adapters for external APIs + shared DB queries
├── db/                       DAOs + DatabaseConnection
└── gui/                      JavaFX screens

src/test/java/                JUnit 5 tests
sql/                          Schema, seed data, cross-subsystem views
```

## Git Workflow
- Branch from `main` using `feature/UC-XX-description` (e.g. `feature/UC-08-checkout`)
- Every team member commits under their own GitHub account
- GitHub activity is evidence of individual contribution (briefing requirement)

## Team
| Name    | Role                  | Primary Focus                              |
|---------|-----------------------|--------------------------------------------|
| Ergun   | PM / Programmer       | DatabaseConnection, cross-subsystem, SMTP  |
| Dan     | DPM / Programmer      | Interfaces, CheckoutScreen, OrderService   |
| Alesha  | Analyst / Tester      | MemberDAO, CartScreen, Refund, Reports     |
| Daanish | Designer / Analyst    | DB schema, Campaigns, Sales Report, VP     |
| Marwan  | Designer / Tester     | CatalogueScreen, Promotions, GUI pass      |
| Hassan  | Programmer / Tester   | LoginScreen, Registration, OrderHistory    |
