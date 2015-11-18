package com.craftyn.casinoslots.actions.impl;

import org.bukkit.entity.Player;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.actions.Action;
import com.craftyn.casinoslots.classes.Reward;
import com.craftyn.casinoslots.classes.Type;
import com.craftyn.casinoslots.exceptions.ActionLoadingException;

/**
 * The fire action. Usage: - fire [ticks]
 *
 * @author graywolf336
 * @since 3.0.0
 * @version 1.0.0
 */
public class FireAction extends Action {
    private String name = "Fire";
    private int length = 0;

    public FireAction(CasinoSlots plugin, Type type, String... args) throws ActionLoadingException {
        super(plugin, type, args);

        switch (args.length) {
            case 1:
                try {
                    length = Integer.parseInt(args[0]);
                } catch (NumberFormatException e) {
                    throw new ActionLoadingException("The number argument for the '" + this.getName() + "' action for " + type.getName() + " is not valid. (not a valid number)");
                }
                break;
            default:
                length = 120;
        }
    }

    public boolean isValid() {
        return length > 0;
    }

    public boolean execute(Type type, Reward reward, Player player) {
        player.setFireTicks(length);
        return true;
    }

    public String getName() {
        return this.name;
    }
}
