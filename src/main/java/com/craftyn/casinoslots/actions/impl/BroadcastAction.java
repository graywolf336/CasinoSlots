package com.craftyn.casinoslots.actions.impl;

import org.bukkit.entity.Player;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.actions.Action;
import com.craftyn.casinoslots.classes.Reward;
import com.craftyn.casinoslots.classes.Type;
import com.craftyn.casinoslots.exceptions.ActionLoadingException;

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

    public BroadcastAction(CasinoSlots plugin, Type type, String... args) throws ActionLoadingException {
        super(plugin, type, args);
        this.plugin = plugin;

        if (args.length < 1)
            throw new ActionLoadingException("The arguments for the '" + this.getName() + "' action for " + type.getName() + " are not valid, requires at least one argument.");

        for (String s : args)
            message += " " + s;

        message = message.replace("[cost]", String.valueOf(type.getCost())).replace("[type]", type.getName()).trim();

        if (message.isEmpty())
            throw new ActionLoadingException("The arguments for the '" + this.getName() + "' action for " + type.getName() + " are not valid, the resulting message is empty.");
    }

    public boolean isValid() {
        return false;
    }

    public boolean execute(Type type, Reward reward, Player player) {
        String msg = new String(message)
                .replace("[moneywon]", String.valueOf(reward.getMoney()))
                .replace("[player]", player.getName())
                .replace("[playername]", player.getDisplayName())
                .replace("[playeruuid]", player.getUniqueId().toString());

        plugin.debug("Broadcast message for " + type.getName() + ": " + msg);
        int amount = plugin.getServer().broadcastMessage(msg);

        return amount != 0;
    }

    public String getName() {
        return this.name;
    }
}
