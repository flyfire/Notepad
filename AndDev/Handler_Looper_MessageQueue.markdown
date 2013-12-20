Handler,Looper,MessageQueue
=============================

+ ``Message`` Handler接收和处理的消息对象。
+ ``Looper`` 每个线程只能拥有一个Looper，它的loop方法负责读取MessageQueue中的消息，独到消息之后就把该消息交给该消息的Handler进行处理。
+ ``MessageQueue`` 消息队列，它采用FIFO的方式来管理Message，程序创建Looper时会在它的构造函数中创建Looper对象。 程序在初始化Looper时会创建一个与之关联的MessageQueue，这个MessageQueue就负责管理消息。

``Handler``的作用有两个，发送消息和处理消息。程序使用Handler发送消息，被Handler发送的消息必须被送到指定的MessageQueue。也就是说，如果希望Handler正常工作，必须保证当前线程中有过一个MessageQueue，否则消息就没有MessageQueue进行保存了。不过MessageQueue是由Looper负责管理的，也就是说，如果希望Handler正常工作，必须在当前线程中有一个Looper对象，为了保证当前线程中有Looper对象，可以分以下2中情况进行处理。
+ 主UI线程中，系统已经初始化了一个Looper对象，因此程序直接创建Handler即可，然后就可通过Handler来发送消息、处理消息。
+ 自己启动的线程中，必须自己创建一个Looper对象，并启动它，创建Looper对象使用Looper的静态方法prepare()方法即可，prepare方法保证每个线程最多只有一个Looper对象。然后调用Looper的静态loop方法启动它，loop使用一个死循环不断取出MessageQueue中的消息，并将取出的消息分给该消息对应的Handler进行处理。

``Handler``把消息发送给Looper管理的MessageQueue，并负责处理Looper分给它的消息。
在线程中使用Handler的步骤如下：
- 调用Looper的prepare方法为当前线程创建Looper对象，创建Looper对象时，它的构造方法会创建与之对应的MessageQueue。
- 有了Looper之后，创建Handler子类的实例，重写handleMessage方法，该方法负责处理来自于其他线程的消息。
- 调用Looper的loop方法启动Looper。

为了解决新线程不能更新UI组件的问题，Android提供了如下几种解决方案：
+ 使用Handler实现线程之间的通信
+ ``Activity.runOnUiThread(Runnable)``
+ ``View.post(Runnable)``
+ ``View.postDelayed(Runnable, long)``

``AsyncTask<Params,Progress,Result>``是抽象类，参数泛型类型：
+ ``Params`` 启动任务执行时输入参数的类型
+ ``Progress`` 后台任务完成的进度值的类型
+ ``Result`` 后台执行任务完成后返回结果的类型
创建``AsyncTask``子类，指定三个泛型参数的类型，如果某个泛型参数不需要指定类型，可将它指定为void。
+ ``doInBackground(Params...)`` 后台线程将要完成的任务，可以在方法内调用``publishProgress(Progress... values)``方法更新任务的执行进度。
+ ``onProgressUpdate(Progress... values)`` ``doInBackground``方法中调用``publishProgress``方法更新任务的执行进度后，将会触发该方法。
+ ``onPreExecute`` 该方法将在执行后台耗时操作前被调用，通常用于完成一些初始化准备工作，比如在界面上显示进度条等。
+ ``onPostExecute`` 当``doInBackground``完成后，系统会自动调用``onPostExecute``，并将``doInBackground``方法的返回值传给该方法。
+ 调用``AsyncTask``子类的实例的``execute``方法开始执行耗时操作。

使用``AsyncTask``需要遵循以下准则
+ 必须在UI线程中创建``AsyncTask``实例
+ 必须在UI线程中调用``AsyncTask``的``execute``方法
+ ``AsyncTask``的``onPreExecute``，``onPostExecute``，``doInBackground``，``onProgressUpdate``方法，由Android系统调用。
+ 每个``AsyncTask``只能被执行一次，多次调用将会引发异常。