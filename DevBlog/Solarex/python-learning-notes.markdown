Python Learning Notes
========================
+ [Python](http://edu.51cto.com/lesson/id-11823.html)
+ Python中的变量类似于C语言中的指针，变的是指向的内存地址。
```python
houruhou@compiler168:Solarex$ cat test.py
#!/bin/python

x = 1
y = 2
print 'x = ',x,id(x)
print 'y = ',y,id(y)
x = 2
print 'x = ',x,id(x)
x = 3
y = 4
print 'x = ',x,id(x)
print 'y = ',y,id(y)
houruhou@compiler168:Solarex$ python test.py
x =  1 30696088
y =  2 30696064
x =  2 30696064
x =  3 30696040
y =  4 30696016
```