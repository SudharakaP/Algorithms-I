/* *****************************************************************************
 *  Name: PointSET.java
 *  Date: 2019-03-19
 *  Description: Represents a set of points in a unit square (using Red-Black BST)
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.Arrays;
import java.util.TreeSet;

public class PointSET {
    private TreeSet<Point2D> points;

    public PointSET() {
        points = new TreeSet<>();
    }

    public boolean isEmpty() {
        return points.isEmpty();
    }

    public int size() {
        return points.size();
    }

    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        points.add(p);
    }

    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        return points.contains(p);
    }

    public void draw() {
        for (Point2D point : points)
            point.draw();
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException();
        Point2D[] pointsWithin = new Point2D[size()];
        int i = 0;
        for (Point2D point : points) {
            if (rect.contains(point)) {
                pointsWithin[i++] = point;
            }
        }
        Point2D[] pointsWithinNotNull = new Point2D[i + 1];
        for (int j = 0; j < i; j++) {
            pointsWithinNotNull[j] = pointsWithin[j];
        }
        return Arrays.asList(pointsWithinNotNull);
    }

    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        if (isEmpty())
            return null;
        Point2D nearest = null;
        for (Point2D point : points) {
            if (nearest == null || point.distanceTo(p) < nearest.distanceTo(p))
                nearest = point;
        }
        return nearest;
    }

    public static void main(String[] args) {

    }
}
