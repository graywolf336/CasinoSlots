package com.craftyn.casinoslots.oldcommands;

import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.classes.OldSlotMachine;
import com.craftyn.casinoslots.classes.SlotType;
import com.craftyn.casinoslots.util.PermissionUtil;

public class CasinoList extends AnCommand {

    public CasinoList(CasinoSlots plugin, String[] args, CommandSender sender) {
        super(plugin, args, sender);
    }

    public boolean process() {

        // Admin permission
        if(player != null) {
            if(!PermissionUtil.canCreate(player)) {
                noPermission();
                return true;
            }
        }

        // Valid command format
        if(args.length == 2) {

            // List slot machines
            if(args[1].equalsIgnoreCase("slots")) {

                senderSendMessage("Registered slot machines:");
                for(OldSlotMachine slot : plugin.getSlotManager().getSlots()) {

                    // If not admin, list only owned by player
                    if(isOwner(slot)) {
                        Block b = slot.getController();
                        String c = b.getX()+ "," +b.getY()+ "," +b.getZ();
                        senderSendMessage(slot.getName() + " - type: " + slot.getType() + " - owner: " + slot.getOwner() + " - managed: " + slot.isManaged().toString() +" @ " + c);
                    }
                }
            }

            // List types
            else if(args[1].equalsIgnoreCase("types")) {

                senderSendMessage("Available types:");
                for(SlotType type : plugin.getTypeManager().getTypes()) {
                    if(player == null) {
                        senderSendMessage(type.getName() + " - cost: " + type.getCost());
                    }else if(PermissionUtil.canCreate(player, type)) {
                        // If not admin, list only permitted types
                        senderSendMessage(type.getName() + " - cost: " + type.getCost());
                    }
                }
            }

            // Invalid args
            else {
                senderSendMessage("Usage:");
                senderSendMessage("/casino list slots - List slot machines");
                senderSendMessage("/casino list types - List types");
            }
        }

        // Incorrect command format
        else {
            senderSendMessage("Usage:");
            senderSendMessage("/casino list slots - List slot machines");
            senderSendMessage("/casino list types - List types");
        }
        return true;
    }

}