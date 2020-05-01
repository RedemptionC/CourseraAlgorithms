import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

class Solution {
    class Trie {
        private class Node {
            private Integer value;
            private Node[] next = new Node[26];
        }

        private Node root;
        private int count;// for setting value

        /** Initialize your data structure here. */
        public Trie() {
            root = new Node();
            count = 0;
        }

        private Node insert(String word, int val, Node node, int d) {
            if (node == null) {
                node = new Node();
            }
            if (d == word.length()) {
                node.value = val;
                return node;
            }
            int c = word.charAt(d) - 'a';
            node.next[c] = insert(word, val, node.next[c], d + 1);
            return node;
        }

        /** Inserts a word into the trie. */
        public void insert(String word) {
            insert(word, count, root, 0);
            count++;
        }

        private boolean search(String word, Node node, int d) {
            if (node == null) {
                return false;
            }
            if (d == word.length()) {
                if (node.value == null)
                    return false;
                else
                    return true;
            }
            int c = word.charAt(d) - 'a';
            return search(word, node.next[c], d + 1);
        }

        /** Returns if the word is in the trie. */
        public boolean search(String word) {
            return search(word, root, 0);
        }

        // 注意，这里是判断是否有以prefix为前缀的字符串在trie里，所以当d==len后，下一步是判断node.next是否有不为null
        private boolean startsWith(String prefix, Node node, int d) {
            if (d > prefix.length()) {
                return false;
            }
            if (node == null) {
                return false;
            }
            // 按说这里不能只根据next[i]不为空就判断存在，但是因为这里没有delete，所以只要next[i]不为空
            // 那么对应的value也不为空（也就是不会被删除）
            if (d == prefix.length()) {
                return true;
            }
            int c = prefix.charAt(d) - 'a';
            return startsWith(prefix, node.next[c], d + 1);
        }

        /** Returns if there is any word in the trie that starts with the given prefix. */
        public boolean startsWith(String prefix) {
            return startsWith(prefix, root, 0);
        }


    }

    private int rows, cols;
    private char[][] board;
    private boolean[][] marked;
    // 上下左右，左上，右上，左下，右下
    private final int[] drow = { -1, 1, 0, 0 };
    private final int[] dcol = { 0, 0, -1, 1 };
    private Trie set;

    private void dfs(HashSet<String> rs, int row, int col, StringBuilder sb) {
        char c = board[row][col];
        sb.append(c);
        if (!set.startsWith(sb.toString())) {
            return;
        }
        marked[row][col] = true;
        {
            String t = sb.toString();
            if (set.search(t)) {
                rs.add(t);
            }
        }
        for (int i = 0; i < 4; i++) {
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

    private boolean outOfBound(int row, int col) {
        return row < 0 || row >= rows || col < 0 || col >= cols;
    }

    public List<String> findWords(char[][] board, String[] words) {
        set = new Trie();
        for (String w : words) {
            set.insert(w);
        }
        HashSet<String> rs = new HashSet<>();
        this.board = board;
        //dfs:以所有的点作为起点，然后向左右，上下，对角线，搜索
        rows = board.length;
        cols = board[0].length;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // FIXTHIS:这里是可以仅new一次的
                marked = new boolean[rows][cols];
                dfs(rs, i, j, new StringBuilder());
            }
        }
        return new ArrayList<>(rs);
    }
}
