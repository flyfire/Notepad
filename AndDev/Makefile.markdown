Makefile
==========

makefile遇到一个.o文件会自动地把.c文件加到依赖关系中。

```makefile
.PHONY: clean
clean :
	-rm tgt $(objects)
```

Makefile 中只有行注释，和 UNIX 的 Shell 脚本一样，其注释是用“#”字符，这个就
像 C/C++中的“//”一样。如果你要在你的 Makefile 中使用“#”字符，可以用反斜框进行
转义，如：“\#”。