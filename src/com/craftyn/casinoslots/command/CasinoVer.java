package com.craftyn.casinoslots.command;

import org.bukkit.entity.Player;

import com.craftyn.casinoslots.CasinoSlots;

public class CasinoVer extends AnCommand {
	
	public CasinoVer(CasinoSlots plugin, String[] args, Player player) {
		super(plugin, args, player);
	}

	public Boolean process() {
		
		// Admin permission
		if(!plugin.permission.isAdmin(player)) {
			noPermission();
			return true;
		}
		
		if(args.length == 1) {
			String ver = plugin.pluginVer;
			sendMessage("Version " + ver);
			return true;
		}else {
			sendMessage("Usage:");
			sendMessage("    /casino ver");
			sendMessage("    /casino version");
			return true;
		}
	}
}
