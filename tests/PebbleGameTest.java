import com.ca.pebblegame.Bag;
import com.ca.pebblegame.PebbleGame;
import com.ca.pebblegame.PebbleGame.Player;
import com.ca.pebblegame.PebbleWeightException;
import com.ca.pebblegame.UserQuitException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

/**
 * Test class for the PebbleGame object.
 */
public class PebbleGameTest {

    private final ByteArrayOutputStream os = new ByteArrayOutputStream();
    private PebbleGame game = new PebbleGame();
    private Player test_Player;

    @Before
    public void setUp() {
        System.setOut(new PrintStream(os));
        test_Player = game.new Player("test_player");
    }
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test PebbleGame.runGame
     * @throws Exception
     */
    @Test
    public void runGameTest() throws Exception {
        setUp();
        game.runGame(1, new String[] {"test_bag.csv", "test_bag.csv", "test_bag.csv"});
        String resp = os.toString();
        assertTrue(resp.contains("Let the game begin!"));
    }

    /**
     * Test PebbleGame.readInput
     * @throws Exception
     */
    @Test
    public void readInputTest() {
        ByteArrayInputStream in = new ByteArrayInputStream("Test".getBytes());
        System.setIn(in);
        String response = game.readInput("Test");
        assertEquals(response, "Test");
    }

    /**
     * Test PebbleGame.getNumPlayers
     * @throws Exception
     */
    @Test
    public void getNumPlayersTest() throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream("1".getBytes());
        System.setIn(in);
        int response = game.getNumPlayers();
        assertEquals(response, 1);
    }
    /**
     * Test PebbleGame.getNumPlayers - Bad input test
     * @throws NullPointerException
     */
    @Test(expected = NullPointerException.class)
    public void getNumPlayersTestStringFail() throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream("Fail\n1".getBytes());
        System.setIn(in);
        game.getNumPlayers();
    }
    /**
     * Test PebbleGame.getNumPlayers - User quit simulation
     * @throws Exception
     */
    @Test(expected = UserQuitException.class)
    public void getNumPlayersTestQuit() throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream("E".getBytes());
        System.setIn(in);
        game.getNumPlayers();
    }

    /**
     * Test PebbleGame.getFileLocation
     * @throws Exception
     */
    @Test
    public void getFileLocationTest() throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream("test".getBytes());
        System.setIn(in);
        String response = game.getFileLocation("Test");
        assertEquals(response, "test");
    }
    /**
     * Test PebbleGame.getFileLocation - User quit simulation
     * @throws UserQuitException
     */
    @Test(expected = UserQuitException.class)
    public void getFileLocationQuit() throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream("E".getBytes());
        System.setIn(in);
        game.getFileLocation("Test");
    }

    /**
     * Test PebbleGame.clearLog
     */
    @Test
    public void clearLogTest() {
        try {
            PrintWriter printer = new PrintWriter(new FileWriter("logs/test_output.txt", true));
            printer.printf("%s" + "%n", "Test");
            printer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        game.clearLogs();
        assertTrue(new File("logs").listFiles().length == 0);
    }

    /**
     * Test PebbleGame.endGame
     */
    @Test
    public void endGameTest() {
        setUp();

        game.players = new Player[] {test_Player};

        Bag[] whiteBags = new Bag[3];
        Bag[] blackBags = new Bag[3];
        String[] bagNames = {"X", "Y", "Z","A","B","C"};

        for (int i = 0; i < 3; i++) {
            blackBags[i] = new Bag(bagNames[i]);
            try {
                blackBags[i].readWeights(1, "test_bag_no_win.csv");
            } catch (Exception e) {
                e.printStackTrace();
            }
            whiteBags[i] = new Bag(bagNames[i + 3]);
        }

        game.setBags(blackBags, whiteBags);
        test_Player.start();
        game.endGame(null);
        assertTrue(test_Player.isInterrupted());
    }

    /**
     * Test PebbleGame.dump
     */
    @Test
    public void dumpTest() {
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

        game.dump(0);
    }
    /**
     * Test PebbleGame.dump - Index past boundary test.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void dumpTestFail() {
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

        game.dump(-1);
    }

    /**
     * Test PebbleGame.setBags
     */
    @Test
    public void setBagsTest() {
        Bag[] whiteBags = new Bag[3];
        Bag[] blackBags = new Bag[3];

        for (int i = 0; i < 3; i++) {
            whiteBags[i] = new Bag();
            blackBags[i] = new Bag();
        }

        assertTrue(game.setBags(blackBags, whiteBags));
    }

    /**
     * Test PebbleGame.setHand
     */
    @Test
    public void setHandTest() {
        setUp();
        boolean equal = true;

        int[] expected = new int[] {1,1,1,1,1,1,1,1,1,1};
        test_Player.setHand(expected);

        int[] contents = test_Player.getHand();

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
     * Test PebbleWeightException
     * @throws PebbleWeightException
     */
    @Test(expected = PebbleWeightException.class)
    public void pebbleWeightExceptionTest() throws PebbleWeightException {
        throw new PebbleWeightException("Test");
    }
    /**
     * Test UserQuitException
     * @throws UserQuitException
     */
    @Test(expected = UserQuitException.class)
    public void userQuitExceptionTest() throws UserQuitException {
        throw new UserQuitException("Test");
    }

}