/* *****************************************************************************
 *  Name: Permutation.java
 *  Date: 2019-03-03
 *  Description: Client program that takes an integer k as a command-line argument;
 *  reads in a sequence of strings from standard input using StdIn.readString();
 *  and prints exactly k of them, uniformly at random.
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;

public class Permutation {
    public static void main(String[] args) {
        int numberOfStrings = Integer.parseInt(args[0]);
        RandomizedQueue<String> randomizedQueue = new RandomizedQueue<>();
        while (!StdIn.isEmpty()) {
            randomizedQueue.enqueue(StdIn.readString());
        }
        for (int i = 0; i < numberOfStrings; i++)
            System.out.println(randomizedQueue.dequeue());
    }
}
