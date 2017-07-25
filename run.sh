#!/bin/sh
type="test"
if [ $# -gt 0 ];then
    type=$1
fi
if [ $type == 'prod' ];then
    mvn clean install -Dmaven.test.skip=true -Pprod
    scp -r ./target/scheduler-v2.war root@192.168.1.5:/data/tomcat/
fi
if [ $type == 'test' ]; then
    mvn clean install  -Dmaven.test.skip=true -Ptest
    scp -r ./target/scheduler-v2.war spiderdt@192.168.1.2:/data/tmp/
    ssh -p 22 spiderdt@192.168.1.2 'sudo mv /data/tmp/scheduler-v2.war /data/tomcat/'
fi
#default
if [ $type ==  'dev' ]; then
    mvn clean install  -Dmaven.test.skip=true -Pdev
    cp ./target/scheduler-v2.war /Users/fivebit/apache-tomcat-8.5.14/webapps/
fi
