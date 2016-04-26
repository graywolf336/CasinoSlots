package com.craftyn.casinoslots.actions.impl;

import org.bukkit.entity.Player;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.actions.Action;
import com.craftyn.casinoslots.classes.Reward;
import com.craftyn.casinoslots.classes.SlotType;
import com.craftyn.casinoslots.exceptions.ActionLoadingException;
import com.craftyn.casinoslots.util.Util;

/**
 * The broadcast action. Usage: - broadcast The message goes here
 *
 * @author graywolf336
 * @since 3.0.0
 * @version 1.0.0
 */
public class BroadcastAction extends Action {
    private String name = "Broadcast";
    private CasinoSlots plugin;
    private String message = "";

    public BroadcastAction(CasinoSlots plugin, String... args) throws ActionLoadingException {
        super(plugin, args);
        this.plugin = plugin;

        if (args.length < 1)
            throw new ActionLoadingException("The arguments for the '" + this.getName() + "' action are not valid, requires at least one argument.");

        for (String s : args)
            this.message += " " + s;

        this.message = this.message.trim();

        if (this.message.isEmpty())
            throw new ActionLoadingException("The arguments for the '" + this.getName() + "' action are not valid, the resulting message is empty.");
    }

    public boolean isValid() {
        //returning true here since we check above if the broadcast message is empty or not
        return true;
    }

    public boolean execute(SlotType type, Reward reward, Player player) {
        String msg = Util.colorizeAndTokenize(type, reward, player, message);

        plugin.debug("Broadcast message for " + type.getName() + ": " + msg);
        return plugin.getServer().broadcastMessage(msg) != 0;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return this.name.toLowerCase() + " " + message;
    }
}
