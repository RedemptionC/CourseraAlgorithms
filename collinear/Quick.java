import edu.princeton.cs.algs4.StdRandom;

public class Quick {
    private static void swap(int[] arr, int i, int j) {
        int t = arr[i];
        arr[i] = arr[j];
        arr[j] = t;
    }

    private static int partition(int[] a, int lo, int hi) {
        // int pivot = lo;
        // int i = lo, j = hi;
        // while (true) {
        //     while (arr[i++] <= arr[pivot])
        //         if (i == hi)
        //             break;
        //     while (arr[j--] >= arr[pivot])
        //         if (j == lo)
        //             break;
        //     if (i >= j)
        //         break;
        //     swap(arr, i, j);
        // }
        // swap(arr, pivot, j);
        // return j;
        int i = lo, j = hi + 1;
        while (true) {
            while (a[++i] <= a[lo])
                // 如果这里break，说明pivot（lo）右边的都比他小（所以最后要把pivot和最后一个互换
                // 表示pivot是最大的
                if (i == hi) break;
            while (a[lo] <= a[--j])
                if (j == lo) break;
            // 上面两趟循环分别找到从左第一个比pivot大的，从右第一个比pivot小的，什么时候出现i>=j?
            // i遍历过的都比pivot小，j遍历过的都比pivot大
            if (i >= j) break;
            swap(a, i, j);
        }
        //3 5 12 56 121 788
        swap(a, lo, j);
        return j;
    }

    private static int select(int[] arr, int k) {
        StdRandom.shuffle(arr);
        int lo = 0, hi = arr.length - 1;
        while (hi > lo) {
            int j = partition(arr, lo, hi);
            if (j > k) hi = j - 1;
            else if (j < k) lo = j + 1;
            else return arr[j];
        }
        return arr[k];
    }

    private static void sort(int[] arr) {
        StdRandom.shuffle(arr);
        sort(arr, 0, arr.length - 1);
    }

    private static void sort(int[] arr, int lo, int hi) {
        if (lo >= hi)
            return;
        int j = partition(arr, lo, hi);
        sort(arr, lo, j - 1);
        sort(arr, j + 1, hi);
    }

    public static void main(String[] args) {
        int[] arr = { 2, 1 };
        sort(arr);
        for (int i = 0; i < arr.length; i++) {
            System.out.printf("%d ", arr[i]);
        }
        System.out.println();
        System.out.println(select(arr, arr.length - 2));
    }
}

