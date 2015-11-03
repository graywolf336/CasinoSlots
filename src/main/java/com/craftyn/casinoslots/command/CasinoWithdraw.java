package com.craftyn.casinoslots.command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.slot.SlotMachine;

public class CasinoWithdraw extends AnCommand {

    // Command for withdrawing money from a managed slot machine
    public CasinoWithdraw(CasinoSlots plugin, String[] args, Player player) {
        super(plugin, args, player);
    }

    public Boolean process() {
        // Correct command format
        if(args.length == 3) {

            // Slot exists
            if(plugin.getSlotData().isSlot(args[1])) {
                SlotMachine slot = plugin.getSlotData().getSlot(args[1]);

                // Can access slot
                if(isOwner(slot)) {
                    if(slot.isBusy()) {
                        sendMessage(ChatColor.RED + "You can't withdraw money while the machine is in use!");
                        return true;
                    }
                    
                    String Line3 = args[2];
                    double amount;
                    try {
                        if (Line3.startsWith("-")) {
                            sendMessage("Must deposit a postive amount.");
                            return true;
                        }else {
                            amount = Double.parseDouble(args[2]);
                        }
                    } catch (NumberFormatException e) {
                        sendMessage("Third arugment must be a number.");
                        return true;
                    }

                    if (amount > slot.getFunds()) {
                        sendMessage("You can't withdraw more than is in the slot's account.");
                        return true;
                    }else {
                        slot.withdraw(amount);
                        plugin.getEconomy().depositPlayer(player, amount);
                        sendMessage(amount +  " withdrew from " + args[1] + ".");
                        sendMessage(args[1] + " now has " + slot.getFunds() + " in it.");
                        plugin.getSlotData().saveSlot(slot);
                    }
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
            sendMessage("/casino withdraw <slotname> <amount>");
        }
        return true;
    }

}