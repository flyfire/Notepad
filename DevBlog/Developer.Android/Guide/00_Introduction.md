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

High-level permissions restricting access to entire components of the system or application can be applied through your ``AndroidManifest.xml``. All that this requires is including an ``android:permission`` attribute on the desired component, naming the permission that will be used to control access to it.

``Activity`` permissions (applied to the <activity> tag) restrict who can start the associated activity. The permission is checked during ``Context.startActivity()`` and ``Activity.startActivityForResult()``; if the caller does not have the required permission then ``SecurityException`` is thrown from the call.``Activity`` permissions 限制谁可以启动关联的 activity。

``Service`` permissions (applied to the <service> tag) restrict who can start or bind to the associated service. The permission is checked during ``Context.startService()``, ``Context.stopService()`` and ``Context.bindService()``; if the caller does not have the required permission then ``SecurityException`` is thrown from the call.``Service`` permissions 限制谁可以启动或bind到一个service上。

``BroadcastReceiver`` permissions (applied to the <receiver> tag) restrict who can send broadcasts to the associated receiver. The permission is checked after ``Context.sendBroadcast()`` returns, as the system tries to deliver the submitted broadcast to the given receiver. As a result, a permission failure will not result in an exception being thrown back to the caller; it will just not deliver the intent. In the same way, a permission can be supplied to ``Context.registerReceiver()`` to control who can broadcast to a programmatically registered receiver. Going the other way, a permission can be supplied when calling ``Context.sendBroadcast()`` to restrict which ``BroadcastReceiver`` objects are allowed to receive the broadcast.``BroadcastReceive`` permissions 限制了谁可以向关联的receiver发送广播。动态注册的receiver也可以有 permission 来限制。同样的，调用``sendBroadcast``时也可以使用permission来限制哪个receiver可以接收这个广播。

``ContentProvider`` permissions (applied to the <provider> tag) restrict who can access the data in a ``ContentProvider``. (Content providers have an important additional security facility available to them called ``URI permissions`` which is described later.) Unlike the other components, there are two separate permission attributes you can set: ``android:readPermission`` restricts who can read from the provider, and ``android:writePermission`` restricts who can write to it. Note that if a provider is protected with both a read and write permission, holding only the write permission does not mean you can read from a provider. The permissions are checked when you first retrieve a provider (if you don't have either permission, a ``SecurityException`` will be thrown), and as you perform operations on the provider. Using ``ContentResolver.query()`` requires holding the read permission; using ``ContentResolver.insert()``, ``ContentResolver.update()``, ``ContentResolver.delete()`` requires the write permission. In all of these cases, not holding the required permission results in a ``SecurityException`` being thrown from the call.``ContentProvider`` permission 限制谁可以访问 ``ContentProvider`` 提供的数据。``android:readPermission``和``android:writePermission``分别限制读写。

In addition to the permission enforcing who can send Intents to a registered ``BroadcastReceiver``, you can also specify a required permission when sending a broadcast. By calling ``Context.sendBroadcast()`` with a permission string, you require that a receiver's application must hold that permission in order to receive your broadcast.除了receiver可以设置permission来限制谁可以给他发广播，在调用``sendBroadcast``时使用permission string也可以限制接收这个广播的application要有这个permission。Note that both a receiver and a broadcaster can require a permission. When this happens, both permission checks must pass for the Intent to be delivered to the associated target.

Arbitrarily fine-grained permissions can be enforced at any call into a service. This is accomplished with the ``Context.checkCallingPermission()`` method. Call with a desired permission string and it will return an integer indicating whether that permission has been granted to the current calling process. Note that this can only be used when you are executing a call coming in from another process, usually through an IDL interface published from a service or in some other way given to another process.

## URI Permissions
The standard permission system described so far is often not sufficient when used with content providers. A content provider may want to protect itself with read and write permissions, while its direct clients also need to hand specific URIs to other applications for them to operate on. A typical example is attachments in a mail application. Access to the mail should be protected by permissions, since this is sensitive user data. However, if a URI to an image attachment is given to an image viewer, that image viewer will not have permission to open the attachment since it has no reason to hold a permission to access all e-mail.一个Email App可能想要保护用户的email数据，但是在打开email中的图片附件时，能打开图片的应用由于没有读取Eamil App ContentProvider的permission会导致无法打开图片。

The solution to this problem is per-URI permissions: when starting an activity or returning a result to an activity, the caller can set ``Intent.FLAG_GRANT_READ_URI_PERMISSION`` and/or ``Intent.FLAG_GRANT_WRITE_URI_PERMISSION``. This grants the receiving activity permission access the specific data URI in the Intent, regardless of whether it has any permission to access data in the content provider corresponding to the Intent.解决方法是在调用打开图片时，给``startActivity``设置``per-URI permission``，即``Intent.FLAG_GRANT_READ_URI_PERMISSION``或``Intent.FLAG_GRANT_WRITE_URI_PERMISSION``。这将会给接收数据的activity赋予来读取或者写入Intent传过来的URI的权限而不用管这个接收数据的activity是否有读取ContentProvider数据的权限。

This mechanism allows a common capability-style model where user interaction (opening an attachment, selecting a contact from a list, etc) drives ad-hoc granting of fine-grained permission. This can be a key facility for reducing the permissions needed by applications to only those directly related to their behavior.临时赋予一些App权限。

The granting of fine-grained URI permissions does, however, require some cooperation with the content provider holding those URIs. It is strongly recommended that content providers implement this facility, and declare that they support it through the ``android:grantUriPermissions`` attribute or ``<grant-uri-permissions>`` tag.给App临时赋予读取URI的权限需要持有这些URI的ContentProvider的配合，需要ContentProvider来申请``android:grantUriPermission``权限。More information can be found in the ``Context.grantUriPermission()``, ``Context.revokeUriPermission()``, and ``Context.checkUriPermission()`` methods.