package com.craftyn.casinoslots.actions.impl;

import java.util.Random;

import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.actions.Action;
import com.craftyn.casinoslots.classes.Reward;
import com.craftyn.casinoslots.classes.SlotType;
import com.craftyn.casinoslots.exceptions.ActionLoadingException;

/**
 * The slap action. Usage: - slap
 *
 * @author graywolf336
 * @since 3.0.0
 * @version 1.0.0
 */
public class SlapAction extends Action {
    private String name = "Slap";
    private Random random;

    public SlapAction(CasinoSlots plugin, String... args) throws ActionLoadingException {
        super(plugin, args);
        random = new Random();
    }

    public boolean isValid() {
        return true;
    }

    public boolean execute(SlotType type, Reward reward, Player player) {
        Vector v = new Vector(random.nextDouble() * 2.0 - 1, random.nextDouble() * 1, random.nextDouble() * 2.0 - 1);
        player.setVelocity(v);
        return true;
    }

    public String getName() {
        return this.name;
    }
    
    public String toString() {
        return this.name.toLowerCase();
    }
}
