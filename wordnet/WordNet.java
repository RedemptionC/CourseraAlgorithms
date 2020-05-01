/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class WordNet {
    private Digraph wordnet;
    private HashMap<String, List<Integer>> nouns2id;
    private List<String> nouns;
    private SAP sap;

    private boolean isRootedDAG() {
        // 看似需要判断两个条件：只有一个根，没有环
        // 根的特点是出度为0，
        // 判断环，看似需要用拓扑排序，实际上，能不能进行拓扑排序，和不能有多个根，是等价的
        // 因此只需要判断是否恰有一个点的出度为0
        int count = 0;
        for (int i = 0; i < wordnet.V(); i++) {
            if (wordnet.outdegree(i) == 0)
                count++;
        }
        return count == 1;
    }

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null)
            throw new IllegalArgumentException("args can not be null");
        nouns = new ArrayList<>();
        nouns2id = new HashMap<>();
        In synsetsIn = new In(synsets);
        int count = 0;
        while (synsetsIn.hasNextLine()) {
            String line = synsetsIn.readLine();
            String[] fields = line.split(",");
            nouns.add(fields[1]);
            for (String noun : fields[1].split(" ")) {
                List<Integer> vertexs = nouns2id.getOrDefault(noun, new LinkedList<Integer>());
                vertexs.add(count);
                nouns2id.put(noun, vertexs);
            }
            count++;
        }
        // count的个数是节点（同义词集）的个数，而不是单词的个数
        wordnet = new Digraph(count);
        In hypernymsIn = new In(hypernyms);
        while (hypernymsIn.hasNextLine()) {
            String line = hypernymsIn.readLine();
            String[] data = line.split(",");
            Integer id = Integer.parseInt(data[0]);
            int j = 1;
            while (j < data.length) {
                wordnet.addEdge(id, Integer.parseInt(data[j++]));
            }
        }
        if (!isRootedDAG())
            throw new IllegalArgumentException("must be rooted dag");
        sap = new SAP(wordnet);
    }

    // returns all WordNet nouns
    // noun指的不是图的节点（集合），而是集合的元素（单词），用一个set保存?
    public Iterable<String> nouns() {
        return nouns2id.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null)
            throw new IllegalArgumentException("args can not be null");
        return nouns2id.keySet().contains(word);
    }

    // distance between nounA and nounB (defined below)
    // 注意，noun之间的距离，实际上是节点（集合）之间的距离，所以不应该只保存noun，应该保存noun和集合id的对应关系
    public int distance(String nounA, String nounB) {
        if (nounA == null || !isNoun(nounA) || nounB == null || !isNoun(nounB))
            throw new IllegalArgumentException("args can not be null");
        List<Integer> vA = nouns2id.get(nounA);
        List<Integer> vB = nouns2id.get(nounB);
        // if (sap == null)
        //     sap = new SAP(wordnet);
        return sap.length(vA, vB);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (nounA == null || !isNoun(nounA) || nounB == null || !isNoun(nounB))
            throw new IllegalArgumentException("args can not be null");
        List<Integer> vA = nouns2id.get(nounA);
        List<Integer> vB = nouns2id.get(nounB);
        // if (sap == null)
        //     sap = new SAP(wordnet);
        int ancestor = sap.ancestor(vA, vB);
        // 这里得到的ancestor是一个数字，返回的需要是单词？还是数字？我先返回数字吧，如果返回单词的路径，那就需要吧hashmap的kv反转
        // 👆 注意，这里之所以写成字符串为key，是因为本函数给出字符串，我需要得到数字
        // 这里的路径信息存在bfs的pathto里，但是sap又没有获取这个路径的接口，难道只有在本类里再实现一遍bfs的？
        return nouns.get(ancestor);
    }


    public static void main(String[] args) {
        WordNet wordNet = new WordNet("synsets3.txt", "hypernyms3InvalidTwoRoots.txt");
        // System.out.println(wordNet.sap("worm", "bird") + " " + wordNet.distance("worm", "bird"));
        // System.out.println(wordNet.nouns2id.get("zymosis"));
        // System.out.println(wordNet.nouns2id.keySet().size());
        // System.out.println(wordNet.nouns.size());
    }
}
