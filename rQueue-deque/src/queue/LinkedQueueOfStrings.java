package queue;

public class LinkedQueueOfStrings {

    private class node {
        String item;
        node next;
    }

    // 会被自动初始化为null(但是不是不应该依赖这种特性？
    private node first, last;

    public boolean isEmpty() {
        return first == null;
    }

    public void enqueue(String item) {
        node oldlast = last;
        last = new node();
        last.item = item;
        last.next = null;
        if (isEmpty()) {
            first = last;
        } else {
            oldlast.next = last;
        }
    }

    public String dequeue() {
        if (isEmpty()) {
            return null;
        }
        String item = first.item;
        first = first.next;
        if (isEmpty()) {
            // 如果移除一个后空，那么last=null
            last = null;
        }
        return item;
    }

    public static void main(String[] args) {
        LinkedQueueOfStrings queue = new LinkedQueueOfStrings();
        System.out.println(queue.isEmpty());
        queue.enqueue("hello");
        queue.enqueue(",");
        queue.enqueue("world");
        queue.enqueue("!");
        while (!queue.isEmpty()) {
            System.out.println(queue.dequeue());
        }
    }
}

