package com.ca.pebblegame;

import java.io.*;
import java.util.Random;

/**
 * This class is designed to simulate the PebbleGame specified in the ECM2414 CA.
 * Uses console input and output.
 *
 * @version 1.0
 * @author 660021130
 * @author 650031807
 */
public class PebbleGame {
    Bag[] whiteBags;
    Bag[] blackBags;
    public Player[] players;

    /**
     * Main method used to start simulation of the game.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        PebbleGame pebbleGame = new PebbleGame();
        if (pebbleGame.clearLogs()) {
            try {
                int numPlayers = pebbleGame.getNumPlayers();
                String bagXLoc = pebbleGame.getFileLocation("X");
                String bagYLoc = pebbleGame.getFileLocation("Y");
                String bagZLoc = pebbleGame.getFileLocation("Z");

                pebbleGame.runGame(numPlayers, new String[] {bagXLoc, bagYLoc, bagZLoc});
            } catch (UserQuitException uqe) {
                System.out.println("Thank you for playing. Bye bye.");
            }
        } else {
            System.out.println("Log cleaning error: Aborting simulation.");
        }
    }

    /**
     * Main method used to start simulation of the game.
     * @param numPlayers Number of players to simulate.
     * @param bagLocations Array of three file locations to generate the bags from.
     */
    public void runGame(int numPlayers, String[] bagLocations) throws UserQuitException {

        String[] bagNames = {"X", "Y", "Z","A","B","C"};

        players = new Player[numPlayers];

        whiteBags = new Bag[3];
        blackBags = new Bag[3];

        for (int i = 0; i < 3; i++) {
            blackBags[i] = new Bag(bagNames[i]);
            try {
                blackBags[i].readWeights(numPlayers, bagLocations[i]);
            } catch (FileNotFoundException e) {
                System.out.println("Invalid file location. Relocate your files and try again.");
                throw new UserQuitException("Game ended. File error.");
            } catch (PebbleWeightException e) {
                System.out.println("Invalid pebble weights found in file. Please check your files and try again.");
                throw new UserQuitException("Game ended. Pebble weight error.");
            } catch (IOException e) {
                System.out.println("Critical IO failure. - Game terminating.");
                throw new UserQuitException("Game ended. IO Failure.");
            }
            whiteBags[i] = new Bag(bagNames[i + 3]);
        }

        for (int j = 0; j < numPlayers; j++) {
            players[j] = new Player("player" + Integer.toString(j + 1));
            players[j].start();
        }

        System.out.println("Let the game begin!");
    }

    /**
     * This function clears the local directory "logs" to prepare for a new game.
     */
    public boolean clearLogs() {
        File[] files = new File("logs").listFiles();
        if (files != null) for (File f : files) f.delete();
        return true;
    }

    /**
     * This function gets the user input player count, validates it and returns it.
     * @return Number of players specified by the user.
     * @throws UserQuitException This indicates that the user has terminated the game.
     */
    public int getNumPlayers() throws UserQuitException {
        int num;
        try {
            String resp = readInput("How many players? ");
            if ((resp.equals("E")) || (resp.equals("e"))) {
                throw new UserQuitException("Command to end game received.");
            }
            num = Integer.parseInt(resp);
            if (num < 0) {
                System.out.print("Must be a positive number of players. Try again.");
                return getNumPlayers();
            }
        } catch (NumberFormatException ex) {
            System.out.print("Invalid input. Try again.");
            return getNumPlayers();
        }

        return num;
    }

    /**
     * This function gets the user inputted file location, validates it and returns it.
     * @return File location specified by the user.
     * @throws UserQuitException This indicates that the user has terminated the game.
     */
    public String getFileLocation(String bagName) throws UserQuitException {
        String resp = readInput("File location for bag " + bagName + ": ");
        if (!(resp.equals("E") || resp.equals("e"))) {
            return resp;
        }
        throw new UserQuitException("Command to end game received.");
    }

    /**
     * This function prompts the user and returns their response.
     * @param message This is the prompt string displayed to the user.
     * @return Users response to prompt.
     * @throws UserQuitException This indicates that the user has terminated the game.
     */
    public String readInput(String message) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.print(message);
        String response;
        try {
            response = reader.readLine();
        } catch (IOException ex) {
            return "";
        }

        return response;
    }

    /**
     * This function gets called when a Player has won the game and it alerts all other Players.
     * @param winner The Player that has won the game.
     */
    public void endGame(Thread winner) {
        for (Thread player: players) {
            if (player != winner) {
                player.interrupt();
            }
        }
    }

    /**
     * This function dumps the content of a white bag into its paired black bag.
     * @param bagPair Index of the pair to dump.
     */
    public void dump(int bagPair) {
        if (bagPair >= 0 && bagPair <= 2) {
            try {
                this.blackBags[bagPair].fill(
                        this.whiteBags[bagPair].drop()
                );
            } catch (PebbleWeightException e) {
                e.printStackTrace();
            }
        } else {
            throw new IndexOutOfBoundsException("Invalid bagPair entered.");
        }
    }

    /**
     * This function overrides the current bags. Should only be used for testing purposes.
     * @param blackBags Array of bags to populate the Game's black bags.
     * @param whiteBags Array of bags to populate the Game's white bags.
     * @return True, indicating a successful reallocation.
     */
    public Boolean setBags(Bag[] blackBags, Bag[] whiteBags) {
        this.blackBags = blackBags;
        this.whiteBags = whiteBags;
        return true;
    }

    /**
     * This innerclass represents a Player in the game as a thread.
     */
    public class Player extends Thread {
        private Random rnd = new Random();
        private int currentBag;
        private Bag hand;

        /**
         * Constructor for the Player class, assigns the Thread Name.
         * @param name Name to be assigned to the Thread Name.
         */
        public Player(String name) {
            super(name);
            hand = new Bag();
        }

        /**
         * This function is inherited from the Thread object and is called on Player.start().
         */
        @Override
        public void run() {
            boolean won = false;

            while (hand.contents().length < 10) {
                draw();
            }

            while (!Thread.currentThread().isInterrupted()) {
                won = checkHand();
                if (!won) {
                    draw();
                    discard();
                    log(Thread.currentThread().getName() + " hand is " + String.join(", ", hand.contentsAsString()) + " - " + Integer.toString(sumHand()));
                } else {
                    System.out.println(Thread.currentThread().getName() + " has won the game.");
                    PebbleGame.this.endGame(this);
                    break;
                }
            }
            if (won && Thread.currentThread().isInterrupted()) {
                System.out.println(Thread.currentThread().getName() + " has drawn.");
            }
        }

        /**
         * This function checks whether the players hand contains a sum weight of 100.
         * @return True if hand weights 100.
         */
        public boolean checkHand() {
            int total = sumHand();
            return total == 100;
        }

        /**
         * This functions returns the total hand weight.
         * @return Combined pebble weight as int.
         */
        public int sumHand() {
            int total = 0;

            for (int i : hand.contents())
                total += i;
            return total;
        }

        /**
         * Fetch the current player hand.
         * @return Players hand as int array.
         */
        public int[] getHand() {
            return hand.contents();
        }
        /**
         * Override the current Player hand. Should only be used for testing purposes.
         * @param newHand Int array to replace the Players current hand.
         */
        public void setHand(int[] newHand) {
            try {
                hand.fill(newHand);
            } catch (PebbleWeightException e) {
                e.printStackTrace();
            }
        }

        /**
         * This function randomly generates a new bag index.
         * @return new bag index as int.
         */
        public int getNewBag() {
            return rnd.nextInt(3);
        }

        /**
         * Write a given message to the Player specific log file.
         * @param message Message to write to file.
         */
        public void log(String message) {
            try {
                PrintWriter printer = new PrintWriter(new FileWriter("logs/" + Thread.currentThread().getName() + "_output.txt", true));
                printer.printf("%s" + "%n", message);
                printer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * This function discards the oldest pebble in the Players hand.
         * @return The integer value of the pebble discarded.
         */
        public int discard() {
            int oldPebble = hand.remove(0);
            PebbleGame.this.whiteBags[currentBag].add(oldPebble);
            log(Thread.currentThread().getName() + " has discarded a " + Integer.toString(oldPebble) + " to bag " + whiteBags[currentBag].getName());
            return oldPebble;
        }

        /**
         * This function draws a new pebble from a random black bag to the players hand.
         * @return The integer value of the pebble drawn.
         */
        public int draw() {
            currentBag = getNewBag();
            int newPebble = 0;
            if (PebbleGame.this.blackBags[currentBag].contents().length > 0) {
                newPebble = PebbleGame.this.blackBags[currentBag].remove(0);
            } else {
                if (PebbleGame.this.whiteBags[currentBag].contents().length > 0) {
                    PebbleGame.this.dump(currentBag);
                    newPebble = PebbleGame.this.blackBags[currentBag].remove(0);
                } else {
                    newPebble = draw();
                }
            }
            hand.add(newPebble);
            log(Thread.currentThread().getName() + " has drawn a " + Integer.toString(newPebble) + " from bag " + blackBags[currentBag].getName());
            return newPebble;
        }
    }
}
