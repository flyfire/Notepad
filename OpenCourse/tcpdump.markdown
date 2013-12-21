tcpdump
========

+ ``tcpdump -c 10 -w cap.log`` 10 packets
+ ``tcpdump -r cap.log`` read log
+ ``tcpdump -D`` list all network interfaces
+ ``tcpdump -i eth1`` 仅监听eth1网卡流量 
+ ``tcpdump -v`` ``tcpdump -vv``
+ ``tcpdump -n`` not resolve to domain name,just show ip address
+ ``tcpdump udp`` 只抓取udp协议包 局域网内的nfs，视频，语音聊天，dns
+ ``tcpdump icmp`` ping ``tcpdump tcp`` http