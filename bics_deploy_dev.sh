#!/bin/bash
DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
#创建项目jar包工程目录
if [ ! -d /root/mytools/wtcp/server ];then
  mkdir -p /root/mytools/wtcp/server
fi
#创建备份目录
date_stamp=`date "+%Y-%m-%d-%H-%M-%S"`
echo $date_stamp
if [ ! -d /root/mytools/wtcp/backup/$date_stamp ];then
  mkdir -p /root/mytools/wtcp/backup/$date_stamp
fi
#备份
if [ ! -f "/root/mytools/wtcp/server/wtcp-bics-2.1.0.jar" ];then
echo "wtcp-bics-2.1.0.jar文件不存在"
else
cp /root/mytools/wtcp/server/wtcp-bics-2.1.0.jar  /root/mytools/wtcp/backup/$date_stamp
fi
#杀死java进程
appName=wtcp-bics
PID=`ps -ef |grep java | grep $appName | grep -v grep | awk '{print $2}'`
if [ "$PID" == "" ]; then
                sleep 1;
                echo "no process";
        else
                echo "process exsits";
                kill -9 $PID
fi
#删除老包，替换新包
rm -rf /root/mytools/wtcp/server/wtcp-bics-2.1.0.jar
cp $DIR/wtcp-bics-2.1.0.jar  /root/mytools/wtcp/server
#启动新包
if [ ! -d /root/mytools/wtcp/logs/bics ];then
echo "文件夹不存在，创建新文件夹"
mkdir -p /root/mytools/wtcp/logs/bics
fi
sleep 2s
nohup java -server -Xms1024m -Xmx1024m -Xmn256M -Xss512k -XX:+AggressiveOpts -XX:+UseBiasedLocking -XX:+DisableExplicitGC -Djava.awt.headless=true -DLOG_HOME="/root/mytools/wtcp/logs/bics" -jar /root/mytools/wtcp/server/wtcp-bics-2.1.0.jar --spring.profiles.active=dev > /root/mytools/wtcp/logs/wtcp-bics.out 2>&1 &