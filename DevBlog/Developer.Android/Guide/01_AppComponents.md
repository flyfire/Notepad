01_AppComponents
============================
#Intents and Intent Filters

To ensure your app is secure, always use an explicit intent when starting a Service and do not declare intent filters for your services. Using an implicit intent to start a service is a security hazard because you cannot be certain what service will respond to the intent, and the user cannot see which service starts.When starting a Service, you should always specify the component name. Otherwise, you cannot be certain what service will respond to the intent, and the user cannot see which service starts.To avoid inadvertently running a different app's Service, always use an explicit intent to start your own service and do not declare intent filters for your service.

If you want to set both the URI and MIME type, do not call ``setData()`` and ``setType()`` because they each nullify the value of the other. Always use ``setDataAndType()`` to set both URI and MIME type.

``Intent``:ComponentName,Action,Data,Category represent the defining characteristics of an intent. By reading these properties, the Android system is able to resolve which app component it should start.Extras,Key-value pairs that carry additional information required to accomplish the requested action.Flags defined in the Intent class that function as metadata for the intent. The flags may instruct the Android system how to launch an activity (for example, which task the activity should belong to) and how to treat it after it's launched (for example, whether it belongs in the list of recent activities).

It's possible that a user won't have any apps that handle the implicit intent you send to ``startActivity()``. If that happens, the call will fail and your app will crash. To verify that an activity will receive the intent, call ``resolveActivity()`` on your Intent object. If the result is non-null, then there is at least one app that can handle the intent and it's safe to call ``startActivity()``. If the result is null, you should not use the intent and, if possible, you should disable the feature that issues the intent.

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

Check whether the system have component to handle the intent.
```java
private static boolean isIntentAvailable(Intent intent){
        boolean isAvailable = false;
        PackageManager pm = mContext.getPackageManager();
        List<ResolveInfo> list = pm.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        isAvailable = list.sieze() > 0;
        log("Checking intent " + intent + " isAvailable = " + isAvailable);
        return isAvailable;
    }
```

Forcing an app chooser
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

Each intent filter is defined by an <intent-filter> element in the app's manifest file, nested in the corresponding app component (such as an <activity> element). Inside the <intent-filter>, you can specify the type of intents to accept using one or more of these three elements:``<action>``Declares the intent action accepted, in the name attribute. The value must be the literal string value of an action, not the class constant,``<data>``Declares the type of data accepted, using one or more attributes that specify various aspects of the data URI (scheme, host, port, path, etc.) and MIME type,``<category>``Declares the intent category accepted, in the name attribute. The value must be the literal string value of an action, not the class constant.**In order to receive implicit intents, you must include the ``CATEGORY_DEFAULT`` category in the intent filter. The methods ``startActivity()`` and ``startActivityForResult()`` treat all intents as if they declared the ``CATEGORY_DEFAULT`` category. If you do not declare this category in your intent filter, no implicit intents will resolve to your activity.**

Restricting access to components:Using an intent filter is not a secure way to prevent other apps from starting your components. Although intent filters restrict a component to respond to only certain kinds of implicit intents, another app can potentially start your app component by using an explicit intent if the developer determines your component names. If it's important that only your own app is able to start one of your components, set the exported attribute to "false" for that component.

The ``ACTION_MAIN`` action indicates this is the main entry point and does not expect any intent data.The ``CATEGORY_LAUNCHER`` category indicates that this activity's icon should be placed in the system's app launcher. If the ``<activity>`` element does not specify an icon with icon, then the system uses the icon from the ``<application>`` element.These two must be paired together in order for the activity to appear in the app launcher.

A ``PendingIntent`` object is a wrapper around an ``Intent`` object. The primary purpose of a ``PendingIntent`` is to grant permission to a foreign application to use the contained ``Intent`` as if it were executed from your app's own process.

Major use cases for a pending intent include:
+ Declare an ``intent`` to be executed when the user performs an action with your ``Notification`` (the Android system's ``NotificationManager`` executes the ``Intent``).
+ Declare an intent to be executed when the user performs an action with your App Widget (the Home screen app executes the Intent).
+ Declare an intent to be executed at a specified time in the future (the Android system's ``AlarmManager`` executes the Intent).

Because each ``Intent`` object is designed to be handled by a specific type of app component (either an ``Activity``, a ``Service``, or a ``BroadcastReceiver``), so too must a ``PendingIntent`` be created with the same consideration. When using a pending intent, your app will not execute the intent with a call such as ``startActivity()``. You must instead declare the intended component type when you create the ``PendingIntent`` by calling the respective creator method:
+ ``PendingIntent.getActivity()`` for an ``Intent`` that starts an ``Activity``.
+ ``PendingIntent.getService()`` for an ``Intent`` that starts a ``Service``.
+ ``PendingIntent.getBroadcast()`` for a ``Intent`` that starts an ``BroadcastReceiver``.
Unless your app is receiving pending intents from other apps, the above methods to create a ``PendingIntent`` are the only ``PendingIntent`` methods you'll probably ever need.Each method takes the current app ``Context``, the ``Intent`` you want to wrap, and one or more flags that specify how the intent should be used (such as whether the intent can be used more than once).More information about using pending intents is provided with the documentation for each of the respective use cases, such as in the [``Notifications``](https://developer.android.com/guide/topics/ui/notifiers/notifications.html) and [``App Widgets``](https://developer.android.com/guide/topics/appwidgets/index.html) API guides.https://developer.android.com/reference/android/app/PendingIntent.html

@todo:20140505+[Intent](https://android.googlesource.com/platform/frameworks/base/+/refs/heads/master/core/java/android/content/Intent.java)
@question:how intent be resolved ActivityManagerService.java PackageManagerService.java

``Intent matching``:The ``PackageManager`` has a set of ``query...()`` methods that return all components that can accept a particular intent, and a similar series of ``resolve...()`` methods that determine the best component to respond to an intent. For example, ``queryIntentActivities()`` returns a list of all activities that can perform the intent passed as an argument, and ``queryIntentServices()`` returns a similar list of services. Neither method activates the components; they just list the ones that can respond. There's a similar method, ``queryBroadcastReceivers()``, for broadcast receivers.

If there are no apps on the device that can receive the implicit intent, your app will crash when it calls ``startActivity()``. To first verify that an app exists to receive the intent, call ``resolveActivity()`` on your ``Intent`` object. If the result is non-null, there is at least one app that can handle the intent and it's safe to call ``startActivity()``. If the result is null, you should not use the intent and, if possible, you should disable the feature that invokes the intent.

@todo:https://developer.android.com/training/camera/photobasics.html,https://developer.android.com/training/camera/videobasics.html

Retrieve a specific type of file:To request that the user select a file such as a document or photo and return a reference to your app, use the ``ACTION_GET_CONTENT`` action and specify your desired MIME type. The file reference returned to your app is transient to your activity's current lifecycle, so if you want to access it later you must import a copy that you can read later. This intent also allows the user to create a new file in the process (for example, instead of selecting an existing photo, the user can capture a new photo with the camera).The result intent delivered to your ``onActivityResult()`` method includes data with a URI pointing to the file. The URI could be anything, such as an http: URI, file: URI, or content: URI. However, if you'd like to restrict selectable files to only those that are accessible from a content provider (a content: URI) and that are available as a file stream with ``openFileDescriptor()``, you should add the ``CATEGORY_OPENABLE`` category to your intent.On Android 4.3 (API level 18) and higher, you can also allow the user to select multiple files by adding ``EXTRA_ALLOW_MULTIPLE`` to the intent, set to true. You can then access each of the selected files in a ``ClipData`` object returned by ``getClipData()``.

Instead of retrieving a copy of a file that you must import to your app (by using the ``ACTION_GET_CONTENT`` action), when running on Android 4.4 or higher, you can instead request to open a file that's managed by another app by using the ``ACTION_OPEN_DOCUMENT`` action and specifying a MIME type. To also allow the user to instead create a new document that your app can write to, use the ``ACTION_CREATE_DOCUMENT`` action instead. For example, instead of selecting from existing PDF documents, the ``ACTION_CREATE_DOCUMENT`` intent allows users to select where they'd like to create a new document (within another app that manages the document's storage)—your app then receives the URI location of where it can write the new document.Whereas the intent delivered to your ``onActivityResult()`` method from the ``ACTION_GET_CONTENT`` action may return a URI of any type, the result intent from ``ACTION_OPEN_DOCUMENT`` and ``ACTION_CREATE_DOCUMENT`` always specify the chosen file as a content: URI that's backed by a DocumentsProvider. You can open the file with ``openFileDescriptor()`` and query its details using columns from ``DocumentsContract.Document``.The returned URI grants your app long-term read access to the file (also possibly with write access). So the ``ACTION_OPEN_DOCUMENT`` action is particularly useful (instead of using ``ACTION_GET_CONTENT``) when you want to read an existing file without making a copy into your app, or when you want to open and edit a file in place.You can also allow the user to select multiple files by adding ``EXTRA_ALLOW_MULTIPLE`` to the intent, set to true. If the user selects just one item, then you can retrieve the item from ``getData()``. If the user selects more than one item, then ``getData()`` returns null and you must instead retrieve each item from a ``ClipData`` object that is returned by ``getClipData()``.Your intent must specify a MIME type and must declare the ``CATEGORY_OPENABLE`` category. If appropriate, you can specify more than one MIME type by adding an array of MIME types with the ``EXTRA_MIME_TYPES`` extra—if you do so, you must set the primary MIME type in setType() to ``*/*``.

#Activities
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

The foreground lifetime of an activity happens between the call to ``onResume()`` and the call to ``onPause()``. During this time, the activity is in front of all other activities on screen and has user input focus. An activity can frequently transition in and out of the foreground—for example, ``onPause()`` is called when the device goes to sleep or when a dialog appears. Because this state can transition often, the code in these two methods should be fairly lightweight in order to avoid slow transitions that make the user wait.

The system calls ``onSaveInstanceState()`` before making the activity vulnerable to destruction. The system passes this method a Bundle in which you can save state information about the activity as name-value pairs, using methods such as ``putString()`` and ``putInt()``. Then, if the system kills your application process and the user navigates back to your activity, the system recreates the activity and passes the ``Bundle`` to both ``onCreate()`` and ``onRestoreInstanceState()``. Using either of these methods, you can extract your saved state from the ``Bundle`` and restore the activity state. If there is no state information to restore, then the ``Bundle`` passed to you is null (which is the case when the activity is created for the first time).There's no guarantee that ``onSaveInstanceState()`` will be called before your activity is destroyed, because there are cases in which it won't be necessary to save the state (such as when the user leaves your activity using the Back button, because the user is explicitly closing the activity). If the system calls ``onSaveInstanceState()``, it does so before ``onStop()`` and possibly before ``onPause()``.

However, even if you do nothing and do not implement ``onSaveInstanceState()``, some of the activity state is restored by the Activity class's default implementation of ``onSaveInstanceState()``. Specifically, the default implementation calls the corresponding ``onSaveInstanceState()`` method for every ``View`` in the layout, which allows each view to provide information about itself that should be saved. Almost every widget in the Android framework implements this method as appropriate, such that any visible changes to the UI are automatically saved and restored when your activity is recreated. For example, the EditText widget saves any text entered by the user and the CheckBox widget saves whether it's checked or not. The only work required by you is to provide a unique ID (with the ``android:id`` attribute) for each widget you want to save its state. If a widget does not have an ID, then the system cannot save its state.通过在layout xml文件中给View设置id，可以让view保存自己的状态，而不需要在activity的``onSaveInstanceState``中做保存状态的操作。You can also explicitly stop a view in your layout from saving its state by setting the ``android:saveEnabled`` attribute to "false" or by calling the ``setSaveEnabled()`` method. Usually, you should not disable this, but you might if you want to restore the state of the activity UI differently.

When one activity starts another, they both experience lifecycle transitions. The first activity pauses and stops (though, it won't stop if it's still visible in the background), while the other activity is created. In case these activities share data saved to disc or elsewhere, it's important to understand that the first activity is not completely stopped before the second one is created. Rather, the process of starting the second one overlaps with the process of stopping the first one.Activity A 启动 Activity B时，两者的生命周期有重叠部分，overlap。

## Fragment
A Fragment represents a behavior or a portion of user interface in an Activity. You can combine multiple fragments in a single activity to build a multi-pane UI and reuse a fragment in multiple activities.A fragment must always be embedded in an activity and the fragment's lifecycle is directly affected by the host activity's lifecycle.

## Loader


## Tasks and Back Stack



#Services

#Content Providers

#App Widgets

#Processes and Threads