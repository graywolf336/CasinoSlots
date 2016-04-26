package com.craftyn.casinoslots;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.craftyn.casinoslots.actions.ActionFactory;
import com.craftyn.casinoslots.classes.Reel;
import com.craftyn.casinoslots.classes.SimpleLocation;
import com.craftyn.casinoslots.classes.SlotMachine;
import com.craftyn.casinoslots.classes.SlotMachineMessages;
import com.craftyn.casinoslots.classes.SlotMachineOwner;
import com.craftyn.casinoslots.classes.SlotType;
import com.craftyn.casinoslots.command.AnCommandExecutor;
import com.craftyn.casinoslots.enums.Settings;
import com.craftyn.casinoslots.listeners.BlockListener;
import com.craftyn.casinoslots.listeners.EntityListener;
import com.craftyn.casinoslots.listeners.PlayerListener;
import com.craftyn.casinoslots.slot.SlotManager;
import com.craftyn.casinoslots.slot.TypeManager;
import com.craftyn.casinoslots.util.ConfigData;
import com.craftyn.casinoslots.util.StatData;
import com.craftyn.casinoslots.util.TownyChecks;
import com.palmergames.bukkit.towny.Towny;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class CasinoSlots extends JavaPlugin {
    private Economy economy = null;
    private PluginManager pm = null;
    private Towny towny = null;
    private WorldGuardPlugin worldGuard = null;
    private Update update;
    private int updateCheckTask;

    public boolean useTowny = false, useWorldGuard = false;
    private boolean internalDebug = false;

    private PlayerListener playerListener;
    private BlockListener blockListener;
    private EntityListener entity;
    private AnCommandExecutor commandExecutor;

    private ConfigData configData;
    private SlotManager slotManager;
    private StatData statsData;
    private TownyChecks townyChecks = null;

    private ActionFactory actionFactory = null;
    private TypeManager typeManager;

    public void onLoad() {
        this.loadConfig();
        Settings.setPlugin(this);
        this.loadSerializableClasses();
        new CasinoSlotsStaticAPI(this);
    }

    public void onEnable() {
        //Verify vault is installed before loading anything
        pm = this.getServer().getPluginManager();
        if (!pm.isPluginEnabled("Vault")) {
            error("Vault is required in order to use this plugin.");
            error("dev.bukkit.org/server-mods/vault/");
            pm.disablePlugin(this);
            return;
        } else {
            if (!setupEconomy()) {
                error("An economy plugin is required in order to use this plugin.");
                pm.disablePlugin(this);
                return;
            }
        }

        //Load the ActionFactory first, since it throws an exception if something else wrong
        try {
            this.actionFactory = new ActionFactory(this);
        } catch (Exception e) {
            e.printStackTrace();
            this.getLogger().severe("Failed to load the action definitions, please see the error above!");
            pm.disablePlugin(this);
            return;
        }

        try {
            this.typeManager = new TypeManager(this);
        } catch (Exception e) {
            e.printStackTrace();
            this.getLogger().severe("Failed to load the type definitions, please see the error above!");
            pm.disablePlugin(this);
            return;
        }

        this.playerListener = new PlayerListener(this);
        this.blockListener = new BlockListener(this);
        this.entity = new EntityListener(this);
        this.commandExecutor = new AnCommandExecutor(this);

        this.configData = new ConfigData(this);
        this.slotManager = new SlotManager(this);
        this.statsData = new StatData(this);

        //Loads just the configuration, not the types, stats, or slots
        configData.load();

        debug("Use World Guard:" + useWorldGuard);
        if (useWorldGuard) {
            checkWorldGuard();
            if (worldGuard == null) {
                useWorldGuard = false;
                error("World Guard was not found even though you had it enabled, disabling checks.");
            } else {
                log("World Guard checking enabled.");
            }
        }

        debug("Use Towny: " + useTowny);
        if (useTowny) {
            checkTowny();

            if (towny == null) {
                useTowny = false;
                error("Towny was not found even though you had it enabled, disabling checks.");
            } else {
                townyChecks = new TownyChecks(this);
                log("Towny checking enabled.");
            }
        }

        pm.registerEvents(playerListener, this);
        pm.registerEvents(blockListener, this);
        pm.registerEvents(entity, this);

        reloadUpdateCheck();

        //Allow actions to be injected before we load anything
        this.getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            public void run() {
                //getTypeManager().loadTypes();
                //getSlotManager().loadSlots();
                //getStatData().loadStats();
            }
        }, 5L);
    }

    public void onDisable() {
        if (economy != null) {
            //configData.save();
            //configData.saveSlots();
            //configData.saveStats();

            this.configData = null;
            this.slotManager = null;
            this.typeManager = null;
            this.statsData = null;
            this.townyChecks = null;

            this.towny = null;
        }

        this.saveConfig();
    }

    private void loadSerializableClasses() {
        ConfigurationSerialization.registerClass(SimpleLocation.class, "CasinoSlotsSimpleLocation");
        ConfigurationSerialization.registerClass(Reel.class, "CasinoSlotsReel");
        ConfigurationSerialization.registerClass(SlotMachineMessages.class, "CasinoSlotsSlotMachineMessages");
        ConfigurationSerialization.registerClass(SlotType.class, "CasinoSlotsSlotType");
        ConfigurationSerialization.registerClass(SlotMachineOwner.class, "CasinoSlotsSlotMachineOwner");
        ConfigurationSerialization.registerClass(SlotMachine.class, "CasinoSlotsSlotMachine");
    }

    private void loadConfig() {
        //Only create the default config if it doesn't exist
        this.saveDefaultConfig();

        //Append new key-value paris to the config
        getConfig().options().copyDefaults(true);

        //Set the header and save
        getConfig().options().header(getHeader());
        saveConfig();
    }

    private String getHeader() {
        String sep = System.getProperty("line.separator");

        return "###################" + sep
                + "CasinoSlots v" + this.getDescription().getVersion() + " config file" + sep
                + "Note: You -must- use spaces instead of tabs!" + sep +
                "###################";
    }

    public boolean onCommand(CommandSender sender, Command command, String cmdlabel, final String[] args) {
        if (cmdlabel.equals("casino")) {
            return this.commandExecutor.onCommand(sender, command, cmdlabel, args);
        } else {
            return false;
        }
    }

    // Provides a way to shutdown the server from some other class
    public void disablePlugin() {
        if (pm == null) {
            log("Sorry couldn't disable the plugin for some odd reason. :(");
        } else {
            pm.disablePlugin(this);
        }
    }

    private void checkWorldGuard() {
        Plugin pl = pm.getPlugin("WorldGuard");

        if (pl != null && pl instanceof WorldGuardPlugin) {
            worldGuard = (WorldGuardPlugin) pl;
        }
    }

    private void checkTowny() {
        Plugin pl = pm.getPlugin("Towny");
        if (pl != null && pl instanceof Towny) {
            towny = (Towny) pl;
        }
    }

    /** Reloads the update checker, in case they changed a setting about it. */
    public void reloadUpdateCheck() {
        getServer().getScheduler().cancelTask(updateCheckTask);
        update = new Update(this);
        debug("Check for updates: " + getConfig().getBoolean("options.update-checking.enabled"));

        if (getConfig().getBoolean("options.update-checking.enabled")) {
            try {
                updateCheckTask = getServer().getScheduler().runTaskTimerAsynchronously(this, new Runnable() {
                    public void run() {
                        update.query();
                    }
                }, 100L, getConfig().getInt("options.update-checking.time", 120) * 1200).getTaskId();
            } catch (Exception e) {
                e.printStackTrace();
                getLogger().severe("Was unable to schedule the update checking, please check your time format is correct.");
            }
        }
    }

    /**
     * Returns the instance of the update checking class.
     *
     * @return instance of {@link Update}
     */
    public Update getUpdate() {
        return this.update;
    }

    /**
     * Sends a properly formatted message to the player.
     *
     * @param player The player to send the message to
     * @param message The message to send to the player
     */
    public void sendMessage(Player player, String message) {
        message = Settings.CHAT_COLOR.asString() + message;
        if (Settings.CHAT_USE_PREFIX.asBoolean()) {
            message = Settings.CHAT_PREFIX.asString() + " " + message;
        }

        player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    /**
     * Sends a properly formatted message to the command sender.
     *
     * @param sender The command sender to send the message to
     * @param message The message to send to the player
     */
    public void sendMessage(CommandSender sender, String message) {
        message = Settings.CHAT_COLOR.asString() + message;
        if (Settings.CHAT_USE_PREFIX.asBoolean()) {
            message = Settings.CHAT_PREFIX.asString() + " " + message;
        }

        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    public void debug(String message) {
        if (Settings.inDebug() || internalDebug)
            getLogger().info("[Debug] " + message);
    }

    /**
     * Logs a properly formatted message to the console with a info prefix.
     *
     * @param message The info message to log.
     */
    public void log(String message) {
        getLogger().info(message);
    }

    /**
     * Logs a properly formatted message to the console with a error prefix.
     *
     * @param message The warning message to log.
     */
    public void error(String message) {
        getLogger().warning(message);
    }

    /**
     * Logs a properly formatted message to the console with the severe prefix.
     *
     * @param message The warning message to log.
     */
    public void severe(String message) {
        getLogger().severe(message);
    }

    // Registers economy with Vault
    private Boolean setupEconomy() {
        RegisteredServiceProvider<Economy> economyProvider = getServer().getServicesManager().getRegistration(net.milkbowl.vault.economy.Economy.class);
        if (economyProvider != null) {
            economy = economyProvider.getProvider();
        }
        return economy != null;
    }

    /**
     * Returns the instance of the economy.
     *
     * @return the {@link Economy} instance
     */
    public Economy getEconomy() {
        return this.economy;
    }

    /**
     * Returns the instance of World Guard.
     *
     * @return the {@link WorldGuardPlugin} instance
     */
    public WorldGuardPlugin getWorldGuard() {
        return this.worldGuard;
    }

    /**
     * Returns the instance of the {@link ActionFactory}.
     *
     * @return the {@link ActionFactory} instance
     */
    public ActionFactory getActionFactory() {
        return this.actionFactory;
    }

    /**
     * Returns the instance of the {@link ConfigData}.
     *
     * @return the {@link ConfigData} instance
     */
    public ConfigData getConfigData() {
        return this.configData;
    }

    /**
     * Returns the instance of the {@link SlotManager}.
     *
     * @return the {@link SlotManager} instance
     */
    public SlotManager getSlotManager() {
        return this.slotManager;
    }

    /**
     * Returns the instance of the {@link TypeManager}.
     *
     * @return the {@link TypeManager} instance
     */
    public TypeManager getTypeManager() {
        return this.typeManager;
    }

    /**
     * Returns the instance of the {@link StatData}.
     *
     * @return the {@link StatData} instance
     */
    public StatData getStatData() {
        return this.statsData;
    }

    /**
     * Returns the instance of the {@link TownyChecks}.
     *
     * @return the {@link TownyChecks} instance
     */
    public TownyChecks getTownyChecks() {
        return this.townyChecks;
    }
}
