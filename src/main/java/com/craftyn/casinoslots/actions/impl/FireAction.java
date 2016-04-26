package com.craftyn.casinoslots.actions.impl;

import org.bukkit.entity.Player;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.actions.Action;
import com.craftyn.casinoslots.classes.Reward;
import com.craftyn.casinoslots.classes.SlotType;
import com.craftyn.casinoslots.exceptions.ActionLoadingException;

/**
 * The fire action. Usage: - fire [seconds]
 *
 * @author graywolf336
 * @since 3.0.0
 * @version 1.0.0
 */
public class FireAction extends Action {
    private static final int TICKS_PER_SECOND = 20;
    private String name = "Fire";
    private int length = 0;
    private int ticks = 0;

    public FireAction(CasinoSlots plugin, String... args) throws ActionLoadingException {
        super(plugin, args);

        if (args.length < 1)
            throw new ActionLoadingException("The arguments for the '" + this.getName() + "' action are not valid, requires at least one argument.");

        try {
            length = Integer.parseInt(args[0]);
            ticks = length * TICKS_PER_SECOND;
        } catch (NumberFormatException e) {
            throw new ActionLoadingException("The number argument for the '" + this.getName() + "' action is not valid. (not a valid number)");
        }
    }

    public boolean isValid() {
        return ticks > 0;
    }

    public boolean execute(SlotType type, Reward reward, Player player) {
        player.setFireTicks(ticks);
        return true;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return this.name.toLowerCase() + " " + length;
    }
}
