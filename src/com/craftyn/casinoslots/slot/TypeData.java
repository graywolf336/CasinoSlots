package com.craftyn.casinoslots.slot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import com.craftyn.casinoslots.CasinoSlots;

public class TypeData {
	
	protected CasinoSlots plugin;
	private HashMap<String, Type> types;
	private final Logger logger = Logger.getLogger("Minecraft");
	
	// Initialize TypeData
	public TypeData(CasinoSlots plugin) {
		this.plugin = plugin;
	}
	
	// Returns a type
	public Type getType(String name) {
		return types.get(name);
	}
	
	// Returns collection of all types
	public Collection<Type> getTypes() {
		return this.types.values();
	}
	
	// Registers a new type
	public void addType(Type type) {
		String name = type.getName();
		types.put(name, type);
		
		plugin.configData.config.set("types." + type.getName() +".cost", type.getCost());
		plugin.configData.config.set("types." + type.getName() +".create-cost", type.getCreateCost());
		plugin.saveConfig();
	}
	
	public void removeType(String type) {		
		types.remove(type);
		plugin.configData.config.set("types." + type, null);
		plugin.saveConfig();		
	}
	
	/**
	 * Provides a way to refresh the types information.
	 */
	public void reloadTypes() {
		for(Type t : getTypes()) {
			t.clearType();
			this.types.remove(t.getName());
		}
		
		plugin.log("There are " + types.size() + " types in the hashmap still.");
		plugin.log(types.toString());
		loadTypes();
	}
	
	// Check if a type exists
	public Boolean isType(String type) {
		if(types.containsKey(type)) {
			return true;
		}
		return false;
	}
	
	// Load all types into memory
	public void loadTypes() {		
		this.types = new HashMap<String, Type>();
		Integer i = 0;
		
		if(plugin.configData.config.isConfigurationSection("types")) {
			Set<String> types = plugin.configData.config.getConfigurationSection("types").getKeys(false);
			if(!types.isEmpty()) {
				for(String name : types) {
					if (!plugin.configData.config.contains("types." + name + ".messages")) {
						plugin.log("Please make sure your slots in the config file contains 'messages:'.");
						//If there is no "messages", disables the plugin and forces them to check their config
						logger.severe("[CasinoSlots]" + " PLEASE CHECK ONE OF YOUR CONFIG FILES");
						plugin.disablePlugin();
						return;
					}else {
						loadType(name);
						i++;
					}
				}
			}
		}
		plugin.log("Loaded " + i + " types.");
	}
	
	// Load type into memory
	private void loadType(String name) {		
		String path = "types." + name +".";
		
		Double cost = plugin.configData.config.getDouble(path + "cost");
		Double createCost = plugin.configData.config.getDouble(path + "create-cost");
		ArrayList<String> reel = getReel(name);
		
		Map<String, String> messages = getMessages(name);
		List<String> helpMessages = plugin.configData.config.getStringList(path + "messages.help");
		Map<String, Reward> rewards = getRewards(name);
		
		Type type = new Type(name, cost, createCost, reel, messages, helpMessages, rewards);
		this.types.put(name, type);
	}
	
	// Returns the parsed reel of a type
	private ArrayList<String> getReel(String type) {		
		List<String> reel = plugin.configData.config.getStringList("types." + type + ".reel");
		
		ArrayList<String> parsedReel = new ArrayList<String>();
		for(String m : reel) {
			String[] mSplit = m.split("\\,");
			int i = 1;
			if (mSplit.length == 3) {
				i = Integer.parseInt(mSplit[2]);
			}else {
				i = Integer.parseInt(mSplit[1]);
			}
			
			while(i > 0) {
				if (mSplit.length == 3) {
					parsedReel.add(mSplit[0] + ":" + mSplit[1]);
				}else {				
					parsedReel.add(mSplit[0]);
				}
				i--;
			}
		}
		return parsedReel;
	}
	
	// Returns reward of id
	public Reward getReward(String type, String id) {
		// Split the id so that a damage value is optional
		String blockId = String.valueOf(0);
		String[] idSplit = id.split("\\,");
			if (Integer.parseInt(idSplit[1]) == 0) {
				blockId = idSplit[0];
			}else {
				blockId = id;
			}
		
		String path = "types." + type + ".rewards." + blockId + ".";
		
		String message = plugin.configData.config.getString(path + "message", "Award given!");
		Double money = plugin.configData.config.getDouble(path + "money", 0.0);
		List<String> action = null;
		
		if(plugin.configData.config.isSet(path + "action")) {
			if(plugin.configData.config.isList(path + "action")) {
				action = plugin.configData.config.getStringList(path + "action");
			}
			else {
				String a = plugin.configData.config.getString(path + "action");
				action = Arrays.asList(a);
			}
		}
		
		Reward reward = new Reward(message, money, action);
		return reward;		
	}
	
	// Rreturns Map of all rewards for this type
	public Map<String, Reward> getRewards(String type) {
		Set<String> ids = plugin.configData.config.getConfigurationSection("types." + type +".rewards").getKeys(false);
		Map<String, Reward> rewards = new HashMap<String, Reward>();
		
		for(String itemId : ids) {
			int id = 1; //setting this to 1 just in case something is wrong
			byte data = 0;
			String[] itemSplit = itemId.split("\\,");
			if (itemSplit.length == 2) {
				id = Integer.parseInt(itemSplit[0]);
				data = Byte.parseByte(itemSplit[1]);
			}else {
				id = Integer.parseInt(itemSplit[0]);
			}
			
			Reward reward = getReward(type, id + "," + data);
			rewards.put(id + ":" + data, reward);
		}		
		return rewards;
	}
	
	// Returns map of messages
	private HashMap<String, String> getMessages(String type) {		
		HashMap<String, String> messages = new HashMap<String, String>();
		Double cost = plugin.configData.config.getDouble("types." + type +".cost");

		messages.put("noPermission", plugin.configData.config.getString("types." + type +".messages.insufficient-permission", "You don't have permission to use this slot."));
		messages.put("noFunds", plugin.configData.config.getString("types." + type +".messages.insufficient-funds", "You can't afford to use this."));
		messages.put("inUse", plugin.configData.config.getString("types." + type +".messages.in-use", "This slot machine is already in use."));
		messages.put("noWin", plugin.configData.config.getString("types." + type +".messages.no-win", "No luck this time."));
		messages.put("start", plugin.configData.config.getString("types." + type +".messages.start", "[cost] removed from your account. Lets roll!"));
		
		// Parse shortcodes
		for(Map.Entry<String, String> entry : messages.entrySet()) {
			String message = entry.getValue();
			String key = entry.getKey();
			message = message.replaceAll("\\[cost\\]", "" + cost);
			messages.put(key, message);
		}		
		return messages;
	}
	
	// Returns value of the highest money reward
	public Double getMaxPrize(String type) {		
		Map<String, Reward> rewards = getRewards(type);
		Double max = 0.0;
		
		for(Map.Entry<String, Reward> entry : rewards.entrySet()) {
			Reward reward = entry.getValue();
			Double money = reward.getMoney();
			if(money > max) {
				max = money;
			}
		}
		return max;
	}
	
	public void newType(String name) {
		String path = "types." + name + ".";
		List<String> reel = Arrays.asList("42,10", "41,5", "57,2");
		List<String> help = Arrays.asList("Instructions:", "Get 3 in a row to win.", "3 iron blocks: $250", "3 gold blocks: $500", "3 diamond blocks: $1200");
		
		plugin.configData.config.set(path + "cost", 100);
		plugin.configData.config.set(path + "create-cost", 1000);
		plugin.configData.config.set(path + "reel", reel);
		
		path = path + "rewards.";
		
		plugin.configData.config.set(path + "42.message", "Winner!");
		plugin.configData.config.set(path + "42.money", 250);
		plugin.configData.config.set(path + "41.message", "Winner!");
		plugin.configData.config.set(path + "41.money", 500);
		plugin.configData.config.set(path + "57.message", "Winner!");
		plugin.configData.config.set(path + "57.money", 1200);
		
		path = "types." + name + ".messages.";
		
		plugin.configData.config.set(path + "insufficient-funds", "You can't afford to use this.");
		plugin.configData.config.set(path + "in-use", "This slot machine is already in use.");
		plugin.configData.config.set(path + "no-win", "No luck this time.");
		plugin.configData.config.set(path + "start", "[cost] removed from your account. Let's roll!");
		plugin.configData.config.set(path + "help", help);
		
		plugin.saveConfig();
		loadType(name);
	}
}