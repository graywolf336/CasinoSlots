package com.craftyn.casinoslots.classes;

import java.util.ArrayList;
import java.util.List;

import com.craftyn.casinoslots.actions.Action;

/**
 * Representation of a reward to be given out when a slot machine is won.
 *
 * @author graywolf336
 * @since 1.0.0
 * @version 2.5.0
 */
public class Reward {
    private String message = "";
    private double money = 0;
    private List<Action> actions = new ArrayList<Action>();

    public Reward(String message, double money, List<Action> actions) {
        if (message != null)
            this.message = message;

        this.money = money;

        if (actions != null)
            this.actions = actions;
    }

    /**
     * Checks whether the message is there or not.
     *
     * @return whether there is a message
     */
    public boolean hasMessage() {
        return !message.isEmpty();
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
     * Checks whether there is money to give or not.
     *
     * @return whether there is money
     */
    public boolean hasMoney() {
        return this.money != 0;
    }

    /**
     * The amount of money to give the player who won, it will be zero if none
     * and can be negative.
     *
     * @return amount of money to give to the player
     */
    public double getMoney() {
        return this.money;
    }

    /**
     * Checks whether there are any actions to execute or not.
     *
     * @return whether there are actions or not
     */
    public boolean hasActions() {
        return !this.actions.isEmpty();
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
