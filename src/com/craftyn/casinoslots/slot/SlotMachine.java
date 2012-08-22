package com.craftyn.casinoslots.slot;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class SlotMachine {
	
	private String name, type, owner, world, reelChunk, controllerChunk;
	private Boolean managed, busy = false, enabled = true, item;
	private int itemID, itemAMT;
	private ArrayList<Block> blocks;
	private Block controller, sign = null;
	private Double funds;
	
	/**
	 * Instantiates a new slot machine, usually from the config.
	 *
	 * @param name       		The name of the slot machine.
	 * @param type       		The type of the slot machine being created.
	 * @param owner      		The owner of the slot machine.
	 * @param world      		The world in which the slot machine exists.
	 * @param reelchunk			The chunk in which the controller block is in.
	 * @param controllerchunk	The chunk in which the controller block is in.
	 * @param managed    		If it is managed or not (true or false).
	 * @param blocks     		An ArrayList of the blocks of the slot machine.
	 * @param controller 		The block of the controller.
	 * @param funds      		Amount of money that the slot machine has.
	 * @param item       		If it is an item slot or not (true or false).
	 * @param itemID     		The item id that it accepts (should be set to 0 if false).
	 * @param itemAmount 		The amount of the item that it takes from the player.
	 */
	public SlotMachine(String name, String type, String owner, String world, String reelChunk, String controllerChunk, Block sign, Boolean managed, ArrayList<Block> blocks, Block controller, Double funds, Boolean item, int itemID, int itemAmount) {
		
		this.name = name;
		this.type = type;
		this.owner = owner;
		this.world = world;
		this.reelChunk = reelChunk;
		this.controllerChunk = controllerChunk;
		this.sign = sign;
		this.managed = managed;
		this.blocks = blocks;
		this.controller = controller;
		this.funds = funds;
		this.item = item;
		this.itemID = itemID;
		this.itemAMT = itemAmount;
	}
	
	/**
	 * Instantiates a new slot machine, usually from a command.
	 *
	 * @param name     The name of the slot machine as a string.
	 * @param type     The type of the slot machine being created as a string.
	 * @param owner    The owner of the slot machine as a string.
	 * @param world    The world in which the slot machine exists.
	 * @param managed  If it is managed or not (true or false).
	 * @param item     If it is an item slot or not (true or false).
	 * @param itemId   The item id that it accepts (should be set to 0 if false).
	 * @param itemAmt  The amount of the item that it takes from the player.
	 */
	public SlotMachine(String name, String type, String owner, String world, Boolean managed, Boolean item, int itemId, int itemAmt) {
		
		this.name = name;
		this.type = type;
		this.owner = owner;
		this.world = world;
		this.managed = managed;
		this.funds = 0.0;
		this.item = item;
		this.itemID = itemId;
		this.itemAMT = itemAmt;
		
		if(isManaged()) {
			enabled = false;
		}
	}
	
	// Returns name of slot machine
	public String getName() {
		return this.name;
	}
	
	// Returns type name of slot machine
	public String getType() {
		return this.type;
	}
	
	// Returns owner of slot machine
	public String getOwner() {
		return this.owner;
	}
	
	// Returns if slot machine is managed
	public Boolean isManaged() {
		return this.managed;
	}
	
	// Returns if slot machine is item machine, aka paid with items
	public Boolean isItem() {
		return this.item;
	}
	
	// Returns which item it costs
	public int getItem() {
		return this.itemID;
	}
	
	// Returns who many of the items it costs
	public int getItemAmount() {
		return this.itemAMT;
	}
	
	// Returns world name of slot machine
	public String getWorld() {
		return this.world;
	}
	
	// Check if slot is in use
	public Boolean isBusy() {
		return this.busy;
	}
	
	public Boolean isEnabled() {
		return this.enabled;
	}
	
	// Returns available funds
	public Double getFunds() {
		return this.funds;
	}
	
	// Returns blocks in reel
	public ArrayList<Block> getBlocks() {
		return this.blocks;
	}
	
	// Returns controller block
	public Block getController() {
		return this.controller;
	}
	
	/**
	 * Provides an easy way to get the chunk of one of the Reel blocks.
	 * 
	 * @return A string of the chunk of one of the Reel blocks in <em>x,z</em> format.
	 */
	public String getReelChunk() {
		return this.reelChunk;
	}
	
	/**
	 * Provides an easy way to get the chunk of the Controller Block.
	 * 
	 * @return A string of the chunk of the Controller block in <em>x,z</em> format.
	 */
	public String getControllerChunk() {
		return this.controllerChunk;
	}
	
	public Block getSign() {
		return this.sign;
	}
	
	// Sets reel blocks
	public void setBlocks(ArrayList<Block> blocks) {
		this.blocks = blocks;
	}
	
	// Sets controller block
	public void setController(Block c) {
		this.controller = c;		
		c.setType(Material.NOTE_BLOCK);
		setControllerChunk(c.getChunk().getX() + "," + c.getChunk().getZ());
	}
	
	/**
	 * Provides a way to set the chunk for the Reel block, used from SlotData.
	 * 
	 * @param chunk	The chunk x,z for the base reel block.
	 */
	public void setReelChunk(String chunk) {
		this.reelChunk = chunk;
	}
	
	/**
	 * Provides a way to set the chunk for the Controller block, used from setController.
	 * 
	 * @param chunk	The chunk x,z for the base reel block.
	 */
	public void setControllerChunk(String chunk) {
		this.controllerChunk = chunk;
	}
	
	public void setSign(Block sign) {
		this.sign = sign;
	}
	
	// Sets type
	public void setType(String type) {
		this.type = type;
	}
	
	// Sets managed
	public void setManaged(Boolean managed) {
		this.managed = managed;
	}
	
	// Sets owner
	public void setOwner(String owner) {
		this.owner = owner;
	}
	
	// Deposit the amount into the slot machine
	public void deposit(Double amount) {
		this.funds += amount;
	}
	
	// Withdraw the amount from the slot machine
	public void withdraw(Double amount) {
		this.funds -= amount;
	}
	
	// Set disabled state
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	
	// Set use status
	public void toggleBusy() {
		
		if(busy) {
			this.busy = false;
		}
		else {
			this.busy = true;
		}
	}
}