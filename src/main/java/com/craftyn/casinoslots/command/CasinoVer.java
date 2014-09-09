package com.craftyn.casinoslots.command;

import org.bukkit.command.CommandSender;

import com.craftyn.casinoslots.CasinoSlots;

public class CasinoVer extends AnCommand {
	
	public CasinoVer(CasinoSlots plugin, String[] args, CommandSender sender) {
		super(plugin, args, sender);
	}

	public Boolean process() {
		
		// Admin permission
		if(player != null) {
			if(!plugin.permission.isAdmin(player)) {
				noPermission();
				return true;
			}
		}
		
		if(args.length == 1) {
			String ver = plugin.pluginVer;
			senderSendMessage("Version " + ver);
			return true;
		}else {
			senderSendMessage("Usage:");
			senderSendMessage("    /casino ver");
			senderSendMessage("    /casino version");
			return true;
		}
	}
}
