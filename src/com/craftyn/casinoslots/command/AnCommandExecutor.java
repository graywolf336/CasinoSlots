package com.craftyn.casinoslots.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.craftyn.casinoslots.CasinoSlots;

public class AnCommandExecutor implements CommandExecutor{
	
	protected CasinoSlots plugin;
	private AnCommand cmd;
	
	public AnCommandExecutor(CasinoSlots plugin) {
		this.plugin = plugin;
	}
	
	public boolean onCommand(CommandSender sender, Command command, String commandlabel, final String[] args) {
		
		if(sender instanceof Player) {
			Player player = (Player) sender;
			
			// Valid command format
			if(args.length >= 1) {
				// casino add
				if(args[0].equalsIgnoreCase("add")) {
					cmd = new CasinoAdd(plugin, args, player);
				}
				
				// casino addmanaged
				else if(args[0].equalsIgnoreCase("addmanaged")) {
					cmd = new CasinoAddManaged(plugin, args, player);
				}
				
				// casino additem
				else if(args[0].equalsIgnoreCase("additem")) {
					cmd = new CasinoAddItem(plugin, args, player);
				}
				
				// casino remove
				else if(args[0].equalsIgnoreCase("remove")) {
					cmd = new CasinoRemove(plugin, args, player);
				}
				
				// casino list
				else if(args[0].equalsIgnoreCase("list")) {
					cmd = new CasinoList(plugin, args, player);
				}
				
				// casino reload
				else if(args[0].equalsIgnoreCase("reload")) {
					cmd = new CasinoReload(plugin, args, player);
				}
				
				// casino stats
				else if(args[0].equalsIgnoreCase("stats")) {
					cmd = new CasinoStats(plugin, args, player);
				}
				
				// casino type
				else if(args[0].equalsIgnoreCase("type")) {
					cmd = new CasinoType(plugin, args, player);
				}
				
				// casino setowner
				else if(args[0].equalsIgnoreCase("setowner")) {
					cmd = new CasinoSet(plugin, args, player);
				}
				
				// casino deposit
				else if(args[0].equalsIgnoreCase("deposit")) {
					cmd = new CasinoDeposit(plugin, args, player);
				}
				
				// casino deposit
				else if(args[0].equalsIgnoreCase("withdraw")) {
					cmd = new CasinoWithdraw(plugin, args, player);
				}
				
				// casino toggle
				else if(args[0].equalsIgnoreCase("toggle")) {
					cmd = new CasinoToggle(plugin, args, player);
				}
				
				// invalid command
				else {
					plugin.sendMessage(player, "Incorrect command syntax, see /casino for help.");
					return true;
				}
				
			}
			
			// no arguments
			else {
				cmd = new Casino(plugin, args, player);
			}
			
			return cmd.process();
		}
		
		// No commands by console
		else {
			plugin.log("This command cannot be executed as console.");
		}
		return true;
	}

}