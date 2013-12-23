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













