package com.craftyn.casinoslots.actions;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.classes.Type;
import com.craftyn.casinoslots.exceptions.ActionLoadingException;

/**
 * The class which the actions must extend to be added which can be used.
 * 
 * @author graywolf336
 * @since 3.0.0
 * @version 1.0.0
 */
public abstract class Action implements IAction {
    public Action(CasinoSlots plugin, Type type, String... args) throws ActionLoadingException {
        plugin.debug("Loading the action: " + this.getClass().getSimpleName());
    }
}
