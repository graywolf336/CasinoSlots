package com.craftyn.casinoslots.actions.impl;

import org.bukkit.entity.Player;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.actions.Action;
import com.craftyn.casinoslots.classes.Reward;
import com.craftyn.casinoslots.classes.SlotType;
import com.craftyn.casinoslots.exceptions.ActionLoadingException;

/**
 * The AddXpLevel action. Usage: - addxplvl amount [0]
 *
 * @author graywolf336
 * @since 3.0.0
 * @version 1.0.0
 */
public class AddXpLevelAction extends Action {
    private String name = "AddXpLvl";
    private int lvl = 0;

    public AddXpLevelAction(CasinoSlots plugin, String... args) throws ActionLoadingException {
        super(plugin, args);

        if (args.length < 1)
            throw new ActionLoadingException("The arguments for the '" + this.getName() + "' action are not valid, requires at least one argument.");

        try {
            lvl = Integer.parseInt(args[0]);
        } catch (NumberFormatException e) {
            throw new ActionLoadingException("The number passed into '" + this.getName() + "' action is not a valid number.");
        }
    }

    public boolean isValid() {
        return lvl > -1;
    }

    public boolean execute(SlotType type, Reward reward, Player player) {
        player.setLevel(player.getLevel() + lvl);
        return true;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return this.name.toLowerCase() + " " + lvl;
    }
}
