package com.craftyn.casinoslots.command;

import org.bukkit.command.CommandSender;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.util.Stat;

public class CasinoStats extends AnCommand {
	
	// Command for listing slot machine statistics
	public CasinoStats(CasinoSlots plugin, String[] args, CommandSender sender) {
		super(plugin, args, sender);
	}
	
	public Boolean process() {
		
		// Permissions
		if(player != null) {
			if(!plugin.permission.isAdmin(player)) {
				noPermission();
				return true;
			}
		}
		
		senderSendMessage("Statistics for registered types:");
		for(Stat stat : plugin.statsData.getStats()) {
			
			String type = stat.getType();
			Integer spins = stat.getSpins();
			Double won = stat.getWon();
			Double lost = stat.getLost();
			
			senderSendMessage(type + " - spins: " + spins +" - money won: " + won + " - money lost: " + lost);
		}
		
		return true;
	}

}