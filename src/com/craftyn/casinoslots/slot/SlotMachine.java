package com.craftyn.casinoslots.slot;

import java.util.ArrayList;

import org.bukkit.Material;
import org.bukkit.block.Block;

public class SlotMachine {
	
	private String name, type, owner, world;
	private Boolean managed, busy = false, enabled = true, item;
	private int itemID, itemAMT;
	private ArrayList<Block> blocks;
	private Block controller;
	private Double funds;
	
	// Complete slot machine constructor
	public SlotMachine(String name, String type, String owner, String world, Boolean managed, ArrayList<Block> blocks, Block controller, Double funds, int itemID, int itemAmount) {
		
		this.name = name;
		this.type = type;
		this.owner = owner;
		this.world = world;
		this.managed = managed;
		this.blocks = blocks;
		this.controller = controller;
		this.funds = funds;
		
	}
	
	// New slot machine constructor
	public SlotMachine(String name, String type, String owner, String world, Boolean managed, Boolean item, int itemId, int itemAmt) {
		
		this.name = name;
		this.type = type;
		this.owner = owner;
		this.world = world;
		this.managed = managed;
		this.item = item;
		this.itemID = itemId;
		this.itemAMT = itemAmt;
		this.funds = 0.0;
		
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
	
	// Sets reel blocks
	public void setBlocks(ArrayList<Block> blocks) {
		this.blocks = blocks;
	}
	
	// Sets controller block
	public void setController(Block controller) {
		this.controller = controller;
		controller.setType(Material.NOTE_BLOCK);
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