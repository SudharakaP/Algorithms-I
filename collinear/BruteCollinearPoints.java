/* *****************************************************************************
 *  Name: BruteCollinearPoints.java
 *  Date: 2019-03-07
 *  Description: Examines 4 points at a time and checks whether they all lie on the same line segment,
 *  returning all such line segments.
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class BruteCollinearPoints {
    private int numberOfSegments;
    private LineSegment[] lineSegments;

    public BruteCollinearPoints(Point[] points) {

        if (points == null)
            throw new IllegalArgumentException();
        for (Point point : points) {
            if (point == null)
                throw new IllegalArgumentException();
        }

        int length = points.length;
        for (int i = 0; i < length; i++) {
            for (int j = 0; j < length && j != i; j++) {
                if (points[i].compareTo(points[j]) == 0)
                    throw new IllegalArgumentException();
            }
        }

        LineSegment[] segments = new LineSegment[length * (length - 1) * (length - 2) * (length - 3)
                / 24];

        for (int i = 0; i < length; i++) {
            for (int j = i + 1; j < length; j++) {
                for (int k = j + 1; k < length; k++) {
                    for (int m = k + 1; m < length; m++) {
                        if (points[i].slopeTo(points[j]) == points[i].slopeTo(points[k])
                                && points[i].slopeTo(points[j]) == points[i].slopeTo(points[m])) {
                            Point[] segmentPoints = { points[i], points[j], points[k], points[m] };
                            Arrays.sort(segmentPoints);
                            segments[numberOfSegments++] = new LineSegment(segmentPoints[0],
                                                                           segmentPoints[3]);
                        }
                    }
                }
            }
        }

        lineSegments = new LineSegment[numberOfSegments];
        int i = 0;
        for (LineSegment lineSegment : segments) {
            if (lineSegment != null)
                lineSegments[i++] = lineSegment;
        }
    }

    public int numberOfSegments() {
        return numberOfSegments;
    }

    public LineSegment[] segments() {
        return lineSegments.clone();
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

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
