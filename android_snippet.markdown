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


+ ``StringBuffer`` write to ``File``
```java
public static void writeToFile(String pFilename, StringBuffer pData) throws IOException {
	BufferedWriter out = new BufferedWriter(new FileWriter(pFilename));
	out.write(pData.toString());
	out.flush();
	out.close();
}
public static StringBuffer readFromFile(String pFilename) throws IOException {
	BufferedReader in = new BufferedReader(new FileReader(pFilename));
	StringBuffer data = new StringBuffer();
	int c = 0;
	while ((c = in.read()) != -1) {
		data.append((char)c);
	}
	in.close();
	return data;
}
```

+ 得到当前wifi的名称
```java
public static String getCurrentSsid(Context context) {
	String ssid = null;
	ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
	if (networkInfo.isConnected()) {
		final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
		if (connectionInfo != null && !StringUtil.isBlank(connectionInfo.getSSID())) {
			ssid = connectionInfo.getSSID();
		}
	}
	return ssid;
}

<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
```

```java
private void getAvailableWlanList(TextView tv)
{
	WifiManager wifiManager = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
	List<ScanResult> results = wifiManager.getScanResults();
	StringBuffer sb = new StringBuffer();

	if (results != null) {
		final int size = results.size();
		if (size == 0) {
			sb.append("No wifi available, check wireless is on.");
		}
		else {
			ScanResult bestSignal = results.get(0);
			int count = 1;
			int index = count;
			for (ScanResult scanResult : results) {
				sb.append(count++ + ". " + scanResult.SSID + " : " + scanResult.level + "\n"
						+ scanResult.BSSID + "\n" + scanResult.capabilities
						+ "\n====================\n");

				if (WifiManager.compareSignalLevel(bestSignal.level, scanResult.level) < 0) {
					bestSignal = scanResult;
					index = count - 1;
				}
			}
			sb.append("AP has strongest signal: " + index + " : " + bestSignal.SSID);
		}
	}
	tv.setText(sb.toString());
}
```

```java
public String getCurrentSsid(Context context) {

  String ssid = null;
  ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
  NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
  if (networkInfo.isConnected()) {
    final WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
    final WifiInfo connectionInfo = wifiManager.getConnectionInfo();
    if (connectionInfo != null && !(connectionInfo.getSSID().equals(""))) {
        //if (connectionInfo != null && !StringUtil.isBlank(connectionInfo.getSSID())) {
      ssid = connectionInfo.getSSID();
    }
 	// Get WiFi status MARAKANA
    WifiInfo info = wifiManager.getConnectionInfo();
    String textStatus = "";
    textStatus += "\n\nWiFi Status: " + info.toString();
    String BSSID = info.getBSSID();
    String MAC = info.getMacAddress();

    List<ScanResult> results = wifiManager.getScanResults();
    ScanResult bestSignal = null;
    int count = 1;
    String etWifiList = "";
    for (ScanResult result : results) {
        etWifiList += count++ + ". " + result.SSID + " : " + result.level + "\n" +
                result.BSSID + "\n" + result.capabilities +"\n" +
                "\n=======================\n";
    }
    Log.v(TAG, "from SO: \n"+etWifiList);

    // List stored networks
    List<WifiConfiguration> configs = wifiManager.getConfiguredNetworks();
    for (WifiConfiguration config : configs) {
        textStatus+= "\n\n" + config.toString();
    }
    Log.v(TAG,"from marakana: \n"+textStatus);
  }
  return ssid;
}
```
