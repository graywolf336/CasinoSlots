package com.craftyn.casinoslots.command;

import org.bukkit.entity.Player;

import com.craftyn.casinoslots.CasinoSlots;

public class CasinoReload extends AnCommand {
	//TODO: Redo this whole thing!!!!
	
	// casino reload command
	public CasinoReload(CasinoSlots plugin, String[] args, Player player) {
		super(plugin, args, player);
	}
	
	public Boolean process() {
		
		// Permissions
		if(!plugin.permission.isAdmin(player)) {
			noPermission();
			return true;
		}
		
		plugin.log("Testing");
		
		plugin.reloadConfig();
		plugin.log("Testing");
		
		
		plugin.slotData.loadSlots();
		plugin.log("Testing");
		
		
		plugin.typeData.loadTypes();
		plugin.log("Testing");
		
		sendMessage("Configuration reloaded");
		return true;
	}

}