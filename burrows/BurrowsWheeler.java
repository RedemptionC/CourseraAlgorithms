import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        // first /n last column of sorted suffix
        String s = BinaryStdIn.readString();
        CircularSuffixArray circularSuffixArray = new CircularSuffixArray(s);
        int first = -1;
        for (int i = 0; i < s.length(); i++) {
            if (circularSuffixArray.index(i) == 0) {
                first = i;
                break;
            }
        }
        BinaryStdOut.write(first);
        for (int i = 0; i < s.length(); i++) {
            BinaryStdOut
                    .write(s.charAt((circularSuffixArray.index(i) + s.length() - 1) % s.length()));
        }
        BinaryStdOut.close();
    }

    // 假设所以字符对应的值都不小于0
    private static char[] keyIndexedCounting(char[] a, int[] next) {
        int max = -1;
        int min = 10000;
        for (char i : a) {
            if (i > max) {
                max = i;
            }
            if (i < min) {
                min = i;
            }
        }
        int[] count = new int[max - min + 1 + 1];
        for (int i = 0; i < a.length; i++) {
            count[a[i] - min + 1]++;
        }
        for (int i = 0; i < count.length - 1; i++) {
            count[i + 1] += count[i];
        }
        char[] aux = new char[a.length];
        for (int i = 0; i < a.length; i++) {
            aux[count[a[i] - min]] = a[i];
            // 虽然不懂，但是这就是next数组！
            // System.out.printf("%d -> %d\n", count[a[i] - min], i);
            next[count[a[i] - min]] = i;
            count[a[i] - min]++;
        }
        return aux;
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String s = BinaryStdIn.readString();
        // System.out.println(first + "\n" + s);
        int[] next = new int[s.length()];
        char[] firstColumn = keyIndexedCounting(s.toCharArray(), next);
        // for (char c : firstColumn) {
        //     System.out.println(c);
        // }
        // 怎么在O(n)内算出next数组？暂时没想到，先O(n*n)
        // StringBuilder sb = new StringBuilder(s);
        // char[] temp = s.toCharArray();
        // int[] next = new int[s.length()];
        // for (int i = 0; i < s.length(); i++) {
        //     for (int j = 0; j < temp.length; j++) {
        //         if (firstColumn[i] == temp[j]) {
        //             next[i] = j;
        //             temp[j] = '\0';
        //             break;
        //         }
        //     }
        // }
        // for (int c : next) {
        //     System.out.println(c);
        // }
        // StringBuilder sb = new StringBuilder();
        int t = first;
        for (int i = 0; i < next.length; i++) {
            // sb.append(firstColumn[t]);
            BinaryStdOut.write(firstColumn[t], 8);
            t = next[t];
        }
        // BinaryStdOut.write(sb.toString());
        BinaryStdOut.close();
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        String method = args[0];
        if (method.equals("-")) {
            BurrowsWheeler.transform();
        }
        if (method.equals("+")) {
            BurrowsWheeler.inverseTransform();
        }
    }
}
