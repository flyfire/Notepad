Java4Android
===============
From Sundy Java4Android视频

```java
LinearLayout layoutRoot = (LinearLayout)this.getLayoutInflater().inflate(R.layout.main, null);
//ViewGroup中有多少个子组件View
int childCount = layoutRoot.getChildCount();
```

+ 一切皆类，Android中xml文件-->类
+ 使用instanceof检验对象类型，控件元素继承自View
+ 用Arrays工作，string array,etc
+ Iteration迭代器
+ Reflection反射基础
+ Cursor与数据库操作的应用
+ Inner Class内部类(成员内部类/局部内部类/静态内部类/匿名内部类)
+ Javadoc 代码文档 annotation
+ String基础知识
+ Date&Time基础
+ Generics泛型基础
+ Java Thread基础
+ Java 网络编程
+ Java I/O操作

反射，依赖注入，控制反转 Java程序运行中，需要在运行时了解类的信息，得到类实例，并且继而得到类的方法，构造，权限，变量以及其他信息，这个时候就需要用到反射。

多线程 阻塞，互斥，信号量