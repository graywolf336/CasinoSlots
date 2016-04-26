package com.craftyn.casinoslots.util;

import java.io.File;
import java.io.IOException;
import java.util.Collection;

import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.classes.OldSlotMachine;
import com.craftyn.casinoslots.enums.Settings;

public class ConfigData {
    private CasinoSlots plugin;

    public Configuration config;
    public FileConfiguration slots;
    public FileConfiguration stats;

    private File configFile;
    private File slotsFile;
    private File statsFile;

    //towny stuff
    public Boolean onlyMayors, onlyTowns;
    public String noMayor, noTown, noOwnership;

    // Initialize ConfigData
    public ConfigData(CasinoSlots plugin) {
        this.plugin = plugin;
    }

    // Load all config data
    public void load() {
        this.config = this.plugin.getConfig();

        statsFile = new File(plugin.getDataFolder(), "stats.yml");
        stats = YamlConfiguration.loadConfiguration(statsFile);

        slotsFile = new File(plugin.getDataFolder(), "slots.yml");
        slots = YamlConfiguration.loadConfiguration(slotsFile);
    }

    /**
     * Reload all the configs from disk.
     * 
     * <p>
     * 
     * This method reloads the config.yml, sets the slots config to null and then loads it again, and sets the stats file to null and then loads it again.
     */
    public void reloadConfigs() {
        plugin.log("Reloading the configs.");

        configFile = new File(plugin.getDataFolder(), "config.yml");
        config = YamlConfiguration.loadConfiguration(configFile);

        reloadGlobals();

        //Set it to null, then reload it
        slots = null;
        slots = YamlConfiguration.loadConfiguration(slotsFile);

        //Set it to null, then reload it.
        stats = null;
        stats = YamlConfiguration.loadConfiguration(statsFile);
    }

    /** This reloads all the global variables, like debugging, prefix, tracking stats, etc. */
    public void reloadGlobals() {
        plugin.useWorldGuard = config.getBoolean("options.enable-worldguard-check", false);
        plugin.useTowny = config.getBoolean("options.towny-checks.enabled", false);
        if(plugin.useTowny) {
            this.onlyMayors = config.getBoolean("options.towny-checks.only-mayors", true);
            this.noMayor = config.getString("options.towny-checks.no-mayor", "You must be a mayor to create a Casino Slot.");
            this.onlyTowns = config.getBoolean("options.towny-checks.only-towns", true);
            this.noTown = config.getString("options.towny-checks.no-town", "To create a slot you must be part of a town.");
            this.noOwnership = config.getString("options.towny-checks.no-ownership", "You don't own the plot where that would be at, please make sure you are the owner and then try again.");
        }
    }

    // Save slots data
    public void saveSlots() {
        Collection<OldSlotMachine> slots = plugin.getSlotManager().getSlots();

        if(slots != null && !slots.isEmpty()) {
            for (OldSlotMachine slot : slots) {
                String path = "slots." + slot.getName() + ".";
                this.slots.set(path + "name", slot.getName());
                this.slots.set(path + "type", slot.getType().getName());
                this.slots.set(path + "ownerid", slot.getOwnerId().toString());
                this.slots.set(path + "owner", slot.getOwner());
                this.slots.set(path + "world", slot.getWorld());
                this.slots.set(path + "managed", slot.isManaged());
                this.slots.set(path + "funds", slot.getFunds());
            }
        }

        try {
            if(Settings.DEBUG.asBoolean()) plugin.debug("Saving the slots.yml.");
            this.slots.save(slotsFile);
        } catch (IOException e) {
            plugin.severe("There was a problem saving your slots.yml file.");
            if(Settings.DEBUG.asBoolean()) e.printStackTrace();
        }
    }

    // Save stats data
    public void saveStats() {
        Collection<Stat> stats = plugin.getStatData().getStats();
        if(stats != null && !stats.isEmpty()) {
            for(Stat stat : stats) {
                String path = "types." + stat.getType() +".";
                this.stats.set(path + "spins", stat.getSpins());
                this.stats.set(path + "wins", stat.getWins());
                this.stats.set(path + "losts", stat.getLosts());
                this.stats.set(path + "won", stat.getWon());
                this.stats.set(path + "lost", stat.getLost());
            }
        }

        this.stats.set("global.spins", plugin.getStatData().globalSpins);
        this.stats.set("global.wins", plugin.getStatData().globalWins);
        this.stats.set("global.losts", plugin.getStatData().globalLosts);
        this.stats.set("global.won", plugin.getStatData().globalWon);
        this.stats.set("global.lost", plugin.getStatData().globalLost);

        try {
            if(Settings.DEBUG.asBoolean()) plugin.debug("Saving the stats.yml.");
            this.stats.save(statsFile);
        } catch (IOException e) {
            plugin.severe("There was a problem saving your stats.yml file.");
            if(Settings.DEBUG.asBoolean()) e.printStackTrace();
        }
    }
}