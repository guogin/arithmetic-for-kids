package com.yahaha.arithmetic.error;

public class InvalidScopeException extends Exception {
    private int errorCode;

    public static final int DERIVED_MIN_GT_MAX = 1;
    public static final int BORROW_NOT_POSSIBLE = 2;

    public InvalidScopeException(int errorCode, String message) {
        super(message);

        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
