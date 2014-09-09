package test.com.craftyn.casinoslots.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class MockPlayerInventory implements PlayerInventory {

    private int armorSize = 4, inventorySize = 36;
    ItemStack[] armorContents = new ItemStack[armorSize];
    ItemStack[] inventoryContents = new ItemStack[inventorySize];

    @Override
    public ItemStack[] getArmorContents() {
        return armorContents;
    }

    @Override
    public ItemStack getHelmet() {
        return armorContents[0];
    }

    @Override
    public ItemStack getChestplate() {
        return armorContents[1];
    }

    @Override
    public ItemStack getLeggings() {
        return armorContents[2];
    }

    @Override
    public ItemStack getBoots() {
        return armorContents[3];
    }

    @Override
    public void setArmorContents(ItemStack[] itemStacks) {
        this.armorContents = itemStacks;
    }

    @Override
    public void setHelmet(ItemStack itemStack) {
        this.armorContents[0] = itemStack;
    }

    @Override
    public void setChestplate(ItemStack itemStack) {
        this.armorContents[1] = itemStack;
    }

    @Override
    public void setLeggings(ItemStack itemStack) {
        this.armorContents[2] = itemStack;
    }

    @Override
    public void setBoots(ItemStack itemStack) {
        this.armorContents[3] = itemStack;
    }

    @Override
    public ItemStack getItemInHand() {
        return null;
    }

    @Override
    public void setItemInHand(ItemStack itemStack) {

    }

    @Override
    public int getHeldItemSlot() {
        return 0;
    }

    @Override
    public int clear(int i, int i2) {
        return 0;
    }

    @Override
    public HumanEntity getHolder() {
        return null;
    }

    @Override
    public int getSize() {
        return inventoryContents.length + armorContents.length;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public ItemStack getItem(int i) {
        if (i >= 0 && i < inventorySize) {
            return inventoryContents[i];
        } else if (i >= inventorySize
                && i < inventorySize + armorSize) {
            return armorContents[i - inventorySize];
        } else {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        if (i >= 0 && i < inventorySize) {
            inventoryContents[i] = itemStack;
        } else if (i >= inventorySize
                && i < inventorySize + armorSize) {
            armorContents[i - inventorySize] = itemStack;
        } else {
            throw new ArrayIndexOutOfBoundsException();
        }
    }

    @Override
    public HashMap<Integer, ItemStack> addItem(ItemStack... itemStacks) {
        return null;
    }

    @Override
    public HashMap<Integer, ItemStack> removeItem(ItemStack... itemStacks) {
        return null;
    }

    @Override
    public ItemStack[] getContents() {
        return this.inventoryContents;
    }

    @Override
    public void setContents(ItemStack[] itemStacks) {
        this.inventoryContents = itemStacks;
    }

    @Override
    public boolean contains(int i) {
        return false;
    }

    @Override
    public boolean contains(Material material) {
        return false;
    }

    @Override
    public boolean contains(ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean contains(int i, int i1) {
        return false;
    }

    @Override
    public boolean contains(Material material, int i) {
        return false;
    }

    @Override
    public boolean contains(ItemStack itemStack, int i) {
        return false;
    }

    @Override
    public HashMap<Integer, ? extends ItemStack> all(int i) {
        return null;
    }

    @Override
    public HashMap<Integer, ? extends ItemStack> all(Material material) {
        return null;
    }

    @Override
    public HashMap<Integer, ? extends ItemStack> all(ItemStack itemStack) {
        return null;
    }

    @Override
    public int first(int i) {
        return 0;
    }

    @Override
    public int first(Material material) {
        return 0;
    }

    @Override
    public int first(ItemStack itemStack) {
        return 0;
    }

    @Override
    public int firstEmpty() {
        return 0;
    }

    @Override
    public void remove(int i) {

    }

    @Override
    public void remove(Material material) {

    }

    @Override
    public void remove(ItemStack itemStack) {

    }

    @Override
    public void clear(int i) {
        inventoryContents[i] = null;
    }

    @Override
    public void clear() {

    }

    @Override
    public List<HumanEntity> getViewers() {
        return null;
    }

    @Override
    public String getTitle() {
        return null;
    }

    @Override
    public InventoryType getType() {
        return null;
    }

    @Override
    public ListIterator<ItemStack> iterator() {
        return null;
    }

    @Override
    public int getMaxStackSize() {
        return 0;
    }

    @Override
    public void setMaxStackSize(int i) {

    }

    @Override
    public ListIterator<ItemStack> iterator(int i) {
        return null;
    }

    @Override
    public boolean containsAtLeast(final ItemStack itemStack, final int i) {
        return false;
    }

    @SuppressWarnings("deprecation")
    private static Map<String, Object> makeMap(ItemStack[] items) {
        Map<String, Object> contents = new LinkedHashMap<String, Object>(
                items.length);
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null && items[i].getTypeId() != 0) {
                contents.put(Integer.valueOf(i).toString(), items[i]);
            }
        }
        return contents;
    }

    public String toString() {
        return "{\"inventoryContents\":" + makeMap(getContents())
                + ",\"armorContents\":" + makeMap(getArmorContents()) + "}";
    }

    @Override
    public void setHeldItemSlot(int slot) {

    }
}