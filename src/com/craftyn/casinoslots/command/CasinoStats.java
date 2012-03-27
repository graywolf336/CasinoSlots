package com.craftyn.casinoslots.command;

import org.bukkit.entity.Player;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.util.Stat;

public class CasinoStats extends AnCommand {
	
	// Command for listing slot machine statistics
	public CasinoStats(CasinoSlots plugin, String[] args, Player player) {
		super(plugin, args, player);
	}
	
	public Boolean process() {
		
		// Permissions
		if(!plugin.permission.isAdmin(player)) {
			noPermission();
			return true;
		}
		
		sendMessage("Statistics for registered types:");
		for(Stat stat : plugin.statsData.getStats()) {
			
			String type = stat.getType();
			Integer spins = stat.getSpins();
			Double won = stat.getWon();
			Double lost = stat.getLost();
			
			sendMessage(type + " - spins: " + spins +" - money won: " + won + " - money lost: " + lost);
		}
		
		return true;
	}

}