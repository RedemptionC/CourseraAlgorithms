/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

package bst;

import edu.princeton.cs.algs4.Queue;

public class Bst {
    private static final boolean RED = true;
    private static final boolean BLACK = false;
    private int key, val;
    private Bst left, right;
    private int size;
    // llrb
    private boolean color;

    public String toString() {
        String t = color == RED ? "RED" : "BLACK";
        return key + " : " + val + " size: " + size + " color :" + t;
    }

    public Bst(int key, int val, boolean color) {
        this.key = key;
        this.val = val;
        this.left = this.right = null;
        this.size = 1;
        this.color = color;
    }

    public Bst search(int key) {
        return search(key, this);
    }

    public Bst search(int key, Bst node) {
        while (node != null && node.key != key) {
            if (node.key > key) {
                node = node.left;
                continue;
            }
            if (node.key < key) {
                node = node.right;
                continue;
            }
        }
        return node;
    }

    public Bst insert(int k, int v) {
        return insert(k, v, this);
    }

    private boolean isRED(Bst node) {
        if (node == null)
            return false;
        return node.color == RED;
    }

    public Bst insert(int k, int v, Bst node) {
        if (node == null)
            return new Bst(k, v, RED);
        if (node.key == k) {
            node.val = v;
        }
        else if (node.key < k) {
            node.right = insert(k, v, node.right);
        }
        else
            node.left = insert(k, v, node.left);
        node.size = 1 + size(node.left) + size(node.right);
        if (!isRED(node.left) && isRED(node.right))
            node = leftRotate(node);
        if (isRED(node.left) && isRED(node.left.left))
            node = rightRotate(node);
        if (isRED(node.left) && isRED(node.right))
            flipColor(node);
        return node;
    }

    private Bst leftRotate(Bst node) {
        Bst t = node.right;
        node.right = t.left;
        t.left = node;
        t.color = node.color;
        t.color = node.color;
        node.color = RED;
        node.size = 1 + node.size(node.left);
        t.size = 1 + t.size(t.left) + t.size(t.right);
        return t;
    }

    // 需要flip(这两个旋转函数之所以需要返回节点
    // 是因为原来位置的节点不是原来的节点了
    private Bst rightRotate(Bst node) {
        Bst t = node.left;
        node.left = t.right;
        t.right = node;
        t.color = node.color;
        node.color = RED;
        node.size = 1 + node.right.size();
        t.size = 1 + t.left.size() + t.right.size();
        return t;
    }

    private void flipColor(Bst node) {
        node.left.color = BLACK;
        node.right.color = BLACK;
        node.color = RED;
    }


    public Bst floor(int k) {
        return floor(k, this);
    }

    //  Largest key ≤ a given key.
    // 如果相等，那么就是他，如果node.key>key,那么在左子树里找
    // 如果node.key<key,那么要看它的右子树里有没有小于key的
    private Bst floor(int k, Bst node) {
        if (node == null)
            return null;
        if (node.key == k)
            return node;
        else if (node.key < k)
            return floor(k, node.right) == null ? node : floor(k, node.right);
        else return floor(k, node.left);
    }

    public Bst celling(int k) {
        return celling(k, this);
    }

    //Smallest key ≥ a given key.
    // 如果相等，直接返回。如果key<node.key,就看左子树有没有比他大的
    // 若key>node.key,看右子树
    private Bst celling(int k, Bst node) {
        if (node == null)
            return null;
        if (node.key == k)
            return node;
        else if (node.key < k)
            return celling(k, node.right);
        else
            return celling(k, node.left) == null ? node : celling(k, node.left);
    }

    public int size() {
        return size(this);
    }

    private int size(Bst node) {
        if (node == null)
            return 0;
        return node.size;
    }

    public void inorder() {
        inorder(this);
    }

    private void inorder(Bst node) {
        if (node == null)
            return;
        inorder(node.left);
        System.out.println(node);
        inorder(node.right);
    }

    // 三种情况:1.无孩子，那么只要找到他，并且设为null即可
    // 2.一个孩子，那么直接让孩子继位
    // 3.两孩子，那么直接与右子树的最小交换（不包括孩子指针），然后删除右子树中的该key
    public Bst delete(int key) {
        return delete(key, this);
    }

    private Bst delete(int key, Bst node) {
        if (node == null)
            return null;
        if (node.key == key) {
            if (node.left == null && node.right == null)
                return null;
            if (node.left == null || node.right == null)
                return node.left == null ? node.right : node.left;
            // 都不为空,返回右子树中最小的
            Bst t = findMin(node.right);
            swap(node, t);
            node.right = delete(key, node.right);
            return node;
        }
        else if (node.key > key) {
            node.left = delete(key, node.left);
        }
        else
            node.right = delete(key, node.right);
        return node;
    }

    private void swap(Bst n1, Bst n2) {
        int k = n1.key, v = n1.val;
        n1.key = n2.key;
        n1.val = n2.val;
        n2.key = k;
        n2.val = v;
    }

    public Bst findMin() {
        return findMin(this);
    }

    private Bst findMin(Bst node) {
        while (node.left != null)
            node = node.left;
        return node;
    }

    public void levelOrder() {
        Queue<Bst> queue = new Queue<>();
        queue.enqueue(this);
        while (!queue.isEmpty()) {
            // 用一个容器把他装起来，然后每次输出一层
            Queue<Bst> thisLevel = new Queue<>();
            while (!queue.isEmpty()) {
                Bst t = queue.dequeue();
                System.out.printf("%s ** ", t.toString());
                if (t.left != null)
                    thisLevel.enqueue(t.left);
                if (t.right != null)
                    thisLevel.enqueue(t.right);
            }
            System.out.println();
            while (!thisLevel.isEmpty()) {
                queue.enqueue(thisLevel.dequeue());
            }
        }
    }

    public static void main(String[] args) {
        // 这种实习和官方对比就有一个问题，那就是我第一次插入不是insert，而是在构造函数
        // 虽然现在也能用，但是有时候会显得很别扭
        Bst bst = new Bst(0, 0, RED);
        for (int i = 0; i < 10; i++) {
            bst = bst.insert(i, i);
        }
        // System.out.println(bst.search(0));
        // System.out.println(bst.search(1));
        // System.out.println(bst.search(2));
        // System.out.println(bst.search(-1));
        // System.out.println(bst.floor(-1));
        // System.out.println(bst.celling(2));
        // bst.inorder();
        // System.out.println();
        // bst = bst.delete(1);
        // bst.inorder();
        bst.levelOrder();
    }
}
