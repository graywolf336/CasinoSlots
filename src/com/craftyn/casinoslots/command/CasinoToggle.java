package com.craftyn.casinoslots.command;

import org.bukkit.entity.Player;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.slot.SlotMachine;

public class CasinoToggle extends AnCommand {

	// Command for withdrawing money from a managed slot machine
	public CasinoToggle(CasinoSlots plugin, String[] args, Player player) {
		super(plugin, args, player);
	}
	
	public Boolean process() {
		if (args.length < 2) {
			plugin.sendMessage(player, "Please type which slot you want to toggle.");
			return true;
		}
		// Slot exists
		if(plugin.slotData.isSlot(args[1])) {
			SlotMachine slot = plugin.slotData.getSlot(args[1]);
			slot.toggleBusy();
			plugin.sendMessage(player, "Slot machine toggled.");
			return true;
		}
		
		// Slot does not exist
		else {
			plugin.sendMessage(player, "Invalid slot machine.");
			return true;
		}
	}
}
