package com.craftyn.casinoslots.command;

import org.bukkit.entity.Player;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.slot.SlotMachine;
import com.craftyn.casinoslots.slot.Type;

public class CasinoSet extends AnCommand {
	
	// Command for setting the owner of a managed slot machine
	public CasinoSet(CasinoSlots plugin, String[] args, Player player) {
		super(plugin, args, player);
	}
	
	public Boolean process() {
		if(!plugin.permission.isAdmin(player)) {
			noPermission();
			return true;
		}
		
		switch(args.length) {
			case 2:
				if (args[1].equalsIgnoreCase("sign")) {
					// Incorrect command format
					sendMessage("Usage:");
					sendMessage("  /casino set sign <slotname>");
				}else if(args[1].equalsIgnoreCase("itemcost")) {
					// Incorrect command format
					sendMessage("Usage:");
					sendMessage("  /casino set itemcost <type> <itemid,damage,amount>");
				}else if (args[1].equalsIgnoreCase("type")) {
					// Incorrect command format
					sendMessage("Usage:");
					sendMessage("  /casino set type <slotname> <type>");
				}else if(args[1].equalsIgnoreCase("debug")) {
					if(plugin.configData.inDebug()) {
						plugin.configData.debug = false;
						sendMessage("Debugging disabled.");
						return true;
					}else {
						plugin.configData.debug = true;
						sendMessage("Debugging enabled.");
						return true;
					}
				} else {
					sendMessage("Usage:");
					//                      0   1         2       3
					sendMessage("  /casino set sign <slotname>");
					sendMessage("  /casion set itemcost <type> <itemid,damage,amount>");
					sendMessage("  /casino set type <slotname> <type>");
				} return true;
			case 3:
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
				}else if(args[1].equalsIgnoreCase("itemcost")) {
					// Incorrect command format
					sendMessage("Usage:");
					sendMessage("  /casino set itemcost <type> <itemid,damage,amount>");
				}else if (args[1].equalsIgnoreCase("type")) {
					// Incorrect command format
					sendMessage("Usage:");
					sendMessage("  /casino set type <slotname> <type>");
				}else {
					// Incorrect command format
					sendMessage("Usage:");
					sendMessage("  /casino set sign <slotname>");
					sendMessage("  /casino set type <slotname> <type>");
				} return true;
			case 4:
				if (args[1].equalsIgnoreCase("type")) {
					
					// Slot exists
					if(plugin.slotData.isSlot(args[2])) {
						if(plugin.typeData.isType(args[3])) {
							SlotMachine slot = plugin.slotData.getSlot(args[2]);
							String typeName = args[3];
							
							String oldType = slot.getType();
							slot.setType(typeName);
							plugin.slotData.saveSlot(slot);
							sendMessage("Type successfully changed from '" + oldType + "' to '" + typeName + "'.");
							
						}else {
							// Type does not exist
							sendMessage("Invalid type of a slot machine.");
						}
					}else {
						// Slot does not exist
						sendMessage("Invalid slot machine.");
					}
				}else if (args[1].equalsIgnoreCase("itemcost")) {
					if(plugin.typeData.isType(args[2])) {//verify it is a valid type
						String[] item = args[3].split("\\,");
						if(item.length == 2 || item.length == 3) {//verify the length of the given item
							if(!args[3].equalsIgnoreCase("0")) {//the given item is not air.
								Type t = plugin.typeData.getType(args[2]);
								t.setItemCost(args[3]);
								plugin.typeData.setItemCost(t, args[3]);
								sendMessage("itemCost successfully set for the type " + t.getName() + ".");
							}else {
								//can not be air
								sendMessage("Please set the itemCost to be something other than air.");
							}
						}else {
							//The item length is not correct, must be <itemid,damage,amount> or <itemid,amount>
							sendMessage("The item must be seperated by a comma and can only contain the item id, the item data value, and the amount.");
						}
					}else {
						// Type does not exist
						sendMessage("Invalid type of a slot machine.");
					}
				}else {
					// Incorrect command format
					sendMessage("Usage:");
					sendMessage("  /casino set type <slotname> <type>");
				} return true;
			default:
				sendMessage("Usage:");
				//                      0   1         2       3
				sendMessage("  /casino set sign <slotname>");
				sendMessage("  /casion set itemcost <type> <itemid,damage,amount>");
				sendMessage("  /casino set type <slotname> <type>");
				return true;
		}
	}
}