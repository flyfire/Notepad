AndroidDevTools
===================
[Android Develop Tools](https://developer.android.com/tools/index.html)

# Developer Tools

# Workflow
[Android Development Workflow](https://developer.android.com/images/developing/developing_overview.png)

+ ``android list targets``
+ ``android create avd -n <name> -t <targetID> [-<option> <value>] ... ``
+ ``android create avd -n <name> -t <targetID> --skin WVGA800`` ``emulator -avd WVGA800 -scale 96dpi -dpi-device 160`` ``android create avd -n my_android1.5 -t 2 -p path/to/my/avd``
+ ``android move avd -n <name> [-<option> <value>] ...`` moving an AVD
+ If, for any reason, the platform/add-on root folder has its name changed (maybe because the user has installed an update of the platform/add-on) then the AVD will not be able to load the system image that it is mapped to. In this case, the android list targets command will produce this output:

>The following Android Virtual Devices could not be loaded:<br>
>Name: foo<br>
>Path: <path>/.android/avd/foo.avd<br>
>Error: Invalid value in image.sysdir. Run 'android update avd -n foo'<br>

To fix this error, use the ``android update avd`` command to recompute the path to the system images.
+ ``android delete avd -n <name> ``

+ By default, the emulator loads the SD card image that is stored with the active AVD (see the ``-avd`` startup option).Alternatively, you can start the emulator with the ``-sdcard`` flag and specify the name and path of your image (relative to the current working directory):``emulator -sdcard <filepath>``.emulator默认使用AVD中的SD卡，可以通过``-sdcard``选项来为emulator指定sd卡。
+ The emulator uses mountable disk images stored on your development machine to simulate flash (or similar) partitions on an actual device. For example, it uses a disk image containing an emulator-specific kernel, the Android system, a ramdisk image, and writeable images for user data and simulated SD card.To run properly, the emulator requires access to a specific set of disk image files. By default, the Emulator always looks for the disk images in the private storage area of the AVD in use. If no images exist there when the Emulator is launched, it creates the images in the AVD directory based on default versions stored in the SDK.emulator使用mountable disk images来模拟真实设备上的分区，为了能正常运行，emulator在启动时会查找那些disk images，如果在``~/.android/avd``中没有找到，就会在AVD所在文件夹依据SDK默认版本创建images。
+ The emulator uses three types of image files: default image files, runtime image files, and temporary image files. The sections below describe how to override the location/name of each type of file.
  + Default image files:When the emulator launches, but does not find an existing user data image in the active AVD's storage area, it creates a new one from a default version included in the SDK. The default user data image is read-only. The image files are read-only.Use emulator startup option ``-system <dir>``override the location where the emulator looks for the default user data image,use ``-initdata <file>`` to override the initial user-data disk image name which is ``userdata.img``.
  + Runtime images: user data and SD card:At runtime, the emulator reads and writes data to two disk images: a user-data image and (optionally) an SD card image. These images emulate the user-data partition and removable storage media on actual device.The emulator provides a default user-data disk image. At startup, the emulator creates the default image as a copy of the system user-data image (``user-data.img``), described above. The emulator stores the new image with the files of the active AVD.The emulator provides startup options to let you override the actual names and storage locations of the runtime images to load, as described in the following table. When you use one of these options, the emulator looks for the specified file(s) in the current working directory, in the AVD directory, or in a custom location (if you specified a path with the filename).``userdata-qemu.img`` is an image to which the emulator writes runtime user-data for a unique user.Override using ``-data <filepath>``,If the file at ``<filepath>`` does not exist, the emulator creates an image from the default ``userdata.img``, stores it under the name you specified, and persists user data to it at shutdown.``sdcard.img`` is an image representing an SD card inserted into the emulated device.Override using ``-sdcard <filepath>``.
    + Each emulator instance uses a writeable user-data image to store user- and session-specific data. For example, it uses the image to store a unique user's installed application data, settings, databases, and files.每个模拟器都会使用一个可写的user-data image来保存user或者session特定的数据。模拟器启动的时候回去查找user data image，如果找到了就mount为系统可读写的，让系统来读写用户数据。
    + If it does not find one, it creates an image by copying the system user-data image (``userdata.img``), described above. At device power-off, the system persists the user data to the image, so that it will be available in the next session. Note that the emulator stores the new disk image at the location/name that you specify in ``-data`` startup option.如果emulator在启动的时候没有找到user data image，就会使用默认的``userdata.img``来创建一个user data image，设备关机时，系统会保留用户信息到这个image。
  + Temporary Images,The emulator creates two writeable images at startup that it deletes at device power-off. The images are:A writable copy of the Android system image,The ``/cache`` partition image.The emulator does not permit renaming the temporary system image or persisting it at device power-off.The ``/cache`` partition image is initially empty, and is used by the browser to cache downloaded web pages and images. The emulator provides an ``-cache <file>``, which specifies the name of the file in which to persist the ``/cache`` image at device power-off. If ``<file>`` does not exist, the emulator creates it as an empty file.模拟器在启动时会创建2个可写的image，它会在设备关闭的时候把这2个设备删除掉：一个可写的Android系统镜像的拷贝，一个``/cache``分区用来保存浏览器缓存下来的网页和图像。模拟器提供了一个``-cache <file>``选项来保留``/cache``镜像。You can also disable the use of the cache partition by specifying the ``-nocache`` option at startup.也可以通过模拟器启动时的``-nocache``选项来禁止cache。
+ Emulator Networking
Each instance of the emulator runs behind a virtual router/firewall service that isolates it from your development machine's network interfaces and settings and from the internet. An emulated device can not see your development machine or other emulator instances on the network. Instead, it sees only that it is connected through Ethernet to a router/firewall.每个模拟器都运行在一个虚拟的网络接口后面，彼此隔离，模拟器看不到开发宿主机，也看不到其他的模拟器。The virtual router for each instance manages the 10.0.2/24 network address space — all addresses managed by the router are in the form of 10.0.2.<xx>, where <xx> is a number. 每个虚拟忘了接口都会给每个虚拟机实例分配一个地址，``10.0.2/24``格式，Addresses within this space are pre-allocated by the emulator/router as follows:

<table border="1">
<tr><td>Network Address</td><td>Description</td></tr>
<tr><td>10.0.2.1</td><td>Router/gateway address</td></tr>
<tr><td>10.0.2.2</td><td>Special alias to your host loopback interface (i.e., 127.0.0.1 on your development machine)</td></tr>
<tr><td>10.0.2.3</td><td>First DNS server</td></tr>
<tr><td>10.0.2.4 / 10.0.2.5 / 10.0.2.6</td><td>Optional second, third and fourth DNS server (if any)</td></tr>
<tr><td>10.0.2.15</td><td>The emulated device's own network/ethernet interface</td></tr>
<tr><td>127.0.0.1</td><td>The emulated device's own loopback interface</td></tr>

Note that the same address assignments are used by all running emulator instances. That means that if you have two instances running concurrently on your machine, each will have its own router and, behind that, each will have an IP address of 10.0.2.15. The instances are isolated by a router and can not see each other on the same network.Also note that the address 127.0.0.1 on your development machine corresponds to the emulator's own loopback interface. If you want to access services running on your development machine's loopback interface (a.k.a. 127.0.0.1 on your machine), you should use the special address ``10.0.2.2`` instead.每个虚拟机实例都被隔离开来，虚拟机上的地址``10.0.2.2``指向宿主机上的``127.0.0.1``。

To communicate with an emulator instance behind its virtual router, you need to set up network redirection on the virtual router. Clients can then connect to a specified guest port on the router, while the router directs traffic to/from that port to the emulated device's host port.如果要和emulator通信，需要通过virtual router建立连接来设置network redirection。
+ 通过emulator console来设置network redirection，``telnet localhost 5554``，``redir add <protocol>:<host-port>:<guest-port>``。Sets up a redirection that handles all incoming TCP connections to your host (development) machine on ``127.0.0.1:5000`` and will pass them through to the emulated system's ``10.0.2.15:6000``:``redir add tcp:5000:6000``.可以通过``redir del``和``redir list``来删除或者列出network redirection。
+ 通过ADB来设置network redirection，只能通过杀死ADB server的方式来删除一个network redirection。

At startup, the emulator reads the list of DNS servers that your system is currently using. It then stores the IP addresses of up to four servers on this list and sets up aliases to them on the emulated addresses 10.0.2.3, 10.0.2.4, 10.0.2.5 and 10.0.2.6 as needed.emulator启动时会读取宿主机的DNS服务器设置，从中取出四个alias到虚拟地址``10.0.2.3``，``10.0.2.4``，``10.0.2.5``和``10.0.2.6``。On Linux and OS X, the emulator obtains the DNS server addresses by parsing the file ``/etc/resolv.conf``. On Windows, the emulator obtains the addresses by calling the ``GetNetworkParams()`` API. Note that this usually means that the emulator ignores the content of your "hosts" file (/etc/hosts on Linux/OS X, %WINDOWS%/system32/HOSTS on Windows).emulator会忽略掉宿主机的hosts文件。When starting the emulator at the command line, you can also use the ``-dns-server <serverList>`` option to manually specify the addresses of DNS servers to use, where ``<serverList>`` is a comma-separated list of server names or IP addresses. You might find this option useful if you encounter DNS resolution problems in the emulated network (for example, an "Unknown Host error" message that appears when using the web browser).可以在emulator启动时通过``-dns-server <serverList>``参数来指定DNS服务器。If your emulator must access the Internet through a proxy server, you can use the ``-http-proxy <proxy>`` option when starting the emulator.可以通过``-http-proxy <proxy>``参数来指定代理服务器。The ``-http-proxy`` option forces the emulator to use the specified HTTP/HTTPS proxy for all outgoing TCP connections. Redirection for UDP is not currently supported.``-http-proxy``参数强制使emulator来对出去的TCP连接使用指定的HTTP/HTTPS代理，不支持UDP。Alternatively, you can define the environment variable ``http_proxy`` to the value you want to use for ``<proxy>``. In this case, you do not need to specify a value for <proxy> in the ``-http-proxy`` command — the emulator checks the value of the ``http_proxy`` environment variable at startup and uses its value automatically, if defined.You can use the ``-verbose-proxy`` option to diagnose proxy connection problems.可以定义``http_proxy``环境变量，可以使用``-verbose-proxy``来诊断代理连接问题。

+ ``assets/``,This is empty. You can use it to store raw asset files. Files that you save here are compiled into an ``.apk`` file as-is, and the original filename is preserved. You can navigate this directory in the same way as a typical file system using URIs and read files as a stream of bytes using the ``AssetManager``. For example, this is a good location for textures and game data.``raw/``,For arbitrary raw asset files. Saving asset files here instead of in the ``assets/`` directory only differs in the way that you access them. These files are processed by ``aapt`` and must be referenced from the application using a resource identifier in the ``R`` class. For example, this is a good place for media, such as MP3 or Ogg files.在``assets/``文件夹中的文件文件名会被保存，可以通过``AssetManager``像读取字节流一样读取这些文件。在``raw/``文件夹中的文件，会被``aapt``工具处理，只能通过``R``访问。


# Support Library

# Tools Help
The most important SDK tools include the Android SDK Manager (android sdk), the AVD Manager (android avd) the emulator (emulator), and the Dalvik Debug Monitor Server (ddms).



# Revisions

# ADK