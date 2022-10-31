#! /bin/sh
cd /data/mycode/commonsqy/
export GPG_TTY=$(tty)
mvn clean install
