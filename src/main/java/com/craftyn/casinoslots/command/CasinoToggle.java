package com.craftyn.casinoslots.command;

import org.bukkit.command.CommandSender;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.classes.OldSlotMachine;
import com.craftyn.casinoslots.util.PermissionUtil;

public class CasinoToggle extends AnCommand {

    // Command for withdrawing money from a managed slot machine
    public CasinoToggle(CasinoSlots plugin, String[] args, CommandSender sender) {
        super(plugin, args, sender);
    }

    public Boolean process() {
        // Admin permission
        if(player != null) {
            if(!PermissionUtil.isAdmin(player)) {
                noPermission();
                return true;
            }
        }

        if (args.length < 2) {
            senderSendMessage("Please type which slot you want to toggle.");
            senderSendMessage("   /casino toggle <slot>");
            return true;
        }
        // Slot exists
        if(plugin.getSlotManager().isSlot(args[1])) {
            OldSlotMachine slot = plugin.getSlotManager().getSlot(args[1]);
            slot.toggleBusy();
            senderSendMessage("Slot machine toggled.");
            return true;
        }

        // Slot does not exist
        else {
            senderSendMessage("Invalid slot machine.");
            senderSendMessage("   /casino toggle <slot>");
            return true;
        }
    }
}
