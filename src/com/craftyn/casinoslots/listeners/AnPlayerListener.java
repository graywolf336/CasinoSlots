package com.craftyn.casinoslots.listeners;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.slot.SlotMachine;
import com.craftyn.casinoslots.slot.Type;
import com.craftyn.casinoslots.slot.game.Game;

public class AnPlayerListener implements Listener {
	
	protected CasinoSlots plugin;
	
	public AnPlayerListener(CasinoSlots plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onPlayerInteract(PlayerInteractEvent event) {
		if(event.isCancelled()) return;
		
		// Check if plugin is enabled
		if(plugin.isEnabled()) {
			Player player = event.getPlayer();
			Block b = event.getClickedBlock();
			if(b == null) return;
				
			// Left clicked note block
			if(b.getType() == Material.NOTE_BLOCK) {
					
				// Look for matching controller block
				for(SlotMachine slot : plugin.slotData.getSlots()) {
					
					// Match found
					if(b.equals(slot.getController())) {
						Type type = plugin.typeData.getType(slot.getType());
						Double cost = type.getCost();
						
						// Left click event
						if(event.getAction() == Action.LEFT_CLICK_BLOCK) {
							
							//  Player has permission
							if(plugin.permission.canUse(player, type)) {
								
								// Player has enough money
								if(plugin.economy.has(player.getName(), cost)) {
									
									// Slot is not busy
									if(!slot.isBusy()) {
										
										//Let's go!
										Game game = new Game(slot, player, plugin);
										game.play();
										return;										
									}
									
									// Slot is busy
									else {
										plugin.sendMessage(player, type.getMessages().get("inUse"));
									}
								}
								
								// Player does not have enough money
								else {
									plugin.sendMessage(player, type.getMessages().get("noFunds"));
								}
							}		
							
							// Player does not have type permission
							else {
								plugin.sendMessage(player, type.getMessages().get("noPermission"));
							}
						}// End Left click
						
						// Right click event
						else if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
							if(plugin.permission.isOwner(player, slot)) {
								if (slot.isManaged()) {
									if(slot.getFunds() >= plugin.typeData.getMaxPrize(slot.getType())) {
										slot.setEnabled(true);
									}else {
										slot.setEnabled(false);
									}
								}
								plugin.sendMessage(player, slot.getName() +":");
								plugin.sendMessage(player, "    Type: " + slot.getType());
								plugin.sendMessage(player, "    Owner: " + slot.getOwner());
								plugin.sendMessage(player, "    Managed: " + slot.isManaged().toString());
								if(slot.isManaged()) {
									if (slot.isEnabled()) {
										plugin.sendMessage(player, "    Enabled: " + slot.isEnabled().toString());
									}else {
										plugin.sendMessage(player, "    Enabled: " + ChatColor.RED + slot.isEnabled().toString());
									}
									plugin.sendMessage(player, "    Funds: " + slot.getFunds() + " " + plugin.economy.currencyNamePlural());
									plugin.sendMessage(player, "    Funds required: " + plugin.typeData.getMaxPrize(slot.getType()));
								}
							}
						}
						
						// Match found
						break;
					}
				}
			}
			
			// Creating slots
			if(event.getAction() == Action.LEFT_CLICK_BLOCK && plugin.slotData.isCreatingSlots(player)) {				
				BlockFace face = event.getBlockFace();
				
				if(face != BlockFace.DOWN && face != BlockFace.UP) {
					SlotMachine slot = plugin.slotData.creatingSlots.get(player);
					plugin.slotData.createReel(player, face, slot);
					
					plugin.slotData.toggleCreatingSlots(player, slot);
					plugin.slotData.togglePlacingController(player, slot);
					plugin.sendMessage(player, "Punch a block to serve as the controller for this slot machine.");
				}
				else {
					plugin.sendMessage(player, "Only sides of blocks are valid targets for this operation.");
				}
			}
			
			// Placing controller
			else if(event.getAction() == Action.LEFT_CLICK_BLOCK && plugin.slotData.isPlacingController(player)) {
				
				SlotMachine slot = plugin.slotData.placingController.get(player);
				slot.setController(b);
				plugin.slotData.togglePlacingController(player, slot);
				plugin.slotData.addSlot(slot);
				plugin.slotData.saveSlot(slot);
				plugin.sendMessage(player, "Slot machine set up successfully!");
			}
		}
	}
}