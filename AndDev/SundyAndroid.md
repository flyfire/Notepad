SundyAndroid
===============
#Android多线程系统概述
线程系统：操作系统内核实现了线程模型（核心级线程），Windows线程与进程的多对多模型，线程的调度者在核外。操作系统核外实现的线程（用户级线程），Linux，部分Unix，线程与进程的一对一，一对多模型，线程的调度者在核外。

Linux线程库：LinuxThreads/NPTL(Native Posix Thread Library)，新版内核多采用NPTL线程库。线程与进程一对一，轻量级进程资源共享。

Android进行多线程或者异步处理的方式：Thread/Runnable/Handler/AsyncTask。

进行IO操作（网络操作，文件操作，数据库操作），复杂的运算工作，消息接收方法中耗时操作，计划任务等时需要多线程和异步。

多线程与界面交互：``Activity.runOnUiThread(Runnable)``，``View.post(Runnable)``，``View.postDelay(Runnable, long)``，``Handler``，``AsyncTask``。

``Message``，``MessageQueue``，``Looper``，``Handler``