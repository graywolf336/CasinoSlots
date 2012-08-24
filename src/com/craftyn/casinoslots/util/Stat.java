package com.craftyn.casinoslots.util;

public class Stat {
	
	private String type;
	private Integer spins;
	private Double won, lost;
	
	// Initialize new stat object
	public Stat(String type, Integer spins, Double won, Double lost) {
		
		this.type = type;
		this.spins = spins;
		this.won = won;
		this.lost = lost;
		
	}
	
	// Returns type this stat belongs to
	public String getType() {
		return this.type;
	}
	
	// Returns spins
	public Integer getSpins() {
		return this.spins;
	}
	
	// Returns amount won
	public Double getWon() {
		return this.won;
	}
	
	// Returns amount lost
	public Double getLost() {
		return this.lost;
	}
	
	// Update stat with new values
	public void add(Double won, Double lost) {
		
		this.spins += 1;
		this.won += won;
		this.lost += lost;
		
	}

}