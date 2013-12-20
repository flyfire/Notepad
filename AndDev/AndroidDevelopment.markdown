Android开发
================
#Learning Android

+ Destroyed状态的Activity就不再驻于内存了。当Activity Manager认为某个Activity已经不再需要的时候，就会销毁它，并把它视作Destroyed。在销毁之前，它会调用回调函数``onDestroy()``，允许开发者在这里执行一些清理工作。不过Destroyed状态之前不一定是Stopped状态，Paused状态的Activity也有被销毁的可能，因此最好将一些重要的工作(比如保存数据)置于``onPause()``而不是``onDestroy()``中。

+ 虽说Service运行于后台，但这并不是说它默认执行于另一个独立的线程中。如果一个Service需要执行一段耗时的操作(比如网络调用)，依然需要程序员将它放在单独的线程中处理。否则，你的用户界面将响应不灵。一言以蔽之，Service默认与Activity在同一个线程中执行，而这个线程也被称作UI线程。

+ Application Context即当前应用程序所在的进程以及其运行环境，它为不同的构件所共享，因此我们可以通过它实现在不同的构件中共享数据和资源。不管应用程序中首先启动的是哪个构件(Activity，Service还是其它)，都会首先初始化Application Context。从此它的生存周期也就与整个应用程序保持一致，而与Activity或者某构件无关。我们可以通过``Context.getApplicationContext()``或者``Activity.getApplication()``获得它的引用。留意Activity与Service都是Context的子类，因此也就继承了它所有的方法。

+ 日志拥有多个级别：``.d()``表示调试，``.e()``表示错误、``.w()``表示警告(warning)、``.i()``表示信息(info)。有趣的是，还有个``.wtf()``表示“不该发生的错误”，它的字面意思是“What a Terrible Failure”。Eclipse会按照不同的颜色显示不同级别的日志。

+ Android程序默认运行在单线程之下。单线程顺序执行所有的操作，这一操作完成之前，下一个操作绝不会执行。这一行为被称作“阻塞”(blocking)。程序若长时间无响应(一般是五秒)，Android系统会自动弹出一个对话框，询问是否将该程序所在的进程杀死。这个对话框就是Application Not Responding(应用程序无响应)，简称ANR。

+ 使用多线程的方法有很多，Java的``Thread``类是其中之一。使用``Thread``类，就是使用Java的原生特性。但这里有一点，那就是Java线程不可以直接访问其它线程的私有数据，比如UI线程中的控件。这样设计可以避免一些同步问题。比如UI线程若在执行中，就并不希望其它线程来扰乱自己的状态。为弥补这点不足，也作为Java标准的线程机制的补充，Android提供了一个``AsyncTask``类。既要避免长时间的阻塞，又要访问UI线程的私有数据，那该怎么办？使用Android内置的``AsyncTask``(异步任务)机制。要使用它，我们需要创建一个``AsyncTask``的子类，并覆盖``doInBackground()``与``onPostExecute()``两个方法。这两个方法里面分别对应后台执行的相关代码：任务进行时的操作，以及任务完成时的操作。

+  AsyncTasks should ideally be used for short operations (a few seconds at the most.) If you need to keep threads running for long periods of time, it is highly recommended you use the various APIs provided by the ``java.util.concurrent`` pacakge such as ``Executor``, ``ThreadPoolExecutor`` and ``FutureTask``.An asynchronous task is defined by 3 generic types, called ``Params``, ``Progress`` and ``Result``, and 4 steps, called ``onPreExecute``, ``doInBackground``, ``onProgressUpdate`` and ``onPostExecute``.AsyncTask must be subclassed to be used. The subclass will override at least one method (``doInBackground(Params...)``), and most often will override a second one (``onPostExecute(Result)``.)

+  There are a few threading rules that must be followed for ``AsyncTask`` class to work properly:
  + The ``AsyncTask`` class must be loaded on the UI thread. This is done automatically as of ``JELLY_BEAN``.
  + The task instance **must** be created on the UI thread.
  + ``execute(Params...)`` must be invoked on the UI thread.
  + Do not call ``onPreExecute()``, ``onPostExecute(Result)``, ``doInBackground(Params...)``, ``onProgressUpdate(Progress...)`` manually.
  + The task can be executed only once (an exception will be thrown if a second execution is attempted.)

+ 0到255之间的值，换成十六进制就是0到FF。表示颜色的话，一般还是十六进制用的多。格式是AARRGGBB，其中的每个字符都是0到F之间的数字。这一格式有一种简化形式，那就是ARGB，其中每个字符表示重复的两个字符。比如#3A9F与#33AA99FF相同，对应的Alpha为#33、红为#AA、绿为#99、蓝为#FF。可以留意这里的#号，它用来表示这是个十六进制数，与十进制做区别。

+ 应用程序的资源文件都在``/res/``目录之下，要在XML中引用它们，只需填上地址即可；而引用系统资源，则需要为地址加一个前缀``android:``，比如``@android:drawable/ic_menu_preferences``。在Java中引用的话，就用``android.R``替换掉原先的``R``。系统的资源文件都位于SDK中，与平台相关。比如你使用Android 9 (Gingerbread)，那它的实际地址就在``android-sdk/platforms/android-9/data/res/``。

+ 要在程序中访问选项数据的内容，就使用Android框架的``SharedPreference``类。这个类允许我们在程序的任何部分(比如``Activity``,``Service``,``BroadcastReceiver``,``ContentProvider``)中访问选项数据，这也正是``SharedPreference``这个名字的由来。

+ 每个程序都有自己唯一的``SharedPreferences``对象，可供当前上下文中所有的构件访问。我们可以通过``PreferenceManager.getDefaultSharedPreferences()``来获取它的引用。名字中的"Shared"可能会让人疑惑，它是指允许在当前程序的各部分间共享，而不能与其它程序共享。

+ Android设备主要有三个分区：系统分区：/system/，SDCard分区：/sdcard/，用户数据分区：/data/。系统分区用于存放整个Android操作系统。预装的应用程序、系统库、Android框架、Linux命令行工具等等，都存放在这里。系统分区是以只读模式挂载的，应用开发者针对它的发挥空间不大，在此我们不多做关注。在仿真器中，系统分区对应的映像文件是system.img，它与平台相关，位于``android-sdk/platforms/android-8/images``目录。SDCard分区是个通用的存储空间。你的程序只要拥有``WRITE_TO_EXTERNAL_STORAGE``权限，即可随意读写这个文件系统。这里最适合存放音乐、照片、视频等大文件。留意，Android自FroYo版本开始，Eclipse File中显示的挂载点从``/sdcard``改为了``/mnt/sdcard``。这是因为FroYo引入的新特性，允许在SDCard中存储并执行程序。对应用开发者而言，SDCard分区无疑十分有用。不过它的文件结构也相对松散一些，管理时需要注意。这个分区的镜像文件一般是``sdcard.img``，位于对应设备的AVD目录之下，也就是``~/.android/avd``之下的某个子目录。对真机而言，它就是一个SD卡。

+ 对开发者和用户来讲，用户数据分区才是最重要的。用户数据都储存在这里，下载的应用程序储存在这里，而且所有的应用程序数据也都储存在这里。用户安装的应用程序都储存在``/data/app``目录，而开发者关心的数据文件都储存在``/data/data``目录。在这个目录之下，每个应用程序对应一个单独的子目录，按照Java package的名字作为标识。从这里可以再次看出Java package在Android安全机制中的地位。Android框架提供了许多相关的辅助函数，允许应用程序访问文件系统，比如``getFilesDir()``。这个分区的镜像文件是``user-data.img``，位于对应设备的AVD目录之下。同前面一样，也是在``~/.android/avd/``之下的某个子目录。新建应用程序的时候，你需要为Java代码指定一个package，按约定，它的名字一般都是逆序的域名，比如``com.marakana.yamba``。应用安装之后，Android会为应用单独创建一个目录``/data/data/com.marakana.yamba/``。其中的内容就是应用程序的私有数据。``/data/data``之下的每个子目录都有单独的用户帐号，由Linux负责管理。也就是说，只有我们自己的应用程序才拥有访问这一目录的权限。数据既然无法读取，明文也就是安全的。

+ ``Service`` 与 ``Activity`` 一样，同为 Android 的基本构件。其不同在于``Service`` 只是应用程序在后台执行的一段代码，而不需要提供用户界面。``Service`` 独立于 ``Activity`` 执行，无需理会 ``Activity`` 的状态如何。同 ``Activity`` 一样，``Service`` 也有着一套精心设计的生存周期，开发者可以定义其状态转换时发生的行为。``Activity`` 的状态由 ``ActivityManager`` 控制，而 ``Service`` 的状态受 ``Intent`` 影响。``Service``分为``Bound Service``和``Unbound Service``两种。``Bound Service`` 提供了一组API，以允许其他应用通过 ``AIDL``(Android Interface Definition Language，Andorid接口描述语言) 与之进行交互。``Unbound Service``只有两种状态：执行( Started ) 或停止( Stopped 或 Destoyed )。而且它的生存周期与启动它的``Activity``无关。

+ 需要用到应用其它部分的功能了，怎么办？可以复制粘贴，但不提倡这样。正确的做法是把需要重用的代码分离出来，统一放到一个各部分都可以访问的地方。对 Android 而言，这个地方就是 ``Application`` 对象。``Application`` 对象中保存着程序各部分所共享的状态。只要程序中的任何部分在执行，这个对象都会被系统创建并维护。 大多数应用直接使用来自框架提供的基类 ``android.app.Application`` 。不过你也可以继承它，来添加自己的函数。

+ ``Service``有几个方法需要覆盖：``onCreate()`` 在 ``Service`` 初始化时调用。``onStartCommand()`` 在 ``Service`` 启动时调用。``onDestory()`` 在 ``Service`` 结束时调用。

+ 针对SQLite数据库的相关操作，Android提供了一套优雅的接口。要访问数据库，你需要一个辅助类来获取数据库的“连接”，或者在必要时创建数据库连接。这个类就是 Android框架中的``SQLiteOpenHelper``，它可以返回一个 ``SQLiteDatabase`` 对象。为什么不直接使用 SQL 呢？有三个主要的原因：首先从安全角度考虑，直接使用 SQL 语句很容易导致 SQL 注入攻击。 这是因为 SQL 语句中会包含用户的输入， 而用户的输入都是不可信任的，不加检查地构造 SQL 语句的话，很容易导致安全漏洞。其次从性能的角度，重复执行SQL语句非常耗时，因为每次执行都需要对其进行解析。最后，使用 DbHelper 有助于提高程序的健壮性，使得许多编程错误可以在编译时发现。若是使用SQL，这些错误一般得到运行时才能被发现。很遗憾，Android框架对SQL的``DDL``(Data Definition Language,数据定义语言)部分支持不多，缺少相应的封装。因此要创建表，我们只能通过``execSQL()``调用来运行``CREATE TABLE``之类的SQL语句。但这里不存在用户输入，也就没有安全问题；而且这些代码都很少执行，因此也不会对性能造成影响。查询得到的数据将按照``Cursor``(游标)的形式返回。通过``Cursor``，你可以读出得到的第一行数据并移向下一行，直到遍历完毕、返回空为止。也可以在数据集中自由移动，读取所得数据的任意一行。一般而言，SQL的相关操作都存在触发``SQLException``异常的可能，这是因为数据库不在我们代码的直接控制范围内。比如数据库的存储空间用完了，或者执行过程被意外中断等等，对我们程序来说都属于不可预知的错误。因此好的做法是，将数据库的有关操作统统放在``try/catch``中间，捕获``SQLException``异常。

+ ``SQLiteDatabase``返回``Cursor``，```startManagingCursor()``用于提示``Activity``自动管理``Cursor``的生命周期，使之同自己保持一致。“保持一致”的意思是，它能保证在``Activity``销毁时，同时释放掉``Cursor``关联的数据，这样有助于优化垃圾收集的性能。如果没有自动管理，那就只能在各个方法中添加代码，手工地管理``Cursor``了。关闭数据库的同时会销毁Cursor对象，因此我们在读取数据完毕之前不能关闭数据库，而Cursor对象的生存周期则需要使用者负责管理。针对这一情景，可以利用Activity的``startManagingCursor()``方法。对于 cursor 的当前记录，我们可以通过类型与列号来获取其中的值。由此，``cursor.getString(3)`` 返回一个字符串，表示消息的内容； ``cursor.getLong(1)`` 返回一个数值，表示消息创建时的时间戳。但是，把列号硬编码在代码中不是个好习惯，因为数据库的原型一旦有变化，我们就不得不手工调整相关的代码，而且可读性也不好。更好的方法是，使用列名——调用 ``cursor.getColumnIndex()``得到列号，然后再取其中的值。

+ 一个``ScrollView``足以应付几十条记录，但是数据库里要有上百上千条记录时怎么办？全部读取并输出当然是很低效的。更何况，用户不一定关心所有的数据。针对这一问题，Android提供了``Adapter``，从而允许开发者为一个``View``绑定一个数据源。典型的情景是，``View``即``ListView``，数据即``Cursor``或者数组(``Array``)，``Adapter``也就与之对应：``CursorAdapter``或者``ArrayAdapter``。

+ 要把它设为程序的“主界面”，我们需要为它注册到特定的``Intent``。通常情况是，用户点击启动你的程序，系统就会发送一个``Intent``。你必须有个``Activity``能“侦听”到这个``Intent``才行，因此Android提供了``IntentFilter``，使之可以过滤出各自感兴趣的``Intent``。在XML中，它通过``<intent-filter>``元素表示，其下至少应含有一个``<action>``元素，以表示我们感兴趣的``Intent``。

+ ``Broadcast Receiver``是Android中发布/订阅机制(又称为``Observer``模式)的一种实现。应用程序作为发布者，可以广播一系列的事件，而不管接收者是谁。``Receiver``作为订阅者，从发布者那里订阅事件，并过滤出自己感兴趣的事件。如果某事件符合过滤规则，那么订阅者就被激活，并得到通知。``Broadcast Receiver``是应用程序针对事件做出响应的相关机制，而事件就是广播发送的``Intent``。``Receiver``会在接到``Intent``时唤醒，并触发相关的代码，也就是``onReceive()``。

+ 要将``BootReceiver``注册到系统，我们需要在Manifest文件中添加它的声明，还需要定义它的``intent-filter``，用以过滤出自己关心的事件。

```xml
<receiver android:name=".BootReceiver">
  <intent-filter>
    <action android:name="android.intent.action.BOOT_COMPLETED" />
  </intent-filter>
</receiver>
```
+ 针对某特定事件，在声明了它的``intent-filter``之后，还需要赋予应用程序以相应的权限。在这里那就是``android.permission.RECEIVE_BOOT_COMPLETED``。如果不声明权限，那么在事件发生时应用程序就会得不到通知，也就无从执行响应的代码。不幸的是，程序根本无法察觉事件丢失的情况，也没有任何错误提示，出现bug的话将很难调试。

```xml
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
```

+ 在``Activity``或者``Service``中，我们可以直接调用``startActivity()``、``startService()``、``stopService()``等方法。这是因为``Activity``与``Service``都是``Context``的子类，它们自身就是一个``Context``对象。但``BroadcastReceiver``并不是``Context``的子类，因此发送``Intent``，仍需通过系统传递的``Context``对象。

+ 网络连接变化

```xml
<receiver android:name=".NetworkReceiver">
  <intent-filter>
    <action android:name="android.net.conn.CONNECTIVITY_CHANGE" />
  </intent-filter>
</receiver>
```
注册权限

```xml
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

+ 现在UpdaterService可以广播Intent，但我们不一定希望所有程序都可以收发这条广播。不然，其它程序只要知道Action事件的名字，即可伪造我们的广播，更可以随意监听我们程序的动态，从而导致一些不可预料的问题。为此，我们需要为 Yamba 定义自己的权限，对发布者与订阅者双方都做些限制，以修补这项安全漏洞。首先是给出权限的定义。解释它们是什么、如何使用、处于何种保护级别。添加两项自定义的权限，也为应用程序申请了对这两项权限。接下来，我们需要保证广播的发布者与订阅者分别能够符合自己的权限。将权限的名字作为``sendBroadcast()``调用的第二个参数。如果Receiver没有相应的权限，就不会收到这条广播。还需要检查``Receiver``得到的广播是否合法。为此，在注册Receiver时为它添加上相关的权限信息。在``onResume()``中注册``TimelineReceiver``时，添加它所需的权限信息。限制只对拥有这项权限的发送者进行响应。

+ ``Content Provider`` 提供了一个统一的存储接口，允许多个程序访问同一个数据源。在联系人的这个例子里，还有个应用程序在背后扮演着数据源的角色，提供一个 ``Content Provider ``作为接口，供其它程序读写数据。接口本身很简单：``insert()``、``update()``、``delete()``与``query()``，与数据库的接口类似。在 Android 内部就大量使用了 ``Content Provider``。除了联系人外，系统设置、书签也都是 ``Content Provider`` 。另外系统中所有的多媒体文件也都注册到了一个 ``Content Provider`` 里，它被称作 ``MediaStore``，通过它可以检索系统中所有的图片、音乐与视频。

+ 程序内部的不同对象可以通过变量名相互引用，因为它们共享着同一个地址空间。但是不同程序的地址空间是相互隔离的，要引用对方的对象，就需要某种额外的机制。Android对此的解决方案就是全局资源标识符(Uniform Resource Identifier, URI)，使用一个字符串来表示Content Provider以及其中资源的位置。每个URI由三或四个部分组成，如下：URI的各个部分

```
content://com.marakana.yamba.statusprovider/status/47
   A              B                           C    D
```

A部分，总是``content://``，固定不变。B部分，``con.marakana.yamba.provider``，称作"典据"(authority)。通常是类名，全部小写。这个典据必须与我们在Manifest文件中的声明相匹配。C部分，status，表示对应数据的类型。它可以由任意/分隔的单词组成。D部分，47，表示对应条目的ID，此项可选。如果忽略，则表示数据的整个集合。有时需要引用整个Content Provider，那就忽略D部分；有时只需要其中一项，那就保留D部分，指明对应的条目。另外我们这里只有一个表，因此C部分是可以省略的。

+ When you want to access data in a ``content provider``, you use the ``ContentResolver`` object in your application's Context to communicate with the provider as a client. The ``ContentResolver`` object communicates with the provider object, an instance of a class that implements ContentProvider. The provider object receives data requests from clients, performs the requested action, and returns the results.

+ 小部件所在的View位于另一个进程中，因此使用``RemoteViews``。``RemoteViews``是专门为小部件设计的某种共享内存机制。

+ 使用系统服务，就调用``getSystemService()``。它返回一个表示系统服务的``Manager``对象，随后凭它就可以访问系统服务了。系统服务大多都是发布/订阅的接口，使用起来大约就是准备一个回调方法，将你的程序注册到相应的系统服务，然后等它的通知。而在Java中的通行做法是，实现一个内含回调方法的侦听器(Listener)，并把它传递给系统服务。有一点需要注意，那就是访问系统服务可能会比较费电。比如访问GPS数据或者传感器操作，都会额外消耗设备的电能。为了节约电能，我们可以仅在界面激活时进行传感器操作，使不必要的操作减到最少。用Activity生命周期的说法就是，我们仅在Running状态中响应这些操作。进入Running状态之前必经``onResume()``，离开Running状态之后必经``onPause()``。因此要保证只在Running状态中使用系统服务，就在``onResume()``中注册到系统服务，并在``onPause()``中注销即可。在某些情景中，我们可能希望将Activity注册在``onStart()``与``onStop()``之间，甚至``onCreate()``与``onDestroy()``之间，好让它在整个生命周期里都在注册中。但在这里，我们并不希望在``onCreate()``中就开始使用系统服务，因为``onCreate()``时，Activity还未显示，在此注册到系统服务只会空耗电能。由此可以看出，对Activity的生命周期有所理解，对省电是肯定有帮助的。

+ 将一个``view``设置为全屏

```java
// Set full screen view
getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
    WindowManager.LayoutParams.FLAG_FULLSCREEN);
requestWindowFeature(Window.FEATURE_NO_TITLE);

setContentView(oneWidget);
```

+ 要注册``LocationListener``系统服务，必先获取相应的权限。GPS和网络是最常用的位置信息源，不过Android也预留了其它信息源的扩展接口——未来也可能有更好更精确的信息源出现。为此，Android将位置信息的权限分成了两级：精确位置(Fine Location)和近似位置(Coarse Location)。

+ ``IntentService``是``Service``的一个子类，也是通过``startService()``发出的``intent``激活。与一般``Service``的不同在于，它默认在一个独立的 **工人线程(Worker Thread)**中执行，因此不会阻塞UI线程。另一点，它一旦执行完毕，生命周期也就结束了。不过只要接到``startService()``发出的``Intent``，它就会新建另一个生命周期，从而实现重新执行。在此，我们可以利用``Alarm``服务实现定期执行。同一般的``Service``不同，我们不需要覆盖``onCreate()、onStartCommand()、onDestroy()及onBind()``，而是覆盖一个``onHandleIntent()``方法。网络操作的相关代码就放在这里。另外``IntentService``要求我们实现一个构造函数，这也是与一般``Service``不同的地方。简言之，除了创建一个独立线程使用普通的``Service``之外，我们可以使用``IntentService``在它自己的工人线程中实现同样的功能，而且更简单。到现在剩下的只是定期唤醒它，这就引出了``AlarmManager``——另一个系统服务。``Alarm服务``由``AlarmManager``控制，允许你在一定时间之后触发某操作。它可以是一次结束，也可以是周期重复，因此可以利用它来定时启动我们的``Service``。``Alarm``服务通知的事件都是以``Intent``形式发出，或者更准确一点，以``PendingIntent``的形式。``PendingIntent``是``Intent``与某操作的组合。一般是把它交给他人，由他人负责发送，并在未来执行那项操作。发送``Intent``的方法并不多，创建``PendingIntent``的方法也是如此——只有``PendingIntent``类中的几个静态方法，在创建的同时也将相关的操作绑定了。回忆一下，同为发送``Intent``，启动``Activity``靠``startActivity()``，启动``Service``靠``startService()``，发送广播靠``sendBroadcast()``，而创建对应于``startService()``操作的``PendingIntent``，则靠它的静态方法``getService()``。

+ 如果应用程序需要加载某个本地库，那就必须保证系统能够找到它。在Linux下，它们通常都是通过``LD_LIBRARY_PATH``中定义的路径找到。在Android中，这个环境变量中只包含一个路径，即``system/lib``。这里存在一个问题，那就是``/system``分区一般都是只读的，应用程序无法将自己的本地库安装到``/system/lib``。为解决这一问题，NDK使用了这样的机制，那就是将本地库打包进APK文件，当用户安装含有本地库的APK文件时，系统将在``/data/data/your_package/lib``创建一个目录，用来存放应用程序的本地库，而不允许其它程序访问。这样的打包机制简化了Android的安装过程，也避免了本地库的版本冲突。此外，这也大大地扩展了Android的功能。


#Pro Android 4

+ ``android.os``表示可以通过java访问的操作系统服务。一些重要的类包括``BatteryManager``、``Binder``、``FileObserver``、``Handler``、``Looper``、``PowerManager``。``Binder``支持进程间通信，``FileObserver``监视对文件的更改。``Handler``用于运行与消息线程有关的任务，``Looper``用于运行消息线程。

+ 开发期间创建的xml会使用AAPT(Android Asset Packaging Tool，Android 资产打包工具)编译为二进制文件。因此，当应用程序安装在设备上时，设备上的文件将存储为二进制形式。当在运行时需要某个文件时，将读取该文件的二进制形式，而不是将其转换为xml。

+ 如果希望将xml编译为二进制形式，还希望它们容易本地化，可以将这些xml文件放在``/res/xml``或``/res``目录下非``raw``文件夹下。如果将文件放在``/res/raw``目录下，它们将不会编译为二进制格式。但是，因为它们是资源，Android通过``R.java``生成一个ID。可以通过``R.raw.filename_withou_extension``来访问这些文件。必须使用明确基于流式传输的API(``InputStream/OutputStream``)来读取这些文件。音频和视频文件属于这一类别。AAPT中的资源编译器会编译除``raw``资源之外的所有资源，并将它们全部放到最终的apk文件中，apk文件中包含Android应用程序的代码和资源，相当于Java中的jar文件。``/assets/*.*/*.*``，Android支持``/assets``子目录下任意子目录中的任意文件，这些文件不是真正的资源，只是原始文件。与``/res``资源子目录不同，这个目录支持任意深度的目录。这些文件不会生成任何资源ID，必须使用以``/assets``开始（不包含它）的相对路径名。可以使用``AssetManager``类来访问这些文件。

+ ``android list avd``列出可用的avd。``emulator avdname``运行指定avdname的模拟器。

+ 常规``Intent``可用于启动活动或服务或调用广播接收程序。使用``Intent``调用不同类型组件的性质是不同的。为解决此情况，一个Android上下文提供了3种不同的方法，分别是``startActivity(intent)``，``startService(intent)``，``sendBroadcast(intent)``，有了这些变体，如果希望存储``Intent``供以后重用，在收到广播后，Android如何知道启动活动、启动服务还是启动广播接收程序？这就是我们必须在创建挂起的``Intent``时显式指定其用途的原因，它也解释了以下3个独立方法的含义：``PendingIntent.getActivity(context,requestCode,intent,flags)``，``PendingIntent.getService(context,requestCode,intent,flags)``，``PendingIntent.getBroadcast(context,requestCode,intent,flags)``。

+ A different strategy is needed for implicit intents. In the absence of a designated target, the Android system must find the best component (or components) to handle the intent — a single activity or service to perform the requested action or the set of broadcast receivers to respond to the broadcast announcement. It does so by comparing the **contents of the Intent object** to **intent filters**, structures associated with components that can potentially receive intents. Filters advertise the capabilities of a component and delimit(划定，界限) the intents it can handle. They open the component to the possibility of receiving implicit intents of the advertised type. If a component does not have any intent filters, it can receive only explicit intents. A component with filters can receive both explicit and implicit intents.Only three aspects of an Intent object are consulted when the object is tested against an intent filter:``action``，``data (both URI and data type)``，``category``。The extras and flags play no part in resolving which component receives an intent.

+ To inform the system which implicit intents they can handle, activities, services, and broadcast receivers can have one or more intent filters. Each filter describes a capability of the component, a set of intents that the component is willing to receive. It, in effect, filters in intents of a desired type, while filtering out unwanted intents — but only unwanted implicit intents (those that don't name a target class). An explicit intent is always delivered to its target, no matter what it contains; the filter is not consulted. But an implicit intent is delivered to a component only if it can pass through one of the component's filters.An intent filter is an instance of the ``IntentFilter ``class. However, since the Android system must know about the capabilities of a component before it can launch that component, intent filters are generally **not** set up in Java code, but in the application's manifest file (``AndroidManifest.xml``) as **<intent-filter> **elements. (The one exception would be filters for broadcast receivers that are registered dynamically by calling ``Context.registerReceiver()``; they are directly created as ``IntentFilter`` objects.)

+ A filter has fields that parallel the action, data, and category fields of an Intent object. **An implicit intent is tested against the filter in all three areas.** To be delivered to the component that owns the filter, **it must pass all three tests.** If it fails even one of them, the Android system won't deliver it to the component — at least not on the basis of that filter. However, since a component can have multiple intent filters, an intent that does not pass through one of a component's filters might make it through on another.
  + To pass this test, the action specified in the Intent object must match one of the actions listed in the filter. If the object or the filter does not specify an action, the results are as follows:

    + If the filter fails to list any actions, there is nothing for an intent to match, so all intents fail the test. No intents can get through the filter.
    + On the other hand, an Intent object that doesn't specify an action automatically passes the test — as long as the filter contains at least one action.
  + For an intent to pass the category test, **every category in the Intent object must match a category in the filter. **The filter can list additional categories, but it cannot omit any that are in the intent.
    + In principle, therefore, an Intent object with no categories should always pass this test, regardless of what's in the filter. That's mostly true. However, with one exception, Android treats all implicit intents passed to ``startActivity()`` as if they contained at least one category: ``android.intent.category.DEFAULT`` (the ``CATEGORY_DEFAULT`` constant). Therefore, activities that are willing to receive implicit intents must include ``android.intent.category.DEFAULT`` in their intent filters. (Filters with ``android.intent.action.MAIN`` and ``android.intent.category.LAUNCHER`` settings are the exception. They mark activities that begin new tasks and that are represented on the launcher screen. They can include ``android.intent.category.DEFAULT`` in the list of categories, **but don't need to.**)
  + Each ``<data>`` element can specify a URI and a data type (MIME media type). There are separate attributes — ``scheme``, ``host``, ``port``, and ``path`` — for each part of the URI:``scheme://host:port/path``。For example, in the following URI,``content://com.example.project:200/folder/subfolder/etc``，the scheme is "content", the host is "com.example.project", the port is "200", and the path is "folder/subfolder/etc". **The host and port together constitute the URI authority;** if a host is not specified, the port is ignored.Each of these attributes is optional, but they are not independent of each other: For an authority to be meaningful, a scheme must also be specified. For a path to be meaningful, both a scheme and an authority must be specified.**When the URI in an Intent object is compared to a URI specification in a filter, it's compared only to the parts of the URI actually mentioned in the filter. **For example, if a filter specifies only a scheme, all URIs with that scheme match the filter. If a filter specifies a scheme and an authority but no path, all URIs with the same scheme and authority match, regardless of their paths. If a filter specifies a scheme, an authority, and a path, only URIs with the same scheme, authority, and path match. However, a path specification in the filter can contain wildcards to require only a partial match of the path.The ``type`` attribute of a <data> element specifies the MIME type of the data. It's more common in filters than a URI. Both the Intent object and the filter can use a "*" wildcard for the subtype field — for example, "text/*" or "audio/*" — indicating any subtype matches.
If an intent can pass through the filters of more than one activity or service, the user may be asked which component to activate. An exception is raised if no target can be found.

#Developer.Android
##Training
+ ``android list target``
+ ``android create project --target 2 --name HelloAndroid --path E:\Workspace\Android\HelloAndroid --activity MainActivity --package org.solarex.helloandroid``
+ 安装手机驱动，打开USB调试
+ ``android update project --target 2 --path E:\Workspace\Android\HelloAndroid``
+ ``ant debug``
+ ``adb install bin\HelloAndroid.apk``
+ ``@+id/edit_message``The plus sign (+) before the resource type is needed only when you're defining a resource ID for the first time. When you compile the app, the SDK tools use the ID name to create a new resource ID in your project's ``gen/R.java`` file that refers to the ``EditText`` element. Once the resource ID is declared once this way, other references to the ID do not need the plus sign.``@string/edit_message``refers to a concrete resource (not just an identifier), it does not need the plus sign.
+ In Eclipse, press Ctrl + Shift + O to import missing classes (Cmd + Shift + O on Mac).
+ If you are including the v4 support and v7 appcompat libraries in your application, you should specify a minimum SDK version of "7" (and not "4"). The highest support library level you include in your application determines the lowest API version in which it can operate.

##Guide
###ContentProvider
+ A provider isn't required to have a primary key, and it isn't required to use ``_ID`` as the column name of a primary key if one is present. However, if you want to bind data from a provider to a ``ListView``, one of the column names has to be ``_ID``. 如果一个provider要绑定到``ListView``，数据库列表中必须有一列为``_ID``。
+ ``query(Uri,projection,selection,selectionArgs,sortOrder)``，``SELECT projection FROM Uri WHERE selection SORT BY sortOrder``。
+ A content URI is a URI that identifies data in a provider. Content URIs include the symbolic name of the entire provider (its ``authority``) and a name that points to a table (a path).In the preceding lines of code, the constant ``CONTENT_URI`` contains the content URI of the user dictionary's "words" table. The ``ContentResolver`` object parses out the URI's ``authority``, and uses it to "resolve" the provider by comparing the authority to a system table of known providers. The ContentResolver can then dispatch the query arguments to the correct provider.``ContentResolver``解析URI的``authority``，然后用它去和系统中已知的provider表对照找出相应的provider。The ContentProvider uses the path part of the content URI to choose the table to access. A provider usually has a path for each table it exposes.In the previous lines of code, the full URI for the "words" table is:``content://user_dictionary/words``.where the user_dictionary string is the provider's authority, and words string is the table's path. The string content:// (the scheme) is always present, and identifies this as a content URI.``content://``固定，``user_dictionary``是provider的``authority``，后面的``words``是数据库中表的路径。Many providers allow you to access a single row in a table by appending an ID value to the end of the URI. For example, to retrieve a row whose _ID is 4 from user dictionary, you can use this content URI:``Uri singleUri = ContentUris.withAppendedId(UserDictionary.Words.CONTENT_URI,4);``,You often use id values when you've retrieved a set of rows and then want to update or delete one of them.可以指定``_ID``来访问数据库中的一行。The ``Uri`` and ``Uri.Builder`` classes contain convenience methods for constructing well-formed Uri objects from strings. The ``ContentUris`` contains convenience methods for appending id values to a URI.``Uri``和``Uri.Builder``包含从字符串构建Uri对象的方法。``ContentUris``包含往URI添加id字段的方法。
+ To retrieve data from a provider, follow these basic steps:
  + Request the read access permission for the provider.
  + Define the code that sends a query to the provider.
To retrieve data from a provider, your application needs "read access permission" for the provider. You can't request this permission at run-time; instead, you have to specify that you need this permission in your manifest, using the ``<uses-permission>`` element and the exact permission name defined by the provider. When you specify this element in your manifest, you are in effect "requesting" this permission for your application. When users install your application, they implicitly grant this request.要访问一个provider的数据，必须在``manifest``文件中声明provider规定的权限。
+ The expression that specifies the rows to retrieve is split into a selection clause and selection arguments. The selection clause is a combination of logical and Boolean expressions, column names, and values (the variable mSelectionClause). If you specify the replaceable parameter ? instead of a value, the query method retrieves the value from the selection arguments array (the variable mSelectionArgs).SQL语句中的``WHERE``字段被分解为``selectionClause``和``selectionArgs``。``selectionsClause``是一系列的判断语句(key ><= value,etc)，如果value部分被?替代，将使用``selectionArgs``数组中的值代替。

```java
String[] mSelectionArgs = {""};

// Gets a word from the UI
mSearchString = mSearchWord.getText().toString();

// Remember to insert code here to check for invalid or malicious input.

// If the word is the empty string, gets everything
if (TextUtils.isEmpty(mSearchString)) {
    // Setting the selection clause to null will return all words
    mSelectionClause = null;
    mSelectionArgs[0] = "";

} else {
    // Constructs a selection clause that matches the word that the user entered.
    mSelectionClause = UserDictionary.Words.WORD + " = ?";

    // Moves the user's input string to the selection arguments.
    mSelectionArgs[0] = mSearchString;

}
```
避免SQL注入，不要使用
```java
// Constructs a selection clause by concatenating the user's input to the column name
String mSelectionClause =  "var = " + mUserInput;
```
If you do this, you're allowing the user to concatenate malicious SQL onto your SQL statement. For example, the user could enter "nothing; DROP TABLE *;" for mUserInput, which would result in the selection clause var = nothing; DROP TABLE *;. Since the selection clause is treated as an SQL statement, this might cause the provider to erase all of the tables in the underlying SQLite database (unless the provider is set up to catch SQL injection attempts).
要使用
```java
// Constructs a selection clause with a replaceable parameter
String mSelectionClause =  "var = ?";
```
然后建立``selectionArgs``如``String[] selectionArgs = {""};``，把用户输入传入``selectionArgs``，``selectionArgs[0] = mUserInput;``。即使底层不是使用SQL数据库实现，这样也是最佳实践。

+ The ``ContentResolver.query()`` client method always returns a Cursor containing the columns specified by the query's projection for the rows that match the query's selection criteria.If no rows match the selection criteria, the provider returns a Cursor object for which ``Cursor.getCount()`` is 0 (an empty cursor).如果数据库表中没有行匹配查询条件，返回的``Cursor``调用方法``Cursor.getCount()``将返回0。If an internal error occurs, the results of the query depend on the particular provider. It may choose to return ``null``, or it may throw an ``Exception``.查询内部出错，``Cursor``的返回值取决于特定的``ContentProvider``，或者返回``null``，或者触发异常。Since a Cursor is a "list" of rows, a good way to display the contents of a Cursor is to link it to a ``ListView`` via a ``SimpleCursorAdapter``.

+ To back a ListView with a Cursor, the cursor must contain a column named _ID. Because of this, the query shown previously retrieves the _ID column for the "words" table, even though the ListView doesn't display it. This restriction also explains why most providers have a _ID column for each of their tables.为了使用``ListView``呈现查询出来的数据，查询返回的``cursor``中必须包含一列``_ID``，所以查询的时候必须将``_ID``放入``projection``查询列中。这个限制也解释了为什么大多数``ContentProvider``在他们的数据表中有一列``_ID``。
+ 出了呈现数据，还可以做其他的事情。``int index = cursor.getColumnIndex(UserDictionary.Words.WORD); newWord = cursor.getString(index);``。``cursor``还有其他的``get``方法来获取``cursor``中每一行中数据不同的属性。
+ A provider's application can specify permissions that other applications must have in order to access the provider's data. These permissions ensure that the user knows what data an application will try to access. Based on the provider's requirements, other applications request the permissions they need in order to access the provider. End users see the requested permissions when they install the application.提供``ContentProvider``的应用程序制定一个权限，其他应用程序要访问这个``ContentProvider``必须有这个权限。其他应用程序要求的这个权限会在安装时显示给用户。If a provider's application doesn't specify any permissions, then other applications have no access to the provider's data. However, components in the provider's application always have full read and write access, regardless of the specified permissions.如果一个``ContentProvider``没有指定权限，除了提供``ContentProvider``本身的应用程序内部组件可以访问这个``ContentProvider``外，其他应用程序无权访问。To get the permissions needed to access a provider, an application requests them with a ``<uses-permission>`` element in its manifest file. When the Android Package Manager installs the application, a user must approve all of the permissions the application requests. If the user approves all of them, Package Manager continues the installation; if the user doesn't approve them, Package Manager aborts the installation.
+ In the same way that you retrieve data from a provider, you also use the interaction between a provider client and the provider's ContentProvider to modify data. You call a method of ``ContentResolver`` with arguments that are passed to the corresponding method of ``ContentProvider``. The provider and provider client automatically handle security and inter-process communication.调用``ContentResolver``相应的方法可以插入、修改、删除相应``ContentProvider``中的数据，和读取``ContentProvider``中相应数据相似。``ContentProvider``和``ContentResolver``自动处理了数据安全和进程间通讯问题。
+ The data for the new row goes into a single ``ContentValues`` object, which is similar in form to a one-row cursor. The columns in this object don't need to have the same data type, and if you don't want to specify a value at all, you can set a column to ``null`` using ``ContentValues.putNull()``.``ContentResolver``的``insert``方法会返回一个新的``Uri``，The content URI returned in newUri identifies the newly-added row, with the following format:``content://user_dictionary/words/<id_value>``.To get the value of ``_ID`` from the returned Uri, call ``ContentUris.parseId()``.
+ To update a row, you use a ``ContentValues`` object with the updated values just as you do with an insertion, and selection criteria just as you do with a query. The client method you use is ``ContentResolver.update()``. You only need to add values to the ContentValues object for columns you're updating. If you want to clear the contents of a column, set the value to null.
+ Deleting rows is similar to retrieving row data: you specify selection criteria for the rows you want to delete and the client method returns the number of deleted rows. 删除数据和查询数据类似，设定要删除行的查询条件，返回删除的行数。
+ Content providers can offer many different data types. The User Dictionary Provider offers only text, but providers can also offer the following formats:``integer``,``long integer (long)``,``floating point``,``long floating point (double)``.Another data type that providers often use is ``Binary Large OBject (BLOB)`` implemented as a 64KB byte array. You can see the available data types by looking at the ``Cursor`` class "get" methods.
+ The data type for each column in a provider is usually listed in its documentation. You can also determine the data type by calling ``Cursor.getType()``.
+ Providers also maintain MIME data type information for each content URI they define. You can use the MIME type information to find out if your application can handle data that the provider offers, or to choose a type of handling based on the MIME type. You usually need the MIME type when you are working with a provider that contains complex data structures or files. For example, the ``ContactsContract.Data`` table in the Contacts Provider uses MIME types to label the type of contact data stored in each row. To get the MIME type corresponding to a content URI, call ``ContentResolver.getType()``.
+ Three alternative forms of provider access are important in application development:
  + Batch access: You can create a batch of access calls with methods in the ``ContentProviderOperation`` class, and then apply them with ``ContentResolver.applyBatch()``.
  + Asynchronous queries: You should do queries in a separate thread. One way to do this is to use a ``CursorLoader`` object. The examples in the ``Loaders`` guide demonstrate how to do this.
  + Data access via intents: Although you can't send an ``intent`` directly to a provider, you can send an intent to the provider's application, which is usually the best-equipped to modify the provider's data.
+ Batch access to a provider is useful for inserting a large number of rows, or for inserting rows in multiple tables in the same method call, or in general for performing a set of operations across process boundaries as a transaction (an atomic operation).To access a provider in "batch mode", you create an array of ``ContentProviderOperation`` objects and then dispatch them to a content provider with ``ContentResolver.applyBatch()``. You pass the content provider's ``authority`` to this method, rather than a particular content URI. This allows each ``ContentProviderOperation`` object in the array to work against a different table. A call to ContentResolver.applyBatch() returns an array of results.``Batch Access``通常用来插入大量的行数据，或者在同一个方法调用中插入到不同的数据库表或者是为了执行一系列跨进程原子操作。
+ Intents can provide indirect access to a content provider. You allow the user to access data in a provider even if your application doesn't have access permissions, either by getting a result intent back from an application that has permissions, or by activating an application that has permissions and letting the user do work in it.可以通过Intent间接调用ContentProvider，即使用户没有权限。用户可以向有权限的应用发送Intent然后解析应用发送回来的result intent，或者激活一个有权限的应用让用户使用它来干活(helper app)。
+ You can access data in a content provider, even if you don't have the proper access permissions, by sending an intent to an application that does have the permissions and receiving back a result intent containing "URI" permissions. These are permissions for a specific content URI that last until the activity that receives them is finished. The application that has permanent permissions grants temporary permissions by setting a flag in the result intent:``Read permission: FLAG_GRANT_READ_URI_PERMISSION``,``Write permission: FLAG_GRANT_WRITE_URI_PERMISSION``.向有永久权限的应用发送Intent，返回的result intent可以带有临时的权限(权限在接收result intent的activity finish的时候消失)。有永久权限的应用可以在result intent中设置flag来进行临时授权。These flags don't give general read or write access to the provider whose authority is contained in the content URI. The access is only for the URI itself.这些授权只针对返回的URI，不包括返回这个URI的provider。content URI的这个权限需要provider定义，A provider defines URI permissions for content URIs in its manifest, using the ``android:grantUriPermission`` attribute of the ``<provider>`` element, as well as the ``<grant-uri-permission>`` child element of the ``<provider>`` element.
+ A contract class defines ``constants`` that help applications work with the content URIs, column names, intent actions, and other features of a content provider. Contract classes are not included automatically with a provider; the provider's developer has to define them and then make them available to other developers. Many of the providers included with the Android platform have corresponding contract classes in the package android.provider.要为provider提供一个具体类，这个类定义了一些常量，这些常量是provider中的content URI，数据库表的列名，接受的intent actions和其他的东西。
+ Content providers can return standard MIME media types, or custom MIME type strings, or both.MIME types have the format:``type/subtype``，Custom MIME type strings, also called "vendor-specific" MIME types, have more complex type and subtype values. The type value is always ``vnd.android.cursor.dir`` for multiple rows, or ``vnd.android.cursor.item`` for a single row.The subtype is provider-specific.

+ 自己实现一个``ContentProvider``，设计数据时需要考虑的几点：数据库表要有主键(Table data should always have a "primary key" column that the provider maintains as a unique numeric value for each row. You can use this value to link the row to related rows in other tables (using it as a "foreign key"). Although you can use any name for this column, using BaseColumns._ID is the best choice, because linking the results of a provider query to a ListView requires one of the retrieved columns to have the name _ID.)，如果需要向外界提供图像等文件，把图像等保存在文件中然后间接提供给用户而不是直接保存在表中，如果保存在了表中，需要告知用户使用``ContentResolver``的``file``相关的方法来获取数据(If you want to provide bitmap images or other very large pieces of file-oriented data, store the data in a file and then provide it indirectly rather than storing it directly in a table. If you do this, you need to tell users of your provider that they need to use a ContentResolver file method to access the data.)，使用BLOB存储大小不定或者有多种结构的数据(Use the Binary Large OBject (BLOB) data type to store data that varies in size or has a varying structure. For example, you can use a BLOB column to store a protocol buffer or JSON structure.
You can also use a BLOB to implement a schema-independent table. In this type of table, you define a primary key column, a MIME type column, and one or more generic columns as BLOB. The meaning of the data in the BLOB columns is indicated by the value in the MIME type column. This allows you to store different row types in the same table. The Contacts Provider's "data" table ``ContactsContract.Data`` is an example of a schema-independent table.在schema-independent的表中，定义一个主键，一个MIME Type列，一列或多列BLOB)。

+ 要实现抽象类``ContentProvider``的具体子类，需要实现``insert(),delete(),update(),query(),getType(),onCreate()``六个方法。``onCreate``初始化provider，Android系统在创建provider的时候调用这个方法，provider直到一个``ContentResolver``try to access it的时候才会被创建。Your provider is not created until a ``ContentResolver``object tries to access it.除了``onCreate``，其他的方法必须是thread-safe的，不要再``oncreate``方法中做过多操作，除了要返回数据的方法，其他方法不一定必须要实现，比如为了阻止外部程序往一个表里面插入数据，可以忽略对``insert()``方法的调用直接返回0.
  + 实现``query()``方法，如果底层使用SQLiteDatabase，可以直接返回``SQLiteDatabase``类的``query()``返回的东西。如果底层实现不使用SQLiteDatabase，使用``cursor``的一个具体子类(If you aren't using an SQLite database as your data storage, use one of the concrete subclasses of Cursor. For example, the ``MatrixCursor`` class implements a cursor in which each row is an array of Object. With this class, use ``addRow()`` to add a new row.)，Android跨进程可以处理下列异常：``IllegalArgumentException``和``NullPointerException``。
  + 实现``insert()``方法，方法要返回新插入数据行的content URI，可以把插入行的``_ID``添加到数据库表的URI后面来构建这个URI。``ContentUris.withAppendedId()``。
  + 实现``delete()``方法，The ``delete()`` method does not have to physically delete rows from your data storage. If you are using a sync adapter with your provider, you should consider marking a deleted row with a "delete" flag rather than removing the row entirely. The sync adapter can check for deleted rows and remove them from the server before deleting them from the provider.
  + 实现``delete()``方法，重用。The update() method takes the same ContentValues argument used by insert(), and the same selection and selectionArgs arguments used by delete() and ContentProvider.query(). This may allow you to re-use code between these methods.
  + 实现``onCreate()``方法，不要做过多操作，过多操作不仅会减慢provider的启动，还会减慢对其他应用程序的响应。
  + 实现``getType()``，``getType()``必须实现，``getStreamTypes()``A method that you're expected to implement if your provider offers files.
  + MIME Types，标准类型列表：http://www.iana.org/assignments/media-types/index.htm。The ``getType()``method returns a String in MIME format that describes the type of data returned by the content URI argument. The Uri argument can be a pattern rather than a specific URI; in this case, you should return the type of data associated with content URIs that match the pattern.

  For content URIs that point to a row or rows of table data, ``getType()`` should return a MIME type in Android's vendor-specific MIME format:

```java
Type part: vnd
Subtype part:
If the URI pattern is for a single row: android.cursor.item/
If the URI pattern is for more than one row: android.cursor.dir/
Provider-specific part: vnd.<name>.<type>
```
You supply the ``<name>`` and ``<type>``. The ``<name>`` value should be globally unique, and the ``<type>`` value should be unique to the corresponding URI pattern. A good choice for ``<name>`` is your company's name or some part of your application's Android package name. A good choice for the ``<type>`` is a string that identifies the table associated with the URI.
    + MIME Types for files。If your provider offers files, implement ``getStreamTypes()``. The method returns a String array of MIME types for the files your provider can return for a given content URI. You should filter the MIME types you offer by the MIME type filter argument, so that you return only those MIME types that the client wants to handle.
    + Implementing a Contract Class.A contract class is a ``public final`` class that contains constant definitions for the URIs, column names, MIME types, and other meta-data that pertain to the provider. The class establishes a contract between the provider and other applications by ensuring that the provider can be correctly accessed even if there are changes to the actual values of URIs, column names, and so forth.
    + 完成Permissions(越往下优先级越高).
      + Single read-write provider-level permission:One permission that controls both read and write access to the entire provider, specified with the ``android:permission`` attribute of the ``<provider>`` element.
      + Separate read and write provider-level permission:A read permission and a write permission for the entire provider. You specify them with the ``android:readPermission`` and ``android:writePermission`` attributes of the ``<provider>`` element. They take precedence over the permission required by android:permission.
      + Path-level permission:Read, write, or read/write permission for a content URI in your provider. You specify each URI you want to control with a ``<path-permission>`` child element of the ``<provider>`` element. For each content URI you specify, you can specify a read/write permission, a read permission, or a write permission, or all three. The read and write permissions take precedence over the read/write permission. Also, path-level permission takes precedence over provider-level permissions.
      + Temporary permission:A permission level that grants temporary access to an application, even if the application doesn't have the permissions that are normally required. The temporary access feature reduces the number of permissions an application has to request in its manifest. When you turn on temporary permissions, the only applications that need "permanent" permissions for your provider are ones that continually access all your data. To turn on temporary permissions, either set the ``android:grantUriPermissions`` attribute of the ``<provider> ``element, or add one or more ``<grant-uri-permission>`` child elements to your ``<provider>`` element. If you use temporary permissions, you have to call ``Context.revokeUriPermission()`` whenever you remove support for a content URI from your provider, and the content URI is associated with a temporary permission. The attribute's value determines how much of your provider is made accessible. If the attribute is set to true, then the system will grant temporary permission to your entire provider, overriding any other permissions that are required by your provider-level or path-level permissions.If this flag is set to false, then you must add ``<grant-uri-permission>`` child elements to your ``<provider>`` element. Each child element specifies the content URI or URIs for which temporary access is granted.To delegate temporary access to an application, an intent must contain the ``FLAG_GRANT_READ_URI_PERMISSION`` or the ``FLAG_GRANT_WRITE_URI_PERMISSION`` flags, or both. These are set with the ``setFlags()`` method.If the ``android:grantUriPermissions`` attribute is not present, it's assumed to be ``false``.

#Google.Android.SDK开发范例大全
##Chapter3.用户人机界面
+ ``TextView``中设置``android:autolink="all"``即可支持HTML Tag输出。
+ 在resource中定义好``drawable``，在java文件中使用

```java
Resources resources = getBaseContext().getResources();
Drawabel blue = resources.getDrawable(R.drawable.blue);
textview.setBackgroundDrawable(blue);
textview.setTextColor(Color.YELLOW);
```
+ Android中，确实有方法可以直接以``R.string.sth``来转换ID为String，不过，这样的数据类型转换是非常规不妥的，正确的做法是使用``Context.getString``方法来获取存放在global中的Resource ID.``CharSequence str = getString(R.string.str);``
+ 使用``DisplayMetrics``获取手机分辨率

```java
DisplayMetrics dm = new DisplayMetrics();
getWindowManager().getDefaultDisplay().getMetrics(dm);
String fbl = "手机分辨率是："+dm.widthPixels+"*"+dm.heightPixels;
```
+ 可以多个控件使用同一个``style``，像使用css一样。在``resources``中创建样式名，然后在控件中以``style=@style/sth``进行应用即可。

```xml
<style name="sth">
	<item name="android:textSize">18sp</item>
	<item name="android:textColor">#FFFFFF</item>
</style>
```
+ 当系统中添加Activity时，必须在``AndroidManifest.xml``里定义一个新的``Activity``。

```java
<activity
android:name="org.solarex.ex02activityjump.ActivityJumpedTo">
</activity>
```
+ 当程序中出现2个以上的Activity时，系统要决定主程序是哪一个。

```java
<activity
	android:name="org.solarex.ex02activityjump.LayoutJumpMain"
	android:label="@string/app_name" >
	<intent-filter>
		<action android:name="android.intent.action.MAIN" />
		<category android:name="android.intent.category.LAUNCHER" />
	</intent-filter>
</activity>
```
其中``<action android:name="android.intent.action.MAIN" />``表示程序启动时，先运行这个程序。如果``AndroidManifest.xml``中没有一个Activity设置此参数，程序不会被运行。

+ 在``Activity``之间传递数据，``intent.putExtras(bundle)``，``Bundle``可以传入一系列的``key/value``。如果需要单击模拟器的返回按钮，仍然返回到上一个Activity，不要写``OrigActivity.this.finish();``。``OrigActivity``重写``onActivityResult()``这个方法，令``OrigActivity``在返回收到result后，重新加载写回原来输入传出去的值。
+ 截图实现

```java
//判断SD卡是否可用
private boolean getAvailableSdcard(Context context)
{
    boolean sdCardExist = Environment.getExternalStorageState().equals(android.os.Environment.MEDIA_MOUNTED);
    Toast.makeText(context, sdCardExist?"SD卡存在":"SD卡不存在", Toast.LENGTH_SHORT).show();
    if(sdCardExist)
    {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();

        long sdCardSize = ( availableBlocks * blockSize ) / 1024; //KB值
        Toast.makeText(context, "SD卡剩余空间"+sdCardSize/1024+"MB", Toast.LENGTH_SHORT).show();

        if(sdCardSize > minSdcardSize)
        {
            Toast.makeText(context, "剩余空间充足", Toast.LENGTH_SHORT).show();
            return true;
        }
        else
        {
            Toast.makeText(context, "剩余空间不足", Toast.LENGTH_SHORT).show();
        }
    }
    return false;
}

//截图
private Bitmap takeScreencast(Activity activity)
{
    View view = activity.getWindow().getDecorView();
    view.setDrawingCacheEnabled(true);
    view.buildDrawingCache();
    Bitmap b1 = view.getDrawingCache();

    //获取状态栏的高度
    Rect frame = new Rect();
    view.getWindowVisibleDisplayFrame(frame);
    int statusBarHeight = frame.top;
    Toast.makeText(activity, "statusbar高度"+statusBarHeight, Toast.LENGTH_SHORT).show();
    //获得屏幕长和高
    int width = activity.getWindowManager().getDefaultDisplay().getWidth();
    int height = activity.getWindowManager().getDefaultDisplay().getHeight();

    //去除标题栏
    Bitmap bResult = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);

    view.destroyDrawingCache();

    return bResult;
}
//保存图片
private void savePic(Bitmap bitmap, String filePath, String fileName)
{
    File f = new File(filePath);
    if(!f.exists())
    {
        f.mkdir();
    }
    FileOutputStream fos = null;
    try
    {
        fos = new FileOutputStream(filePath + File.separator + fileName );
        if(null != fos)
        {
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.flush();
            fos.close();
        }
    }
    catch(FileNotFoundException e)
    {
        e.printStackTrace();
    }
    catch(IOException e)
    {
        e.printStackTrace();
    }
    catch(Exception e)
    {
        e.printStackTrace();
    }
}

//整体框架
private void takeScreenShot(Context context,Activity activity)
{
    if(getAvailableSdcard(context))
    {
        File path = Environment.getExternalStorageDirectory();
        Toast.makeText(activity, path.toString(), Toast.LENGTH_SHORT).show();
        String filePath = path.getPath() + "/solarex";
        EditText et = (EditText)findViewById(R.id.filename);
        String fileName = et.getText().toString() + ".png";
        savePic(takeScreencast(activity), filePath, fileName);
        Toast.makeText(context, "图片保存在" + filePath + File.separator + fileName, Toast.LENGTH_LONG).show();
    }
}
```
+ 创建对话框
```java
new AlertDialog.Builder(activity).setTitle(R.string.dialogTitle).setMessage(R.string.dialogMsg).setPositiveButton(R.string.str_ok, new DialogInterface.OnClickListener(){
    @Override
    public void onClick(DialogInterface arg0, int arg1)
    {
        // TODO Auto-generated method stub
        //在这里设计当对话框按钮被点击之后要运行的事件
    }

}).show();
```
+ ``TextView``使用不同的字体

```java
tv.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/YAHEI.CONSOLAS.TTF"));
```
``TextView``改变字体大小时，``tv.getTextSize()``得到的是the size (in pixels) of the default text size in this TextView，是以px为单位的。``tv.setTextSize(size)``中``size``是The scaled pixel size，是以sp为单位的。设置的时候可以统一单位，使用``tv.setTextSize(TypedValue.COMPLEX_UNIT_PX,size);``。

+ 弹出对话框

```java
new AlertDialog.Builder(this)
.setTitle(R.string.exit).setMessage(R.string.exit_msg)
.setNegativeButton(R.string.str_no, new DialogInterface.OnClickListener()
{

    @Override
    public void onClick(DialogInterface arg0, int arg1)
    {
        // TODO Auto-generated method stub

    }
}).setPositiveButton(R.string.str_ok, new DialogInterface.OnClickListener()
{

    @Override
    public void onClick(DialogInterface arg0, int arg1)
    {
        // TODO Auto-generated method stub
        finish();
    }
}).show();
```

+ 密码框显示密码

```java
if(ck.isChecked())
{
    et.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
}
else
{
    et.setTransformationMethod(PasswordTransformationMethod.getInstance());
}
```

+ ``Toast``显示后会在一定时间内消失，``Toast.LENGTH_LONG``提示时间较长，``Toast.LENGTH_SHORT``提示时间较短。

+ 两个进程间通信

```java
/* intent发起者 */
public class Tudou extends Activity
{
    private static int USER_DEFINE_RETURN_CODE = 1024;
    private static String TAG = "TUDOU";
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tudou);

        tv = (TextView)findViewById(R.id.tv);
        Button btn = (Button)findViewById(R.id.btn);
        btn.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View arg0)
            {
                // TODO Auto-generated method stub
                try
                {
                    Intent intent = new Intent();
                    Log.v(TAG, "发起通信前");
                    intent.setClassName("org.solarex.ex10digua", "org.solarex.ex10digua.Digua");
                    Log.v(TAG, "即将启动通信");
                    Bundle bundle = new Bundle();
                    bundle.putString("DIGUA", "我是土豆，请回答");
                    intent.putExtras(bundle);
                    startActivityForResult(intent,USER_DEFINE_RETURN_CODE);
                    Log.v(TAG, "已发起通信");
                }
                catch(Exception e)
                {
                    Log.v(TAG, "无法发起通信");
                    Toast.makeText(Tudou.this, "请先安装Digua应用程序，以便通信", Toast.LENGTH_LONG).show();
                    tv.setText("请先安装Digua应用程序，以便通信");
                }
            }

        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.tudou, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        // TODO Auto-generated method stub
        if(requestCode == USER_DEFINE_RETURN_CODE)
        {
            if(resultCode == RESULT_OK)
            {
                String responseFromDigua = data.getStringExtra("TUDOU");
//              Toast.makeText(this, responseFromDigua, Toast.LENGTH_LONG);
                tv.setText(responseFromDigua);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
```

```java
/* intent消息接收回应者*/
public class Digua extends Activity
{
    private static String TAG = "DIGUA";
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_digua);
        TextView tv = (TextView)findViewById(R.id.tv);
        Button btn = (Button)findViewById(R.id.btn);
        try
        {
            Log.v(TAG, "准备接收消息");
            Bundle receiveBundle = getIntent().getExtras();
            String requestInfo = receiveBundle.getString("DIGUA");
            tv.setText(requestInfo);
            Log.v(TAG, "接收消息完毕");
        }
        catch(Exception e)
        {
            Log.v(TAG, "尚未接收到消息");
            tv.setText("尚未接收到消息");
            btn.setEnabled(false);
        }

        btn.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View arg0)
            {
                // TODO Auto-generated method stub
                try
                {
                    Log.v(TAG, "准备回应消息");
                    Bundle postBundle = new Bundle();
                    postBundle.putString("TUDOU", "土豆，土豆，我是地瓜");
                    Intent intent = new Intent();
                    intent.putExtras(postBundle);
                    setResult(RESULT_OK, intent);
                    Log.v(TAG, "已发送回应消息");
                    finish();
                }
                catch(Exception e)
                {
                    Log.v(TAG, "发送回应消息不成功");
                    Toast.makeText(Digua.this, "发送回应失败", Toast.LENGTH_SHORT).show();
                }
            }

        });
//      try
//      {
//          Bundle receiveBundle = getIntent().getExtras();
//          String requestFromTudou = receiveBundle.getString("DIGUA");
//          Toast.makeText(this, "收到消息：" + requestFromTudou + "\n正在回应。。。",
//                          Toast.LENGTH_SHORT).show();
//          try
//          {
//              Bundle postBundle = new Bundle();
//              postBundle.putString("TUDOU", "土豆，土豆，我是地瓜");
//              Intent intent = new Intent();
//              intent.putExtras(postBundle);
//              setResult(RESULT_OK, intent);
//              Toast.makeText(this, "已发送回应", Toast.LENGTH_SHORT).show();
//              finish();
//          }
//          catch(Exception e)
//          {
//              Toast.makeText(this, "发送回应失败", Toast.LENGTH_SHORT).show();
//          }
//      }
//      catch (Exception e)
//      {
//          Toast.makeText(this, "尚未接收到消息", Toast.LENGTH_SHORT).show();
//      }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.digua, menu);
        return true;
    }

}
```

+ 在``AndroidManifest.xml``中设置``android:installLocation``为``preferExternal``可让应用程序默认安装到SD卡上，如果设置为``auto``，程序默认会安装到手机内置内存中。

+ ``RadioGroup``清除选择状态``mRadioGroup.clearCheck();``

+ 发送``android.intent.action.MANAGE_PACKAGE_STORAGE`` intent引导用户移动应用到SD卡。
