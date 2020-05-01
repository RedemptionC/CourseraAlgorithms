/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

public class KMP {
    public static void main(String[] args) {
        String pat = args[0];
        // 假设只有ABC三种字符
        int[][] dfa = new int[3][pat.length()];
        // 每一行从上到下对应ABC
        dfa[pat.charAt(0) - 'A'][0] = 1;
        int x = 0;
        for (int i = 1; i < pat.length(); i++) {
            for (int j = 0; j < 3; j++) {
                dfa[j][i] = dfa[j][x];
            }
            dfa[pat.charAt(i) - 'A'][i] = i + 1;
            System.out.printf("%d ", x);
            // 代表每次在之前的restart state上根据当前的字符进行转台转移
            x = dfa[pat.charAt(i) - 'A'][x];
        }
        System.out.println();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < pat.length(); j++) {
                System.out.printf("%d ", dfa[i][j]);
            }
            System.out.println();
        }
    }
}
