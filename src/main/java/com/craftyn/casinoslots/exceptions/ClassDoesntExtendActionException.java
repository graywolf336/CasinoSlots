package com.craftyn.casinoslots.exceptions;

/**
 * Thrown when trying to add an action and it doesn't extend the Action class.
 * 
 * @version 1.0.0
 * @author graywolf336
 * @since 2.6.0
 */
public class ClassDoesntExtendActionException extends Exception {
    private static final long serialVersionUID = -4568169459218447601L;

    public ClassDoesntExtendActionException(String nameOfClass) {
        super(nameOfClass + " doesn't extend the Action class.");
    }
}
