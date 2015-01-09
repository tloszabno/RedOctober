import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.List;

import model.FriendFilter;
import model.Player;
import model.Torpedo;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


public class FriendFilterTest {

	private static Player my;
	private static List<Player> all;
	private static List<Torpedo> torpedos;
	private static double radar_range = 100;
	private static double intervals = 0.2;
	private static Torpedo michal_torp;
	private static Torpedo szymon_torp;
	private static Torpedo mateusz_torp;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		my = new Player("marcin", "red", .0, .0, 1.0, 0.0);
		Player michal = new Player("michal", "red",  200.0, .0, 1.0, 0.0);
		Player szymon = new Player("szymon", "blue",  50.0, .0, 1.0, 0.0);
		Player mateusz = new Player("mateusz", "blue",  400.0, .0, 1.0, 0.0);
		all = new LinkedList<Player>();
		all.add(my);
		all.add(michal);
		all.add(szymon);
		all.add(mateusz);
		michal_torp = new Torpedo("michal", 200.0, 0.0, 1.0, 0.0);
		szymon_torp = new Torpedo("szymon", 50.0, 0.0, 1.0, 0.0);
		mateusz_torp = new Torpedo("mateusz", 400.0, 0.0, 1.0, 0.0);
		torpedos = new LinkedList<Torpedo>();
		torpedos.add(michal_torp);
		torpedos.add(szymon_torp);
		torpedos.add(mateusz_torp);
	}

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testTorpedoes() {
		FriendFilter filter = new FriendFilter(my, all, torpedos, intervals, radar_range);
		List<Torpedo> filtered = filter.torpedoes();
		assertTrue(filtered.contains(michal_torp));
		assertTrue(filtered.contains(szymon_torp));
		assertFalse(filtered.contains(mateusz_torp));
	}

}
