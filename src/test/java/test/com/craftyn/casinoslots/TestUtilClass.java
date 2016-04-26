package test.com.craftyn.casinoslots;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.PluginDescriptionFile;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import test.com.craftyn.casinoslots.util.TestInstanceCreator;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.util.Util;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ CasinoSlots.class, PluginDescriptionFile.class })
public class TestUtilClass {
    private static TestInstanceCreator creator;
    private static CasinoSlots main;
    
    @BeforeClass
    public static void setUp() throws Exception {
        creator = new TestInstanceCreator();
        assertNotNull("The instance creator is null.", creator);
        assertTrue(creator.setup());
        main = creator.getMain();
        assertNotNull("The CasinoSlots class is null.", main);
        assertTrue("The CasinoSlots plugin is disabled.", main.isEnabled());
    }

    @AfterClass
    public static void tearDown() throws Exception {
        creator.tearDown();
    }
    
    @Test
    public void testItemWithoutMetadata() {
        assertEquals("The itemStackToString output wasn't correct.", "iron_axe:0 16", Util.itemStackToString(new ItemStack(Material.IRON_AXE, 16)));
    }
    
    @Test
    public void testItemWithOnlyDisplayName() {
        ItemStack item = new ItemStack(Material.IRON_AXE, 16);
        ItemMeta m = item.getItemMeta();
        m.setDisplayName("This is a test");
        item.setItemMeta(m);
        
        assertEquals("The itemStackToString output wasn't correct.", "iron_axe:0 16 name:This_is_a_test", Util.itemStackToString(item));
    }
    
    @Test
    public void testItemWithOnlyColoredDisplayName() {
        ItemStack item = new ItemStack(Material.IRON_AXE, 16);
        ItemMeta m = item.getItemMeta();
        m.setDisplayName(ChatColor.AQUA + "This is a test");
        item.setItemMeta(m);
        
        assertEquals("The itemStackToString output wasn't correct.", "iron_axe:0 16 name:&bThis_is_a_test", Util.itemStackToString(item));
    }
    
    @Test
    public void testItemWithOnlyEnchantments() {
        ItemStack item = new ItemStack(Material.IRON_AXE, 16);
        item.addEnchantment(Enchantment.DURABILITY, 5);
        
        assertEquals("The itemStackToString output wasn't correct.", "iron_axe:0 16 durability:5", Util.itemStackToString(item));
    }
    
    @Test
    public void testItemWithTwoLoreLines() {
        ItemStack item = new ItemStack(Material.IRON_AXE, 16);
        ItemMeta m = item.getItemMeta();
        m.setLore(Arrays.asList(ChatColor.GOLD + "Use this to play", ChatColor.GOLD + "a Slot Machine!"));
        item.setItemMeta(m);
        
        assertEquals("The itemStackToString output wasn't correct.", "iron_axe:0 16 lore:&6Use_this_to_play|&6a_Slot_Machine!", Util.itemStackToString(item));
    }
    
    @Test
    public void testItemWithEverything() {
        ItemStack item = new ItemStack(Material.DIAMOND_AXE, 16);
        item.addUnsafeEnchantment(Enchantment.KNOCKBACK, 15);
        ItemMeta m = item.getItemMeta();
        m.setDisplayName(ChatColor.AQUA + "Slot Token");
        m.setLore(Arrays.asList(ChatColor.GOLD + "Use this to play", ChatColor.GOLD + "a Slot Machine!"));
        m.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        item.setItemMeta(m);
        
        assertEquals("The itemStackToString output wasn't correct.", "diamond_axe:0 16 knockback:15 name:&bSlot_Token lore:&6Use_this_to_play|&6a_Slot_Machine! flags:hide_attributes", Util.itemStackToString(item));
    }
    
    @SuppressWarnings("deprecation")
    @Test
    public void testMaterialDataToString() {
        assertEquals("The materialDataToString output wasn't correct.", "iron_axe:0", Util.materialDataToString(new MaterialData(Material.IRON_AXE)));
        assertEquals("The materialDataToString output wasn't correct.", "stone:3", Util.materialDataToString(new MaterialData(Material.STONE, (byte) 3)));
    }
    
    @SuppressWarnings("deprecation")
    @Test
    public void testMaterialDataFromString() {
        assertEquals("The parseMaterialDataFromString output for iron_axe wasn't equal.", new MaterialData(Material.IRON_AXE), Util.parseMaterialDataFromString("iron_axe"));
        assertEquals("The parseMaterialDataFromString output for iron_axe:0 wasn't equal.", new MaterialData(Material.IRON_AXE), Util.parseMaterialDataFromString("iron_axe:0"));
        assertEquals("The parseMaterialDataFromString output for stone:3 wasn't equal.", new MaterialData(Material.STONE, (byte) 3), Util.parseMaterialDataFromString("stone:3"));
    }
}
