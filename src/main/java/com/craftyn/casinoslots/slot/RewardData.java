package com.craftyn.casinoslots.slot;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import com.craftyn.casinoslots.CasinoSlots;

public class RewardData {
    private CasinoSlots plugin;
    private static final Random random = new Random();

    public RewardData(CasinoSlots plugin) {
        this.plugin = plugin;
    }

    // Sends reward to player
    public void send(SlotMachine slot, Player player, Reward reward, Type type) {

        if(reward.getMessage() != null && !reward.getMessage().isEmpty()) {
            plugin.sendMessage(player, reward.getMessage());
        }

        if(reward.getMoney() != null) {
            if(reward.getMoney() < 0) {
                plugin.getEconomy().withdrawPlayer(player, Math.abs(reward.getMoney()));
            } else {
                plugin.getEconomy().depositPlayer(player, reward.getMoney());
            }
            
            //Take the money from the slot machine, if it is managed
            if(slot.isManaged()) {
                if(reward.getMoney() < 0) {
                    slot.deposit(Math.abs(reward.getMoney()));
                }else {
                    slot.withdraw(reward.getMoney());
                }
            }
        }

        if(reward.getAction() != null && !reward.getAction().isEmpty()) {
            executeAction(reward.getAction(), player, type, reward);
        }
    }

    // Parses reward actions
    private void executeAction(List<String> actionList, Player p, Type type, Reward reward) {
        plugin.debug("The size of the actionList is: " + actionList.size());
        for(String action : actionList) {
            String[] a = action.split(" ");

            // Give action
            if(a[0].equalsIgnoreCase("give")) {
                ItemStack is = null;

                if(a.length >= 3) {
                    int amount = Integer.parseInt(a[2]);

                    if(a[1].contains(":")) {
                        String[] parts = a[1].split(":");
                        int item = Integer.parseInt(parts[0]);
                        short damage = Short.parseShort(parts[1]);

                        is = new ItemStack(item, amount, damage);
                    }else {
                        int item = Integer.parseInt(a[1]);

                        is = new ItemStack(item, amount);
                    }
                }

                if(a.length >= 4) {
                    final String[] metas = new String[a.length - 3];
                    System.arraycopy(a, 3, metas, 0, metas.length);

                    for(final String meta : metas) {
                        if(meta.contains(":")) {
                            String[] parts = meta.split(":");
                            String name = parts[0];
                            String value = parts[1];

                            if(name.equals("name")) {
                                value = ChatColor.translateAlternateColorCodes('&', value.replace('_', ' '));

                                ItemMeta isMeta = is.getItemMeta();
                                isMeta.setDisplayName(value);
                                is.setItemMeta(isMeta);
                            }

                            else if(name.equals("lore")) {
                                String[] lore = value.split("\\|");
                                for(int i = 0; i < lore.length; i++)
                                    lore[i] = ChatColor.translateAlternateColorCodes('&', lore[i].replace('_', ' '));

                                ItemMeta isMeta = is.getItemMeta();
                                isMeta.setLore(Arrays.asList(lore));
                                is.setItemMeta(isMeta);
                            }

                            else{
                                int enID = Integer.parseInt(name);
                                Enchantment enchantment = Enchantment.getById(enID);

                                //check if the enchantment is valid
                                if(enchantment == null) {
                                    plugin.severe("There is an invalid enchantment ID for the type " + type.getName());
                                    continue;
                                }

                                int enLevel = Integer.parseInt(value);
                                if (enLevel > 127) enLevel = 127;
                                if (enLevel < 1) enLevel = enchantment.getMaxLevel();

                                try {
                                    is.addUnsafeEnchantment(enchantment, enLevel);
                                } catch (Exception e) {
                                    plugin.severe("Enchanting one of your rewards for " + type.getName() + " wasn't successful.");
                                }
                            }
                        }
                    }
                }

                p.getInventory().addItem(is);
            }

            // Kill action
            else if(a[0].equalsIgnoreCase("kill")) {
                p.setHealth(0);
            }

            // Kick action
            else if(a[0].equalsIgnoreCase("kick")) {
                p.kickPlayer("You cheated the Casino!");
            }

            // Addxp action
            else if(a[0].equalsIgnoreCase("addxp")) {

                int exp = Integer.parseInt(a[1]);
                p.giveExp(exp);
            }

            // AddXPlvl action
            else if(a[0].equalsIgnoreCase("addxplvl")) {

                int exp = Integer.parseInt(a[1]);
                int oldLvl = p.getLevel();
                int newLvl = oldLvl+exp;
                p.setLevel(newLvl);
            }

            // Tpto action
            else if(a[0].equalsIgnoreCase("tpto")) {

                String[] xyz = a[1].split("\\,");
                World world = p.getWorld();
                Location loc = new Location(world, Integer.parseInt(xyz[0]), Integer.parseInt(xyz[1]), Integer.parseInt(xyz[2]));
                p.teleport(loc);
            }

            // Smite action
            else if(a[0].equalsIgnoreCase("smite")) {
                if(a.length == 2) {
                    int times = Integer.parseInt(a[1]);
                    for(int i = 1; i < times; i++) {
                        p.getWorld().strikeLightning(p.getLocation());
                    }
                }else
                    p.getWorld().strikeLightning(p.getLocation());
            }

            // Fire action
            else if(a[0].equalsIgnoreCase("fire")) {
                if(a.length == 2) {
                    int ticks = Integer.parseInt(a[1]);
                    p.setFireTicks(ticks);
                }else {
                    p.setFireTicks(120);
                }
            }

            // goBlind action
            else if(a[0].equalsIgnoreCase("goblind")) {
                if(a.length == 2) {
                    int ticks = Integer.parseInt(a[1]);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, ticks, 90));
                }else {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 300, 90));
                }
            }

            // gocrazy action
            else if (a[0].equalsIgnoreCase("gocrazy")) {
                if(a.length == 2) {
                    int ticks = Integer.parseInt(a[1]);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, ticks, 1000));
                }else {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 160, 1000));
                }
            }

            // highjump action
            else if (a[0].equalsIgnoreCase("highjump")) {
                if(a.length == 2) {
                    int ticks = Integer.parseInt(a[1]);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, ticks, 2));
                }else {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 120, 2));
                }
            }

            // digfast action
            else if (a[0].equalsIgnoreCase("digfast")) {
                if(a.length == 2) {
                    int ticks = Integer.parseInt(a[1]);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, ticks, 2));
                }else {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 400, 2));
                }
            }

            // hulkup action
            else if (a[0].equalsIgnoreCase("hulkup")) {
                if(a.length == 2) {
                    int ticks = Integer.parseInt(a[1]);
                    p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, ticks, 15));
                }else {
                    p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 400, 15));
                }
            }

            // DrugUp action
            else if (a[0].equalsIgnoreCase("drugup")) {
                p.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 900, 200));
                p.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 400, 10));
            }

            else if (a[0].equalsIgnoreCase("slap")) {
                // special thanks to CommandBook for this code, loved it enough to add it. Source:
                // https://github.com/sk89q/commandbook/blob/master/src/main/java/com/sk89q/commandbook/FunComponent.java#L204
                p.setVelocity(new Vector(random.nextDouble() * 2.0 - 1, random.nextDouble() * 1, random.nextDouble() * 2.0 - 1));
            }

            else if (a[0].equalsIgnoreCase("rocket")) {
                // Special thanks to CommandBook for this code, loved it enough to add it. Source:
                // https://github.com/sk89q/commandbook/blob/master/src/main/java/com/sk89q/commandbook/FunComponent.java#L282
                p.setVelocity(new Vector(0, 30, 0));
            }

            //command
            else if (a[0].equalsIgnoreCase("command")) {
                //Check to make sure that the action "command" is greater than 1
                if (a.length < 2) {
                    plugin.error("The command action for " + type.getName() + " needs something other than 'command' for it to run.");
                    continue;
                }

                //Generate the command
                String command = action.substring(8)
                		.replace("[cost]", type.getCost().toString())
                		.replace("[moneywon]", reward.getMoney().toString())
                		.replace("[player]", p.getName())
                		.replace("[playername]", p.getDisplayName())
                		.replace("[type]", type.getName());

                plugin.debug("Command for " + type.getName() + ": " + command);
                if(command.isEmpty()) {
                	plugin.error("A command action for " + type.getName() + " is empty, check your config.yml.");
                }else {
                	plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), command);
                }
            }

            // Broadcast action
            else if(a[0].equalsIgnoreCase("broadcast")) {
                //Check to make sure that there is actually something to broadcast
                if (a.length < 2) {
                    plugin.error("The broadcast action needs something other than 'broadcast' for it to run.");
                    continue;
                }

                //Set the message to broadcast to everything after "broadcast ", which is 10.
                String msg = ChatColor.translateAlternateColorCodes('&', action.substring(10)
                		.replace("[cost]", type.getCost().toString())
                		.replace("[moneywon]", reward.getMoney().toString())
                		.replace("[player]", p.getDisplayName())
                		.replace("[type]", type.getName()));

                plugin.debug("Broadcast message for " + type.getName() + ": " + msg);
                if(msg.isEmpty()) {
                	plugin.error("The broadcast message for " + type.getName() + " is empy.");
                }else {
                	//Broadcast the message
                    plugin.getServer().broadcastMessage(msg);
                }
            }
        }
    }
}