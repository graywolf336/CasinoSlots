package com.craftyn.casinoslots.slot.game;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.classes.OldSlotMachine;
import com.craftyn.casinoslots.classes.SlotType;
import com.craftyn.casinoslots.enums.SlotMachineColumnType;
import com.craftyn.casinoslots.util.Util;

public class Game {
    private CasinoSlots plugin;
    protected BukkitScheduler scheduler;
    private OldSlotMachine slot;
    private Player player;

    // Initializes a new game object
    public Game(OldSlotMachine slot, Player player, CasinoSlots plugin) {
        this.slot = slot;
        this.player = player;
        this.plugin = plugin;
    }

    public CasinoSlots getPlugin() {
        return this.plugin;
    }

    // Returns the active slot machine
    public OldSlotMachine getSlot() {
        return this.slot;
    }

    // Returns the type of the active slot machine
    public SlotType getType() {
        return slot.getType();
    }

    // Returns the game's player
    public Player getPlayer() {
        return this.player;
    }

    // Plays the game.
    public void play() {

        this.scheduler = plugin.getServer().getScheduler();
        int[] task = new int[3];

        if (slot.isManaged()) {
            if (slot.getFunds() >= slot.getType().getMaxPrize()) {
                slot.setEnabled(true);
            } else {
                slot.setEnabled(false);
            }
        }

        if (!slot.isEnabled()) {
            plugin.sendMessage(player, "This slot machine is currently disabled. Deposit more funds to enable.");
            return;
        }

        // Cost
        if (!slot.isItem()) {
            Double cost = getType().getCost();
            plugin.getEconomy().withdrawPlayer(player, cost);
            if (slot.isManaged()) {
                slot.deposit(cost);
                plugin.getSlotManager().saveSlot(slot);
            }
        }

        // Start playing
        slot.toggleBusy();
        if (!slot.isItem()) {
            plugin.sendMessage(player, Util.colorizeAndTokenize(getType(), getType().getMessages().getStart()));
        } else {
            int itemAmt = slot.getItemAmount();
            Material itemMat = new ItemStack(slot.getItem()).getType();
            if (itemAmt == 1) {
                plugin.sendMessage(player, itemAmt + " " + itemMat.toString().toLowerCase() + " removed from your inventory.");
            } else {
                plugin.sendMessage(player, itemAmt + " " + itemMat.toString().toLowerCase() + "es removed from your inventory.");
            }
        }

        // Initiate tasks
        for (SlotMachineColumnType c : SlotMachineColumnType.values()) {
            task[c.getIndex()] = scheduler.scheduleSyncRepeatingTask(plugin, new RotateTask(this, c), 0L, 6L);
            scheduler.scheduleSyncDelayedTask(plugin, new StopRotateTask(this, task[c.getIndex()]), c.getDelay());
        }

        // Results task
        scheduler.scheduleSyncDelayedTask(plugin, new ResultsTask(this), SlotMachineColumnType.FIRST.getDelay());
    }
}
