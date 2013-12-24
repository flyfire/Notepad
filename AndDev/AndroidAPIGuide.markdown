Android API Guides
==================
[API Guides](https://developer.android.com/guide/index.html)

#Introduction

##Application Fundamentals

Once installed on a device, each Android app lives in its own security sandbox:
+ The Android operating system is a multi-user Linux system in which each app is a different user.
+ By default, the system assigns each app a unique Linux user ID (the ID is used only by the system and is unknown to the app). The system sets permissions for all the files in an app so that only the user ID assigned to that app can access them.系统为每个应用分配一个User ID.
+ Each process has its own virtual machine (VM), so an app's code runs in isolation from other apps.
+ By default, every app runs in its own Linux process. Android starts the process when any of the app's components need to be executed, then shuts down the process when it's no longer needed or when the system must recover memory for other apps.每个应用运行在自己的进程中，应用的一个组件需要启动的时候，应用的进程就会启动。

However, there are ways for an app to share data with other apps and for an app to access system services:
+ It's possible to arrange for two apps to share the same Linux user ID, in which case they are able to access each other's files. To conserve system resources, apps with the same user ID can also arrange to run in the same Linux process and share the same VM (the apps must also be signed with the same certificate).可以为两个应用设置一个共享User ID，这样他们就可以访问彼此的文件。为了节省系统资源，可以让两个应用运行在一个进程中，共享同一个VM。前提是应用必须使用相同的签名。
+ An app can request permission to access device data such as the user's contacts, SMS messages, the mountable storage (SD card), camera, Bluetooth, and more. All app permissions must be granted by the user at install time.

When the system starts a component, it starts the process for that app (if it's not already running) and instantiates the classes needed for the component. For example, if your app starts the activity in the camera app that captures a photo, that activity runs in the process that belongs to the camera app, not in your app's process. Therefore, unlike apps on most other systems, Android apps don't have a single entry point (there's no main() function, for example).应用启动其他应用中的组件时，其他应用的进程会起来，所以Android 应用没有一个单独的进入点。

Because the system runs each app in a separate process with file permissions that restrict access to other apps, your app cannot directly activate a component from another app. The Android system, however, can. So, to activate a component in another app, you must deliver a message to the system that specifies your intent to start a particular component. The system then activates the component for you.由于权限限制，A应用不能直接启动B应用，要想启动B应用，A应用必须给系统发消息指定要启动的应用或者组件，系统就会激活那个组件或者应用。

Three of the four component types—activities, services, and broadcast receivers—are activated by an asynchronous message called an intent. Intents bind individual components to each other at runtime (you can think of them as the messengers that request an action from other components), whether the component belongs to your app or another.``Activity``，``Service``，``BroadcastReceiver``可以被``Intent``激活。``ContentProvider``是从别处调用``ContentResolver``时间接调用的。For activities and services, an intent defines the action to perform (for example, to "view" or "send" something) and may specify the URI of the data to act on (among other things that the component being started might need to know). 对``Activity``和``Service``来说，``Intent``指定要做的动作。For broadcast receivers, the intent simply defines the announcement being broadcast (for example, a broadcast to indicate the device battery is low includes only a known action string that indicates "battery is low").对``BroadcastReceiver``来说，``Intent``只是指定要广播的声明。The other component type, content provider, is not activated by intents. Rather, it is activated when targeted by a request from a ``ContentResolver``. The content resolver handles all direct transactions with the content provider so that the component that's performing transactions with the provider doesn't need to and instead calls methods on the ``ContentResolver`` object. This leaves a layer of abstraction between the content provider and the component requesting information (for security).``ContentProvider``是被``ContentResolver``请求激活的。``ContentResolver``是直接请求操作的组件和``ContentProvider``之间的一个抽象层。

There are separate methods for activating each type of component:

+ You can start an activity (or give it something new to do) by passing an Intent to ``startActivity()`` or ``startActivityForResult()`` (when you want the activity to return a result).
+ You can start a service (or give new instructions to an ongoing service) by passing an Intent to ``startService()``. Or you can bind to the service by passing an Intent to ``bindService()``.
+ You can initiate a broadcast by passing an Intent to methods like ``sendBroadcast()``, ``sendOrderedBroadcast()``, or ``sendStickyBroadcast()``.
You can perform a query to a content provider by calling ``query()`` on a ``ContentResolver``.

Before the Android system can start an app component, the system must know that the component exists by reading the app's ``AndroidManifest.xml`` file (the "manifest" file). Your app must declare all its components in this file, which must be at the root of the app project directory.

The manifest does a number of things in addition to declaring the app's components, such as:

+ Identify any user permissions the app requires, such as Internet access or read-access to the user's contacts.
+ Declare the minimum API Level required by the app, based on which APIs the app uses.
+ Declare hardware and software features used or required by the app, such as a camera, bluetooth services, or a multitouch screen.
+ API libraries the app needs to be linked against (other than the Android framework APIs), such as the Google Maps library.
+ And more

The way the system identifies the components that can respond to an intent is by comparing the intent received to the intent filters provided in the manifest file of other apps on the device.系统判断一个组件是否响应某个Intent的依据是比较发出的Intent与manifest文件中组件声明的intent filter。

There are a variety of devices powered by Android and not all of them provide the same features and capabilities. In order to prevent your app from being installed on devices that lack features needed by your app, it's important that you clearly define a profile for the types of devices your app supports by declaring device and software requirements in your manifest file. Most of these declarations are informational only and the system does not read them, but external services such as Google Play do read them in order to provide filtering for users when they search for apps from their device.


##Device Compatibility

Android execution environment are defined by the Android compatibility program and each device must pass the Compatibility Test Suite (CTS) in order to be considered compatible.Android设备必须通过CTS测试。

In order for you to manage your app’s availability based on device features, Android defines feature IDs for any hardware or software feature that may not be available on all devices. For instance, the feature ID for the compass sensor is ``FEATURE_SENSOR_COMPASS`` and the feature ID for app widgets is ``FEATURE_APP_WIDGETS``.Android为硬件feature和软件feature定义了feature id。

If necessary, you can prevent users from installing your app when their devices don't provide a given feature by declaring it with a ``<uses-feature>``element in your app's manifest file.

Google Play Store compares the features your app requires to the features available on each user's device to determine whether your app is compatible with each device. If the device does not provide all the features your app requires, the user cannot install your app.GooglePlayStore会对用户设备进行判断。

However, if your app's primary functionality does not require a device feature, you should set the required attribute to "false" and check for the device feature at runtime. If the app feature is not available on the current device, gracefully degrade the corresponding app feature. For example, you can query whether a feature is available by calling ``hasSystemFeature()`` like this:
```xml
<manifest ... >
    <uses-feature android:name="android.hardware.sensor.compass"
                  android:required="false" />
    ...
</manifest>
```

```java
PackageManager pm = getPackageManager();
if (!pm.hasSystemFeature(PackageManager.FEATURE_SENSOR_COMPASS)) {
    // This device does not have a compass, turn off the compass feature
    disableCompassFeature();
}
```

The ``targetSdkVersion`` attribute does not prevent your app from being installed on platform versions that are higher than the specified value, but it is important because it indicates to the system whether your app should inherit behavior changes in newer versions. If you don't update the ``targetSdkVersion`` to the latest version, the system assumes that your app requires some backward-compatibility behaviors when running on the latest version. For example, among the [behavior changes in Android 4.4](https://developer.android.com/about/versions/android-4.4.html#Behaviors), alarms created with the ``AlarmManager`` APIs are now inexact by default so the system can batch app alarms and preserve system power, but the system will retain the previous API behavior for your app if your target API level is lower than "19".``targetSdkVersion``小于当前设备sdk version的应用将会被保留老版本sdk特性。

However, if your app uses APIs added in a more recent platform version, but does not require them for its primary functionality, you should check the API level at runtime and gracefully degrade the corresponding features when the API level is too low. In this case, set the ``minSdkVersion`` to the lowest value possible for your app's primary functionality, then compare the current system's version, ``SDK_INT``, to one the codename constants in ``Build.VERSION_CODES`` that corresponds to the API level you want to check. For example:可以在运行时比较当前设备系统版本，来决定是否使用某些特性。

```java
if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
    // Running on something older than API level 11, so disable
    // the drag/drop features that use ClipboardManager APIs
    disableDragAndDrop();
}
```

Filtering for technical compatibility (such as required hardware components) is always based on information contained within your APK file. But filtering for non-technical reasons (such as geographic locale) is always handled in the Google Play developer console.技术方面的过滤在代码中完成，非技术方面的过滤，如限制某个国家的人不能下载该应用，可以在google play store完成。

##System Permissions

Android is a privilege-separated operating system, in which each application runs with a distinct system identity (Linux user ID and group ID). Parts of the system are also separated into distinct identities. Linux thereby isolates applications from each other and from the system.Additional finer-grained security features are provided through a "permission" mechanism that enforces restrictions on the specific operations that a particular process can perform, and per-URI permissions for granting ad hoc access to specific pieces of data.Linux本身的多用户机制已经使Android比较安全了，Android为一些特定的操作提供了``permission``机制，只有获取了``permission``才能进行相关敏感的操作，如获取网络等。

A central design point of the Android security architecture is that no application, by default, has permission to perform any operations that would adversely impact other applications, the operating system, or the user. This includes reading or writing the user's private data (such as contacts or emails), reading or writing another application's files, performing network access, keeping the device awake, and so on.Android安全架构的核心是应用默认没有权限去进行敏感的操作。Because each Android application operates in a process sandbox, applications must explicitly share resources and data. They do this by declaring the permissions they need for additional capabilities not provided by the basic sandbox. Applications statically declare the permissions they require, and the Android system prompts the user for consent at the time the application is installed. Android has no mechanism for granting permissions dynamically (at run-time) because it complicates the user experience to the detriment of security.应用要进行敏感操作，必须在manifest文件中声明需要的permission，由用户在安装的时候进行确认。

All APKs (.apk files) must be signed with a certificate whose private key is held by their developer.The purpose of certificates in Android is to distinguish application authors. This allows the system to grant or deny applications access to signature-level permissions and to grant or deny an application's request to be given the same Linux identity as another application.所有的APK文件必须被签名，签名的目的是分辨应用的作者。这样就为系统在为应用授权或拒绝``signature-level permission``或分配和其他应用相同User ID时提供了依据。

At install time, Android gives each package a distinct Linux user ID. The identity remains constant for the duration of the package's life on that device. On a different device, the same package may have a different UID; what matters is that each package has a distinct UID on a given device.应用在安装时会被分配一个User ID。Because security enforcement happens at the process level, the code of any two packages cannot normally run in the same process, since they need to run as different Linux users. You can use the ``sharedUserId`` attribute in the ``AndroidManifest.xml``'s manifest tag of each package to have them assigned the same user ID. By doing this, for purposes of security the two packages are then treated as being the same application, with the same user ID and file permissions. Note that in order to retain security, only two applications signed with the same signature (and requesting the same sharedUserId) will be given the same user ID.可以在应用的manifest文件中设置``sharedUserId``来为两个应用分配相同的UserID，也就有了相同的文件访问权限。为了保证安全，只有两个签名相同的应用才会被分配相同的UserID。

Any data stored by an application will be assigned that application's user ID, and not normally accessible to other packages. When creating a new file with ``getSharedPreferences(String, int)``, ``openFileOutput(String, int)``, or ``openOrCreateDatabase(String, int, SQLiteDatabase.CursorFactory)``, you can use the ``MODE_WORLD_READABLE`` and/or ``MODE_WORLD_WRITEABLE`` flags to allow any other package to read/write the file. When setting these flags, the file is still owned by your application, but its global read and/or write permissions have been set appropriately so any other application can see it.一个应用存储的任意数据都会被设置为拥有者为应用的UserID，这样其他的应用就没有了访问权限。当应用创建文件时，使用方法``getSharedPreferences(String, int)``，``openFileOutput(String, int)``，或者 ``openOrCreateDatabase(String, int, SQLiteDatabase.CursorFactory)``时可以使用``MODE_WORLD_READABLE`` and/or ``MODE_WORLD_WRITEABLE`` flags来允许其他的应用来读/写这个文件。设置了这样的flag之后，文件的拥有者依旧是创建这个文件的应用的UserId，但是其他应用已经有读/写权限，已经被设置了合适的权限来去获取这个文件。

At application install time, permissions requested by the application are granted to it by the package installer, based on checks against the signatures of the applications declaring those permissions and/or interaction with the user. No checks with the user are done while an application is running; the app is either granted a particular permission when installed, and can use that feature as desired, or the permission is not granted and any attempt to use the feature fails without prompting the user.































#App Components




#App Resources




#App Manifest





#User Interface





#Animation and Graphics





#Computation





#Media and Camera






#Location and Sensors







#Connectivity







#Text and Input







#Data Storage








#Administration










#Web Apps







#Best Practices













