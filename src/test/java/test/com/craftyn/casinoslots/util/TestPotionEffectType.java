package test.com.craftyn.casinoslots.util;

import org.bukkit.potion.PotionEffectType;

public class TestPotionEffectType extends PotionEffectType {
    private String name;
    private boolean instant;
    private double modifier;

    protected TestPotionEffectType(PotionEffectType type, String name, boolean instant, double modifier) {
        super(type.hashCode());
        
        this.name = name;
        this.instant = instant;
        this.modifier = modifier;
    }

    public double getDurationModifier() {
        return this.modifier;
    }

    public String getName() {
        return this.name;
    }

    public boolean isInstant() {
        return this.instant;
    }
}
