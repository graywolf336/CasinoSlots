package com.craftyn.casinoslots.util;

public class Stat {
	
	private String type;
	private Integer spins, wins, losts;
	private Double won, lost;
	
	// Initialize new stat object
	public Stat(String type, Integer spins, Integer wins, Integer losts, Double won, Double lost) {
		
		this.type = type;
		this.spins = spins;
		this.wins = wins;
		this.losts = losts;
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
	
	public Integer getWins() {
		return this.wins;
	}
	
	public Integer getLosts() {
		return this.losts;
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
	public void addWon(Double won, Double lost) {
		
		this.spins += 1;
		this.wins += 1;
		this.won += won;
		this.lost += lost;
		
	}
	
	// Update stat with new values
	public void addLost(Double won, Double lost) {
		
		this.spins += 1;
		this.losts += 1;
		this.won += won;
		this.lost += lost;
		
	}

}