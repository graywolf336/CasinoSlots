package com.craftyn.casinoslots.actions.impl;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.actions.Action;
import com.craftyn.casinoslots.classes.Reward;
import com.craftyn.casinoslots.classes.Type;
import com.craftyn.casinoslots.exceptions.ActionLoadingException;

/**
 * The rocket action. Usage: - rocket
 *
 * @author graywolf336
 * @since 3.0.0
 * @version 1.0.0
 */
public class RocketAction extends Action {
    private String name = "Rocket";

    public RocketAction(CasinoSlots plugin, Type type, String... args) throws ActionLoadingException {
        super(plugin, type, args);
    }

    public boolean isValid() {
        return true;
    }

    public boolean execute(Type type, Reward reward, Player player) {
        player.setVelocity(new Vector(0, 30, 0));
        return true;
    }

    public String getName() {
        return this.name;
    }
}
