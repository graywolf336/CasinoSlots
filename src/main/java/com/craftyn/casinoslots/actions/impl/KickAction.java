package com.craftyn.casinoslots.actions.impl;

import org.bukkit.entity.Player;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.actions.Action;
import com.craftyn.casinoslots.classes.Reward;
import com.craftyn.casinoslots.classes.SlotType;
import com.craftyn.casinoslots.exceptions.ActionLoadingException;
import com.craftyn.casinoslots.util.Util;

/**
 * The Kick action. Usage: - kick the message goes here ["You cheated the Casino!"]
 *
 * @author graywolf336
 * @since 3.0.0
 * @version 1.0.0
 */
public class KickAction extends Action {
    private String name = "Kick";
    private String message = "";

    public KickAction(CasinoSlots plugin, String... args) throws ActionLoadingException {
        super(plugin, args);

        for (String s : args)
            message += (!message.isEmpty() ? " " : "") + s;

        if (message.isEmpty())
            message = "&4You cheated the Casino!";
    }

    public boolean isValid() {
        return !message.isEmpty();
    }

    public boolean execute(SlotType type, Reward reward, Player player) {
        player.kickPlayer(Util.colorizeAndTokenize(type, reward, player, message));
        return true;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return this.name.toLowerCase() + " " + message;
    }
}
