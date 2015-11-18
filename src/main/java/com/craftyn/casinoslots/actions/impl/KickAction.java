package com.craftyn.casinoslots.actions.impl;

import org.bukkit.entity.Player;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.actions.Action;
import com.craftyn.casinoslots.classes.Reward;
import com.craftyn.casinoslots.classes.Type;
import com.craftyn.casinoslots.exceptions.ActionLoadingException;

/**
 * The Kick action. Usage: - kick the message goes here ["You cheated the Casino!"]
 *
 * @author graywolf336
 * @since 3.0.0
 * @version 1.0.0
 */
public class KickAction extends Action {
    private String name = "Kick";
    private String message = "You cheated the Casino!";

    public KickAction(CasinoSlots plugin, Type type, String... args) throws ActionLoadingException {
        super(plugin, type, args);

        for (String s : args)
            message += " " + s;
    }

    public boolean isValid() {
        return !message.isEmpty();
    }

    public boolean execute(Type type, Reward reward, Player player) {
        player.kickPlayer(message);
        return true;
    }

    public String getName() {
        return this.name;
    }
}
