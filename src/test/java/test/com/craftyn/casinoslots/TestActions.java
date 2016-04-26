package test.com.craftyn.casinoslots;

import static org.junit.Assert.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import test.com.craftyn.casinoslots.util.TestInstanceCreator;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.actions.Action;
import com.craftyn.casinoslots.classes.Reward;
import com.craftyn.casinoslots.classes.SlotType;
import com.craftyn.casinoslots.exceptions.ActionLoadingException;
import com.craftyn.casinoslots.exceptions.UnknownActionException;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ CasinoSlots.class, PluginDescriptionFile.class })
@PowerMockIgnore("javax.management.*")
public class TestActions {
    private static TestInstanceCreator creator;
    private static CasinoSlots main;
    private static Player player;

    @BeforeClass
    public static void setUp() throws Exception {
        creator = new TestInstanceCreator();
        assertNotNull("The instance creator is null.", creator);
        assertTrue(creator.setup());
        main = creator.getMain();
        assertNotNull("The CasinoSlots class is null.", main);
        assertTrue("The CasinoSlots plugin is disabled.", main.isEnabled());
        assertFalse("The types doesn't have any values.", main.getTypeManager().getTypes().isEmpty());
        player = creator.getNotOpPlayer();
        assertNotNull("The player value is null.", player);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        creator.tearDown();
        main = null;
    }

    @Test
    public void actionsExistTest() {
        assertTrue("The addxp is not a valid action.", main.getActionFactory().isActionValid("addxp"));
        assertTrue("The addxplvl is not a valid action.", main.getActionFactory().isActionValid("addxplvl"));
        assertTrue("The broadcast is not a valid action.", main.getActionFactory().isActionValid("broadcast"));
        assertTrue("The command is not a valid action.", main.getActionFactory().isActionValid("command"));
        assertTrue("The fire is not a valid action.", main.getActionFactory().isActionValid("fire"));
        assertTrue("The give is not a valid action.", main.getActionFactory().isActionValid("give"));
        assertTrue("The kick is not a valid action.", main.getActionFactory().isActionValid("kick"));
        assertTrue("The kill is not a valid action.", main.getActionFactory().isActionValid("kill"));
        assertTrue("The potion is not a valid action.", main.getActionFactory().isActionValid("potion"));
        assertTrue("The rocket is not a valid action.", main.getActionFactory().isActionValid("rocket"));
        assertTrue("The slap is not a valid action.", main.getActionFactory().isActionValid("slap"));
        assertTrue("The smite is not a valid action.", main.getActionFactory().isActionValid("smite"));
        assertTrue("The tpto is not a valid action.", main.getActionFactory().isActionValid("tpto"));
    }
    
    @Test(expected=ActionLoadingException.class)
    public void addXpActionInvalidTest() throws UnknownActionException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Constructor<?> con = main.getActionFactory().getAction("addxp");
        con.newInstance(main, new String[] {});
    }
    
    @Test
    public void addXpActionTest() throws UnknownActionException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Constructor<?> con = main.getActionFactory().getAction("addxp");
        SlotType t = main.getTypeManager().getTypes().iterator().next();
        Action a = (Action)con.newInstance(main, new String[] { "10" });
        
        assertTrue("The addXp action isn't valid.", a.isValid());
        assertTrue("The addXp action didn't execute successfully.", a.execute(t, new Reward(null, 0, null), player));
        assertEquals("The addXp action toString() isn't correct.", "addxp 10", a.toString());
    }
    
    @Test(expected=ActionLoadingException.class)
    public void addXpLvlActionInvalidTest() throws UnknownActionException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Constructor<?> con = main.getActionFactory().getAction("addxplvl");
        con.newInstance(main, new String[] {});
    }
    
    @Test
    public void addXpLvlActionTest() throws UnknownActionException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Constructor<?> con = main.getActionFactory().getAction("addxplvl");
        SlotType t = main.getTypeManager().getTypes().iterator().next();
        Action a = (Action)con.newInstance(main, new String[] { "10" });
        
        assertTrue("The addXpLvl action isn't valid.", a.isValid());
        assertTrue("The addXpLvl action didn't execute successfully.", a.execute(t, new Reward(null, 0, null), player));
        assertEquals("The addXpLvl action toString() isn't correct.", "addxplvl 10", a.toString());
    }
    
    @Test(expected=ActionLoadingException.class)
    public void broadcastActionInvalidTest() throws UnknownActionException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Constructor<?> con = main.getActionFactory().getAction("broadcast");
        con.newInstance(main, new String[] {});
    }
    
    @Test
    public void broadcastActionTest() throws UnknownActionException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Constructor<?> con = main.getActionFactory().getAction("broadcast");
        SlotType t = main.getTypeManager().getTypes().iterator().next();
        Action a = (Action)con.newInstance(main, new String[] { "[playername]", "([playeruuid])", "has", "won", "[moneywon]", "from", "a", "[type]", "type", "casino", "after", "paying", "[cost]", "to", "play!", "[[player]]" });
        
        assertTrue("The broadcast action isn't valid.", a.isValid());
        assertTrue("The broadcast action didn't execute successfully.", a.execute(t, new Reward(null, 1550, null), player));
        assertEquals("The broadcast action toString() isn't correct.", "broadcast [playername] ([playeruuid]) has won [moneywon] from a [type] type casino after paying [cost] to play! [[player]]", a.toString());
    }
    
    @Test(expected=ActionLoadingException.class)
    public void commandActionInvalidTest() throws UnknownActionException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Constructor<?> con = main.getActionFactory().getAction("command");
        con.newInstance(main, new String[] {});
    }
    
    @Test
    public void commandActionTest() throws UnknownActionException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Constructor<?> con = main.getActionFactory().getAction("command");
        SlotType t = main.getTypeManager().getTypes().iterator().next();
        Action a = (Action)con.newInstance(main, new String[] { "broadcast", "[playername]", "([playeruuid])", "has", "won", "[moneywon]", "from", "a", "[type]", "type", "casino", "after", "paying", "[cost]", "to", "play!", "[[player]]" });
        
        assertTrue("The command action isn't valid.", a.isValid());
        assertTrue("The command action didn't execute successfully.", a.execute(t, new Reward(null, 1550, null), player));
        assertEquals("The command action toString() isn't correct.", "command broadcast [playername] ([playeruuid]) has won [moneywon] from a [type] type casino after paying [cost] to play! [[player]]", a.toString());
    }
    
    @Test(expected=ActionLoadingException.class)
    public void fireActionInvalidTest() throws UnknownActionException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Constructor<?> con = main.getActionFactory().getAction("fire");
        con.newInstance(main, new String[] {});
    }
    
    @Test
    public void fireActionTest() throws UnknownActionException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Constructor<?> con = main.getActionFactory().getAction("fire");
        SlotType t = main.getTypeManager().getTypes().iterator().next();
        Action a = (Action)con.newInstance(main, new String[] { "5" });
        
        assertTrue("The fire action isn't valid.", a.isValid());
        assertTrue("The fire action didn't execute successfully.", a.execute(t, new Reward(null, 0, null), player));
        assertEquals("The fire action toString() isn't correct.", "fire 5", a.toString());
    }
    
    @Test(expected=ActionLoadingException.class)
    public void giveActionInvalidArgumentsTest() throws UnknownActionException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Constructor<?> con = main.getActionFactory().getAction("give");
        con.newInstance(main, new String[] { });
    }
    
    @Test
    public void giveActionValidArgumentsTest() throws UnknownActionException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Constructor<?> con = main.getActionFactory().getAction("give");
        SlotType t = main.getTypeManager().getTypes().iterator().next();
        Action action = (Action)con.newInstance(main, new String[] { "stone:4", "64" });
        
        assertTrue("The give action isn't valid.", action.isValid());
        assertTrue("The give action didn't execute successfully.", action.execute(t, new Reward(null, 0, null), player));
        assertEquals("The give action toString() isn't correct.", "give stone:4 64", action.toString());
    }
    
    @Test
    public void kickActionValidNoArgumentsTest() throws UnknownActionException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Constructor<?> con = main.getActionFactory().getAction("kick");
        SlotType t = main.getTypeManager().getTypes().iterator().next();
        Action action = (Action)con.newInstance(main, new String[] { });
        
        assertTrue("The kick action isn't valid.", action.isValid());
        assertTrue("The kick action didn't execute successfully.", action.execute(t, new Reward(null, 0, null), player));
        assertEquals("The kick action toString() isn't correct.", "kick &4You cheated the Casino!", action.toString());
    }
    
    @Test
    public void kickActionValidArgumentsTest() throws UnknownActionException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Constructor<?> con = main.getActionFactory().getAction("kick");
        SlotType t = main.getTypeManager().getTypes().iterator().next();
        Action action = (Action)con.newInstance(main, new String[] { "&4How", "dare", "you", "[player]", "ever", "win", "[type]!" });
        
        assertTrue("The kick action isn't valid.", action.isValid());
        assertTrue("The kick action didn't execute successfully.", action.execute(t, new Reward(null, 0, null), player));
        assertEquals("The kick action toString() isn't correct.", "kick &4How dare you [player] ever win [type]!", action.toString());
    }
    
    @Test
    public void killActionValidNoArgumentsTest() throws UnknownActionException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Constructor<?> con = main.getActionFactory().getAction("kill");
        SlotType t = main.getTypeManager().getTypes().iterator().next();
        Action action = (Action)con.newInstance(main, new String[] { });
        
        assertTrue("The kill action isn't valid.", action.isValid());
        assertTrue("The kill action didn't execute successfully.", action.execute(t, new Reward(null, 0, null), player));
        assertTrue("The kill action didn't set the player's health to 0.", 0 == player.getHealth());
        assertEquals("The kill action toString() isn't correct.", "kill", action.toString());
    }
    
    @Test(expected=ActionLoadingException.class)
    public void potionActionInvalidArgumentsTest() throws UnknownActionException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Constructor<?> con = main.getActionFactory().getAction("potion");
        con.newInstance(main, new String[] { });
    }
    
    @Test
    public void potionActionValidArgumentsTest() throws UnknownActionException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Constructor<?> con = main.getActionFactory().getAction("potion");
        SlotType t = main.getTypeManager().getTypes().iterator().next();
        Action action = (Action)con.newInstance(main, new String[] { "blindness", "1", "5" });
        
        assertTrue("The potion action isn't valid.", action.isValid());
        assertTrue("The potion action didn't execute successfully.", action.execute(t, new Reward(null, 0, null), player));
        assertEquals("The potion action toString() isn't correct.", "potion blindness 1 5", action.toString());
    }
    
    @Test
    public void rocketActionTest() throws UnknownActionException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Constructor<?> con = main.getActionFactory().getAction("rocket");
        SlotType t = main.getTypeManager().getTypes().iterator().next();
        Action a = (Action)con.newInstance(main, new String[] {});
        
        assertTrue("The rocket action isn't valid.", a.isValid());
        assertTrue("The rocket action didn't execute successfully.", a.execute(t, new Reward(null, 0, null), player));
    }
    
    @Test
    public void slapActionTest() throws UnknownActionException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Constructor<?> con = main.getActionFactory().getAction("slap");
        SlotType t = main.getTypeManager().getTypes().iterator().next();
        Action a = (Action)con.newInstance(main, new String[] {});
        
        assertTrue("The slap action isn't valid.", a.isValid());
        assertTrue("The slap action didn't execute successfully.", a.execute(t, new Reward(null, 0, null), player));
    }
    
    @Test(expected=ActionLoadingException.class)
    public void smiteActionNoArgumentsTest() throws UnknownActionException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Constructor<?> con = main.getActionFactory().getAction("smite");
        con.newInstance(main, new String[] {});
    }
    
    @Test
    public void smiteActionValidArgumentsTest() throws UnknownActionException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Constructor<?> con = main.getActionFactory().getAction("smite");
        SlotType t = main.getTypeManager().getTypes().iterator().next();
        Action a = (Action)con.newInstance(main, new String[] { "10" });
        
        assertTrue("The smite action isn't valid.", a.isValid());
        assertTrue("The smite action didn't execute successfully.", a.execute(t, new Reward(null, 0, null), player));
        assertEquals("The smite action toString() isn't correct.", "smite 10", a.toString());
    }
    
    @Test(expected=ActionLoadingException.class)
    public void smiteActionInvalidArgumentsTest() throws UnknownActionException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Constructor<?> con = main.getActionFactory().getAction("smite");
        con.newInstance(main, new String[] { "abc" });
    }
}
