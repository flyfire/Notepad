Android Commandline
====================

#Android Commandline

##创建avd
+ 进入android-sdk目录下的tools目录下再执行``android list target``
+ ``android create avd -n avd10 -t 1``，If the target you selected was a standard Android system image ("Type: platform"), the android tool next asks you whether you want to create a custom hardware profile.在``hardware emulation settings``中设置``hw.ramSize``。
+ ``android updata avd -n avd10``
+ ``android delete avd -n avd10``
