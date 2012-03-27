package com.craftyn.casinoslots.command;

import org.bukkit.entity.Player;

import com.craftyn.casinoslots.CasinoSlots;

public class Casino extends AnCommand {
	
	public Casino(CasinoSlots plugin, String[] args, Player player) {
		super(plugin, args, player);
	}
	
	public Boolean process() {
		
		if(plugin.permission.isAdmin(player)) {
			sendMessage("Command guide:");
			sendMessage("/casino add - Add a new slot machine");
			sendMessage("/casino addmanaged - Add a new managed slot machine");
			sendMessage("/casino slot - Manage slot machines");
			sendMessage("/casino list - List slot machines and types");
			sendMessage("/casino reload - Reload config files from disk");
			sendMessage("/casino remove - Remove an existing slot machine");
			sendMessage("/casino stats - Global usage statistics");
			sendMessage("/casino type - Manage slot machine types");
		}
		
		else if(plugin.permission.canCreate(player)) {
			sendMessage("Command guide:");
			sendMessage("/casino add - Add a new slot machine");
			sendMessage("/casino addmanaged - Add a new managed slot machine");
			sendMessage("/casino slot - Manage slot machines");
			sendMessage("/casino list - List slot machines and types");
			sendMessage("/casino remove - Remove an existing slot machine");
		}
		else {
			noPermission();
		}
		return true;
	}
}