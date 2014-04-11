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

