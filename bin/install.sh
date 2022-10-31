#! /bin/sh
cd /data/mycode/httpserver/
export GPG_TTY=$(tty)
mvn clean install
