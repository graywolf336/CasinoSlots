package com.craftyn.casinoslots.slot;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.craftyn.casinoslots.CasinoSlots;

public class RewardData {
	
	private CasinoSlots plugin;
	
	public RewardData(CasinoSlots plugin) {
		this.plugin = plugin;
	}
	
	// Sends reward to player
	public void send(Player player, Reward reward) {
		
		if(reward.message != null) {
			plugin.sendMessage(player, reward.message);
		}
		
		if(reward.money != null) {
			plugin.economy.depositPlayer(player.getName(), reward.money);
		}
		
		if(reward.action != null && !reward.action.isEmpty()) {
			executeAction(reward.action, player);
		}
	}
		
	// Parses reward actions
	private void executeAction(List<String> actionList, Player p) {
		
		for(String action : actionList) {
			
			String[] a = action.split(" ");
			
			// Give action
			if(a[0].equalsIgnoreCase("give")) {
				
				String[] itemData = a[1].split(":");
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
			
			// Broadcast action
			else if(a[0].equalsIgnoreCase("broadcast")) {
				
				String message = a[1];
				for(Integer i = 2; i < a.length; i++) {
					message = message + " " + a[i];
				}
				
				message = message.replaceAll("(?i)&([a-f0-9])", "\u00A7$1");
				plugin.getServer().broadcastMessage(message);
			}
		}
		
	}

}