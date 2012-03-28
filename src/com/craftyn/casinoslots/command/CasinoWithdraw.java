package com.craftyn.casinoslots.command;

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
			if(plugin.slotData.isSlot(args[1])) {
				SlotMachine slot = plugin.slotData.getSlot(args[1]);
				
				// Can access slot
				if(isOwner(slot)) {
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
					slot.withdraw(amount);
					sendMessage(amount +  " withdrew from " + args[1] + ".");
					sendMessage(args[1] + " now has " + slot.getFunds() + " in it's account.");
					plugin.slotData.saveSlot(slot);
					plugin.configData.saveSlots();
					//plugin.configData.slots.set(slot.getName() + ".funds", slot.getFunds());
					//plugin.configData.save();
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
			sendMessage("/casino deposit <slotname> <amount>");
		}
		return true;
	}

}