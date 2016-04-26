package com.craftyn.casinoslots.classes;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.entity.Player;

@SerializableAs(value="CasinoSlotsSlotMachineOwner")
public class SlotMachineOwner implements ConfigurationSerializable {
    private UUID uuid;
    private String name;
    
    public SlotMachineOwner(Player player) {
        this.uuid = player.getUniqueId();
        this.name = player.getName();
    }
    
    public SlotMachineOwner(UUID uuid, String name) { 
        this.uuid = uuid;
        this.name = name;
    }
    
    public UUID getUUID() {
        return this.uuid;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }
    
    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<String, Object>();
        
        map.put("uuid", this.uuid.toString());
        map.put("name", this.name);
        
        return map;
    }

    public static SlotMachineOwner deserialize(Map<String, Object> map) {
        return new SlotMachineOwner(UUID.fromString((String)map.get("uuid")), (String)map.get("name"));
    }
}
