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
	    	String reelChunk = slot.getReelChunk();
	    	String conChunk = slot.getControllerChunk();
	    	
	    	//Split the reel chunk up
	    	String[] rChunk = reelChunk.split("\\,");
	    	int rX = Integer.parseInt(rChunk[0]);
	    	int rZ = Integer.parseInt(rChunk[1]);
	    	
	    	//Split the controller chunk up
	    	String[] cChunk = conChunk.split("\\,");
	    	int cX = Integer.parseInt(cChunk[0]);
	    	int cZ = Integer.parseInt(cChunk[1]);
	    	
	    	String world = slot.getWorld();
	    	
	    	if (w.equalsIgnoreCase(world)) {
	    		if (x == rX && z == rZ) {
	    			event.setCancelled(true);
	    			plugin.log("Kept chunk: (" + x + ", " + z + " " + world + ") for CasinoSlot - " + slot.getName() + " - loaded.");
	    		}else if (x == cX && z == cZ) {
	    			event.setCancelled(true);
	    			plugin.log("Kept chunk: (" + x + ", " + z + " " + world + ") for CasinoSlot - " + slot.getName() + " - loaded.");
	    		}else {
	    			continue;
	    		}
	    	}else {
	    		continue;
	    	}
	    }
	}
}
