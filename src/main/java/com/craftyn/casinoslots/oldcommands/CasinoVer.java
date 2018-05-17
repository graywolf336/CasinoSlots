package com.craftyn.casinoslots.oldcommands;

import org.bukkit.command.CommandSender;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.util.PermissionUtil;

public class CasinoVer extends AnCommand {

    public CasinoVer(CasinoSlots plugin, String[] args, CommandSender sender) {
        super(plugin, args, sender);
    }

    public boolean process() {

        // Admin permission
        if (player != null) {
            if (!PermissionUtil.isAdmin(player)) {
                noPermission();
                return true;
            }
        }

        senderSendMessage("Version " + plugin.getDescription().getVersion());
        return true;
    }
}
