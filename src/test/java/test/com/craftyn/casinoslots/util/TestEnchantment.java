package test.com.craftyn.casinoslots.util;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentTarget;
import org.bukkit.inventory.ItemStack;

public class TestEnchantment extends Enchantment {
    private String name;
    
    public TestEnchantment(Enchantment enchant, String name) {
        super(enchant.hashCode());
        this.name = name.toUpperCase();
    }

    public String getName() {
        return this.name;
    }

    public int getMaxLevel() {
        return 5;
    }

    public int getStartLevel() {
        return 1;
    }

    public EnchantmentTarget getItemTarget() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean conflictsWith(Enchantment other) {
        return false;
    }

    public boolean canEnchantItem(ItemStack item) {
        return true;
    }

    public boolean isTreasure() {
        return false;
    }

    public boolean isCursed() {
        return false;
    }
}
