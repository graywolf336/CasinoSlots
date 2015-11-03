package com.craftyn.casinoslots.command;

import org.bukkit.entity.Player;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.slot.SlotMachine;
import com.craftyn.casinoslots.util.PermissionUtil;

public class CasinoAdd extends AnCommand {

    private String name;
    private String type;
    private String owner;
    private String world;

    // Command for adding unmanaged slot machine
    public CasinoAdd(CasinoSlots plugin, String[] args, Player player) {
        super(plugin, args, player);
    }

    public Boolean process() {

        // Permissions
        if(!PermissionUtil.canCreate(player)) {
            noPermission();
            return true;
        }

        //Check for simple player things before they try to add a slot
        if(plugin.useTowny) {
            if(plugin.getConfigData().onlyTowns) {
                if(!plugin.getTownyChecks().checkTown(player)) {
                    plugin.sendMessage(player, plugin.getConfigData().noTown);
                    return true;
                }
            }

            if(plugin.getConfigData().onlyMayors) {
                if(!plugin.getTownyChecks().checkMayor(player)) {
                    plugin.sendMessage(player, plugin.getConfigData().noMayor);
                    return true;
                }
            }
        }

        // Valid command format
        if(args.length >= 2 && args.length <= 3) {

            // Slot does not exist
            if(!plugin.getSlotData().isSlot(args[1])) {

                this.name = args[1];

                // Valid type
                if(args.length < 3) {

                    type = "default";
                }

                else if(plugin.getTypeData().isType(args[2])) {
                    String typeName = args[2];

                    // Has type permission
                    if(!PermissionUtil.canCreate(player, typeName)) {
                        sendMessage("Invalid type " + typeName);
                        return true;
                    }
                    else {
                        type = typeName;
                    }
                }

                // Invalid type
                else {
                    sendMessage("Invalid type " + args[2]);
                    return true;
                }

                owner = player.getName();

                // Creation cost
                Double createCost = plugin.getTypeData().getType(type).getCreateCost();
                if(plugin.getEconomy().has(owner, createCost)) {
                    plugin.getEconomy().withdrawPlayer(owner, createCost);
                }
                else {
                    sendMessage(player.getName());
                    sendMessage("You can't afford to create this slot machine. Cost: " + createCost);
                    return true;
                }

                world = player.getWorld().getName();

                //Good to start punching the blocks to create the slot.
                SlotMachine slot = new SlotMachine(plugin, name, type, owner, world, false, false, 0, 0);
                plugin.getSlotData().toggleCreatingSlots(player.getName(), slot);
                sendMessage("Punch a block to serve as the base for this slot machine.");
            }

            // Slot exists
            else {
                sendMessage("Slot machine " + args[1] +" already registered.");
            }
        }

        // incorrect command format
        else {
            sendMessage("Usage:");
            sendMessage("/casino add <name> (<type>)");
        }
        return true;

    }

}