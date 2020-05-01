三个文件 

## SAP 最短祖先路径

求两个东西：最短祖先路径的长度，以及最短祖先路径上的祖先

分为两种情况：求两个点的SAP，求两个Iterable的SAP

**这里需要厘清wordnet里的一些概念**

* 三个概念：noun,synset,word
* synset才是wordnet中的节点，因此求两个点的SAP就是求两个synset的SAP，求两个iterable的SAP就是求两组synset的SAP
* synset是同义词集，是noun的集合，而noun有时候不止一个词组成，如：36,AND_circuit AND_gate,a circuit in a computer that fires only when all of its inputs fire   ，这是synsets.txt中第36号同义词集的条目，这个synset包含两个noun，AND_circuit AND_gate，中间用空格分开，而这两个noun都由不止一个word组成，word之间用下划线分开
* 本project主要使用的是概念是noun和synset，不使用word

其实两个点，和两个iterable的SAP，算法是类似的：底层都是使用BFS，课堂上教的BFS（也包括DFS），其中在遍历时会用到三个数组，distTo，marked，edgeTo，其中marked很好理解，标记当前节点是否被访问，另外两个我之前一度很排斥，但是实践证明这两个数组可以让代码可复用性更高：

* edgeTo[i]，表示在当前遍历中，指向节点i的点
* distTo[i],表示从一个给定的节点，到节点i，的距离（边数

由于BFS的特性（一圈圈扩大范围），让他十分便于求最短路径

要求**两点的SAP**，就对两点分别运行BFS，然后对于点A，根据其distTo数组，把所有**可达**的点放进一个集合，然后遍历点B的distTo数组，如果当前点也从B可达，并且该点也在之前的集合里，则说明这个点对于AB都可达，那么该点就是一个公共祖先，此时只要选出其中距离最短(A的distTo加上B的distTo最小)，那么该点就是SAP上的祖先，相应的distTo也能算出来SAP的长度，然后根据edgeTo数组，就能反推出SAP的表示（当然本题并不需要

对于iterable间的SAP，其实唯一的区别在于BFS的实现上：对于单个起点的BFS，我们是把起点入队，然后依次访问邻接点，入队...直到队空

对于iterable为起点，也就是多个起点，其实唯一的区别在于，最开始不是入队一个点，而是把iterable中的所有点入队（当然在入队的时候，和之前一样，也是把相应的点的marked设为true，把distTo设为0（这个多源的BFS算法不难实现，但是理解很重要

## wordnet

写好了SAP，wordnet其实只要厘清数据结构，就很好写了：

为了减少io，需要在构造函数中把节点的信息(synsets.txt)，以及节点之间的信息(hypernyms.txt)保存到合适的数据机构中，数据结构的选择取决于需要：

* nouns和isNoun函数，对数据结构都没什么要求，是list，set都可以

* 关键在于sap和distance函数，他们的参数是noun，并且要求相应的sap，注意到：一个noun可能属于多个synset，所以我们需要获取的是该noun对应的全部synset，因此在读进数据时，我们选择hashset保存noun和synset的集合的关系：

  ```java
  private HashMap<String, List<Integer>> nouns2id;
  String line = synsetsIn.readLine();
  String[] fields = line.split(",");
  for (String noun : fields[1].split(" ")) {
      List<Integer> vertexs = nouns2id.getOrDefault(noun, new LinkedList<Integer>());
      vertexs.add(count);
      nouns2id.put(noun, vertexs);
  }
  ```

* 👆，首先判断当前noun是否已经对应了synset，如果没有，就新建一个list，然后把noun:list的对应放进hashmap；如果有，就取出该list，添加当前的synset，再放入。这样就实现了noun和全部synset（包含自己）的对应

设计好数据结构，剩下的就顺理成章了

## outcast

这个更简单，题意是给出一系列的noun，然后找出其中与其他noun语义差的最远的一个

这个语义差的最远，实际上利用了之前的实现：即sap的length

## 结语：没有一个bug不可修复

下面是之前得98分时候的bug，之前觉得这种random的样例很难reproduce，所以不是很抱希望，实际上根据他给的描述，随便写了个测试，发现原因在于：我之前想用保存v，w来判断之前的计算结果是否可重用，而Iterable我采取的方案不重用，一律重新计算sap，而bug出在这种情形：

首先计算两点的sap，再计算两iterable的sap，在计算两点的sap，而中间的一次计算没有重用之前的计算结果，但是修改了原来的计算结果，因此再后面遇到一样的点，判断为可以重用，殊不知结果已经被修改过了，解决方法是计算完iterable的sap后，把点v,w设为null，这样就不会在下次判断为可重用，从而得出错误的结果

> 2020年2月27日 14点57分
>
> 还剩两份，correctness里还有一个没通过
>
> Test 20: random calls to both version of length() and ancestor(),
>          with probabilities p1 and p2, respectively
>
>   * random calls in a random rooted DAG (20 vertices, 100 edges)
>     (p1 = 0.5, p2 = 0.5)
>     - no path from v or w to ancestor
>     - failed on call 77 to ancestor()
>     - v = 18, w = 6
>     - reference length   = 1
>     - student   ancestor = 4
>     - reference ancestor = 18
>
>   * random calls in a random digraph (20 vertices, 100 edges)
>     (p1 = 0.5, p2 = 0.5)
>     - failed on call 44 to length()
>     - v                  = 14
>     - w                  = 7
>     - student   length() = 0
>     - reference length() = 1
>
> ==> FAILED



