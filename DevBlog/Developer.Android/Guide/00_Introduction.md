00_Introduction.md
============================
#App Fundamentals

#Device Compatibility

#System Permissions

Because security enforcement happens at the process level, the code of any two packages cannot normally run in the same process, since they need to run as different Linux users. You can use the sharedUserId attribute in the AndroidManifest.xml's manifest tag of each package to have them assigned the same user ID. By doing this, for purposes of security the two packages are then treated as being the same application, with the same user ID and file permissions. Note that in order to retain security, only two applications signed with the same signature (and requesting the same sharedUserId) will be given the same user ID.

Any data stored by an application will be assigned that application's user ID, and not normally accessible to other packages. When creating a new file with getSharedPreferences(String, int), openFileOutput(String, int), or openOrCreateDatabase(String, int, SQLiteDatabase.CursorFactory), you can use the MODE_WORLD_READABLE and/or MODE_WORLD_WRITEABLE flags to allow any other package to read/write the file. When setting these flags, the file is still owned by your application, but its global read and/or write permissions have been set appropriately so any other application can see it.

The permissions provided by the Android system can be found at [Manifest.permission](https://developer.android.com/reference/android/Manifest.permission.html). Any application may also define and enforce its own permissions, so this is not a comprehensive list of all possible permissions.

You can see which permissions were added with each release in the [Build.VERSION_CODES](https://developer.android.com/reference/android/os/Build.VERSION_CODES.html) documentation.

To enforce your own permissions, you must first declare them in your ``AndroidManifest.xml`` using one or more ``<permission>`` tags.For example, an application that wants to control who can start one of its activities could declare a permission for this operation as follows:
```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    package="com.me.app.myapp" >
    <permission android:name="com.me.app.myapp.permission.DEADLY_ACTIVITY"
        android:label="@string/permlab_deadlyActivity"
        android:description="@string/permdesc_deadlyActivity"
        android:permissionGroup="android.permission-group.COST_MONEY"
        android:protectionLevel="dangerous" />
    ...
</manifest>
```