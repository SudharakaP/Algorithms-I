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
        boolean[][] lineSegmentDuplicate = new boolean[length][length];
        for (int i = 0; i < length; i++) {
            Point[] pointSegments = new Point[3 * length];
            pointSegments[0] = points[i];
            if (binarySearchElement(points, points[0], 1, length - 1) != -1)
                throw new IllegalArgumentException();
            Arrays.sort(pointsClone, pointSegments[0].slopeOrder());
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

                    int lineSegIndStart = binarySearchElement(pointsCloneForBinarySearch,
                                                              smallestLargest[0], 0,
                                                              length - 1);
                    int lineSegIndEnd = binarySearchElement(pointsCloneForBinarySearch,
                                                            smallestLargest[1], 0,
                                                            length - 1);

                    if (!lineSegmentDuplicate[lineSegIndStart][lineSegIndEnd]) {
                        if (segments.length <= numberOfSegments)
                            segments = resizeArray(segments);
                        segments[numberOfSegments++] = new LineSegment(smallestLargest[0],
                                                                       smallestLargest[1]);
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

    private int binarySearchElement(Point[] points, Point element, int firstElementIndex,
                                    int lastElementIndex) {
        int midPoint = (firstElementIndex + lastElementIndex) >>> 1;
        if (firstElementIndex > lastElementIndex)
            return -1;
        else if (element.compareTo(points[midPoint]) > 0) {
            firstElementIndex = midPoint + 1;
        }
        else if (element.compareTo(points[midPoint]) < 0) {
            lastElementIndex = midPoint - 1;
        }
        else {
            return midPoint;
        }
        return binarySearchElement(points, element, firstElementIndex, lastElementIndex);
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
