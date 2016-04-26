package com.craftyn.casinoslots.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.material.MaterialData;
import org.bukkit.util.NumberConversions;

@SerializableAs(value = "CasinoSlotsReel")
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
        map.put("block_data", this.data.getData());
        map.put("count", this.count);
        map.put("reward_money", this.moneyReward);
        map.put("reward_message", this.messageReward);
        map.put("reward_actions", this.actionRewards);

        return map;
    }

    @SuppressWarnings({ "deprecation", "unchecked" })
    public static Reel deserialize(Map<String, Object> map) {
        Reel reel = new Reel();

        Material type = Material.matchMaterial((String) map.get("block_type"));
        byte data = NumberConversions.toByte(map.get("block_data"));
        reel.setMaterialData(new MaterialData(type, data));

        reel.setFrequency(NumberConversions.toInt(map.get("count")));
        reel.setRewardMoney(NumberConversions.toDouble(map.get("reward_money")));
        reel.setRewardMessage((String) map.get("reward_message"));
        reel.setRewardActions((List<String>) map.get("reward_actions"));

        return reel;
    }
}
