linux chmod 命令总结
===================
[ref](http://vbird.dic.ksu.edu.tw/linux_basic/0210filepermission.php#chmod)

文件权限的改变使用的是chmod这个指令，但是，权限的设定方法有两种，分别可以使用数字或者是符号来进行权限的变更。Linux文件的基本权限就有九个，分别是``owner/group/others``三种身份各有自己的``read/write/execute``权限。可以使用数字来代表各个权限，各权限的分数对照表如下：``r:4,w:2,x:1``。九个权限分别是(1)user (2)group (3)others三种身份啦！那么我们就可以藉由u, g, o来代表三种身份的权限！此外， a 则代表 all 亦即全部的身份！那么读写的权限就可以写成r, w, x！

```shell
houruhou@compiler168:testdir$ touch test1
houruhou@compiler168:testdir$ ll test1
-rw-r--r-- 1 houruhou 0 2013-12-05 16:21 test1
houruhou@compiler168:testdir$ chmod +x test1
houruhou@compiler168:testdir$ ll test1
-rwxr-xr-x 1 houruhou 0 2013-12-05 16:21 test1*
houruhou@compiler168:testdir$ chmod 755 test1
houruhou@compiler168:testdir$ ll test1
-rwxr-xr-x 1 houruhou 0 2013-12-05 16:21 test1*
houruhou@compiler168:testdir$ chmod u-w,g-r,o-r test1
houruhou@compiler168:testdir$ ll test1
-r-x--x--x 1 houruhou 0 2013-12-05 16:21 test1*
houruhou@compiler168:testdir$ echo "test1" >test1
-bash: test1: 权限不够
houruhou@compiler168:testdir$ chmod 644 test1
houruhou@compiler168:testdir$ echo "test1" >test1
houruhou@compiler168:testdir$ cat test1
test1
```

文件是实际含有数据的地方，包括一般文本文件、数据库内容文件、二进制可执行文件(binary program)等等。 因此，权限对于文件来说，他的意义是这样的：
+ ``r (read)``：可读取此一文件的实际内容，如读取文本文件的文字内容等；
+ ``w (write)``：可以编辑、新增或者是修改该文件的内容(但不含删除该文件)；
+ ``x (execute)``：该文件具有可以被系统执行的权限。

可读(r)代表读取文件内容。在Linux底下，我们的文件是否能被执行，是藉由是否具有``x``这个权限来决定的，跟文件扩展名无关。对一个文件具有w权限时，你可以具有写入/编辑/新增/修改文件的内容的权限， 但并不具备有删除该文件本身的权限，对于文件的rwx来说， 主要都是针对『文件的内容』而言，与文件档名的存在与否没有关系。因为文件记录的是实际的数据嘛。

文件是存放实际数据的所在，那么目录主要是储存啥玩意啊？目录主要的内容在记录文件名列表，文件名与目录有强烈的关连啦！ 所以如果是针对目录时，那个 r, w, x 对目录是什么意义呢？
+ ``r (read contents in directory)`` 表示具有读取目录结构列表的权限，所以当你具有读取(r)一个目录的权限时，表示你可以查询该目录下的文件名数据。 所以你就可以利用 ls 这个指令将该目录的内容列表显示出来！
+ ``w (modify contents of directory)`` 这个可写入的权限对目录来说，是很了不起的！ 因为他表示你具有异动该目录结构列表的权限，也就是底下这些权限：
  + 建立新的文件与目录；
  + 删除已经存在的文件与目录(不论该文件的权限为何！)
  + 将已存在的文件或目录进行更名；
  + 搬移该目录内的文件、目录位置。
总之，目录的w权限就与该目录底下的文件名异动有关就对了。
+ ``x (access directory)`` 目录的执行权限有啥用途啊？目录只是记录文件名而已，总不能拿来执行吧？没错！目录不可以被执行，目录的x代表的是用户能否进入该目录。

```shell
houruhou@compiler168:~$ ll testdir/
总用量 12K
drwxr-xr-x  2 houruhou 4.0K 2013-12-05 16:21 ./
drwxrwx--- 17 houruhou 4.0K 2013-12-05 16:21 ../
-rw-r--r--  1 houruhou    6 2013-12-05 16:24 test1
houruhou@compiler168:~$ touch testdir/a
houruhou@compiler168:~$ chmod -x testdir/
houruhou@compiler168:~$ cd testdir/
-bash: cd: testdir/: 权限不够
houruhou@compiler168:~$ chmod +x testdir/
houruhou@compiler168:~$ chmod -r testdir/
houruhou@compiler168:~$ ls testdir/
ls: 无法打开目录testdir/: 权限不够
houruhou@compiler168:~$ chmod +r testdir/
houruhou@compiler168:~$ ls testdir/
a  test1
houruhou@compiler168:~$ cd testdir/
/opt2/houruhou/testdir
houruhou@compiler168:testdir$ cd ..
houruhou@compiler168:~$ mv testdir/a testdir/b
houruhou@compiler168:~$ chmod -w testdir/
houruhou@compiler168:~$ touch testdir/c
touch: 无法创建"testdir/c": 权限不够
houruhou@compiler168:~$ mv testdir/b testdir/d
mv: 无法将"testdir/b" 移动至"testdir/d": 权限不够
houruhou@compiler168:~$ chmod +w testdir/
houruhou@compiler168:~$ mv testdir/b testdir/d
houruhou@compiler168:~$ touch testdir/c
```
