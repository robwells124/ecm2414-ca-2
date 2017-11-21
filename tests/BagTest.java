import com.ca.pebblegame.Bag;
import com.ca.pebblegame.PebbleGame;
import com.ca.pebblegame.PebbleWeightException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;

import static org.junit.Assert.*;

/**
 * Test class for the Bag object.
 */
public class BagTest {

    Bag testBag = new Bag();

    @Before
    public void setUp() throws Exception {
    }
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test Bag()
     */
    @Test
    public void constructorTest() {
        Bag bag = new Bag();
        assertEquals(bag.getName(), "Not defined.");
    }
    /**
     * Test overloaded Bag() with name parameter.
     */
    @Test
    public void constructorTestWithName() {
        Bag bag = new Bag("Test");
        assertEquals(bag.getName(), "Test");
    }

    /**
     * Test Bag.readWeights
     * @throws Exception
     */
    @Test
    public void readWeightsTest() throws Exception {
        int[] expectedPebbles = new int[] {
                10, 10, 10, 10, 10,
                10, 10, 10, 10, 10,
                10, 0,0,0,0,0,0,0,0,0
        };
        testBag.readWeights(1,"test_bag.csv");

        assertTrue(testBag.getElementsUsed() == 11);
    }
    /**
     * Test Bag.readWeights - Invalid pebbles weights given.
     * @throws PebbleWeightException
     */
    @Test(expected = PebbleWeightException.class)
    public void readWeightsTestWeightFail() throws Exception {
        testBag.readWeights(1,"test_bag_weight_fail.csv");
    }

    /**
     * Test Bag.getName
     */
    @Test
    public void getNameTest() {
        assertEquals(testBag.getName(), "Not defined.");
    }

    /**
     * Test Bag.add
     */
    @Test
    public void addTest() {
        testBag.add(1);
        assertEquals(testBag.contents()[0], 1);
    }

    /**
     * Test Bag.get
     */
    @Test
    public void getTest() {
        testBag.add(1);
        testBag.add(2);
        assertEquals(testBag.get(1), 2);
    }
    /**
     * Test Bag.get - Index outside of boundary.
     */
    @Test
    public void getTestIndexFail() {
        assertEquals(testBag.get(999), -1);
    }

    /**
     * Test Bag.remove
     */
    @Test
    public void removeTest() {
        testBag.add(1);
        assertEquals(testBag.remove(0), 1);
    }
    /**
     * Test Bag.remove - Index outside of boundary.
     */
    @Test
    public void removeTestIndexFail() {
        assertEquals(testBag.remove(999), -1);
    }

    /**
     * Test Bag.contents - Bag contains no elements.
     */
    @Test
    public void contentsTest() {
        assertEquals(testBag.contents().length, 0);
    }
    /**
     * Test Bag.contents - Bag contains elements.
     */
    @Test
    public void contentsTestWithElements() {
        int[] expected = new int[] {1,2};
        boolean equal = true;
        testBag.add(1);
        testBag.add(2);
        int[] contents = testBag.contents();

        if (contents.length != expected.length) {
            equal = false;
        }

        for (int i=0; i<contents.length; i++) {
            if (contents[i] != expected[i]) {
                equal = false;
            }
        }
        assertTrue(equal);
    }

    /**
     * Test Bag.contentsAsString - Bag contains no elements.
     */
    @Test
    public void contentsAsStringTestEmpty() {
        assertEquals(testBag.contentsAsString().length, 0);
    }
    /**
     * Test Bag.contentsAsString - Bag contains elements.
     */
    @Test
    public void contentsAsStringTestWithElements() {
        String[] expected = new String[] {"1","2"};
        boolean equal = true;
        testBag.add(1);
        testBag.add(2);
        String[] contents = testBag.contentsAsString();

        if (contents.length != expected.length) {
            equal = false;
        }

        for (int i=0; i<contents.length; i++) {
            if (!contents[i].equals(expected[i])) {
                equal = false;
            }
        }

        assertTrue(equal);
    }

    /**
     * Test Bag.fill
     * @throws Exception
     */
    @Test
    public void fillTest() throws Exception {
        int[] test_pebbles = new int[] {1,2,3,4,5,6,7,8,9};

        testBag.fill(test_pebbles);

        assertEquals(testBag.contents().length, test_pebbles.length);
    }
    /**
     * Test Bag.fill - Invalid weights given.
     * @throws PebbleWeightException
     */
    @Test(expected = PebbleWeightException.class)
    public void fillTestStringFail() throws Exception {
        int[] test_pebbles = new int[] {-1,2,3,4,5,6,7,8,9};

        testBag.fill(test_pebbles);
    }

    /**
     * Test Bag.drop - Bag contains no elements.
     * @throws Exception
     */
    @Test
    public void dropTestEmpty() throws Exception {
        assertTrue(testBag.drop().length == 0);
    }
    /**
     * Test Bag.drop - Bag contains elements.
     * @throws Exception
     */
    @Test
    public void dropTestWithElements() throws Exception {
        int[] test_pebbles = new int[] {1,2,3,4,5,6,7,8,9};
        testBag.fill(test_pebbles);

        assertEquals(testBag.drop().length, test_pebbles.length);
    }

}