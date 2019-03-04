/* *****************************************************************************
 *  Name: RandomizedQueue
 *  Date: 2019-03-03
 *  Description: Implements a RandomizedQueue (similar to stack or queue but item removed is chosen uniformly at random)
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] queue;
    private int finalElementIndex;
    private int size;

    public RandomizedQueue() {
        queue = (Item[]) new Object[1];
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void enqueue(Item item) {
        if (item == null)
            throw new IllegalArgumentException();
        if (queue.length == size)
            resizeQueue();
        queue[finalElementIndex] = item;
        finalElementIndex++;
        size++;
    }

    public Item dequeue() {
        if (isEmpty())
            throw new NoSuchElementException();
        int random = StdRandom.uniform(size);
        Item randomDequeElement = queue[random];
        queue[random] = queue[finalElementIndex];
        queue[finalElementIndex] = null;
        finalElementIndex--;
        size--;
        return randomDequeElement;
    }

    public Item sample() {
        if (isEmpty())
            throw new NoSuchElementException();
        int random = StdRandom.uniform(size);
        return queue[random];
    }

    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private void resizeQueue() {
        Item[] resizedQueue = (Item[]) new Object[2 * size];
        int i = 0;
        for (Item item : queue) {
            resizedQueue[i] = item;
            i++;
        }
        queue = resizedQueue;
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        Item[] itemRandomizedArray = (Item[]) new Object[size];
        int iterator = 0;

        public RandomizedQueueIterator() {
            for (int i = 0; i < size; i++) {
                int random = StdRandom.uniform(i + 1);
                itemRandomizedArray[random] = queue[i];
                itemRandomizedArray[i] = queue[random];
            }
        }

        @Override
        public boolean hasNext() {
            return !isEmpty();
        }

        @Override
        public Item next() {
            if (isEmpty())
                throw new NoSuchElementException();
            return itemRandomizedArray[iterator++];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args)   // unit testing (optional)
    {

    }
}
