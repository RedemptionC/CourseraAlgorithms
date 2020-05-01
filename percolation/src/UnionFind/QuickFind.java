package UnionFind;

import java.util.Scanner;
/*
* 这个算法union:O(n),find:O(1)
* 但是如果要对n个obj进行union操作,那么时间复杂度是O(n*n)
* 因为对于每个元素,都要遍历数组,修改与其属于同一
* 连通分量的obj对应的数组元素的值
* */
public class QuickFind {
    public static void showStatus(int[] arr){
        for(int i:arr){
            System.out.printf("%d ",i);
        }
        System.out.println();
    }

    public static void main(String[] args) {
        System.out.printf("input number of objects\n$");
        Scanner in=new Scanner(System.in);
        int n=in.nextInt();

        int[] arr=new int[n+1];
//        表示一开始所有元素都不相连
        for(int i=1;i<=n;i++){
            arr[i]=i;
        }

        int choice;
        System.out.printf("1.union\n2.find\n3.exit\n$");
        while(in.hasNext()){
            choice=in.nextInt();
            switch (choice){
                case 1:
                    System.out.printf("union(p,q)\n$");
                    int p=in.nextInt();
                    int q=in.nextInt();
//                    因为arr[p]会被修改,因此不能将他作为对照,需要提前保存
                    int old=arr[p];
                    for(int i=0;i<arr.length;i++){
                        if(arr[i]==old){
                            arr[i]=arr[q];
                        }
                    }
                    break;
                case 2:
                    System.out.println("find(p,q)\n$");
                    p=in.nextInt();
                    q=in.nextInt();
                    String rs=arr[p]==arr[q]?"connected":"not connected";
                    System.out.println(rs);
                    break;
                case 3:
                    System.out.println("press ctrl+d to exit");
                    break;
            }
            showStatus(arr);
            System.out.printf("1.union\n2.find\n3.exit\n$");
        }
    }
}
