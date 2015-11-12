package com.craftyn.casinoslots.util;

import org.bukkit.entity.Player;

import com.craftyn.casinoslots.classes.SlotMachine;
import com.craftyn.casinoslots.classes.Type;

public class PermissionUtil {
    private static String admin = "casinoslots.admin";
    private static String create = "casinoslots.create";
    private static String use = "casinoslots.use";

    /**
     * Checks if has admin permissions.
     *
     * @param player The player who is attempting to create.
     * @return       True if has permission; false if doesn't have permission.
     */
    public static boolean isAdmin(Player player) {
        return (player.isOp() || player.hasPermission(admin) || player.hasPermission(admin + ".*"));
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
     * @return       True if the player can create that slot, false if the player can't
     */
    public static boolean canCreate(Player player) {
        return (isAdmin(player) || player.hasPermission(create) || player.hasPermission(create + ".*"));
    }

    /**
     * Returns true if the player can create a slot with the type.
     * 
     * The following are the permissions it checks:
     *  <ul>
     *  	<li>{@link isAdmin}</li>
     *  	<li>casinoslots.create.nameoftype</li>
     *  	<li>casinoslots.create.*</li>
     *      <li>casinoslots.create</li>
     *  </ul>
     * 
     * @param player The Player that is attempting to create a lot
     * @param type   The Type of the slot that the Player is attempting to create
     * @return       True if the player can create that slot, false if the player can't
     */
    public static boolean canCreate(Player player, Type type) {
        return canCreate(player, type.getName());
    }

    /**
     * Returns true if the player can create a slot with the type (type being a string).
     * 
     * @param player The Player that is attempting to create a lot
     * @param type   The String Type of the slot that the Player is attempting to create
     * @return       True if the player can create that slot, false if the player can't
     */
    public static boolean canCreate(Player player, String type) {
        return (isAdmin(player) || player.hasPermission(create + "." + type) || player.hasPermission(create + ".*") || player.hasPermission(create));
    }

    /**
     * Returns true if the player can create a managed slot.
     * 
     * The player must either have:
     * <ul>
     * 	<li>{@link isAdmin}</li>
     *  <li>casinoslots.create.managed.*</li>
     *  <li>casinoslots.create.managed</li>
     *  <li>casinoslots.create.*</li>
     *  <li>casinoslots.create</li>
     * </ul>
     * 
     * @param player The Player that is attempting to create a lot.
     * @return       True if the player can create managed; false if the player can't.
     */
    public static boolean canCreateManaged(Player player) {
        return (isAdmin(player) || player.hasPermission(create + ".managed.*") || player.hasPermission(create + ".managed") || player.hasPermission(create + ".*") || player.hasPermission(create));
    }

    /**
     * Returns true if the player can build a managed slot, false if not.
     * 
     * The player must either have:
     * <ul>
     * 	<li>{@link isAdmin}</li>
     *  <li>casinoslots.create.managed.(type)</li>
     *  <li>casinoslots.create.managed.*</li>
     *  <li>casinoslots.create.managed</li>
     *  <li>casinoslots.create.*</li>
     *  <li>casinoslots.create</li>
     * </ul>
     * 
     * @param player	Is the player that tries to create the slot
     * @param type		Type is the name of the type of slot
     * @return			True if the player can create a managed slot; false if the player can't
     */
    public static boolean canCreateManagedType(Player player, String type) {
        return (isAdmin(player) || player.hasPermission(create + ".managed." + type)|| player.hasPermission(create + ".managed.*") || player.hasPermission(create + ".managed") || player.hasPermission(create + ".*") || player.hasPermission(create));
    }

    /**
     * Returns true if the player can build an item slot, false if not.
     * 
     * The player must either have:
     * <ul>
     * 	<li>{@link isAdmin}</li>
     *  <li>casinoslots.create.items.(type)</li>
     *  <li>casinoslots.create.items.*</li>
     *  <li>casinoslots.create.items</li>
     *  <li>casinoslots.create.*</li>
     *  <li>casinoslots.create</li>
     * </ul>
     * 
     * @param player	Is the player that tries to create the slot
     * @param type		Type is the name of the type of slot
     * @return			True if the player can create an item slot; false if the player can't
     */
    public static boolean canCreateItemsType(Player player, String type) {
        return (isAdmin(player) || player.hasPermission(create + ".items." + type)|| player.hasPermission(create + ".items.*") || player.hasPermission(create + ".items") || player.hasPermission(create + ".*") || player.hasPermission(create));
    }

    // Returns true if the player can use the type
    public static boolean canUse(Player player, Type type) {
        String name = type.getName();
        return (isAdmin(player) || player.hasPermission(use + "." + name) || player.hasPermission(use + ".*") || player.hasPermission(use));
    }

    public static boolean isOwner(Player player, SlotMachine slot) {
        return (isAdmin(player) || slot.getOwner().equalsIgnoreCase(player.getName()));
    }
}