class Solution {
    // gcd里一定包含了s1,s2中有的全部种类的字符
    // 且s1,s2一定是一个模式（字符串）进行重复得到的，重复的次数最低为1
    // s1,s2都可以通过gcd进行重复得到

    // 其中last表示第一个子串的下一个字符
    private boolean isSubString(String s, int len, int last) {
        // 首先判断长度能否整除
        if (len % last != 0)
            return false;
        for (int i = 0; i < len; ) {
            for (int j = 0; j < last; ) {
                if (s.charAt(i++) != s.charAt(j++)) {
                    return false;
                }
            }
        }
        return true;
    }

    public String gcdOfStrings(String str1, String str2) {
        int len1 = str1.length();
        int len2 = str2.length();
        // 首先找到s1中第一个子串（可以通过重复组成整个串
        int l1 = 0;
        // 这个循环，如果结束后l1还为0，那么说明没有与他相同的，如:ABCDEF这种
        for (int i = 1; i < len1; i++) {
            if (str1.charAt(i) == str1.charAt(0)) {
                l1 = i;
                break;
            }
        }
        if (l1 == 0) {
            l1 = len1;
        }
        // 还需要判断这个子串是否真的能通过重复得到s1,否则直接返回""
        if (!isSubString(str1, len1, l1)) {
            return "";
        }
        // 现在来找s2中第一个子串
        int l2 = 0;
        for (int i = 1; i < len2; i++) {
            if (str2.charAt(i) == str2.charAt(0)) {
                l2 = i;
                break;
            }
        }
        if (l2 == 0) {
            l2 = len2;
        }
        if (!isSubString(str2, len2, l2)) {
            return "";
        }
        // 现在来取一个最大公约数长度的字符串
        String sub1 = str1.substring(0, l1);
        String sub2 = str2.substring(0, l2);
        if (sub1.equals(sub2)) {
            return sub1;
        }
        else
            return "";
    }

    public static void main(String[] args) {
        // new Solution().gcdOfStrings("OBCNOOBCNOOBCNOOBCNOOBCNOOBCNOOBCNOOBCNO",
        //                             "OBCNOOBCNOOBCNOOBCNOOBCNOOBCNOOBCNOOBCNOOBCNOOBCNOOBCNOOBCNOOBCNO");
        System.out.println("hello,vscode!!!");
        System.out.println("hello");
    }
}
