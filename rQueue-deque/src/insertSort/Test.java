package insertSort;

public class Test {
    public static void main(String[] args) {
//         这种做法确实可以不用数组保存序列，同时使用这个序列
//        int[] arr = {1, 8, 6, 5, 3};
//        for (int i = 0; i < arr.length; i++) {
//            for (int j = i; j > 0; j--) {
//                if (arr[j] < arr[j - 1]) {
//                    int temp = arr[j];
//                    arr[j] = arr[j - 1];
//                    arr[j - 1] = temp;
//                }
//            }
//        }
//        for (int i : arr) {
//            System.out.println(i);
//        }
        int[] arr = {1, 8, 6, 5, 3, 3, 4, 1, 3, 2, 5, 6, 7};
        int n = arr.length;
        int h = 1;
        while (h < n / 3) {
            h = 3 * h + 1;
        }

        while (h >= 1) {
            for (int i = h; i < n; i++) {
                for (int j = i; j >= h && arr[j] < arr[j - h]; j -= h) {
                    int t = arr[j];
                    arr[j] = arr[j - h];
                    arr[j - h] = t;
                }
            }
            h = h / 3;
        }

        for (int i : arr) {
            System.out.println(i);
        }
    }
}
