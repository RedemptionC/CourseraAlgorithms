package shellSort;

public class shellSort {
    private static void sort(Comparable[] a) {
        int n = a.length;

        int h = 1;
        while (h < n / 3)
            h = 3 * h + 1;

        while (h >= 1) {
            for (int i = h; i < n; i++) {
                for (int j = i; j >= h && less(a[j], a[j - h]); j -= h) {
                    exch(a, j, j - h);
                }
            }
            h = h / 3;
        }
    }

    private static boolean less(Comparable i, Comparable j) {
        return i.compareTo(j) < 0;
    }

    private static void exch(Comparable[] a, int i, int j) {
        Comparable t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    public static void main(String[] args) {
        Integer[] a = {4, 3, 12, 65, 4, 658, 51};
        shellSort.sort(a);
        System.out.println(a.toString());
    }
}
