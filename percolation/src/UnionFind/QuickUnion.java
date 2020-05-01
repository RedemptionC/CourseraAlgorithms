package UnionFind;

public class QuickUnion {
    private int roots[];

    public QuickUnion(int n){
        roots=new int[n+1];
        for(int i=1;i<=n;i++){
            roots[i]=i;
        }
    }

//    get root
    private int root(int i){
        while(i!=roots[i]){
            i=roots[i];
        }
        return i;
    }

    public void Union(int p,int q){
        int rp=root(p);
        int rq=root(q);
//        因为对于根来说,对应数组的值就是自身
//        因此这里直接把p的根设为q的跟
        roots[rp]=rq;
    }

    public boolean Find(int p,int q){
        return roots[p]==roots[q];
    }

    public void showStatus(){
        for(int i:roots){
            System.out.printf("%d ",i);
        }
        System.out.println();
    }
    public static void main(String[] args) {
        QuickUnion qu=new QuickUnion(10);
        qu.showStatus();
        qu.Union(2,3);
        qu.showStatus();
        qu.Union(4,5);
        qu.showStatus();
        System.out.println(qu.Find(2,5));
        qu.showStatus();
        qu.Union(2,4);
        System.out.println(qu.Find(3,5));
        qu.showStatus();
    }
}
/*
* 注意这里并没有修改同一连通分量里所有节点的根
* 而是通过树的结构,仅修改一个把他们相连
* */
/*
* 0 1 2 3 4 5 6 7 8 9 10
0 1 3 3 4 5 6 7 8 9 10
0 1 3 3 5 5 6 7 8 9 10
false
0 1 3 3 5 5 6 7 8 9 10
true
0 1 3 5 5 5 6 7 8 9 10
* */