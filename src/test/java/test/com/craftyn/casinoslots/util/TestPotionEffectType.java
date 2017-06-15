package test.com.craftyn.casinoslots.util;

import org.bukkit.Color;
import org.bukkit.potion.PotionEffectType;

public class TestPotionEffectType extends PotionEffectType {
    private String name;
    private boolean instant;
    private double modifier;
    private Color color;

    protected TestPotionEffectType(PotionEffectType type, String name, boolean instant, double modifier) {
        super(type.hashCode());
        
        this.name = name;
        this.instant = instant;
        this.modifier = modifier;
        this.color = Color.BLACK;
    }

    protected TestPotionEffectType(PotionEffectType type, String name, boolean instant, double modifier, Color color) {
        super(type.hashCode());

        this.name = name;
        this.instant = instant;
        this.modifier = modifier;
        this.color = color;
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

    public Color getColor() {
        return this.color;
    }
}
