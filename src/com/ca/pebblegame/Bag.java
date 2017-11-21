package com.ca.pebblegame;

import java.util.Random;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * @author Robert Wells
 */
public class Bag {
    private int[] pebbles;
    private int elementsUsed;
    private String name;


    public Bag() {
        this.pebbles = new int[10];
        this.elementsUsed = 0;
        this.name = "Not defined.";
    }

    public Bag(String name) {
        this.pebbles = new int[10];
        this.elementsUsed = 0;
        this.name = name;
    }

    public void readWeights(int players, String fileLocation) throws FileNotFoundException, PebbleWeightException {
        // Read from the file
        BufferedReader br = null;
        String line = "";
        try {
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
        } catch (IOException e) {
            // Make this meaningful.
            e.printStackTrace();
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    // Make this meaningful.
                    e.printStackTrace();
                }
            }
        }
    }

    public String getName() {
        return this.name;
    }

    /**
     * Method adds the argument to the end of the list. <code>null</code> reference
     * elements are not supported, so ensure that you do not add them.
     *
     * @param i Object instance to be added
     */
    public synchronized void add(int i) {
        if (this.elementsUsed == this.pebbles.length)
            this.resizeArray();
        this.pebbles[elementsUsed] = i;
        this.elementsUsed++;
    }

    /*
     * Method doubles the capacity of the array
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
     * @param index index of element in list to be returned
     * @return Object at corresponding index
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
     *
     * @param index index of element in list to be returned
     * @return Object at corresponding index
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
     * @return shallow copy of contents of list as an array
     */
    public int[] contents() {
        int[] temp = new int[this.elementsUsed];
        for (int i = 0; i < this.elementsUsed; i++)
            temp[i] = this.pebbles[i];
        return temp;
    }

    public String[] contentsAsString() {
        String[] temp = new String[this.elementsUsed];
        for (int i = 0; i < this.elementsUsed; i++)
            temp[i] = Integer.toString(this.pebbles[i]);
        return temp;
    }


    public synchronized void fill(int[] pebbles) {
        this.pebbles = pebbles;
        this.elementsUsed = pebbles.length;
    }

    public synchronized int[] drop() {
        int[] temp = this.contents();
        this.pebbles = new int[10];
        this.elementsUsed = 0;
        return temp;
    }
}
