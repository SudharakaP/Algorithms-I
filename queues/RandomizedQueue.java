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
        size++;
        queue[size - 1] = item;

    }

    public Item dequeue() {
        if (isEmpty())
            throw new NoSuchElementException();
        int random = StdRandom.uniform(size);
        Item randomDequeElement = queue[random];
        queue[random] = queue[size - 1];
        queue[size - 1] = null;
        size--;
        if (size < queue.length / 4 && size != 0)
            resizeQueue();
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
            if (i < resizedQueue.length) {
                resizedQueue[i] = item;
                i++;
            }
        }
        queue = resizedQueue;
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        Item[] itemRandomizedArray = queue.clone();
        int iterator = 0;

        public RandomizedQueueIterator() {
            for (int i = 0; i < size; i++) {
                int random = StdRandom.uniform(i + 1);
                Item swap = itemRandomizedArray[i];
                itemRandomizedArray[i] = itemRandomizedArray[random];
                itemRandomizedArray[random] = swap;
            }
        }

        @Override
        public boolean hasNext() {
            if (iterator == itemRandomizedArray.length)
                return false;
            return itemRandomizedArray[iterator] != null;
        }

        @Override
        public Item next() {
            if (isEmpty() || iterator == itemRandomizedArray.length)
                throw new NoSuchElementException();
            return itemRandomizedArray[iterator++];
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    public static void main(String[] args) {

    }
}
