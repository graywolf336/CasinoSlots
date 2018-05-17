package com.craftyn.casinoslots.command.commands;

import java.util.Collections;
import java.util.List;

import org.bukkit.command.CommandSender;

import com.craftyn.casinoslots.CasinoManager;
import com.craftyn.casinoslots.command.Command;
import com.craftyn.casinoslots.command.CommandInfo;

@CommandInfo(
        maxArgs = 3,
        minimumArgs = 2,
        needsPlayer = true,
        pattern = "add",
        permission = "casinoslots.command.add",
        usage = "/casino add [name] [type]"
        )
public class CasinoAddCommand implements Command {
	public boolean execute(CasinoManager cm, CommandSender sender, String... args) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	public List<String> provideTabCompletions(CasinoManager cm, CommandSender sender, String... args) throws Exception {
		// TODO Auto-generated method stub
		return Collections.emptyList();
	}
}
