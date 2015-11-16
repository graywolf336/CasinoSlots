package com.craftyn.casinoslots.classes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.actions.Action;

public class Type {
    private CasinoSlots plugin;
    private String name, itemCost = "0";
    private double cost = 0, createCost = 0;
    private ArrayList<String> reel;
    private Map<String, String> messages;
    private List<String> helpMessages;
    private Map<String, Reward> rewards;
    private MaterialData controllerData;
    
    public Type(CasinoSlots plugin, String name) {
        this.plugin = plugin;
        this.name = name;
    }

    // Initialize new type
    public Type(CasinoSlots plugin, String name, double cost, String itemCost, double createCost, ArrayList<String> reel, Map<String, String> messages, List<String> helpMessages, Map<String, Reward> rewards, MaterialData controllerData) {
        this.plugin = plugin;
        this.name = name;
        this.cost = cost;
        this.itemCost = itemCost;
        this.createCost = createCost;
        this.reel = reel;
        this.messages = messages;
        this.helpMessages = helpMessages;
        this.rewards = rewards;
        this.controllerData = controllerData;
    }

    /** Gets the name of this type. */
    public String getName() {
        return this.name;
    }

    /** Gets the cost to play. */
    public double getCost() {
        return this.cost;
    }
    
    /** Sets the cost to play. */
    public void setCost(double cost) {
        this.cost = cost;
    }

    /**
     * Gets the item that this type costs to use.
     * 
     * @return the item cost
     */
    public String getItemCost() {
        return this.itemCost;
    }
    
    /**
     * Sets the item that this slot costs to use.
     * 
     * @param itemCost the item to set as the cost
     */
    public void setItemCost(String itemCost) {
        this.itemCost = itemCost;
    }

    /** Gets the amount it takes to create this type. */
    public double getCreateCost() {
        return this.createCost;
    }
    
    /** Sets the amount it takes to create this type. */
    public void setCreateCost(double createCost) {
        this.createCost = createCost;
    }
    
    /**
     * Gets the controller's block data
     * 
     * @return the controller's data
     */
    public MaterialData getControllerData() {
        return this.controllerData;
    }
    
    /**
     * Sets the controller's block data.
     * 
     * @param data the data for the controller.
     */
    public void setControllerData(MaterialData data) {
        this.controllerData = data;
    }

    /** Gets the reel. */
    public ArrayList<String> getReel() {
        return this.reel;
    }
    
    /** Sets the reel. */
    public void setReel(ArrayList<String> reel) {
        this.reel = reel;
    }

    // Returns map of type messages
    public Map<String, String> getMessages() {
        return this.messages;
    }
    
    public void setMessages(Map<String, String> messages) {
        this.messages = messages;
    }

    // Returns help messages
    public List<String> getHelpMessages() {
        return this.helpMessages;
    }
    
    public void setHelpMessages(List<String> messages) {
        this.helpMessages = messages;
    }
    
    public void setRewards(Map<String, Reward> rewards) {
        this.rewards = rewards;
    }

    // Returns type reward of id
    public Reward getReward(String id) {
        return this.rewards.get(id);
    }

    public double sendRewards(List<String> results, Player player) {
        double won = 0;
        List<String> messagesSent = new ArrayList<String>();
        
        for(String r : results) {
            if(this.rewards.containsKey(r)) {
                Reward re = this.rewards.get(r);
                
                //Make sure we don't send the same message more than once anymore!
                if(!re.getMessage().isEmpty() && !messagesSent.contains(re.getMessage())) {
                    plugin.sendMessage(player, re.getMessage());
                    messagesSent.add(re.getMessage());
                }
                
                //Add the amount won to the total amount, so we give it all at once
                won += re.getMoney();
                
                for(Action a : re.getActions()) {
                    a.execute(this, re, player);
                }
            }
        }
        
        if (won < 0) {
            plugin.getEconomy().withdrawPlayer(player, Math.abs(won));
        } else {
            plugin.getEconomy().depositPlayer(player, won);
        }
        
        return won;
    }
}