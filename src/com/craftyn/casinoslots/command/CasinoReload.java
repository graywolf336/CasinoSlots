package com.craftyn.casinoslots.command;

import org.bukkit.command.CommandSender;

import com.craftyn.casinoslots.CasinoSlots;

public class CasinoReload extends AnCommand {
	
	/**
	 * Is initiated by a /casino reload, which is intended to reload the config.
	 * 
	 * @param plugin The main plugin class
	 * @param args The other arguments passed along with 'reload'
	 * @param player The one who did the command
	 */
	public CasinoReload(CasinoSlots plugin, String[] args, CommandSender sender) {
		super(plugin, args, sender);
	}
	
	public Boolean process() {
		
		// Permissions
		if(!plugin.permission.isAdmin(player)) {
			noPermission();
			return true;
		}
		
		plugin.configData.reloadConfigs();
		
		plugin.typeData.reloadTypes();
		
		plugin.slotData.reloadSlots();
		
		if(plugin.configData.inDebug()) senderSendMessage("Debugging enabled.");
		senderSendMessage("Configuration reloaded");
		return true;
	}

}