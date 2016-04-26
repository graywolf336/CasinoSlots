package com.craftyn.casinoslots.exceptions;

public class ClassConstructorIsntTwoException extends Exception {
    private static final long serialVersionUID = -1383945009133283606L;

    public ClassConstructorIsntTwoException(String nameOfClass, int parameterCount) {
        super(nameOfClass + "'s constructor contains " + parameterCount + " parameters instead of the required 2.");
    }
}
