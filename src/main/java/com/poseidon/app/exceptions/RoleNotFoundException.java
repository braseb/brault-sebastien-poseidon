package com.poseidon.app.exceptions;

public class RoleNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 6775637982847207488L;
    
    public RoleNotFoundException(String message) {
        super(message);
    }

}
