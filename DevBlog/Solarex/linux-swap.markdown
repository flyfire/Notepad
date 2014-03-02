Linux Swap分区
==================
+ ``dd if=/dev/zero of=/tmp/swapfile bs=1024k count=10`` ``mkswap -f /tmp/swapfile`` ``swapon /tmp/swapfile``
+ ``swapon -s``显示fstab中交换分区信息
+ ``swapon -a``将``/etc/fstab``文件中所有设置为swap的设备开启，标记noauto参数的设备除外
+ ``swapoff -a``将``/etc/fstab``文件中所有设置为swap的设备关闭
+ ``swapoff /tmp/swapfile``