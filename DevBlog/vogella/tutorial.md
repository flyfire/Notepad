The Android system installs every Android application with a unique user and group ID. Each application file is private to this generated user, e.g., other applications cannot access these files. In addition each Android application is started in its own process.

Therefore, by means of the underlying Linux kernel, every Android application is isolated from other running applications.

If data should be shared, the application must do this explicitly via an Android component which handles the sharing of the data, e.g., via a service or a content provider.

Android device emulator shortcuts:``Alt+Enter``,Maximizes the emulator,``Ctrl+F11``,Changes the orientation of the emulator from landscape to portrait and vice versa,``F8``,Turns the network on and off.


An AVD created for Android contains the programs from the Android Open Source Project. An AVD created for the Google API's contains additional Google specific code.

AVDs created for the Google API allow you to test applications which use Google Play services, e.g., the new Google maps API or the new location services.

Instances of the class ``android.content.Context`` provide the connection to the Android system which executes the application. It also gives access to the resources of the project and the global information about the application environment.

A service performs tasks without providing an user interface. They can communicate with other Android components, for example, via broadcast receivers and notify the user via the notification framework in Android.

A content provider (provider) defines a structured interface to application data. A provider can be used for accessing data within one application, but can also be used to share data with other applications.

Android contains an SQLite database which is frequently used in conjunction with a content provider. The SQLite database would store the data, which would be accessed via the provider.

All activities, services and content provider components of the application must be statically declared in this file. Broadcast receiver can be defined statically in the manifest file or dynamically at runtime in the application.

The intent filter part in the Android manifest file, tells the Android runtime that this activity should be registered as a possible entry point into the application and made available in the launcher of the Android system. The action defines that it (android:name="android.intent.action.MAIN" ) can be started and the category android:name="android.intent.category.LAUNCHER" parameter tells the Android system to add the activity to the launcher.

Android also provides resources. These are called system resources. System resources are distinguished from local resources by the android namespace prefix. For example, android.R.string.cancel defines the platform string for a cancel operation.

The Resources class allows to access individual resources. An instance of the Resources class can be retrieved via the ``getResources()`` method of the ``Context`` class. As activities and services extend the ``Context`` class, you can directly use this method in implementations of these components.

```java
// BitmapFactory requires an instance of the Resource class
BitmapFactory.decodeResource(getResources(), R.drawable.ic_action_search); 
```

In your XML files, for example, your layout files, you can refer to other resources via the @ sign.For example, if you want to refer to a color, which is defined in an XML resource, you can refer to it via @color/your_id. Or if you defined a String with the "titlepage" key in an XML resource, you could access it via @string/titlepage.To use an Android system resource, include the android namespace into the references, e.g., ``android.R.string.cancel``.

While the ``res`` directory contains structured values which are known to the Android platform, the ``assets`` directory can be used to store any kind of data.You can access files stored in this folder based on their path. The ``assets`` directory also allows you to have sub-folders.

```java
// get the AssetManager
AssetManager manager = getAssets();
// read the "logo.png" bitmap from the assets folder
InputStream open = null;
try {
  open = manager.open("logo.png");
  Bitmap bitmap = BitmapFactory.decodeStream(open);
  // assign the bitmap to an ImageView in this layout
  ImageView view = (ImageView) findViewById(R.id.imageView1);
  view.setImageBitmap(bitmap);
  } catch (IOException e) {
    e.printStackTrace();
  } finally {
    if (open != null) {
      try {
        open.close();
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
} 
```
