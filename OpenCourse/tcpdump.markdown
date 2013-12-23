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
+ ``tcpdump port 80`` 抓取80端口的流量
+ ``tcpdump portrange 1-1024``
+ ``tcpdump src port 1234``
+ ``tcpdump dst port 80``
+ ``tcpdump host www.baidu.com`` 只侦听包中包含baidu地址的包，不管是源还是目标 ``tcpdump host 192.168.1.1``
+ ``tcpdump greater 1000`` 只抓取大于1000字节的流量 ``tcpdump less 10`` 只抓取小于10字节的流量，tcp,udp都有自己的最小包大小，如果小于这个最小的字节数，就是不正常流量，可以监视下这些不正常流量。
+ ``tcpdump -A`` 以ASCII码的形式显示每个数据包的内容，可能乱码，图片，视频等二进制信息。``tcpdump -X`` 以16进制和ASCII码的形式显示数据包内容
+ ``tcpdump src 192.168.1.109`` 只监听从源地址为192.168.1.109发出的数据包 ``tcpdump dst 192.168.1.1`` 只监听目标地址为192.168.1.1的数据包
+ ``tcpdump``各个规则可以使用``and``，``or``，``not``进行逻辑组合。
+ ``tcpdump tcp and src 192.168.1.109 and port 1000``
+ ``tcpdump src 192.168.1.109 or src 192.168.1.105``
+ ``tcpdump not port 80``
+ ``tcpdump tcp and src 192.168.1.109 and not port 80``