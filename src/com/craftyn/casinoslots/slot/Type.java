package com.craftyn.casinoslots.slot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.craftyn.casinoslots.CasinoSlots;

public class Type {
	
	protected CasinoSlots plugin;
	
	private String name;
	private Double cost, createCost;
	private ArrayList<String> reel;
	private Map<String, String> messages;
	private List<String> helpMessages;
	private Map<Integer, Reward> rewards;
	
	// Initialize new type
	public Type(String name, Double cost, Double createCost, ArrayList<String> reel, Map<String, String> messages, List<String> helpMessages, Map<Integer, Reward> rewards) {
		
		this.name = name;
		this.cost = cost;
		this.createCost = createCost;
		this.reel = reel;
		this.messages = messages;
		this.helpMessages = helpMessages;
		this.rewards = rewards;
		
	}
	
	// Returns type name
	public String getName() {
		return this.name;
	}
	
	// Returns type use cost
	public Double getCost() {
		return this.cost;
	}
	
	public Double getCreateCost() {
		return this.createCost;
	}
	
	// Returns type reel
	public ArrayList<String> getReel() {
		return this.reel;
	}
	
	// Returns map of type messages
	public Map<String, String> getMessages() {
		return this.messages;
	}
	
	// Returns help messages
	public List<String> getHelpMessages() {
		return this.helpMessages;
	}
	
	// Returns type reward of id
	public Reward getReward(Integer id) {
		return this.rewards.get(id);
	}
	
	// Set type use cost
	public void setCost(Double cost) {
		this.cost = cost;
	}
	
	// Set type use cost
	public void setCreateCost(Double createCost) {
		this.createCost = createCost;
	}

}