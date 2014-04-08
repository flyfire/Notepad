Traceview War Story
======================
[refs](http://android-developers.blogspot.com/2010/10/traceview-war-story.html)

```java
public void run() {
	android.os.Debug.startMethodTracing("lsd");
	// ... method body elided
	android.os.Debug.stopMethodTracing();
}
```