/* *****************************************************************************
 *  Name: KdTree.java
 *  Date: 2019-03-19
 *  Description:Implementation of a 2-d tree.
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.TreeSet;

public class KdTree {
    private TreeSet<Point2D> points;
    private Node root;

    public boolean isEmpty() {
        return points.size() == 0;
    }

    public int size() {
        return points.size();
    }

    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        if (isEmpty())
            root = new Node(p, null, null, null, 0);
        else {
            insertNode(new Node(p, null, null, null, 0), root);
        }
        points.add(p);
    }

    private void insertNode(Node node, Node parent) {
        if ((node.compareTo(parent) > 0 && parent.rightChild == null) || (node.compareTo(parent) < 0
                && parent.leftChild == null)) {
            node.parent = parent;
            node.depth = parent.depth + 1;
            return;
        }

        if (node.compareTo(parent) > 0) {
            insertNode(node, parent.rightChild);
        }
        else if (node.compareTo(parent) < 0) {
            insertNode(node, parent.leftChild);
        }
        else {
            return;
        }
    }

    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        return points.contains(p);
    }

    public void draw() {

    }

    public Iterable<Point2D> range(RectHV rect) {
        // TODO
        // if (rect == null)
        //     throw new IllegalArgumentException();
        // Point2D[] pointsWithin = new Point2D[size()];
        // int i = 0;
        // for (Point2D point : nodes) {
        //     if (rect.contains(point)) {
        //         pointsWithin[i++] = point;
        //     }
        // }
        // Point2D[] pointsWithinNotNull = new Point2D[i + 1];
        // for (int j = 0; j < i; j++) {
        //     pointsWithinNotNull[j] = pointsWithin[j];
        // }
        // return Arrays.asList(pointsWithinNotNull);
        return null;
    }

    public Point2D nearest(Point2D p) {
        // TODO
        // if (p == null)
        //     throw new IllegalArgumentException();
        // if (isEmpty())
        //     return null;
        // Point2D nearest = null;
        // for (Point2D point : nodes) {
        //     if (nearest == null || point.distanceTo(p) < nearest.distanceTo(p))
        //         nearest = point;
        // }
        // return nearest;
        return null;
    }

    private class Node implements Comparable<Node> {
        private Point2D point;
        private Node parent;
        private Node leftChild;
        private Node rightChild;
        private int depth;

        public Node(Point2D point, Node parent, Node leftChild, Node rightChild, int depth) {
            this.point = point;
            this.parent = parent;
            this.leftChild = leftChild;
            this.rightChild = rightChild;
            this.depth = depth;
        }

        @Override
        public int compareTo(Node node) {
            if (depth % 2 == 0)
                return Double.compare(this.point.x(), node.point.x());
            else
                return Double.compare(this.point.y(), node.point.y());
        }
    }

    public static void main(String[] args) {

    }
}
