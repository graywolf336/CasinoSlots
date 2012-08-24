package com.craftyn.casinoslots.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.slot.SlotMachine;

public class ConfigData {
	
	protected CasinoSlots plugin;
	
	public Configuration config;
	public FileConfiguration slots;
	public FileConfiguration stats;
	
	private File configFile;
	private File slotsFile;
	private File statsFile;
	
	public String prefixColor, chatColor, prefix;
	public Boolean displayPrefix, trackStats, allowDiagonals, protection, debug;
	
	// Initialize ConfigData
	public ConfigData(CasinoSlots plugin) {
		this.plugin = plugin;
	}
	
	// Load all config data
	public void load() {
		config = plugin.getConfig().getRoot();
		config.options().copyDefaults(true);
		
		setGlobals();
		
		statsFile = new File(plugin.getDataFolder(), "stats.yml");
		stats = YamlConfiguration.loadConfiguration(statsFile);
		
		slotsFile = new File(plugin.getDataFolder(), "slots.yml");
		slots = YamlConfiguration.loadConfiguration(slotsFile);
		
		plugin.typeData.loadTypes();
		plugin.slotData.loadSlots();
		plugin.statsData.loadStats();
		
		//if (firstload) save();
	}
	
	// Save all config data
	//public void save() {
	//	plugin.saveConfig();
	//	saveSlots();
	//	saveStats();
	//}
	
	/**
	 * Reload all the configs from disk.
	 * 
	 * This method reloads the config.yml, sets the slots config to null and then loads it again, and sets the stats file to null and then loads it again.
	 */
	public void reloadConfigs() {
		plugin.log("Reloading the configs.");
		
		configFile = new File(plugin.getDataFolder(), "config.yml");
		config = YamlConfiguration.loadConfiguration(configFile);
		this.debug = config.getBoolean("options.debug");
		
		//Set it to null, then reload it
		slots = null;
		slots = YamlConfiguration.loadConfiguration(slotsFile);
		
		//Set it to null, then reload it.
		stats = null;
		stats = YamlConfiguration.loadConfiguration(statsFile);
	}
	
	// Set up global settings
	private void setGlobals() {
		this.prefixColor = config.getString("options.chat.plugin-prefix-color", "&c");
		this.prefix = config.getString("options.chat.plugin-prefix", "[Casino]");
		this.chatColor = config.getString("options.chat.chat-color", "&a");
		this.displayPrefix = config.getBoolean("options.chat.display-plugin-prefix", true);
		
		this.debug = config.getBoolean("options.debug", false);
		this.trackStats = config.getBoolean("options.track-statistics", true);
		this.allowDiagonals = config.getBoolean("options.allow-diagonal-winnings", false);
		this.protection = config.getBoolean("options.enable-slot-protection", true);
	}
	
	// Save slots data
	public void saveSlots() {
		Collection<SlotMachine> slots = plugin.slotData.getSlots();
		
		if(slots != null && !slots.isEmpty()) {
			for (SlotMachine slot : slots) {
				String path = "slots." + slot.getName() + ".";
				this.slots.set(path + "name", slot.getName());
				this.slots.set(path + "type", slot.getType());
				this.slots.set(path + "owner", slot.getOwner());
				this.slots.set(path + "world", slot.getWorld());
				this.slots.set(path + "managed", slot.isManaged());
				this.slots.set(path + "funds", slot.getFunds());
			}
		}
		
		try {
				if(debug) plugin.debug("Saving the slots.yml.");
			this.slots.save(slotsFile);
		} catch (IOException e) {
			plugin.severe("There was a problem saving your slots.yml file.");
				if(debug) e.printStackTrace();
		}
	}
	
	// Save stats data
	public void saveStats() {
		Collection<Stat> stats = plugin.statsData.getStats();
		if(stats != null && !stats.isEmpty()) {
			for(Stat stat : stats) {
				String path = "types." + stat.getType() +".";
				this.stats.set(path + "spins", stat.getSpins());
				this.stats.set(path + "won", stat.getLost());
				this.stats.set(path + "lost", stat.getLost());
			}
		}
		
		this.stats.set("global.spins", plugin.statsData.globalSpins);
		this.stats.set("global.won", plugin.statsData.globalWon);
		this.stats.set("global.lost", plugin.statsData.globalLost);
		
		try {
				if(debug) plugin.debug("Saving the stats.yml.");
			this.stats.save(statsFile);
		} catch (IOException e) {
			plugin.severe("There was a problem saving your stats.yml file.");
				if(debug) e.printStackTrace();
		}
	}
	
	public boolean inDebug() {
		return this.debug;
	}
	
	/**
	 * For the command to add a new type, the parameter it takes is for the name of the new type.
	 * @param name
	 */
	public void setTypeDefaults(String name) {
		config.set("types."+ name +".cost", 100);
		config.set("types."+ name +".create-cost", 1000);
		
		ArrayList<String> reel = new ArrayList<String>();
		reel.add("42,10");
		reel.add("41,5");
		reel.add("57,2");
		config.set("types."+ name +".reel", reel);
		
		config.set("types."+ name +".rewards.42.message", "Winner");
		config.set("types."+ name +".rewards.42.money", 100);
		config.set("types."+ name +".rewards.41.message", "Winner");
		config.set("types."+ name +".rewards.41.money", 150);
		config.set("types."+ name +".rewards.57.message", "Winner");
		config.set("types."+ name +".rewards.57.money", 300);
		
		config.set("types."+ name +".messages.insufficient-funds", "Insufficient funds.");
		config.set("types."+ name +".messages.in-use", "In use.");
		config.set("types."+ name +".messages.no-win", "You didn't win.");
		config.set("types."+ name +".messages.start", "Start.");
		config.set("types."+ name +".messages.help", new ArrayList<String>());
		
		plugin.saveConfig();
	}
}