/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

package undirectedGraph;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.Stack;

public class UndirectedGraph {
    // 采用邻接表的结构
    private Bag<Integer>[] adj;
    private boolean[] marked;
    private int[] edgeTo;
    private int[] componentId;

    // 将所有能连接到的设置连通分量id
    private void setCCHelper(int count, int v) {
        if (marked[v])
            return;
        componentId[v] = count;
        marked[v] = true;
        for (int i : adjVs(v)) {
            if (!marked[i]) {
                componentId[i] = count;
                setCCHelper(count, i);
                marked[i] = true;
            }
        }
    }

    // 判断所有的连通分量
    // 注意marked这个数组最初是在遍历时标记是否访问过的，但是之前
    // 因为DFS的函数要进行递归调用，所以把marked的new放在了构造函数里
    // 实际上这个函数应该是每次遍历时new的
    private void setCC() {
        componentId = new int[adj.length];
        marked = new boolean[adj.length];
        int count = 0;
        for (int i = 1; i < adj.length; i++) {
            if (marked[i]) {
                continue;
            }
            setCCHelper(count, i);
            count++;
        }
    }

    public boolean isConnected(int v, int w) {
        if (componentId == null)
            setCC();
        return componentId[v] == componentId[w];
    }

    public UndirectedGraph(int v) {
        adj = (Bag<Integer>[]) new Bag[v];
        for (int i = 0; i < v; i++) {
            adj[i] = new Bag<>();
        }
        edgeTo = new int[adj.length];
    }

    public void addEdge(int v, int w) {
        adj[v].add(w);
        adj[w].add(v);
    }

    public Iterable<Integer> adjVs(int v) {
        return adj[v];
    }

    public static UndirectedGraph sampleGraph() {
        UndirectedGraph undirectedGraph = new UndirectedGraph(7);
        undirectedGraph.addEdge(0, 1);
        undirectedGraph.addEdge(0, 2);
        undirectedGraph.addEdge(0, 6);
        undirectedGraph.addEdge(0, 5);
        undirectedGraph.addEdge(3, 5);
        undirectedGraph.addEdge(3, 4);
        undirectedGraph.addEdge(4, 5);
        undirectedGraph.addEdge(4, 6);
        return undirectedGraph;
    }

    // 一层层的往外遍历，并输出与起点的距离
    public void BFS(int start) {
        Queue<Integer> queue = new Queue<>();
        queue.enqueue(start);
        int distance = 0;
        marked = new boolean[adj.length];
        marked[start] = true;
        while (!queue.isEmpty()) {
            int size = queue.size();
            while (size-- > 0) {
                int v = queue.dequeue();
                System.out.println(v + " " + distance);
                for (int i : adjVs(v)) {
                    if (!marked[i]) {
                        queue.enqueue(i);
                        marked[i] = true;
                        edgeTo[i] = v;
                    }
                }
            }
            distance++;
        }
    }

    public void DFS(int start) {
        marked = new boolean[adj.length];
        DFSHelper(start);
    }

    public void DFSHelper(int start) {
        marked[start] = true;
        System.out.println(start);
        for (int i : adjVs(start)) {
            if (!marked[i]) {
                DFSHelper(i);
                edgeTo[i] = start;
            }
        }
    }

    public void DFSStack(int start) {
        Stack<Integer> stack = new Stack<>();
        marked = new boolean[adj.length];
        marked[start] = true;
        stack.push(start);
        while (!stack.isEmpty()) {
            int v = stack.pop();
            System.out.println(v);
            for (int i : adjVs(v)) {
                if (!marked[i]) {
                    marked[i] = true;
                    stack.push(i);
                    edgeTo[i] = v;
                }
            }
        }
    }

    public Iterable<Integer> pathTo(int root, int v) {
        Stack<Integer> stack = new Stack<>();
        for (int i = v; i != root; i = edgeTo[i]) {
            stack.push(i);
        }
        stack.push(root);
        return stack;
    }

    public static void main(String[] args) {
        UndirectedGraph undirectedGraph = sampleGraph();
        undirectedGraph.BFS(0);
        System.out.println(undirectedGraph.isConnected(0, 2));
        // undirectedGraph.DFSStack(0);
        // for (int i : undirectedGraph.pathTo(0, 3))
        //     System.out.printf("%d ", i);
        // System.out.println();
        // System.out.println();
        // UndirectedGraph undirectedGraph2 = sampleGraph();
        // undirectedGraph2.BFS(0);
        // for (int i : undirectedGraph2.pathTo(0, 3))
        //     System.out.printf("%d ", i);
        // System.out.println();
    }
}
