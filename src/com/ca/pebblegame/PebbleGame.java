// C:\Users\mitch\Google Drive\University\2nd Year\ECM2414 - Software Development\Coursework - Copy\ecm2414-ca\bag1.csv

package com.ca.pebblegame;

import java.io.*;
import java.util.Random;

public class PebbleGame {

    /**
     * @param args the command line arguments
     */

    Bag[] whiteBags;
    Bag[] blackBags;

    public Player[] players;

    public static void main(String[] args) {
        PebbleGame pebbleGame = new PebbleGame();
        if (pebbleGame.clearLogs()) {
            pebbleGame.runGame();
        } else {
            System.out.println("Log cleaning error: Aborting simulation.");
        }
    }

    private void runGame() {

        String[] bagNames = {"X", "Y", "Z","A","B","C"};

        try {
            int numPlayers = getNumPlayers();
            players = new Player[numPlayers];

            whiteBags = new Bag[3];
            blackBags = new Bag[3];

            for (int i = 0; i < 3; i++) {
                blackBags[i] = new Bag(bagNames[i]);
                try {
                    blackBags[i].readWeights(numPlayers, getFileLocation(bagNames[i]));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                whiteBags[i] = new Bag(bagNames[i + 3]);
            }

            for (int j = 0; j < numPlayers; j++) {
                players[j] = new Player("player" + Integer.toString(j + 1));
                players[j].start();
            }
        } catch (UserQuitException uqe) {
            System.out.println("Thank you for playing. Bye bye.");
        }
    }

    public boolean clearLogs() {
        File[] files = new File("logs").listFiles();
        if (files != null) for (File f : files) f.delete();
        return true;
    }

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

    public String getFileLocation(String bagName) throws UserQuitException {
        String resp = readInput("File location for bag " + bagName + ": ");
        if (!(resp.equals("E") || resp.equals("e"))) {
            return resp;
        }
        throw new UserQuitException("Command to end game received.");
    }

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

    public void endGame(Thread winner) {
        for (Thread player: players) {
            if (player != winner) {
                player.interrupt();
            }
        }
    }

    public void dump(int bagPair) {
        if (bagPair >= 0 && bagPair <= 2) {
            this.blackBags[bagPair].fill(
                    this.whiteBags[bagPair].drop()
            );
        } else {
            throw new IndexOutOfBoundsException("Invalid bagPair entered.");
        }
    }

    public void setBags(Bag[] blackBags, Bag[] whiteBags) {
        this.blackBags = blackBags;
        this.whiteBags = whiteBags;
    }

    public class Player extends Thread {
        private Random rnd = new Random();
        private int currentBag;
        private Bag hand;

        public Player(String name) {
            super(name);
            hand = new Bag();
        }

        @Override
        public void run() {
            boolean won = false;

            while (hand.contents().length < 10) {
                draw();
            }

            while (!Thread.currentThread().isInterrupted()) {
                won = checkHand();
                if (!won) {
                    discard();
                    draw();
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

        public boolean checkHand() {
            int total = sumHand();
            return total == 100;
        }

        private int sumHand() {
            int total = 0;

            for (int i : hand.contents())
                total += i;
            return total;
        }

        private int getNewBag() {
            return rnd.nextInt(3);
        }

        private void log(String message) {
            try {
                PrintWriter printer = new PrintWriter(new FileWriter("logs/" + Thread.currentThread().getName() + "_output.txt", true));
                printer.printf("%s" + "%n", message);
                printer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        private int discard() {
            int oldPebble = hand.remove(0);
            if (oldPebble == -1) {
                System.out.println("Cheese");
            }
            PebbleGame.this.whiteBags[currentBag].add(oldPebble);
            log(Thread.currentThread().getName() + " has discarded a " + Integer.toString(oldPebble) + " to bag " + whiteBags[currentBag].getName());
            return oldPebble;
        }

        private int draw() {
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
