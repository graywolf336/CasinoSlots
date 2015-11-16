package com.craftyn.casinoslots.exceptions;

public class ClassConstructorIsntThreeException extends Exception {
    private static final long serialVersionUID = -1383945009133283606L;

    public ClassConstructorIsntThreeException(String nameOfClass) {
        super(nameOfClass + " doesn't have the required amount of three Parameters which an action requires.");
    }
}
