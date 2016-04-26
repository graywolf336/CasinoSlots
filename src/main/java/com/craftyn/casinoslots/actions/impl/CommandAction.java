package com.craftyn.casinoslots.actions.impl;

import org.bukkit.entity.Player;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.actions.Action;
import com.craftyn.casinoslots.classes.Reward;
import com.craftyn.casinoslots.classes.SlotType;
import com.craftyn.casinoslots.exceptions.ActionLoadingException;
import com.craftyn.casinoslots.util.Util;

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

    public CommandAction(CasinoSlots plugin, String... args) throws ActionLoadingException {
        super(plugin, args);
        this.plugin = plugin;

        if (args.length < 1)
            throw new ActionLoadingException("The arguments for the '" + this.getName() + "' action are not valid, requires at least one argument.");

        for (String s : args)
            command += (!command.isEmpty() ? " " : "") + s;

        if (command.isEmpty())
            throw new ActionLoadingException("The arguments for the '" + this.getName() + "' action are not valid, the resulting command is empty.");
    }

    public boolean isValid() {
        return !command.isEmpty();
    }

    public boolean execute(SlotType type, Reward reward, Player player) {
        String cmd = Util.tokenize(type, reward, player, command);

        plugin.debug("Command for " + type.getName() + ": " + cmd);
        return plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), cmd);
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return this.name.toLowerCase() + " " + command;
    }
}
