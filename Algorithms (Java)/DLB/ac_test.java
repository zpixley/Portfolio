import java.util.*;
import java.lang.*;
import java.io.*;

public class ac_test {
    public static void main(String[] args) {
        char input;
        long start, end;
        double elapsed;
        int numWords;
        String word, chosenWord;
        String[] options = new String[5];
        StringBuilder prefix = new StringBuilder();
        DLB dict = new DLB();
        DLB userHistory = new DLB();
        Scanner fScan, inScan;

        /** Build dict DLB from file **/
        File inFile = new File("dictionary.txt");
        try {
            fScan = new Scanner(inFile);
            while (fScan.hasNextLine()) {
                word = fScan.nextLine();
                dict.put(word);
            }
        } catch (FileNotFoundException e){}
        /** Build userHistory DLB from file **/
        inFile = new File("user_history.txt");
        try {
            fScan = new Scanner(inFile);
            while (fScan.hasNextLine()) {
                word = fScan.nextLine();
                userHistory.put(word);
            }
        } catch (FileNotFoundException e){}


        /** Start user interaction **/
        inScan = new Scanner(System.in);
        System.out.print("Enter your first character: ");
        input = inScan.next().charAt(0);


        while (!(input == '!' || input == '$' || (input >= '1' && input <= '5'))) {
            prefix.append(input);
            start = System.nanoTime();

            /** Find words that start with prefix
             *  1. check userHistory
             *  2. pull remaining possibilities from dict
             **/
            numWords = userHistory.search(prefix, options, 5);
            if (numWords < 5) {
                numWords += dict.search(prefix, options, (5 - numWords));
            }
            end = System.nanoTime();
            elapsed = (end - start) / (1000000000.0);
            System.out.format("(%.6f s)\n", elapsed);

            /** Display predictions to user **/
            System.out.println("Predictions:");
            int i;
            for (i = 0; i < numWords - 1; i++) {
                System.out.print("(" + i + ") " + options[i] + "\t");
            }
            System.out.println("(" + i + ") " + options[i]);

            /** User response **/
            System.out.print("Enter the next character: ");
            input = inScan.next().charAt(0);
        }

        /** Add chosen option to chosen DLB **/
        if (input >= '1' && input <= '5') {
            chosenWord = options[((int) input) - 49];
            userHistory.put(chosenWord);
        }
        /** Add new word to chosen DLB **/
        else if (input == '$') {
            chosenWord = prefix.toString();
            userHistory.put(chosenWord);
        }
        /** Clean and exit **/
        else {
            userHistory.export("user_history.txt");
        }
    }
}