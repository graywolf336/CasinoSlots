package com.craftyn.casinoslots.actions.impl;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.actions.Action;
import com.craftyn.casinoslots.classes.Reward;
import com.craftyn.casinoslots.classes.SlotType;
import com.craftyn.casinoslots.exceptions.ActionLoadingException;

/**
 * The command action. Usage:
 * 
 * <pre>
 * - give [item id|item id:damage value] [amount] (item meta...)
 * </pre>
 *
 * @author graywolf336
 * @since 3.0.0
 * @version 1.0.0
 */
public class GiveAction extends Action {
    private String name = "Give";
    private String originalItem = "";
    private ItemStack item;

    @SuppressWarnings("deprecation")
    public GiveAction(CasinoSlots plugin, String... args) throws ActionLoadingException {
        super(plugin, args);
        for (String s : args) {
            originalItem += (!originalItem.isEmpty() ? " " : "") + s;
            plugin.debug(s);
        }

        item = new ItemStack(Material.AIR);

        if (args.length >= 2) {
            try {
                item.setAmount(Integer.parseInt(args[1]));
            } catch (NumberFormatException e) {
                throw new ActionLoadingException("The amount for the item passed into '" + this.getName() + "' action is not a valid number.");
            }

            if (args[0].contains(","))
                throw new ActionLoadingException("You're using an old version of the give action, please update: https://github.com/graywolf336/CasinoSlots/wiki");

            if (args[0].contains(":")) {
                String[] materialParts = args[0].split(":");

                if (materialParts.length != 2)
                    throw new ActionLoadingException("The item's material for the item passed into '" + this.getName() + "' action is not a valid material. (" + args[0] + ")");

                try {
                    //parseInt should throw an exception if it can't parse it
                    //then we try to handle it as a material
                    item.setTypeId(Integer.parseInt(materialParts[0]));
                } catch (NumberFormatException e) {
                    try {
                        Material m = Material.matchMaterial(materialParts[0]);

                        if (m == null)
                            throw new Exception();

                        item.setType(m);
                    } catch (Exception ex) {
                        throw new ActionLoadingException("The item's material for the item passed into '" + this.getName() + "' action is not a valid material. (" + materialParts[0] + ")");
                    }
                }

                try {
                    item.setDurability(Short.parseShort(materialParts[1]));
                } catch (NumberFormatException e) {
                    throw new ActionLoadingException("The item's data for the item passed into '" + this.getName() + "' action is not a valid number. (" + args[0] + ")");
                }
            } else {
                try {
                    //parseInt should throw an exception if it can't parse it
                    //then we try to handle it as a material
                    item.setTypeId(Integer.parseInt(args[0]));
                } catch (NumberFormatException e) {
                    try {
                        Material m = Material.matchMaterial(args[0]);

                        if (m == null)
                            throw new Exception();

                        item.setType(m);
                    } catch (Exception ex) {
                        throw new ActionLoadingException("The item's material for the item passed into '" + this.getName() + "' action for is not a valid material. (" + args[0] + ")");
                    }
                }
            }
        }

        if (args.length >= 3) {
            final String[] metas = new String[args.length - 2];
            System.arraycopy(args, 2, metas, 0, metas.length);

            for (final String meta : metas) {
                if (meta.contains(":")) {
                    String[] parts = meta.split(":");
                    String name = parts[0];
                    String value = parts[1];

                    if (name.equalsIgnoreCase("name")) {
                        value = ChatColor.translateAlternateColorCodes('&', value.replace('_', ' '));

                        ItemMeta isMeta = item.getItemMeta();
                        isMeta.setDisplayName(value);
                        item.setItemMeta(isMeta);
                    }

                    else if (name.equalsIgnoreCase("lore")) {
                        String[] lore = value.split("\\|");
                        for (int i = 0; i < lore.length; i++)
                            lore[i] = ChatColor.translateAlternateColorCodes('&', lore[i].replace('_', ' '));

                        ItemMeta isMeta = item.getItemMeta();
                        isMeta.setLore(Arrays.asList(lore));
                        item.setItemMeta(isMeta);
                    }

                    else {
                        Enchantment enchant = null;

                        try {
                            enchant = Enchantment.getById(Integer.parseInt(name));
                        } catch (NumberFormatException e) {
                            try {
                                enchant = Enchantment.getByName(name.toUpperCase());

                                if (enchant == null)
                                    throw new Exception();
                            } catch (Exception ex) {
                                throw new ActionLoadingException("Could not parse the enchantment for the item passed into '" + this.getName() + "' action. (" + meta + ")");
                            }
                        }

                        int enLevel = Integer.parseInt(value);
                        if (enLevel > 127)
                            enLevel = 127;
                        if (enLevel < 1)
                            enLevel = enchant.getMaxLevel();

                        try {
                            item.addUnsafeEnchantment(enchant, enLevel);
                        } catch (Exception e) {
                            throw new ActionLoadingException("Could not add the enchantment for the item passed into '" + this.getName() + "' action. (" + meta + ")");
                        }
                    }
                } else {
                    plugin.getLogger().warning("Invalid meta data for " + args[0] + ": " + meta);
                }
            }
        }

        if (item.getType() == Material.AIR)
            throw new ActionLoadingException("The arguments for the '" + this.getName() + "' action are not valid.");
    }

    public boolean isValid() {
        return item != null;
    }

    public boolean execute(SlotType type, Reward reward, Player player) {
        return player.getInventory().addItem(item).size() == 0;
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return this.name.toLowerCase() + " " + originalItem;
    }
}
