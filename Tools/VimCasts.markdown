Vim Casts
=============
#VimCasts

##show invisibles
+ ``set list!`` ``nmap <leader>l :set list!<CR>`` ``\l`` ``:help listchars`` ``set listchars=tab:	,eol: `` ``ctrl-v u00ac`` ``ctrl-v u25b8`` ``ctrl-v ctrl-i``

##tabs and spaces
``tabstop softtabstop shiftwidth expandtab``

##whitespace preferences and filetypes
``autocmd FileType javascript setlocal ts=4 sts=4 sw=4 noexpandtab`` ``filttype on`` 
```
if has("autocmd")
	...
endif
```
``autocmd BufNewFile,BufRead *.rss,*.atom setfiletype xml``

##tidying whitespace
``set noexpandtab`` ``:retab!``
``nnoremap <F5> :%s/\s\+$//e<CR>``
``autocmd BufWritePre *.py,*.js :call <SID>StripTrailingWhitespaces()``
``:g/^$/d``

##indentation commands
``vi}`` visual inner sections
``<{motion} >{motion} ={motion}``
``gg=G``
``=i}`` ``:help text-objects``

##buffers
``vim *.txt`` ``:bn`` buffer next ``:bp`` buffer previous`` ``ctrl+^`` last buffer
``#h + a.txt`` hidden buffer ``:w :e! :bd! :q!`` save/restore/force delete the buffer,discard any changes/quit without saving changes
``set hidden`` 