package test.com.craftyn.casinoslots.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_9_R1.inventory.CraftItemStack;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class MockPlayerInventory implements PlayerInventory {
    private int armorSize = 4, inventorySize = 36, heldSlot = 0, maxStackSize = 64;
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
        return this.inventoryContents[this.heldSlot];
    }

    @Override
    public void setItemInHand(ItemStack itemStack) {
        this.inventoryContents[this.heldSlot] = itemStack;
    }

    @Override
    public int getHeldItemSlot() {
        return this.heldSlot;
    }

    @Override
    public int clear(int i, int i2) {
        int count = 0;
        for (int d = i; d > i2; d++) {
            if (this.inventoryContents[d] != null) {
                this.inventoryContents[d] = null;
                count++;
            }
        }

        return count;
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
        return "MockInventory";
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
    public HashMap<Integer, ItemStack> addItem(ItemStack... items) {
        HashMap<Integer, ItemStack> leftover = new HashMap<Integer, ItemStack>();

        for (int i = 0; i < items.length; i++) {
            ItemStack item = items[i];

            for (;;) {
                int firstPartial = firstPartial(item);
                if (firstPartial == -1) {
                    int firstFree = firstEmpty();
                    if (firstFree == -1) {
                        leftover.put(Integer.valueOf(i), item);
                        break;
                    }

                    if (item.getAmount() > this.getMaxStackSize()) {
                        CraftItemStack stack = CraftItemStack.asCraftCopy(item);
                        stack.setAmount(getMaxStackSize());
                        setItem(firstFree, stack);
                        item.setAmount(item.getAmount() - getMaxStackSize());
                    } else {
                        setItem(firstFree, item);
                        break;
                    }
                } else {
                    ItemStack partialItem = getItem(firstPartial);

                    int amount = item.getAmount();
                    int partialAmount = partialItem.getAmount();
                    int maxAmount = partialItem.getMaxStackSize();

                    if (amount + partialAmount <= maxAmount) {
                        partialItem.setAmount(amount + partialAmount);

                        setItem(firstPartial, partialItem);
                        break;
                    }

                    partialItem.setAmount(maxAmount);

                    setItem(firstPartial, partialItem);
                    item.setAmount(amount + partialAmount - maxAmount);
                }
            }
        }

        return leftover;
    }

    @Override
    public HashMap<Integer, ItemStack> removeItem(ItemStack... items) {
        HashMap<Integer, ItemStack> leftover = new HashMap<Integer, ItemStack>();

        for (int i = 0; i < items.length; i++) {
            ItemStack item = items[i];

            int toDelete = item.getAmount();

            do {
                int first = first(item, false);
                if (first == -1) {
                    item.setAmount(toDelete);
                    leftover.put(Integer.valueOf(i), item);
                    break;
                }

                ItemStack itemStack = getItem(first);
                int amount = itemStack.getAmount();
                if (amount <= toDelete) {
                    toDelete -= amount;

                    clear(first);
                } else {
                    itemStack.setAmount(amount - toDelete);
                    setItem(first, itemStack);
                    toDelete = 0;
                }
            } while (toDelete > 0);
        }
        return leftover;
    }

    @Override
    public ItemStack[] getContents() {
        List<ItemStack> contents = new ArrayList<ItemStack>();
        
        for(int content = 0; content < this.inventorySize; content++) {
            contents.add(this.inventoryContents[content]);
        }
        
        for(int armor = 0; armor < this.armorSize; armor++) {
            contents.add(this.armorContents[armor]);
        }
        
        return contents.toArray(new ItemStack[contents.size()]);
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

    @SuppressWarnings("deprecation")
    @Override
    public int first(int materialId) {
        ItemStack[] inventory = getStorageContents();
        for (int i = 0; i < inventory.length; i++) {
            ItemStack item = inventory[i];
            if (item != null && item.getTypeId() == materialId) {
                return i;
            }
        }

        return -1;
    }

    @SuppressWarnings("deprecation")
    @Override
    public int first(Material material) {
        return first(material.getId());
    }

    @Override
    public int first(ItemStack itemStack) {
        return first(itemStack, true);
    }

    private int first(ItemStack item, boolean withAmount) {
        if (item == null) {
            return -1;
        }

        ItemStack[] inventory = getStorageContents();

        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] != null) {
                if (withAmount ? item.equals(inventory[i]) : item.isSimilar(inventory[i])) {
                    return i;
                }
            }
        }

        return -1;
    }

    @Override
    public int firstEmpty() {
        ItemStack[] inventory = getStorageContents();
        for (int i = 0; i < inventory.length; i++) {
            if (inventory[i] == null) {
                return i;
            }
        }

        return -1;
    }

    private int firstPartial(ItemStack item) {
        ItemStack[] inventory = getStorageContents();
        ItemStack filteredItem = CraftItemStack.asCraftCopy(item);

        if (item == null) {
            return -1;
        }

        for (int i = 0; i < inventory.length; i++) {
            ItemStack cItem = inventory[i];

            if (cItem != null && cItem.getAmount() < cItem.getMaxStackSize() && cItem.isSimilar(filteredItem)) {
                return i;
            }
        }
        return -1;
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
    public void clear(int index) {
        setItem(index, null);
    }

    @Override
    public void clear() {
        for (int i = 0; i < getSize(); i++) {
            clear(i);
        }
    }

    @Override
    public List<HumanEntity> getViewers() {
        return null;
    }

    @Override
    public String getTitle() {
        return "MockInventory";
    }

    @Override
    public InventoryType getType() {
        return InventoryType.PLAYER;
    }

    @Override
    public ListIterator<ItemStack> iterator() {
        return null;
    }

    @Override
    public int getMaxStackSize() {
        return this.maxStackSize;
    }

    @Override
    public void setMaxStackSize(int size) {
        this.maxStackSize = size;
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

    public void setHeldItemSlot(int slot) {
        this.heldSlot = slot;
    }

    public Location getLocation() {
        return null;
    }

    public ItemStack getItemInMainHand() {
        return null;
    }

    public ItemStack getItemInOffHand() {
        return null;
    }

    public void setItemInMainHand(ItemStack item) {

    }

    public void setItemInOffHand(ItemStack item) {

    }

    public void setExtraContents(ItemStack[] contents) {

    }

    public ItemStack[] getExtraContents() {
        return null;
    }

    public void setStorageContents(ItemStack[] contents) {

    }

    public ItemStack[] getStorageContents() {
        return getContents();
    }
}
