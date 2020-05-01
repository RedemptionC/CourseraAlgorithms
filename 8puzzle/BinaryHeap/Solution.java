/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

package BinaryHeap;

// 写一个最大堆
public class Solution {
    private Integer[] maxBh;
    private int N;

    private void swap(Integer[] a, int x, int y) {
        int t = a[x];
        a[x] = a[y];
        a[y] = t;
    }

    // n表示一个满二叉树中该节点的编号(1-indexing)
    // 比较自己与父亲，如果不符合父亲比自己大，就交换
    // 父亲是n/2,
    private void swim(int n) {
        int parent = n / 2;
        while (parent >= 1) {
            if (maxBh[parent] < maxBh[n]) {
                swap(maxBh, parent, n);
                n = parent;
                parent /= 2;
            }
            else {
                break;
            }
        }
    }

    // 用于将编号为n的数字放到应该的位置
    // 做法是比较n的数字与其儿子中较大的
    // (2K+1)，swap if necessary
    private void sink(int n) {
        while (2 * n <= N) {
            int child = 2 * n;
            if (child < N && maxBh[2 * n + 1] != null && maxBh[child] < maxBh[child + 1])
                child++;
            if (maxBh[child] > maxBh[n]) {
                swap(maxBh, child, n);
                n = child;
            }
            else {
                break;
            }
        }

    }

    public int delMax() {
        int max = maxBh[1];
        maxBh[1] = maxBh[N--];
        maxBh[N + 1] = null;
        sink(1);
        return max;
    }

    // 插入一个数,放在结尾，然后上升
    public void insert(int v) {
        maxBh[++N] = v;
        swim(N);
    }

    public boolean isEmpty() {
        return N == 0;
    }

    public Solution(int cap) {
        maxBh = new Integer[cap + 1];
        N = 0;
    }

    // resizing array
    // public Solution() {
    //
    // }


    public static void main(String[] args) {
        Solution s = new Solution(20);
        int[] arr = { 5, 3, 1, 12, 32, 99, 101, 12 };
        for (int i : arr)
            s.insert(i);
        while (!s.isEmpty()) {
            System.out.printf("%d ", s.delMax());
        }
        System.out.println();
    }
}
