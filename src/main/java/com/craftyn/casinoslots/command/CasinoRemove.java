package com.craftyn.casinoslots.command;

import org.bukkit.command.CommandSender;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.slot.SlotMachine;

public class CasinoRemove extends AnCommand {

    // Command for removing slot machine
    public CasinoRemove(CasinoSlots plugin, String[] args, CommandSender sender) {
        super(plugin, args, sender);
    }

    public Boolean process() {

        // Permissions
        if(player != null) {
            if(!plugin.permission.canCreate(player)) {
                noPermission();
                return true;
            }
        }

        // Correct command format
        if(args.length == 2) {

            // Slot exists
            if(plugin.slotData.isSlot(args[1])) {
                SlotMachine slot = plugin.slotData.getSlot(args[1]);

                // Can access slot
                if(isOwner(slot)) {
                    plugin.slotData.removeSlot(slot);
                    senderSendMessage("Slot machine removed.");
                }
                // No access
                else {
                    plugin.sendMessage(player,"You do not own this slot machine.");
                }
            }

            // Slot does not exist
            else {
                senderSendMessage("Invalid slot machine.");
            }
        }

        // Incorrect command format
        else {
            senderSendMessage("Usage:");
            senderSendMessage("/casino remove <name>");
        }
        return true;
    }

}