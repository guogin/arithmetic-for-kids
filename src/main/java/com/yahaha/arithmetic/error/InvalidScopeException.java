package com.yahaha.arithmetic.error;

public class InvalidScopeException extends Exception {
    private String i18nMessageKey;
    private Object[] arguments;

    public InvalidScopeException(String message, String i18nMessageKey, Object...arguments) {
        super(message);

        this.i18nMessageKey = i18nMessageKey;
        this.arguments = arguments;
    }

    public String getI18nMessageKey() {
        return i18nMessageKey;
    }

    public Object[] getArguments() {
        return arguments;
    }
}
