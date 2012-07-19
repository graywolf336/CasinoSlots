package com.craftyn.casinoslots.listeners;

import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkUnloadEvent;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.slot.SlotMachine;

public class AnChunkListener implements Listener {

	protected CasinoSlots plugin;
	
	public AnChunkListener(CasinoSlots plugin) {
		this.plugin = plugin;
	}
	
	@EventHandler
	public void onChunkUnload(ChunkUnloadEvent event) {
		String w = event.getWorld().getName();
	    Chunk c = event.getChunk();
	    int x = c.getX();
	    int z = c.getZ();
	    
	    for(SlotMachine slot : plugin.slotData.getSlots()) {
	    	String chunk = slot.getChunkXZ();
	    	String[] sC = chunk.split("\\,");
	    	int sX = Integer.parseInt(sC[0]);
	    	int sZ = Integer.parseInt(sC[1]);
	    	String world = slot.getWorld();
	    	
	    	if (w == world) {
	    		if (x == sX && z == sZ) {
	    			event.setCancelled(true);
	    			plugin.log("Stopped unloading of chunk at: " + x + ", " + z);
	    		}else {
	    			return;
	    		}
	    	}else {
	    		return;
	    	}
	    	
	    }
	}
}
