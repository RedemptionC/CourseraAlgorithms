/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BaseballElimination {
    private int numOfTeams;
    // private List<String> teams;
    private String[] teams;
    private int[] win;
    private int[] lose;
    private int[] remain;
    private int[][] remainGameBetween;
    // 需要建立队名与节点的数值表示的映射
    private HashMap<String, Integer> teamToValue;
    private FlowNetwork flowNetwork;
    private FordFulkerson fordFulkerson;
    // 保存上一个被判断的team
    private String lastTeam;
    private String trivalCertTeam;

    private boolean validTeam(String team) {
        return teamToValue.containsKey(team);
    }

    // create a baseball division from given filename in format specified below
    public BaseballElimination(String filename) {
        In in = new In(filename);
        numOfTeams = Integer.parseInt(in.readLine());
        teams = new String[numOfTeams];
        win = new int[numOfTeams];
        lose = new int[numOfTeams];
        remain = new int[numOfTeams];
        remainGameBetween = new int[numOfTeams][numOfTeams];
        teamToValue = new HashMap<>();
        int i = 0;
        while (in.hasNextLine()) {
            // 下面因为已经判断hasNextLine了，所以不会为null
            String s = in.readLine().trim();
            String[] info = s.split(" +");
            teams[i] = (info[0]);
            win[i] = Integer.parseInt(info[1]);
            lose[i] = Integer.parseInt(info[2]);
            remain[i] = Integer.parseInt(info[3]);
            for (int j = 4; j < info.length; j++) {
                remainGameBetween[i][j - 4] = Integer.parseInt(info[j]);
            }
            teamToValue.put(info[0], i);
            i++;
        }
    }

    // number of teams
    public int numberOfTeams() {
        return numOfTeams;
    }

    // all teams
    public Iterable<String> teams() {
        return teamToValue.keySet();
    }

    // number of wins for given team
    public int wins(String team) {
        if (!validTeam(team))
            throw new IllegalArgumentException("no such team");
        return win[teamToValue.get(team)];
    }

    // number of losses for given team
    public int losses(String team) {
        if (!validTeam(team))
            throw new IllegalArgumentException("no such team");
        return lose[teamToValue.get(team)];
    }

    // number of remaining games for given team
    public int remaining(String team) {
        if (!validTeam(team))
            throw new IllegalArgumentException("no such team");
        return remain[teamToValue.get(team)];
    }

    // number of remaining games between team1 and team2
    public int against(String team1, String team2) {
        if (!validTeam(team1) || !validTeam(team2))
            throw new IllegalArgumentException("no such team");
        return remainGameBetween[teamToValue.get(team1)][teamToValue.get(team2)];
    }

    private void constructNetwork(String team) {

        int excludeTeam = teamToValue.get(team);
        int numOfV = 2 + (numOfTeams - 1) + (numOfTeams * numOfTeams - 3 * numOfTeams + 2) / 2;
        flowNetwork = new FlowNetwork(numOfV);
        int luckyWin = wins(team) + remaining(team);
        // 首先建立team与t之间的edge
        for (int i = 0; i < numOfTeams; i++) {
            if (i == excludeTeam)
                continue;
            flowNetwork.addEdge(new FlowEdge(i, numOfV - 1, luckyWin - wins(teams[i])));
        }
        // 然后建立s->game->team这两种edge
        int delta = 0;
        for (int t1 = 0; t1 < numOfTeams; t1++) {
            if (t1 == excludeTeam)
                continue;
            // 由于对阵双方的比赛是一个对称矩阵，我们只取上三角
            for (int t2 = t1 + 1; t2 < numOfTeams; t2++) {
                if (t2 == excludeTeam)
                    continue;

                // 对于每一个比赛节点，既需要连接到s，也需要连接到对应的比赛双方
                int cap = remainGameBetween[t1][t2];
                // 使用excludeTeam的value作为s
                flowNetwork.addEdge(new FlowEdge(excludeTeam, numOfTeams + delta, cap));
                flowNetwork.addEdge(new FlowEdge(numOfTeams + delta, t1, Double.POSITIVE_INFINITY));
                flowNetwork.addEdge(new FlowEdge(numOfTeams + delta, t2, Double.POSITIVE_INFINITY));
                delta++;
            }
        }
    }

    // is given team eliminated?
    public boolean isEliminated(String team) {
        lastTeam = team;
        if (!validTeam(team))
            throw new IllegalArgumentException("no such team");
        // 首先判断win+remain是否小于其中之一
        int luckyWin = wins(team) + remaining(team);
        for (int i = 0; i < win.length; i++) {
            if (luckyWin < win[i]) {
                trivalCertTeam = teams[i];
                fordFulkerson = null;
                return true;
            }
        }
        // 然后通过max flow来判断
        constructNetwork(team);
        // 检查从s指出的边，只要有一个not full:flow<cap,那么eliminated
        int numOfV = 2 + (numOfTeams - 1) + (numOfTeams * numOfTeams - 3 * numOfTeams + 2) / 2;
        int excludeTeam = teamToValue.get(team);
        // System.out.println(flowNetwork.toString());
        fordFulkerson = new FordFulkerson(flowNetwork, excludeTeam, numOfV - 1);
        for (FlowEdge e : flowNetwork.edges()) {
            if (e.from() == excludeTeam && e.flow() < e.capacity())
                return true;
        }
        // System.out.println(flowNetwork.toString());
        // System.out.println(fordFulkerson.value());
        // for (int i = 0; i < numOfTeams; i++) {
        //     System.out.println(i + " in cut :" + fordFulkerson.inCut(i));
        // }
        return false;
    }

    // subset R of teams that eliminates given team; null if not eliminated
    // 这里要判断是否之前调用了isEliminated，如果没有，就要自己调用,如果结果为false,那么返回null
    public Iterable<String> certificateOfElimination(String team) {
        if (!validTeam(team))
            throw new IllegalArgumentException("no such team");
        boolean isEliminate = true;
        if (!team.equals(lastTeam)) {
            isEliminate = isEliminated(team);
        }
        if (isEliminate) {
            int excludeTeam = teamToValue.get(team);
            List<String> rs = new ArrayList<>();
            if (fordFulkerson == null) {
                rs.add(trivalCertTeam);
                return rs;
            }
            for (int i = 0; i < numOfTeams; i++) {
                if (i == excludeTeam)
                    continue;
                if (fordFulkerson.inCut(i)) {
                    rs.add(teams[i]);
                }
            }
            return rs;
        }
        else
            return null;
    }

    public static void main(String[] args) {
        BaseballElimination division = new BaseballElimination(args[0]);
        System.out.println(division.certificateOfElimination("Atlanta"));

    }
}
