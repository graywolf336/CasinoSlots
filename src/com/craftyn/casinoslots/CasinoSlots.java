package com.craftyn.casinoslots;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.craftyn.casinoslots.command.AnCommandExecutor;
import com.craftyn.casinoslots.listeners.BlockListener;
import com.craftyn.casinoslots.listeners.ChunkListener;
import com.craftyn.casinoslots.listeners.PlayerListener;
import com.craftyn.casinoslots.listeners.EntityListener;
import com.craftyn.casinoslots.slot.RewardData;
import com.craftyn.casinoslots.slot.SlotData;
import com.craftyn.casinoslots.slot.TypeData;
import com.craftyn.casinoslots.util.ConfigData;
import com.craftyn.casinoslots.util.Permissions;
import com.craftyn.casinoslots.util.StatData;
import com.craftyn.casinoslots.util.TownyChecks;

import com.palmergames.bukkit.towny.Towny;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;

public class CasinoSlots extends JavaPlugin {
	
	protected CasinoSlots plugin;
	public Economy economy = null;
	public Server server;
	private PluginManager pm = null;
	private Towny towny = null;
	private WorldGuardPlugin worldGuard = null;
	
	public String pluginVer;
	public boolean useTowny = false, useWorldGuard = false, enableSounds = true;
	
	private PlayerListener playerListener = new PlayerListener(this);
	private BlockListener blockListener = new BlockListener(this);
	private ChunkListener chunkListener = new ChunkListener(this);
	private EntityListener entity = new EntityListener(this);
	private AnCommandExecutor commandExecutor = new AnCommandExecutor(this);
	
	public ConfigData configData = new ConfigData(this);
	public SlotData slotData = new SlotData(this);
	public TypeData typeData = new TypeData(this);
	public StatData statsData = new StatData(this);
	public RewardData rewardData = new RewardData(this);
	public Permissions permission = new Permissions(this);
	public TownyChecks townyChecks = null;

	public void onDisable() {
		if (economy != null) {
			//configData.save();
			configData.saveSlots();
			configData.saveStats();
			
			this.configData = null;
			this.slotData = null;
			this.typeData = null;
			this.statsData = null;
			this.rewardData = null;
			this.permission = null;
			this.townyChecks = null;
			
			this.towny = null;
		}
	}

	public void onEnable() {
		server = this.getServer();
		pm = this.getServer().getPluginManager();
		if(!pm.isPluginEnabled("Vault")) {
			error("Vault is required in order to use this plugin.");
			error("dev.bukkit.org/server-mods/vault/");
			pm.disablePlugin(this);
			return;
		} else {
			if(!setupEconomy()) {
				error("An economy plugin is required in order to use this plugin.");
				pm.disablePlugin(this);
				return;
			}
		}
		
		configData.load();
		saveConfig();
		
		if(configData.getBukkitVersion() < 2377) {
			enableSounds = false;
			error("Disabling sound support because your config value for the version is lower than the Recommended Build which enabled sounds.");
		}
		
		if(configData.inDebug()) debug("Use World Guard checks? " + useWorldGuard);
		if(useWorldGuard) {
			checkWorldGuard();
			if(worldGuard == null) {
				useWorldGuard = false;
				error("World Guard was not found even though you had it enabled, disabling checks.");
				if(configData.inDebug()) debug("Use World Guard checks now? " + useWorldGuard);
			}else {
				log("World Guard checking enabled.");
			}
		}
		
		if(configData.inDebug()) debug("Use Towny checks? " + useTowny);
		if(configData.inDebug()) debug("Based upon the above {^} what is below? {V}");
		if(useTowny) {
			checkTowny();
			if(towny == null) {
				useTowny = false;
				error("Towny was not found even though you had it enabled, disabling checks.");
				if(configData.inDebug()) debug("Use Towny checks now? " + useTowny);
			}else {
				townyChecks = new TownyChecks(this);
				log("Towny checking enabled.");
			}
		}
		
		pm.registerEvents(playerListener, this);
		pm.registerEvents(blockListener, this);
		pm.registerEvents(chunkListener, this);
		pm.registerEvents(entity, this);
		
		
		getCommand("casino").setExecutor(commandExecutor);
		pluginVer = getDescription().getVersion();
	}
	
	// Provides a way to shutdown the server from some other class
	public void disablePlugin() {
		if (pm == null) {
			log("Sorry couldn't disable the plugin for some odd reason. :(");
		}else {
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
        	towny = (Towny)pl;
        }
	}
	
	/**
	 * Sends a properly formatted message to the player.
	 *
	 * @param player The player to send the message to
	 * @param message The message to send to the player
	 */
	public void sendMessage(Player player, String message) {		
		message = configData.prefixColor + configData.prefix + configData.chatColor + " " + message;
		message = message.replaceAll("(?i)&([0-9abcdefklmnor])", "\u00A7$1");
		player.sendMessage(message);
	}
	
	/**
	 * Sends a properly formatted message to the command sender.
	 *
	 * @param sender The command sender to send the message to
	 * @param message The message to send to the player
	 */
	public void sendMessage(CommandSender sender, String message) {		
		message = configData.prefixColor + configData.prefix + configData.chatColor + " " + message;
		message = message.replaceAll("(?i)&([0-9abcdefklmnor])", "\u00A7$1");
		sender.sendMessage(message);
	}
	
	public void debug(String message) {
		getLogger().info("-debug- " + message);
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
        return (economy != null); 
    }
	
	/** Returns the instance of World Guard. */
	public WorldGuardPlugin getWorldGuard() {
		return worldGuard;
	}
}