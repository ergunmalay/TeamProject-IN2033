package com.novasolutions.ipospu.service;

public class LoginResult {
    private final boolean success;
    private final String message;

    private LoginResult(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public static LoginResult success(String message) {
        return new LoginResult(true, message);
    }

    public static LoginResult failure(String message) {
        return new LoginResult(false, message);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }
}