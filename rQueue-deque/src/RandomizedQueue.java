import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

/*
 * 因为要在O(1)内获取随机位置，所以必须使用数组保存
 * resizing array
 * 数组满时double
 * 数组只有四分之一满时，half，half时注意要新建一个数组然后把
 * 原来数组里的元素逐一复制到新数组里去，然后指向新数组
 * */
public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] rqueue;
    private int size, capacity;

    public RandomizedQueue() {
        rqueue = (Item[]) new Object[1];
        size = 0;
        capacity = 1;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    private void resize(int newcap) {
        Item[] newqueue = (Item[]) new Object[newcap];
        // 注意，resize也可能变小，所以这里的循环控制条件不是size，而是size和newcap
        for (int i = 0; i < size && i < newcap; i++) {
            newqueue[i] = rqueue[i];
        }
        rqueue = newqueue;
        capacity = newcap;
    }

    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("arg can not be null");
        }
        // 如果恰好只有一个空位，也要resize，因为size指向的是下一个？
        if ((capacity - size) <= 1) {
            resize(capacity * 2);
        }
        rqueue[size++] = item;
    }

    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("can not remove when deque is empty");
        }
        if (size == capacity / 4) {
            resize(capacity / 2);
        }
        int index = StdRandom.uniform(size);
        Item rs = rqueue[index];
        // 把最后一个移到出队的元素处，并size--
        rqueue[index] = rqueue[--size];
        return rs;
    }

    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException("can not remove when deque is empty");
        }
        int index = StdRandom.uniform(size);
        return rqueue[index];
    }

    private class rqueueIterator implements Iterator<Item> {
        //用一个元素记录已遍历的个数
//        private int cur;
        private Item[] copy;
        private int copySize;

        public rqueueIterator() {
            //创建一份拷贝，每输出一个就deque掉
            copy = (Item[]) new Object[size];
            for (int i = 0; i < size; i++) {
                copy[i] = rqueue[i];
            }
            copySize = size;
        }

        public boolean hasNext() {
            return copySize > 0;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("no more items");
            }
            int index = StdRandom.uniform(copySize);
            // 因为每次都是随机选一个，所以不能从前到后记录遍历位置，而是把已经遍历的元素直接出队
            // 这样就不会选到已经遍历的元素
            Item rs = copy[index];
            copy[index] = copy[--copySize];
            return rs;
        }

        public void remove() {
            throw new UnsupportedOperationException("no remove operation");
        }
    }

    public Iterator<Item> iterator() {
        return new rqueueIterator();
    }

    public static void main(String[] args) {
        RandomizedQueue<String> randomizedQueue = new RandomizedQueue<>();
        for (int i = 0; i < 1000; i++) {
            randomizedQueue.enqueue(i + "");
//            randomizedQueue.dequeue();
        }
        StdOut.println(randomizedQueue.size());
        for (String i : randomizedQueue) {
            StdOut.println(i);
        }
        StdOut.println("*****************************");
        for (int i = 0; i < 3; i++) {
            StdOut.println(randomizedQueue.sample());
        }
        StdOut.println("*****************************");
        StdOut.println("test iterator independence");
        for (String i : randomizedQueue) {
            for (String j : randomizedQueue) {
                StdOut.println(i + "-" + j);
            }
        }
        StdOut.println("*****************************");

        while (!randomizedQueue.isEmpty()) {
            StdOut.println(randomizedQueue.dequeue());
        }
        StdOut.println(randomizedQueue.isEmpty());

    }

}
/* 这才才是independent iterator
*         int[] a = {1, 2, 3};
            for (int i : a) {
                for (int j : a) {
                    System.out.println(i + "-" + j);
                }
            }
* 1-1
1-2
1-3
2-1
2-2
2-3
3-1
3-2
3-3
* */
