class Solution {
    public int fib(int n) {
        double a0 = 0, a1 = 1;
        if (n == 0)
            return (int) a0;
        if (n == 1)
            return (int) a1;
        for (int i = 2; i <= n; i++) {
            double t = a1;
            a1 = a0 + a1;
            a0 = t;
            System.out.println(a1 % (1e9 + 7));
        }
        return (int) (a1 % (1e9 + 7));
    }
//    public int fib(int n) {
//        if (n == 0) {
//            return 0;
//        }
//        if (n == 1) {
//            return 1;
//        }
//        long first = 0;
//        long sec = 1;
//        for (int i = 2; i <= n; i++) {
//            long nval = first + sec;
//            first = sec;
//            sec = nval % 1000000007;
//            System.out.println(sec);
//        }
//        return (int) sec;
//    }

    public static void main(String[] args) {
        System.out.println(new Solution().fib(100));
    }
}
