package com.craftyn.casinoslots.classes;

import java.util.ArrayList;
import java.util.List;

import com.craftyn.casinoslots.actions.Action;

public class Reward {
    private String message = "";
    private double money = 0;
    private List<Action> actions = new ArrayList<Action>();

    public Reward(String message, double money, List<Action> actions) {
        if(message != null)
            this.message = message;
        
        if(actions != null)
            this.actions = actions;

        this.money = money;
    }

    // Returns reward message
    public String getMessage() {
        return this.message;
    }

    // Returns reward money
    public double getMoney() {
        return this.money;
    }

    // Returns reward actions
    public List<Action> getActions() {
        return this.actions;
    }
}