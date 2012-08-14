package com.craftyn.casinoslots.util;

import org.bukkit.entity.Player;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.slot.SlotMachine;
import com.craftyn.casinoslots.slot.Type;

public class Permissions {
	
	protected CasinoSlots plugin;
	
	private String admin = "casinoslots.admin";
	private String create = "casinoslots.create";
	private String manage = "casinoslots.manage";
	private String use = "casinoslots.use";
	
	// Initialize permissions
	public Permissions(CasinoSlots plugin) {
		this.plugin = plugin;
	}
	
	/**
	 * Checks if has admin permissions.
	 *
	 * @param player The player who is attempting to create.
	 * @return       True if has permission; false if doesn't have permission.
	 */
	public Boolean isAdmin(Player player) {		
		if(player.isOp() || player.hasPermission(admin) || player.hasPermission(admin + ".*")) {
			return true;
		}
		return false;
	}
	
	/**
	 * Returns true if the player can create slots. It checks the following permissions:
	 * 
	 * <ul>
	 *  <li>{@link isAdmin}</li>
	 *  <li>casinoslots.create</li>
	 *  <li>casinoslots.create.*</li>
	 * </ul>
	 * 
	 * @param player The Player that is attempting to create a lot
	 * @param type   The Type of the slot that the Player is attempting to create
	 * @return       True if the player can create that slot, false if the player can't
	 */
	public Boolean canCreate(Player player) {			
		if(isAdmin(player) || player.hasPermission(create) || player.hasPermission(create + ".*")) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Returns true if the player can create a slot with the type.
	 * 
	 * The following are the permissions it checks:
	 *  - {@link isAdmin}
	 *  - casinoslots.create. type
	 *  - casinoslots.create.*
	 * 
	 * @param player The Player that is attempting to create a lot
	 * @param type   The Type of the slot that the Player is attempting to create
	 * @return       True if the player can create that slot, false if the player can't
	 */
	public Boolean canCreate(Player player, Type type) {		
		String name = type.getName();
		
		if(isAdmin(player) || player.hasPermission(create + "." + name) || player.hasPermission(create + ".*")) {
			return true;
		}
		return false;
	}
	
	/**
	 * Returns true if the player can create a slot with the type (type being a string).
	 * 
	 * @param player The Player that is attempting to create a lot
	 * @param type   The String Type of the slot that the Player is attempting to create
	 * @return       True if the player can create that slot, false if the player can't
	 */
	public Boolean canCreate(Player player, String type) {		
		if(isAdmin(player) || player.hasPermission(create + "." + type) || player.hasPermission(create + ".*")) {
			return true;
		}
		return false;
	}
	
	/**
	 * Returns true if the player can create a managed slot.
	 * 
	 * @param player The Player that is attempting to create a lot.
	 * @return       True if the player can create managed; false if the player can't.
	 */
	public Boolean canCreateManaged(Player player) {		
		if(isAdmin(player) || player.hasPermission(create + ".managed") || player.hasPermission(create + ".*")) {
			return true;
		}
		return false;
	}
	
	/**
	 * Returns true if the player can build a managed slot, false if not.
	 * 
	 * The player must either have:
	 * <ul>
	 * 	<li>{@link isAdmin}</li>
	 *  <li>casinoslots.create.managed.{@value type}</li>
	 *  <li>casinoslots.create.managed.*</li>
	 *  <li>casinoslots.create.*</li>
	 *  <li>casinoslots.create</li>
	 * 
	 * @param player
	 * @param type
	 * @return
	 */
	public Boolean canCreateManaged(Player player, String type) {		
		if(isAdmin(player) || player.hasPermission(create + ".managed." + type)|| player.hasPermission(create + ".managed.*") || player.hasPermission(create + ".*") || player.hasPermission(create)) {
			return true;
		}
		return false;
	}
	
	// Returns true if the player can manage slot machines
	public Boolean canManage(Player player) {		
		if(isAdmin(player) || player.hasPermission(manage)) {
			return true;
		}
		return false;
	}
	
	// Returns true if the player can use the type
	public Boolean canUse(Player player, Type type) {		
		String name = type.getName();
		if(isAdmin(player) || player.hasPermission(use + "." + name) || player.hasPermission(use + ".*") || player.hasPermission(use)) {
			return true;
		}
		return false;
	}
	
	public Boolean isOwner(Player player, SlotMachine slot) {		
		if(isAdmin(player) || slot.getOwner().equalsIgnoreCase(player.getName())) {
			return true;
		}
		return false;
	}
}