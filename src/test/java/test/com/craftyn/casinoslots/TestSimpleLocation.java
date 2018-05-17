package test.com.craftyn.casinoslots;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.PluginDescriptionFile;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.craftyn.casinoslots.CasinoSlots;
import com.craftyn.casinoslots.classes.SimpleLocation;

import test.com.craftyn.casinoslots.util.TestInstanceCreator;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ CasinoSlots.class, PluginDescriptionFile.class })
public class TestSimpleLocation {
	private static TestInstanceCreator creator;
	private static World world;

	@BeforeClass
    public static void setUp() throws Exception {
        creator = new TestInstanceCreator();
        assertNotNull("The instance creator is null.", creator);
        assertTrue("Some reason the creator setup failed", creator.setup());
        assertNotNull("The world instance is null", Bukkit.getWorld("world"));
        world = Bukkit.getWorld("world");
	}
	
    @AfterClass
    public static void tearDown() throws Exception {
        creator.tearDown();
    }

	@Test
	public void testSimpleLocationLocation() {
		assertNotNull("The simple location returned null on a new instance", new SimpleLocation(world.getSpawnLocation()));
	}

	@Test
	public void testSimpleLocationStringDoubleDoubleDouble() {
		assertNotNull("The simple location returned null on a new instance", new SimpleLocation("world", 1.1, 2.2, 3.3));
	}

	@Test
	public void testSimpleLocationStringDoubleDoubleDoubleFloatFloat() {
		assertNotNull("The simple location returned null on a new instance",
				new SimpleLocation("world", 1.1, 2.2, 3.3, (float)0.3334, (float)0.55556));
	}

	@Test
	public void testGetWorld() {
		Location l = world.getSpawnLocation();
		SimpleLocation sl = new SimpleLocation(l);

		assertEquals("The worlds are not equal", l.getWorld(), sl.getWorld());
	}

	@Test
	public void testGetWorldName() {
		SimpleLocation sl = new SimpleLocation(world.getSpawnLocation());
		
		assertEquals("The world names are not equal", "world", sl.getWorldName());
	}

	@Test
	public void testHasValidWorld() {
		SimpleLocation sl = new SimpleLocation("world", 1.1, 2.2, 3.3);
		
		assertTrue("The world doesn't seem to be valid", sl.hasValidWorld());
	}

	@Test
	public void testGetX() {
		SimpleLocation sl = new SimpleLocation("world", 1.1, 2.2, 3.3);
		
		assertEquals("The x coordinate doesn't match up", 1.1, sl.getX(), 0.0001);
	}

	@Test
	public void testGetY() {
		SimpleLocation sl = new SimpleLocation("world", 1.1, 2.2, 3.3);
		
		assertEquals("The y coordinate doesn't match up", 2.2, sl.getY(), 0.0001);
	}

	@Test
	public void testGetZ() {
		SimpleLocation sl = new SimpleLocation("world", 1.1, 2.2, 3.3);
		
		assertEquals("The z coordinate doesn't match up", 3.3, sl.getZ(), 0.0001);
	}

	@Test
	public void testGetYaw() {
		SimpleLocation sl = new SimpleLocation("world", 1.1, 2.2, 3.3, (float)0.3334, (float)0.55556);
		
		assertEquals("The yaw is messed up", 0.3334, sl.getYaw(), 0.00001);
	}

	@Test
	public void testGetPitch() {
		SimpleLocation sl = new SimpleLocation("world", 1.1, 2.2, 3.3, (float)0.3334, (float)0.55556);
		
		assertEquals("The yaw is messed up", 0.55556, sl.getPitch(), 0.00001);
	}

	@Test
	public void testGetLocation() {
		SimpleLocation sl = new SimpleLocation("world", 1.1, 2.2, 3.3, (float)0.3334, (float)0.55556);
		
		assertNotNull("The resulting location is null", sl.getLocation());
		
		Location l = sl.getLocation();
		
		assertEquals("The worlds don't align", sl.getWorld(), l.getWorld());
		assertEquals("The x coord isn't the same", sl.getX(), l.getX(), 0.00001);
		assertEquals("The y coord isn't the same", sl.getY(), l.getY(), 0.00001);
		assertEquals("The z coord isn't the same", sl.getZ(), l.getZ(), 0.00001);
		assertEquals("The pitch isn't the same", sl.getPitch(), l.getPitch(), 0.00001);
		assertEquals("The yaw isn't the same", sl.getYaw(), l.getYaw(), 0.00001);
	}

	@Test
	public void testGetBlock() {
		SimpleLocation sl = new SimpleLocation("world", 1.1, 2.2, 3.3, (float)0.3334, (float)0.55556);
		SimpleLocation invalidWorld = new SimpleLocation("nope", 1.1, 2.2, 3.3, (float)0.3334, (float)0.55556);
		
		assertNotNull("The block does not exist", sl.getBlock());
		assertNull("The world does exist to get a block from", invalidWorld.getBlock());
	}

	@Test
	public void testToString() {
		SimpleLocation sl = new SimpleLocation("world", 1.1, 2.2, 3.3, (float)0.3334, (float)0.55556);
		
		assertEquals("The to string returned an invalid result", "world,1.1,2.2,3.3,0.3334,0.55556", sl.toString());
	}

	@Test
	public void testToBlockString() {
		SimpleLocation sl = new SimpleLocation("world", 1.1, 2.2, 3.3, (float)0.3334, (float)0.55556);
		
		assertEquals("The to block string returned an invalid result", "world,1,2,3,0,0", sl.toBlockString());
	}

	@Test
	public void testSerialize() {
		SimpleLocation sl = new SimpleLocation("world", 1.1, 2.2, 3.3, (float)0.3334, (float)0.55556);
		
		assertNotNull(sl.serialize());
		
		Map<String, Object> result = sl.serialize();
		
		assertEquals("The world isn't as expected", "world", result.get("world"));
		assertEquals("The x coordinate isn't what we want", 1.1, result.get("x"));
		assertEquals("The y coordinate isn't what we want", 2.2, result.get("y"));
		assertEquals("The z coordinate isn't what we want", 3.3, result.get("z"));
		assertEquals("The yaw isn't what we want", (float) 0.3334, result.get("yaw"));
		assertEquals("The pitch isn't what we want", (float) 0.55556, result.get("pitch"));
	}

	@Test
	public void testDeserialize() {
        Map<String, Object> map = new HashMap<String, Object>();

        map.put("world", "world");
        map.put("x", 1.1);
        map.put("y", 2.2);
        map.put("z", 3.3);
        map.put("yaw", (float) 0.3334);
        map.put("pitch", (float) 0.55556);

		assertNotNull("The deserialized result is null", SimpleLocation.deserialize(map));
		
		SimpleLocation sl = SimpleLocation.deserialize(map);
		
		assertEquals("The world name isn't what we expect", "world", sl.getWorldName());
		assertEquals("The x coordinate isn't what we want", 1.1, sl.getX(), 0.0001);
		assertEquals("The y coordinate isn't what we want", 2.2, sl.getY(), 0.0001);
		assertEquals("The z coordinate isn't what we want", 3.3, sl.getZ(), 0.0001);
		assertEquals("The yaw isn't what we want", (float) 0.3334, sl.getYaw(), 0.0001);
		assertEquals("The pitch isn't what we want", (float) 0.55556, sl.getPitch(), 0.0001);
	}

}
