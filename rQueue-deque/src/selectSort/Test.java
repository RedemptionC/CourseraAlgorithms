package selectSort;

public class Test {
    public static void main(String[] args) {
        int[] arr = {5, 4, 3, 2, 1};
        for (int i = 0; i < 5; i++) {
            int min = i;
            for (int j = i + 1; j < 5; j++) {
                if (arr[j] < arr[min]) {
                    min = j;
                }
            }
            int t = arr[min];
            arr[min] = arr[i];
            arr[i] = t;
        }
        for (int i : arr) {
            System.out.println(i);
        }
    }
}
