Android Snippets
======================
+ ``register receiver``

```java
tcardFilter.addAction("android.intent.action.MEDIA_EJECT");
tcardFilter.addAction("android.intent.action.MEDIA_UNMOUNTED");
tcardFilter.addAction("android.intent.action.MEDIA_MOUNTED");
```

```java
IntentFilter filter = new IntentFilter(Intent.ACTION_MEDIA_MOUNTED);
filter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
filter.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL);
filter.addAction(Intent.ACTION_MEDIA_UNSHARED);
filter.addAction(Intent.ACTION_MEDIA_SHARED);
filter.addDataScheme("file");
```

```java
IntentFilter filter = new IntentFilter(Intent.ACTION_PACKAGE_ADDED);
filter.addAction(Intent.ACTION_PACKAGE_REMOVED);
filter.addAction(Intent.ACTION_MEDIA_MOUNTED);
filter.addAction(Intent.ACTION_MEDIA_UNMOUNTED);
filter.addAction(Intent.ACTION_MEDIA_SHARED);
filter.addAction(Intent.ACTION_MEDIA_BAD_REMOVAL);
filter.addDataScheme("file");
filter.addDataScheme("package");
registerReceiver(mReceiver, filter);
```

