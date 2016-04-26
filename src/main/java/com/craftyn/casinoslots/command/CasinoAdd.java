package com.craftyn.casinoslots.command;

import org.bukkit.entity.Player;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.classes.OldSlotMachine;
import com.craftyn.casinoslots.classes.SlotType;
import com.craftyn.casinoslots.util.PermissionUtil;

public class CasinoAdd extends AnCommand {

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
            if(!plugin.getSlotManager().isSlot(args[1])) {
                SlotType type;

                // Check to see if the type is valid
                if(args.length < 3) {
                    type = plugin.getTypeManager().getType("default");
                } else if(plugin.getTypeManager().isValidType(args[2])) {
                    if(PermissionUtil.canCreate(player, args[2])) {
                        type = plugin.getTypeManager().getType(args[2]);
                    } else {
                        sendMessage("You do not have permission to create a machine with the type: " + args[2]);
                        return true;
                    }
                } else {
                    // Invalid type
                    sendMessage("Invalid type " + args[2]);
                    return true;
                }
                
                // Creation cost
                Double createCost = type.getCreateCost();
                if(plugin.getEconomy().has(player, createCost)) {
                    plugin.getEconomy().withdrawPlayer(player, createCost);
                } else {
                    sendMessage(player.getName());
                    sendMessage("You do not have enough to afford to create this slot machine. The cost is: " + createCost);
                    return true;
                }

                //Good to start punching the blocks to create the slot.
                OldSlotMachine slot = new OldSlotMachine(args[1], type, player.getUniqueId(), player.getName(), player.getWorld().getName(), false, false, 0, 0);
                plugin.getSlotManager().toggleCreatingSlots(player.getName(), slot);
                sendMessage("Punch a block to serve as the base for this slot machine.");
            }

            // Slot exists
            else {
                sendMessage("A slot machine by the name of \"" + args[1] + "\" is already registered.");
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