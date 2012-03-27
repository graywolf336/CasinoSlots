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
		
		plugin.reloadConfig();
		plugin.slotData.loadSlots();
		plugin.typeData.loadTypes();
		sendMessage("Configuration reloaded");
		return true;
	}

}