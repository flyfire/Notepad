Java4Android
===============

```java
LinearLayout layoutRoot = (LinearLayout)this.getLayoutInflater().inflate(R.layout.main, null);
//ViewGroup中有多少个子组件View
int childCount = layoutRoot.getChildCount();
```

反射，依赖注入，控制反转 Java程序运行中，需要在运行时了解类的信息，得到类实例，并且继而得到类的方法，构造，权限，变量以及其他信息，这个时候就需要用到反射。

多线程 阻塞，互斥，信号量