package com.craftyn.casinoslots.exceptions;

public class ActionClassConstructorParameterNotExpectedTypeException extends Exception {
    private static final long serialVersionUID = 3016761079252475071L;

    public ActionClassConstructorParameterNotExpectedTypeException(String nameOfClass, int parameterPosition) {
        super(nameOfClass + " doesn't have the expected type of parameter at position " + parameterPosition + ".");
    }
}
