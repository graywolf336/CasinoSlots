package com.craftyn.casinoslots;

import org.bukkit.command.CommandSender;

import com.craftyn.casinoslots.actions.ActionFactory;
import com.craftyn.casinoslots.slot.SlotManager;
import com.craftyn.casinoslots.slot.TypeManager;

import net.milkbowl.vault.economy.Economy;

public class CasinoSlotsStaticAPI {
    public static CasinoSlots plugin;

    protected CasinoSlotsStaticAPI(CasinoSlots pl) {
        plugin = pl;
    }

    public static ActionFactory getActionFactory() {
        return plugin.getActionFactory();
    }

    public static TypeManager getTypeManager() {
        return plugin.getTypeManager();
    }

    public static SlotManager getSlotManager() {
        return plugin.getSlotManager();
    }

    public static Economy getEconomy() {
        return plugin.getEconomy();
    }

    public static void sendMessageToPlayer(CommandSender recipient, String message) {
        plugin.sendMessage(recipient, message);
    }
}
