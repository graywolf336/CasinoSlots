package com.craftyn.casinoslots;

import com.craftyn.casinoslots.actions.ActionFactory;
import com.craftyn.casinoslots.slot.SlotManager;
import com.craftyn.casinoslots.slot.TypeManager;

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
}
