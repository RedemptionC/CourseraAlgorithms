public class Trie {
    private class Node {
        private Integer value;
        private Node[] next = new Node[26];
    }

    private Node root;
    private int count;// for setting value

    /** Initialize your data structure here. */
    public Trie() {
        root = new Node();
        count = 0;
    }

    private Node insert(String word, int val, Node node, int d) {
        if (node == null) {
            node = new Node();
        }
        if (d == word.length()) {
            node.value = val;
            return node;
        }
        int c = word.charAt(d) - 'A';
        node.next[c] = insert(word, val, node.next[c], d + 1);
        return node;
    }

    /** Inserts a word into the trie. */
    public void insert(String word) {
        insert(word, count, root, 0);
        count++;
    }

    private boolean search(String word, Node node, int d) {
        if (node == null) {
            return false;
        }
        if (d == word.length()) {
            if (node.value == null)
                return false;
            else
                return true;
        }
        int c = word.charAt(d) - 'A';
        return search(word, node.next[c], d + 1);
    }

    /** Returns if the word is in the trie. */
    public boolean search(String word) {
        return search(word, root, 0);
    }

    // 注意，这里是判断是否有以prefix为前缀的字符串在trie里，所以当d==len后，下一步是判断node.next是否有不为null
    private boolean startsWith(String prefix, Node node, int d) {
        if (d > prefix.length()) {
            return false;
        }
        if (node == null) {
            return false;
        }
        // 按说这里不能只根据next[i]不为空就判断存在，但是因为这里没有delete，所以只要next[i]不为空
        // 那么对应的value也不为空（也就是不会被删除）
        if (d == prefix.length()) {
            return true;
        }
        int c = prefix.charAt(d) - 'A';
        return startsWith(prefix, node.next[c], d + 1);
    }

    /** Returns if there is any word in the trie that starts with the given prefix. */
    public boolean startsWith(String prefix) {
        return startsWith(prefix, root, 0);
    }

    public static void main(String[] args) {
        Trie trie = new Trie();
        trie.insert("apple");
        System.out.println(trie.search("apple"));   // 返回 true
        System.out.println(trie.search("app"));    // 返回 false
        System.out.println(trie.startsWith("app")); // 返回 true
        trie.insert("app");
        System.out.println(trie.search("app"));     // 返回 true

    }
}

/**
 * Your Trie object will be instantiated and called as such:
 * Trie obj = new Trie();
 * obj.insert(word);
 * boolean param_2 = obj.search(word);
 * boolean param_3 = obj.startsWith(prefix);
 */
