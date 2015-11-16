package com.craftyn.casinoslots.actions;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.classes.Type;
import com.craftyn.casinoslots.exceptions.ActionLoadingException;

public abstract class Action implements IAction {
    public Action(CasinoSlots plugin, Type type, String... args) throws ActionLoadingException {
        plugin.debug("Loading the action: " + this.getClass().getSimpleName());
    }
}
