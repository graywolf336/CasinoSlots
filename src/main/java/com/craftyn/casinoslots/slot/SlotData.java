package com.craftyn.casinoslots.slot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import com.craftyn.casinoslots.CasinoSlots;
import com.google.common.collect.Sets;

public class SlotData {
    private CasinoSlots plugin;
    private HashMap<String, SlotMachine> slots = new HashMap<String, SlotMachine>();

    private HashMap<String, SlotMachine> creatingSlots;
    private HashMap<String, SlotMachine> placingController;
    private HashMap<String, SlotMachine> punchingSign;

    public SlotData(CasinoSlots plugin) {
        this.plugin = plugin;

        this.creatingSlots = new HashMap<String, SlotMachine>();
        this.placingController = new HashMap<String, SlotMachine>();
        this.punchingSign = new HashMap<String, SlotMachine>();
    }

    // Returns a slot machine
    public SlotMachine getSlot(String name) {
        return this.slots.get(name);
    }

    // Returns collection of all slot machines
    public Collection<SlotMachine> getSlots() {
        return this.slots.values();
    }

    /**
     * Returns the amount of slots there are.
     * 
     * @return The number of slots.
     */
    public int getAmountofSlots() {
        return this.slots.size();
    }

    /**
     * Adds a slot machine to our storage.
     * 
     * @param slot machine to add.
     */
    public void addSlot(SlotMachine slot) {
        plugin.debug("Adding a slot machine: " + slot.getName());
        this.slots.put(slot.getName(), slot);
    }

    /**
     * Checks if the given string is a valid slot machine.
     * 
     * @param name of the slot machine.
     * @return true if it is valid, false if otherwise
     */
    public boolean isSlot(String name) {
        return this.slots.containsKey(name);
    }

    /**
     * Removes the slot machine from our records and deletes the blocks associated with it.
     * 
     * @param slot machine to remove
     */
    public void removeSlot(SlotMachine slot) {
        plugin.debug("Removing the slot machine: " + slot.getName());
        for(Block b : slot.getBlocks())
            b.setType(Material.AIR);

        this.slots.remove(slot.getName());
        slot.getController().setType(Material.AIR);
        plugin.configData.slots.set("slots." + slot.getName(), null);
        plugin.configData.saveSlots();
    }

    /**
     * Provides a way to clear the loaded slots and then load them again from the config.
     */
    public void reloadSlots() {
        slots.clear();
        loadSlots();
    }


    // Loads all slot machines into memory
    public void loadSlots() {
        int i = 0;
        this.slots = new HashMap<String, SlotMachine>();
        if(plugin.configData.slots.isConfigurationSection("slots")) {
            Set<String> slots = plugin.configData.slots.getConfigurationSection("slots").getKeys(false);
            if(!slots.isEmpty()) {
                for(String name : slots) {
                    loadSlot(name);
                    i++;
                }
            }
        }

        plugin.log("Loaded " + i + " slot machines.");
    }

    // Writes slot machine data to disk
    public void saveSlot(SlotMachine slot) {

        String path = "slots." + slot.getName() + ".";
        ArrayList<String> xyz = new ArrayList<String>();

        for(Block b : slot.getBlocks()) {
            xyz.add(b.getX() + "," + b.getY() + "," + b.getZ());
        }

        Block con = slot.getController();
        String cXyz = con.getX() + "," + con.getY() + "," + con.getZ();

        Block sign = slot.getSign();
        String sXyz;
        if (sign == null) {
            sXyz = null;
        }else {
            sXyz = sign.getX() + "," + sign.getY() + "," + sign.getZ();
        }

        plugin.configData.slots.set(path + "name", slot.getName());
        plugin.configData.slots.set(path + "type", slot.getType());
        plugin.configData.slots.set(path + "owner", slot.getOwner());
        plugin.configData.slots.set(path + "world", slot.getWorld());
        plugin.configData.slots.set(path + "sign", sXyz);
        plugin.configData.slots.set(path + "managed", slot.isManaged());
        plugin.configData.slots.set(path + "funds", slot.getFunds());
        plugin.configData.slots.set(path + "item", slot.isItem());
        plugin.configData.slots.set(path + "itemID", slot.getItem());
        plugin.configData.slots.set(path + "itemAmt", slot.getItemAmount());
        plugin.configData.slots.set(path + "controller", cXyz);
        plugin.configData.slots.set(path + "location", xyz);


        plugin.configData.saveSlots();
    }

    // Loads a slot machine into memory
    private void loadSlot(String name) {

        String path = "slots." + name + ".";

        String type = plugin.configData.slots.getString(path + "type");
        String owner = plugin.configData.slots.getString(path + "owner");
        String world = plugin.configData.slots.getString(path + "world");
        Boolean managed = plugin.configData.slots.getBoolean(path + "managed");
        Double funds = plugin.configData.slots.getDouble(path + "funds");
        Boolean item = plugin.configData.slots.getBoolean(path + "item", false);
        int itemID = plugin.configData.slots.getInt(path + "itemID", 0);
        int itemAmt = plugin.configData.slots.getInt(path + "itemAmt", 0);
        ArrayList<Block> blocks = getBlocks(name);
        Block controller = getController(name);
        Block sign = getSign(name);

        //Get the chunks
        String rChunk = getRchunk(blocks);
        String cChunk = getCchunk(controller);

        SlotMachine slot = new SlotMachine(plugin, name, type, owner, world, rChunk, cChunk, sign, managed, blocks, controller, funds, item, itemID, itemAmt);
        addSlot(slot);
    }

    // Gets reel blocks location from disk
    private ArrayList<Block> getBlocks(String name) {

        List<String> xyz = plugin.configData.slots.getStringList("slots." + name + ".location");
        ArrayList<Block> blocks = new ArrayList<Block>();
        World world = Bukkit.getWorld(plugin.configData.slots.getString("slots." + name + ".world", "world"));

        if (world == null) {
            plugin.error("The world for the slot '" + name + "' was null, please fix this and restart the server.");
            plugin.disablePlugin();
            return null;
        }

        for(String coord : xyz) {
            String[] b = coord.split("\\,");
            Location loc = new Location(world, Integer.parseInt(b[0]), Integer.parseInt(b[1]), Integer.parseInt(b[2]));
            blocks.add(loc.getBlock());
            loc.getChunk().load();
        }

        return blocks;
    }

    // Gets controller block from disk
    private Block getController(String name) {

        String location = plugin.configData.slots.getString("slots." + name + ".controller");
        World world = Bukkit.getWorld(plugin.configData.slots.getString("slots." + name + ".world"));
        String[] b = location.split("\\,");
        Location loc = new Location(world, Integer.parseInt(b[0]), Integer.parseInt(b[1]), Integer.parseInt(b[2]));

        return loc.getBlock();
    }

    private Block getSign(String name) {
        String location = plugin.configData.slots.getString("slots." + name + ".sign");

        if(location == null) {
            return null;
        }

        World world = Bukkit.getWorld(plugin.configData.slots.getString("slots." + name + ".world"));
        String[] b = location.split("\\,");
        Location loc = new Location(world, Integer.parseInt(b[0]), Integer.parseInt(b[1]), Integer.parseInt(b[2]));

        return loc.getBlock();
    }

    private String getRchunk(ArrayList<Block> blocks) {
        Block b = blocks.get(1);
        return b.getChunk().getX() + "," + b.getChunk().getZ();
    }

    private String getCchunk(Block c) {
        return c.getChunk().getX() + "," + c.getChunk().getZ();
    }

    // Creates the slot machine in the world
    public void createReel(Player player, BlockFace face, SlotMachine slot) {
        Block center = player.getTargetBlock(Sets.newHashSet(Material.AIR), 0);
        ArrayList<Block> blocks = new ArrayList<Block>();

        slot.setReelChunk(center.getChunk().getX() + "," + center.getChunk().getZ());

        for(int i = 0; i < 3; i++) {
            blocks.add(center.getRelative(getDirection(face, "left"), 2));
            blocks.add(center);
            blocks.add(center.getRelative(getDirection(face, "right"), 2));
            center = center.getRelative(BlockFace.UP, 1);
        }

        for(Block b : blocks) {
            if(plugin.configData.inDebug()) {
                if(blocks.get(0) == b || blocks.get(1) == b || blocks.get(2) == b) {
                    b.setType(Material.DIAMOND_BLOCK);
                }else if(blocks.get(3) == b || blocks.get(4) == b || blocks.get(5) == b) {
                    b.setType(Material.IRON_BLOCK);
                }else if(blocks.get(6) == b || blocks.get(7) == b || blocks.get(8) == b) {
                    b.setType(Material.GOLD_BLOCK);
                }
            }else
                b.setType(Material.DIAMOND_BLOCK);
        }

        slot.setBlocks(blocks);
    }

    // Used for orienting the slot machine correctly
    public BlockFace getDirection(BlockFace face, String direction) {
        if(face == BlockFace.NORTH) {
            if(direction.equalsIgnoreCase("left")) {
                return BlockFace.EAST;
            }
            else if(direction.equalsIgnoreCase("right")) {
                return BlockFace.WEST;
            }
        }else if(face == BlockFace.SOUTH) {
            if(direction.equalsIgnoreCase("left")) {
                return BlockFace.WEST;
            }
            else if(direction.equalsIgnoreCase("right")) {
                return BlockFace.EAST;
            }
        }else if(face == BlockFace.WEST) {
            if(direction.equalsIgnoreCase("left")) {
                return BlockFace.SOUTH;
            }
            else if(direction.equalsIgnoreCase("right")) {
                return BlockFace.NORTH;
            }
        }else if(face == BlockFace.EAST) {
            if(direction.equalsIgnoreCase("left")) {
                return BlockFace.NORTH;
            }
            else if(direction.equalsIgnoreCase("right")) {
                return BlockFace.SOUTH;
            }
        }

        return BlockFace.SELF;
    }

    // If a player is creating slot machine
    public boolean isCreatingSlots(String player) {
        return creatingSlots.containsKey(player);
    }

    // If a player is placing controller
    public boolean isPlacingController(String player) {
        return placingController.containsKey(player);
    }

    // If a player is placing controller
    public boolean isPunchingSign(String player) {
        return punchingSign.containsKey(player);
    }

    // Toggles creating slots
    public void toggleCreatingSlots(String player, SlotMachine slot) {
        if(this.creatingSlots.containsKey(player)) {
            this.creatingSlots.remove(player);
        } else {
            this.creatingSlots.put(player, slot);
        }
    }

    /**
     * Gets the slot machine the player is creating, if nothing then will be null.
     * 
     * @param player the player who is creating
     * @return the {@link SlotMachine} they are creating
     */
    public SlotMachine getCreatingSlot(String player) {
        return this.creatingSlots.get(player);
    }

    // Toggles placing controller
    public void togglePlacingController(String player, SlotMachine slot) {
        if(this.placingController.containsKey(player)) {
            this.placingController.remove(player);
        } else {
            this.placingController.put(player, slot);
        }
    }

    /**
     * Gets the slot machine the player is punching a controller block for.
     * 
     * @param player the player who is creating
     * @return the {@link SlotMachine} the player is creating
     */
    public SlotMachine getPlacingSlot(String player) {
        return this.placingController.get(player);
    }

    // Toggles creating a sign for the slot
    public void togglePunchingSign(String player, SlotMachine slot) {
        if(this.punchingSign.containsKey(player)) {
            this.punchingSign.remove(player);
        }else {
            this.punchingSign.put(player, slot);
        }
    }

    /**
     * Gets the slot machine the player is punching signs for.
     * 
     * @param player the player who is creating
     * @return the {@link SlotMachine} they are creating
     */
    public SlotMachine getSignPunchingSlot(String player) {
        return this.punchingSign.get(player);
    }
}