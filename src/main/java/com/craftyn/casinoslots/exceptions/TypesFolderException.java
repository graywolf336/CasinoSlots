package com.craftyn.casinoslots.exceptions;

/**
 * Thrown when trying to load the types but there is an issue with the folder.
 * 
 * @author graywolf336
 * @version 1.0.0
 * @since 3.0.0
 */
public class TypesFolderException extends Exception {
    private static final long serialVersionUID = 1159592952830601082L;

    public TypesFolderException() {
        super("The Types folder has a problem, likely is a file instead of a folder.");
    }
}
