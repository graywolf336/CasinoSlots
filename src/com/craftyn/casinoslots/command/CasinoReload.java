package com.craftyn.casinoslots.command;

import org.bukkit.entity.Player;

import com.craftyn.casinoslots.CasinoSlots;

public class CasinoReload extends AnCommand {
	//TODO: Redo this whole thing!!!!
	
	/**
	 * Is initiated by a /casino reload, which is intended to reload the config.
	 * 
	 * @param plugin The main plugin class
	 * @param args The other arguments passed along with 'reload'
	 * @param player The one who did the command
	 */
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
		plugin.log("Testing");
		
		
		plugin.typeData.loadTypes();
		plugin.log("Testing");
		
		
		plugin.slotData.loadSlots();
		plugin.log("Testing");
		
		
		sendMessage("Configuration reloaded");
		return true;
	}

}