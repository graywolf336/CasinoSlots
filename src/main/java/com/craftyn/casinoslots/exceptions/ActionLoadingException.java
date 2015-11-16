package com.craftyn.casinoslots.exceptions;

/**
 * Thrown when trying to load an action and the arguments passed to it aren't the expected format.
 * 
 * @version 1.0.0
 * @author graywolf336
 * @since 2.6.0
 */
public class ActionLoadingException extends Exception {
    private static final long serialVersionUID = 7223364566770975047L;

    public ActionLoadingException(String message) {
        super(message);
    }
}
