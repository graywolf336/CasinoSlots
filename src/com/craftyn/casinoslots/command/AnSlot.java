package com.craftyn.casinoslots.command;

import org.bukkit.entity.Player;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.slot.SlotMachine;
import com.craftyn.casinoslots.slot.Type;

public class AnSlot extends AnCommand {
	
	private SlotMachine slot;
	private Type type;
	
	// Command for managing slot machines
	public AnSlot(CasinoSlots plugin, String[] args, Player player) {
		super(plugin, args, player);
	}
	
	public Boolean process() {
		
		//Permissions
		if(!plugin.permission.isAdmin(player) && !plugin.permission.canCreate(player)) {
			noPermission();
			return true;
		}
		
		// Invalid args
		if(args.length < 3) {
			usage();
			return true;
		}
		
		// Valid slot machine
		if(plugin.slotData.isSlot(args[1])) {
			sendMessage("Invalid slot machine " + args[1]);
			return true;
		}
		else {
			this.slot = plugin.slotData.getSlot(args[1]);
		}
		
		// Slot owner
		if(!isOwner(slot)) {
			sendMessage("You do not own this slot machine.");
			return true;
		}
		
		// Edit slot type
		if(args[2].equalsIgnoreCase("type") && args.length == 4) {
			
			if(plugin.typeData.isType(args[3])) {
				this.type = plugin.typeData.getType(args[3]);
				
				if(plugin.permission.canCreate(player, type)) {
					String name = player.getName();
					Double cost = type.getCreateCost();
					
					if(plugin.economy.has(name, cost)) {
						plugin.economy.withdrawPlayer(name, cost);
						slot.setType(type.getName());
					}
					else {
						sendMessage("You don't have enough money. Cost: " + cost);
					}
					
				}
				else {
					noPermission();
				}
				
			}
			else {
				sendMessage("Invalid type " + args[3]);
			}
		}
		
		// Set slot managed
		else if(args[2].equalsIgnoreCase("setmanaged") && args.length == 3) {
			
			if(plugin.permission.canCreateManagedType(player, slot.getType())) {
				
				if(slot.isManaged()) {
					slot.setManaged(false);
					sendMessage(slot.getName() + " is now unmanaged.");
				}
				else {
					slot.setManaged(true);
					sendMessage(slot.getName() + " is now managed.");
				}
				
			}
			else {
				noPermission();
			}
		}
		
		// Set slot controller
		else if(args[2].equalsIgnoreCase("setcontroller") && args.length == 3) {
			
			plugin.slotData.togglePlacingController(player, slot);
			sendMessage("Punch a new block to serve as this slot machine's controller.");
		}
		
		else if(args[2].equalsIgnoreCase("deposit") && args.length == 4) {
			
			Double amount = Double.parseDouble(args[3]);
			
			// Insufficient funds
			if(!plugin.economy.has(player.getName(), amount)) {
				sendMessage("You can't afford to deposit this much.");
			}
			else {
				// Enable if needed
				if((slot.getFunds() + amount) > plugin.typeData.getMaxPrize(slot.getType())) {
					slot.setEnabled(true);
					sendMessage("Sufficient funds. Slot machine enabled.");
				}
				
				slot.deposit(amount);
				plugin.economy.withdrawPlayer(player.getName(), amount);
				sendMessage("Deposited " + amount +" to " + slot.getName());
			}
		}
		
		else if(args[2].equalsIgnoreCase("withdraw") && args.length == 4) {
			
			Double amount = Double.parseDouble(args[3]);
			
			// Insufficient funds
			if(slot.getFunds() < amount) {
				sendMessage("Not enough funds in " + slot.getName() + ". Withdrawing all available funds.");
				amount = slot.getFunds();
			}
			
			// Disable if necessary
			if((slot.getFunds() - amount) < plugin.typeData.getMaxPrize(slot.getType())) {
				slot.setEnabled(false);
			}
			
			slot.withdraw(amount);
			plugin.economy.depositPlayer(player.getName(), amount);
			sendMessage("Withdrew " + amount +" from " + slot.getName());
		}
		
		// Invalid args
		else {
			usage();
			return true;
		}
		
		// Save the slot machine
		plugin.slotData.addSlot(slot);
		plugin.slotData.saveSlot(slot);
		plugin.configData.saveSlots();
		
		return true;
	}
	
	// Sends usage information to the player
	private void usage() {
		
		String[] messages = {
				"Usage:",
				"/casino slot <slot> type <type>",
				"/casino slot <slot> setmanaged",
				"/casino slot <slot> setcontroller",
				"/casino slot <slot> deposit <amount>",
				"/casino slot <slot> withdraw <amount>"};
		
		for(String m : messages) {
			sendMessage(m);
		}
	}

}