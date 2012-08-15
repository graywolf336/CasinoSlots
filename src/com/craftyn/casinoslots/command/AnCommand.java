package com.craftyn.casinoslots.command;

import org.bukkit.entity.Player;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.slot.SlotMachine;

public abstract class AnCommand {
	
	public CasinoSlots plugin;
	public Player player;
	public String[] args;
	
	// Initializes new command
	public AnCommand(CasinoSlots plugin, String[] args, Player player) {
		
		this.plugin = plugin;
		this.args = args;
		this.player = player;
		
	}
	
	// Processes command, handled by subclasses
	public Boolean process() {
		return true;
	}
	
	// Returns true if player owns this slot machine
	public Boolean isOwner(SlotMachine slot) {
		
		if(plugin.permission.isAdmin(player) || slot.getOwner().equalsIgnoreCase(player.getName())) {
			return true;
		}
		return false;
	}
	
	// Called when a player is denied permission to a command
	public void noPermission() {
		plugin.sendMessage(player, "You don't have permission to do this.");
	}
	
	// Sends a message to the player
	/**
	 * Sends a message to the player who did the command.
	 *
	 * @param message The message to send to the player.
	 */
	public void sendMessage(String message) {
		plugin.sendMessage(player, message);
	}

}