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

+ PassDialog
```java
/**
 * Dialog displayed to request a password.
 */
public class PassDialog extends Dialog implements android.view.View.OnClickListener, android.view.View.OnKeyListener, OnCancelListener {
	private final Callback callback;
	private final EditText pass;
	/**
	 * Creates the dialog
	 * @param context context
	 * @param setting if true, indicates that we are setting a new password instead of requesting it.
	 * @param callback callback to receive the password entered (null if canceled)
	 */
	public PassDialog(Context context, boolean setting, Callback callback) {
		super(context);
		final View view = getLayoutInflater().inflate(R.layout.pass_dialog, null);
		((TextView)view.findViewById(R.id.pass_message)).setText(setting ? R.string.enternewpass : R.string.enterpass);
		((Button)view.findViewById(R.id.pass_ok)).setOnClickListener(this);
		((Button)view.findViewById(R.id.pass_cancel)).setOnClickListener(this);
		this.callback = callback;
		this.pass = (EditText) view.findViewById(R.id.pass_input);
		this.pass.setOnKeyListener(this);
		setTitle(setting ? R.string.pass_titleset : R.string.pass_titleget);
		setOnCancelListener(this);
		setContentView(view);
	}
	@Override
	public void onClick(View v) {
		final Message msg = new Message();
		if (v.getId() == R.id.pass_ok) {
			msg.obj = this.pass.getText().toString();
		}
		dismiss();
		this.callback.handleMessage(msg);
	}
	@Override
	public boolean onKey(View v, int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_ENTER) {
			final Message msg = new Message();
			msg.obj = this.pass.getText().toString();
			this.callback.handleMessage(msg);
			dismiss();
			return true;
		}
		return false;
	}
	@Override
	public void onCancel(DialogInterface dialog) {
		this.callback.handleMessage(new Message());
	}
}


处理PassDialog时，设置密码
new PassDialog(this, true, new android.os.Handler.Callback() {
	public boolean handleMessage(Message msg) {
		if (msg.obj != null) {
			setPassword((String)msg.obj);
		}
		return false;
	}
}).show();

验证密码
new PassDialog(this, false, new android.os.Handler.Callback() {
	public boolean handleMessage(Message msg) {
		//cancel
		if (msg.obj == null) {
			MainActivity.this.finish();
			android.os.Process.killProcess(android.os.Process.myPid());
			return false;
		}
		//ok
		if (!pwd.equals(msg.obj)) {
			requestPassword(pwd);
			return false;
		}
		// Password correct
		showOrLoadApplications();
		return false;
	}
}).show();
```

+ 弹出对话框
```java
@Override
public boolean onKeyDown(final int keyCode, final KeyEvent event) {
	// Handle the back button when dirty
	// 用户有输入，未保存，按下返回键
	if (keyCode == KeyEvent.KEYCODE_BACK) {
		final SharedPreferences prefs = getSharedPreferences(Api.PREFS_NAME, 0);
        if (script.getText().toString().equals(prefs.getString(Api.PREF_CUSTOMSCRIPT, ""))
                && script2.getText().toString().equals(prefs.getString(Api.PREF_CUSTOMSCRIPT2, ""))) {
            // Nothing has been changed, just return
            return super.onKeyDown(keyCode, event);
        }
        final DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    resultOk();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    // Propagate the event back to perform the desired action
                    CustomScriptActivity.super.onKeyDown(keyCode, event);
                    break;
                }
            }
        };
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.unsaved_changes).setMessage(R.string.unsaved_changes_message)
                .setPositiveButton(R.string.apply, dialogClickListener)
                .setNegativeButton(R.string.discard, dialogClickListener).show();
        // Say that we've consumed the event
        return true;
    }
    return super.onKeyDown(keyCode, event);
}
```

+ 联系人排序时根据地域
```java
new CursorLoader(getActivity(), baseUri,
        CONTACTS_SUMMARY_PROJECTION, select, null,
        Contacts.DISPLAY_NAME + " COLLATE LOCALIZED ASC");
```
+ 将``Activity``的``android:launchMode``设置为``singleTop``可以避免现有的不可见的Activity仍存在时创建新的Activity。
+ 判断是否有应用响应某个Intent
```java
public static boolean isIntentAvailable(Context context, String action) {
    final PackageManager packageManager = context.getPackageManager();
    final Intent intent = new Intent(action);
    List<ResolveInfo> list =
            packageManager.queryIntentActivities(intent,
                    PackageManager.MATCH_DEFAULT_ONLY);
    return list.size() > 0;
}
```
+ BinarySerach
```java
public int findIndexHinted(String iface, int uid, int set, int tag, int hintIndex)
{
    for (int offset = 0; offset < size; offset++) {
        final int halfOffset = offset / 2;

        // search outwards from hint index, alternating forward and backward
        final int i;
        if (offset % 2 == 0) {
            i = (hintIndex + halfOffset) % size;
        } else {
            i = (size + hintIndex - halfOffset - 1) % size;
        }

        if (uid == this.uid[i] && set == this.set[i] && tag == this.tag[i]
            && Objects.equal(iface, this.iface[i])) {
                return i;
            }
    }
    return -1;
}
```
```java
public int getIndexBefore(long time) {
    int index = Arrays.binarySearch(bucketStart, 0, bucketCount, time);
    if (index < 0) {
        index = (~index) - 1;
    } else {
        index -= 1;
    }
    return MathUtils.constrain(index, 0, bucketCount - 1);
}
```
```java
public int getIndexAfter(long time) {
    int index = Arrays.binarySearch(bucketStart, 0, bucketCount, time);
    if (index < 0) {
        index = ~index;
    } else {
        index += 1;
    }
    return MathUtils.constrain(index, 0, bucketCount - 1);
}
```

+ 自动扩容
```java
/**
 * Ensure that buckets exist for given time range, creating as needed.
 */
 private void ensureBuckets(long start, long end) {
    // normalize incoming range to bucket boundaries
    start -= start % bucketDuration;
    end += (bucketDuration - (end % bucketDuration)) % bucketDuration;

    for (long now = start; now < end; now += bucketDuration) {
        // try finding existing bucket
        final int index = Arrays.binarySearch(bucketStart, 0, bucketCount, now);
        if (index < 0) {
            // bucket missing, create and insert
            insertBucket(~index, now);
        }
    }
}
/**
 * Insert new bucket at requested index and starting time.
 */
 private void insertBucket(int index, long start) {
    // create more buckets when needed
    if (bucketCount >= bucketStart.length) {
        final int newLength = Math.max(bucketStart.length, 10) * 3 / 2;
        bucketStart = Arrays.copyOf(bucketStart, newLength);
        if (activeTime != null) activeTime = Arrays.copyOf(activeTime, newLength);
        if (rxBytes != null) rxBytes = Arrays.copyOf(rxBytes, newLength);
        if (rxPackets != null) rxPackets = Arrays.copyOf(rxPackets, newLength);
        if (txBytes != null) txBytes = Arrays.copyOf(txBytes, newLength);
        if (txPackets != null) txPackets = Arrays.copyOf(txPackets, newLength);
        if (operations != null) operations = Arrays.copyOf(operations, newLength);
    }

    // create gap when inserting bucket in middle
    if (index < bucketCount) {
        final int dstPos = index + 1;
        final int length = bucketCount - index;

        System.arraycopy(bucketStart, index, bucketStart, dstPos, length);
        if (activeTime != null) System.arraycopy(activeTime, index, activeTime, dstPos, length);
        if (rxBytes != null) System.arraycopy(rxBytes, index, rxBytes, dstPos, length);
        if (rxPackets != null) System.arraycopy(rxPackets, index, rxPackets, dstPos, length);
        if (txBytes != null) System.arraycopy(txBytes, index, txBytes, dstPos, length);
        if (txPackets != null) System.arraycopy(txPackets, index, txPackets, dstPos, length);
        if (operations != null) System.arraycopy(operations, index, operations, dstPos, length);
    }

    bucketStart[index] = start;
    setLong(activeTime, index, 0L);
    setLong(rxBytes, index, 0L);
    setLong(rxPackets, index, 0L);
    setLong(txBytes, index, 0L);
    setLong(txPackets, index, 0L);
    setLong(operations, index, 0L);
    bucketCount++;
}
```
+ 显示/隐藏密码输入
```java
EditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance())
EditText.setTransformationMethod(PasswordTransformationMethod.getInstance())
```
+ 动态更改语系
```java
Resources res = getResources();
Configuration conf = res.getConfiguration();
conf.locale = Locale.JAPAN;
DisplayMetrics dm = res.getDisplayMetrics();
res.updateConfiguration(conf,dm);
```
+ 判断程序targetSdkVersion
```java
if(getApplicationInfo.targetSdkVersion <= Build.VERSION_CODES.GINGERBREAD)
{
    //do sth
}
```