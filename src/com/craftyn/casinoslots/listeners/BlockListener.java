package com.craftyn.casinoslots.listeners;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.slot.SlotMachine;

public class BlockListener implements Listener {
	
	protected CasinoSlots plugin;
	
	public BlockListener(CasinoSlots plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onBlockBreak(BlockBreakEvent event) {
		if(event.isCancelled()) return;
		
		// Check if plugin is enabled
		if(plugin.isEnabled()) {
			
			// Slot protection enabled
			if(plugin.configData.protection) {
				
				Block b = event.getBlock();
				
				// Look for match in slots
				for(SlotMachine slot : plugin.slotData.getSlots()) {
					
					for(Block current : slot.getBlocks()) {
						
						// Match found, cancel event
						if(b.equals(current)) {
							event.setCancelled(true);
							return;
						}
					}
					
					// Check controller as well
					Block current = slot.getController();
					if(b.equals(current)) {
						event.setCancelled(true);
						return;
					}
				}
			}
		}
	}

}