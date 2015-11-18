package com.craftyn.casinoslots.actions.impl;

import org.bukkit.entity.Player;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.actions.Action;
import com.craftyn.casinoslots.classes.Reward;
import com.craftyn.casinoslots.classes.Type;
import com.craftyn.casinoslots.exceptions.ActionLoadingException;

/**
 * The smite action. Usage: - smite [times]
 *
 * @author graywolf336
 * @since 3.0.0
 * @version 1.0.0
 */
public class SmiteAction extends Action {
    private String name = "Smite";
    private CasinoSlots plugin;
    private int times;

    public SmiteAction(CasinoSlots plugin, Type type, String... args) throws ActionLoadingException {
        super(plugin, type, args);
        this.plugin = plugin;

        switch (args.length) {
            case 1:
                try {
                    times = Integer.parseInt(args[0]);
                } catch (NumberFormatException e) {
                    throw new ActionLoadingException("The number argument for the '" + this.getName() + "' action for " + type.getName() + " is not valid. (not a valid number)");
                }
                break;
            default:
                times = 1;
        }
    }

    public boolean isValid() {
        return times > 0;
    }

    public boolean execute(Type type, Reward reward, final Player player) {
        for (int i = 1; i < times; i++) {
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                public void run() {
                    player.getWorld().strikeLightning(player.getLocation());
                }
            }, 2 * i);
        }

        return true;
    }

    public String getName() {
        return this.name;
    }
}
