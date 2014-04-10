#!/bin/bash
# filename:update_repo.sh
for repos in `find . -maxdepth 1 -type d | /bin/grep '/'`
do
	cd $repos;echo $repos
	if [ -d '.svn' ];then
		echo "svn repo"
		svn cleanup;svn up
	elif [ -d '.git' ];then
		echo "git repo"
		git pull
	else
		echo "not a vcs repo"
	fi
done