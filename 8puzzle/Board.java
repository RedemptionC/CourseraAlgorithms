/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */


import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Board {
    private int dim;
    private int[][] board;
    private int hanmmingDistance;
    private int manhattanDistance;
    private String representation;
    private List<Board> neighbor;
    private int blankTile = -1;

    private void swap(int[][] tiles, int x1, int y1, int x2, int y2) {
        int t = tiles[x1][y1];
        tiles[x1][y1] = tiles[x2][y2];
        tiles[x2][y2] = t;
    }

    private int[][] copy() {
        int[][] tiles = new int[dim][dim];
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                tiles[i][j] = board[i][j];
            }
        }
        return tiles;
    }

    private int findBlank(int[][] tiles) {
        if (blankTile != -1)
            return blankTile;
        for (int k = 0; k < dim * dim; k++) {
            int row = k / dim;
            int col = k % dim;
            if (tiles[row][col] == 0) {
                blankTile = k;
                return k;
            }
        }
        return blankTile;
    }

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        hanmmingDistance = 0;
        manhattanDistance = 0;
        dim = tiles.length;
        board = tiles;
        int shouldCol = -1, shouldRow = -1;
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                if (board[i][j] != 0) {
                    // 完成对汉明距离的计算
                    if (board[i][j] != 0 && board[i][j] != i * dim + j + 1) {
                        hanmmingDistance++;
                    }
                    // 完成对曼哈顿距离的计算
                    int val = board[i][j];
                    int q = (val - 1) / dim;
                    int r = (val - 1) % dim;
                    shouldCol = r;
                    shouldRow = q;
                    manhattanDistance += Math.abs(shouldCol - j) + Math
                            .abs(shouldRow - i);

                }
            }
        }
        if (board[dim - 1][dim - 1] == dim * dim)
            hanmmingDistance++;

    }

    // string representation of this board
    public String toString() {
        if (representation != null)
            return representation;
        StringBuilder s = new StringBuilder();
        s.append(dim + "\n");
        for (int i = 0; i < dim; i++) {
            for (int j = 0; j < dim; j++) {
                s.append(String.format("%2d ", board[i][j]));
            }
            s.append("\n");
        }
        representation = s.toString();
        return representation;
    }

    // board dimension n
    public int dimension() {
        return dim;
    }

    // 这里返回的仅仅是距离,注意blank 即使out of place,也不算在内
    // number of tiles out of place
    public int hamming() {
        return hanmmingDistance;
    }


    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        return manhattanDistance;
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == this)
            return true;
        if (y == null)
            return false;
        if (y.getClass() != this.getClass())
            return false;
        Board t = (Board) y;
        if (t.dimension() != dim)
            return false;
        return Arrays.deepEquals(t.board, board);
    }

    // 注意应该是在交换之后，再创建board，否则汉明路径啥的不对（因为我这个是在构造函数里计算这些
    // all neighboring boards
    public Iterable<Board> neighbors() {
        if (neighbor != null)
            return neighbor;
        List<Board> rs = new LinkedList<>();
        neighbor = rs;
        int k = findBlank(board);
        int i = k / dim, j = k % dim;


        // 0或者说可移动板块的位置有三种情况，角落，边线，中间
        // 首先处理角落处，即x,y都在边线，此时只有两种
        if ((i == 0 || i == dim - 1) && (j == 0 || j == dim - 1)) {
            // 此时有两个neighbor
            int[][] tiles1 = copy(), tiles2 = copy();
            if (i == 0 && j == 0) {
                swap(tiles1, 0, 0, 0, 1);
                swap(tiles2, 0, 0, 1, 0);
            }
            if (i == 0 && j == dim - 1) {
                swap(tiles1, 0, dim - 1, 0, dim - 2);
                swap(tiles2, 0, dim - 1, 1, dim - 1);

            }
            if (i == dim - 1 && j == 0) {
                swap(tiles1, dim - 1, 0, dim - 1, 1);
                swap(tiles2, dim - 1, 0, dim - 2, 0);
            }
            if (i == dim - 1 && j == dim - 1) {
                swap(tiles1, dim - 1, dim - 1, dim - 1, dim - 2);
                swap(tiles2, dim - 1, dim - 1, dim - 2, dim - 1);
            }
            Board n1 = new Board(tiles1), n2 = new Board(tiles2);
            rs.add(n1);
            rs.add(n2);
            return rs;
        }
        // 再处理边线，因为两个都在边线的，上面已经判断过了，所以这里用或就行
        if ((i == 0 || i == dim - 1) || (j == 0 || j == dim - 1)) {
            // 此时有三个neighbor
            int[][] tiles1 = copy(), tiles2 = copy(), tiles3 = copy();
            if (i == 0) {
                swap(tiles1, i, j, i, j - 1);
                swap(tiles2, i, j, i, j + 1);
                swap(tiles3, i, j, i + 1, j);
            } else if (i == dim - 1) {
                swap(tiles1, i, j, i, j - 1);
                swap(tiles2, i, j, i, j + 1);
                swap(tiles3, i, j, i - 1, j);
            } else if (j == 0) {
                swap(tiles1, i, j, i - 1, j);
                swap(tiles2, i, j, i + 1, j);
                swap(tiles3, i, j, i, j + 1);
            } else if (j == dim - 1) {
                swap(tiles1, i, j, i - 1, j);
                swap(tiles2, i, j, i + 1, j);
                swap(tiles3, i, j, i, j - 1);
            }
            Board n1 = new Board(tiles1), n2 = new Board(tiles2), n3 = new Board(tiles3);
            rs.add(n1);
            rs.add(n2);
            rs.add(n3);
            return rs;
        }
        // 此时只剩下都不在边线，也就是中间
        int[][] tiles1 = copy(), tiles2 = copy(), tiles3 = copy(), tiles4 = copy();
        swap(tiles1, i, j, i - 1, j);
        swap(tiles2, i, j, i + 1, j);
        swap(tiles3, i, j, i, j - 1);
        swap(tiles4, i, j, i, j + 1);
        Board n1 = new Board(tiles1), n2 = new Board(tiles2), n3 = new Board(tiles3), n4 = new Board(tiles4);
        rs.add(n1);
        rs.add(n2);
        rs.add(n3);
        rs.add(n4);
        return rs;
    }

    // a board that is obtained by exchanging any pair of tiles(blank不是tile
    // 那么这里要交换哪两对呢?这里都不让用Random包，应该随便找到两个就行了
    public Board twin() {
        int[][] tiles = copy();

        int blank = findBlank(board);
        int count = 0;
        int x1 = -1, x2 = -1, y1 = -1, y2 = -1;
        int k = 0;
        while (count < 2 && k < dim * dim) {
            if (k == blank) {
                k++;
                continue;
            } else {
                if (count == 0) {
                    y1 = k % dim;
                    x1 = k / dim;
                } else {
                    y2 = k % dim;
                    x2 = k / dim;
                    break;
                }
                k++;
                count++;
            }
        }
        swap(tiles, x1, y1, x2, y2);
        Board rs = new Board(tiles);
        // 在0~dim*dim -1间产生两个数，然后交换，但是这两个数不能是blank tile
        return rs;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] a = {{0, 1}, {2, 3}};
        Board b = new Board(a);
        System.out.println(b.hamming());
        System.out.println(b.manhattan());
        System.out.println(b);
        System.out.println(b.twin());

        for (Board i : b.neighbors()) {
            System.out.println(i);
        }
        // Random r = new Random();
        // int n = r.nextInt(6);
    }

}
