
/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.LinkedList;
import java.util.List;

public class Solver {


    private searchNode finalState;
    private boolean solvable;
    private Stack<Board> path;

    // 先使用曼哈顿距离+moves作为优先级函数
    private class searchNode implements Comparable<searchNode> {
        private Board board;
        private int moves;
        private searchNode prev;
        private int priorityManhattan;
        private int priorityHamming;

        @Override
        public int compareTo(searchNode o) {
            if (this.priorityManhattan != o.priorityManhattan)
                return this.priorityManhattan - o.priorityManhattan;
            else
//                return this.moves - o.moves;
                return this.priorityHamming - o.priorityHamming;
        }

        public searchNode(Board b, int m, searchNode p) {
            board = b;
            moves = m;
            prev = p;
            priorityManhattan = b.manhattan() + moves;
            priorityHamming = b.hamming();
        }

        public Iterable<searchNode> neighbors() {
            List<searchNode> rs = new LinkedList<>();
            Iterable<Board> neighborsBoard = board.neighbors();
            for (Board b : neighborsBoard) {
                searchNode s = new searchNode(b, moves + 1, this);
                rs.add(s);
            }
            return rs;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null)
            throw new IllegalArgumentException("null args not allowed");
        MinPQ<searchNode> minPQ = new MinPQ<>();
        MinPQ<searchNode> minPqTwin = new MinPQ<>();
        searchNode t = new searchNode(initial, 0, null);
        searchNode tTwin = new searchNode(initial.twin(), 0, null);
        minPQ.insert(t);
        minPqTwin.insert(tTwin);
        while (true) {
            searchNode temp = minPQ.delMin();
            if (temp.board.isGoal()) {
                finalState = temp;
                solvable = true;
                break;
            }
            searchNode tempTwin = minPqTwin.delMin();
            // 👇判断成真，代表不可解
            if (tempTwin.board.isGoal()) {
                finalState = tempTwin;
                solvable = false;
                return;
            }
            for (Board s : temp.board.neighbors()) {
                // 什么情况下加入优先队列?仅当有前驱(代表不是第一个)
                // 并且前驱不等于该neighbor时，加入该neighbor
                if (!(temp.prev != null && s.equals(temp.prev.board)))
                    minPQ.insert(new searchNode(s, temp.moves + 1, temp));
            }

            for (Board s : tempTwin.board.neighbors()) {
                if (!(tempTwin.prev != null && s.equals(tempTwin.prev.board)))
                    minPqTwin.insert(new searchNode(s, tempTwin.moves + 1, tempTwin));
            }
        }
        // 再根据finalState,得到路径，
        if (solvable) {
            path = new Stack<Board>();
            while (finalState != null) {
                path.push(finalState.board);
                finalState = finalState.prev;
            }
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board
    public int moves() {
        if (path == null)
            return -1;
        return path.size() - 1;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        return path;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In("./puzzle44.txt");
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }

    }

}
