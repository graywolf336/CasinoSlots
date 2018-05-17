package com.craftyn.casinoslots.oldcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.classes.OldSlotMachine;
import com.craftyn.casinoslots.util.PermissionUtil;

public abstract class AnCommand {
    protected CasinoSlots plugin;
    protected Player player;
    protected CommandSender sender;
    protected String[] args;

    // Initializes new command
    public AnCommand(CasinoSlots plugin, String[] args, Player player) {
        this.plugin = plugin;
        this.args = args;
        this.sender = player;
        this.player = player;
    }

    public AnCommand(CasinoSlots plugin, String[] args, CommandSender sender) {
        this.plugin = plugin;
        this.args = args;
        this.sender = sender;

        if(sender instanceof Player) player = (Player) sender;
    }

    // Processes command, handled by subclasses
    public boolean process() {
        return false;
    }

    // Returns true if player owns this slot machine
    public Boolean isOwner(OldSlotMachine slot) {
        if(slot == null) throw new IllegalArgumentException("The slot passed in to check owner of is null!");
        
        if(!(sender instanceof Player)) return true; //The console

        if(PermissionUtil.isAdmin(player) || slot.getOwnerId().equals(player.getUniqueId()))
            return true;
        else
            return false;
    }

    // Called when a player is denied permission to a command
    public void noPermission() {
        plugin.sendMessage(player, "You don't have permission to do this.");
    }

    /**
     * Sends a message to the player who did the command.
     *
     * @param message The message to send to the player.
     */
    public void sendMessage(String message) {
        plugin.sendMessage(player, message);
    }

    /**
     * Sends a message to the sender of the command.
     * 
     * @param message The message to send to the sender of the command.
     */
    public void senderSendMessage(String message) {
        plugin.sendMessage(sender, message);
    }

}