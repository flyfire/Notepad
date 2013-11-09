Sunny Android
==============
+ Android系统启动的时候，PMS读取所有应用程序的Manifest信息，并保存到系统级共享内存里。应用程序启动时，Launcher从系统级共享内存里读取应用程序的相关信息，进行系统级判断，如最小SDK等，进行实例化。
+ 一个Activiy构造的时候会构造一个窗口PhoneWindow，并且只有一个。这个窗口有一个ViewRoot(ViewGroup，View)，在根视图上面addView添加View。在视图上的操作，WindowManagerService接收消息，并且回调Activity函数。
+ ``Context`` ``ContextImpl``