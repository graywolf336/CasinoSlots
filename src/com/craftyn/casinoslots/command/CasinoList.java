package com.craftyn.casinoslots.command;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.slot.SlotMachine;
import com.craftyn.casinoslots.slot.Type;

public class CasinoList extends AnCommand {
	
	public CasinoList(CasinoSlots plugin, String[] args, Player player) {
		super(plugin, args, player);
	}
	
	public Boolean process() {
		
		// Admin permission
		if(!plugin.permission.canCreate(player)) {
			noPermission();
			return true;
		}
		
		// Valid command format
		if(args.length == 2) {
			
			// List slot machines
			if(args[1].equalsIgnoreCase("slots")) {
				
				sendMessage("Registered slot machines:");
				for(SlotMachine slot : plugin.slotData.getSlots()) {
					
					// If not admin, list only owned by player
					if(isOwner(slot)) {
						Block b = slot.getController();
						String c = b.getX()+ "," +b.getY()+ "," +b.getZ();
						sendMessage(slot.getName() + " - type: " + slot.getType() + " - owner: " + slot.getOwner() + " - managed: " + slot.isManaged().toString() +" @ " + c);
					}
				}
			}
			
			// List types
			else if(args[1].equalsIgnoreCase("types")) {
				
				sendMessage("Available types:");
				for(Type type : plugin.typeData.getTypes()) {
					
					// If not admin, list only permitted types
					if(plugin.permission.canCreate(player, type)) {
						sendMessage(type.getName() + " - cost: " + type.getCost());
					}
				}
			}
			
			// Invalid args
			else {
				sendMessage("Usage:");
				sendMessage("/casino list slots - List slot machines");
				sendMessage("/casino list types - List types");
			}
		}
		
		// Incorrect command format
		else {
			sendMessage("Usage:");
			sendMessage("/casino list slots - List slot machines");
			sendMessage("/casino list types - List types");
		}
		return true;
	}

}