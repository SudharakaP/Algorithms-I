/* *****************************************************************************
 *  Name: KdTree.java
 *  Date: 2019-03-19
 *  Description:Implementation of a 2-d tree.
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;

public class KdTree {
    private Node root;
    private int size;

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        if (isEmpty()) {
            root = new Node(p, null, null, null, 0);
            root.rectHV = new RectHV(0, 0, 1, 1);
            size++;
        }
        else {
            insertNode(new Node(p, null, null, null, 0), root);
        }
    }

    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        if (isEmpty())
            return false;
        return nodeExists(root, p);
    }

    public void draw() {
        drawSubtree(root);
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException();
        List<Point2D> points = new ArrayList<>();
        nodesContainedInRect(root, rect, points);
        return points;
    }

    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        if (isEmpty())
            return null;
        return nearestPointInSubTree(root, p, root).point;
    }

    private void drawSubtree(Node node) {
        if (node == null)
            return;
        drawNode(node);
        drawSubtree(node.leftChild);
        drawSubtree(node.rightChild);
    }

    private boolean nodeExists(Node node, Point2D point) {
        if (Double.compare(node.point.x(), point.x()) == 0
                && Double.compare(node.point.y(), point.y()) == 0)
            return true;
        Node currentNode = new Node(point, null, null, null, 0);
        if (currentNode.compareTo(node) >= 0) {
            node = node.rightChild;
            return node != null && nodeExists(node, point);
        }
        else {
            node = node.leftChild;
            return node != null && nodeExists(node, point);
        }
    }

    private Node nearestPointInSubTree(Node node, Point2D point, Node championNode) {
        double distance = node.point.distanceSquaredTo(point);
        double minDistance = championNode.point.distanceSquaredTo(point);
        if (minDistance < node.rectHV.distanceSquaredTo(point))
            return championNode;

        if (distance < minDistance) {
            championNode = node;
        }

        if (node.leftChild != null && node.rightChild != null) {
            if (node.leftChild.rectHV.distanceSquaredTo(point) < node.rightChild.rectHV
                    .distanceSquaredTo(point)) {
                championNode = nearestPointInSubTree(node.leftChild, point, championNode);
                if (championNode.point.distanceSquaredTo(point) > node.rightChild.rectHV
                        .distanceSquaredTo(point))
                    return nearestPointInSubTree(node.rightChild, point, championNode);
            }
            else {
                championNode = nearestPointInSubTree(node.rightChild, point, championNode);
                if (championNode.point.distanceSquaredTo(point) > node.leftChild.rectHV
                        .distanceSquaredTo(point))
                    return nearestPointInSubTree(node.leftChild, point, championNode);
            }
        }
        else if (node.leftChild != null && minDistance > node.leftChild.rectHV
                .distanceSquaredTo(point))
            return nearestPointInSubTree(node.leftChild, point, championNode);
        else if (node.rightChild != null && minDistance > node.rightChild.rectHV
                .distanceSquaredTo(point))
            return nearestPointInSubTree(node.rightChild, point, championNode);
        return championNode;
    }

    private void nodesContainedInRect(Node node, RectHV rect, List<Point2D> points) {
        if (node == null)
            return;
        RectHV currentRect = node.rectHV;
        if (currentRect.intersects(rect)) {
            if (rect.contains(node.point))
                points.add(node.point);
            nodesContainedInRect(node.leftChild, rect, points);
            nodesContainedInRect(node.rightChild, rect, points);
        }
    }

    private void drawNode(Node node) {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        node.point.draw();
        StdDraw.setPenRadius();
        if (node.depth % 2 == 0) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.point.x(), node.rectHV.ymin(), node.point.x(), node.rectHV.ymax());
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(node.rectHV.xmin(), node.point.y(), node.rectHV.xmax(), node.point.y());
        }
    }

    private void insertNode(Node node, Node parent) {
        if (node.point.equals(parent.point))
            return;

        if (node.compareTo(parent) >= 0 && parent.rightChild != null) {
            insertNode(node, parent.rightChild);
        }
        else if (node.compareTo(parent) < 0 && parent.leftChild != null) {
            insertNode(node, parent.leftChild);
        }

        double parentXmin = parent.rectHV.xmin();
        double parentXmax = parent.rectHV.xmax();
        double parentYmin = parent.rectHV.ymin();
        double parentYmax = parent.rectHV.ymax();

        if (node.compareTo(parent) >= 0 && parent.rightChild == null) {
            node.parent = parent;
            node.depth = parent.depth + 1;
            node.parent.rightChild = node;

            if (parent.depth % 2 == 0)
                node.rectHV = new RectHV(parent.point.x(), parentYmin,
                                         parentXmax, parentYmax);
            else
                node.rectHV = new RectHV(parentXmin, parent.point.y(),
                                         parentXmax, parentYmax);

            size++;
            return;
        }

        if (node.compareTo(parent) < 0 && parent.leftChild == null) {
            node.parent = parent;
            node.depth = parent.depth + 1;
            node.parent.leftChild = node;

            if (parent.depth % 2 == 0)
                node.rectHV = new RectHV(parentXmin, parentYmin,
                                         parent.point.x(), parentYmax);
            else
                node.rectHV = new RectHV(parentXmin, parentYmin,
                                         parentXmax, parent.point.y());
            size++;
            return;
        }
    }

    private class Node implements Comparable<Node> {
        private final Point2D point;
        private Node parent;
        private Node leftChild;
        private Node rightChild;
        private RectHV rectHV;
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
            if (node.depth % 2 == 0)
                return Double.compare(this.point.x(), node.point.x());
            else
                return Double.compare(this.point.y(), node.point.y());
        }
    }

    public static void main(String[] args) {
        // initialize the data structures from file
        String filename = args[0];
        In in = new In(filename);
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
        }

        System.out.println(kdtree.nearest(new Point2D(0.625, 1.0)));
    }
}
