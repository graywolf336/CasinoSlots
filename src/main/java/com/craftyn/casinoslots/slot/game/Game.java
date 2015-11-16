package com.craftyn.casinoslots.slot.game;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitScheduler;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.classes.SlotMachine;
import com.craftyn.casinoslots.classes.Type;

public class Game {
    private CasinoSlots plugin;
    protected BukkitScheduler scheduler;
    private SlotMachine slot;
    private Player player;

    // Initializes a new game object
    public Game(SlotMachine slot, Player player, CasinoSlots plugin) {
        this.slot = slot;
        this.player = player;
        this.plugin = plugin;
    }

    public CasinoSlots getPlugin() {
        return this.plugin;
    }

    // Returns the active slot machine
    public SlotMachine getSlot() {
        return this.slot;
    }

    // Returns the type of the active slot machine
    public Type getType() {
        return slot.getType();
    }

    // Returns the game's player
    public Player getPlayer() {
        return this.player;
    }

    // Plays the game.
    public void play() {

        this.scheduler = plugin.getServer().getScheduler();
        Integer[] task = new Integer[3];
        Long[] delay = { 60L, 80L, 100L };

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
            plugin.sendMessage(player, getType().getMessages().get("start"));
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
        for (Integer i = 0; i < 3; i++) {
            task[i] = scheduler.scheduleSyncRepeatingTask(plugin, new RotateTask(this, i), 0L, 6L);
            scheduler.scheduleSyncDelayedTask(plugin, new StopRotateTask(this, task[i]), delay[2 - i]);
        }

        // Results task
        scheduler.scheduleSyncDelayedTask(plugin, new ResultsTask(this), delay[2]);
    }
}
