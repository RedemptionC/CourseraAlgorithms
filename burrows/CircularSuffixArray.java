import java.util.Arrays;
import java.util.Comparator;

public class CircularSuffixArray {
    private String s;
    private Integer[] circularSuffixIndex;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null)
            throw new IllegalArgumentException("arg can not be null");
        this.s = s;
        circularSuffixIndex = new Integer[s.length()];
        // circularSuffixIndex[i]代表以第i个字符开头
        // 刚好就是original suffix的第i行
        for (int i = 0; i < s.length(); i++) {
            circularSuffixIndex[i] = i;
        }
        Comparator<Integer> cmp = (a1, a2) -> {
            // i,j是index数组里的元素，下面要把他们对应的suffix array
            // 从高到低比较
            for (int i = 0; i < s.length(); i++) {
                int i1 = (a1 + i) % s.length();
                int i2 = (a2 + i) % s.length();
                if (s.charAt(i1) == s.charAt(i2))
                    continue;
                else
                    return Character.compare(s.charAt(i1), s.charAt(i2));
            }
            return 0;
        };
        // 把循环后缀数组排序
        Arrays.sort(circularSuffixIndex, cmp);
    }

    // length of s
    public int length() {
        return s.length();
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i >= s.length())
            throw new IllegalArgumentException("index out of range");
        return circularSuffixIndex[i];
    }

    // unit testing (required)
    public static void main(String[] args) {
        String s = "ABRACADABRA!";
        CircularSuffixArray circularSuffixArray = new CircularSuffixArray(s);
        for (int i = 0; i < s.length(); i++) {
            System.out.println(circularSuffixArray.index(i));
        }
        // String s = "banana";
        // Queue<Character> queue = new Queue<>();
        // for (char c : s.toCharArray()) {
        //     queue.enqueue(c);
        // }
        // int i = s.length();
        // // System.out.println(queue.toString());
        // while (i-- > 0) {
        //     char ch = queue.dequeue();
        //     queue.enqueue(ch);
        //     System.out.println(queue.toString());
        // }
    }
}
