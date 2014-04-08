Traceview War Story
======================
[refs](http://android-developers.blogspot.com/2010/10/traceview-war-story.html)
+ turn on/off traceview
```java
public void run() {
	android.os.Debug.startMethodTracing("lsd");
	// ... method body elided
	android.os.Debug.stopMethodTracing();
}
```
The first call turns tracing on, the argument "lsd" (stands for Life Saver Debug, of course) tells the system to put the trace log in ``/sdcard/lsd.trace``. Remember that doing this means you have to add the ``WRITE_EXTERNAL_STORAGE`` permission so you can save the trace info; don't forget to remove that before you ship.
```bash
adb pull /sdcard/lsd.trace
traceview lsd
```