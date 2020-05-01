package UnionFind;

/**
 * @author red
 * 修改原因是：
 * 在quickUnion算法中，我们将两棵树
 * 连成一棵的操作是随机的，可能会生成一颗很长的树
 * 经过分析，决定采用size也就是节点总数而不是高度
 * 作为衡量标准
 */
public class QuickUnionImprove {
    private int[] roots;
//    private int[] depth;
    private int[] size;
    public QuickUnionImprove(int n){
        size=new int[n+1];
        roots=new int[n+1];
        for(int i=1;i<=n;i++){
            roots[i]=i;
            size[i]=1;
        }
    }

    //    get root
    private int root(int i){
        while(i!=roots[i]){
            roots[i]=roots[roots[i]];
            i=roots[i];
        }
        return i;
    }
//    要修改的是main tree的size，增加数为另一颗树的节点数
    public void Union(int p,int q){
        int rp=root(p);
        int rq=root(q);
//        如果两根已经在一棵树上，就不用合并
        if(rp==rq){
            return;
        }
        if(size[rp]<=size[rq]){
            roots[rp]=rq;
            size[rq]+=size[rp];
        }
        else{
            roots[rq]=rp;
            size[rp]+=size[rq];
        }
    }

    public boolean Find(int p,int q){
        return roots[p]==roots[q];
    }

    public void showStatus(){
        System.out.println("ids:");
        for(int i:roots){
            System.out.printf("%d ",i);
        }
        System.out.println();
        System.out.println("size");
        for(int i:size){
            System.out.printf("%d ",i);
        }
        System.out.println();
    }
    public static void main(String[] args) {
        QuickUnionImprove qu=new QuickUnionImprove(10);
        System.out.println("begin:");
        qu.showStatus();
        qu.Union(2,3);
        System.out.println("after union(2,3)");
        qu.showStatus();
        qu.Union(4,5);
        System.out.println("after union(4,5)");
        qu.showStatus();
        System.out.println(qu.Find(2,5));
//        qu.Union(2,4);
//        System.out.println(qu.Find(3,5));
//        qu.showStatus();
        qu.Union(2,6);
        System.out.println("after union(2,6)");
        qu.showStatus();
        qu.Union(2,4);
        System.out.println("after union(2,4)");
        qu.showStatus();
    }
}
/*
* begin:
ids:
0 1 2 3 4 5 6 7 8 9 10
size
0 1 1 1 1 1 1 1 1 1 1
after union(2,3)
ids:
0 1 3 3 4 5 6 7 8 9 10
size
0 1 1 2 1 1 1 1 1 1 1
after union(4,5)
ids:
0 1 3 3 5 5 6 7 8 9 10
size
0 1 1 2 1 2 1 1 1 1 1
false
after union(2,6)
ids:
0 1 3 3 5 5 3 7 8 9 10
size
0 1 1 3 1 2 1 1 1 1 1
after union(2,4)
ids:
0 1 3 3 5 3 3 7 8 9 10
size
0 1 1 5 1 2 1 1 1 1 1
* 加上路径压缩
* begin:
ids:
0 1 2 3 4 5 6 7 8 9 10
size
0 1 1 1 1 1 1 1 1 1 1
after union(2,3)
ids:
0 1 3 3 4 5 6 7 8 9 10
size
0 1 1 2 1 1 1 1 1 1 1
after union(4,5)
ids:
0 1 3 3 5 5 6 7 8 9 10
size
0 1 1 2 1 2 1 1 1 1 1
false
after union(2,6)
ids:
0 1 3 3 5 5 3 7 8 9 10
size
0 1 1 3 1 2 1 1 1 1 1
after union(2,4)
ids:
0 1 3 3 5 3 3 7 8 9 10
size
0 1 1 5 1 2 1 1 1 1 1
* */