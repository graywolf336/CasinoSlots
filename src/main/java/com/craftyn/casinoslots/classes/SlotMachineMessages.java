package com.craftyn.casinoslots.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;

@SerializableAs(value="CasinoSlotsSlotMachineMessages")
public class SlotMachineMessages implements ConfigurationSerializable {
    private List<String> helpMessages;
    private String noPermission = "";
    private String noFunds = "";
    private String beingUsed = "";
    private String lost = "";
    private String start = "";
    
    public SlotMachineMessages() {
        this.helpMessages = new ArrayList<String>();
    }
    
    /**
     * Gets the "No Permission" message.
     * 
     * @return the message
     */
    public String getNoPermission() {
        return this.noPermission;
    }
    
    /**
     * Sets the "No Permission" message.
     * 
     * @param message the message to set as
     */
    public void setNoPermission(String message) {
        this.noPermission = message;
    }
    
    /**
     * Gets the "No Funds" message.
     * 
     * @return the message
     */
    public String getNoFunds() {
        return this.noFunds;
    }
    
    /**
     * Sets the "No Funds" message.
     * 
     * @param message the message to set as
     */
    public void setNoFunds(String message) {
        this.noFunds = message;
    }
    
    /**
     * Gets the "Being Used" message.
     * 
     * @return the message
     */
    public String getBeingUsed() {
        return this.beingUsed;
    }
    
    /**
     * Sets the "Being Used" message.
     * 
     * @param message the message to set as
     */
    public void setBeingUsed(String message) {
        this.beingUsed = message;
    }
    
    /**
     * Gets the "Lost" message.
     * 
     * @return the message
     */
    public String getLost() {
        return this.lost;
    }
    
    /**
     * Sets the "Lost" message.
     * 
     * @param message the message to set as
     */
    public void setLost(String message) {
        this.lost = message;
    }
    
    /**
     * Gets the "Start" message.
     * 
     * @return the message
     */
    public String getStart() {
        return this.start;
    }
    
    /**
     * Sets the "Start" message.
     * 
     * @param message the message to set as
     */
    public void setStart(String message) {
        this.start = message;
    }
    
    /**
     * Gets the "Help" messages.
     * 
     * @return the help messages
     */
    public List<String> getHelps() {
        return this.helpMessages;
    }
    
    /**
     * Sets the "Help" messages.
     * 
     * @param messages the list of help messages.
     */
    public void setHelps(List<String> messages) {
        this.helpMessages = messages;
    }
    
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("no_permission", this.noPermission);
        map.put("player_cant_afford", this.noFunds);
        map.put("being_used", this.beingUsed);
        map.put("lost", this.lost);
        map.put("start", this.start);
        map.put("help", this.helpMessages);
        
        return map;
    }
    
    @SuppressWarnings("unchecked")
    public static SlotMachineMessages deserialize(Map<String, Object> map) {
        SlotMachineMessages msgs = new SlotMachineMessages();
        
        msgs.setNoPermission((String)map.get("no_permission"));
        msgs.setNoFunds((String)map.get("player_cant_afford"));
        msgs.setBeingUsed((String)map.get("being_used"));
        msgs.setLost((String)map.get("lost"));
        msgs.setStart((String)map.get("start"));
        msgs.setHelps((List<String>)map.get("help"));
        
        return msgs;
    }
}
