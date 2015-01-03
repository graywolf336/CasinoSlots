package test.com.craftyn.casinoslots;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.bukkit.plugin.PluginDescriptionFile;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import test.com.craftyn.casinoslots.util.TestInstanceCreator;

import com.craftyn.casinoslots.CasinoSlots;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ CasinoSlots.class, PluginDescriptionFile.class })
public class TestPermissionUtil {
    private static TestInstanceCreator creator;
    private static CasinoSlots main;

    @BeforeClass
    public static void setUp() throws Exception {
        creator = new TestInstanceCreator();
        assertNotNull("The instance creator is null.", creator);
        assertTrue(creator.setup());
        main = creator.getMain();
        assertNotNull("The CasinoSlots class is null.", main);
    }

    @AfterClass
    public static void tearDown() throws Exception {
        creator.tearDown();
        main = null;
    }

    @Test
    public void verifyPermissionClassExists() {
        assertNotNull("The permission class is null.", main.permission);
    }

    @Test
    public void testAdminPermissions() {
        assertTrue("The op'd player isn't an admin.", main.permission.isAdmin(creator.getOpPlayer()));
        assertTrue("The op'd player can't create slot machines.", main.permission.canCreate(creator.getOpPlayer()));
        assertTrue("The op'd player can't create a slot machine with a type of 'testing'.", main.permission.canCreate(creator.getOpPlayer(), "testing"));
        assertTrue("The op'd player can't create managed slots.", main.permission.canCreateManaged(creator.getOpPlayer()));
        assertTrue("The op'd player can't create item slot with type of 'testingItem'.", main.permission.canCreateItemsType(creator.getOpPlayer(), "testingItem"));
    }

    @Test
    public void testNoPermissions() {
        assertFalse("The non-op is an admin.", main.permission.isAdmin(creator.getNotOpPlayer()));
        assertFalse("The non-op player can create slot machines.", main.permission.canCreate(creator.getNotOpPlayer()));
        assertFalse("The non-op player can create a slot machine with a type of 'testing'.", main.permission.canCreate(creator.getNotOpPlayer(), "testing"));
        assertFalse("The non-op player can create managed slots.", main.permission.canCreateManaged(creator.getNotOpPlayer()));
        assertFalse("The non-op player can create item slot with type of 'testingItem'.", main.permission.canCreateItemsType(creator.getNotOpPlayer(), "testingItem"));
    }
}
