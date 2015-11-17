package test.com.craftyn.casinoslots;

import static org.junit.Assert.*;

import java.lang.reflect.Constructor;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.craftyn.casinoslots.actions.ActionFactory;
import com.craftyn.casinoslots.actions.impl.AddXpAction;
import com.craftyn.casinoslots.exceptions.ActionClassConstructorParameterNotExpectedTypeException;
import com.craftyn.casinoslots.exceptions.ClassConstructorIsntThreeException;
import com.craftyn.casinoslots.exceptions.ClassDoesntExtendActionException;
import com.craftyn.casinoslots.exceptions.UnknownActionException;

public class TestActionFactory {
    private ActionFactory factory;
    
    @Before
    public void setUp() throws Exception {
        factory = new ActionFactory();
        assertNotNull("The factory is null?!?!", factory);
    }

    @After
    public void tearDown() throws Exception {
        factory = null;
        assertNull("The factory isn't null.", factory);
    }

    @Test(expected=UnknownActionException.class)
    public void unknownActionTest() throws UnknownActionException {
        factory.getAction("unknownaction");
    }
    
    @Test(expected=ClassDoesntExtendActionException.class)
    public void classNotExtendedTest() throws ClassDoesntExtendActionException, ClassConstructorIsntThreeException, ActionClassConstructorParameterNotExpectedTypeException {
        factory.addAction("doesntextend", this.getClass());
    }
    
    @Test
    public void addxpActionExistsTest() throws SecurityException, UnknownActionException {
        assertTrue("The addxp action doesn't exist.", factory.isActionValid("addxp"));
        
        Constructor<?> expected = AddXpAction.class.getConstructors()[0];
        Constructor<?> actual = factory.getAction("addxp");
        
        assertEquals("The addxp action constructor isn't the expected value.", expected, actual);
        assertEquals("The constructor in the factory isn't the expected count.", 3, actual.getParameterCount());
        assertEquals("com.craftyn.casinoslots.CasinoSlots", actual.getParameterTypes()[0].getName());
        assertEquals("com.craftyn.casinoslots.classes.Type", actual.getParameterTypes()[1].getName());
        assertEquals("[Ljava.lang.String;", actual.getParameterTypes()[2].getName());
    }
    
    @Test
    public void addxplvlActionExistsTest() {
        assertTrue("The addxplvl action doesn't exist.", factory.isActionValid("addxplvl"));
    }
    
    @Test
    public void broadcastActionExistsTest() {
        assertTrue("The broadcast action doesn't exist.", factory.isActionValid("broadcast"));
    }
    
    @Test
    public void commandActionExistsTest() {
        assertTrue("The command action doesn't exist.", factory.isActionValid("command"));
    }
    
    @Test
    public void fireActionExistsTest() {
        assertTrue("The fire action doesn't exist.", factory.isActionValid("fire"));
    }
    
    @Test
    public void giveActionExistsTest() {
        assertTrue("The give action doesn't exist.", factory.isActionValid("give"));
    }
    
    @Test
    public void kickActionExistsTest() {
        assertTrue("The kick action doesn't exist.", factory.isActionValid("kick"));
    }
    
    @Test
    public void killActionExistsTest() {
        assertTrue("The kill action doesn't exist.", factory.isActionValid("kill"));
    }
    
    @Test
    public void potionActionExistsTest() {
        assertTrue("The potion action doesn't exist.", factory.isActionValid("potion"));
    }
    
    @Test
    public void rocketActionExistsTest() {
        assertTrue("The rocket action doesn't exist.", factory.isActionValid("rocket"));
    }
    
    @Test
    public void slapActionExistsTest() {
        assertTrue("The slap action doesn't exist.", factory.isActionValid("slap"));
    }
    
    @Test
    public void smiteActionExistsTest() {
        assertTrue("The smite action doesn't exist.", factory.isActionValid("smite"));
    }
    
    @Test
    public void tptoActionExistsTest() {
        assertTrue("The tpto action doesn't exist.", factory.isActionValid("tpto"));
    }
}
