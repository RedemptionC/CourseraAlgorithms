/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.LinkedList;
import java.util.List;

public class KdTree {
    private Node root;
    private int size;
    private static final boolean HORIZONTAL = true;
    private static final boolean VERTICAL = false;
    private List<Point2D> list;
    private Point2D point;
    private double minDistance = 3;

    private static class Node {
        private Point2D p;      // the point
        private RectHV rect;    // the axis-aligned rectangle corresponding to this node
        private Node lb;        // the left/bottom subtree
        private Node rt;        // the right/top subtree

        // 为了简便，暂时先不管矩形的设置
        public Node(Point2D p, RectHV rect) {
            this.p = p;
            this.rect = rect;
        }
    }

    // construct an empty set of points
    // ?应该做什么?--- 什么也不做，插入时再创建根
    public KdTree() {
    }

    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the set
    // 既然本题不要删除，只要插入，那么只要在插入时（仅当成功）size++即可
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("null params not allowed");
        root = insert(null, root, p, VERTICAL);
    }

    // 先把这个写完，等会再改contains,那个也需要orientation
    private Node insert(Node parent, Node node, Point2D p, boolean orientation) {
        if (node == null) {
            size++;
            RectHV rectHV = null;
            if (parent == null) {
                // 说明这个要返回的node就是根
                // 那么他的矩形就是一整个
                rectHV = new RectHV(0, 0, 1, 1);
            }
            else {
                // 不是根，说明此时已经有父节点了，则要根据
                // 父节点的矩形来判断
                // 如果我是横线，parent就是竖线，那么就是左右
                double xmin, xmax, ymin, ymax;
                if (orientation == HORIZONTAL) {
                    if (p.x() < parent.p.x()) {
                        xmin = parent.rect.xmin();
                        ymin = parent.rect.ymin();
                        xmax = parent.p.x();
                        ymax = parent.rect.ymax();
                        return new Node(p, new RectHV(xmin, ymin, xmax, ymax));
                    }
                    // 此时有没有可能相等?有!相等放在右子树?
                    if (p.x() >= parent.p.x()) {
                        xmin = parent.p.x();
                        ymin = parent.rect.ymin();
                        xmax = parent.rect.xmax();
                        ymax = parent.rect.ymax();
                        return new Node(p, new RectHV(xmin, ymin, xmax, ymax));
                    }
                }
                else {
                    if (p.y() >= parent.p.y()) {
                        xmin = parent.rect.xmin();
                        ymin = parent.p.y();
                        xmax = parent.rect.xmax();
                        ymax = parent.rect.ymax();
                        return new Node(p, new RectHV(xmin, ymin, xmax, ymax));
                    }
                    if (p.y() < parent.p.y()) {
                        xmin = parent.rect.xmin();
                        ymin = parent.rect.ymin();
                        xmax = parent.rect.xmax();
                        ymax = parent.p.y();
                        return new Node(p, new RectHV(xmin, ymin, xmax, ymax));
                    }
                }
            }
            return new Node(p, rectHV);
        }
        // 如果完全相同，就直接返回
        if (node.p.compareTo(p) == 0)
            return node;
        // 注意，每层比较的key不一样，是x,y,x,y....
        // 第一层是VERTICAL,则比较x，HORIZONTAL，则比较y
        // 👆好像不对，如果两者相同，那么就去右边
        double cmp;
        //注意，我是vertical，父辈就是horizontal，则上下
        // 👆不对，这里时比较每一层，所以这里不是看父辈的orientation，就是看自己，如果
        // 是vertical，就左右
        if (orientation == VERTICAL) {
            // cmp = node.p.y() == p.y() ? -1 : node.p.y() - p.y();
            if (node.p.x() - p.x() == 0) {
                cmp = -1;
            }
            else
                cmp = node.p.x() - p.x();
        }
        else {
            // cmp = node.p.x() == p.x() ? -1 : node.p.x() - p.x();
            if (node.p.y() - p.y() == 0) {
                cmp = -1;
            }
            else
                cmp = node.p.y() - p.y();
        }
        if (cmp > 0) {
            node.lb = insert(node, node.lb, p, !orientation);
        }
        if (cmp < 0) {
            node.rt = insert(node, node.rt, p, !orientation);
        }
        return node;
    }

    public boolean contains(Point2D p) {
        return contains(p, VERTICAL);
    }

    // does the set contain point p?
    private boolean contains(Point2D p, boolean orientation) {
        if (p == null)
            throw new IllegalArgumentException("null params not allowed");
        Node t = root;
        while (t != null && t.p.compareTo(p) != 0) {
            double cmp = orientation == VERTICAL ? t.p.x() - p.x() : t.p.y() - p.y();
            if (cmp > 0) {
                t = t.lb;
                // 每下一层，orientation反转
                orientation = !orientation;
                // 这里之所以要continue，是因为此时t可能为null
                // 如果继续进行下面的判断，可能会空指针
                continue;
            }
            // 两者相等，也去右边(?)
            if (cmp <= 0) {
                t = t.rt;
                orientation = !orientation;
                continue;
            }
        }
        // t!=null,说明找到了，返回true,否则，说明没找到
        return t != null;
    }

    // draw all points to standard draw
    // 好像不需要画1*1的那个框?
    // 竖线是红色，横线是蓝色，点黑色
    public void draw() {
        StdDraw.enableDoubleBuffering();
        StdDraw.setCanvasSize();
        StdDraw.setScale(-.05, 1.05);
        draw(root, VERTICAL);
        StdDraw.show();
    }

    private void draw(Node node, boolean orientation) {
        if (node == null)
            return;
        StdDraw.setPenColor(StdDraw.BLACK);
        // StdDraw.setPenRadius(0.01);
        StdDraw.point(node.p.x(), node.p.y());
        StdDraw.text(node.p.x(), node.p.y(), node.p.toString());
        if (orientation == VERTICAL) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.p.x(), node.rect.ymin(), node.p.x(), node.rect.ymax());
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.p.y());
        }
        draw(node.lb, !orientation);
        draw(node.rt, !orientation);
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException("null params not allowed");
        list = new LinkedList<>();
        // range(rect, root, VERTICAL);
        if (root != null)
            range(rect, root);
        return list;
    }

    // private void range(RectHV rect, Node node, boolean orientation) {
    //     if (node == null)
    //         return;
    //     if (rect.contains(node.p)) {
    //         list.add(node.p);
    //         range(rect, node.lb, !orientation);
    //         range(rect, node.rt, !orientation);
    //     }
    //     else if (intersectSplitLine(rect, node, orientation)) {
    //         range(rect, node.lb, !orientation);
    //         range(rect, node.rt, !orientation);
    //     }
    //     else {
    //         // 此时点不在矩形内，且线与矩形也不相交,包括相切，也只需要检查一边
    //         // 但是检查一边时要检查相切!
    //         if (orientation == VERTICAL) {
    //             // 当前线是竖线，看左右
    //             // 即矩形整个的在竖线的左或者右
    //             if (rect.xmax() <= node.p.x()) {
    //                 range(rect, node.lb, !orientation);
    //             }
    //             if (rect.xmin() >= node.p.x()) {
    //                 // 应该这里必然是xmin>x
    //                 range(rect, node.rt, !orientation);
    //             }
    //         }
    //         else {
    //             if (rect.ymin() >= node.p.y()) {
    //                 range(rect, node.rt, !orientation);
    //             }
    //             if (rect.ymax() <= node.p.y()) {
    //                 range(rect, node.lb, !orientation);
    //             }
    //         }
    //     }
    //
    // }
    private void range(RectHV rect, Node temp) {
        if (rect.contains(temp.p)) list.add(temp.p);
        if ((temp.lb != null) && (temp.lb.rect.intersects(rect)))
            range(rect, temp.lb);
        if ((temp.rt != null) && (temp.rt.rect.intersects(rect)))
            range(rect, temp.rt);
    }

    private boolean intersectSplitLine(RectHV rect, Node node, boolean orientation) {
        // 如果当前线是竖着的，就要判断该竖线在不在矩形的长之内,相切也算?感觉是不算的，只是增加了
        // 搜索量
        if (orientation == VERTICAL) {
            return node.p.x() > rect.xmin() && node.p.x() < rect.xmax();
        }
        else {
            return node.p.y() > rect.ymin() && node.p.y() < rect.ymax();
        }
    }

    private int checkSize() {
        return checkSize(root);
    }

    private int checkSize(Node node) {
        if (node == null)
            return 0;
        System.out.println(node.p.toString());
        return 1 + checkSize(node.lb) + checkSize(node.rt);
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("null params not allowed");
        nearest(p, root, VERTICAL);
        minDistance = 3;
        return point;
    }

    private void levelOrder() {
        Queue<Node> queue = new Queue<>();
        queue.enqueue(root);
        while (!queue.isEmpty()) {
            // 用一个容器把他装起来，然后每次输出一层
            Queue<Node> thisLevel = new Queue<>();
            while (!queue.isEmpty()) {
                Node t = queue.dequeue();
                System.out.printf("%s ** rect:%s ##", t.p.toString(), t.rect.toString());
                if (t.lb != null)
                    thisLevel.enqueue(t.lb);
                if (t.rt != null)
                    thisLevel.enqueue(t.rt);
            }
            System.out.println();
            while (!thisLevel.isEmpty()) {
                queue.enqueue(thisLevel.dequeue());
            }
        }
    }

    private boolean sameCordinateTurnRight(Point2D that, Node node, boolean orientation) {
        if (orientation == VERTICAL) {
            return that.x() == node.p.x();
        }
        else {
            return that.y() == node.p.y();
        }
    }

    private void nearest(Point2D that, Node node, boolean orientation) {
        if (node == null)
            return;
        double disRoot = node.p.distanceSquaredTo(that);
        if (minDistance > disRoot) {
            minDistance = disRoot;
            point = node.p;
        }
        // 如果我要查的那个点在当前线的某一边，可能那一边没有点吗?可能
        // 因为离他最近的点可能不在那一边，所以要判空
        // 算法是：query point在哪一边，就首先去哪一边，如果那一边的点确实更近，就不去另一边
        // 看的一个思路很奇特：不是判断另一边的点是不是比他更近，而是判断另一边的矩形是不是更近
        // boolean closer = false;
        if (orientation == VERTICAL) {
            if (that.x() < node.p.x()) {
                // if (node.lb != null && node.lb.p.distanceSquaredTo(that) < minDistance) {
                //     closer = true;
                // }
                nearest(that, node.lb, !orientation);
                if (node.rt != null && node.rt.rect.distanceSquaredTo(that) < minDistance) {
                    nearest(that, node.rt, !orientation);
                }
            }
            else {
                // >=,就看右边
                // if (node.rt != null && node.rt.p.distanceSquaredTo(that) < minDistance) {
                //     closer = true;
                // }
                nearest(that, node.rt, !orientation);
                if (node.lb != null && node.lb.rect.distanceSquaredTo(that) < minDistance) {
                    nearest(that, node.lb, !orientation);
                }
            }
        }
        else {
            if (that.y() < node.p.y()) {

                nearest(that, node.lb, !orientation);
                if (node.rt != null && node.rt.rect.distanceSquaredTo(that) < minDistance) {
                    nearest(that, node.rt, !orientation);
                }
            }
            else {
                // >=,就看上边

                nearest(that, node.rt, !orientation);
                if (node.lb != null && node.lb.rect.distanceSquaredTo(that) < minDistance) {
                    nearest(that, node.lb, !orientation);
                }
            }
        }
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        KdTree kdTree = new KdTree();

        // KdTree kdTree = new KdTree();
        // kdTree.insert(new Point2D(0.5, 0.5));
        // kdTree.insert(new Point2D(0.55, 0.65));
        RectHV rectHV = new RectHV(0.5, 0.6, 0.6, 0.7);
        for (Point2D i : kdTree.range(rectHV)) {
            System.out.println(i);
        }
        // for (double i = 0.1; i < 1; i += 0.1) {
        //     Point2D t = new Point2D(i, i);
        //     kdTree.insert(t);
        //     System.out.println(kdTree.contains(t));
        // }
        // kdTree.draw();
        // for (double i = 0.1; i < 1; i += 0.1) {
        //     Point2D t = new Point2D(i, i);
        //     System.out.println(t + " nearest: " + kdTree.nearest(t));
        //     System.out.println(kdTree.contains(t));
        // }
        // System.out.println(kdTree.size());
        // initialize the data structures from file
        // String filename = args[0];
        // In in = new In(filename);
        // PointSET brute = new PointSET();
        // KdTree kdtree = new KdTree();
        // while (!in.isEmpty()) {
        //     double x = in.readDouble();
        //     double y = in.readDouble();
        //     Point2D p = new Point2D(x, y);
        //     System.out.println(p.toString());
        //     kdtree.insert(p);
        //     brute.insert(p);
        // }
        //
        // kdtree.levelOrder();
        // kdtree.draw();
        // // kdtree.checkSize();
        // System.out.println(brute.nearest(new Point2D(0.154296875, 0.763671875)));
        // System.out.println(kdtree.nearest(new Point2D(0.154296875, 0.763671875)));
        // RectHV rect = new RectHV(0.140625, 0.716796875,
        //                          0.373046875, 0.95703125);
        // for (Point2D p : brute.range(rect))
        //     System.out.println(p.toString());
        // System.out.println("kdtree");
        // for (Point2D p : kdtree.range(rect))
        //     System.out.println(p.toString());
        // KdTree kdTree = new KdTree();
        // kdTree.insert(new Point2D(0.25, 0.75));
        // kdTree.insert(new Point2D(1, 0.625));
        // kdTree.insert(new Point2D(0.625, 0.5));
        // kdTree.insert(new Point2D(0.5, 1));
        // kdTree.insert(new Point2D(0.75, 0.375));
        // kdTree.levelOrder();
        // System.out.println(kdTree.size());
        // System.out.println(kdTree.contains(new Point2D(0.25, 0.75)));
        // System.out.println(kdTree.contains(new Point2D(0.625, 0.5)));
        // System.out.println(kdTree.contains(new Point2D(0.2, 0.3)));
        // System.out.println(kdTree.contains(new Point2D(0.4, 0.7)));
        // System.out.println(kdTree.contains(new Point2D(0.9, 0.6)));

    }

}
