package com.craftyn.casinoslots.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Set;

import com.craftyn.casinoslots.CasinoSlots;


public class StatData {
	
	protected CasinoSlots plugin;
	private HashMap<String, Stat> stats = new HashMap<String, Stat>();
	public Integer globalSpins, globalWins;
	public Double globalWon, globalLost;
	
	// Initialize StatData
	public StatData(CasinoSlots plugin) {
		this.plugin = plugin;
	}
	
	// Returns collection of all stats
	public Collection<Stat> getStats() {
		if(!this.stats.isEmpty()) {
			return this.stats.values();
		}
		else return null;
	}
	
	// Returns stats of a type
	public Stat getStat(String type) {
		return this.stats.get(type);
	}
	
	// Check if stat exists
	public Boolean isStat(String type) {
		return stats.containsKey(type);
	}
	
	// Adds a stat
	public void addStat(Stat stat) {
		
		String type = stat.getType();
		Double won = stat.getWon();
		Double lost = stat.getLost();
		
		if(plugin.configData.inDebug()) plugin.debug("Adding a new stat for " + type + ":");
		if(plugin.configData.inDebug()) plugin.debug("   type: " + type);
		if(plugin.configData.inDebug()) plugin.debug("   spins: " + stat.getSpins());
		if(plugin.configData.inDebug()) plugin.debug("   won: " + won);
		if(plugin.configData.inDebug()) plugin.debug("   lost: " + lost);
		
		this.stats.put(type, stat);
		
		this.globalSpins += 1;
		this.globalWon += won;
		this.globalLost += lost;
		
	}
	
	// Loads a stat
	private void loadStat(String type) {
		
		String path = "types." + type +".";
		
		Integer spins = plugin.configData.stats.getInt(path + "spins");
		Double won = plugin.configData.stats.getDouble(path + "won");
		Double lost = plugin.configData.stats.getDouble(path + "lost");
		
		this.globalSpins += spins;
		this.globalWon += won;
		this.globalLost += lost;
		
		Stat stat = new Stat(type, spins, won, lost);
		if(plugin.configData.inDebug()) plugin.debug("We added a stat for " + type + ":");
		if(plugin.configData.inDebug()) plugin.debug("   type: " + type);
		if(plugin.configData.inDebug()) plugin.debug("   spins: " + spins);
		if(plugin.configData.inDebug()) plugin.debug("   won: " + won);
		if(plugin.configData.inDebug()) plugin.debug("   lost: " + lost);
		
		this.stats.put(type, stat);
	}
	
	// Load all stats
	public void loadStats() {
		
		if(plugin.configData.trackStats) {
			
			this.stats = new HashMap<String, Stat>();
			
			this.globalSpins = 0;
			this.globalWon = 0.0;
			this.globalLost = 0.0;
			Integer i = 0;
			
			if(plugin.configData.stats.isConfigurationSection("types")) {
				Set<String> types = plugin.configData.stats.getConfigurationSection("types").getKeys(false);
				for(String type : types) {
					loadStat(type);
					i++;
				}
			}
			plugin.log("Loaded statistics for " + i + " types.");
			if(plugin.configData.inDebug()) plugin.debug("The global is: ");
			if(plugin.configData.inDebug()) plugin.debug("   spins: " + globalSpins);
			if(plugin.configData.inDebug()) plugin.debug("   won: " + globalWon);
			if(plugin.configData.inDebug()) plugin.debug("   lost: " + globalLost);
			
		}else {
			plugin.log("Not tracking statistics.");
		}
	}
	
	

}