package com.craftyn.casinoslots.slot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import com.craftyn.casinoslots.CasinoSlots;

public class SlotData {
	
	private CasinoSlots plugin;
	private HashMap<String, SlotMachine> slots = new HashMap<String, SlotMachine>();
	
	public HashMap<Player, SlotMachine> creatingSlots = new HashMap<Player, SlotMachine>();
	public HashMap<Player, SlotMachine> placingController = new HashMap<Player, SlotMachine>();
	
	
	// Initialize SlotData
	public SlotData(CasinoSlots plugin) {
		this.plugin = plugin;
	}
	
	// Returns a slot machine
	public SlotMachine getSlot(String name) {
		return this.slots.get(name);
	}
	
	// Returns collection of all slot machines
	public Collection<SlotMachine> getSlots() {
		return this.slots.values();
	}
	
	// Registers a new slot machine
	public void addSlot(SlotMachine slot) {
		
		String name = slot.getName();
		this.slots.put(name, slot);
		
	}
	
	// Returns true if slot machine exists
	public Boolean isSlot(String name) {
		if(this.slots.containsKey(name)) {
			return true;
		}
		return false;
	}
	
	// Removes a slot machine
	public void removeSlot(SlotMachine slot) {
		
		this.slots.remove(slot.getName());
		for(Block b : slot.getBlocks()) {
			b.setTypeId(0);
		}
		slot.getController().setTypeId(0);
		plugin.configData.slots.set("slots." + slot.getName(), null);
		plugin.configData.saveSlots();
	}
	
	
	// Loads all slot machines into memory
	public void loadSlots() {
		
		Integer i = 0;
		this.slots = new HashMap<String, SlotMachine>();
		if(plugin.configData.slots.isConfigurationSection("slots")) {
			Set<String> slots = plugin.configData.slots.getConfigurationSection("slots").getKeys(false);
			if(!slots.isEmpty()) {
				for(String name : slots) {
					loadSlot(name);
					i++;
				}
			}
		}
		plugin.log("Loaded " + i + " slot machines.");
	}
	
	// Writes slot machine data to disk
	public void saveSlot(SlotMachine slot) {
		
		String path = "slots." + slot.getName() + ".";
		ArrayList<String> xyz = new ArrayList<String>();
		
		for(Block b : slot.getBlocks()) {
			xyz.add(b.getX() + "," + b.getY() + "," + b.getZ());
		}
		
		Block con = slot.getController();
		String cXyz = con.getX() + "," + con.getY() + "," + con.getZ();
		
		plugin.configData.slots.set(path + "name", slot.getName());
		plugin.configData.slots.set(path + "type", slot.getType());
		plugin.configData.slots.set(path + "owner", slot.getOwner());
		plugin.configData.slots.set(path + "world", slot.getWorld());
		plugin.configData.slots.set(path + "managed", slot.isManaged());
		plugin.configData.slots.set(path + "item", slot.isItem());
		plugin.configData.slots.set(path + "funds", slot.getFunds());
		plugin.configData.slots.set(path + "controller", cXyz);
		plugin.configData.slots.set(path + "location", xyz);
		
		
		plugin.configData.saveSlots();
	}
	
	// Loads a slot machine into memory
	private void loadSlot(String name) {
		
		String path = "slots." + name + ".";
		
		String type = plugin.configData.slots.getString(path + "type");
		String owner = plugin.configData.slots.getString(path + "owner");
		String world = plugin.configData.slots.getString(path + "world");
		Boolean managed = plugin.configData.slots.getBoolean(path + "managed");
		Double funds = plugin.configData.slots.getDouble(path + "funds");
		ArrayList<Block> blocks = getBlocks(name);
		Block controller = getController(name);
		
		SlotMachine slot = new SlotMachine(name, type, owner, world, managed, blocks, controller, funds);
		addSlot(slot);
	}
	
	// Gets reel blocks location from disk
	private ArrayList<Block> getBlocks(String name) {
		
		List<String> xyz = plugin.configData.slots.getStringList("slots." + name + ".location");
		ArrayList<Block> blocks = new ArrayList<Block>();
		World world = Bukkit.getWorld(plugin.configData.slots.getString("slots." + name + ".world", "world"));
		
		for(String coord : xyz) {
			String[] b = coord.split("\\,");
			Location loc = new Location(world, Integer.parseInt(b[0]), Integer.parseInt(b[1]), Integer.parseInt(b[2]));
			blocks.add(loc.getBlock());
		}
		
		return blocks;
	}
	
	// Gets controller block from disk
	private Block getController(String name) {
		
		String location = plugin.configData.slots.getString("slots." + name + ".controller");
		World world = Bukkit.getWorld(plugin.configData.slots.getString("slots." + name + ".world"));
		String[] b = location.split("\\,");
		Location loc = new Location(world, Integer.parseInt(b[0]), Integer.parseInt(b[1]), Integer.parseInt(b[2]));
		
		Block controller = loc.getBlock();
		return controller;
		
	}
	
	// Creates the slot machine in the world
	public void createReel(Player player, BlockFace face, SlotMachine slot) {
		
		Block center = player.getTargetBlock(null, 0);
		ArrayList<Block> blocks = new ArrayList<Block>();
		
		for(int i = 0; i < 3; i++) {
			blocks.add(center.getRelative(getDirection(face, "left"), 2));
			blocks.add(center);
			blocks.add(center.getRelative(getDirection(face, "right"), 2));
			center = center.getRelative(BlockFace.UP, 1);
		}
		
		for(Block b : blocks) {
			b.setTypeId(57);
		}
		
		slot.setBlocks(blocks);
		
	}
	
	// Used for orienting the slot machine correctly
	public BlockFace getDirection(BlockFace face, String direction) {
		
		if(face == BlockFace.NORTH) {
			if(direction.equalsIgnoreCase("left")) {
				return BlockFace.EAST;
			}
			else if(direction.equalsIgnoreCase("right")) {
				return BlockFace.WEST;
			}
		}
		else if(face == BlockFace.SOUTH) {
			if(direction.equalsIgnoreCase("left")) {
				return BlockFace.WEST;
			}
			else if(direction.equalsIgnoreCase("right")) {
				return BlockFace.EAST;
			}
		}
		else if(face == BlockFace.WEST) {
			if(direction.equalsIgnoreCase("left")) {
				return BlockFace.SOUTH;
			}
			else if(direction.equalsIgnoreCase("right")) {
				return BlockFace.NORTH;
			}
		}
		else if(face == BlockFace.EAST) {
			if(direction.equalsIgnoreCase("left")) {
				return BlockFace.NORTH;
			}
			else if(direction.equalsIgnoreCase("right")) {
				return BlockFace.SOUTH;
			}
		}
		return BlockFace.SELF;
		
	}
	
	// If a player is creating slot machine
	public Boolean isCreatingSlots(Player player) {
		
		if(creatingSlots.containsKey(player)) {
			return true;
		}
		
		return false;
	}
	
	// If a player is placing controller
	public Boolean isPlacingController(Player player) {
		
		if(placingController.containsKey(player)) {
			return true;
		}
		
		return false;
	}
	
	// Toggles creating slots
	public void toggleCreatingSlots(Player player, SlotMachine slot) {
		
		if(this.creatingSlots.containsKey(player)) {
			this.creatingSlots.remove(player);
		}
		else {
			this.creatingSlots.put(player, slot);
		}
	}
	
	// Toggles placing controller
	public void togglePlacingController(Player player, SlotMachine slot) {
		
		if(this.placingController.containsKey(player)) {
			this.placingController.remove(player);
		}
		else {
			this.placingController.put(player, slot);
		}
	}

}