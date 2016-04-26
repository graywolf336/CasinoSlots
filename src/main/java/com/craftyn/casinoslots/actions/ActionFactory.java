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
import com.craftyn.casinoslots.classes.SlotType;
import com.craftyn.casinoslots.exceptions.ActionClassConstructorParameterNotExpectedTypeException;
import com.craftyn.casinoslots.exceptions.ActionLoadingException;
import com.craftyn.casinoslots.exceptions.ClassConstructorIsntTwoException;
import com.craftyn.casinoslots.exceptions.ClassDoesntExtendActionException;
import com.craftyn.casinoslots.exceptions.UnknownActionException;

/**
 * The action factory which handles all the registering and getting of actions for rewards.
 *
 * @author graywolf336
 * @since 3.0.0
 * @version 1.0.0
 */
public class ActionFactory {
    private static CasinoSlots plugin;
    private HashMap<String, Constructor<?>> actions;

    public ActionFactory(CasinoSlots plug) throws ClassDoesntExtendActionException, ClassConstructorIsntTwoException, ActionClassConstructorParameterNotExpectedTypeException {
        plugin = plug;
        this.actions = new HashMap<String, Constructor<?>>();
        this.loadActions();
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
     * @param type The instance of the {@link SlotType} this action is used in.
     * @param args The arguments to be passed into the {@link Action}.
     * @return The constructed {@link Action} instance.
     * @throws UnknownActionException Thrown if the action doesn't exist.
     * @throws InstantiationException Thrown if the action's class couldn't be constructed.
     * @throws IllegalAccessException Thrown if the action's constructor is private.
     * @throws IllegalArgumentException Thrown if the action's constructor isn't defined correctly.
     * @throws InvocationTargetException Thrown if the action's constructor throws an exception which we don't know about
     * @throws ActionLoadingException Thrown when the action can't load successfully, couldn't parse the arguments is generally what it means.
     */
    public Action getConstructedAction(String name, SlotType type, String... args) throws UnknownActionException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, ActionLoadingException {
        Constructor<?> c = this.getAction(name);

        try {
            return (Action) c.newInstance(plugin, type, args);
        } catch (InvocationTargetException e) {
            if (e.getTargetException() instanceof ActionLoadingException) {
                throw (ActionLoadingException) e.getTargetException();
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

    private void loadActions() throws ClassDoesntExtendActionException, ClassConstructorIsntTwoException, ActionClassConstructorParameterNotExpectedTypeException {
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
     * @return The first constructor of the class, assumes it is "CasinoSlot plugin, String... args".
     * @throws ClassDoesntExtendActionException Thrown when the class doesn't implement {@link IAction}.
     * @throws ClassConstructorIsntTwoException Thrown when the class's first constructor doesn't contain two parameters.
     * @throws ActionClassConstructorParameterNotExpectedTypeException Thrown when one of the parameters in the constructor isn't the expected type.
     */
    public Constructor<?> addAction(String name, Class<?> actionClass) throws ClassDoesntExtendActionException, ClassConstructorIsntTwoException, ActionClassConstructorParameterNotExpectedTypeException {
        if (!Action.class.isAssignableFrom(actionClass))
            throw new ClassDoesntExtendActionException(actionClass.getName() + "(" + name + ")");

        Constructor<?> constructor = actionClass.getConstructors()[0];
        if (constructor.getParameterCount() != 2)
            throw new ClassConstructorIsntTwoException(actionClass.getName() + "(" + name + ")", constructor.getParameterCount());

        if (!constructor.getParameterTypes()[0].getName().equals("com.craftyn.casinoslots.CasinoSlots"))
            throw new ActionClassConstructorParameterNotExpectedTypeException(actionClass.getName() + "(" + name + ")", 0);

        if (!constructor.getParameterTypes()[1].getName().equals("[Ljava.lang.String;"))
            throw new ActionClassConstructorParameterNotExpectedTypeException(actionClass.getName() + "(" + name + ")", 2);

        return actions.put(name, actionClass.getConstructors()[0]);
    }
}
