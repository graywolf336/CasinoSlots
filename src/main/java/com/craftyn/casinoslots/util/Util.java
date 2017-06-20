package com.craftyn.casinoslots.util;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Instrument;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Note;
import org.bukkit.Note.Tone;
import org.bukkit.block.NoteBlock;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;

import com.craftyn.casinoslots.classes.Reward;
import com.craftyn.casinoslots.classes.SlotType;

public class Util {
    /**
     * Plays the CasinoSlot game sound at the given location.
     * 
     * @param location the {@link Location} to play the sound at.
     */
    public static void playGameSound(Location location) {
        playNoteBlockSound(location, Instrument.PIANO, new Note((byte) 0, Tone.C, false));
    }
    
    /**
     * Plays a note at the given location, if the block is a {@link NoteBlock}.
     * 
     * @param location the {@link Location} to play the sound at.
     * @param instrument the {@link Instrument} to use.
     * @param note the {@link Note} to play.
     */
    public static void playNoteBlockSound(Location location, Instrument instrument, Note note) {
        if(location.getBlock().getState() instanceof NoteBlock) {
            NoteBlock n = (NoteBlock) location.getBlock().getState();
            n.play(instrument, note);
        }
    }
    
    /**
     * Verifies the file passed is a directory and creates it if it doesn't exist.
     * 
     * @param f the {@link File} to check against
     * @param name the name of the file.
     * @return whether it is good to go or not.
     */
    public static boolean verifyDirectoryExists(File f, String name) {
        if(f.exists()) {
            if(f.isDirectory()) {
                return true;
            }else {
                Bukkit.getLogger().severe("The path where the " + name + " folder should go is not a folder but is instead a file.");
                return false;
            }
        }else {
            if(f.mkdirs()) {
                return true;
            }else {
                Bukkit.getLogger().severe("Failed to make the directory(s) for the " + name + " folder!");
                return false;
            }
        }
    }
    
    @SuppressWarnings("deprecation")
    public static String itemStackToString(ItemStack stack) {
        StringBuilder result = new StringBuilder();
        
        //[item type:damage value] [amount] (item meta...)
        
        result.append(stack.getData().getItemType().toString().toLowerCase());
        result.append(':');
        result.append(stack.getData().getData());
        result.append(" ");
        result.append(stack.getAmount());
        result.append(" ");
        
        for(Entry<Enchantment, Integer> e : stack.getEnchantments().entrySet()) {
            result.append(e.getKey().getName().toLowerCase());
            result.append(':');
            result.append(e.getValue().intValue());
            result.append(" ");
        }
        
        if(stack.hasItemMeta()) {
            ItemMeta m = stack.getItemMeta();
            
            if(m.hasDisplayName()) {
                result.append("name:");
                result.append(m.getDisplayName().replace('§', '&').replace(' ', '_'));
                result.append(" ");
            }
            
            if(m.hasLore()) {
                result.append("lore:");
                m.getLore().stream().forEach(l -> {
                    result.append(l.replace('§', '&').replace(' ', '_'));
                    result.append('|');
                });
                
                //Remove the last |
                result.setCharAt(result.length() - 1, ' ');
            }
            
            List<ItemFlag> flags = new ArrayList<ItemFlag>();
            for(ItemFlag f : ItemFlag.values()) {
                if(m.hasItemFlag(f)) {
                    flags.add(f);
                }
            }
            
            if(!flags.isEmpty()) {
                result.append("flags:");
                flags.forEach(f -> {
                    result.append(f.toString().toLowerCase());
                    result.append(',');
                });
                
                //Remove the last ,
                result.setCharAt(result.length() - 1, ' ');
            }
        }
        
        
        return result.toString().trim();
    }
    
    /**
     * Converts a {@link MaterialData} to string.
     * 
     * @param data the {@link MaterialData} to turn into a string.
     * @return string value of the data.
     */
    @SuppressWarnings("deprecation")
    public static String materialDataToString(MaterialData data) {
        return data.getItemType().toString().toLowerCase() + ":" + data.getData();
    }
    
    /**
     * Parses a {@link MaterialData} from a string.
     * 
     * @param valueToParse String in the format: <code>stone:2</code>
     * @return the {@link MaterialData}
     */
    @SuppressWarnings("deprecation")
    public static MaterialData parseMaterialDataFromString(String valueToParse) {
        String[] matSplit = valueToParse.toUpperCase().split(":");
        Material mat = Material.matchMaterial(matSplit[0]);
        byte data = 0;
        if(matSplit.length == 2) {
            data = Byte.parseByte(matSplit[1]);
        }
        
        return new MaterialData(mat, data);
    }
    
    public static String colorizeAndTokenize(SlotType type, String message) {
        return ChatColor.translateAlternateColorCodes('&', message).replace("[cost]", String.valueOf(type.getCost()));
    }
    
    public static String colorizeAndTokenize(SlotType type, Reward reward, Player player, String message) {
        return ChatColor.translateAlternateColorCodes('&', message).replace("[cost]", String.valueOf(type.getCost()))
                .replace("[type]", type.getName())
                .replace("[moneywon]", String.valueOf(reward.getMoney()))
                .replace("[player]", player.getName())
                .replace("[playername]", player.getDisplayName())
                .replace("[playeruuid]", player.getUniqueId().toString());
    }
    
    public static String tokenize(SlotType type, Reward reward, Player player, String message) {
        return new String(message).replace("[cost]", String.valueOf(type.getCost()))
                .replace("[type]", type.getName())
                .replace("[moneywon]", String.valueOf(reward.getMoney()))
                .replace("[player]", player.getName())
                .replace("[playername]", player.getDisplayName())
                .replace("[playeruuid]", player.getUniqueId().toString());
    }
}
