package com.craftyn.casinoslots.command;

import org.bukkit.command.CommandSender;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.enums.Settings;
import com.craftyn.casinoslots.exceptions.TypesFolderException;
import com.craftyn.casinoslots.util.PermissionUtil;

public class CasinoReload extends AnCommand {
    /**
     * Is initiated by a /casino reload, which is intended to reload the config.
     * 
     * @param plugin The main plugin class
     * @param args The other arguments passed along with 'reload'
     * @param sender The one who did the command
     */
    public CasinoReload(CasinoSlots plugin, String[] args, CommandSender sender) {
        super(plugin, args, sender);
    }

    public boolean process() {
        // Permissions
        if(player != null) {
            if(!PermissionUtil.isAdmin(player)) {
                noPermission();
                return true;
            }
        }

        plugin.reloadConfig();
        plugin.getConfigData().reloadConfigs();

        try {
            plugin.getTypeManager().loadAllTheTypes(plugin);
        } catch (TypesFolderException e) {
            this.senderSendMessage("Failed to reload the Types.");
        }

        plugin.getSlotManager().reloadSlots();
        plugin.reloadUpdateCheck();

        if(Settings.inDebug()) senderSendMessage("Debugging enabled.");
        senderSendMessage("Configuration reloaded");
        return true;
    }
}