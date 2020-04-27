三个类

主要是使用数组(resizing array)和链表实现一些可重用的数据结构

## RandomizedQueue

因为要实现在出队时，随机选择元素，所有要使用（resizing）array实现

resizing array有几个点：

* 扩容时，新建一个满足需求容量的数组，然后把原数组的元素移过来
* 当仅容纳1/4的元素时，进行缩容，缩为原来的一半
* 删除元素时，把最后一个元素赋值给出队元素的位置，然后size--

## Deque

就是实现一个双端链表,同时需要熟悉java中iterator和泛型的使用

## Permutation

是对随机队列的一个应用，输出所有输入字符串的一个排列

