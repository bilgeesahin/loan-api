package com.trial.loan_api.dto.response;

public class SimpleResponse {
    private final boolean success;
    private final String message;

    public static SimpleResponse success(String message) {
        return new SimpleResponse(true, message);
    }

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        return message;
    }

    public SimpleResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public static SimpleResponse fail(String message) {
        return new SimpleResponse(false, message);
    }

    public static SimpleResponse fail(Exception ex) {
        return new SimpleResponse(false, ex.getMessage());
    }
}
