import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

class Solution {
    private void shuffle(int[] ar) {
        // If running on Java 6 or older, use `new Random()` on RHS here
        Random rnd = ThreadLocalRandom.current();
        for (int i = ar.length - 1; i > 0; i--) {
            int index = rnd.nextInt(i + 1);
            // Simple swap
            int a = ar[index];
            ar[index] = ar[i];
            ar[i] = a;
        }
    }

    private void swap(int[] a, int i, int j) {
        int t = a[i];
        a[i] = a[j];
        a[j] = t;
    }

    private int partition(int arr[], int lo, int hi) {
        int i = lo, j = hi + 1;
        while (true) {
            while (arr[++i] <= arr[lo])
                if (i == hi)
                    break;
            while (arr[--j] >= arr[lo])
                if (j == lo)
                    break;
            if (i >= j)
                break;
            swap(arr, i, j);
        }
        swap(arr, lo, j);
        return j;
    }

    // k=1时，是a[len-1],
    public int findKthLargest(int[] nums, int k) {
        shuffle(nums);
        k = nums.length - k;
        int lo = 0, hi = nums.length - 1;
        int j = 0;
        while (lo < hi) {
            j = partition(nums, lo, hi);
            if (j > k) hi = j - 1;
            else if (j < k) lo = j + 1;
            else return nums[j];
        }
        return nums[k];
    }

    public static void main(String[] args) {
        Solution s = new Solution();
        System.out.println(s.findKthLargest(new int[] { 2, 1 }, 2));
    }
}
