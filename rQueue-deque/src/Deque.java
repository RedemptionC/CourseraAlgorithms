import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;
import java.util.NoSuchElementException;


public class Deque<Item> implements Iterable<Item> {
    private node first, last;
    private int size;

    public Iterator<Item> iterator() {
        return new dequeIterator();
    }

    private class node {
        Item item;
        node next, prev;
    }

    private class dequeIterator implements Iterator<Item> {
        private node current = first;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("no more items");
            }
            Item t = current.item;
            current = current.next;
            return t;
        }

        public void remove() {
            throw new UnsupportedOperationException("no remove operation");
        }
    }

    public Deque() {
        first = last = null;
        size = 0;
    }

    public boolean isEmpty() {
        return first == null;
    }

    public int size() {
        return size;
    }

    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("arg can not be null");
        }
        node n = new node();
        n.item = item;
        n.next = first;
        n.prev = null;
        if (size == 0) {
            // 如果添加之前为空，需要设置last,first为新增元素
            last = n;
            first = n;
        } else {
            first.prev = n;
            first = n;
        }
        size++;
    }

    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("arg can not be null");
        }
        node n = new node();
        n.item = item;
        n.next = null;
        n.prev = last;
        if (size == 0) {
            last = n;
            first = n;
        } else {
            last.next = n;
            last = n;
        }
        size++;
    }

    public Item removeFirst() {
        // 注意要判断非空
        if (isEmpty()) {
            throw new NoSuchElementException("can not remove when deque is empty");
        }
        node t = first;
        Item i = t.item;
        if (size == 1) {
            last = first = null;
            size--;
            return i;
        }
        first = first.next;
        first.prev = null;
        size--;
        t = null;
        return i;
    }

    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("can not remove when deque is empty");
        }

        node t = last;
        Item i = t.item;
        if (size == 1) {
            last = first = null;
            size--;
            return i;
        }
        last = last.prev;
        t = null;
        last.next = null;
        size--;
        return i;
    }


    public static void main(String[] args) {
        Deque<String> deque = new Deque<>();
        for (int i = 0; i < 3; i++) {
            deque.addLast(i + "");
        }
        while (!deque.isEmpty()) {
            StdOut.println(deque.removeFirst());
        }
        Deque<Integer> ideque = new Deque<>();
        for (int i = 0; i < 20; i++) {
            ideque.addFirst(i);
        }
        Iterator<Integer> iterator = ideque.iterator();
        while (iterator.hasNext()) {
            StdOut.println(iterator.next());
        }

        while (!ideque.isEmpty()) {
            StdOut.println(ideque.removeLast());
        }
        System.out.println("************************");
        for (int i = 0; i < 3; i++) {
            if (i % 2 == 0) {
                ideque.addFirst(i);
            } else {
                StdOut.println(ideque.removeLast());
            }
        }

    }
}
