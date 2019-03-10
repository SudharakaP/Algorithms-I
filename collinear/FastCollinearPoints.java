/* *****************************************************************************
 *  Name: FastCollinearPoints.java
 *  Date: 2019-03-08
 *  Description: A faster search algorithm (n^2 log n) to find every subset that contains 4 or more
 *  collinear points.
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class FastCollinearPoints {
    private int numberOfSegments;
    private LineSegment[] lineSegments;

    public FastCollinearPoints(Point[] points) {
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

        Point[] pointsClone = points.clone();
        boolean[] accessedElements = new boolean[length];
        for (int i = 0; i < length; i++) {
            if (accessedElements[i])
                continue;
            Point[] pointSegments = new Point[length + 1];
            pointSegments[0] = pointsClone[i];
            accessedElements[i] = true;
            Arrays.sort(pointsClone, pointsClone[i].slopeOrder());
            int pointSegCount = 1;
            for (int j = 0; j + 1 < length; j++) {
                if (i != j && pointSegments[0].slopeTo(pointsClone[j]) == pointSegments[0]
                        .slopeTo(pointsClone[j + 1])) {
                    pointSegments[pointSegCount] = pointsClone[j];
                    pointSegments[pointSegCount + 1] = pointsClone[j + 1];
                    accessedElements[j] = true;
                    accessedElements[j + 1] = true;
                    pointSegCount += 2;
                }
            }
            if (pointSegCount < 2)
                continue;
            int k = 0;
            for (Point point : pointSegments)
                if (point != null)
                    k++;
            Point[] nonNullSegments = new Point[k];
            int n = 0;
            for (Point point : pointSegments)
                if (point != null) {
                    nonNullSegments[n] = point;
                    n++;
                }
            Arrays.sort(nonNullSegments);
            segments[numberOfSegments++] = new LineSegment(nonNullSegments[0],
                                                           nonNullSegments[k - 1]);
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
