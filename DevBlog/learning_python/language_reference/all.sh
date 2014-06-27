#!/bin/bash
for md in `ls *.md`
do
    name=`basename $md .md`
    echo $name > $md
    echo "======================================" >>$md
done
