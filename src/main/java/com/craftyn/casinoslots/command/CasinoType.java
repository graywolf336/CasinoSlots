package com.craftyn.casinoslots.command;

import org.bukkit.entity.Player;

import com.craftyn.casinoslots.CasinoSlots;

public class CasinoType extends AnCommand {
	
	// Command for managing types
	public CasinoType(CasinoSlots plugin, String[] args, Player player) {
		super(plugin, args, player);
	}
	
	public Boolean process() {
		
		// Permissions
		if(!plugin.permission.isAdmin(player)) {
			noPermission();
			return true;
		}
		
		// Invalid args
		if(args.length < 3) {
			
			sendMessage("Usage:");
			sendMessage("/casino type add <type>");
			sendMessage("/casino type edit <type>");
			sendMessage("/casino type remove <type>");
			return true;
		}
		
		// Add type command
		if(args[1].equalsIgnoreCase("add")) {
			
			if(plugin.typeData.isType(args[2])) {
				sendMessage("Type " + args[2] +" already exists.");
			}
			
			else {
				plugin.typeData.newType(args[2]);
				sendMessage("Type " + args[2] + " created! Configure it to your needs in config.yml before using it.");
			}
		}
		
		// Edit type command
		else if(args[1].equalsIgnoreCase("edit")) {
			sendMessage("Not yet implemented.");
		}
		
		// Remove type command
		else if(args[1].equalsIgnoreCase("remove")) {
			
			if(plugin.typeData.isType(args[2])) {
				plugin.typeData.removeType(args[2]);
				sendMessage("Type " + args[2] + " removed. Make sure to update any slot machines using this type.");
			}
			else {
				sendMessage("Invalid type " + args[2]);
			}
		}
		
		return true;
	}

}