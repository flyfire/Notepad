Using Fragment in Android
==========================
> [reference](https://developer.android.com/reference/android/app/Fragment.html)
> [tutorial](http://www.vogella.com/articles/AndroidFragments/article.html)

#reference
+ A ``Fragment`` is a piece of an application's user interface or behavior that can be placed in an ``Activity``. Interaction with fragments is done through ``FragmentManager``, which can be obtained via ``Activity.getFragmentManager()`` and ``Fragment.getFragmentManager()``.
+ All subclasses of Fragment must include a public empty constructor. The framework will often re-instantiate a fragment class when needed, in particular during state restore, and needs to be able to find this constructor to instantiate it. If the empty constructor is not available, a runtime exception will occur in some cases during state restore.所有``Fragment``的子类必须实现一个空的public的构造方法，方便在activity的状态改变的时候重新实例化fragment。
+ landscape是横向，portrait是纵向
+ Lifecycle
	+ The core series of lifecycle methods that are called to bring a fragment up to resumed state (interacting with the user) are:将fragment带到前台与用户交互的方法
		+ ``onAttach(Activity)`` called once the fragment is associated with its activity.
		+ ``onCreate(Bundle)`` called to do initial creation of the fragment.
		+ ``onCreateView(LayoutInflater, ViewGroup, Bundle)`` creates and returns the view hierarchy associated with the fragment.
		+ ``onActivityCreated(Bundle)`` tells the fragment that its activity has completed its own ``Activity.onCreate()``.
		+ ``onViewStateRestored(Bundle)`` tells the fragment that all of the saved state of its view hierarchy has been restored.
		+ ``onStart()`` makes the fragment visible to the user (based on its containing activity being started).
		+ ``onResume()`` makes the fragment interacting with the user (based on its containing activity being resumed).
	+ As a fragment is no longer being used, it goes through a reverse series of callbacks:fragment进入后台的方法
		+ ``onPause()`` fragment is no longer interacting with the user either because its activity is being paused or a fragment operation is modifying it in the activity.
		+ ``onStop()`` fragment is no longer visible to the user either because its activity is being stopped or a fragment operation is modifying it in the activity.
		+ ``onDestroyView()`` allows the fragment to clean up resources associated with its View.
		+ ``onDestroy()`` called to do final cleanup of the fragment's state.
		+ ``onDetach()`` called immediately prior to the fragment no longer being associated with its activity.


#tutorial
+ Check if the ``fragment`` is already part of your layout you can use the ``FragmentManager`` class

```
DetailFragment fragment = (DetailFragment) getFragmentManager().
   findFragmentById(R.id.detail_frag);
if (fragment==null || ! fragment.isInLayout()) {
  // start new Activity
  }
else {
  fragment.update(...);
}
```