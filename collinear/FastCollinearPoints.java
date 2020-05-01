/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class FastCollinearPoints {
    private List<LineSegment> lineSegments;
    private int numberOfSegments;


    private boolean floatEqual(double a, double b, double threshold) {
        if (a == Double.NEGATIVE_INFINITY)
            return b == Double.NEGATIVE_INFINITY;
        if (a == Double.POSITIVE_INFINITY)
            return b == Double.POSITIVE_INFINITY;
        return Math.abs(a - b) < threshold;
    }

    // 👆题目要求的时间复杂度是O(n*nlgn)可以看出来，确实是这个做法：
    // 首先按照与当前点slope的值排序，然后找到所有共线的点，
    // 再对这些点按照natural order排序，找到起点和重点，而其中一个要点就是
    // ，这两次排序都是在新复制的数组中做的，不能破坏原来的顺序
    public FastCollinearPoints(Point[] points) {
        lineSegments = new LinkedList<>();
        if (points == null)
            throw new IllegalArgumentException("points can not be null");
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null)
                throw new IllegalArgumentException("point can not be null");
            for (int j = i + 1; j < points.length; j++) {
                if (points[j] == null)
                    throw new IllegalArgumentException("point can not be null");
                if (points[i].compareTo(points[j]) == 0)
                    throw new IllegalArgumentException("repeated point not allowed");
            }
        }
        if (points.length < 4)
            return;
        Point[] t = Arrays.copyOf(points, points.length);
        for (int i = 0; i < points.length; i++) {
            Arrays.sort(t, points[i].slopeOrder());
            double slope = points[i].slopeTo(t[1]);
            int count = 2;
            for (int j = 2; j < t.length; ) {
                // 这里用while代表要把所有在这条线上的点都找到
                while (j < t.length && floatEqual(points[i].slopeTo(t[j]), slope,
                                                  0.000000000000000001)) {
                    count++;
                    j++;
                }
                if (count >= 4) {
                    // 对这条线上的点进行排序，从而找到端点
                    // 注意copyof不是始终设置起始几个点，而是从[j-count,j)
                    // 注意这里的第一个点实际上是t[0]
                    Point[] thisline = Arrays.copyOfRange(t, j - count, j);
                    thisline[0] = t[0];
                    Arrays.sort(thisline);
                    // count是从2开始数的，这里的thisline并没有包括第一个
                    LineSegment lineSegment = new LineSegment(thisline[0], thisline[count - 1]);
                    // 注意list.contain是表示同一个object，而不是指line的端点相同
                    boolean contain = false;
                    // 判断当前基准的点，是否在natural order中是起点
                    if (t[0].compareTo(thisline[0]) != 0)
                        contain = true;

                    if (contain == false) {

                        lineSegments.add(lineSegment);
                        numberOfSegments++;
                    }

                }
                if (j < t.length) {
                    // 说明遇到了不等于之前slope的点
                    // 要更新slope，准备添加新线
                    slope = points[i].slopeTo(t[j]);
                    count = 2;
                    j++;
                }

            }
        }
    }

    public int numberOfSegments()        // the number of line segments
    {
        return numberOfSegments;
    }

    public LineSegment[] segments()                // the line segments
    {
        return lineSegments.toArray(new LineSegment[lineSegments.size()]);
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        //test null exception
        // points[0] = null;
        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}

