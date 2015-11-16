package com.craftyn.casinoslots.actions;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.actions.impl.AddXpAction;
import com.craftyn.casinoslots.actions.impl.AddXpLevelAction;
import com.craftyn.casinoslots.actions.impl.BroadcastAction;
import com.craftyn.casinoslots.actions.impl.CommandAction;
import com.craftyn.casinoslots.actions.impl.FireAction;
import com.craftyn.casinoslots.actions.impl.GiveAction;
import com.craftyn.casinoslots.actions.impl.KickAction;
import com.craftyn.casinoslots.actions.impl.KillAction;
import com.craftyn.casinoslots.actions.impl.PotionAction;
import com.craftyn.casinoslots.actions.impl.RocketAction;
import com.craftyn.casinoslots.actions.impl.SlapAction;
import com.craftyn.casinoslots.actions.impl.SmiteAction;
import com.craftyn.casinoslots.actions.impl.TpToAction;
import com.craftyn.casinoslots.classes.Type;
import com.craftyn.casinoslots.exceptions.ActionClassConstructorParameterNotExpectedTypeException;
import com.craftyn.casinoslots.exceptions.ActionLoadingException;
import com.craftyn.casinoslots.exceptions.ClassConstructorIsntThreeException;
import com.craftyn.casinoslots.exceptions.ClassDoesntExtendActionException;
import com.craftyn.casinoslots.exceptions.UnknownActionException;

public class ActionFactory {
    private HashMap<String, Constructor<?>> actions;

    public ActionFactory() throws ClassDoesntExtendActionException, ClassConstructorIsntThreeException, ActionClassConstructorParameterNotExpectedTypeException {
        actions = new HashMap<String, Constructor<?>>();
        loadActions();
    }

    /**
     * Gets an instance of an action, throwing an exception if not found.
     *
     * @param name The name of the action to get.
     * @return The instance of the action.
     * @throws UnknownActionException Thrown if no action by that name is found, use {@link #isActionValid(String)}.
     */
    public Constructor<?> getAction(String name) throws UnknownActionException {
        if (!actions.containsKey(name))
            throw new UnknownActionException(name + " is not a valid action.");

        return actions.get(name);
    }
    
    /**
     * Returns a constructed {@link Action}.
     * 
     * @param name The name of the action.
     * @param plugin The instance of the {@link CasinoSlots} plugin.
     * @param type The instance of the {@link Type} this action is used in.
     * @param args The arguments to be passed into the {@link Action}.
     * @return The constructed {@link Action} instance.
     * @throws UnknownActionException Thrown if the action doesn't exist.
     * @throws InstantiationException Thrown if the action's class couldn't be constructed.
     * @throws IllegalAccessException Thrown if the action's constructor is private.
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     * @throws ActionLoadingException 
     */
    public Action getConstructedAction(String name, CasinoSlots plugin, Type type, String... args) throws UnknownActionException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ActionLoadingException {
        Constructor<?> c = this.getAction(name);
        
        try {
            return (Action)c.newInstance(plugin, type, args);
        }catch(InvocationTargetException e) {
            if(e.getTargetException() instanceof ActionLoadingException) {
                throw (ActionLoadingException)e.getTargetException();
            }
            
            throw e;
        }
    }

    /**
     * Determines if the action is valid.
     *
     * @param name The name of the action to get.
     * @return Whether the action is valid.
     */
    public boolean isActionValid(String name) {
        return actions.containsKey(name);
    }

    private void loadActions() throws ClassDoesntExtendActionException, ClassConstructorIsntThreeException, ActionClassConstructorParameterNotExpectedTypeException {
        addAction("addxp", AddXpAction.class);
        addAction("addxplvl", AddXpLevelAction.class);
        addAction("broadcast", BroadcastAction.class);
        addAction("command", CommandAction.class);
        addAction("fire", FireAction.class);
        addAction("give", GiveAction.class);
        addAction("kick", KickAction.class);
        addAction("kill", KillAction.class);
        addAction("potion", PotionAction.class);
        addAction("rocket", RocketAction.class);
        addAction("slap", SlapAction.class);
        addAction("smite", SmiteAction.class);
        addAction("tpto", TpToAction.class);
    }

    /**
     * Adds an action to the factory's list, allowing any type to use it.
     *
     * @param name the name of the action, what is prepended on the actions array on a type.
     * @param actionClass The class for the action.
     * @return The first constructor of the class, assumes it is "CasinoSlot plugin, Type type, String... args".
     * @throws ClassDoesntExtendActionException Thrown when the class doesn't implement {@link IAction}.
     * @throws ClassConstructorIsntThreeException Thrown when the class's first constructor doesn't contain three parameters.
     * @throws ActionClassConstructorParameterNotExpectedTypeException Thrown when one of the parameters in the constructor isn't the expected type.
     */
    public Constructor<?> addAction(String name, Class<?> actionClass) throws ClassDoesntExtendActionException, ClassConstructorIsntThreeException, ActionClassConstructorParameterNotExpectedTypeException {
        if (!Action.class.isAssignableFrom(actionClass))
            throw new ClassDoesntExtendActionException(actionClass.getName() + "(" + name + ")");

        Constructor<?> constructor = actionClass.getConstructors()[0];
        if(constructor.getParameterCount() != 3)
            throw new ClassConstructorIsntThreeException(actionClass.getName() + "(" + name + ")");
        
        if(!constructor.getParameterTypes()[0].getName().equals("com.craftyn.casinoslots.CasinoSlots"))
            throw new ActionClassConstructorParameterNotExpectedTypeException(actionClass.getName() + "(" + name + ")", 0);
        
        if(!constructor.getParameterTypes()[1].getName().equals("com.craftyn.casinoslots.classes.Type"))
            throw new ActionClassConstructorParameterNotExpectedTypeException(actionClass.getName() + "(" + name + ")", 1);
        
        if(!constructor.getParameterTypes()[2].getName().equals("[Ljava.lang.String;"))
            throw new ActionClassConstructorParameterNotExpectedTypeException(actionClass.getName() + "(" + name + ")", 2);
        
        return actions.put(name, actionClass.getConstructors()[0]);
    }
}
