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
        LineSegment[] segments = new LineSegment[length];

        Point[] pointsClone = points.clone();
        Point[] pointsCloneForBinarySearch = points.clone();
        Arrays.sort(pointsCloneForBinarySearch);
        Point[] pointSegments = new Point[3 * length];
        for (int i = 0; i < length; i++) {
            pointSegments[0] = pointsCloneForBinarySearch[i];
            Arrays.sort(pointsClone, pointSegments[0].slopeOrder());
            if (length > 1 && pointsClone[0].compareTo(pointsClone[1]) == 0)
                throw new IllegalArgumentException();
            int pointSegCount = 1;
            boolean lineSegment = false;
            for (int j = 0; j + 1 < length; j++) {
                if (j + 2 < length
                        && Double.compare(pointSegments[0].slopeTo(pointsClone[j]),
                                          pointSegments[0].slopeTo(pointsClone[j + 1])) == 0
                        && Double.compare(pointSegments[0].slopeTo(pointsClone[j]),
                                          pointSegments[0].slopeTo(pointsClone[j + 2])) == 0) {
                    lineSegment = true;
                    pointSegments[pointSegCount] = pointsClone[j];
                    pointSegments[pointSegCount + 1] = pointsClone[j + 1];
                    pointSegments[pointSegCount + 2] = pointsClone[j + 2];
                    pointSegCount += 3;
                }
                else if (lineSegment && pointSegCount > 3) {
                    Point[] nonNullSegments = new Point[pointSegCount];
                    for (int t = 0; t < pointSegCount; t++)
                        nonNullSegments[t] = pointSegments[t];

                    Point[] smallestLargest = smallestAndLargest(nonNullSegments);

                    if (pointSegments[0] == smallestLargest[0]) {
                        if (segments.length <= numberOfSegments)
                            segments = resizeArray(segments);
                        segments[numberOfSegments++] = new LineSegment(smallestLargest[0],
                                                                       smallestLargest[1]);
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

    private LineSegment[] resizeArray(LineSegment[] segmentArray) {
        LineSegment[] newLineSegments = new LineSegment[2 * segmentArray.length];

        for (int i = 0; i < segmentArray.length; i++) {
            newLineSegments[i] = segmentArray[i];
        }
        return newLineSegments;
    }

    private Point[] smallestAndLargest(Point[] pointArray) {
        Point smallestElement = pointArray[0];
        Point largestElement = pointArray[0];

        for (Point point : pointArray) {
            if (point.compareTo(smallestElement) < 0)
                smallestElement = point;
            else if (point.compareTo(largestElement) > 0)
                largestElement = point;
        }
        Point[] smallestLargest = { smallestElement, largestElement };
        return smallestLargest;
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
