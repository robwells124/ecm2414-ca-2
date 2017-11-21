import com.ca.pebblegame.Bag;
import com.ca.pebblegame.PebbleGame;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class PlayerTest {

    private final ByteArrayOutputStream os = new ByteArrayOutputStream();
    PebbleGame game = new PebbleGame();
    PebbleGame.Player testPlayer;


    @Before
    public void setUp() {
        System.setOut(new PrintStream(os));
        Bag[] whiteBags = new Bag[3];
        Bag[] blackBags = new Bag[3];

        String[] bagNames = {"X", "Y", "Z","A","B","C"};

        for (int i = 0; i < 3; i++) {
            whiteBags[i] = new Bag(bagNames[i]);
            try {
                whiteBags[i].readWeights(1, "test_bag.csv");
            } catch (Exception e) {
                e.printStackTrace();
            }
            blackBags[i] = new Bag(bagNames[i + 3]);
        }

        game.setBags(blackBags, whiteBags);

        testPlayer = game.new Player("test_player");
        game.players = new PebbleGame.Player[] {testPlayer};
    }
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void runTest() {
        setUp();
        testPlayer.run();
        String resp = os.toString();
        assertTrue(resp.contains("won"));
    }

    @Test
    public void checkHandTest() {
        setUp();
        testPlayer.setHand(new int[] {10,10,10,10,10,10,10,10,10,10});
        assertTrue(testPlayer.checkHand());
    }
    @Test
    public void checkHandTestEmptyHand() {
        setUp();
        assertFalse(testPlayer.checkHand());
    }
    @Test
    public void checkHandTestFail() {
        setUp();
        testPlayer.setHand(new int[] {1,1,1,1,1,1,1,1,1,1});
        assertFalse(testPlayer.checkHand());
    }

    @Test
    public void sumHandTest() {
        setUp();
        assertTrue(testPlayer.sumHand() == 0);
    }
    @Test
    public void sumHandTestEmpty() {
        setUp();
        assertTrue(testPlayer.sumHand() == 0);
    }

    @Test
    public void getNewBagTest() {

    }

    @Test
    public void logTest() {

    }

    @Test
    public void discardTest() {}

    @Test
    public void drawTest() {}

}