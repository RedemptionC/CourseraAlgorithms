/* *****************************************************************************
 *  Name:Red
 *  Date:2020年1月26日
 *  Description:----
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class BruteCollinearPoints {
    private List<LineSegment> lineSegments;
    private int numberOfSegments;

    private boolean floatEqual(double a, double b, double threshold) {
        if (a == Double.NEGATIVE_INFINITY)
            return b == Double.NEGATIVE_INFINITY;
        if (a == Double.POSITIVE_INFINITY)
            return b == Double.POSITIVE_INFINITY;
        return Math.abs(a - b) < threshold;
    }

    public BruteCollinearPoints(Point[] tpoints) {
        lineSegments = new LinkedList<>();
        if (tpoints == null)
            throw new IllegalArgumentException("points can not be null");
        for (int i = 0; i < tpoints.length; i++) {
            if (tpoints[i] == null)
                throw new IllegalArgumentException("point can not be null");
            for (int j = i + 1; j < tpoints.length; j++) {
                if (tpoints[j] == null)
                    throw new IllegalArgumentException("point can not be null");
                if (tpoints[i].compareTo(tpoints[j]) == 0)
                    throw new IllegalArgumentException("repeated point not allowed");
            }
        }
        Point[] points = Arrays.copyOf(tpoints, tpoints.length);
        Arrays.sort(points);
        for (int i = 0; i < points.length - 3; i++) {
            for (int j = i + 1; j < points.length - 2; j++) {
                for (int k = j + 1; k < points.length - 1; k++) {
                    for (int m = k + 1; m < points.length; m++) {
                        double slopeIj = points[i].slopeTo(points[j]);
                        double slopeIk = points[i].slopeTo(points[k]);
                        double slopeIm = points[i].slopeTo(points[m]);

                        if (floatEqual(slopeIj, slopeIk, 0.0000000000000000001) && floatEqual(
                                slopeIj, slopeIm,
                                0.000000000000000001)) {
                            // 为了选出i,j,k,m这条线的端点，这里需要用natural order对这四个点进行排序
                            // 或者一开始就对整个数组排序
                            lineSegments.add(new LineSegment(points[i], points[m]));
                            numberOfSegments++;
                        }
                    }
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
        // 类型转换必须这么写，而不是(Line[])xxx.toArray
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
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
