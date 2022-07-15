package com.scut.exception;

public class TokenNotFoundException extends RuntimeException{
    private static final long serialVersionUID = -1601891767378579L;

    @Override
    public String getMessage() {
        return "Token not found";
    }
}
