package stack;

public class ArrayStackOfStrings {
    private String[] stack;
    private int top = 0;

    public ArrayStackOfStrings(int cap) {
        stack = new String[cap];
    }

    public void push(String item) {
        stack[top] = item;
        top++;
    }

    // 考虑到java的gc，这里有一个问题
    // 在c里，我会手动的free stack数组
    // 但是java是根据有无引用进行gc的
    // 所以即使pop了，但是数组没有改变
    // 为了帮助gc，可以把已经pop的项设为null
    public String pop() {
        top--;
        String i = stack[top];
        stack[top] = null;
        return i;
    }

    public boolean isEmpty() {
        return top == 0;
    }

    public static void main(String[] args) {
        ArrayStackOfStrings stack = new ArrayStackOfStrings(3);
        stack.push("hello1");
        stack.push("world");
        stack.push("hello2");
        while (!stack.isEmpty()) {
            System.out.println(stack.pop());
        }
//        System.out.println(stack.pop());
    }

}
