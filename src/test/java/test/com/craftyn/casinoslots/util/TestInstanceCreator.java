package test.com.craftyn.casinoslots.util;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.support.membermodification.MemberMatcher.constructor;
import static org.powermock.api.support.membermodification.MemberModifier.suppress;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import net.milkbowl.vault.economy.Economy;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.v1_9_R1.inventory.CraftItemFactory;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLogger;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.ServicePriority;
import org.bukkit.plugin.ServicesManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;
import org.junit.Assert;
import org.mockito.Matchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.MockGateway;
import org.powermock.core.classloader.annotations.PrepareForTest;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.enums.Settings;

@PrepareForTest({ CraftItemFactory.class })
public class TestInstanceCreator {
    private Random r;
    private CasinoSlots main;
    private Server mockServer;
    private Player mockOpPlayer, mockNotOpPlayer;
    private CommandSender mockSender, mockOpPlayerSender;
    private ConsoleCommandSender consoleSender;

    public static final File serverDirectory = new File("bin" + File.separator + "test" + File.separator + "server");
    public static final File worldsDirectory = new File("bin" + File.separator + "test" + File.separator + "server");
    public static final File pluginDirectory = new File(serverDirectory + File.separator + "plugins" + File.separator + "CasinoSlotsTest");

    public boolean setup() {
        r = new Random();
        try {
            pluginDirectory.mkdirs();
            Assert.assertTrue(pluginDirectory.exists());

            MockGateway.MOCK_STANDARD_METHODS = false;
            
            this.setupEnchantments();
            this.setupPotionEffectTypes();
            
            // Initialize the Mock server.
            mockServer = mock(Server.class);
            when(mockServer.getName()).thenReturn("UnitTestBukkit");
            when(mockServer.getVersion()).thenReturn("UnitTesting-0.0.1");
            when(mockServer.getBukkitVersion()).thenReturn("0.0.1");
            Logger.getLogger("Minecraft").setParent(Util.logger);
            when(mockServer.getLogger()).thenReturn(Util.logger);
            when(mockServer.getWorldContainer()).thenReturn(worldsDirectory);
            when(mockServer.getItemFactory()).thenReturn(CraftItemFactory.instance());
            when(mockServer.broadcastMessage(anyString())).thenReturn(r.nextInt());
            when(mockServer.dispatchCommand(anyObject(), anyString())).thenReturn(true);

            //Mock up the economy
            MockEconomy economy = mock(MockEconomy.class);

            //Mock up the service provider for the economy
            RegisteredServiceProvider<Economy> mockProvider = PowerMockito.spy(new RegisteredServiceProvider<Economy>(net.milkbowl.vault.economy.Economy.class, economy, ServicePriority.Highest, main));
            when(mockProvider.getProvider()).thenReturn(economy);

            //Mock up the services manager
            ServicesManager mockServices = mock(ServicesManager.class);
            when(mockServer.getServicesManager()).thenReturn(mockServices);
            when(mockServices.getRegistration(net.milkbowl.vault.economy.Economy.class)).thenReturn(mockProvider);

            World mockWorld = MockWorldFactory.makeNewMockWorld("world", Environment.NORMAL, WorldType.NORMAL);

            suppress(constructor(CasinoSlots.class));
            main = PowerMockito.spy(new CasinoSlots());
            
            //setup internal debugging
            Field internalDebug = CasinoSlots.class.getDeclaredField("internalDebug");
            internalDebug.setAccessible(true);
            internalDebug.set(main, true);

            PluginDescriptionFile pdf = PowerMockito.spy(new PluginDescriptionFile("CasinoSlots", "2.6.0-Test", "com.craftyn.casinoslots.CasinoSlots"));
            when(pdf.getPrefix()).thenReturn("CasinoSlots");
            when(pdf.isDatabaseEnabled()).thenReturn(false);
            List<String> authors = new ArrayList<String>();
            authors.add("graywolf336");
            authors.add("Darazo");
            when(pdf.getAuthors()).thenReturn(authors);
            when(main.getDataFolder()).thenReturn(pluginDirectory);
            when(main.isEnabled()).thenReturn(true);
            when(main.getLogger()).thenReturn(Util.logger);
            when(main.getServer()).thenReturn(mockServer);
            when(main.getEconomy()).thenReturn(economy);
            
            Field description = JavaPlugin.class.getDeclaredField("description");
            description.setAccessible(true);
            description.set(main, pdf);
            
            Field classLoader = JavaPlugin.class.getDeclaredField("classLoader");
            classLoader.setAccessible(true);
            classLoader.set(main, getClass().getClassLoader());
            
            Field configFile = JavaPlugin.class.getDeclaredField("configFile");
            configFile.setAccessible(true);
            configFile.set(main, new File(pluginDirectory, "config.yml"));

            Field logger = JavaPlugin.class.getDeclaredField("logger");
            logger.setAccessible(true);
            logger.set(main, new PluginLogger(main));

            when(main.getResource("config.yml")).thenReturn(getClass().getClassLoader().getResourceAsStream("config.yml"));

            // Mock the Plugin Manager
            PluginManager mockPluginManager = PowerMockito.mock(PluginManager.class);
            when(mockPluginManager.getPlugins()).thenReturn(new JavaPlugin[] { main });
            when(mockPluginManager.isPluginEnabled("Vault")).thenReturn(true);
            when(mockPluginManager.getPlugin("CasinoSlots")).thenReturn(main);
            when(mockPluginManager.getPermission(anyString())).thenReturn(null);

            // Give the server some worlds
            when(mockServer.getWorld(anyString())).thenAnswer(new Answer<World>() {
                public World answer(InvocationOnMock invocation) throws Throwable {
                    String arg;
                    try {
                        arg = (String) invocation.getArguments()[0];
                    } catch (Exception e) {
                        return null;
                    }
                    return MockWorldFactory.getWorld(arg);
                }
            });

            when(mockServer.getWorld(any(UUID.class))).thenAnswer(new Answer<World>() {
                public World answer(InvocationOnMock invocation) throws Throwable {
                    UUID arg;
                    try {
                        arg = (UUID) invocation.getArguments()[0];
                    } catch (Exception e) {
                        return null;
                    }
                    return MockWorldFactory.getWorld(arg);
                }
            });

            when(mockServer.getWorlds()).thenAnswer(new Answer<List<World>>() {
                public List<World> answer(InvocationOnMock invocation) throws Throwable {
                    return MockWorldFactory.getWorlds();
                }
            });
            when(mockServer.getPluginManager()).thenReturn(mockPluginManager);

            when(mockServer.createWorld(Matchers.isA(WorldCreator.class))).thenAnswer(
                    new Answer<World>() {
                        public World answer(InvocationOnMock invocation) throws Throwable {
                            WorldCreator arg;
                            try {
                                arg = (WorldCreator) invocation.getArguments()[0];
                            } catch (Exception e) {
                                return null;
                            }
                            // Add special case for creating null worlds.
                            // Not sure I like doing it this way, but this is a special case
                            if (arg.name().equalsIgnoreCase("nullworld")) {
                                return MockWorldFactory.makeNewNullMockWorld(arg.name(), arg.environment(), arg.type());
                            }
                            return MockWorldFactory.makeNewMockWorld(arg.name(), arg.environment(), arg.type());
                        }
                    }
                    );

            when(mockServer.unloadWorld(anyString(), anyBoolean())).thenReturn(true);

            // add mock scheduler
            final BukkitTask bt = mock(BukkitTask.class);
            when(bt.getTaskId()).thenReturn(r.nextInt());
            when(bt.getOwner()).thenReturn(main);
            when(bt.isSync()).thenReturn(false);
            
            BukkitScheduler mockScheduler = mock(BukkitScheduler.class);
            when(mockScheduler.scheduleSyncDelayedTask(any(Plugin.class), any(Runnable.class), anyLong())).
            thenAnswer(new Answer<Integer>() {
                public Integer answer(InvocationOnMock invocation) throws Throwable {
                    Runnable arg;
                    try {
                        arg = (Runnable) invocation.getArguments()[1];
                    } catch (Exception e) {
                        return r.nextInt();
                    }
                    arg.run();
                    return r.nextInt();
                }});
            when(mockScheduler.scheduleSyncDelayedTask(any(Plugin.class), any(Runnable.class))).
            thenAnswer(new Answer<Integer>() {
                public Integer answer(InvocationOnMock invocation) throws Throwable {
                    Runnable arg;
                    try {
                        arg = (Runnable) invocation.getArguments()[1];
                    } catch (Exception e) {
                        return r.nextInt();
                    }
                    arg.run();
                    return r.nextInt();
                }});
            when(mockScheduler.runTaskTimerAsynchronously(any(Plugin.class), any(Runnable.class), anyLong(), anyLong())).
            thenAnswer(new Answer<BukkitTask>() {
                public BukkitTask answer(InvocationOnMock invocation) throws Throwable {
                    Runnable arg;
                    try {
                        arg = (Runnable) invocation.getArguments()[1];
                    } catch (Exception e) {
                        return bt;
                    }
                    arg.run();
                    return bt;
                }});
            when(mockServer.getScheduler()).thenReturn(mockScheduler);

            // Set server
            Field serverField = JavaPlugin.class.getDeclaredField("server");
            serverField.setAccessible(true);
            serverField.set(main, mockServer);

            // Init our command sender
            final Logger consoleSenderLogger = Logger.getLogger("ConsoleCommandSender");
            consoleSenderLogger.setParent(Util.logger);
            consoleSender = mock(ConsoleCommandSender.class);
            doAnswer(new Answer<Void>() {
                public Void answer(InvocationOnMock invocation) throws Throwable {
                    consoleSenderLogger.info(ChatColor.stripColor((String) invocation.getArguments()[0]));
                    return null;
                }
            }).when(consoleSender).sendMessage(anyString());
            when(consoleSender.getServer()).thenReturn(mockServer);
            when(consoleSender.getName()).thenReturn("MockCommandSender");
            when(consoleSender.isPermissionSet(anyString())).thenReturn(true);
            when(consoleSender.isPermissionSet(Matchers.isA(Permission.class))).thenReturn(true);
            when(consoleSender.hasPermission(anyString())).thenReturn(true);
            when(consoleSender.hasPermission(Matchers.isA(Permission.class))).thenReturn(true);
            when(consoleSender.addAttachment(main)).thenReturn(null);
            when(consoleSender.isOp()).thenReturn(true);
            when(mockServer.getConsoleSender()).thenReturn(consoleSender);

            // Init our command sender
            final Logger commandSenderLogger = Logger.getLogger("CommandSender");
            commandSenderLogger.setParent(Util.logger);
            mockSender = mock(CommandSender.class);
            doAnswer(new Answer<Void>() {
                public Void answer(InvocationOnMock invocation) throws Throwable {
                    commandSenderLogger.info(ChatColor.stripColor((String) invocation.getArguments()[0]));
                    return null;
                }
            }).when(mockSender).sendMessage(anyString());
            when(mockSender.getServer()).thenReturn(mockServer);
            when(mockSender.getName()).thenReturn("MockCommandSender");
            when(mockSender.isPermissionSet(anyString())).thenReturn(true);
            when(mockSender.isPermissionSet(Matchers.isA(Permission.class))).thenReturn(true);
            when(mockSender.hasPermission(anyString())).thenReturn(true);
            when(mockSender.hasPermission(Matchers.isA(Permission.class))).thenReturn(true);
            when(mockSender.addAttachment(main)).thenReturn(null);
            when(mockSender.isOp()).thenReturn(true);

            // Init our player, who is op and who has all permissions (with name of graywolf336)
            mockOpPlayer = mock(Player.class);
            when(mockOpPlayer.getUniqueId()).thenReturn(UUID.fromString("062c14ba-4c47-4757-911b-bbf9a60dab7b"));
            when(mockOpPlayer.getName()).thenReturn("graywolf336");
            when(mockOpPlayer.getDisplayName()).thenReturn("TheGrayWolf");
            when(mockOpPlayer.isPermissionSet(anyString())).thenReturn(true);
            when(mockOpPlayer.isPermissionSet(Matchers.isA(Permission.class))).thenReturn(true);
            when(mockOpPlayer.hasPermission(anyString())).thenReturn(true);
            when(mockOpPlayer.hasPermission(Matchers.isA(Permission.class))).thenReturn(true);
            when(mockOpPlayer.isOp()).thenReturn(true);
            when(mockOpPlayer.getInventory()).thenReturn(new MockPlayerInventory());
            when(mockOpPlayer.getWorld()).thenReturn(mockWorld);
            when(mockOpPlayer.getLocation()).thenReturn(new Location(mockWorld, 23, 70, -242));
            when(mockOpPlayer.addPotionEffect(anyObject(), anyBoolean())).thenReturn(true);
            when(mockOpPlayer.addPotionEffect(anyObject())).thenReturn(true);
            when(mockServer.getPlayer("graywolf336")).thenReturn(mockOpPlayer);
            when(mockServer.getPlayer(UUID.fromString("062c14ba-4c47-4757-911b-bbf9a60dab7b"))).thenReturn(mockOpPlayer);

            mockNotOpPlayer = mock(Player.class);
            when(mockNotOpPlayer.getUniqueId()).thenReturn(UUID.fromString("89ae2bf2-0705-435f-b5d6-c24533649540"));
            when(mockNotOpPlayer.getName()).thenReturn("goose160");
            when(mockNotOpPlayer.getDisplayName()).thenReturn("goose160");
            when(mockNotOpPlayer.isPermissionSet(anyString())).thenReturn(false);
            when(mockNotOpPlayer.isPermissionSet(Matchers.isA(Permission.class))).thenReturn(false);
            when(mockNotOpPlayer.hasPermission(anyString())).thenReturn(false);
            when(mockNotOpPlayer.hasPermission(Matchers.isA(Permission.class))).thenReturn(false);
            when(mockNotOpPlayer.isOp()).thenReturn(false);
            when(mockNotOpPlayer.getInventory()).thenReturn(new MockPlayerInventory());
            when(mockNotOpPlayer.getWorld()).thenReturn(mockWorld);
            when(mockNotOpPlayer.getLocation()).thenReturn(new Location(mockWorld, 23, 70, -242));
            when(mockNotOpPlayer.addPotionEffect(anyObject(), anyBoolean())).thenReturn(true);
            when(mockNotOpPlayer.addPotionEffect(anyObject())).thenReturn(true);
            when(mockServer.getPlayer("goose160")).thenReturn(mockNotOpPlayer);
            when(mockServer.getPlayer(UUID.fromString("89ae2bf2-0705-435f-b5d6-c24533649540"))).thenReturn(mockNotOpPlayer);

            // Init our second command sender, but this time is an instance of a player
            mockOpPlayerSender = mockOpPlayer;
            when(mockOpPlayerSender.getServer()).thenReturn(mockServer);
            when(mockOpPlayerSender.getName()).thenReturn("graywolf336");
            when(mockOpPlayerSender.isPermissionSet(anyString())).thenReturn(true);
            when(mockOpPlayerSender.isPermissionSet(Matchers.isA(Permission.class))).thenReturn(true);
            when(mockOpPlayerSender.hasPermission(anyString())).thenReturn(true);
            when(mockOpPlayerSender.hasPermission(Matchers.isA(Permission.class))).thenReturn(true);
            when(mockOpPlayerSender.addAttachment(main)).thenReturn(null);
            when(mockOpPlayerSender.isOp()).thenReturn(true);
            
            Bukkit.setServer(mockServer);
            main.onLoad();
            Settings.DEBUG.setValue(true);

            // Enable it and turn on debugging
            main.onEnable();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean tearDown() {
        try {
            Field serverField = Bukkit.class.getDeclaredField("server");
            serverField.setAccessible(true);
            serverField.set(Class.forName("org.bukkit.Bukkit"), null);
        } catch (Exception e) {
            Util.log(Level.SEVERE, "Error while trying to unregister the server from Bukkit. Has Bukkit changed?");
            e.printStackTrace();
            Assert.fail(e.getMessage());
            return false;
        }

        main.onDisable();

        MockWorldFactory.clearWorlds();

        deleteFolder(pluginDirectory);
        deleteFolder(worldsDirectory);
        deleteFolder(serverDirectory);
        return true;
    }

    public CasinoSlots getMain() {
        return this.main;
    }

    public Server getServer() {
        return this.mockServer;
    }

    public CommandSender getCommandSender() {
        return this.mockSender;
    }

    public Player getOpPlayer() {
        return this.mockOpPlayer;
    }

    public Player getNotOpPlayer() {
        return this.mockNotOpPlayer;
    }

    public CommandSender getOpPlayerCommandSender() {
        return this.mockOpPlayerSender;
    }

    private void deleteFolder(File folder) {
        File[] files = folder.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory()) {
                    deleteFolder(f);
                } else {
                    f.delete();
                }
            }
        }

        folder.delete();
    }
    
    private void setupEnchantments() {
        Enchantment.registerEnchantment(new TestEnchantment(Enchantment.ARROW_DAMAGE, "ARROW_DAMAGE"));
        Enchantment.registerEnchantment(new TestEnchantment(Enchantment.ARROW_FIRE, "ARROW_FIRE"));
        Enchantment.registerEnchantment(new TestEnchantment(Enchantment.ARROW_INFINITE, "ARROW_INFINITE"));
        Enchantment.registerEnchantment(new TestEnchantment(Enchantment.ARROW_KNOCKBACK, "ARROW_KNOCKBACK"));
        Enchantment.registerEnchantment(new TestEnchantment(Enchantment.DAMAGE_ALL, "DAMAGE_ALL"));
        Enchantment.registerEnchantment(new TestEnchantment(Enchantment.DAMAGE_ARTHROPODS, "DAMAGE_ARTHROPODS"));
        Enchantment.registerEnchantment(new TestEnchantment(Enchantment.DAMAGE_UNDEAD, "DAMAGE_UNDEAD"));
        Enchantment.registerEnchantment(new TestEnchantment(Enchantment.DEPTH_STRIDER, "DEPTH_STRIDER"));
        Enchantment.registerEnchantment(new TestEnchantment(Enchantment.DIG_SPEED, "DIG_SPEED"));
        Enchantment.registerEnchantment(new TestEnchantment(Enchantment.DURABILITY, "DURABILITY"));
        Enchantment.registerEnchantment(new TestEnchantment(Enchantment.FIRE_ASPECT, "FIRE_ASPECT"));
        Enchantment.registerEnchantment(new TestEnchantment(Enchantment.FROST_WALKER, "FROST_WALKER"));
        Enchantment.registerEnchantment(new TestEnchantment(Enchantment.KNOCKBACK, "KNOCKBACK"));
        Enchantment.registerEnchantment(new TestEnchantment(Enchantment.LOOT_BONUS_BLOCKS, "LOOT_BONUS_BLOCKS"));
        Enchantment.registerEnchantment(new TestEnchantment(Enchantment.LOOT_BONUS_MOBS, "LOOT_BONUS_MOBS"));
        Enchantment.registerEnchantment(new TestEnchantment(Enchantment.LUCK, "LUCK"));
        Enchantment.registerEnchantment(new TestEnchantment(Enchantment.LURE, "LURE"));
        Enchantment.registerEnchantment(new TestEnchantment(Enchantment.MENDING, "MENDING"));
        Enchantment.registerEnchantment(new TestEnchantment(Enchantment.OXYGEN, "OXYGEN"));
        Enchantment.registerEnchantment(new TestEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, "PROTECTION_ENVIRONMENTAL"));
        Enchantment.registerEnchantment(new TestEnchantment(Enchantment.PROTECTION_EXPLOSIONS, "PROTECTION_EXPLOSIONS"));
        Enchantment.registerEnchantment(new TestEnchantment(Enchantment.PROTECTION_FALL, "PROTECTION_FALL"));
        Enchantment.registerEnchantment(new TestEnchantment(Enchantment.PROTECTION_FIRE, "PROTECTION_FIRE"));
        Enchantment.registerEnchantment(new TestEnchantment(Enchantment.PROTECTION_PROJECTILE, "PROTECTION_PROJECTILE"));
        Enchantment.registerEnchantment(new TestEnchantment(Enchantment.SILK_TOUCH, "SILK_TOUCH"));
        Enchantment.registerEnchantment(new TestEnchantment(Enchantment.THORNS, "THORNS"));
        Enchantment.registerEnchantment(new TestEnchantment(Enchantment.WATER_WORKER, "WATER_WORKER"));
        Enchantment.stopAcceptingRegistrations();
    }
    
    private void setupPotionEffectTypes() {
        PotionEffectType.registerPotionEffectType(new TestPotionEffectType(PotionEffectType.SPEED, "SPEED", false, 1.0));
        PotionEffectType.registerPotionEffectType(new TestPotionEffectType(PotionEffectType.SLOW, "SLOW", false, 0.5));
        PotionEffectType.registerPotionEffectType(new TestPotionEffectType(PotionEffectType.FAST_DIGGING, "FAST_DIGGING", false, 1.5));
        PotionEffectType.registerPotionEffectType(new TestPotionEffectType(PotionEffectType.SLOW_DIGGING, "SLOW_DIGGING", false, 0.5));
        PotionEffectType.registerPotionEffectType(new TestPotionEffectType(PotionEffectType.INCREASE_DAMAGE, "INCREASE_DAMAGE", false, 1.0));
        PotionEffectType.registerPotionEffectType(new TestPotionEffectType(PotionEffectType.HEAL, "HEAL", true, 1.0));
        PotionEffectType.registerPotionEffectType(new TestPotionEffectType(PotionEffectType.HARM, "HARM", true, 0.5));
        PotionEffectType.registerPotionEffectType(new TestPotionEffectType(PotionEffectType.JUMP, "JUMP", false, 1.0));
        PotionEffectType.registerPotionEffectType(new TestPotionEffectType(PotionEffectType.CONFUSION, "CONFUSION", false, 0.25));
        PotionEffectType.registerPotionEffectType(new TestPotionEffectType(PotionEffectType.REGENERATION, "REGENERATION", false, 0.25));
        PotionEffectType.registerPotionEffectType(new TestPotionEffectType(PotionEffectType.DAMAGE_RESISTANCE, "DAMAGE_RESISTANCE", false, 1.0));
        PotionEffectType.registerPotionEffectType(new TestPotionEffectType(PotionEffectType.FIRE_RESISTANCE, "FIRE_RESISTANCE", false, 1.0));
        PotionEffectType.registerPotionEffectType(new TestPotionEffectType(PotionEffectType.WATER_BREATHING, "WATER_BREATHING", false, 1.0));
        PotionEffectType.registerPotionEffectType(new TestPotionEffectType(PotionEffectType.INVISIBILITY, "INVISIBILITY", false, 1.0));
        PotionEffectType.registerPotionEffectType(new TestPotionEffectType(PotionEffectType.BLINDNESS, "BLINDNESS", false, 0.25));
        PotionEffectType.registerPotionEffectType(new TestPotionEffectType(PotionEffectType.NIGHT_VISION, "NIGHT_VISION", false, 1.0));
        PotionEffectType.registerPotionEffectType(new TestPotionEffectType(PotionEffectType.HUNGER, "HUNGER", false, 0.5));
        PotionEffectType.registerPotionEffectType(new TestPotionEffectType(PotionEffectType.WEAKNESS, "WEAKNESS", false, 0.5));
        PotionEffectType.registerPotionEffectType(new TestPotionEffectType(PotionEffectType.POISON, "POISON", false, 0.25));
        PotionEffectType.registerPotionEffectType(new TestPotionEffectType(PotionEffectType.WITHER, "WITHER", false, 0.25));
        PotionEffectType.registerPotionEffectType(new TestPotionEffectType(PotionEffectType.HEALTH_BOOST, "HEALTH_BOOST", false, 1.0));
        PotionEffectType.registerPotionEffectType(new TestPotionEffectType(PotionEffectType.ABSORPTION, "ABSORPTION", false, 1.0));
        PotionEffectType.registerPotionEffectType(new TestPotionEffectType(PotionEffectType.SATURATION, "SATURATION", true, 1.0));
        PotionEffectType.registerPotionEffectType(new TestPotionEffectType(PotionEffectType.GLOWING, "GLOWING", false, 1.0));
        PotionEffectType.registerPotionEffectType(new TestPotionEffectType(PotionEffectType.LEVITATION, "LEVITATION", false, 1.0));
        PotionEffectType.registerPotionEffectType(new TestPotionEffectType(PotionEffectType.LUCK, "LUCK", false, 1.0));
        PotionEffectType.registerPotionEffectType(new TestPotionEffectType(PotionEffectType.UNLUCK, "UNLUCK", false, 1.0));
        PotionEffectType.stopAcceptingRegistrations();
    }
}
