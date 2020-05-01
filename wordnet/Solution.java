class Solution {
    private boolean helper(ListNode head, TreeNode root) {
        if (head.next == null)
            return true;
        if (root.left != null && root.left.val == head.next.val)
            return helper(head.next, root.left);
        if (root.right != null && root.right.val == head.next.val)
            return helper(head.next, root.right);
        // 如果上面三个都没返回，说明链表还有，树没了
        return false;
    }

    public boolean isSubPath(ListNode head, TreeNode root) {
        if (root == null)
            return false;
        if (head != null && head.val == root.val)
            return helper(head, root);
        return isSubPath(head, root.left) || isSubPath(head, root.right);
    }
}
