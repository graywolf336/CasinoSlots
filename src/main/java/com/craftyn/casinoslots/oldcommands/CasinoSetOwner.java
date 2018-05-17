package com.craftyn.casinoslots.oldcommands;

import org.bukkit.entity.Player;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.classes.OldSlotMachine;

public class CasinoSetOwner extends AnCommand {

    // Command for setting the owner of a managed slot machine
    public CasinoSetOwner(CasinoSlots plugin, String[] args, Player player) {
        super(plugin, args, player);
    }

    public boolean process() {
        // Correct command format
        if(args.length == 3) {

            // Slot exists
            if(plugin.getSlotManager().isSlot(args[1])) {
                OldSlotMachine slot = plugin.getSlotManager().getSlot(args[1]);

                // Can access slot
                if(isOwner(slot)) {
                    String owner = args[2];
                    slot.setOwner(owner);
                    sendMessage(owner + " is now the owner of the " + args[1] + " slot machine.");
                }
                // No access
                else {
                    sendMessage("You do not own this slot machine.");
                }
            }

            // Slot does not exist
            else {
                sendMessage("Invalid slot machine.");
            }
        }

        // Incorrect command format
        else {
            sendMessage("Usage:");
            sendMessage("/casino setowner <slotname> <owner>");
        }
        return true;
    }

}