import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.LinkedList;

public class MoveToFront {

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        LinkedList<Character> ascii = new LinkedList<>();
        for (int i = 0; i < 256; i++) {
            ascii.add((char) i);
        }
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar(8);
            // System.out.printf("%c %d\n", c, ascii.get((int) c));
            int i = ascii.indexOf((c));
            // System.out.println(c + " " + i);
            BinaryStdOut.write(i, 8);
            Character t = ascii.remove(i);
            ascii.addFirst(t);
        }
        // System.out.println();
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        LinkedList<Character> ascii = new LinkedList<>();
        for (int i = 0; i < 256; i++) {
            ascii.add((char) i);
        }
        while (!BinaryStdIn.isEmpty()) {
            char c = BinaryStdIn.readChar(8);
            BinaryStdOut.write(ascii.get((int) c), 8);
            Character t = ascii.remove((int) c);
            ascii.addFirst(t);
        }
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        String method = args[0];
        if (method.equals("-")) {
            MoveToFront.encode();
        }
        else {
            MoveToFront.decode();
        }
    }
}
