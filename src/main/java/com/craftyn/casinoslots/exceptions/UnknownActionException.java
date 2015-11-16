package com.craftyn.casinoslots.exceptions;

/**
 * Thrown when trying to get an action when it isn't defined.
 * 
 * @version 1.0.0
 * @author graywolf336
 * @since 2.6.0
 */
public class UnknownActionException extends Exception {
    private static final long serialVersionUID = -6578707858719862709L;

    public UnknownActionException(String message) {
        super(message);
    }
}
