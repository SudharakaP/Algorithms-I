/* *****************************************************************************
 *  Name: PointSET.java
 *  Date: 2019-03-19
 *  Description: Represents a set of points in a unit square (using Red-Black BST)
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class PointSET {
    private final TreeSet<Point2D> points;

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
        List<Point2D> pointsWithin = new ArrayList<>();
        for (Point2D point : points) {
            if (rect.contains(point)) {
                pointsWithin.add(point);
            }
        }
        return pointsWithin;
    }

    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        if (isEmpty())
            return null;
        Point2D nearest = null;
        for (Point2D point : points) {
            if (nearest == null || point.distanceSquaredTo(p) < nearest.distanceSquaredTo(p))
                nearest = point;
        }
        return nearest;
    }

    public static void main(String[] args) {
        // No unit tests
    }
}
