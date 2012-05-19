package com.craftyn.casinoslots.slot;

import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
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
	public void send(Player player, Reward reward, Type type) {
		
		if(reward.message != null) {
			plugin.sendMessage(player, reward.message);
		}
		
		if(reward.money != null) {
			plugin.economy.depositPlayer(player.getName(), reward.money);
		}
		
		if(reward.action != null && !reward.action.isEmpty()) {
			executeAction(reward.action, player, type);
		}
	}
		
	// Parses reward actions
	private void executeAction(List<String> actionList, Player p, Type type) {
		
		for(String action : actionList) {
			
			String[] a = action.split(" ");
			
			// Give action
			if(a[0].equalsIgnoreCase("give")) {
				
				String[] itemData = a[1].split("\\,");
				
				int item = Integer.parseInt(itemData[0]);
				
				byte data = 0;
				short damage = 0;
				
				if(itemData.length == 2) {
					data = (byte) Integer.parseInt(itemData[1]);
				}
				
				int n = Integer.parseInt(a[2]);
				p.getInventory().addItem(new ItemStack(item, n, damage, data));
			}
			
			// Kill action
			else if(a[0].equalsIgnoreCase("kill")) {
				p.setHealth(0);
			}
			
			// Addxp action
			else if(a[0].equalsIgnoreCase("addxp")) {
				
				int exp = Integer.parseInt(a[1]);
				p.giveExp(exp);
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
				p.getWorld().strikeLightning(p.getLocation());
			}
			
			// Fire action
			else if(a[0].equalsIgnoreCase("fire")) {
				//Haven't tested this, just adding more actions
				p.setFireTicks(120);
			}
			
			// goBlind action
			else if(a[0].equalsIgnoreCase("goblind")) {
				p.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 300, 90));
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
				p.setVelocity(new Vector(0, 20, 0));
			}
			
			//command
			else if (a[0].equalsIgnoreCase("command")) {
				//Check to make sure that the action "command" is greater than 1
				if (a.length < 2) {
					plugin.error("The command action needs something other than 'command' for it to run.");
					return;
				}
				
				//Initialize the command
				String command = null;
				
				//Set the sender of the command as the console
				CommandSender sender = plugin.server.getConsoleSender();
				
				for(String bit : a) {
					//Strip the "command"
					if (bit.equalsIgnoreCase("command")) {
						continue;
					}
					
					//Replace the "null" with the word after command
					if (bit.equalsIgnoreCase(a[1])) {
						command = bit;
						continue;
					}
					
					//Strip [player] and make it equal the player who played the slot
					if (bit.equalsIgnoreCase("[player]")) {
						bit = p.getName();
					}
					
					// Add the current bit to the command
					command = command + " " + bit;
				}
				
				//Check to make sure the command isn't actually null
				if (command != null) {
					plugin.server.dispatchCommand(sender, command);
					return;
				}else {
					// if it is, then return an error in the console and don't do anything.
					plugin.error("Couldn't find a command to do, please check your config.yml file.");
					return;
				}
			}
			
			// Broadcast action
			else if(a[0].equalsIgnoreCase("broadcast")) {
				//Check to make sure that there is actually something to broadcast
				if (a.length < 2) {
					plugin.error("The broadcast action needs something other than 'broadcast' for it to run.");
					return;
				}
				
				//Initiate the message to broadcast
				String message = null;
				
				//Start the loop for the message
				for(String bit : a) {
					//Strip the "broadcast"
					if (bit.equalsIgnoreCase("broadcast")) continue;
					
					//Replace the "null" with the word right after 'broadcast'
					if (bit.equalsIgnoreCase(a[1])) {
						message = bit;
						continue;
					}
					
					//Strip [player] and make bit equal to the player who played the slot
					if (bit.equalsIgnoreCase("[player]")) {
						bit = p.getName();
					}
					
					//Strip [cost] and make bit equal to the player who played the slot
					if (bit.equalsIgnoreCase("[cost]")) {
						bit = String.valueOf(type.getCost());
					}
					
					//Add onto the message that is going to be broadcasted
					message = message + " " + bit;
				}
				
				//Convert all color codes so that Minecraft shows them as color
				message = message.replaceAll("(?i)&([a-f0-9])", "\u00A7$1");
				
				//Broadcast the message
				plugin.server.broadcastMessage(message);
			}
		}	
	}
}