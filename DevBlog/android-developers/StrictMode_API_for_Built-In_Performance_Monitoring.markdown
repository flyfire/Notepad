StrictMode API for Built-In Performance Monitoring
=====================================================
[refs](http://android-developers.blogspot.com/2010/12/new-gingerbread-api-strictmode.html)
+ You can’t depend on the flash components or filesystems used in most Android devices to be consistently fast. The YAFFS filesystem used on many Android devices, for instance, has a global lock around all its operations. Only one disk operation can be in-flight across the entire device. Even a simple “stat” operation can take quite a while if you are unlucky. Other devices with more traditional block device-based filesystems still occasionally suffer when the block rotation layer decides to garbage collect and do some slow internal flash erase operations. (For some good geeky background reading, see lwn.net/Articles/353411) The take-away is that the “disk” (or filesystem) on mobile devices is usually fast, but the 90th percentile latencies are often quite poor. Also, most filesystems slow down quite a bit as they get more full. (See slides from Google I/O Zippy Android apps talk, linked off code.google.com/p/zippy-android)
+ Enable ``StrictMode``,in your application or component’s ``onCreate()``:
```java
public void onCreate() {
     if (DEVELOPER_MODE) {
         StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                 .detectDiskReads()
                 .detectDiskWrites()
                 .detectNetwork()
                 .penaltyLog()
                 .build());
     }
     super.onCreate();
}
// or just use default
public void onCreate() {
     if (DEVELOPER_MODE) {
         StrictMode.enableDefaults();
     }
     super.onCreate();
 }
```
+ Where we couldn’t automatically speed up the system, we instead added APIs to make certain patterns easier to do efficiently. For example, there is a new method ``SharedPreferences.Editor.apply()``, which you should be using instead of ``commit()`` if you don’t need ``commit()’s`` return value. (It turns out almost nobody ever checks it.) You can even use reflection to conditionally use apply() vs. commit() depending on the user’s platform version.
+ http://code.google.com/p/zippy-android/source/browse/trunk/examples/SharedPreferencesCompat.java
+