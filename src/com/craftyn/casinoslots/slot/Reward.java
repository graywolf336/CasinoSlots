package com.craftyn.casinoslots.slot;

import java.util.List;

public class Reward {
	
	protected String message;
	protected Double money;
	protected List<String> action;
	
	// Initializes a Reward object
	public Reward(String message, Double money, List<String> action) {
		
		this.message = message;
		this.money = money;
		this.action = action;
	}
	
	// Returns reward message
	public String getMessage() {
		return this.message;
	}
	
	// Returns reward money
	public Double getMoney() {
		return this.money;
	}
	
	// Returns reward actions
	public List<String> getAction() {
		return this.action;
	}
}