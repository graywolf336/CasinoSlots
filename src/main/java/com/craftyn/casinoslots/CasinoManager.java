package com.craftyn.casinoslots;

public class CasinoManager {
	private CasinoSlots plugin;

	public CasinoManager(CasinoSlots plugin) {
		this.plugin = plugin;
	}
	
	public CasinoSlots getPlugin() {
		return this.plugin;
	}
}
