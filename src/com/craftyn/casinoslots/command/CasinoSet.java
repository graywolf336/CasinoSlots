package com.craftyn.casinoslots.command;

import org.bukkit.entity.Player;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.slot.SlotMachine;

public class CasinoSet extends AnCommand {
	
	// Command for setting the owner of a managed slot machine
	public CasinoSet(CasinoSlots plugin, String[] args, Player player) {
		super(plugin, args, player);
	}
	
	public Boolean process() {		
		// Correct command format
		if(args.length == 3) {
			if (args[1].equalsIgnoreCase("sign")) {

				// Slot exists
				if(plugin.slotData.isSlot(args[2])) {
					SlotMachine slot = plugin.slotData.getSlot(args[2]);
					plugin.slotData.togglePunchingSign(player, slot);
					sendMessage("Please punch the sign that you want us to know about.");
				}else {
					// Slot does not exist
					sendMessage("Invalid slot machine.");
				}
			}else {
				// Incorrect command format
				sendMessage("Usage:");
				sendMessage("  /casino set sign <slotname>");
			}
		}else if(args.length == 4) {
			if (args[1].equalsIgnoreCase("type")) {
				
				// Slot exists
				if(plugin.slotData.isSlot(args[2])) {
					if(plugin.typeData.isType(args[3])) {
						SlotMachine slot = plugin.slotData.getSlot(args[2]);
						String typeName = args[3];
						
						String oldType = slot.getType();
						slot.setType(typeName);
						sendMessage("Type successfully changed from '" + oldType + "' to '" + typeName + "'.");
						
					}else {
						// Type does not exist
						sendMessage("Invalid type of a slot machine.");
					}
				}else {
					// Slot does not exist
					sendMessage("Invalid slot machine.");
				}
			}else {
				// Incorrect command format
				sendMessage("Usage:");
				sendMessage("  /casino set type <slotname> <type>");
			}
		}
		
		// Incorrect command format
		else {
			sendMessage("Usage:");
			//                      0   1      2          3
			sendMessage("  /casino set sign <slotname>");
			sendMessage("  /casino set type <slotname> <type>");
		}
		
		return true;
	}

}
