package com.craftyn.casinoslots.actions.impl;

import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.actions.Action;
import com.craftyn.casinoslots.classes.Reward;
import com.craftyn.casinoslots.classes.SlotType;
import com.craftyn.casinoslots.exceptions.ActionLoadingException;

/**
 * The potion action.
 *
 * Usage:
 * <ul>
 * <li>potion effect
 * <li>
 * <li>potion effect amplifier</li>
 * <li>potion effect amplifier duration</li>
 * <li>potion effect:amplifier</li>
 * <li>potion effect:amplifier:duration</li>
 * </ul>
 *
 * @author graywolf336
 * @since 3.0.0
 * @version 1.0.0
 */
public class PotionAction extends Action {
    private String name = "Potion";
    private static final int TICKS_PER_SECOND = 20;
    private static final int DEFAULT_POTION_AMPLIFIER = 0;
    private static final int DEFAULT_POTION_DURATION = Integer.MAX_VALUE;
    private PotionEffect potion;

    public PotionAction(CasinoSlots plugin, String... args) throws ActionLoadingException {
        super(plugin, args);
        String exceptionMsg = "The arguments for the '" + this.getName() + "' action are not valid.";

        switch (args.length) {
            case 1:
                //effect
                //effect:amplifier
                //effect:amplifier:duration
                if (args[0].contains(":")) {
                    String[] parts = args[0].split(":");

                    PotionEffectType effect = getType(parts[0]);

                    if (effect == null)
                        throw new ActionLoadingException(exceptionMsg);

                    int amp = DEFAULT_POTION_AMPLIFIER;
                    if (parts.length >= 2)
                        amp = this.getAmplification(parts[1]);

                    int dur = DEFAULT_POTION_DURATION;
                    if (parts.length >= 3)
                        dur = this.getDuration(parts[2]);

                    potion = new PotionEffect(effect, dur, amp);
                } else {
                    PotionEffectType effect = getType(args[0]);

                    if (effect == null)
                        throw new ActionLoadingException(exceptionMsg);

                    potion = new PotionEffect(effect, DEFAULT_POTION_DURATION, DEFAULT_POTION_AMPLIFIER);
                }
                break;
            case 2:
                //effect amplifier
                PotionEffectType effect2 = getType(args[0]);

                if (effect2 == null)
                    throw new ActionLoadingException(exceptionMsg);

                potion = new PotionEffect(effect2, DEFAULT_POTION_DURATION, this.getAmplification(args[1]));
                break;
            case 3:
                //effect amplifier duration
                PotionEffectType effect3 = getType(args[0]);

                if (effect3 == null)
                    throw new ActionLoadingException(exceptionMsg + " (Invalid Potion Type)");

                potion = new PotionEffect(effect3, this.getDuration(args[2]), this.getAmplification(args[1]));
                break;
            default:
                throw new ActionLoadingException(exceptionMsg);
        }
    }

    public boolean isValid() {
        return potion != null;
    }

    public boolean execute(SlotType type, Reward reward, Player player) {
        return player.addPotionEffect(potion, true);
    }

    public String getName() {
        return this.name;
    }

    public String toString() {
        return this.name.toLowerCase() + " " + potion.getType().getName().toLowerCase() + " " + potion.getAmplifier() + " " + potion.getDuration() / TICKS_PER_SECOND;
    }

    @SuppressWarnings("deprecation")
    private PotionEffectType getType(String type) {
        PotionEffectType effect = null;

        if (type.matches("[0-9]+")) {
            effect = PotionEffectType.getById(Integer.parseInt(type));
        } else {
            effect = PotionEffectType.getByName(type.toUpperCase());
        }

        return effect;
    }

    private int getDuration(String duration) {
        int dur = DEFAULT_POTION_DURATION;

        if (duration.matches("[0-9]+")) {
            dur = Integer.parseInt(duration) * TICKS_PER_SECOND;
        }

        return dur;
    }

    private int getAmplification(String amplifier) {
        int amp = DEFAULT_POTION_AMPLIFIER;

        if (amplifier.matches("[0-9]+")) {
            amp = Integer.parseInt(amplifier);
        }

        return amp;
    }
}
