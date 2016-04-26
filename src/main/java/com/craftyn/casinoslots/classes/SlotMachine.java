package com.craftyn.casinoslots.classes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.material.MaterialData;

import com.craftyn.casinoslots.CasinoSlotsStaticAPI;

/**
 * Represents a slot machine.
 *
 * @author graywolf336
 * @version 3.0.0
 * @since 1.0.0
 */
@SerializableAs(value = "CasinoSlotsSlotMachine")
public class SlotMachine implements ConfigurationSerializable {
    private String name, typeName;
    private SlotMachineOwner owner;
    private SimpleLocation controller;
    private List<SimpleLocation> blocks;

    public SlotMachine(String name) {
        this.name = name;
        this.blocks = new ArrayList<SimpleLocation>();
    }

    /**
     * Sets the name of the {@link SlotMachine}.
     *
     * @param name the new name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the name of this {@link SlotMachine}.
     *
     * @return the name
     */
    public String getName() {
        return this.name;
    }

    /**
     * Sets the {@link SlotType} of this {@link SlotMachine}.
     *
     * @param type the {@link SlotType} to set.
     */
    public void setType(SlotType type) {
        this.typeName = type.getName();
    }

    public SlotType getType() {
        return CasinoSlotsStaticAPI.getTypeManager().getType(this.typeName);
    }

    /**
     * Sets the name of the type this {@link SlotMachine} is.
     *
     * @param name the name of the {@link SlotType} to set.
     */
    public void setTypeName(String name) {
        this.typeName = name;
    }

    /**
     * Gets the name of the {@link SlotType} this SlotMachine is.
     *
     * @return the name of the {@link SlotType}
     */
    public String getTypeName() {
        return this.typeName;
    }

    /**
     * Sets the {@link SlotMachineOwner} for this {@link SlotMachine}.
     *
     * @param owner the {@link SlotMachineOwner owner} of this
     *            {@link SlotMachine machine}
     */
    public void setOwner(SlotMachineOwner owner) {
        this.owner = owner;
    }

    /**
     * Gets the {@link SlotMachineOwner} for this {@link SlotMachine}.
     *
     * @return the {@link SlotMachineOwner owner} of this {@link SlotMachine
     *         machine}
     */
    public SlotMachineOwner getOwner() {
        return this.owner;
    }

    /**
     * Sets the {@link SimpleLocation} of the controller block.
     *
     * @param location the location where the controller is
     */
    public void setControllerLocation(SimpleLocation location) {
        this.controller = location;
    }

    /**
     * Sets the {@link Location} of the controller block, also updates the block
     * to be the {@link SlotType}'s set block.
     *
     * @param location the location where the controller is
     */
    @SuppressWarnings("deprecation")
    public void setControllerLocation(Location location) {
        this.controller = new SimpleLocation(location);

        MaterialData d = this.getType().getControllerData();
        this.controller.getBlock().setTypeIdAndData(d.getItemTypeId(), d.getData(), false);
    }

    /**
     * Gets the {@link Location} of the controller block.
     *
     * @return the {@link Location} of the controller
     */
    public Location getControllerLocation() {
        return this.controller.getLocation();
    }

    /**
     * Gets the {@link Block} at the controller's position.
     *
     * @return the controller {@link Block}
     */
    public Block getController() {
        return this.controller == null ? null : this.controller.getBlock();
    }

    /**
     * Checks if this {@link SlotMachine} is a valid machine.
     *
     * @return whether the machine is valid or not.
     */
    public boolean isValid() {
        if (this.name == null || this.name.isEmpty())
            return false;
        else if (this.typeName == null || this.typeName.isEmpty())
            return false;
        else if (!CasinoSlotsStaticAPI.getTypeManager().isValidType(this.typeName))
            return false;
        else if (this.owner == null)
            return false;
        else if (this.controller == null || this.controller.hasValidWorld())
            return false;

        return true;
    }

    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", this.name);
        map.put("type_name", this.typeName);
        map.put("owner", this.owner);
        map.put("controller", this.controller);
        map.put("blocks", this.blocks);

        return map;
    }

    public static SlotMachine deserialize(Map<String, Object> map) {
        SlotMachine machine = new SlotMachine((String) map.get("name"));

        machine.setTypeName((String) map.get("type_name"));
        machine.setOwner((SlotMachineOwner) map.get("owner"));
        machine.setControllerLocation((SimpleLocation) map.get("controller"));
        //TODO: Blocks

        return machine;
    }
}
