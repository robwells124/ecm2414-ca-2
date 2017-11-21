import com.ca.pebblegame.Bag;
import com.ca.pebblegame.PebbleGame;
import com.ca.pebblegame.PebbleGame.Player;
import com.ca.pebblegame.UserQuitException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;

import static org.junit.Assert.*;

public class PebbleGameTest {

    private final ByteArrayOutputStream os = new ByteArrayOutputStream();
    private PebbleGame game = new PebbleGame();
    Player test_Player;

    @Before
    public void setUp() throws Exception {
        System.setOut(new PrintStream(os));
    }
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void readInputTest() {
        ByteArrayInputStream in = new ByteArrayInputStream("Test".getBytes());
        System.setIn(in);
        String response = game.readInput("Test");
        assertEquals(response, "Test");
    }

    @Test
    public void getNumPlayersTest() throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream("1".getBytes());
        System.setIn(in);
        int response = game.getNumPlayers();
        assertEquals(response, 1);
    }
    @Test(expected = NullPointerException.class)
    public void getNumPlayersTestStringFail() throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream("Fail\n1".getBytes());
        System.setIn(in);
        game.getNumPlayers();
    }
    @Test(expected = UserQuitException.class)
    public void getNumPlayersTestQuit() throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream("E".getBytes());
        System.setIn(in);
        game.getNumPlayers();
    }

    @Test
    public void getFileLocationTest() throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream("test".getBytes());
        System.setIn(in);
        String response = game.getFileLocation("Test");
        assertEquals(response, "test");
    }
    @Test(expected = UserQuitException.class)
    public void getFileLocationQuit() throws Exception {
        ByteArrayInputStream in = new ByteArrayInputStream("E".getBytes());
        System.setIn(in);
        game.getFileLocation("Test");
    }

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

    @Test
    public void endGameTest() {
        test_Player = new PebbleGame().new Player("test_player");

        game.players = new Player[] {test_Player};
        game.endGame(null);
        assertTrue(test_Player.isInterrupted());
    }

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

}