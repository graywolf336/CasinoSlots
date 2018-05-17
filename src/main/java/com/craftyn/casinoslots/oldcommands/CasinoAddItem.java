package com.craftyn.casinoslots.oldcommands;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.classes.OldSlotMachine;
import com.craftyn.casinoslots.classes.SlotType;
import com.craftyn.casinoslots.util.PermissionUtil;

public class CasinoAddItem extends AnCommand {
    // Command for adding unmanaged slot machine
    public CasinoAddItem (CasinoSlots plugin, String[] args, Player player) {
        super(plugin, args, player);
    }

    public boolean process() {
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
        if(args.length == 5) {

            // Slot does not exist
            if(!plugin.getSlotManager().isSlot(args[1])) {
                SlotType type;

                // Valid type
                if(plugin.getTypeManager().isValidType(args[2])) {
                    String typeName = args[2];

                    // Has type permission
                    if(PermissionUtil.canCreateItemsType(player, typeName)) {
                        type = plugin.getTypeManager().getType(typeName);
                    } else {//doesn't have permission
                        plugin.sendMessage(player, ChatColor.RED + "You do not have permission to create an item slot.");
                        return true;
                    }
                } // Invalid type
                else {
                    plugin.sendMessage(player, "Invalid type " + args[2]);
                    return true;
                }

                //see if the itemID is an int
                int cmditemID;
                try {
                    cmditemID = Integer.parseInt(args[3]);
                } catch (NumberFormatException e) {
                    plugin.sendMessage(player, "The item id that it will cost has to be a number.");
                    return true;
                }

                //see if the amount is an int
                int cmditemAMT;
                try {
                    cmditemAMT = Integer.parseInt(args[4]);
                } catch (NumberFormatException e) {
                    plugin.sendMessage(player, "The amount of items that it will cost has to be a number.");
                    return true;
                }

                // Creation cost
                Double createCost = type.getCreateCost();
                if(plugin.getEconomy().has(player, createCost)) {
                    plugin.getEconomy().withdrawPlayer(player, createCost);
                } else {
                    sendMessage("You can't afford to create this slot machine. Cost: " + createCost);
                    return true;
                }

                //Good to start punching the blocks to create the slot.
                OldSlotMachine slot = new OldSlotMachine(args[1], type, player.getUniqueId(), player.getName(), player.getWorld().getName(), false, true, cmditemID, cmditemAMT);
                plugin.getSlotManager().toggleCreatingSlots(player.getName(), slot);
                plugin.sendMessage(player, "Punch a block to serve as the base for this slot machine.");
            }

            // Slot exists
            else {
                plugin.sendMessage(player, "Slot machine " + args[1] +" already registered.");
            }
        }

        // incorrect command format
        else {
            plugin.sendMessage(player, "Usage:");
            plugin.sendMessage(player, "/casino additem <name> <type> <itemID> <amount>");
        }
        return true;

    }
}