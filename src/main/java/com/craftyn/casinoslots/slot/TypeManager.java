package com.craftyn.casinoslots.slot;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;

import org.bukkit.Material;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.material.MaterialData;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.classes.Reel;
import com.craftyn.casinoslots.classes.SlotType;
import com.craftyn.casinoslots.exceptions.TypesFolderException;
import com.craftyn.casinoslots.util.Util;

public class TypeManager {
    private HashMap<String, SlotType> types;
    
    public TypeManager(CasinoSlots plugin) throws TypesFolderException {
        this.types = new HashMap<String, SlotType>();
        this.loadAllTheTypes(plugin);
    }

    public void loadAllTheTypes(CasinoSlots plugin) throws TypesFolderException {
        if(!this.types.isEmpty()) {
            this.types = new HashMap<String, SlotType>();
        }

        File f = new File(plugin.getDataFolder() + File.separator + "types");

        if(Util.verifyDirectoryExists(f, "types")) {
            for (String name : f.list()) {
                if (name.startsWith(".")) {
                    continue;
                }

                File type = new File(f, name);
                if (type.isHidden() || type.isDirectory()) {
                    continue;
                }

                if (this.loadType(type)) {
                    plugin.debug("Successfully loaded " + name + "'s type!");
                } else {
                    plugin.error("Failed to load the type in the file: " + type.getPath());
                }
            }
        }else {
            throw new TypesFolderException();
        }

        //If the types folder is empty, let's fill it with the default types
        if(types.isEmpty()) {
            try {
                YamlConfiguration.loadConfiguration(new InputStreamReader(plugin.getResource("types/default.yml"))).save(new File(f, "default.yml"));
                loadType(new File(f, "default.yml"));
            } catch (IOException e) {
                e.printStackTrace();
                plugin.getLogger().severe("Failed to save the default type!");
            }

            try {
                YamlConfiguration.loadConfiguration(new InputStreamReader(plugin.getResource("types/action-example.yml"))).save(new File(f, "action-example.yml"));
                loadType(new File(f, "action-example.yml"));
            } catch (IOException e) {
                e.printStackTrace();
                plugin.getLogger().severe("Failed to save the default type!");
            }

            try {
                YamlConfiguration.loadConfiguration(new InputStreamReader(plugin.getResource("types/testing.yml"))).save(new File(f, "testing.yml"));
                loadType(new File(f, "testing.yml"));
            } catch (IOException e) {
                e.printStackTrace();
                plugin.getLogger().severe("Failed to save the default type!");
            }
        }
    }

    private boolean loadType(File file) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        if (!config.contains("type")) {
            return false;
        }

        try {
            SlotType t = (SlotType) config.get("type");
            this.types.put(t.getName(), t);
        } catch (Exception e) {
            return false;
        }

        return true;
    }

    public Collection<SlotType> getTypes() {
        return this.types.values();
    }

    public SlotType getType(String name) {
        return this.types.get(name);
    }

    public boolean isValidType(String name) {
        return this.types.containsKey(name);
    }

    public boolean saveTestingType(CasinoSlots plugin) throws IOException {
        SlotType type = new SlotType("testing");
        type.setCost(150.54);
        type.setCreateCost(1509849.99);
        type.setControllerData(new MaterialData(Material.NOTE_BLOCK));

        Reel diamond = new Reel();
        diamond.setMaterialData(new MaterialData(Material.DIAMOND_BLOCK));
        diamond.setFrequency(1);
        diamond.setRewardMessage("Go diamond go!");
        diamond.setRewardMoney(1500);
        
        Reel gold = new Reel();
        gold.setMaterialData(new MaterialData(Material.GOLD_BLOCK));
        gold.setFrequency(1);
        gold.setRewardActions(Arrays.asList("slap"));
        gold.setRewardMessage("Go gold forever!!");
        gold.setRewardMoney(1250);
        
        Reel iron = new Reel();
        iron.setMaterialData(new MaterialData(Material.IRON_BLOCK));
        iron.setFrequency(2);
        iron.setRewardActions(Arrays.asList("give IRON_FENCE 16 SILK_TOUCH:1 name:Silky_Iron_Fence lore:This_fence_can_be_used|to_get_some_lovely_&3grass!"));
        iron.setRewardMessage("IRON!!!!!!!");
        iron.setRewardMoney(1000);
        
        type.setReel(Arrays.asList(diamond, gold, iron));
        
        type.getMessages().setNoPermission("Sorry, you can't play this slot.");
        type.getMessages().setNoFunds("You can't afford to use this.");
        type.getMessages().setBeingUsed("This slot machine is currently in use.");
        type.getMessages().setLost("No luck this time.");
        type.getMessages().setStart("[cost] was removed from your account. Let's roll!");
        type.getMessages().getHelps().addAll(Arrays.asList("Instructions:",
                "    Get 3 in a row in order to win. [cost] per spin.",
                "    3 Diamond Blocks: 1500",
                "    3 Gold Blocks: 1250",
                "    3 Iron Blocks: 1000"));
        
        File f = new File(plugin.getDataFolder() + File.separator + "types", type.getName() + ".yml");
        YamlConfiguration typeFile = YamlConfiguration.loadConfiguration(f);
        typeFile.set("type", type);
        typeFile.save(f);
        
        this.types.put(type.getName(), type);
        
        return true;
    }
}
