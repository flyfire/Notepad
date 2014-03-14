Android源代码下载脚本
===================
```bash
#!/bin/bash
# http://source.android.com/source/downloading.html
if [ ! -f repo ];then
    # download repo to $PWD
    curl http://commondatastorage.googleapis.com/git-repo-downloads/repo >repo
    chmod +x repo
    # PATH=.:$PATH:
fi
./repo init -u https://android.googlesource.com/platform/manifest -b android-2.3.7_r1
./repo sync
while [ $? = 1 ]
do
    echo "==============sync failed,re-sync again============="
    sleep 3
    ./repo sync
done
```

保存到服务器上``down_and_src.sh``，然后``nohup bash -x down_and_src.sh &``让服务器慢慢下吧。