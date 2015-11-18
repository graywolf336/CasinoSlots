package com.craftyn.casinoslots.classes;

import java.util.ArrayList;
import java.util.List;

import com.craftyn.casinoslots.actions.Action;

/**
 * Representation of a reward to be given out when a slot machine is won.
 *
 * @author graywolf336
 * @since 1.0.0
 * @version 2.0.0
 */
public class Reward {
    private String message = "";
    private double money = 0;
    private List<Action> actions = new ArrayList<Action>();

    public Reward(String message, double money, List<Action> actions) {
        if (message != null)
            this.message = message;

        if (actions != null)
            this.actions = actions;

        this.money = money;
    }

    /**
     * The message to send to the player who won, it can be empty but not null.
     *
     * @return the message to send
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * The amount of money to give the player who won, it will be zero if none and can be negative.
     *
     * @return amount of money to give to the player
     */
    public double getMoney() {
        return this.money;
    }

    /**
     * The list of actions to be executed when this reward is being given out.
     *
     * @return list of actions to be ran
     */
    public List<Action> getActions() {
        return this.actions;
    }
}
