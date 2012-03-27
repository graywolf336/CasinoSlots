package com.craftyn.casinoslots.command;

import org.bukkit.entity.Player;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.slot.SlotMachine;

public class CasinoAddManaged extends AnCommand {
	
	private String name;
	private String type;
	private String owner;
	private String world;
	
	// Command for adding unmanaged slot machine
	public CasinoAddManaged(CasinoSlots plugin, String[] args, Player player) {
		super(plugin, args, player);
	}
	
	public Boolean process() {
		
		// Permissions
		if(!plugin.permission.canCreate(player) || !plugin.permission.canManage(player)) {
			noPermission();
			return true;
		}
		
		// Valid command format
		if(args.length >= 2 && args.length <= 3) {
						
			// Slot does not exist
			if(!plugin.slotData.isSlot(args[1])) {
							
				this.name = args[1];
								
					// Valid type
				if(args.length < 3) {
					
					this.type = "default";
				}
				
				else if(plugin.typeData.isType(args[2])) {
					String typeName = args[2];
					
					// Has type permission
					if(plugin.permission.canCreate(player, typeName)) {
						this.type = typeName;
						this.owner = player.getName();
					}
					else {
						plugin.sendMessage(player, "Invalid type " + typeName);
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
				}
				else {
					sendMessage("You can't afford to create this slot machine. Cost: " + createCost);
					return true;
				}
				
				// Good to go
				this.world = player.getWorld().getName();
				SlotMachine slot = new SlotMachine(name, type, owner, world, true);
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
			plugin.sendMessage(player, "/casino addmanaged <name> (<type>)");
		}
		return true;

	}
}