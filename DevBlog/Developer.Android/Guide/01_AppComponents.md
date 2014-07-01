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
@question:how intent be resolved ActivityManagerService.java PackageManagerService.java ActivityResolver.java

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

If you need a ``Context`` object within your Fragment, you can call ``getActivity()``. However, be careful to call ``getActivity()`` only when the fragment is attached to an activity. When the fragment is not yet attached, or was detached during the end of its lifecycle, ``getActivity()`` will return null.``Fragment``之间通信可以通过``setTargetFragment``和``getTargetFragment``方式来实现。

## Loader
An application that uses loaders typically includes the following:

+ An ``Activity`` or ``Fragment``.
+ An instance of the ``LoaderManager``.
+ A ``CursorLoader`` to load data backed by a ``ContentProvider``. Alternatively, you can implement your own subclass of ``Loader`` or ``AsyncTaskLoader`` to load data from some other source.
+ An implementation for ``LoaderManager.LoaderCallbacks``. This is where you create new loaders and manage your references to existing loaders.
+ A way of displaying the loader's data, such as a ``SimpleCursorAdapter``.
+ A data source, such as a ``ContentProvider``, when using a ``CursorLoader``.

``getLoaderManager().initLoader(0, null, this);``:The ``initLoader()`` call ensures that a loader is initialized and active. It has two possible outcomes:If the loader specified by the ID already exists, the last created loader is reused,If the loader specified by the ID does not exist, ``initLoader()`` triggers the ``LoaderManager.LoaderCallbacks`` method ``onCreateLoader()``. This is where you implement the code to instantiate and return a new loader.

When you use ``initLoader()``, as shown above, it uses an existing loader with the specified ID if there is one. If there isn't, it creates one. But sometimes you want to discard your old data and start over.To discard your old data, you use ``restartLoader()``.

Loaders, in particular ``CursorLoader``, are expected to retain their data after being stopped. This allows applications to keep their data across the activity or fragment's ``onStop()`` and ``onStart()`` methods, so that when users return to an application, they don't have to wait for the data to reload.

``LoaderManager.LoaderCallbacks`` includes these methods:

+ ``onCreateLoader()`` — Instantiate and return a new ``Loader`` for the given ID.
+ ``onLoadFinished()`` — Called when a previously created loader has finished its load.This method is called when a previously created loader has finished its load. This method is guaranteed to be called prior to the release of the last data that was supplied for this loader. At this point you should remove all use of the old data (since it will be released soon), but should not do your own release of the data since its loader owns it and will take care of that.The loader will release the data once it knows the application is no longer using it. For example, if the data is a cursor from a ``CursorLoader``, you should not call ``close()`` on it yourself. If the cursor is being placed in a ``CursorAdapter``, you should use the ``swapCursor()`` method so that the old ``Cursor`` is not closed.
+ ``onLoaderReset()`` — Called when a previously created loader is being reset, thus making its data unavailable.This method is called when a previously created loader is being reset, thus making its data unavailable. This callback lets you find out when the data is about to be released so you can remove your reference to it.This implementation calls ``swapCursor()`` with a value of ``null``.

## Tasks and Back Stack
A task is a collection of activities that users interact with when performing a certain job. The activities are arranged in a stack (the "back stack"), in the order in which each activity is opened.

The ``launchMode`` attribute specifies an instruction on how the activity should be launched into a task. There are four different launch modes you can assign to the launchMode attribute:

+ ``standard``:Default. The system creates a new instance of the activity in the task from which it was started and routes the intent to it. The activity can be instantiated multiple times, each instance can belong to different tasks, and one task can have multiple instances.
+ ``singleTop``:If an instance of the activity already exists at the top of the current task, the system routes the intent to that instance through a call to its ``onNewIntent()`` method, rather than creating a new instance of the activity. The activity can be instantiated multiple times, each instance can belong to different tasks, and one task can have multiple instances (but only if the activity at the top of the back stack is not an existing instance of the activity).For example, suppose a task's back stack consists of root activity A with activities B, C, and D on top (the stack is A-B-C-D; D is on top). An intent arrives for an activity of type D. If D has the default "standard" launch mode, a new instance of the class is launched and the stack becomes A-B-C-D-D. However, if D's launch mode is "singleTop", the existing instance of D receives the intent through ``onNewIntent()``, because it's at the top of the stack—the stack remains A-B-C-D. However, if an intent arrives for an activity of type B, then a new instance of B is added to the stack, even if its launch mode is "singleTop".Note: When a new instance of an activity is created, the user can press the Back button to return to the previous activity. But when an existing instance of an activity handles a new intent, the user cannot press the Back button to return to the state of the activity before the new intent arrived in ``onNewIntent()``.
+ ``singleTask``:The system creates a new task and instantiates the activity at the root of the new task. However, if an instance of the activity already exists in a separate task, the system routes the intent to the existing instance through a call to its ``onNewIntent()`` method, rather than creating a new instance. Only one instance of the activity can exist at a time.Although the activity starts in a new task, the Back button still returns the user to the previous activity.
+ ``singleInstance``:Same as "singleTask", except that the system doesn't launch any other activities into the task holding the instance. The activity is always the single and only member of its task; any activities started by this one open in a separate task.
+ ``FLAG_ACTIVITY_NEW_TASK``:Start the activity in a new task. If a task is already running for the activity you are now starting, that task is brought to the foreground with its last state restored and the activity receives the new intent in ``onNewIntent()``.Behave as ``singleTask``.
+ ``FLAG_ACTIVITY_SINGLE_TOP``:If the activity being started is the current activity (at the top of the back stack), then the existing instance receives a call to ``onNewIntent()``, instead of creating a new instance of the activity.Behave as ``singleTop``.
+ ``FLAG_ACTIVITY_CLEAR_TOP``:If the activity being started is already running in the current task, then instead of launching a new instance of that activity, all of the other activities on top of it are destroyed and this intent is delivered to the resumed instance of the activity (now on top), through ``onNewIntent()``).``FLAG_ACTIVITY_CLEAR_TOP`` is most often used in conjunction with ``FLAG_ACTIVITY_NEW_TASK``. When used together, these flags are a way of locating an existing activity in another task and putting it in a position where it can respond to the intent.

The ``affinity`` indicates which task an activity prefers to belong to. By default, all the activities from the same application have an affinity for each other. So, by default, all activities in the same application prefer to be in the same task. However, you can modify the default affinity for an activity. Activities defined in different applications can share an affinity, or activities defined in the same application can be assigned different task affinities.

When an activity has its ``allowTaskReparenting`` attribute set to "true".In this case, the activity can move from the task it starts to the task it has an affinity for, when that task comes to the foreground.For example, suppose that an activity that reports weather conditions in selected cities is defined as part of a travel application. It has the same affinity as other activities in the same application (the default application affinity) and it allows re-parenting with this attribute. When one of your activities starts the weather reporter activity, it initially belongs to the same task as your activity. However, when the travel application's task comes to the foreground, the weather reporter activity is reassigned to that task and displayed within it.Tip: If an .apk file contains more than one "application" from the user's point of view, you probably want to use the taskAffinity attribute to assign different affinities to the activities associated with each "application".

If the user leaves a task for a long time, the system clears the task of all activities except the root activity. When the user returns to the task again, only the root activity is restored. The system behaves this way, because, after an extended amount of time, users likely have abandoned what they were doing before and are returning to the task to begin something new.There are some activity attributes that you can use to modify this behavior:
+ ``alwaysRetainTaskState``:If this attribute is set to "true" in the root activity of a task, the default behavior just described does not happen. The task retains all activities in its stack even after a long period.
+ ``clearTaskOnLaunch``:If this attribute is set to "true" in the root activity of a task, the stack is cleared down to the root activity whenever the user leaves the task and returns to it. In other words, it's the opposite of ``alwaysRetainTaskState``. The user always returns to the task in its initial state, even after a leaving the task for only a moment.
+ ``finishOnTaskLaunch``:This attribute is like ``clearTaskOnLaunch``, but it operates on a single activity, not an entire task. It can also cause any activity to go away, including the root activity. When it's set to "true", the activity remains part of the task only for the current session. If the user leaves and then returns to the task, it is no longer present.

You can set up an activity as the entry point for a task by giving it an intent filter with ``android.intent.action.MAIN`` as the specified action and ``android.intent.category.LAUNCHER`` as the specified category. This second ability is important: Users must be able to leave a task and then come back to it later using this activity launcher. For this reason, the two launch modes that mark activities as always initiating a task, ``singleTask`` and ``singleInstance``, should be used only when the activity has an ``ACTION_MAIN`` and a ``CATEGORY_LAUNCHER`` filter. 

#Services
Service can work both ways—it can be started (to run indefinitely) and also allow binding. It's simply a matter of whether you implement a couple callback methods: ``onStartCommand()`` to allow components to start it and ``onBind()`` to allow binding.

Traditionally, there are two classes you can extend to create a started service:

+ ``Service``:This is the base class for all services. When you extend this class, it's important that you create a new thread in which to do all the service's work, because the service uses your application's main thread, by default, which could slow the performance of any activity your application is running.
+ ``IntentService``:This is a subclass of ``Service`` that uses a worker thread to handle all start requests, one at a time. This is the best option if you don't require that your service handle multiple requests simultaneously. All you need to do is implement ``onHandleIntent()``, which receives the intent for each start request so you can do the background work.

The ``IntentService`` does the following:

+ Creates a default worker thread that executes all intents delivered to ``onStartCommand()`` separate from your application's main thread.
+ Creates a work queue that passes one intent at a time to your ``onHandleIntent()`` implementation, so you never have to worry about multi-threading.
+ Stops the service after all start requests have been handled, so you never have to call ``stopSelf()``.
+ Provides default implementation of ``onBind()`` that returns null.
+ Provides a default implementation of ``onStartCommand()`` that sends the intent to the work queue and then to your ``onHandleIntent()`` implementation.

Notice that the ``onStartCommand()`` method must return an integer. The integer is a value that describes how the system should continue the service in the event that the system kills it (as discussed above, the default implementation for ``IntentService`` handles this for you, though you are able to modify it). The return value from ``onStartCommand()`` must be one of the following constants:
+ ``START_NOT_STICKY``:If the system kills the service after ``onStartCommand()`` returns, **do not** recreate the service, unless there are pending intents to deliver. This is the safest option to avoid running your service when not necessary and when your application can simply restart any unfinished jobs.
+ ``START_STICKY``:If the system kills the service after ``onStartCommand()`` returns, recreate the service and call ``onStartCommand()``, but do not redeliver the last intent. Instead, the system calls ``onStartCommand()`` with a null intent, unless there were pending intents to start the service, in which case, those intents are delivered. This is suitable for media players (or similar services) that are not executing commands, but running indefinitely and waiting for a job.
+ ``START_REDELIVER_INTENT``:If the system kills the service after ``onStartCommand()`` returns, recreate the service and call ``onStartCommand()``with the last intent that was delivered to the service. Any pending intents are delivered in turn. This is suitable for services that are actively performing a job that should be immediately resumed, such as downloading a file.

The Android system will force-stop a service only when memory is low and it must recover system resources for the activity that has user focus. If the service is bound to an activity that has user focus, then it's less likely to be killed, and if the service is declared to run in the foreground (discussed later), then it will almost never be killed. Otherwise, if the service was started and is long-running, then the system will lower its position in the list of background tasks over time and the service will become highly susceptible to killing—if your service is started, then you must design it to gracefully handle restarts by the system. If the system kills your service, it restarts it as soon as resources become available again (though this also depends on the value you return from ``onStartCommand()``).

To ensure your app is secure, **always use an explicit intent when starting or binding your Service and do not declare intent filters for the service**. If it's critical that you allow for some amount of ambiguity as to which service starts, you can supply intent filters for your services and exclude the component name from the Intent, but you then must set the package for the intent with ``setPackage()``, which provides sufficient disambiguation for the target service.Additionally, you can ensure that your service is available to only your app by including the ``android:exported`` attribute and setting it to ``false``. This effectively stops other apps from starting your service, even when using an explicit intent.

A services runs in the **same** process as the application in which it is declared and in the **main thread** of that application, by default. So, if your service performs intensive or blocking operations while the user interacts with an activity from the same application, the service will slow down activity performance. To avoid impacting application performance, you should start a new thread inside the service.

The ``startService()`` method returns immediately and the Android system calls the service's ``onStartCommand()`` method. If the service is not already running, the system first calls ``onCreate()``, then calls ``onStartCommand()``.If the service does not also provide binding, the intent delivered with ``startService()`` is the only mode of communication between the application component and the service. However, if you want the service to send a result back, then the client that starts the service can create a ``PendingIntent`` for a broadcast (with ``getBroadcast()``) and deliver it to the service in the ``Intent`` that starts the service. The service can then use the broadcast to deliver a result.Multiple requests to start the service result in multiple corresponding calls to the service's ``onStartCommand()``. However, only one request to stop the service (with ``stopSelf()`` or ``stopService()``) is required to stop it.

A started service must manage its own lifecycle. That is, the system does not stop or destroy the service unless it must recover system memory and the service continues to run after ``onStartCommand()`` returns. So, the service must stop itself by calling ``stopSelf()`` or another component can stop it by calling ``stopService()``.Once requested to stop with ``stopSelf()`` or ``stopService()``, the system destroys the service as soon as possible.However, if your service handles multiple requests to ``onStartCommand()`` concurrently, then you shouldn't stop the service when you're done processing a start request, because you might have since received a new start request (stopping at the end of the first request would terminate the second one). To avoid this problem, you can use ``stopSelf(int)`` to ensure that your request to stop the service is always based on the most recent start request. That is, when you call ``stopSelf(int)``, you pass the ID of the start request (the startId delivered to ``onStartCommand()``) to which your stop request corresponds. Then if the service received a new start request before you were able to call ``stopSelf(int)``, then the ID will not match and the service will not stop.如果service需要同时相应多个对``onStartCommand``的请求，可以使用``stopSelf(int)``来停止一个service，将传递给``onStartCommand``的startId传递给``stopSelf``，这样如果有新的对``onStartCommand``的调用，startId将不会匹配，``stopSelf``就不会调用成功。

It's important that your application stops its services when it's done working, to avoid wasting system resources and consuming battery power. If necessary, other components can stop the service by calling ``stopService()``. Even if you enable binding for the service, you must always stop the service yourself if it ever received a call to ``onStartCommand()``.Service done working时需要将其停止，即使一个bind service，如果调用它的``onStartCommand``，也需要显式的将其停止。

You should create a bound service when you want to interact with the service from activities and other components in your application or to expose some of your application's functionality to other applications, through interprocess communication (IPC).需要与其他组件进行交互的时候可以创建bound service。

Once running, a service can notify the user of events using ``Toast Notifications`` or ``Status Bar Notifications``.一个service开始运行，可以通过``Toast``或者``StatusBar Notifications``来和用户进行交互。

A foreground service is a service that's considered to be something the user is actively aware of and thus not a candidate for the system to kill when low on memory. A foreground service must provide a notification for the status bar, which is placed under the "Ongoing" heading, which means that the notification cannot be dismissed unless the service is either stopped or removed from the foreground.

To request that your service run in the foreground, call ``startForeground()``. This method takes two parameters: an integer that uniquely identifies the notification and the ``Notification`` for the status bar.``startForeground(ONGOING_NOTIFICATION_ID, notification);``,The integer ID you give to ``startForeground()`` must not be 0.To remove the service from the foreground, call ``stopForeground()``. This method takes a boolean, indicating whether to remove the status bar notification as well. This method does not stop the service. However, if you stop the service while it's still running in the foreground, then the notification is also removed.

Started Service:The service is created when another component calls ``startService()``. The service then runs indefinitely and must stop itself by calling ``stopSelf()``. Another component can also stop the service by calling ``stopService()``. When the service is stopped, the system destroys it.

Bound Service:The service is created when another component (a client) calls ``bindService()``. The client then communicates with the service through an ``IBinder`` interface. The client can close the connection by calling ``unbindService()``. Multiple clients can bind to the same service and when all of them unbind, the system destroys the service. (The service does **not** need to stop itself.)

These two paths are not entirely separate. That is, you can bind to a service that was already started with ``startService()``. For example, a background music service could be started by calling ``startService()`` with an ``Intent`` that identifies the music to play. Later, possibly when the user wants to exercise some control over the player or get information about the current song, an activity can bind to the service by calling ``bindService()``. In cases like this, ``stopService()`` or ``stopSelf()`` does not actually stop the service until all clients unbind.

The active lifetime of a service begins with a call to either ``onStartCommand()`` or ``onBind()``. Each method is handed the ``Intent`` that was passed to either ``startService()`` or ``bindService()``, respectively.If the service is started, the active lifetime ends the same time that the entire lifetime ends (the service is still active even after ``onStartCommand()`` returns). If the service is bound, the active lifetime ends when ``onUnbind()`` returns.Although a started service is stopped by a call to either ``stopSelf()`` or ``stopService()``, there is not a respective callback for the service (there's no ``onStop()`` callback). So, unless the service is bound to a client, the system destroys it when the service is stopped—``onDestroy()`` is the only callback received.

Keep in mind that any service, no matter how it's started, can potentially allow clients to bind to it. So, a service that was initially started with ``onStartCommand()`` (by a client calling ``startService()``) can still receive a call to ``onBind()`` (when a client calls ``bindService()``).

A bound service is the server in a client-server interface. A bound service allows components (such as activities) to bind to the service, send requests, receive responses, and even perform interprocess communication (IPC). A bound service typically lives only while it serves another application component and does not run in the background indefinitely.Bound Service通常在为其他application component client提供server时存活，如果所有bind到这个service的component client unbind了，这个service就会被系统destory。A bound service is an implementation of the Service class that allows other applications to bind to it and interact with it. To provide binding for a service, you must implement the ``onBind()`` callback method. This method returns an ``IBinder`` object that defines the programming interface that clients can use to interact with the service.

If you do allow your service to be started and bound, then when the service has been started, the system does not destroy the service when all clients unbind. Instead, you must explicitly stop the service, by calling ``stopSelf()`` or ``stopService()``.如果一个service同时实现了``onStartCommand``和``onBind``也即可以改同时被start或者bind，当所有bind到这个service的component client unbind后，必须显式stop这个service，否则系统不会销毁这个service。

Although you should usually implement either ``onBind()`` or ``onStartCommand()``, it's sometimes necessary to implement both. For example, a music player might find it useful to allow its service to run indefinitely and also provide binding. This way, an activity can start the service to play some music and the music continues to play even if the user leaves the application. Then, when the user returns to the application, the activity can bind to the service to regain control of playback.

A client can bind to the service by calling ``bindService()``. When it does, it must provide an implementation of ``ServiceConnection``, which monitors the connection with the service. The ``bindService()`` method returns immediately without a value, but when the Android system creates the connection between the client and service, it calls ``onServiceConnected()`` on the ``ServiceConnection``, to deliver the ``IBinder`` that the client can use to communicate with the service.一个component client如果想要bind到一个service上，必须提供``ServiceConnection``实现。调用``bindService()``方法时会立即返回，但是系统在创建client和service之间的connection时，会调用``ServiceConnection``上的``onServiceConnected``方法。Multiple clients can connect to the service at once. However, the system calls your service's ``onBind()`` method to retrieve the ``IBinder`` only when the first client binds. The system then delivers the same ``IBinder`` to any additional clients that bind, without calling ``onBind()`` again.多个client可以同时bind到一个service上，系统只在第一个client bind到service时调用``onBind``方法来获取``IBinder``，然后把这个``IBinder``传递给其他的component client。

When creating a service that provides binding, you must provide an ``IBinder`` that provides the programming interface that clients can use to interact with the service. There are three ways you can define the interface:

+ ``Extending the Binder class``:If your service is private to your own application and runs in the same process as the client (which is common), you should create your interface by extending the ``Binder`` class and returning an instance of it from ``onBind()``. The client receives the ``Binder`` and can use it to directly access public methods available in either the ``Binder`` implementation or even the ``Service``.This is the preferred technique when your service is merely a background worker for your own application. The only reason you would not create your interface this way is because your service is used by other applications or across separate processes.
+ ``Using a Messenger``:If you need your interface to work across different processes, you can create an interface for the service with a ``Messenger``. In this manner, the service defines a ``Handler`` that responds to different types of ``Message`` objects. This ``Handler`` is the basis for a ``Messenger`` that can then share an ``IBinder`` with the client, allowing the client to send commands to the service using ``Message`` objects. Additionally, the client can define a ``Messenger`` of its own so the service can send messages back.This is the simplest way to perform interprocess communication (IPC), because the ``Messenger`` queues all requests into a single thread so that you don't have to design your service to be thread-safe.
+ ``AIDL``:AIDL (Android Interface Definition Language) performs all the work to decompose objects into primitives that the operating system can understand and marshall them across processes to perform IPC.Using a ``Messenger``, is actually based on AIDL as its underlying structure. As mentioned above, the ``Messenger`` creates a queue of all the client requests in a single thread, so the service receives requests one at a time. If, however, you want your service to handle multiple requests simultaneously, then you can use ``AIDL`` directly. In this case, your service must be capable of multi-threading and be built thread-safe.Most applications should not use AIDL to create a bound service, because it may require multithreading capabilities and can result in a more complicated implementation.

#Content Providers
Content providers manage access to a structured set of data. They encapsulate the data, and provide mechanisms for defining data security. Content providers are the standard interface that connects data in one process with code running in another process.

When you want to access data in a content provider, you use the ``ContentResolver`` object in your application's Context to communicate with the provider as a client. The ``ContentResolver`` object communicates with the provider object, an instance of a class that implements ``ContentProvider``. The provider object receives data requests from clients, performs the requested action, and returns the results.

Together, providers and provider clients offer a consistent, standard interface to data that also handles inter-process communication and secure data access.

A provider isn't required to have a primary key, and it isn't required to use ``_ID`` as the column name of a primary key if one is present. However, if you want to bind data from a provider to a ``ListView``, one of the column names has to be ``_ID``. 

The ``ContentResolver`` object in the client application's process and the ``ContentProvider`` object in the application that owns the provider automatically handle inter-process communication. ``ContentProvider`` also acts as an abstraction layer between its repository of data and the external appearance of data as tables.

``CONTENT_URI`` contains the content URI of the user dictionary's "words" table. The ``ContentResolver`` object parses out the URI's authority, and uses it to "resolve" the provider by comparing the authority to a system table of known providers. The ``ContentResolver`` can then dispatch the query arguments to the correct provider.The ``ContentProvider`` uses the path part of the content URI to choose the table to access. A provider usually has a path for each table it exposes.

The ``Uri`` and ``Uri.Builder`` classes contain convenience methods for constructing well-formed ``Uri`` objects from strings. The ``ContentUris`` contains convenience methods for appending id values to a ``URI``. You can use  ``withAppendedId()`` to append an id to the UserDictionary content URI.

To back a ``ListView`` with a ``Cursor``, the cursor must contain a column named ``_ID``. Because of this, the query shown previously retrieves the ``_ID`` column for the "words" table, even though the ``ListView`` doesn't display it. This restriction also explains why most providers have a ``_ID`` column for each of their tables.

A provider's application can specify permissions that other applications must have in order to access the provider's data. These permissions ensure that the user knows what data an application will try to access. Based on the provider's requirements, other applications request the permissions they need in order to access the provider. End users see the requested permissions when they install the application.If a provider's application doesn't specify any permissions, then other applications have no access to the provider's data. However, components in the provider's application always have full read and write access, regardless of the specified permissions.

## Inserting data

```java
// Defines a new Uri object that receives the result of the insertion
Uri mNewUri;

...

// Defines an object to contain the new values to insert
ContentValues mNewValues = new ContentValues();

/*
 * Sets the values of each column and inserts the word. The arguments to the "put"
 * method are "column name" and "value"
 */
mNewValues.put(UserDictionary.Words.APP_ID, "example.user");
mNewValues.put(UserDictionary.Words.LOCALE, "en_US");
mNewValues.put(UserDictionary.Words.WORD, "insert");
mNewValues.put(UserDictionary.Words.FREQUENCY, "100");

mNewUri = getContentResolver().insert(
    UserDictionary.Word.CONTENT_URI,   // the user dictionary content URI
    mNewValues                          // the values to insert
);
```

The data for the new row goes into a single ``ContentValues`` object, which is similar in form to a one-row cursor. The columns in this object don't need to have the same data type, and if you don't want to specify a value at all, you can set a column to ``null`` using ``ContentValues.putNull()``.To get the value of ``_ID`` from the returned ``Uri``, call ``ContentUris.parseId()``.

## Updating data

```java
// Defines an object to contain the updated values
ContentValues mUpdateValues = new ContentValues();

// Defines selection criteria for the rows you want to update
String mSelectionClause = UserDictionary.Words.LOCALE +  "LIKE ?";
String[] mSelectionArgs = {"en_%"};

// Defines a variable to contain the number of updated rows
int mRowsUpdated = 0;

...

/*
 * Sets the updated value and updates the selected words.
 */
mUpdateValues.putNull(UserDictionary.Words.LOCALE);

mRowsUpdated = getContentResolver().update(
    UserDictionary.Words.CONTENT_URI,   // the user dictionary content URI
    mUpdateValues                       // the columns to update
    mSelectionClause                    // the column to select on
    mSelectionArgs                      // the value to compare to
);
```

## Deleting data

```java
// Defines selection criteria for the rows you want to delete
String mSelectionClause = UserDictionary.Words.APP_ID + " LIKE ?";
String[] mSelectionArgs = {"user"};

// Defines a variable to contain the number of rows deleted
int mRowsDeleted = 0;

...

// Deletes the words that match the selection criteria
mRowsDeleted = getContentResolver().delete(
    UserDictionary.Words.CONTENT_URI,   // the user dictionary content URI
    mSelectionClause                    // the column to select on
    mSelectionArgs                      // the value to compare to
);
```

## Provider Data Types
The providers offer the following data formats: integer, long integer(long), floating point, long floating point. Another data type that providers often use is Binary Large OBject (BLOB) implemented as a 64KB byte array. Providers also maintain MIME data type information for each content ``URI`` they define. You can use the MIME type information to find out if your application can handle data that the provider offers, or to choose a type of handling based on the MIME type. You usually need the MIME type when you are working with a provider that contains complex data structures or files. 

Three alternative forms of provider access are important in application development:

+ ``Batch access``: You can create a batch of access calls with methods in the ``ContentProviderOperation`` class, and then apply them with ``ContentResolver.applyBatch()``.
+ ``Asynchronous queries``: You should do queries in a separate thread. One way to do this is to use a ``CursorLoader`` object. The examples in the ``Loaders`` guide demonstrate how to do this.
+ ``Data access via intents``: Although you can't send an intent directly to a provider, you can send an intent to the provider's application, which is usually the best-equipped to modify the provider's data.

Batch access to a provider is useful for inserting a large number of rows, or for inserting rows in multiple tables in the same method call, or in general for performing a set of operations across process boundaries as a transaction (an atomic operation).To access a provider in "batch mode", you create an array of ``ContentProviderOperation`` objects and then dispatch them to a content provider with ``ContentResolver.applyBatch()``. You pass the content provider's authority to this method, rather than a particular content ``URI``. This allows each ``ContentProviderOperation`` object in the array to work against a different table. A call to ``ContentResolver.applyBatch()`` returns an array of results.

Intents can provide indirect access to a content provider. You allow the user to access data in a provider even if your application doesn't have access permissions, either by getting a result intent back from an application that has permissions, or by activating an application that has permissions and letting the user do work in it.

Getting access with temporary permissions,You can access data in a content provider, even if you don't have the proper access permissions, by sending an intent to an application that does have the permissions and receiving back a result intent containing "URI" permissions. These are permissions for a specific content URI that last until the activity that receives them is finished. The application that has permanent permissions grants temporary permissions by setting a flag in the result intent:``FLAG_GRANT_READ_URI_PERMISSION``,``FLAG_GRANT_WRITE_URI_PERMISSION``,These flags don't give general read or write access to the provider whose authority is contained in the content URI. The access is only for the URI itself.A provider defines URI permissions for content URIs in its manifest, using the ``android:grantUriPermission`` attribute of the ``<provider>`` element, as well as the ``<grant-uri-permission>`` child element of the ``<provider>`` element.

MIME types have the format ``type/subtype``,For example, the well-known MIME type ``text/html`` has the text type and the html subtype.Custom MIME type strings, also called "vendor-specific" MIME types, have more complex type and subtype values. The type value is always ``vnd.android.cursor.dir`` for multiple rows, or ``vnd.android.cursor.item`` for a single row.

## Creating a Content Provider
You need to build a content provider if you want to provide one or more of the following features:
+ You want to offer complex data or files to other applications.
+ You want to allow users to copy complex data from your app into other apps.
+ You want to provide custom search suggestions using the search framework.

### Design the raw storage for your data.

+ File data:Data that normally goes into files, such as photos, audio, or videos. Store the files in your application's private space.

+ Structured Data:Data that normally goes into a database, array, or similar structure. Store the data in a form that's compatible with tables of rows and columns. A row represents an entity, such as a person or an item in inventory. A column represents some data for the entity, such a person's name or an item's price. A common way to store this type of data is in an SQLite database, but you can use any type of persistent storage.

+ The Android system includes an ``SQLite`` database API that Android's own providers use to store table-oriented data. The ``SQLiteOpenHelper`` class helps you create databases, and the ``SQLiteDatabase`` class is the base class for accessing databases.

+ For storing file data, Android has a variety of file-oriented APIs.For working with network-based data, use classes in ``java.net`` and ``android.net``. You can also synchronize network-based data to a local data store such as a database, and then offer the data as tables or files. 

+ Table data should always have a "primary key" column that the provider maintains as a unique numeric value for each row. If you want to provide bitmap images or other very large pieces of file-oriented data, store the data in a file and then provide it indirectly rather than storing it directly in a table.Use the Binary Large OBject (BLOB) data type to store data that varies in size or has a varying structure. For example, you can use a BLOB column to store a [protocol buffer](http://code.google.com/p/protobuf) or JSON structure.
+ A content URI is a URI that identifies data in a provider. Content URIs include the symbolic name of the entire provider (its authority) and a name that points to a table or file (a path). The optional id part points to an individual row in a table. 

### Define a concrete implementation of the ContentProvider class and its required methods. This class is the interface between your data and the rest of the Android system. 

### Define the provider's authority string, its content URIs, and column names. If you want the provider's application to handle intents, also define intent actions, extras data, and flags. Also define the permissions that you will require for applications that want to access your data. You should consider defining all of these values as constants in a separate contract class; later, you can expose this class to other developers. 

### Add other optional pieces, such as sample data or an implementation of ``AbstractThreadedSyncAdapter`` that can synchronize data between the provider and cloud-based data.

@@Todo:SampleSyncAdapter

#App Widgets

#Processes and Threads

<table summary="Priorities" border="1">
<thead>
    <tr>
     <th align="left">Process status</th>
     <th align="left">Description</th>
     <th align="left">Priority</th>
 </tr>
</thead>
<tbody>
    <tr>
     <td align="left">Foreground</td>
     <td align="left">
      An application in which the user is interacting with an
      activity, or which has an service which is bound to such an
      activity. Also if a service is executing one of its lifecycle
      methods or a broadcast receiver which runs its
      <code class="code">onReceive()</code>
      method.

  </td>
  <td align="left">1</td>
</tr>
<tr>
 <td align="left">Visible</td>
 <td align="left">User is not interacting with the activity, but the activity
  is still (partially) visible or the application has a service
  which
  is used by a inactive but visible activity.

</td>
<td align="left">2</td>
</tr>
<tr>
 <td align="left">Service</td>
 <td align="left">Application with a running service which does not qualify
  for 1 or 2.

</td>
<td align="left">3</td>
</tr>
<tr>
 <td align="left">Background</td>
 <td align="left">Application with only stopped activities and without a
  service or executing receiver. Android keeps them in
  a least
  recent
  used (LRU) list and if requires terminates the one which
  was least used.

</td>
<td align="left">4</td>
</tr>
<tr>
 <td align="left">Empty</td>
 <td align="left">Application without any active components.

 </td>
 <td align="left">5</td>
</tr>
</tbody>
</table>