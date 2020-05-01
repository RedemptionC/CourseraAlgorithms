import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver {
    private Trie set;
    private int rows, cols;
    private BoggleBoard boggleBoard;
    private boolean[][] marked;
    // 上下左右，左上，右上，左下，右下
    private final int[] drow = { -1, 1, 0, 0, -1, -1, 1, 1 };
    private final int[] dcol = { 0, 0, -1, 1, -1, 1, -1, 1 };

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        set = new Trie();
        for (String s : dictionary) {
            set.insert(s);
        }
    }

    private boolean outOfBound(int row, int col) {
        return row < 0 || row >= rows || col < 0 || col >= cols;
    }

    private void dfs(SET<String> rs, int row, int col, StringBuilder sb) {
        char c = boggleBoard.getLetter(row, col);
        sb.append(c == 'Q' ? "QU" : c);
        if (!set.startsWith(sb.toString())) {
            return;
        }
        marked[row][col] = true;
        if (sb.length() >= 3) {
            String t = sb.toString();
            if (set.search(t)) {
                rs.add(t);
            }
        }
        for (int i = 0; i < 8; i++) {
            int tRow = row + drow[i];
            int tCol = col + dcol[i];
            if (outOfBound(tRow, tCol) || marked[tRow][tCol]) {
                continue;
            }
            dfs(rs, tRow, tCol, new StringBuilder(sb));
            marked[tRow][tCol] = false;
            // if (row == 0 && col == 0)
            //     printMarked();
        }
    }

    private void printMarked() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.printf("%b ", marked[i][j]);
            }
            System.out.println();
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        SET<String> rs = new SET<>();
        boggleBoard = board;
        //dfs:以所有的点作为起点，然后向左右，上下，对角线，搜索
        rows = board.rows();
        cols = board.cols();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // FIXTHIS:这里是可以仅new一次的
                marked = new boolean[rows][cols];
                dfs(rs, i, j, new StringBuilder());
            }
        }
        return rs;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (!set.search(word))
            return 0;
        int len = word.length();
        if (len < 3)
            return 0;
        if (len == 3 || len == 4)
            return 1;
        if (len == 5)
            return 2;
        if (len == 6)
            return 3;
        if (len == 7)
            return 5;
        // if (len >= 8)
        return 11;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
        // System.out.println(solver.scoreOf("STONES"));
    }

}
