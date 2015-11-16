package com.craftyn.casinoslots.slot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.Material;
import org.bukkit.material.MaterialData;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.actions.Action;
import com.craftyn.casinoslots.actions.ActionFactory;
import com.craftyn.casinoslots.classes.Reward;
import com.craftyn.casinoslots.classes.Type;

public class TypeManager {
    private CasinoSlots plugin;
    private HashMap<String, Type> types;
    private ActionFactory actionFactory;

    // Initialize TypeData
    public TypeManager(CasinoSlots plugin) {
        this.plugin = plugin;
        this.types = new HashMap<String, Type>();
        this.actionFactory = plugin.getActionFactory();
    }

    // Returns a type
    public Type getType(String name) {
        return types.get(name);
    }

    // Returns collection of all types
    public Collection<Type> getTypes() {
        return this.types.values();
    }

    // Registers a new type
    public void addType(Type type) {
        String name = type.getName();
        types.put(name, type);

        plugin.getConfigData().config.set("types." + type.getName() +".cost", type.getCost());
        plugin.getConfigData().config.set("types." + type.getName() +".create-cost", type.getCreateCost());
        plugin.saveConfig();
    }

    public void removeType(String type) {
        types.remove(type);
        plugin.getConfigData().config.set("types." + type, null);
        plugin.saveConfig();
    }

    /** Provides a way to refresh the types information. */
    public void reloadTypes() {
        types.clear();
        loadTypes();
    }

    // Check if a type exists
    public boolean isType(String type) {
        return types.containsKey(type);
    }

    // Load all types into memory
    public void loadTypes() {
        this.types = new HashMap<String, Type>();
        Integer i = 0;

        if(plugin.getConfigData().config.isConfigurationSection("types")) {
            Set<String> types = plugin.getConfigData().config.getConfigurationSection("types").getKeys(false);
            if(!types.isEmpty()) {
                for(String name : types) {
                    if (!plugin.getConfigData().config.contains("types." + name + ".messages")) {
                        plugin.log("Please make sure your slots in the config file contains 'messages:'.");
                        //If there are no "messages", disables the plugin and forces them to check their config
                        plugin.error("[CasinoSlots]" + " PLEASE CHECK ONE OF YOUR CONFIG FILES");
                        plugin.disablePlugin();
                        return;
                    }else {
                        loadType(name);
                        i++;
                    }
                }
            }
        }

        plugin.log("Loaded " + i + " types.");
    }

    // Load type into memory
    private void loadType(String name) {
        String path = "types." + name +".";
        
        Type type = new Type(plugin, name);
        type.setCost(plugin.getConfig().getDouble(path + "cost"));
        type.setItemCost(plugin.getConfig().getString(path + "itemCost", "0"));
        type.setCreateCost(plugin.getConfig().getDouble(path + "create-cost", 100));
        type.setReel(getReel(name));
        type.setMessages(getMessages(name));
        type.setHelpMessages(plugin.getConfig().getStringList(path + "messages.help"));
        type.setRewards(getRewards(type));
        
        String controllerDefinition = plugin.getConfig().getString(path + "controller", "note_block");
        MaterialData controller;
        
        try {
            String[] controllerSplit = controllerDefinition.split(":");
            controller = new MaterialData(Material.matchMaterial(controllerSplit[0]));
            
            if(controllerSplit.length >= 2) {
                controller.setData(Byte.valueOf(controllerSplit[1]));
            }
        }catch(Exception e) {
            plugin.severe("Unable to load the custom controller definition for the slot type " + name + ". The following is not valid: " + controllerDefinition);
            controller = new MaterialData(Material.NOTE_BLOCK);
        }
        
        type.setControllerData(controller);
        this.types.put(name, type);
    }

    // Returns the parsed reel of a type
    private ArrayList<String> getReel(String type) {
        List<String> reel = plugin.getConfigData().config.getStringList("types." + type + ".reel");

        ArrayList<String> parsedReel = new ArrayList<String>();
        for(String m : reel) {
            String[] mSplit = m.split("\\,");
            int i = 1;
            if (mSplit.length == 3) {
                i = Integer.parseInt(mSplit[2]);
            }else {
                i = Integer.parseInt(mSplit[1]);
            }

            while(i > 0) {
                if (mSplit.length == 3) {
                    parsedReel.add(mSplit[0] + ":" + mSplit[1]);
                }else {
                    parsedReel.add(mSplit[0]);
                }
                i--;
            }
        }
        return parsedReel;
    }

    // Returns Map of all rewards for this type
    public Map<String, Reward> getRewards(Type type) {
        Set<String> ids = plugin.getConfigData().config.getConfigurationSection("types." + type.getName() +".rewards").getKeys(false);
        Map<String, Reward> rewards = new HashMap<String, Reward>();

        for(String itemId : ids) {
            int id = 1; //setting this to 1 just in case something is wrong
            byte data = 0;
            String[] itemSplit = itemId.split("\\,");
            if (itemSplit.length == 2) {
                id = Integer.parseInt(itemSplit[0]);
                data = Byte.parseByte(itemSplit[1]);
            }else {
                id = Integer.parseInt(itemSplit[0]);
            }

            rewards.put(id + ":" + data, getReward(type, itemId));
        }
        return rewards;
    }
    
    // Returns reward of id
    private Reward getReward(Type type, String itemId) {
        String path = "types." + type.getName() + ".rewards." + itemId + ".";

        String message = plugin.getConfigData().config.getString(path + "message");
        double money = plugin.getConfigData().config.getDouble(path + "money", 0.0);
        List<Action> action = new ArrayList<Action>();

        if(plugin.getConfigData().config.isSet(path + "action")) {
            if(plugin.getConfigData().config.isList(path + "action")) {
                List<String> configActions = plugin.getConfigData().config.getStringList(path + "action");
                action = this.getActions(type, configActions);
            }else {
                String a = plugin.getConfigData().config.getString(path + "action");
                action = this.getActions(type, Arrays.asList(a));
            }
        }

        return new Reward(message, money, action);
    }
    
    private List<Action> getActions(Type type, List<String> actions) {
        List<Action> created = new ArrayList<Action>();
        
        for(String s : actions) {
            String[] split = s.split(" ");
            String[] args = new String[split.length - 1];
            System.arraycopy(split, 1, args, 0, args.length);
            
            try{
                Action a = this.actionFactory.getConstructedAction(split[0], plugin, type, args);
                created.add(a);
            }catch(Exception e) {
                plugin.getLogger().severe(e.getClass().getSimpleName() + " occured causing us to not be able to load the action '" + split[0] + "' due to the error: " + e.getMessage());
            }
        }
        
        return created;
    }

    // Returns map of messages
    private HashMap<String, String> getMessages(String type) {
        HashMap<String, String> messages = new HashMap<String, String>();
        Double cost = plugin.getConfigData().config.getDouble("types." + type +".cost");

        messages.put("noPermission", plugin.getConfigData().config.getString("types." + type +".messages.insufficient-permission", "You don't have permission to use this slot."));
        messages.put("noFunds", plugin.getConfigData().config.getString("types." + type +".messages.insufficient-funds", "You can't afford to use this."));
        messages.put("inUse", plugin.getConfigData().config.getString("types." + type +".messages.in-use", "This slot machine is already in use."));
        messages.put("noWin", plugin.getConfigData().config.getString("types." + type +".messages.no-win", "No luck this time."));
        messages.put("start", plugin.getConfigData().config.getString("types." + type +".messages.start", "[cost] removed from your account. Lets roll!"));

        // Parse shortcodes
        for(Map.Entry<String, String> entry : messages.entrySet()) {
            String message = entry.getValue();
            String key = entry.getKey();
            message = message.replaceAll("\\[cost\\]", "" + cost);
            messages.put(key, message);
        }
        return messages;
    }

    // Returns value of the highest money reward
    public Double getMaxPrize(Type type) {
        Map<String, Reward> rewards = getRewards(type);//TODO: Use the loaded types!!!!!!!!!!!!!!!!!!!!!!!!!!
        Double max = 0.0;

        for(Map.Entry<String, Reward> entry : rewards.entrySet()) {
            Reward reward = entry.getValue();
            Double money = reward.getMoney();
            if(money > max) {
                max = money;
            }
        }
        return max;
    }

    /**
     * Sets the item cost that this type also costs, in addition to economy money and saves the config.
     * 
     * @param type the {@link Type} of machine
     * @param itemCost the item cost
     */
    public void setItemCost(Type type, String itemCost) {
        String path = "types." + type.getName() + ".itemCost";
        plugin.getConfigData().config.set(path, itemCost);
        plugin.saveConfig();
    }

    public void newType(String name) {
        String path = "types." + name + ".";
        List<String> reel = Arrays.asList("42,10", "41,5", "57,2");
        List<String> help = Arrays.asList("Instructions:", "Get 3 in a row to win.", "3 iron blocks: $250", "3 gold blocks: $500", "3 diamond blocks: $1200");

        plugin.getConfigData().config.set(path + "cost", 100);
        plugin.getConfigData().config.set(path + "create-cost", 1000);
        plugin.getConfigData().config.set(path + "reel", reel);

        path = path + "rewards.";

        plugin.getConfigData().config.set(path + "42.message", "Winner!");
        plugin.getConfigData().config.set(path + "42.money", 250);
        plugin.getConfigData().config.set(path + "41.message", "Winner!");
        plugin.getConfigData().config.set(path + "41.money", 500);
        plugin.getConfigData().config.set(path + "57.message", "Winner!");
        plugin.getConfigData().config.set(path + "57.money", 1200);

        path = "types." + name + ".messages.";

        plugin.getConfigData().config.set(path + "insufficient-funds", "You can't afford to use this.");
        plugin.getConfigData().config.set(path + "in-use", "This slot machine is already in use.");
        plugin.getConfigData().config.set(path + "no-win", "No luck this time.");
        plugin.getConfigData().config.set(path + "start", "[cost] removed from your account. Let's roll!");
        plugin.getConfigData().config.set(path + "help", help);

        plugin.saveConfig();
        loadType(name);
    }
}