ADB
==========
+ ADB是一个通用的命令行工具来供你和Android模拟器或者Android设备交互。ADB是Client/Server架构，包括3个部分。
  + Client，运行在开发机上。可以在shell中运行adb命令来调用它。
  + Server，运行在开发机后台，Server管理Client和运行在模拟器或者Android设备上的adb daemon的通信。
  + Daemon，运行在Android模拟器或者Android设备上的后台程序。
+ Syntax
	+ Target
		+ ``adb [-d|-e|-s <serialNumber>] <command>`` 
			+ ``-d`` Direct an adb command to the only attached USB device.
			+ ``-e`` Direct an adb command to the only running emulator instance.
	+ General
		+ ``adb devices``
	+ Debug
		+ ``adb logcat [option] [filter-specs]`` Prints log data to the screen.
		+ ``adb bugreport`` Prints ``dumpsys``, ``dumpstate``, and ``logcat`` data to the screen, for the purposes of bug reporting. 
		+ ``adb jdwp`` Prints a list of available JDWP processes on a given device.You can use the forward ``jdwp:<pid>`` port-forwarding specification to connect to a specific JDWP process. For example:``adb forward tcp:8000 jdwp:472``,``jdb -attach localhost:8000``
+ When you start an adb client, the client first checks whether there is an adb server process already running. If there isn't, it starts the server process. When the server starts, it binds to local TCP port 5037 and listens for commands sent from adb clients—all adb clients use port 5037 to communicate with the adb server.启动一个adb client时，client首先检查是否有adb server在运行，如果没有就起adb server进程。server进程起来的时候会绑定到5037端口，监听所有从adb client发送过来的命令。The server then sets up connections to all running emulator/device instances. It locates emulator/device instances by scanning odd-numbered ports in the range 5555 to 5585, the range used by emulators/devices. Where the server finds an adb daemon, it sets up a connection to that port. Note that each emulator/device instance acquires a pair of sequential ports — an even-numbered port for console connections and an odd-numbered port for adb connections.adb server通过扫描5555到5585以内的奇数端口来定位Android模拟器和Android设备，然后和它们建立通信。如果adb server在端口中发现了adb daemon，他会建立和那个端口的连接。每个Android模拟器和Android设备需要一对连续的端口，一个偶数端口用来console connection，一个奇数端口用来adb connection。(server与client通信的端口是是5037,adb server会与emulator交互的，使用的端口有两个，一个是5554专门用于与Emulator实例的连接，那么数据可以从Emulator转发给IDE控制台了，另一个则是5555，专门与adb daemon连接为后面调试使用。http://www.cnblogs.com/carmanloneliness/archive/2013/04/16/3023299.html)
+ 
