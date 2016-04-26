package com.craftyn.casinoslots.enums;

import java.util.List;

import com.craftyn.casinoslots.CasinoSlots;

public enum Settings {
    /** Gets the debugging config value. */
    DEBUG("system.debug"),
    UPDATE_CHECKING_ENABLED("system.update-checking.enabled"),
    UPDATE_CHECKING_CHANNEL("system.update-checking.channel"),
    UPDATE_CHECKING_TIME("system.update-checking.time"),
    
    SLOTS_TRACK_STATS("slots.track-statistics"),
    SLOTS_ALLOW_DIAGONAL_WINNINGS("slots.allow-diagonal-winnings"),
    SLOTS_ENABLE_PROTECTION("slots.enable-protection"),
    
    CHAT_USE_PREFIX("chat.use-prefix"),
    CHAT_PREFIX("chat.prefix"),
    CHAT_COLOR("chat.color");
    
    /*
        //TODO: Figure out the worldguard piece
        integrations.worldguard: false
        //TODO: Figure out the Towny piece
        integrations.towny.enabled: false
        integrations.towny.only-mayors: true
        integrations.towny.not-mayor: 'You must be a mayor to create a Casino Slot.'
        integrations.towny.only-towns: true
        integrations.towny.no-town: 'To create a slot you must be part of a town.'
        integrations.towny.not-plot-owner: 'You don't own the plot where that would be at, please make sure you are the owner and then try again.'
     */
    
    private static CasinoSlots pl;
    private String path;
    
    private Settings(String path) {
        this.path = path;
    }
    
    /**
     * Gets the value of the setting as a boolean.
     * 
     * @return the boolean value
     */
    public boolean asBoolean() {
        return pl.getConfig().getBoolean(path);
    }
    
    /**
     * Gets the value of the setting as an integer.
     * 
     * @return the integer value
     */
    public int asInt() {
        return pl.getConfig().getInt(path);
    }
    
    /**
     * Gets the value of the setting as a string.
     * 
     * @return the string value
     */
    public String asString() {
        return pl.getConfig().getString(path);
    }
    
    /**
     * Gets the value of the setting as a List of Strings.
     * 
     * @return the List of Strings value
     */
    public List<String> asStringList() {
        return pl.getConfig().getStringList(path);
    }
    
    /**
     * Sets the value of a property to the given object.
     * 
     * @param obj the object to set
     */
    public void setValue(Object obj) {
        pl.getConfig().set(path, obj);
    }
    
    /**
     * Sets the plugin instance to use to get the config.
     * 
     * @param plugin the {@link CasinoSlots} instance
     */
    public static void setPlugin(CasinoSlots plugin) {
        pl = plugin;
    }
    
    /**
     * Checks whether we are debugging or not.
     * 
     * @return whether we are debugging or not.
     */
    public static boolean inDebug() {
        return DEBUG.asBoolean();
    }
}
