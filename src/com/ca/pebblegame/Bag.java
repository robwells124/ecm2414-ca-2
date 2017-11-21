package com.ca.pebblegame;

import java.util.Random;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * This class is the bag object used by the PebbleGame class.
 * Used as the black and white bags, and the Player objects hand.
 *
 * @version 1.0
 * @author 660021130
 * @author 650031807
 */
public class Bag {
    private int[] pebbles;
    private int elementsUsed;
    private String name;

    /**
     * Constructor method for the bag object.
     * Instantiates the class variables.
     */
    public Bag() {
        this.pebbles = new int[10];
        this.elementsUsed = 0;
        this.name = "Not defined.";
    }

    /**
     * Overloading constructor method for the bag object.
     * Instantiates the class variables.
     * @param name Name assigned to the bag object for reference when logging.
     */
    public Bag(String name) {
        this.pebbles = new int[10];
        this.elementsUsed = 0;
        this.name = name;
    }

    /**
     * Function takes the number of players in the simulation and the file location to read the pebble weights into.
     * @param players Number of players in the simulation.
     * @param fileLocation File location as a string.
     * @throws FileNotFoundException Thrown when the given file location cannot be found.
     * @throws PebbleWeightException Thrown when the pebble weights passed in the file are invalid.
     * @throws IOException Thrown when IO faults occur.
     */
    public void readWeights(int players, String fileLocation) throws FileNotFoundException, PebbleWeightException, IOException {
        // Read from the file
        BufferedReader br = null;
        String line = "";
        br = new BufferedReader(new FileReader(fileLocation));
        for (int i = 0; i < 11 * players; i++) {
            if ((line = br.readLine()) != null) {
                int pebble = Integer.parseInt(line);
                if (pebble > 0) {
                    add(pebble);
                } else {
                    throw new PebbleWeightException("Pebble cannot have negative weight.");
                }
            }
        }
        if (br != null) {
            br.close();
        }
    }

    /**
     * Return the name assigned to the bag.
     * @return String name assigned to the bag.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Return the current number of pebbles in the bag.
     * @return Current amount as int.
     */
    public int getElementsUsed() {return this.elementsUsed; }

    /**
     * Method adds the argument to the end of the list. <code>null</code> reference
     * elements are not supported, so ensure that you do not add them.
     *
     * @param i int to be added to the bag.
     */
    public synchronized void add(int i) {
        if (this.elementsUsed == this.pebbles.length)
            this.resizeArray();
        this.pebbles[elementsUsed] = i;
        this.elementsUsed++;
    }

    /*
     * Method doubles the capacity of the array
     * Synchronized atomic action.
     */
    private synchronized void resizeArray() {
        int[] tempArray = new int[this.pebbles.length * 2];
        // Efficiently copy all the elements of array into tempArray
        System.arraycopy(this.pebbles, 0, tempArray, 0, this.pebbles.length);
        this.pebbles = tempArray;
    }

    /**
     * Method returns the element of the list at the index provided, will
     * return <code>-1</code> if the index is invalid
     *
     * @param index index of element in list to be returned.
     * @return pebble weight at corresponding index.
     */
    public int get(int index) {
        if (this.isInvalid(index))
            return -1;
        return this.pebbles[index];
    }

    /*
     * Checks validity of index given current range, returns true if not valid
     */
    private boolean isInvalid(int index) {
        return ((index < 0) || (index >= this.elementsUsed));
    }

    /*
     * Method contracts effectively removing the index item. If item beyond range of
     * array, returns false, otherwise returns true on successful removal
     */
    private void contract(int index) {
        // Efficiently copy all the elements of array beyond index down one
        // space, effectively removing the index element
        System.arraycopy(this.pebbles, index + 1, this.pebbles, index, this.elementsUsed - (index + 1));
        this.elementsUsed--;
    }

    /**
     * Method removes the element of the list at the index provided, will
     * return <code>-1</code> if the index is invalid. Otherwise will
     * return the instance removed
     * Synchronized atomic action.
     *
     * @param index index of element in list to be returned
     * @return pebble weight at corresponding index
     */
    public synchronized int remove(int index) {
        if (this.isInvalid(index))
            return -1;
        int value = this.get(index);
        this.contract(index);
        return value;
    }

    /**
     * Method returns the contents of this Bag as an array
     *
     * @return shallow copy of contents of bag as an array
     */
    public int[] contents() {
        int[] temp = new int[this.elementsUsed];
        for (int i = 0; i < this.elementsUsed; i++)
            temp[i] = this.pebbles[i];
        return temp;
    }

    /**
     * Method returns the contents of the Bag as a String array
     * @return String cop of the contents of the bag.
     */
    public String[] contentsAsString() {
        String[] temp = new String[this.elementsUsed];
        for (int i = 0; i < this.elementsUsed; i++)
            temp[i] = Integer.toString(this.pebbles[i]);
        return temp;
    }

    /**
     * Replace current pebbles with those passed via the parameter.
     * @param pebbles int array of the new pebble weights.
     * @throws PebbleWeightException Thrown when an invalid pebble weight is passed into the function.
     */
    public synchronized void fill(int[] pebbles) throws PebbleWeightException {
        for (int pebble : pebbles) {
            if (pebble < 1) {
                throw new PebbleWeightException("Negative pebble weights are not valid.");
            }
        }
        this.pebbles = pebbles;
        this.elementsUsed = pebbles.length;
    }

    /**
     * Remove all pebbles from the bag by copying and then reinstanciating the pebbles int array.
     * @return Returns the former bag content.
     */
    public synchronized int[] drop() {
        int[] temp = this.contents();
        this.pebbles = new int[10];
        this.elementsUsed = 0;
        return temp;
    }
}
