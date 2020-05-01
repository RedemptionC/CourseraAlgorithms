/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

public class SAP {
    // 这里的final，仅代表不能修改引用，但是可以修改该图的内容，如添加边
    private final Digraph digraph;
    private Integer v, w, minDistance, ancestor;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        if (G == null)
            throw new IllegalArgumentException("arg can not be null");
        // 这里是创建一个新对象，而不是仅仅设置为传进来的引用，这样实现了SAP的immutable
        digraph = new Digraph(G);
    }

    private void validateV(int v) {
        if (v < 0 || v >= digraph.V()) {
            throw new IllegalArgumentException(
                    "vertex " + v + " out of range(0," + digraph.V() + ")");
        }
    }

    private void validateV(Iterable<Integer> v) {
        if (v == null)
            throw new IllegalArgumentException("null is not allowed");
        for (Integer i : v) {
            if (i == null)
                throw new IllegalArgumentException("null is not allowed");
            validateV(i);
        }
    }

    private void setSAP(int v, int w) {
        validateV(v);
        validateV(w);
        BreadthFirstDirectedPaths breadthFirstDirectedPathsV = new BreadthFirstDirectedPaths(
                digraph, v);
        BreadthFirstDirectedPaths breadthFirstDirectedPathsW = new BreadthFirstDirectedPaths(
                digraph, w);
        HashSet<Integer> setV = new HashSet<>();
        for (int i = 0; i < digraph.V(); i++) {
            // 这里是包括自己的，因为如果A->B，那么B也可以是SAP上的公共祖先
            if (breadthFirstDirectedPathsV.distTo(i) != Integer.MAX_VALUE)
                setV.add(i);
        }
        ancestor = null;
        minDistance = Integer.MAX_VALUE;
        for (int i = 0; i < digraph.V(); i++) {
            if (breadthFirstDirectedPathsW.distTo(i) != Integer.MAX_VALUE && setV.contains(i)) {
                int t = breadthFirstDirectedPathsV.distTo(i) + breadthFirstDirectedPathsW.distTo(i);
                if (t < minDistance) {
                    minDistance = t;
                    ancestor = i;
                }
            }
        }
        this.v = v;
        this.w = w;
        if (minDistance == Integer.MAX_VALUE)
            minDistance = -1;
        if (ancestor == null)
            ancestor = -1;
    }

    private void setSAP(Iterable<Integer> v, Iterable<Integer> w) {
        validateV(v);
        validateV(w);
        BreadthFirstDirectedPaths breadthFirstDirectedPathsV = new BreadthFirstDirectedPaths(
                digraph, v);
        BreadthFirstDirectedPaths breadthFirstDirectedPathsW = new BreadthFirstDirectedPaths(
                digraph, w);
        HashSet<Integer> setV = new HashSet<>();
        for (int i = 0; i < digraph.V(); i++) {
            // 这里是包括自己的，因为如果A->B，那么B也可以是SAP上的公共祖先
            if (breadthFirstDirectedPathsV.distTo(i) != Integer.MAX_VALUE)
                setV.add(i);
        }
        ancestor = null;
        minDistance = Integer.MAX_VALUE;
        for (int i = 0; i < digraph.V(); i++) {
            if (breadthFirstDirectedPathsW.distTo(i) != Integer.MAX_VALUE && setV.contains(i)) {
                int t = breadthFirstDirectedPathsV.distTo(i) + breadthFirstDirectedPathsW.distTo(i);
                if (t < minDistance) {
                    minDistance = t;
                    ancestor = i;
                }
            }
        }
        if (minDistance == Integer.MAX_VALUE)
            minDistance = -1;
        if (ancestor == null)
            ancestor = -1;
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        validateV(v);
        validateV(w);
        if (minDistance != null && this.v != null && this.v == v && this.w != null && this.w == w) {
            return minDistance;
        }
        setSAP(v, w);
        return minDistance;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        validateV(v);
        validateV(w);
        if (ancestor != null && this.v != null && this.v == v && this.w != null && this.w == w) {
            return ancestor;
        }
        setSAP(v, w);
        return ancestor;
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        setSAP(v, w);
        // 注意，这里虽然没有复用之前的结果，但是他修改了distance，和ancestor，而没有修改v,w，如果下次还是与上次一样的vw，就会出现以为可以复用
        // 但是结果已经被修改了的情况，所以这里应该将v,w置为null
        this.v = null;
        this.w = null;
        return minDistance;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        setSAP(v, w);
        this.v = null;
        this.w = null;
        return ancestor;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        List<Integer> list1 = new LinkedList<>();
        int dotLen = 4, dotAnc = 1;
        int setLen = 2, setAnc = 1;
        list1.add(2);
        list1.add(3);
        List<Integer> list2 = new LinkedList<>();
        list2.add(5);
        list2.add(12);
        int count = 0;

        while (count++ < 20) {
            if (count % 2 == 0) {
                System.out.println("dot " +
                                           (sap.length(8, 10) == dotLen) + " " + (
                        sap.ancestor(8, 10) == dotAnc));
            }
            else {
                System.out.println("set " +
                                           (sap.length(list1, list2) == setLen) + " " + (
                        sap.ancestor(list1, list2)
                                == setAnc));
            }
        }
        // while (!StdIn.isEmpty()) {
        //     int v = StdIn.readInt();
        //     int w = StdIn.readInt();
        //     int length = sap.length(v, w);
        //     int ancestor = sap.ancestor(v, w);
        //     StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        //     G.addEdge(0, 3);
        // }
        // Stack<Integer> stack = new Stack<>();
        // stack.push(7);
        // stack.push(4);
        // BreadthFirstDirectedPaths breadthFirstDirectedPaths = new BreadthFirstDirectedPaths(G,
        //                                                                                     stack);
        // for (int i = 0; i < 13; i++)
        //     System.out.println(i + " " + breadthFirstDirectedPaths.hasPathTo(i) + " "
        //                                + breadthFirstDirectedPaths.distTo(i));
        // for (int i : breadthFirstDirectedPaths.pathTo(1))
        //     System.out.println(i);
    }

}
