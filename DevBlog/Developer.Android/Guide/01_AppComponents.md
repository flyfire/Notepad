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

When you perform a fragment transaction, you can also add it to a back stack that's managed by the activity—each back stack entry in the activity is a record of the fragment transaction that occurred. The back stack allows the user to reverse a fragment transaction (navigate backwards), by pressing the Back button.

A fragment is usually used as part of an activity's user interface and contributes its own layout to the activity.To provide a layout for a fragment, you must implement the ``onCreateView()`` callback method, which the Android system calls when it's time for the fragment to draw its layout. Your implementation of this method must return a ``View`` that is the root of your fragment's layout.If your fragment is a subclass of ``ListFragment``, the default implementation returns a ``ListView`` from ``onCreateView()``, so you don't need to implement it.

``fargment``创建过程：At the very beginning, a fragment is instantiated. It now exists as an object in memory.The first thing that is likely to happen is that initialization arguments will be added to your fragment object. This is definitely true in the situation where the system is re-creating your fragment from a saved state. When the system is restoring a fragment from a saved state, the default constructor is invoked, followed by the attachment of the initialization arguments bundle.

```java
public static MyFragment newInstance(int index) {
MyFragment f = new MyFragment();
Bundle args = new Bundle();
args.putInt(“index”, index);
f.setArguments(args);
return f;
}
```

From the client’s point of view, they get a new instance by calling the static
``newInstance()`` method with a single argument. They get the instantiated object back, and the initialization argument has been set on this fragment in the arguments bundle. If this fragment is saved and reconstructed later, the system will go through a very similar process of calling the default constructor and then reattaching the initialization arguments. 

``onInflate``:The next thing that could happen is layout view inflation. If your fragment is defined by a ``<fragment>`` tag in a layout that is being inflated (typically when an activity has called ``setContentView()`` for its main layout), your fragment’s ``onInflate()`` callback is called.This passes in the activity just mentioned, an ``AttributeSet`` with the attributes from the ``<fragment>`` tag, and a saved bundle. The saved bundle is the one with the saved state values in it, put there by ``onSaveInstanceState()``. The expectation of ``onInflate()`` is that you’ll read attribute values and save them for later use. At this stage in the fragment’s life, it’s too early to actually do anything with the user interface. The fragment is not even associated to its activity yet. 

``onAttach``:The ``onAttach()`` callback is invoked after your fragment is associated with its activity.The activity reference is passed to you if you want to use it. You can at least use the activity to determine information about your enclosing activity. You can also use the activity as a context to do other operations. One thing to note is that the Fragment class has a ``getActivity()`` method that will always return the attached activity for your fragment should you need it. Keep in mind that all during this lifecycle, the initialization arguments bundle is available to you from the fragment’s ``getArguments()`` method.However, once the fragment is attached to its activity, you can’t call ``setArguments()`` again. So you can’t add to the initialization arguments except in the very beginning.

``onCreate``:Next up is the ``onCreate()`` callback. Although this is similar to the activity’s ``onCreate()``,the difference is that you should not put code in here that relies on the existence of the activity’s view hierarchy. Your fragment may be associated to its activity by now, but you haven’t yet been notified that the activity’s ``onCreate()`` has finished. That’s coming
up. This callback gets the saved state bundle passed in, if there is one. This callback is about as early as possible to create a background thread to get data that this fragment will need. Your fragment code is running on the UI thread, and you don’t want to do disk I/O or network accesses on the UI thread. In fact, it makes a lot of sense to fire off a background thread to get things ready. Your background thread is where blocking calls should be. You’ll need to hook up with the data later, perhaps using a handler or some
other technique.``fragment``在``onCreate``回调方法中时，``activity``的``onCreate``方法可能还没有走完，因此不能依赖``activity``的view hierarchy。

``onCreateView``:The next callback is ``onCreateView()``. The expectation here is that you will return a view hierarchy for this fragment.

``onActivityCreated()``:You’re now getting close to the point where the user can interact with your fragment.The next callback is ``onActivityCreated()``. This is called after the activity has completed its ``onCreate()`` callback. You can now trust that the activity’s view hierarchy, including your own view hierarchy if you returned one earlier, is ready and available. This is where
you can do final tweaks to the user interface before the user sees it. This could be especially important if this activity and its fragments are being re-created from a saved state. It’s also where you can be sure that any other fragment for this activity has been attached to your activity.

``onStart``:The next callback in your fragment lifecycle is ``onStart()``. Now your fragment is visible to the user. But you haven’t started interacting with the user just yet. This callback is tied to the activity’s ``onStart()``. As such, whereas previously you may have put your logic into the activity’s ``onStart()``, now you’re more likely to put your logic into the fragment’s ``onStart()``, because that is also where the user interface components are.

``onResume``:The last callback before the user can interact with your fragment is ``onResume()``. This callback is tied to the activity’s ``onResume()``. When this callback returns, the user is free to interact with this fragment. 

``onPause``:The first undo callback on a fragment is ``onPause()``. This callback is tied to the activity’s ``onPause()``. 

``onSaveInstanceState``:Similar to activities, fragments have an opportunity to save state for later reconstruction.This callback passes in a ``Bundle`` object to be used as the container for whatever state information you want to hang onto. This is the saved-state bundle passed to the callbacks covered earlier. To prevent memory problems, be careful about what you save into this bundle. Only save what you need. If you need to keep a reference to another fragment, save its tag instead of trying to save the other fragment.Although you may see this method usually called right after ``onPause()``, the activity to which this fragment belongs calls it when it feels that the fragment’s state should be saved. This can occur any time before ``onDestroy()``.

``onStop``:The next undo callback is ``onStop()``. This one is tied to the activity’s ``onStop()`` and serves a purpose similar to an activity’s ``onStop()``. A fragment that has been stopped could go straight back to the ``onStart()``callback, which then leads to ``onResume()``.

``onDestoryView``:If your fragment is on its way to being killed off or saved, the next callback in the undo direction is ``onDestroyView()``. This will be called after the view hierarchy you created on your ``onCreateView()`` callback earlier has been detached from your fragment.

``onDestory``:Next up is ``onDestroy()``. This is called when the fragment is no longer in use. Note that it is still attached to the activity and is still findable, but it can’t do much.

``onDetach``:The final callback in a fragment’s lifecycle is ``onDetach()``. Once this is invoked, the fragment is not tied to its activity, it does not have a view hierarchy anymore, and all its resources should have been released.

``setRetainInstance``:One of the cool features of a fragment is that you can specify that you don’t want the fragment completely destroyed if the activity is being re-created and therefore your fragments will be coming back also. Therefore, fragment comes with a method called ``setRetainInstance()``, which takes a boolean parameter to tell it “Yes; I want you to hang around when my activity restarts” or “No; go away, and I’ll create a new fragment from scratch.” The best place to call ``setRetainInstance()`` is in the ``onCreate()`` callback of a fragment.If the parameter is true, that means you want to keep your fragment object in memory and not start over from scratch. However, if your activity is going away and being re-created, you’ll have to detach your fragment from this activity and attach it to the new one. The bottom line is that if the retain instance value is true, you won’t actually destroy your fragment instance, and therefore you won’t need to create a new one on the other side. 如果在``setRetainInstance``中设置为true，表明将``fragment``保留在内存中，这样在``onDestoryView``后就不会走``onDestory``而直接走``onDetach``，同样在``activity``重新创建时，``fragment``走完``onAttach``后会跳过``onCreate``而直接走``onCreateView``了。

```bash
D/Solarex (  975): TitlesFragment : onInflate enter without activity parameter
D/Solarex (  975): TitlesFragment : onInflate exit without activity parameter
D/Solarex (  975): TitlesFragment : onInflate enter with activity parameter
D/Solarex (  975): TitlesFragment : onInflate exit with activity parameter
D/Solarex (  975): TitlesFragment : onAttach enter
D/Solarex (  975): TitlesFragment : onAttach exit
D/Solarex (  975): TitlesFragment : onCreate enter
D/Solarex (  975): TitlesFragment : onCreate exit
D/Solarex (  975): TitlesFragment : onActivityCreated enter
D/Solarex (  975): TitlesFragment : savedInstanceState is null
D/Solarex (  975): MainActivity : showDetails : index = 0
D/Solarex (  975): MainActivity :  isMulti = false
D/Solarex (  975): MainActivity : showDetails exiting
D/Solarex (  975): TitlesFragment : onActivityCreated exit
D/Solarex (  975): TitlesFragment : onStart enter
D/Solarex (  975): TitlesFragment : onStart exit
D/Solarex (  975): TitlesFragment : onResume enter
D/Solarex (  975): TitlesFragment : onResume exit
D/Solarex (  975): TitlesFragment : onPause enter
D/Solarex (  975): TitlesFragment : onPause exit
D/Solarex (  975): DetailsActivity : onCreate entering
D/Solarex (  975): DetailsFragment : newInstance bundle.index = 0
D/Solarex (  975): DetailsFragment : newInstance index = 0
D/Solarex (  975): DetailsActivity : onCreate exiting
D/Solarex (  975): DetailsFragment : onCreate enter
D/Solarex (  975): DetailsFragment :  savedInstanceState is null
D/Solarex (  975): DetailsFragment : onCreate exiting
D/Solarex (  975): DetailsFragment : onCreateView cont.ainer = android.widget.FrameLayout@417c4b68
D/Solarex (  975): TitlesFragment : onSaveInstanceState enter mCurCheckPosition = 0
D/Solarex (  975): TitlesFragment : onSaveInstanceState exit
D/Solarex (  975): TitlesFragment : onStop enter
D/Solarex (  975): TitlesFragment : onStop exit
```

Besides the view hierarchy, a fragment has a bundle that serves as its initialization arguments. Similar to an activity, a fragment can be saved and later restored automatically by the system. **When the system restores a fragment, it calls the default constructor (with no arguments) and then restores this bundle of arguments to the newly created fragment.** Subsequent callbacks on the fragment have access to these arguments and can use them to get the fragment back to its previous state. For this reason, it is imperative that you 1.Ensure that there’s a default constructor for your fragment class.2.Add a bundle of arguments as soon as you create a new fragment so these subsequent methods can properly set up your fragment, and so the system can restore your fragment properly when necessary.

Declare the fragment inside the activity's layout file：When the system creates this activity layout, it instantiates each ``fragment`` specified in the layout and calls the ``onCreateView()`` method for each one, to retrieve each fragment's layout. The system inserts the ``View`` returned by the ``fragment`` directly in place of the ``<fragment>`` element.Each fragment requires a unique identifier that the system can use to restore the ``fragment`` if the activity is restarted (and which you can use to capture the fragment to perform transactions, such as remove it). There are three ways to provide an ID for a ``fragment``:
  + Supply the ``android:id`` attribute with a unique ID.
  + Supply the ``android:tag`` attribute with a unique string.
  + If you provide neither of the previous two, the system uses the ID of the container view.

To add a ``fragment`` without a UI, add the ``fragment`` from the activity using ``add(Fragment, String)`` (supplying a unique string "tag" for the fragment, rather than a view ID). This adds the fragment, but, because it's not associated with a view in the activity layout, it does not receive a call to ``onCreateView()``. So you don't need to implement that method.Supplying a string tag for the fragment isn't strictly for non-UI fragments—you can also supply string tags to fragments that do have a UI—but if the fragment does not have a UI, then the string tag is the only way to identify it. If you want to get the ``fragment`` from the activity later, you need to use ``findFragmentByTag()``.

To manage the fragments in your activity, you need to use ``FragmentManager``. To get it, call ``getFragmentManager()`` from your activity.
+ Get fragments that exist in the activity, with ``findFragmentById()`` (for fragments that provide a UI in the activity layout) or ``findFragmentByTag()`` (for fragments that do or don't provide a UI).
+ Pop fragments off the back stack, with ``popBackStack()`` (simulating a Back command by the user).
+ Register a listener for changes to the back stack, with ``addOnBackStackChangedListener()``.

If you do not call ``addToBackStack()`` when you perform a transaction that removes a fragment, then that fragment is destroyed when the transaction is committed and the user cannot navigate back to it. Whereas, if you do call ``addToBackStack()`` when removing a fragment, then the fragment is stopped and will be resumed if the user navigates back.For each fragment transaction, you can apply a transition animation, by calling ``setTransition()`` before you commit.Calling ``commit()`` does not perform the transaction immediately. Rather, it schedules it to run on the activity's UI thread (the "main" thread) as soon as the thread is able to do so. If necessary, however, you may call ``executePendingTransactions()`` from your UI thread to immediately execute transactions submitted by ``commit()``. Doing so is usually not necessary unless the transaction is a dependency for jobs in other threads.You can commit a transaction using ``commit()`` only prior to the activity saving its state (when the user leaves the activity). If you attempt to commit after that point, an exception will be thrown. This is because the state after the commit can be lost if the activity needs to be restored. For situations in which its okay that you lose the commit, use ``commitAllowingStateLoss()``.

Specifically, the fragment can access the Activity instance with ``getActivity()`` and easily perform tasks such as find a view in the activity layout:``View listView = getActivity().findViewById(R.id.list);``.Likewise, your activity can call methods in the fragment by acquiring a reference to the ``Fragment`` from ``FragmentManager``, using ``findFragmentById()`` or ``findFragmentByTag()``. For example:``ExampleFragment fragment = (ExampleFragment) getFragmentManager().findFragmentById(R.id.example_fragment);``.

In some cases, you might need a fragment to share events with the activity. A good way to do that is to define a callback interface inside the fragment and require that the host activity implement it. When the activity receives a callback through the interface, it can share the information with other fragments in the layout as necessary.当fragment需要和activity share events时，可以在fragment中定义一个Interface让activity来实现这个interface。

```java
public static class FragmentA extends ListFragment {
    ...
    // Container Activity must implement this interface
    public interface OnArticleSelectedListener {
        public void onArticleSelected(Uri articleUri);
    }
    ...
    OnArticleSelectedListener mListener;
    ...
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnArticleSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnArticleSelectedListener");
        }
    }
    ...
    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        // Append the clicked item's row ID with the content provider Uri
        Uri noteUri = ContentUris.withAppendedId(ArticleColumns.CONTENT_URI, id);
        // Send the event and Uri to the host activity
        mListener.onArticleSelected(noteUri);
    }
    ...
}
```

Your fragments can contribute menu items to the activity's Options Menu (and, consequently, the Action Bar) by implementing ``onCreateOptionsMenu()``. In order for this method to receive calls, however, you must call ``setHasOptionsMenu()`` during ``onCreate()``, to indicate that the fragment would like to add items to the Options Menu (otherwise, the fragment will not receive a call to ``onCreateOptionsMenu()``).Any items that you then add to the Options Menu from the fragment are appended to the existing menu items. The fragment also receives callbacks to ``onOptionsItemSelected()`` when a menu item is selected.You can also register a view in your fragment layout to provide a context menu by calling ``registerForContextMenu()``. When the user opens the context menu, the fragment receives a call to ``onCreateContextMenu()``. When the user selects an item, the fragment receives a call to ``onContextItemSelected()``.Although your fragment receives an on-item-selected callback for each menu item it adds, the activity is first to receive the respective callback when the user selects a menu item. If the activity's implementation of the on-item-selected callback does not handle the selected item, then the event is passed to the fragment's callback. This is true for the Options Menu and context menus.menu item被点击时，activity先对点击作出反应，如果没有方法对此作出反应，则会将事件传递给fragment来进行处理。

Also like an activity, you can retain the state of a fragment using a ``Bundle``, in case the activity's process is killed and you need to restore the fragment state when the activity is recreated. You can save the state during the fragment's ``onSaveInstanceState()`` callback and restore it during either ``onCreate()``, ``onCreateView()``, or ``onActivityCreated()``. The most significant difference in lifecycle between an activity and a fragment is how one is stored in its respective back stack. An activity is placed into a back stack of activities that's managed by the system when it's stopped, by default (so that the user can navigate back to it with the Back button, as discussed in Tasks and Back Stack). However, a fragment is placed into a back stack managed by the host activity only when you explicitly request that the instance be saved by calling ``addToBackStack()`` during a transaction that removes the fragment.activity和fragment生命周期的一个很大不同点在于back stack。activity stop时默认会被放入一个由系统管理的activity back stack，然而一个fragment只有当显式调用``addToBackStack()``时才会被放入到一个由host activity管理的back stack。

If you need a ``Context`` object within your Fragment, you can call ``getActivity()``. However, be careful to call ``getActivity()`` only when the fragment is attached to an activity. When the fragment is not yet attached, or was detached during the end of its lifecycle, ``getActivity()`` will return null.

An application that uses loaders typically includes the following:
+ An ``Activity`` or ``Fragment``.
+ An instance of the ``LoaderManager``.
+ A ``CursorLoader`` to load data backed by a ``ContentProvider``. Alternatively, you can implement your own subclass of ``Loader`` or ``AsyncTaskLoader`` to load data from some other source.
+ An implementation for ``LoaderManager.LoaderCallbacks``. This is where you create new loaders and manage your references to existing loaders.
+ A way of displaying the loader's data, such as a ``SimpleCursorAdapter``.
+ A data source, such as a ``ContentProvider``, when using a ``CursorLoader``.


## Loader


## Tasks and Back Stack



#Services

#Content Providers

#App Widgets

#Processes and Threads