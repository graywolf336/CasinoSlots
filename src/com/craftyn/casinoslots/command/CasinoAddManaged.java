package com.craftyn.casinoslots.command;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.slot.SlotMachine;

public class CasinoAddManaged extends AnCommand {
	
	private String name;
	private String type;
	private String owner;
	private String world;
	
	/**
	 * Instantiates a new managed CasinoSlot.
	 *
	 * @param plugin  The plugin.
	 * @param args    The args sent with the command.
	 * @param player  The player doing the command.
	 */
	public CasinoAddManaged (CasinoSlots plugin, String[] args, Player player) {
		super(plugin, args, player);
	}
	
	public Boolean process() {
		
		//Check for simple player things before they try to add a slot
		if(plugin.useTowny) {
			if(plugin.configData.onlyTowns) {
				if(!plugin.townyChecks.checkTown(player)) {
					plugin.sendMessage(player, plugin.configData.noTown);
					return true;
				}
			}
			
			if(plugin.configData.onlyMayors) {
				if(!plugin.townyChecks.checkMayor(player)) {
					plugin.sendMessage(player, plugin.configData.noMayor);
					return true;
				}
			}
		}
		
		// Valid command format
		if(args.length == 3) {
						
			// Slot does not exist
			if(!plugin.slotData.isSlot(args[1])) {
							
				this.name = args[1];
								
					// Valid type
				if(plugin.typeData.isType(args[2])) {
					String typeName = args[2];
					
					// Has type permission
					if(plugin.permission.canCreateManagedType(player, typeName)) {
						this.type = typeName;
						this.owner = player.getName();
					} else {
						plugin.sendMessage(player, ChatColor.RED + "You do not have permission to create a managed slot.");
						return true;
					}
				}
				
				// Invalid type
				else {
					plugin.sendMessage(player, "Invalid type " + args[2]);
					return true;
				}
				
				// Creation cost
				Double createCost = plugin.typeData.getType(type).getCreateCost();
				if(plugin.economy.has(owner, createCost)) {
					plugin.economy.withdrawPlayer(owner, createCost);
				} else {
					sendMessage("You can't afford to create this slot machine. Cost: " + createCost);
					return true;
				}
				
				world = player.getWorld().getName();
				
				//Good to start punching the blocks to create the slot.
				SlotMachine slot = new SlotMachine(plugin, name, type, owner, world, true, false, 0, 0);
				plugin.slotData.toggleCreatingSlots(player, slot);
				plugin.sendMessage(player, "Punch a block to serve as the base for this slot machine.");
			}
			
			// Slot exists
			else {
				plugin.sendMessage(player, "Slot machine " + args[1] +" already registered.");
			}
		}
		
		// incorrect command format
		else {
			plugin.sendMessage(player, "Usage:");
			plugin.sendMessage(player, "/casino addmanaged <name> <type>");
		}
		return true;

	}
}