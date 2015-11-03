package com.craftyn.casinoslots.command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.slot.SlotMachine;
import com.craftyn.casinoslots.util.PermissionUtil;

public class CasinoAddItem extends AnCommand {

    private String name, type, owner, world;
    private int cmditemID, cmditemAMT;

    // Command for adding unmanaged slot machine
    public CasinoAddItem (CasinoSlots plugin, String[] args, Player player) {
        super(plugin, args, player);
    }

    public Boolean process() {
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
            if(!plugin.getSlotData().isSlot(args[1])) {
                this.name = args[1];

                // Valid type
                if(plugin.getTypeData().isType(args[2])) {
                    String typeName = args[2];

                    // Has type permission
                    if(PermissionUtil.canCreateItemsType(player, typeName)) {
                        this.type = typeName;
                        this.owner = player.getName();
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
                try {
                    cmditemID = Integer.parseInt(args[3]);
                } catch (NumberFormatException e) {
                    plugin.sendMessage(player, "The item id that it will cost has to be a number.");
                    return true;
                }

                //see if the amount is an int
                try {
                    cmditemAMT = Integer.parseInt(args[4]);
                } catch (NumberFormatException e) {
                    plugin.sendMessage(player, "The amount of items that it will cost has to be a number.");
                    return true;
                }

                // Creation cost
                Double createCost = plugin.getTypeData().getType(type).getCreateCost();
                if(plugin.getEconomy().has(owner, createCost)) {
                    plugin.getEconomy().withdrawPlayer(owner, createCost);
                } else {
                    sendMessage("You can't afford to create this slot machine. Cost: " + createCost);
                    return true;
                }

                world = player.getWorld().getName();

                //Good to start punching the blocks to create the slot.
                SlotMachine slot = new SlotMachine(plugin, name, type, owner, world, false, true, cmditemID, cmditemAMT);
                plugin.getSlotData().toggleCreatingSlots(player.getName(), slot);
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