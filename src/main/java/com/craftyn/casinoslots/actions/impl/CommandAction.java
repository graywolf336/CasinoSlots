package com.craftyn.casinoslots.actions.impl;

import org.bukkit.entity.Player;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.actions.Action;
import com.craftyn.casinoslots.classes.Reward;
import com.craftyn.casinoslots.classes.Type;
import com.craftyn.casinoslots.exceptions.ActionLoadingException;

/**
 * The command action. Usage: - command The command goes here
 *
 * @author graywolf336
 * @since 3.0.0
 * @version 1.0.0
 */
public class CommandAction extends Action {
    private String name = "Command";
    private CasinoSlots plugin;
    private String command = "";

    public CommandAction(CasinoSlots plugin, Type type, String... args) throws ActionLoadingException {
        super(plugin, type, args);
        this.plugin = plugin;

        if (args.length < 1)
            throw new ActionLoadingException("The arguments for the '" + this.getName() + "' action for " + type.getName() + " are not valid, requires at least one argument.");

        for (String s : args)
            command += " " + s;

        command = command.replace("[cost]", String.valueOf(type.getCost())).replace("[type]", type.getName()).trim();

        if (command.isEmpty())
            throw new ActionLoadingException("The arguments for the '" + this.getName() + "' action for " + type.getName() + " are not valid, the resulting command is empty.");
    }

    public boolean isValid() {
        return !command.isEmpty();
    }

    public boolean execute(Type type, Reward reward, Player player) {
        String cmd = new String(command)
                .replace("[moneywon]", String.valueOf(reward.getMoney()))
                .replace("[player]", player.getName())
                .replace("[playername]", player.getDisplayName())
                .replace("[playeruuid]", player.getUniqueId().toString());

        plugin.debug("Command for " + type.getName() + ": " + cmd);
        return plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), cmd);
    }

    public String getName() {
        return this.name;
    }
}
