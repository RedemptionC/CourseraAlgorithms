/* *****************************************************************************
 *  Name:Cheng Geng
 *  Date:2019年12月1日
 *  Description:percolation
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private int n;
    // 保存哪些site是open的
    private int[][] grid;
    // 有virtual sites的用来判断percolate，没有的用来判断isfull
    private WeightedQuickUnionUF uf, ufNoV;
    private int openSitesNum;
    private int vT; // virtual top site
    private int vB; // virtual bottom site


    /**
     * 不使用uf
     */
    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("n must >= 0");
        }
        openSitesNum = 0;
        this.n = n;
        // 倒数第二个是top virtual site,倒数第一个是bottom virtual site
        int total = (n + 1) * (n + 1) + 2;
        vT = total - 2;
        vB = total - 1;
        uf = new WeightedQuickUnionUF(total);
        ufNoV = new WeightedQuickUnionUF(total);
        // 将第一行与top virtual site相连
        for (int i = xyTo1d(1, 1); i <= xyTo1d(1, n); i++) {
            uf.union(i, vT);
            ufNoV.union(i, vT);

        }
        // 将最后一行与bottom virtual site相连
        for (int i = xyTo1d(n, 1); i <= xyTo1d(n, n); i++) {
            uf.union(i, vB);
        }

        grid = new int[n + 1][n + 1]; // all init to 0,means blocked
    }

    /**
     * 不使用UF方法
     */
    private void checkIndex(int row, int col) {
        if (row < 1 || row > n) {
            throw new IllegalArgumentException("row " + row + " index out of bound");
        }
        if (col < 1 || col > n) {
            throw new IllegalArgumentException("col " + col + " index out of bound");
        }
    }

    /**
     * 感觉不应该抛出异常
     */
    private boolean isvalid(int row, int col) {
        if (row < 1 || row > n) {
            return false;
        }
        if (col < 1 || col > n) {
            return false;
        }
        return true;
    }

    /**
     * 不使用uf方法
     */
    private int xyTo1d(int row, int col) {
        checkIndex(row, col);
        return row * (n + 1) + col;
    }

    /**
     * 使用uf.union,将其标记为open后，再与周围连成一片
     */
    // opens the site (row, col) if it is not open already
    // 1.检查Index 2.设为open 3.与旁边的open相连
    public void open(int row, int col) {
        checkIndex(row, col);
        if (grid[row][col] == 1) {
            return;
        }
        grid[row][col] = 1; // means open
        if (isvalid(row - 1, col) && isOpen(row - 1, col)) {
            uf.union(xyTo1d(row, col), xyTo1d(row - 1, col));
            ufNoV.union(xyTo1d(row, col), xyTo1d(row - 1, col));

            // openSitesNum++;
        }
        if (isvalid(row + 1, col) && isOpen(row + 1, col)) {
            uf.union(xyTo1d(row, col), xyTo1d(row + 1, col));
            ufNoV.union(xyTo1d(row, col), xyTo1d(row + 1, col));
            // openSitesNum++;
        }
        if (isvalid(row, col + 1) && isOpen(row, col + 1)) {
            uf.union(xyTo1d(row, col), xyTo1d(row, col + 1));
            ufNoV.union(xyTo1d(row, col), xyTo1d(row, col + 1));

            // openSitesNum++;
        }
        if (isvalid(row, col - 1) && isOpen(row, col - 1)) {
            uf.union(xyTo1d(row, col), xyTo1d(row, col - 1));
            ufNoV.union(xyTo1d(row, col), xyTo1d(row, col - 1));

            // openSitesNum++;
        }
        openSitesNum++;
    }

    /**
     * 使用Uf.connected
     */
    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        checkIndex(row, col);
        return grid[row][col] == 1;
    }

    /**
     * 使用uf.connected,判断当前与top virtual site是否相连
     */
    // is the site (row, col) full?
    // 即当前opensite能否与top row连通
    // top row有n个，当然不是对n个进行校验
    // 而是通过virtual site进行判断
    public boolean isFull(int row, int col) {
        checkIndex(row, col);
        return grid[row][col] == 1 && ufNoV.connected(xyTo1d(row, col), vT);
    }

    /**
     * 不使用uf方法，仅返回一个成员变量
     */
    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSitesNum;
    }

    /**
     * 使用Uf.connected,判断bottom virtual site与 top virtual site是否联通
     */
    // does the system percolate?
    // 只需要判断bottom row有没有open site
    // 同样是通过virtual site
    public boolean percolates() {
        return openSitesNum >= n && uf.connected(vB, vT);
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation percolation = new Percolation(1);
        // percolation.open(1, 2);
        // percolation.open(1, 2);
        // percolation.open(1, 2);
        // percolation.open(1, 2);

        // percolation.open(2, 3);
        // int x1 = percolation.xyTo1d(2, 3);
        // int x2 = percolation.xyTo1d(2, 2);
        // StdOut.println(percolation.uf.connected(x1, x2));
        StdOut.println(percolation.percolates());
        StdOut.println(percolation.openSitesNum);

        // int c = 0;
        // for (int i = 0; i < 11; i++) {
        //     for (int j = 0; j < 11; j++) {
        //         StdOut.printf("%4d\t", c++);
        //     }
        //     StdOut.println();
        // }
    }

}
/*
   0	   1	   2	   3	   4	   5	   6	   7	   8	   9	  10
  11	  12	  13	  14	  15	  16	  17	  18	  19	  20	  21
  22	  23	  24	  25	  26	  27	  28	  29	  30	  31	  32
  33	  34	  35	  36	  37	  38	  39	  40	  41	  42	  43
  44	  45	  46	  47	  48	  49	  50	  51	  52	  53	  54
  55	  56	  57	  58	  59	  60	  61	  62	  63	  64	  65
  66	  67	  68	  69	  70	  71	  72	  73	  74	  75	  76
  77	  78	  79	  80	  81	  82	  83	  84	  85	  86	  87
  88	  89	  90	  91	  92	  93	  94	  95	  96	  97	  98
  99	 100	 101	 102	 103	 104	 105	 106	 107	 108	 109
 110	 111	 112	 113	 114	 115	 116	 117	 118	 119	 120
* */
