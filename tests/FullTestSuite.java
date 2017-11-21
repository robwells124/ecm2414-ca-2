import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Test Suite for PebbleGame, PebbleGame.Player and Bag.
 */
@RunWith(Suite.class)
@SuiteClasses({
        PebbleGameTest.class,
        BagTest.class,
        PlayerTest.class
})
public class FullTestSuite {}
