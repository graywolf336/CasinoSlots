package com.craftyn.casinoslots.command;

import org.bukkit.command.CommandSender;

import com.craftyn.casinoslots.CasinoSlots;

public class Casino extends AnCommand {
	
	public Casino(CasinoSlots plugin, String[] args, CommandSender sender) {
		super(plugin, args, sender);
	}
	
	public Boolean process() {
		
		if(player == null) {
			senderSendMessage("Command guide:");
			senderSendMessage("  /casino list - List slot machines and types");
			senderSendMessage("  /casino reload - Reload config files from disk");
			senderSendMessage("  /casino stats - Global usage statistics");
			senderSendMessage("  /casino toggle - Toggles the state of a slot");
			senderSendMessage("  /casino version - To get the current version of the plugin.");
			return true;
		}
		
		if(plugin.permission.isAdmin(player)) {
			sendMessage("Command guide:");
			sendMessage("  /casino add - Add a new slot machine");
			sendMessage("  /casino addmanaged - Add a new managed slot machine");
			sendMessage("  /casino additem - Add a new managed slot machine");
			sendMessage("  /casino deposit - Deposit money to a managed slot");
			sendMessage("  /casino list - List slot machines and types");
			sendMessage("  /casino reload - Reload config files from disk");
			sendMessage("  /casino remove - Remove an existing slot machine");
			sendMessage("  /casino set - Enables various options to set");
			sendMessage("  /casino setowner - Sets the owner of a managed slot");
			sendMessage("  /casino stats - Global usage statistics");
			sendMessage("  /casino toggle - Toggles the state of a slot");
			sendMessage("  /casino type - Manage slot machine types");
			sendMessage("  /casino withdraw - Withdraw money from a managed slot");
			sendMessage("  /casino version - To get the current version of the plugin.");
		}
		
		else if(plugin.permission.canCreate(player)) {
			sendMessage("Command guide:");
			sendMessage("  /casino add - Add a new slot machine");
			sendMessage("  /casino addmanaged - Add a new managed slot machine");
			sendMessage("  /casino additem - Add a new managed slot machine");
			sendMessage("  /casino deposit - Deposit money to a managed slot");
			sendMessage("  /casino list - List slot machines and types");
			sendMessage("  /casino setowner - Sets the owner of a managed slot");
			sendMessage("  /casino remove - Remove an existing slot machine");
			sendMessage("  /casino withdraw - Withdraw money from a managed slot");
		}
		
		else if(plugin.permission.canCreateManaged(player)) {
			sendMessage("Command guide:");
			sendMessage("  /casino addmanaged - Add a new managed slot machine");
			sendMessage("  /casino deposit - Deposit money to a managed slot");
			sendMessage("  /casino withdraw - Withdraw money from a managed slot");
		}
		
		else {
			noPermission();
		}
		return true;
	}
}