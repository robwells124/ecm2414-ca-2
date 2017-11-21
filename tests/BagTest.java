import com.ca.pebblegame.Bag;
import com.ca.pebblegame.PebbleGame;
import com.ca.pebblegame.PebbleWeightException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;

import static org.junit.Assert.*;

public class BagTest {

    Bag testBag = new Bag();

    @Before
    public void setUp() throws Exception {
    }
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void constructorTest() {
        Bag bag = new Bag();
        assertEquals(bag.getName(), "Not defined.");
    }
    @Test
    public void constructorTestWithName() {
        Bag bag = new Bag("Test");
        assertEquals(bag.getName(), "Test");
    }

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
    @Test(expected = PebbleWeightException.class)
    public void readWeightsTestWeightFail() throws Exception {
        testBag.readWeights(1,"test_bag_weight_fail.csv");
    }

    @Test
    public void getNameTest() {
        assertEquals(testBag.getName(), "Not defined.");
    }

    @Test
    public void addTest() {
        testBag.add(1);
        assertEquals(testBag.contents()[0], 1);
    }

    @Test
    public void getTest() {
        testBag.add(1);
        testBag.add(2);
        assertEquals(testBag.get(1), 2);
    }
    @Test
    public void getTestIndexFail() {
        assertEquals(testBag.get(999), -1);
    }

    @Test
    public void removeTest() {
        testBag.add(1);
        assertEquals(testBag.remove(0), 1);
    }
    @Test
    public void removeTestIndexFail() {
        assertEquals(testBag.remove(999), -1);
    }

    @Test
    public void contentsTest() {
        assertEquals(testBag.contents().length, 0);
    }
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

    @Test
    public void contentsAsStringTestEmpty() {
        assertEquals(testBag.contentsAsString().length, 0);
    }
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
            if (contents[i] != expected[i]) {
                equal = false;
            }
        }

        assertTrue(equal);
    }

    @Test
    public void fillTest() throws Exception {
        int[] test_pebbles = new int[] {1,2,3,4,5,6,7,8,9};

        testBag.fill(test_pebbles);

        assertEquals(testBag.contents().length, test_pebbles.length);
    }
    @Test(expected = PebbleWeightException.class)
    public void fillTestStringFail() throws Exception {
        int[] test_pebbles = new int[] {1,2,3,4,5,6,7,8,9};

        testBag.fill(test_pebbles);

        assertEquals(testBag.contents().length, test_pebbles.length);
    }

    @Test
    public void dropTestEmpty() throws Exception {
        assertTrue(testBag.drop().length == 0);
    }
    @Test
    public void dropTestWithElements() throws Exception {
        int[] test_pebbles = new int[] {1,2,3,4,5,6,7,8,9};
        testBag.fill(test_pebbles);

        assertEquals(testBag.drop().length, test_pebbles.length);
    }

}