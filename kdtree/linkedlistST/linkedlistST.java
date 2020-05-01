/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

package linkedlistST;

public class linkedlistST {
    private list[] lists;
    private int cap;

    public linkedlistST(int cap) {
        this.cap = cap;
        lists = new list[cap];
        for (int i = 0; i < cap; i++) {
            lists[i] = new list();
            lists[i].key = i;
            lists[i].next = null;
        }
    }

    // 2:A这种形式的键值对
    // 应该先进行搜素，如果存在，则直接修改值?
    public void insert(char val, int key) {
        int index = key % cap;
        list pos = lists[index];
        if (search(key) != null) {
            while (pos.key != key)
                pos = pos.next;
            pos.data = val;
            return;
        }
        // 找到对应list可以插入的位置
        // 直接在开头插入!符合局部性
        list node = new list();
        node.data = val;
        node.key = key;
        node.next = pos.next;
        pos.next = node;
    }

    public Character search(int key) {
        int index = key % cap;
        list pos = lists[index].next;
        while (pos != null && pos.key != key)
            pos = pos.next;
        if (pos == null)
            return null;
        else
            return pos.data;
    }

    private class list {
        char data;
        Integer key;
        list next;
    }

    public static void main(String[] args) {
        linkedlistST st = new linkedlistST(26);
        System.out.println(st.search(1));
        st.insert('A', 2);
        st.insert('B', 28);
        System.out.println(st.search(2));
        System.out.println(st.search(28));
        st.insert('C', 2);
        System.out.println(st.search(2));
    }
}
