package com.craftyn.casinoslots.actions.impl;

import org.bukkit.entity.Player;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.actions.Action;
import com.craftyn.casinoslots.classes.Reward;
import com.craftyn.casinoslots.classes.Type;
import com.craftyn.casinoslots.exceptions.ActionLoadingException;

/**
 * Represents the "kill" action.
 *
 * @author graywolf336
 * @since 3.0.0
 * @version 1.0.0
 */
public class KillAction extends Action {
    private String name = "Kill";

    public KillAction(CasinoSlots plugin, Type type, String... args) throws ActionLoadingException {
        super(plugin, type, args);
    }

    public boolean isValid() {
        return true;
    }

    public boolean execute(Type type, Reward reward, Player player) {
        player.setHealth(0);
        return true;
    }

    public String getName() {
        return this.name;
    }
}
