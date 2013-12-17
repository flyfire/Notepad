iptables tutorial
==================
[REF](https://www.frozentux.net/documents/iptables-tutorial/)

+ It is very important to understand that iptables was and is specifically built to work on the headers of the Internet and the Transport layers. It is possible to do some very basic filtering with iptables in the Application and Network access layers as well, but it was not designed for this, nor is it very suitable for those purposes.Iptables通常被用来在tcp/ip层工作，尽管也可以在application layer/network access layer工作，但不推荐这样做。For example, if we use a string match and match for a specific string inside the packet, lets say ``get /index.html``. Will that work? Normally, yes. However, if the packet size is very small, it will not. The reason is that iptables is built to work on a per packet basis, which means that if the string is split into several separate packets, iptables will not see that whole string. For this reason, you are much, much better off using a proxy of some sort for filtering in the application layer.

+ If the IP filter implementation is strictly following the definition, it would in other words only be able to filter packets based on their IP headers (Source and Destionation address, TOS/DSCP/ECN , TTL, Protocol , etc. Things that are actually in the IP header.) However, since the Iptables implementation is not perfectly strict around this definition, it is also able to filter packets based on other headers that lie deeper into the packet (TCP, UDP, etc), and shallower (MAC source address).

+ ``iptable -A INPUT -p tcp --dport 80 -m time --timestart 09:00 --timestop 18:00 -j DROP``

+ ``Netfilter``有5个链chain/filter point，为``INPUT,OUTPUT,FORWARD,PREROUTING,POSTROUTING``，4个table，为``raw,filter,nat,mangle``。``filter``用以对数据进行过滤，工作在``INPUT,OUTPUT,FORWARD``，``nat``用以对数据包的源地址，目标地址进行修改，工作在``OUTPUT,PREROUTING,POSTROUTING``中，``mangle``用以对数据包进行高级修改，如``TTL,TOS,QOS``等，工作在5个链中，即``INPUT,OUTPUT,FORWARD,PREROUTING,POSTROUTING``。

+ 作为服务器使用，可以使用``INPUT``链，``filter``表来规定那些主机可以访问我，可以使用``OUTPUT``链，``filter``表来规定哪些数据可以发送出去。作为路由器使用时，可以通过``FORWARD``链，``filter``表来控制哪些可以转发，如果需要修改转发数据的源地址、目标IP地址，可以使用``PREROUTING,POSTROUTING``链，``nat``表。

+ ``iptables``通过规则对数据访问进行控制，一个规则使用一行配置，规则按照顺序排列，当收到、发出、转发数据包时，使用规则对数据包进行匹配，按规则顺序进行逐条匹配。数据包按照第一个匹配上的规则执行相关操作：丢弃，放行，修改，没有匹配规则，则使用默认动作，每个chain都有各自的默认动作。

+ ``iptables -t filter -A INPUT -s 192.168.1.1 -j DROP``表，链，匹配属性，执行动作。匹配属性可以使用源、目标IP地址，协议(TCP,UDP,ICMP)，端口号，接口，TCP状态。动作可以选择``ACCEPT,DROP,REJECT``。``REJECT``相比``DROP``还会返回失败信息。

+ 列出现有iptables规则 ``iptables -L``
+ 插入一个规则 ``iptables -i INPUT 3 -p tcp --dport 22 -j ACCEPT``在``INPUT``链中插入规则，插入到第3个，匹配规则tcp协议，目的端口号22，执行动作允许动作。
+ 删除一个规则，``iptables -D INPUT 3``，``iptables -D INPUT -s 192.168.1.1 -j DROP``删除匹配的规则。
+ 删除所有规则，``iptables -F``

+ 命令行输入的``iptables``规则是临时生效，保存在内存当中，机器重启后就没有了，并不会永久保存。若要永久保存规则，则需要将规则保存在``/etc/sysconfig/iptables``配置文件中，可以通过``service iptables save`` 或``iptables-svae >/etc/sysconfig/iptables``将iptables规则写入配置文件。CentOS/RHEL系统会带有默认iptables规则，保存自定义规则会覆盖默认规则。

+ 匹配参数
  + 基于IP地址，``-s 192.168.1.1``源地址，``-d 10.0.0.0/8``目标IP地址。
  + 基于网络接口(网卡)，``-i eth0``接收接口，``-o eth0``发送接口。
  + 排除参数 ``-s '!' 192.168.1.0/24``取反
  + 基于协议/端口号 ``-p tcp --dport 23``，``-p udp --sport 53``，``-p icmp``

+ 控制到本机的流量
```bash
iptables -A INPUT -s 192.168.1.100 -j DROP
iptables -A INPUT -p tcp --dport 80 -j DROP
iptables -A INPUT -s 192.168.1.0/24 -p tcp --dport 22 -j DROP
iptables -A INPUT -i eth0 -j ACCEPT
```

+ 使用做路由器(进行数据转发)时，可以通过定义forward规则来进行控制转发。禁止所有从192.168.1.0/24到10.1.1.0/24的流量``iptables -A FORWARD -s 192.168.1.0/24 -d 10.1.1.0/24 -j DROP``

+ 使用``nat``表对源IP地址，目标IP地址进行修改，NAT分为两种，``SNAT``对源IP地址进行转换，通常用于内部共享一个公共IP，伪装内部地址。``DNAT``是目标地址转换，通常用于跳转。

```bash
#通过nat实现跳转
iptables -t nat -A PREROUTING -p tcp --dport 80 -j DNAT --to-dest 192.168.1.10
#通过nat对出向数据进行跳转
iptables -t nat -A OUTPUT -p tcp --dport 80 -j DNAT --to-dest 192.168.1.100:8080
#通过nat对数据流进行封装，一般意义上的nat，将内部ip地址全部伪装成一个外部公网ip地址
iptables -t nat -A POSTROUTING -o eth0 -j MASQUERADE
#通过nat隐藏源ip地址
iptables -t nat -A POSTROUTING -j SNAT --to-source 1.2.3.4
```