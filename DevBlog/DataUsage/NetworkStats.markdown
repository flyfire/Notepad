NetworkStats
==================
## NetworkStatsFactory
+ ``NetworkStatsFactory``,Creates NetworkStats instances by parsing various /proc/ files as needed.
![NetworkStatsFactory_uml](NetworkStatsFactory_uml.png "uml")
![/proc/net/xt_qtaguid](NetworkStatsFactory_parse_proc_files.png "NetworkStatsFactory parse proc files")

+ ``public NetworkStats readNetworkStatsSummaryDev() throws IOException``,Parse and return interface-level summary NetworkStats measured
using /proc/net/dev style hooks, which may include non IP layer
traffic. Values monotonically increase since device boot, and may include details about inactive interfaces.
+ ``public static NetworkStats javaReadNetworkStatsDetail(File detailPath, int limitUid)``,Parse and return NetworkStats with UID-level details. Values are expected to monotonically increase since device boot.
+ ``public static native int nativeReadNetworkStatsDetail(NetworkStats stats, String path, int limitUid);``,Parse statistics from file into given {@link NetworkStats} object. Values are expected to monotonically increase since device boot.

## NetworkStats
+ ``NetworkStats``,Collection of active network statistics. Can contain summary details across all interfaces, or details with per-UID granularity. Internally stores data as a large table, closely matching /proc/ data format. This structure optimizes for rapid in-memory comparison, but consider using NetworkStatsHistory  when persisting.
+ ``NetworkStats.Entry``,``ifce,uid,rxBytes,rxPackets,txBytes,txPackets``
![UID_ALL](NetworkStats_UID_ALL.png "UID_ALL/SET_ALL")
![NetworkStats_uml](NetworkStats_uml.png "uml")
+ ``public static <C> NetworkStats subtract(NetworkStats left, NetworkStats right, NonMonotonicObserver<C> observer, C cookie)``,Subtract the two given NetworkStats objects, returning the delta between two snapshots in time. Assumes that statistics rows collect over time, and that none of them have disappeared.If counters have rolled backwards, they are clamped to 0 and reported to the given NonMonotonicObserver.
+ ``public NetworkStats groupedByIface() ``,Return total statistics grouped by iface,doesn't mutate the original structure.
+ ``public NetworkStats groupedByUid() ``,Return total statistics grouped by uid, doesn't mutate the original structure.
+ ``public NetworkStats withoutUids(int[] uids)``,Return all rows except those attributed to the requested UID, doesn't mutate the original structure.

## NetworkStatsHistory
+ ``NetworkStatsHistory``,Collection of historical network statistics, recorded into equally-sized "buckets" in time. Internally it stores data in long series for more efficient persistence.Each bucket is defined by a  bucketStart timestamp, and lasts for bucketDuration. Internally assumes that bucketStart is sorted at all times.
+ ``NetworkStatsHistory.Entry``,``bucketDuration,bucketStart,activeTime,rxBytes,rxPackets,txBytes,txPackets,operations``
![NetworkStatsHistory_]
+ ``public void recordData(long start, long end, NetworkStats.Entry entry)``,Record that data traffic occurred in the given time range. Will distribute across internal buckets, creating new buckets as needed.
+ ``public void recordHistory(NetworkStatsHistory input, long start, long end)``,Record given NetworkStatsHistory into this history, copying only buckets that atomically occur in the inclusive time range. Doesn't interpolate across partial buckets.
+ ``public void removeBucketsBefore(long cutoff)``,Remove buckets older than requested cutoff.
+ ``public void dump(IndentingPrintWriter pw, boolean fullHistory)``

## NetworkPolicy
+ ``NetworkPolicy``,Policy for networks matching a ``NetworkTemplate``, including usage cycle and limits to be enforced.

![NetworkPolicy](NetworkPolicy.png)

+ ``public boolean isOverLimit(long totalBytes)``,Test if given measurement is near enough to ``limitBytes`` to be considered over-limit.Over-estimate, since kernel will trigger limit once first packet trips over limit.
+ ``public void clearSnooze()``,Clear any existing snooze values, setting to ``SNOOZE_NEVER``.

## NetworkTemplate
+ ``NetworkTemplate``,Template definition used to generically match ```NetworkIdentity``,usually when collecting statistics.

![NetworkTemplate](NetworkTemplate_uml.png)
![NetworkTemplate_config_data_usage_network_types](NetworkTemplate_config_data_usage_network_types.png)
![NetworkTemplate_match_pattern](NetworkTemplate_match_pattern.png)

[internal_config](https://android.googlesource.com/platform/frameworks/base/+/refs/heads/master/core/res/res/values/config.xml)

+ ``com.android.internal.R``和``android.R``有什么区别,``com.``开头的包都是隐藏的，运行环境会有，但是开发环境你找不到的，就是系统会用它，但是不能显示的告诉你去用.``android.R`` 这个就是你对应开发环境中的，让你用的。``android.R``位置：SDK开发包对应的平台下/data/res/，``com.``位置：源码framework/core/java/com/android/internal/下面。com开头的要调用，你在项目中建立一个com.android.internal的包，把那个文件拷进去，android开头的直接调用。这样理解2者的不同吧，前者com开头的，就是系统故意隐藏的一些包，这些包，设计到安全的问题，没有开放api给我们，但是其实是可以用的，当然你编译通不过，所以你让编译通过，所以你把那个文件拷进去，它调用的时候自然会用的

+ ``public static NetworkTemplate buildTemplateMobileAll(String subscriberId)``,Template to match ``ConnectivityManager#TYPE_MOBILE`` networks with the given IMSI.
+ ``public boolean matches(NetworkIdentity ident)``,Test if given ``NetworkIdentity`` matches this template.

![NetworkTemplate_match_NetworkIdentity](NetworkTemplate_match_NetworkIdentity.png)

+ ``private boolean matchesMobile(NetworkIdentity ident)``,Check if mobile network with matching IMSI.
+ ``private boolean matchesMobile3gLower(NetworkIdentity ident)``,Check if mobile network classified 3G or lower with matching IMSI.
+ ``private boolean matchesMobile4g(NetworkIdentity ident)``,Check if mobile network classified 4G with matching IMSI.
+ ``private boolean matchesWifi(NetworkIdentity ident)``,Check if matches Wi-Fi network template.
+ ``private boolean matchesEthernet(NetworkIdentity ident)``,Check if matches Ethernet network template.
+ ``private boolean matchesMobileWildcard(NetworkIdentity ident)``
+ ``private boolean matchesWifiWildcard(NetworkIdentity ident)``
+ ``private static String getMatchRuleName(int matchRule)``


## NetworkIdentity
+ ``NetworkIdentity``,Network definition that includes strong identity. Analogous to combining ``NetworkInfo`` and an IMSI.

![NetworkIdentity](NetworkIdentity_uml.png)

+ ``public static NetworkIdentity buildNetworkIdentity(Context context, NetworkState state)``,Build a ``NetworkIdentity`` from the given ``NetworkState``,assuming that any mobile networks are using the current IMSI.

## ConnectivityManager
+ ``ConnectivityManager``,Class that answers queries about the state of network connectivity. It also notifies applications when network connectivity changes. Get an instance of this class by calling ``android.content.Context#getSystemService(String) Context.getSystemService(Context.CONNECTIVITY_SERVICE)``.The primary responsibilities of this class are to:Monitor network connections (Wi-Fi, GPRS, UMTS, etc.),Send broadcast intents when network connectivity changes,Attempt to "fail over" to another network when connectivity to a network is lost,Provide an API that allows applications to query the coarse-grained or fine-grained state of the available networks.

![ConnectivityManager](ConnectivityManager_uml.png)
![networktype](ConnectivityManager_intent_extra_and_network_type.png)
![getNetworkType](ConnectivityManager_getNetworkTypeName.png)

## NetworkInfo
+ ``NetworkInfo``,Describes the status of a network interface.Use ``ConnectivityManager#getActiveNetworkInfo()`` to get an instance that represents the current network connection.

![detailedstate](NetworkInfo_DetailedState.png)
