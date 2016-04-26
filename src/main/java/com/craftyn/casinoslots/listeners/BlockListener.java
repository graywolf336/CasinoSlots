package com.craftyn.casinoslots.listeners;

import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.classes.OldSlotMachine;
import com.craftyn.casinoslots.enums.Settings;

public class BlockListener implements Listener {
    private CasinoSlots plugin;

    public BlockListener(CasinoSlots plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {
        // Check if plugin is enabled
        if(plugin.isEnabled()) {

            // Slot protection enabled
            if(Settings.SLOTS_ENABLE_PROTECTION.asBoolean()) {

                Block b = event.getBlock();

                // Look for match in slots
                for(OldSlotMachine slot : plugin.getSlotManager().getSlots()) {

                    for(Block current : slot.getBlocks()) {

                        // Match found, cancel event
                        if(b.equals(current)) {
                            event.setCancelled(true);
                            return;
                        }
                    }

                    // Check controller as well
                    Block current = slot.getController();
                    if(b.equals(current)) {
                        event.setCancelled(true);
                        return;
                    }
                }
            }
        }
    }

}