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
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import test.com.craftyn.casinoslots.util.TestInstanceCreator;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.actions.Action;
import com.craftyn.casinoslots.classes.Reward;
import com.craftyn.casinoslots.classes.Type;
import com.craftyn.casinoslots.exceptions.ActionLoadingException;
import com.craftyn.casinoslots.exceptions.UnknownActionException;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ CasinoSlots.class, PluginDescriptionFile.class })
public class ActionTests {
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
    
    @Test
    public void slapActionTest() throws UnknownActionException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Constructor<?> con = main.getActionFactory().getAction("slap");
        Type t = main.getTypeManager().getTypes().iterator().next();
        Action a = (Action)con.newInstance(main, t, new String[] {});
        
        assertTrue("The slap action isn't valid.", a.isValid());
        assertTrue("The slap action didn't execute successfully.", a.execute(t, new Reward(null, 0, null), player));
    }
    
    @Test
    public void smiteActionNoArgumentsTest() throws UnknownActionException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Constructor<?> con = main.getActionFactory().getAction("smite");
        Type t = main.getTypeManager().getTypes().iterator().next();
        Action a = (Action)con.newInstance(main, t, new String[] {});
        
        assertTrue("The smite action isn't valid.", a.isValid());
        assertTrue("The smite action didn't execute successfully.", a.execute(t, new Reward(null, 0, null), player));
    }
    
    @Test
    public void smiteActionValidArgumentsTest() throws UnknownActionException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Constructor<?> con = main.getActionFactory().getAction("smite");
        Type t = main.getTypeManager().getTypes().iterator().next();
        Action a = (Action)con.newInstance(main, t, new String[] { "10" });
        
        assertTrue("The smite action isn't valid.", a.isValid());
        assertTrue("The smite action didn't execute successfully.", a.execute(t, new Reward(null, 0, null), player));
    }
    
    @Test(expected=ActionLoadingException.class)
    public void smiteActionInvalidArgumentsTest() throws UnknownActionException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Constructor<?> con = main.getActionFactory().getAction("smite");
        Type t = main.getTypeManager().getTypes().iterator().next();
        con.newInstance(main, t, new String[] { "abc" });
    }
    
    @Test(expected=ActionLoadingException.class)
    public void giveActionInvalidArgumentsTest() throws UnknownActionException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Constructor<?> con = main.getActionFactory().getAction("give");
        Type t = main.getTypeManager().getTypes().iterator().next();
        con.newInstance(main, t, new String[] { });
    }
    
    @Test
    public void giveActionValidArgumentsTest() throws UnknownActionException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        Constructor<?> con = main.getActionFactory().getAction("give");
        Type t = main.getTypeManager().getTypes().iterator().next();
        Action action = (Action)con.newInstance(main, t, new String[] { "stone:4", "64" });
        
        assertTrue("The give action isn't valid.", action.isValid());
        assertTrue("The give action didn't execute successfully.", action.execute(t, new Reward(null, 0, null), player));
    }
}
