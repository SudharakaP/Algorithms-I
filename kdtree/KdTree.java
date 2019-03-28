/* *****************************************************************************
 *  Name: KdTree.java
 *  Date: 2019-03-19
 *  Description:Implementation of a 2-d tree.
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class KdTree {
    private final TreeSet<Node> nodes;
    private Node root;

    public KdTree() {
        nodes = new TreeSet<>();
    }

    public boolean isEmpty() {
        return nodes.isEmpty();
    }

    public int size() {
        return nodes.size();
    }

    public void insert(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        if (isEmpty()) {
            root = new Node(p, null, null, null, 0);
            root.rectHV = new RectHV(0, 0, 1, 1);
            nodes.add(root);
        }
        else {
            insertNode(new Node(p, null, null, null, 0), root);
        }
    }

    public boolean contains(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        return nodes.contains(new Node(p, null, null, null, 0)) || nodes
                .contains(new Node(p, null, null, null, 1));
    }

    public void draw() {
        for (Node node : nodes) {
            drawNode(node);
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null)
            throw new IllegalArgumentException();
        List<Point2D> points = new ArrayList<>();
        nodesContainedInRect(root, rect, points, 0);
        return points;
    }

    public Point2D nearest(Point2D p) {
        if (p == null)
            throw new IllegalArgumentException();
        if (isEmpty())
            return null;
        return nearestPointInSubTree(root, p, root).point;
    }

    private Node nearestPointInSubTree(Node node, Point2D point, Node championNode) {
        double distance = node.point.distanceSquaredTo(point);
        double minDistance = championNode.point.distanceSquaredTo(point);

        if (distance < minDistance) {
            championNode = node;
            minDistance = distance;
        }

        if (node.leftChild != null && node.rightChild != null
                && node.leftChild.rectHV.distanceTo(point) < node.rightChild.rectHV
                .distanceTo(point)) {
            championNode = nearestPointInSubTree(node.leftChild, point, championNode);
            if (node.rightChild.rectHV.distanceSquaredTo(point) < minDistance)
                championNode = nearestPointInSubTree(node.rightChild, point, championNode);
        }
        else if (node.rightChild != null && node.leftChild != null
                && node.rightChild.rectHV.distanceTo(point) < node.leftChild.rectHV
                .distanceTo(point)) {
            championNode = nearestPointInSubTree(node.rightChild, point, championNode);
            if (node.leftChild.rectHV.distanceSquaredTo(point) < minDistance)
                championNode = nearestPointInSubTree(node.leftChild, point, championNode);
        }
        else if (node.leftChild != null) {
            championNode = nearestPointInSubTree(node.leftChild, point, championNode);
        }
        else if (node.rightChild != null) {
            championNode = nearestPointInSubTree(node.rightChild, point, championNode);
        }
        return championNode;
    }

    private void nodesContainedInRect(Node node, RectHV rect, List<Point2D> points, int index) {
        if (node == null)
            return;
        RectHV currentRect = node.rectHV;
        if (currentRect.intersects(rect)) {
            points.add(node.point);
            nodesContainedInRect(node.leftChild, rect, points, index);
            nodesContainedInRect(node.rightChild, rect, points, index);
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
        if (node.compareTo(parent) > 0 && parent.rightChild == null) {
            node.parent = parent;
            node.depth = parent.depth + 1;
            node.parent.rightChild = node;

            if (parent.depth % 2 == 0)
                node.rectHV = new RectHV(parent.point.x(), parent.rectHV.ymin(),
                                         parent.rectHV.xmax(), parent.rectHV.ymax());
            else
                node.rectHV = new RectHV(parent.rectHV.xmin(), parent.point.y(),
                                         parent.rectHV.xmax(), parent.rectHV.ymax());
            nodes.add(node);
            return;
        }

        if (node.compareTo(parent) < 0 && parent.leftChild == null) {
            node.parent = parent;
            node.depth = parent.depth + 1;
            node.parent.leftChild = node;

            if (parent.depth % 2 == 0)
                node.rectHV = new RectHV(parent.rectHV.xmin(), parent.rectHV.ymin(),
                                         parent.point.x(), parent.rectHV.ymax());
            else
                node.rectHV = new RectHV(parent.rectHV.xmin(), parent.rectHV.ymin(),
                                         parent.rectHV.xmax(), parent.point.y());
            nodes.add(node);
            return;
        }

        if (node.compareTo(parent) > 0) {
            insertNode(node, parent.rightChild);
        }
        else if (node.compareTo(parent) < 0) {
            insertNode(node, parent.leftChild);
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
        // No unit tests
    }
}
