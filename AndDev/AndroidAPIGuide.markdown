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

At application install time, permissions requested by the application are granted to it by the package installer, based on checks against the signatures of the applications declaring those permissions and/or interaction with the user. No checks with the user are done while an application is running; the app is either granted a particular permission when installed, and can use that feature as desired, or the permission is not granted and any attempt to use the feature fails without prompting the user.应用请求的权限要么在安装时一次性由用户赋予，要么被用户拒绝而无法安装。

#App Components

##Intent and Intent Filters

Intents facilitate communication between components in several ways, there are three fundamental use-cases:
+ To start an activity:You can start a new instance of an Activity by passing an ``Intent`` to ``startActivity()``. The ``Intent`` describes the activity to start and carries any necessary data.If you want to receive a result from the activity when it finishes, call ``startActivityForResult()``. Your activity receives the result as a separate ``Intent`` object in your activity's ``onActivityResult()``callback.使用``startActivity()``或者``startActivityForResult()``启动``Activity``。
+ To start a service:You can start a service to perform a one-time operation (such as download a file) by passing an ``Intent`` to ``startService()``. The ``Intent`` describes the service to start and carries any necessary data.If the service is designed with a client-server interface, you can bind to the service from another component by passing an ``Intent`` to ``bindService()``.使用``startService()``或者``bindService()``来启动``Service``。
+ To deliver a broadcast:A broadcast is a message that any app can receive. The system delivers various broadcasts for system events, such as when the system boots up or the device starts charging. You can deliver a broadcast to other apps by passing an ``Intent`` to ``sendBroadcast()``, ``sendOrderedBroadcast()``, or ``sendStickyBroadcast()``.使用``sendBroadcast()``，``sendOrderedBroadcast()``或者``sendStickyBroadcast()``来发起广播。

Explicit intents specify the component to start by name (the fully-qualified class name). You'll typically use an explicit intent to start a component in your own app, because you know the class name of the activity or service you want to start.Implicit intents do not name a specific component, but instead declare a general action to perform, which allows a component from another app to handle it.显式Intent，隐式Intent。When you create an explicit intent to start an activity or service, the system immediately starts the app component specified in the Intent object.使用显式Intent来启动``Activity``或者``Service``时，系统会立即启动``Intent``指定的组件。When you create an implicit intent, the Android system finds the appropriate component to start by comparing the contents of the intent to the intent filters declared in the manifest file of other apps on the device. If the intent matches an intent filter, the system starts that component and delivers it the Intent object. If multiple intent filters are compatible, the system displays a dialog so the user can pick which app to use.使用隐式Intent时，Android系统会把这个Intent和系统中应用在manifest中声明的intent filter的内容相比较。若有一个相匹配，则启动这个组件，并把Intent传送给这个组件。若有多个匹配，系统会弹出对话框让用户选择使用哪个应用。An intent filter is an expression in an app's manifest file that specifies the type of intents that the component would like to receive. For instance, by declaring an intent filter for an activity, you make it possible for other apps to directly start your activity with a certain kind of intent. Likewise, if you do not declare any intent filters for an activity, then it can be started only with an explicit intent.manifest中的intent filter表明了组件想要接受的Intent类型。如果一个组件没有声明intent filter，则只能被显式Intent启动。启动``Service``时永远要使用显式Intent，否则不知道具体什么组件启动了Service。

The primary information contained in an Intent is the following:
+ ``Component name``:This field of the Intent is a ComponentName object, which you can specify using a fully qualified class name of the target component, including the package name of the app. For example, com.example.ExampleActivity. You can set the component name with setComponent(), setClass(), setClassName(), or with the Intent constructor.
+ ``Action``:A string that specifies the generic action to perform (such as view or pick).``ACTION_VIEW``:Use this action in an intent with ``startActivity()`` when you have some information that an activity can show to the user, such as a photo to view in a gallery app, or an address to view in a map app.
+ ``Data``:The URI (a Uri object) that references the data to be acted on and/or the MIME type of that data. The type of data supplied is generally dictated by the intent's action.To set only the data URI, call ``setData()``. To set only the MIME type, call ``setType()``. If necessary, you can set both explicitly with ``setDataAndType()``.
+ ``Category``:A string containing additional information about the kind of component that should handle the intent.``CATEGORY_BROWSABLE``,The target activity allows itself to be started by a web browser to display data referenced by a link—such as an image or an e-mail message.``CATEGORY_LAUNCHER``,The activity is the initial activity of a task and is listed in the system's application launcher.You can specify a category with ``addCategory()``.

These properties listed above (component name, action, data, and category) represent the defining characteristics of an intent. By reading these properties, the Android system is able to resolve which app component it should start.Android系统通过读取Intent的``Componnet name``,``action``,``data``和``category``信息，可以判断出哪个组件应该去启动。一个Intent也可以包含其他的信息，这些信息不影响它究竟是如何被解析的。However, an intent can carry additional information that does not affect how it is resolved to an app component. An intent can also supply:
+ ``Extras``:Key-value pairs that carry additional information required to accomplish the requested action.You can add extra data with various ``putExtra()`` methods, each accepting two parameters: the key name and the value. You can also create a ``Bundle`` object with all the extra data, then insert the ``Bundle`` in the Intent with ``putExtras()``.For example, when creating an intent to send an email with ``ACTION_SEND``, you can specify the "to" recipient with the ``EXTRA_EMAIL`` key, and specify the "subject" with the ``EXTRA_SUBJECT`` key.
+ ``Flags``:Flags defined in the Intent class that function as metadata for the intent. The flags may instruct the Android system how to launch an activity (for example, which task the activity should belong to) and how to treat it after it's launched (for example, whether it belongs in the list of recent activities).可以使用``setFlag()``方法来设置flag。

使用隐式Intent时，可以使用``resolveActivity``来确定Intent发起的Action有组件响应。 It's possible that a user won't have any apps that handle the implicit intent you send to ``startActivity()``. If that happens, the call will fail and your app will crash. To verify that an activity will receive the intent, call ``resolveActivity()`` on your Intent object. If the result is non-null, then there is at least one app that can handle the intent and it's safe to call ``startActivity()``. If the result is null, you should not use the intent and, if possible, you should disable the feature that issues the intent.

```java
// Create the text message with a string
Intent sendIntent = new Intent();
sendIntent.setAction(Intent.ACTION_SEND);
sendIntent.putExtra(Intent.EXTRA_TEXT, textMessage);
sendIntent.setType(HTTP.PLAIN_TEXT_TYPE); // "text/plain" MIME type

// Verify that the intent will resolve to an activity
if (sendIntent.resolveActivity(getPackageManager()) != null) {
    startActivity(sendIntent);
}
```

仅仅列出所有可以响应这个Intent Action的应用列表，不让用户选择默认应用。However, if multiple apps can respond to the intent and the user might want to use a different app each time, you should explicitly show a chooser dialog. The chooser dialog asks the user to select which app to use for the action every time (the user cannot select a default app for the action).To show the chooser, create an ``Intent`` using ``createChooser()`` and pass it to ``startActivity()``.

```java
Intent intent = new Intent(Intent.ACTION_SEND);
...

// Always use string resources for UI text.
// This says something like "Share this photo with"
String title = getResources().getString(R.string.chooser_title);
// Create intent to show chooser
Intent chooser = Intent.createChooser(intent, title);

// Verify the intent will resolve to at least one activity
if (sendIntent.resolveActivity(getPackageManager()) != null) {
    startActivity(sendIntent);
}
```

To advertise which implicit intents your app can receive, declare one or more intent filters for each of your app components with an ``<intent-filter>`` element in your manifest file. Each intent filter specifies the type of intents it accepts based on the intent's action, data, and category. The system will deliver an implicit intent to your app component only if the intent can pass through one of your intent filters.要让组件接收隐式Intent，需要在应用的manifest文件中组件的``intent-filter``中声明``intent-filter``。若有匹配，系统会把隐式Intent传递给响应组件。

Each intent filter is defined by an ``<intent-filter>`` element in the app's manifest file, nested in the corresponding app component (such as an ``<activity>`` element). Inside the ``<intent-filter>``, you can specify the type of intents to accept using one or more of these three elements:``<intent-filter>``中可以声明的元素：
+ ``<action>``:Declares the intent action accepted, in the name attribute. The value must be the literal string value of an action, not the class constant.
+ ``<data>``:Declares the type of data accepted, using one or more attributes that specify various aspects of the data URI (scheme, host, port, path, etc.) and MIME type.
+ ``<category>``:Declares the intent category accepted, in the name attribute. The value must be the literal string value of an action, not the class constant.**In order to receive implicit intents, you must include the ``CATEGORY_DEFAULT`` category in the intent filter. The methods ``startActivity()`` and ``startActivityForResult()`` treat all intents as if they declared the ``CATEGORY_DEFAULT`` category. If you do not declare this category in your intent filter, no implicit intents will resolve to your activity.**It's okay to create a filter that includes more than one instance of ``<action>, <data>, or <category>``. If you do, you simply need to be certain that the component can handle any and all combinations of those filter elements.一个组件可以包含多个``<action>,<data>,<category>``域，只要声明``<intent-filter>``的组件能处理这些就可以。

```xml
<activity android:name="ShareActivity">
    <intent-filter>
        <action android:name="android.intent.action.SEND"/>
        <category android:name="android.intent.category.DEFAULT"/>
        <data android:mimeType="text/plain"/>
    </intent-filter>
</activity>
```

To avoid inadvertently running a different app's Service, always use an explicit intent to start your own service and do not declare intent filters for your service.永远使用显式Intent来启动Service。

The ``ACTION_MAIN`` action indicates this is the main entry point and does not expect any intent data.The ``CATEGORY_LAUNCHER`` category indicates that this activity's icon should be placed in the system's app launcher. If the ``<activity>`` element does not specify an icon with icon, then the system uses the icon from the ``<application>`` element.These two must be paired together in order for the activity to appear in the app launcher.``ACTION_MAIN``和``CATEGORY_LAUNCHER``必须同时出现才能保证一个应用出现在桌面上。


A ``PendingIntent`` object is a wrapper around an ``Intent`` object. The primary purpose of a ``PendingIntent`` is to grant permission to a foreign application to use the contained Intent as if it were executed from your app's own process.``PendingIntent``常用于给外界应用授权。Major use cases for a pending intent include:

+ Declare an intent to be executed when the user performs an action with your Notification (the Android system's NotificationManager executes the Intent).
+ Declare an intent to be executed when the user performs an action with your App Widget (the Home screen app executes the Intent).
+ Declare an intent to be executed at a specified time in the future (the Android system's AlarmManager executes the Intent).

Because each Intent object is designed to be handled by a specific type of app component (either an Activity, a Service, or a BroadcastReceiver), so too must a PendingIntent be created with the same consideration. 每个Intent都被设计用来处理特定类型的组件调用，PendingIntent也不例外，只不过调用方法有些区别。When using a pending intent, your app will not execute the intent with a call such as startActivity(). You must instead declare the intended component type when you create the PendingIntent by calling the respective creator method:
+ ``PendingIntent.getActivity()`` for an ``Intent`` that starts an ``Activity``.
+ ``PendingIntent.getService()`` for an ``Intent`` that starts a ``Service``.
+ ``PendingIntent.getBroadcast()`` for a ``Intent`` that starts an ``BroadcastReceiver``.
Unless your app is receiving pending intents from other apps, the above methods to create a ``PendingIntent`` are the only ``PendingIntent`` methods you'll probably ever need.

解析Intent时，如果有Intent中有一个匹配就pass，因此如果Intent中没有指定``action,data,category``，``<intent-filter>``中有相应的字段，会pass。For an intent to pass the category test, every category in the Intent must match a category in the filter. The reverse is not necessary—the intent filter may declare more categories than are specified in the Intent and the Intent will still pass. Therefore, an intent with no categories should always pass this test, regardless of what categories are declared in the filter.

Intents are matched against intent filters not only to discover a target component to activate, but also to discover something about the set of components on the device. For example, the Home app populates the app launcher by finding all the activities with intent filters that specify the ``ACTION_MAIN`` action and ``CATEGORY_LAUNCHER category``.可以根据``<intent-filter>``来对应用程序分类。Your application can use intent matching in a similar way. The ``PackageManager`` has a set of ``query...()`` methods that return all components that can accept a particular intent, and a similar series of ``resolve...()`` methods that determine the best component to respond to an intent. For example, ``queryIntentActivities()`` returns a list of all activities that can perform the intent passed as an argument, and ``queryIntentServices()`` returns a similar list of services. Neither method activates the components; they just list the ones that can respond. There's a similar method, ``queryBroadcastReceivers()``, for broadcast receivers.可以使用``PackageManager``相应方法来根据``Intent``来进行查询。

[https://developer.android.com/guide/components/intents-common.html](https://developer.android.com/guide/components/intents-common.html)列出了一些常用的Intent和Intent Filter。


## Activities

Each time a new activity starts, the previous activity is stopped, but the system preserves the activity in a stack (the "back stack"). When a new activity starts, it is pushed onto the back stack and takes user focus. The back stack abides to the basic "last in, first out" stack mechanism, so, when the user is done with the current activity and presses the Back button, it is popped from the stack (and destroyed) and the previous activity resumes.task栈。

When an activity is stopped because a new activity starts, it is notified of this change in state through the activity's lifecycle callback methods. There are several callback methods that an activity might receive, due to a change in its state—whether the system is creating it, stopping it, resuming it, or destroying it—and each callback provides you the opportunity to perform specific work that's appropriate to that state change.Activity状态发生变化时，会调用相应的回调函数。

Sometimes, you might want to receive a result from the activity that you start. In that case, start the activity by calling ``startActivityForResult()`` (instead of ``startActivity()``). To then receive the result from the subsequent activity, implement the ``onActivityResult()`` callback method. When the subsequent activity is done, it returns a result in an Intent to your ``onActivityResult()`` method.

```java
private void pickContact() {
    // Create an intent to "pick" a contact, as defined by the content provider URI
    Intent intent = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);
    startActivityForResult(intent, PICK_CONTACT_REQUEST);
}

@Override
protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    // If the request went well (OK) and the request was PICK_CONTACT_REQUEST
    if (resultCode == Activity.RESULT_OK && requestCode == PICK_CONTACT_REQUEST) {
        // Perform a query to the contact's content provider for the contact's name
        Cursor cursor = getContentResolver().query(data.getData(),
        new String[] {Contacts.DISPLAY_NAME}, null, null, null);
        if (cursor.moveToFirst()) { // True if the cursor is not empty
            int columnIndex = cursor.getColumnIndex(Contacts.DISPLAY_NAME);
            String name = cursor.getString(columnIndex);
            // Do something with the selected contact's name...
        }
    }
}
```

You can shut down an activity by calling its ``finish()`` method. You can also shut down a separate activity that you previously started by calling ``finishActivity()``.

An activity can exist in essentially three states:
+ Resumed:The activity is in the foreground of the screen and has user focus. (This state is also sometimes referred to as "running".)
+ Paused:Another activity is in the foreground and has focus, but this one is still visible. That is, another activity is visible on top of this one and that activity is partially transparent or doesn't cover the entire screen. A paused activity is completely alive (the Activity object is retained in memory, it maintains all state and member information, and remains attached to the window manager), but can be killed by the system in extremely low memory situations.
+ Stopped:The activity is completely obscured by another activity (the activity is now in the "background"). A stopped activity is also still alive (the Activity object is retained in memory, it maintains all state and member information, but is not attached to the window manager). However, it is no longer visible to the user and it can be killed by the system when memory is needed elsewhere.

If an activity is paused or stopped, the system can drop it from memory either by asking it to finish (calling its finish() method), or simply killing its process. When the activity is opened again (after being finished or killed), it must be created all over.当一个Activity处于paused或者stopped状态时，系统可以从内存中清除它通过调用它的finish方法或者直接杀死它的进程。

```java
public class ExampleActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // The activity is being created.
    }
    @Override
    protected void onStart() {
        super.onStart();
        // The activity is about to become visible.
    }
    @Override
    protected void onResume() {
        super.onResume();
        // The activity has become visible (it is now "resumed").
    }
    @Override
    protected void onPause() {
        super.onPause();
        // Another activity is taking focus (this activity is about to be "paused").
    }
    @Override
    protected void onStop() {
        super.onStop();
        // The activity is no longer visible (it is now "stopped")
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // The activity is about to be destroyed.
    }
}
```

Taken together, these methods define the entire lifecycle of an activity. By implementing these methods, you can monitor three nested loops in the activity lifecycle:
+ The entire lifetime of an activity happens between the call to ``onCreate()`` and the call to ``onDestroy()``. Your activity should perform setup of "global" state (such as defining layout) in ``onCreate()``, and release all remaining resources in ``onDestroy()``.Activity的整个生命周期在``onCreate``和``onDestory``之间。应该在``onCreate``中做些初始化工作，在``onDestory``中做销毁工作。
+ The **visible** lifetime of an activity happens between the call to ``onStart()`` and the call to ``onStop()``. During this time, the user can see the activity on-screen and interact with it.可以在``onStart``中注册广播，在``onStop``中解除注册。
+ The **foreground** lifetime of an activity happens between the call to ``onResume()`` and the call to ``onPause()``. During this time, the activity is in front of all other activities on screen and has user input focus. An activity can frequently transition in and out of the foreground—for example, ``onPause()`` is called when the device goes to sleep or when a dialog appears. Because this state can transition often, the code in these two methods should be fairly lightweight in order to avoid slow transitions that make the user wait.Activity状态切换会经常在``onPause``和``onResume``中发生，``onResume``和``onPause``中的代码应该尽量轻量级。

The system can kill the process hosting the activity at any time after the method(``onPause()``, ``onStop()``, and ``onDestroy()``) returns, without executing another line of the activity's code.在``onPause,onStop,onDestory``执行完返回后，系统可能会杀死它们。Because ``onPause()`` is the first of the three, once the activity is created, ``onPause()`` is the last method that's guaranteed to be called before the process can be killed—if the system must recover memory in an emergency, then ``onStop()`` and ``onDestroy()`` might not be called. Therefore, you should use ``onPause()`` to write crucial persistent data (such as user edits) to storage.应该在``onPause``中做一些重要的工作，比如存储数据之类的。

You can ensure that important information about the activity state is preserved by implementing an additional callback method that allows you to save information about the state of your activity: ``onSaveInstanceState()``.为了避免系统在资源紧张的情况下杀死Activity，可以使用``onSaveInstanceState``来保存现在的状态信息。The system calls ``onSaveInstanceState()`` before making the activity vulnerable to destruction. The system passes this method a ``Bundle`` in which you can save state information about the activity as name-value pairs, using methods such as ``putString()`` and ``putInt()``. Then, if the system kills your application process and the user navigates back to your activity, the system recreates the activity and passes the ``Bundle`` to both ``onCreate()`` and ``onRestoreInstanceState()``. Using either of these methods, you can extract your saved state from the ``Bundle`` and restore the activity state. If there is no state information to restore, then the ``Bundle`` passed to you is ``null`` (which is the case when the activity is created for the first time).系统会在Activity被销毁之前调用``onSaveInstanceState``，系统传递给这个方法一个``Bundle``来作为参数，可以使用``putString``或者``putInt``方法来保存需要保存的数据。当系统杀死了这个``Activity``，用户回到这个``Activity``时，系统会重新创建``Activity``并且会把这个``Bundle``对象传递给``onCreate``和``onRestoreInstanceState``。使用他们中的任意一个，都可以从``Bundle``中得到数据恢复之前的状态。

There's no guarantee that ``onSaveInstanceState()`` will be called before your activity is destroyed, because there are cases in which it won't be necessary to save the state (such as when the user leaves your activity using the Back button, because the user is explicitly closing the activity). If the system calls ``onSaveInstanceState()``, it does so before ``onStop()`` and possibly before ``onPause()``.系统不能保证在Activity被杀死之前调用``onSaveInstanceState``，因为有时不需要保存状态。系统调用``onSaveInstanceState``发生在``onStop``之前或者在``onPause``之前。

However, even if you do nothing and do not implement ``onSaveInstanceState()``, some of the activity state is restored by the ``Activity`` class's default implementation of ``onSaveInstanceState()``. Specifically, the default implementation calls the corresponding ``onSaveInstanceState()`` method for every ``View`` in the layout, which allows each view to provide information about itself that should be saved. Almost every widget in the Android framework implements this method as appropriate, such that any visible changes to the UI are automatically saved and restored when your activity is recreated. For example, the ``EditText`` widget saves any text entered by the user and the ``CheckBox`` widget saves whether it's checked or not. The only work required by you is to provide a unique ID (with the ``android:id`` attribute) for each widget you want to save its state. If a widget does not have an ID, then the system cannot save its state.即使在``onSaveInstanceState``中什么都不做，Activity的一些状态也是会被保存下来。``onSaveInstanceState``的默认实现是调用layout中每个``View``的对应的``onSaveInstanceState``方法，每个``View``的默认``onSaveInstanceState``方法会提供自身需要保存的信息。基本上每个``View``组件都实现了这个方法来保存自身的状态。需要做的仅仅是给需要保存状态的组件一个``id``，在manifest文件中提供``android:id``属性。如果一个组件没有id，就不能保存他的状态。You can also explicitly stop a view in your layout from saving its state by setting the ``android:saveEnabled`` attribute to "false" or by calling the ``setSaveEnabled()`` method. Usually, you should not disable this, but you might if you want to restore the state of the activity UI differently.可以显式的设置一个``View``的``android:saveEnabled``属性为``false``或者调用组件的``setSaveEnabled``方法来设置``View``组件是否保存自身的状态。Because the default implementation of ``onSaveInstanceState()`` helps save the state of the UI, if you override the method in order to save additional state information, you should always call the superclass implementation of ``onSaveInstanceState()`` before doing any work. Likewise, you should also call the superclass implementation of ``onRestoreInstanceState()`` if you override it, so the default implementation can restore view states.在``onSaveInstanceState``和``onRestoreInstanceState``方法中别忘了调用父类的对应方法。

Because ``onSaveInstanceState()`` is not guaranteed to be called, you should use it only to record the transient state of the activity (the state of the UI)—you should never use it to store persistent data. Instead, you should use ``onPause()`` to store persistent data (such as data that should be saved to a database) when the user leaves the activity.``onSaveInstanceState``方法不一定保证执行，应该在``onPause``中做数据持久化工作。

When one activity starts another, they both experience lifecycle transitions. The first activity pauses and stops (though, it won't stop if it's still visible in the background), while the other activity is created. In case these activities share data saved to disc or elsewhere, it's important to understand that the first activity is not completely stopped before the second one is created. Rather, the process of starting the second one overlaps with the process of stopping the first one.当一个``Activity``启动另外一个``Activity``时，他们的生命周期都会发生变化。

The order of lifecycle callbacks is well defined, particularly when the two activities are in the same process and one is starting the other. Here's the order of operations that occur when Activity A starts Acivity B:
+ Activity A's ``onPause()`` method executes.
+ Activity B's ``onCreate()``, ``onStart()``, and ``onResume()`` methods execute in sequence. (Activity B now has user focus.)
+ Then, if Activity A is no longer visible on screen, its ``onStop()`` method executes.

### Fragments


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













