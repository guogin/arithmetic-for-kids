package com.yahaha.arithmetic.error;

import com.yahaha.arithmetic.model.Scope;

public class InvalidScopeException extends Exception {
    private Scope scope;
    private int min;
    private int max;
    private int errorCode;

    public static final int DERIVED_MIN_GT_MAX = 1;

    public InvalidScopeException(Scope scope, int min, int max, int errorCode) {
        super();

        this.scope = scope;
        this.min = min;
        this.max = max;
        this.errorCode = errorCode;
    }

    public Scope getScope() {
        return scope;
    }

    public int getErrorCode() {
        return errorCode;
    }

    @Override
    public String getMessage() {
        String message;

        switch (getErrorCode()) {
            case DERIVED_MIN_GT_MAX:
                message = String.format("The derived minimum %d > maximum %d, please check you scope definition: %s", min, max, scope.toString());
                break;
            default:
                message = "Unknown error";
                break;
        }

        return message;
    }
}
