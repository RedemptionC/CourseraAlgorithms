/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

import java.util.LinkedList;
import java.util.List;

public class PointSET {
    // 该SET会maintain一个按从左到右，从上到下排序的点的集合
    private SET<Point2D> set;

    // construct an empty set of points
    public PointSET() {
        set = new SET<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return set.isEmpty();
    }

    // number of points in the set
    public int size() {
        return set.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("null params not allowed");
        set.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("null params not allowed");
        return set.contains(p);
    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : set) {
            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException("null params not allowed");
        List<Point2D> list = new LinkedList<>();
        for (Point2D p : set) {
            if (rect.contains(p)) {
                list.add(p);
            }
        }
        return list;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException("null params not allowed");
        // 因为是在1*1的里面，所以3就是一个达不到的距离(即使是平方
        double distance = 3;
        Point2D point = null;
        for (Point2D t : set) {
            double temp = t.distanceSquaredTo(p);
            if (temp < distance) {
                distance = temp;
                point = t;
            }
        }
        return point;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {

    }

}
