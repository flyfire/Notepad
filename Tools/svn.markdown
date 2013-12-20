SVN
====
客户端常用操作
```
svn import
svn co (checkout)
svn up (update)
svn st (status)
svn log
svn diff
svn list
svn info
svn add
svn delete
svn revert
svn ci (commit)
```

+ ``svn co uri://repo-url pathname``
+ ``svn up pathname`` ``svn up pathname -r revision``
+ ``svn st`` M表示Modified ?表示未纳入版本控制 !表示文件丢失
+ ``svn log`` ``svn log -r 5`` ``svn log -r 5:19`` ``svn log -v -r 5``
+ ``svn diff``不使用任何参数时，``svn diff``将会比较工作文件(已修改？)与缓存在``.svn``的原始拷贝。``svn diff -r 3 filename``当前文件(已修改？)与指定版本进行比较。``svn diff -r 2:3 filename`` 在两个版本之间比较文件的差异。如果你在本机没有工作拷贝，还可以比较版本库的修订版本，只需要在命令行中输入合适的URL。``svn diff -r 3  http://smartphone/repositories/AndroidStudy/packages-test ``
+ ``svn list``显示版本库文件列表。``svn list uri://repo-url``类似在服务器上执行``ls``命令，``svn list pathname``在本地执行
+ ``svn info``查看版本库信息 ``svn info pathname``查看本地版本库信息 ``svn info uri://repo-url``查看服务器端版本信息
+ ``svn add filename``添加文件 ``svn add directory``添加目录 ``svn add directory --no-ignore`` 添加``--no-ignore``否则可能会忽略一些类型的文件，如``*.o``
+ ``svn import  Contacts http://smartphone/repositories/AndroidStudy/packages-test/Contacts  -m “导入联系人代码”`` 导入目录或文件
+ ``svn del``删除本地或服务器端文件或目录
+ ``svn revert``取消本地修改 需要还原目录中所有的修改时，需要使用``-R``参数 ``svn revert pathname -R``
+ ``svn ci``提交本地修改 ``svn ci pathname/filename -m "comment"`` 提交前``svn st``查看文件状态 ``A`` added ``M`` Modified ``D`` deleted 是需要提交的文件，对某个文件进行修改操作，运行``svn diff``查看具体修改，再决定是否提交
+ ``svn mkdir/mv/cp``不仅可以对本地进行操作，还可以对服务器端进行操作，只需要传入合适的``uri://repo-url``
+ ``svn update``时，服务器对本地文件的更新会在文件名前以``U``标示 服务器上已经提交的代码可能和本地代码有冲突，有冲突的文件会以``C``标示，文件冲突时，svn会生成3个文件``filename.mine``最近修改的文件``filename.roldrev``上次与服务器端同步过来的文件，不包括最近你自己的修改``filename.rnewrev``客户端从服务器端刚刚收到的版本，包含服务器端的修改。