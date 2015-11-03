package test.com.craftyn.casinoslots.util;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyBoolean;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.powermock.api.support.membermodification.MemberMatcher.constructor;
import static org.powermock.api.support.membermodification.MemberModifier.suppress;

import java.io.File;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.WorldType;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginLogger;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;
import org.junit.Assert;
import org.mockito.Matchers;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.MockGateway;

import com.craftyn.casinoslots.CasinoSlots;

public class TestInstanceCreator {
    private CasinoSlots main;
    private Server mockServer;
    private Player mockOpPlayer, mockNotOpPlayer;
    private CommandSender mockSender, mockOpPlayerSender;
    private ConsoleCommandSender consoleSender;

    public static final File serverDirectory = new File("bin" + File.separator + "test" + File.separator + "server");
    public static final File worldsDirectory = new File("bin" + File.separator + "test" + File.separator + "server");
    public static final File pluginDirectory = new File(serverDirectory + File.separator + "plugins" + File.separator + "CasinoSlotsTest");

    public boolean setup() {
        try {
            pluginDirectory.mkdirs();
            Assert.assertTrue(pluginDirectory.exists());

            MockGateway.MOCK_STANDARD_METHODS = false;

            // Initialize the Mock server.
            mockServer = mock(Server.class);
            when(mockServer.getName()).thenReturn("UnitTestBukkit");
            when(mockServer.getVersion()).thenReturn("UnitTesting-0.0.1");
            when(mockServer.getBukkitVersion()).thenReturn("0.0.1");
            Logger.getLogger("Minecraft").setParent(Util.logger);
            when(mockServer.getLogger()).thenReturn(Util.logger);
            when(mockServer.getWorldContainer()).thenReturn(worldsDirectory);

            MockWorldFactory.makeNewMockWorld("world", Environment.NORMAL, WorldType.NORMAL);

            suppress(constructor(CasinoSlots.class));
            main = PowerMockito.spy(new CasinoSlots());

            PluginDescriptionFile pdf = PowerMockito.spy(new PluginDescriptionFile("CasinoSlots", "2.5.8-Test", "com.craftyn.casinoslots.CasinoSlots"));
            when(pdf.getPrefix()).thenReturn("CasinoSlots");
            List<String> authors = new ArrayList<String>();
            authors.add("graywolf336");
            authors.add("Darazo");
            when(pdf.getAuthors()).thenReturn(authors);
            when(main.getDescription()).thenReturn(pdf);
            when(main.getDataFolder()).thenReturn(pluginDirectory);
            when(main.isEnabled()).thenReturn(true);
            when(main.getLogger()).thenReturn(Util.logger);
            when(main.getServer()).thenReturn(mockServer);

            Field configFile = JavaPlugin.class.getDeclaredField("configFile");
            configFile.setAccessible(true);
            configFile.set(main, new File(pluginDirectory, "config.yml"));

            Field logger = JavaPlugin.class.getDeclaredField("logger");
            logger.setAccessible(true);
            logger.set(main, new PluginLogger(main));

            doReturn(getClass().getClassLoader().getResourceAsStream("config.yml")).when(main).getResource("config.yml");

            // Add the plugin to the list of loaded plugins
            JavaPlugin[] plugins = new JavaPlugin[] { main };

            // Mock the Plugin Manager
            PluginManager mockPluginManager = PowerMockito.mock(PluginManager.class);
            when(mockPluginManager.getPlugins()).thenReturn(plugins);
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
                    });

            when(mockServer.unloadWorld(anyString(), anyBoolean())).thenReturn(true);

            // add mock scheduler
            BukkitScheduler mockScheduler = mock(BukkitScheduler.class);
            when(mockScheduler.scheduleSyncDelayedTask(any(Plugin.class), any(Runnable.class), anyLong())).
            thenAnswer(new Answer<Integer>() {
                public Integer answer(InvocationOnMock invocation) throws Throwable {
                    Runnable arg;
                    try {
                        arg = (Runnable) invocation.getArguments()[1];
                    } catch (Exception e) {
                        return null;
                    }
                    arg.run();
                    return null;
                }});
            when(mockScheduler.scheduleSyncDelayedTask(any(Plugin.class), any(Runnable.class))).
            thenAnswer(new Answer<Integer>() {
                public Integer answer(InvocationOnMock invocation) throws Throwable {
                    Runnable arg;
                    try {
                        arg = (Runnable) invocation.getArguments()[1];
                    } catch (Exception e) {
                        return null;
                    }
                    arg.run();
                    return null;
                }});
            when(mockScheduler.runTaskTimerAsynchronously(any(Plugin.class), any(Runnable.class), anyLong(), anyLong())).
            thenAnswer(new Answer<Integer>() {
                public Integer answer(InvocationOnMock invocation) throws Throwable {
                    Runnable arg;
                    try {
                        arg = (Runnable) invocation.getArguments()[1];
                    } catch (Exception e) {
                        return null;
                    }
                    arg.run();
                    return null;
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

            // Load Jail
            main.onLoad();

            // Enable it and turn on debugging
            main.onEnable();
            main.getConfigData().debug = true;

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
        if(files != null) {
            for(File f: files) {
                if(f.isDirectory()) {
                    deleteFolder(f);
                }else {
                    f.delete();
                }
            }
        }

        folder.delete();
    }
}
