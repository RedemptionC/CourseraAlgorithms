import java.util.HashMap;

class Solution {
    private class point {
        private int row;
        private int col;

        public point(int r, int c) {
            row = r;
            col = c;

        }
    }

    public int maxNumberOfFamilies(int n, int[][] reservedSeats) {
        // point[] hasPeople=new point[n];
        HashMap<point, Boolean> map = new HashMap<>();
        for (int i = 0; i < reservedSeats.length; i++) {
            point p = new point(reservedSeats[i][0] - 1, reservedSeats[i][1] - 1);
            // 表示当前位置有人
            map.put(p, true);
        }
        int count = 0;
        for (int row = 0; row < n; row++) {
            int t = 0;
            Boolean s1 = map.getOrDefault(new point(row, 1), null);
            Boolean s2 = map.getOrDefault(new point(row, 2), null);
            Boolean s3 = map.getOrDefault(new point(row, 3), null);
            Boolean s4 = map.getOrDefault(new point(row, 4), null);
            Boolean s5 = map.getOrDefault(new point(row, 5), null);
            Boolean s6 = map.getOrDefault(new point(row, 6), null);
            Boolean s7 = map.getOrDefault(new point(row, 7), null);
            Boolean s8 = map.getOrDefault(new point(row, 8), null);
            boolean c1 = s1 == null && s2 == null && s3 == null && s4 == null;
            boolean c2 = s3 == null && s4 == null && s5 == null && s6 == null;
            boolean c3 = s5 == null && s6 == null && s7 == null && s8 == null;
            if (c1 && c2 && c3) {
                t = 2;
            }
            else if (c1 && !c2 && c3) {
                t = 2;
            }
            else if (!c1 && !c2 && !c3) {
                t = 0;
            }
            else {
                t = 1;
            }
            count += t;
        }
        return count;
    }

    public static void main(String[] args) {
        int[][] arr = { { 1, 2 }, { 1, 3 }, { 1, 8 }, { 2, 6 }, { 3, 1 }, { 3, 10 } };
        System.out.println(new Solution().maxNumberOfFamilies(3, arr));
    }
}
