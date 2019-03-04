/* *****************************************************************************
 *  Name: Permutation.java
 *  Date: 2019-03-03
 *  Description: Client program that takes an integer k as a command-line argument;
 *  reads in a sequence of strings from standard input using StdIn.readString();
 *  and prints exactly k of them, uniformly at random.
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdRandom;

public class Permutation {
    public static void main(String[] args) {
        String numberOfStrings = args[0];
        String fileContent = StdIn.readString();
        String[] fileContentArray = fileContent.split(" ");
        for (int i = 0; i < Integer.parseInt(numberOfStrings); i++) {
            int random = StdRandom.uniform(i + 1);
            String swapObject = fileContentArray[i];
            fileContentArray[i] = fileContentArray[random];
            fileContentArray[random] = swapObject;
        }
        for (int i = 0; i < Integer.parseInt(numberOfStrings); i++) {
            System.out.println(fileContentArray[i]);
        }
    }
}
