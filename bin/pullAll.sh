#!/bin/sh
for dir in `ls .`; do
  if [ -d $dir ]; then
    echo '进入: '$dir
    cd $dir
    git pull
    cd ..
  fi
done
