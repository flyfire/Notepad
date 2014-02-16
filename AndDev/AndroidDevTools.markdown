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
    + 如果没有找到user data image，

# Support Library

# Tools Help
The most important SDK tools include the Android SDK Manager (android sdk), the AVD Manager (android avd) the emulator (emulator), and the Dalvik Debug Monitor Server (ddms).



# Revisions

# ADK