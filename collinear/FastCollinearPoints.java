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
    private final LineSegment[] lineSegments;

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

        LineSegment[] segments = new LineSegment[length];

        Point[] pointsClone = points.clone();
        boolean[][] lineSegmentDuplicate = new boolean[length][length];
        for (int i = 0; i < length; i++) {
            Point[] pointSegments = new Point[length + 1];
            pointSegments[0] = pointsClone[i];
            Arrays.sort(pointsClone, pointsClone[i].slopeOrder());
            int pointSegCount = 1;
            boolean lineSegment = false;
            for (int j = 0; j + 2 < length; j++) {
                if (i != j && pointSegments[0].slopeTo(pointsClone[j]) == pointSegments[0]
                        .slopeTo(pointsClone[j + 1])
                        && pointSegments[0].slopeTo(pointsClone[j]) == pointSegments[0]
                        .slopeTo(pointsClone[j + 2])) {
                    lineSegment = true;
                    pointSegments[pointSegCount] = pointsClone[j];
                    pointSegments[pointSegCount + 1] = pointsClone[j + 1];
                    pointSegments[pointSegCount + 2] = pointsClone[j + 2];
                    pointSegCount += 3;
                }
                else if (lineSegment && pointSegCount > 3) {
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

                    int lineSegIndStart = 0;
                    int lineSegIndEnd = 0;
                    for (int s = 0; s < length; s++) {
                        if (points[s].compareTo(nonNullSegments[0]) == 0)
                            lineSegIndStart = s;
                    }
                    for (int s = 0; s < length; s++) {
                        if (points[s].compareTo(nonNullSegments[k - 1]) == 0)
                            lineSegIndStart = s;
                    }

                    if (!lineSegmentDuplicate[lineSegIndStart][lineSegIndEnd]) {
                        segments[numberOfSegments++] = new LineSegment(nonNullSegments[0],
                                                                       nonNullSegments[k - 1]);
                        lineSegmentDuplicate[lineSegIndStart][lineSegIndEnd] = true;
                        lineSegmentDuplicate[lineSegIndEnd][lineSegIndStart] = true;
                    }
                    lineSegment = false;
                    for (int m = 1; m < length; m++) {
                        pointSegments[m] = null;
                    }
                    pointSegCount = 1;
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
