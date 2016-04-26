package com.craftyn.casinoslots.actions;

import org.bukkit.entity.Player;

import com.craftyn.casinoslots.classes.Reward;
import com.craftyn.casinoslots.classes.SlotType;

/**
 * The interface for all actions which are to be used inside.
 *
 * @author graywolf336
 * @since 3.0.0
 * @version 1.0.0
 */
public interface IAction {
    /**
     * The name of the action, the user friendly name.
     *
     * @return name of the action
     */
    public String getName();

    /**
     * Determines if the action is valid or not.
     *
     * @return whether the action is valid or not.
     */
    public boolean isValid();

    /**
     * Executes/runs the action.
     *
     * @param type the {@link SlotType} this action is being run for
     * @param reward the {@link Reward} this action is being run for
     * @param player the {@link Player} this action is being run for
     * @return Whether the action executed successfully or not.
     */
    public boolean execute(SlotType type, Reward reward, Player player);
}
