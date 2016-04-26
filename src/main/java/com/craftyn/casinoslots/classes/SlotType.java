package com.craftyn.casinoslots.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;
import org.bukkit.material.MaterialData;

import com.craftyn.casinoslots.actions.Action;
import com.craftyn.casinoslots.util.Util;

/**
 * Represents the type a slot machine can be.
 * 
 * @author graywolf336
 * @version 3.0
 * @since 1.0
 */
@SerializableAs(value="CasinoSlotsSlotType")
public class SlotType implements ConfigurationSerializable {
    private String name, itemCost = "0";
    private double cost = 0, createCost = 0;
    private SlotMachineMessages messages;
    private Map<MaterialData, Reward> rewards;
    private MaterialData controllerData;
    private List<Reel> reel;
    
    public SlotType(String name) {
        this.name = name;
        this.messages = new SlotMachineMessages();
    }

    /**
     * Gets the name of this type.
     * 
     * @return name of the Type
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets the cost to play.
     * 
     * @return cost to play
     */
    public double getCost() {
        return this.cost;
    }
    
    /**
     * Sets the cost to play.
     * 
     * @param cost amount of money needed to play
     */
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

    /**
     * Gets the amount it takes to create this type.
     * 
     * @return cost to create
     */
    public double getCreateCost() {
        return this.createCost;
    }
    
    /**
     * Sets the amount it takes to create this type.
     * 
     * @param createCost amount of money needed to create
     */
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

    /**
     * Gets the reel.
     * 
     * @return the list of {@link Reel}'s which make up the reel.
     */
    public List<Reel> getReel() {
        return this.reel;
    }
    
    /**
     * Sets the reel.
     * 
     * @param reel the {@link Reel} we will be using
     */
    public void setReel(List<Reel> reel) {
        this.reel = reel;
    }
    
    /**
     * Gets the {@link SlotMachineMessages messages} for the slot machine. 
     * 
     * @return the {@link SlotMachineMessages} instance with all the messages.
     */
    public SlotMachineMessages getMessages() {
        return this.messages;
    }
    
    public Map<MaterialData, Reward> getRewards() {
        return this.rewards;
    }
    
    public void setRewards(Map<MaterialData, Reward> rewards) {
        this.rewards = rewards;
    }

    // Returns type reward of id
    public Reward getReward(String id) {
        return this.rewards.get(id);
    }
    
    public double getMaxPrize() {
        double max = 0.0;

        for(Reward r : rewards.values())
            if(r.getMoney() > max)
                max = r.getMoney();
                
        return max;
    }

    public double sendRewards(List<String> results, Player player) {
        double won = 0;
        List<String> messagesSent = new ArrayList<String>();
        
        for(String r : results) {
            if(this.rewards.containsKey(r)) {
                Reward re = this.rewards.get(r);
                
                //Make sure we don't send the same message more than once anymore!
                if(!re.getMessage().isEmpty() && !messagesSent.contains(re.getMessage())) {
                    //TODO: THIS
                    //plugin.sendMessage(player, re.getMessage());
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
            //TODO: THIS
            //plugin.getEconomy().withdrawPlayer(player, Math.abs(won));
        } else {
            //TODO: THIS
            //plugin.getEconomy().depositPlayer(player, won);
        }
        
        return won;
    }

    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<String, Object>();
        
        map.put("name", this.name);
        map.put("cost_usage", String.valueOf(this.cost));
        map.put("cost_create", String.valueOf(this.createCost));
        map.put("controller", Util.materialDataToString(this.controllerData));
        map.put("reel", this.reel);
        map.put("messages", this.messages);
        
        return map;
    }
    
    @SuppressWarnings("unchecked")
    public static SlotType deserialize(Map<String, Object> map) {
        SlotType t = new SlotType((String)map.get("name"));
        
        t.setCost(Double.valueOf((String)map.get("cost_usage")));
        t.setCreateCost(Double.valueOf((String)map.get("cost_create")));
        t.setControllerData(Util.parseMaterialDataFromString((String) map.get("controller")));
        t.setReel((List<Reel>)map.get("reel"));
        t.messages = (SlotMachineMessages)map.get("messages");
        
        return t;
    }
}