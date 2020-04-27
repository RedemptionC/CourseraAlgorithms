package stack;

public class LinkedStackOfStrings {
    private class node {
        String item;
        node next;
    }

    private node first = null;

    public boolean isEmpty() {
        return first == null;
    }

    public void push(String item) {
//        node n = new node();
//        n.item = item;
//        // 如果是第一个
//        if (first == null) {
//            // 注意此时没有头节点，是尾插法
//            first = n;
//            first.next = null;
//            return;
//        }
//        n.next = first.next;
//        first.next = n;

//         实际上也是可以头插法的，只要首先保存头节点，不管它
//         为不为空，然后让新节点指向它，这样逻辑也更统一
        node older = first;
        first = new node();
        first.item = item;
        first.next = older;
    }

    public String pop() {
        if (isEmpty()) {
            return null;
        }
        String rs = first.item;
        first = first.next;
        return rs;
    }

    public static void main(String[] args) {
        LinkedStackOfStrings stack = new LinkedStackOfStrings();
        System.out.println(stack.isEmpty());
        stack.push("hello");
        System.out.println(stack.isEmpty());
        System.out.println(stack.pop());
        System.out.println(stack.isEmpty());
        stack.push("hello");
        stack.push("world");
        System.out.println("pop begin");
        while (!stack.isEmpty()) {
            System.out.println(stack.pop());
        }
    }
}
