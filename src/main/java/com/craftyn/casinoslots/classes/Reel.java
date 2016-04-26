package com.craftyn.casinoslots.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.material.MaterialData;

@SerializableAs(value="CasinoSlotsReel")
public class Reel implements ConfigurationSerializable {
    private MaterialData data;
    private int count;
    
    //Rewards
    private double moneyReward;
    private String messageReward;
    private List<String> actionRewards;
    
    public Reel() {
        this.messageReward = "";
        this.actionRewards = new ArrayList<String>();
    }
    
    public void setMaterialData(MaterialData data) {
        this.data = data;
    }
    
    public MaterialData getMaterialData() {
        return this.data;
    }
    
    public void setFrequency(int count) {
        this.count = count;
    }
    
    public int getFrequency() {
        return this.count;
    }
    
    public void setRewardMoney(double amount) {
        this.moneyReward = amount;
    }
    
    public double getRewardMoney() {
        return this.moneyReward;
    }
    
    public void setRewardMessage(String message) {
        this.messageReward = message;
    }
    
    public String getRewardMessage() {
        return this.messageReward;
    }
    
    public void setRewardActions(List<String> actions) {
        this.actionRewards = actions;
    }
    
    @SuppressWarnings("deprecation")
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("block_type", this.data.getItemType().toString().toLowerCase());
        map.put("block_data", String.valueOf(this.data.getData()));
        map.put("count", String.valueOf(this.count));
        map.put("reward_money", String.valueOf(this.moneyReward));
        map.put("reward_message", this.messageReward);
        map.put("reward_actions", this.actionRewards);

        return map;
    }
    
    @SuppressWarnings({ "deprecation", "unchecked" })
    public static Reel deserialize(Map<String, Object> map) {
        Reel reel = new Reel();
        
        Material type = Material.matchMaterial((String) map.get("block_type"));
        byte data = Byte.parseByte((String) map.get("block_data"));
        reel.setMaterialData(new MaterialData(type, data));
        
        int cCount = Integer.parseInt((String) map.get("count"));
        reel.setFrequency(cCount);
        
        double money = Double.parseDouble((String) map.get("reward_money"));
        reel.setRewardMoney(money);
        
        String message = (String) map.get("reward_message");
        reel.setRewardMessage(message);
        
        List<String> actions = (List<String>) map.get("reward_actions");
        reel.setRewardActions(actions);

        return reel;
    }
}
