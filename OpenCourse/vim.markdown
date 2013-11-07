vim
====
+ ``zf3j`` ``zd`` 折叠 ``zf%`` 括号匹配选择范围仍然适用。v进入visual模式，选择要折叠的范围，``zf``折叠 ``zo``打开折叠 ``zc``关闭折叠 ``zr`` ``zm`` ``zR`` ``zM`` 
+ ``mkview`` ``loadview``
+ ``set foldmethod=indent`` 
+ ``ctrl+x`` ``ctrl+k``  补全
+ ``set dictionary+=/usr/share/dict/words``  ``set spell`` 插入模式下``ctrl+x--s``来修正
+ ``map ,ss :setlocal spell!<cr>``
+ ``ctrl+x--f`` 包含头文件 补全路径 ``ctrl+n`` 联想字符串 ``:h i_CTRL-N`` ``ctrl-x--l`` 补全整行 ``set ignorecase``补齐的时候忽略大小写 ``ctrl-x--o`` 特定文件类型中匹配 ``:h new-omni-completion``
+ 插入模式下``xdate``插入当前日期 ``ia myname <c-r>%<cr>``当前文件名  