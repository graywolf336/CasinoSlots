package com.craftyn.casinoslots.classes;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.configuration.serialization.SerializableAs;
import org.bukkit.util.NumberConversions;

@SerializableAs(value = "CasinoSlotsSimpleLocation")
public class SimpleLocation implements ConfigurationSerializable {
    private String world;
    private double x, y, z;
    private float yaw = 0, pitch = 0;

    /**
     * Creates a new {@link SimpleLocation} with all the detail provided from {@link Location}.
     *
     * @param location to convert to a SimpleLocation
     */
    public SimpleLocation(Location location) {
        this.world = location.getWorld().getName();
        this.x = location.getX();
        this.y = location.getY();
        this.z = location.getZ();
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
    }

    /**
     * Creates a new {@link SimpleLocation} with each detail provided separately.
     *
     * @param world as a string
     * @param x coordinate as a double
     * @param y coordinate as a double
     * @param z coordinate as a double
     */
    public SimpleLocation(String world, double x, double y, double z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Creates a new {@link SimpleLocation} with each detail provided separately.
     *
     * @param world as a string
     * @param x coordinate as a double
     * @param y coordinate as a double
     * @param z coordinate as a double
     * @param yaw as a float
     * @param pitch as a float
     */
    public SimpleLocation(String world, double x, double y, double z, float yaw, float pitch) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    /**
     * Returns the instance from Bukkit of the world this location is in.
     *
     * @return the {@link World}
     */
    public World getWorld() {
        return Bukkit.getWorld(this.world);
    }

    /**
     * Returns the name of the world this location is in.
     *
     * @return the name
     */
    public String getWorldName() {
        return this.world;
    }

    /**
     * Returns if the world name stored is a valid {@link World} on the server.
     *
     * @return whether the world exists or not
     */
    public boolean hasValidWorld() {
        return Bukkit.getWorld(this.world) != null;
    }

    /**
     * Gets the x coordinate.
     *
     * @return the x coord
     */
    public double getX() {
        return this.x;
    }

    /**
     * Gets the y coordinate.
     *
     * @return the y coord
     */
    public double getY() {
        return this.y;
    }

    /**
     * Gets the z coordinate.
     *
     * @return the z coord
     */
    public double getZ() {
        return this.z;
    }

    /**
     * Gets the yaw.
     *
     * @return the yaw
     */
    public float getYaw() {
        return this.yaw;
    }

    /**
     * Gets the pitch.
     *
     * @return the pitch
     */
    public float getPitch() {
        return this.pitch;
    }

    /**
     * Returns a new {@link Location} from this SimpleLocation.
     *
     * @return the {@link Location}
     */
    public Location getLocation() {
        return new Location(Bukkit.getWorld(this.world), this.x, this.y, this.z, this.yaw, this.pitch);
    }

    /**
     * Returns the {@link Block} from the {@link Location} this is pointed at.
     *
     * @return the {@link Block} at this location.
     */
    public Block getBlock() {
        return this.hasValidWorld() ? this.getWorld().getBlockAt((int) this.x, (int) this.y, (int) this.z) : null;
    }

    @Override
    public String toString() {
        return this.world + "," + this.x + "," + this.y + "," + this.z + "," + this.yaw + "," + this.pitch;
    }

    public String toBlockString() {
        return this.world + "," + (int) this.x + "," + (int) this.y + "," + (int) this.z + ",0,0";
    }

    public Map<String, Object> serialize() {
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("world", this.world);
        map.put("x", this.x);
        map.put("y", this.y);
        map.put("z", this.z);
        map.put("yaw", this.yaw);
        map.put("pitch", this.pitch);

        return map;
    }

    public static SimpleLocation deserialize(Map<String, Object> map) {
        return new SimpleLocation((String) map.get("world"), NumberConversions.toDouble(map.get("x")), NumberConversions.toDouble(map.get("y")), NumberConversions.toDouble(map.get("z")),
                NumberConversions.toFloat(map.get("yaw")), NumberConversions.toFloat(map.get("pitch")));
    }
}
