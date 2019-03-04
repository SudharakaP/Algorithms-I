/* *****************************************************************************
 *  Name: Deque.java
 *  Date: 2019-03-03
 *  Description: Create a Deque data structure(https://en.wikipedia.org/wiki/Double-ended_queue)
 **************************************************************************** */

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node<Item> firstElement;
    private Node<Item> lastElement;
    private int size;

    public Deque() {

    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        Node<Item> node = new Node<>(item, firstElement, null);
        if (firstElement != null)
            firstElement.prevElement = node;
        else
            lastElement = node;
        firstElement = node;
        size++;
    }

    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }

        Node<Item> node = new Node<>(item, null, lastElement);
        if (lastElement != null)
            lastElement.nextElement = node;
        else
            firstElement = node;
        lastElement = node;
        size++;
    }

    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        Node<Item> originalFirstElement = firstElement;
        firstElement = firstElement.nextElement;
        if (firstElement == null)
            lastElement = null;
        else
            firstElement.prevElement = null;
        originalFirstElement.nextElement = null;
        size--;
        return originalFirstElement.item;
    }

    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }

        Node<Item> originalLastElement = lastElement;
        lastElement = lastElement.prevElement;
        if (lastElement == null)
            firstElement = null;
        else
            lastElement.nextElement = null;
        originalLastElement.prevElement = null;
        size--;
        return originalLastElement.item;
    }

    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class Node<NodeItem> {
        NodeItem item;
        Node<NodeItem> nextElement;
        Node<NodeItem> prevElement;

        public Node(NodeItem item, Node<NodeItem> nextElement, Node<NodeItem> prevElement) {
            this.item = item;
            this.nextElement = nextElement;
            this.prevElement = prevElement;
        }
    }

    private class DequeIterator implements Iterator<Item> {
        private Node<Item> currentElement = firstElement;

        @Override
        public boolean hasNext() {
            return currentElement != null;
        }

        @Override
        public Item next() {
            if (currentElement == null) {
                throw new NoSuchElementException();
            }
            Node<Item> originalCurrentElement = currentElement;
            currentElement = currentElement.nextElement;
            return originalCurrentElement.item;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args) {

    }
}
