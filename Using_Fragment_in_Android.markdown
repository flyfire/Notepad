Using Fragment in Android
==========================
> [reference](https://developer.android.com/reference/android/app/Fragment.html)
> [tutorial](http://www.vogella.com/articles/AndroidFragments/article.html)

#reference
+ A ``Fragment`` is a piece of an application's user interface or behavior that can be placed in an ``Activity``. Interaction with fragments is done through ``FragmentManager``, which can be obtained via ``Activity.getFragmentManager()`` and ``Fragment.getFragmentManager()``.


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