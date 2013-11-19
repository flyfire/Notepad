Android Snippets
================
+ 检查网络是否连接
```java
ConnectivityManager cm =
        (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
boolean isConnected = activeNetwork != null &&
                      activeNetwork.isConnectedOrConnecting();
```
判断网络连接的类型
```java
boolean isWiFi = activeNetwork.getType() == ConnectivityManager.TYPE_WIFI;
```

```java
ConnectivityManager conMgr =  (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

if ( conMgr.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED
    ||  conMgr.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING  ) {


    //notify user you are online

}
else if ( conMgr.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED
    ||  conMgr.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {
    //notify user you are not online

}
```
ref:[ConnectivityManager](http://developer.android.com/reference/android/net/ConnectivityManager.html#TYPE_MOBILE) The constants 0 and 1 represent connection types and these two values are not exhaustive.
The ``ConnectivityManager`` broadcasts the ``CONNECTIVITY_ACTION`` ("android.net.conn.CONNECTIVITY_CHANGE") action whenever the connectivity details have changed. You can register a broadcast receiver in your manifest to listen for these changes and resume (or suspend) your background updates accordingly.``ConnectivityManager``会在网络连接发生变化时发送广播。Changes to a device's connectivity can be very frequent—this broadcast is triggered every time you move between mobile data and Wi-Fi. As a result, it's good practice to monitor this broadcast only when you've previously suspended updates or downloads in order to resume them. 只有在之前暂停了更新或下载并且想恢复它们的时候才应该监听这个广播，因为设备网络连接情况经常发生变化，广播会非常频繁。It's generally sufficient to simply check for Internet connectivity before beginning an update and, should there be none, suspend further updates until connectivity is restored.应该在更新前检查网络连接，并且在网络连接切换为mobile时停止更新，连接恢复为wifi时再进行更新。