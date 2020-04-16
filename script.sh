#!/bin/bash

kafka_dir="/home/user/Desktop/ZiTh0s/docs/uni/2ºSemestre/AS/kafka_2.12-2.4.1/"

zookeeper_start=$kafka_dir"bin/zookeeper-server-start.sh "

zookeeper_stop=$kafka_dir"bin/zookeeper-server-stop.sh "

zookeeper_config=$kafka_dir"config/zookeeper.properties"

broker_command=$kafka_dir"bin/kafka-server-start.sh "

broker_config="/home/user/Desktop/ZiTh0s/docs/uni/2ºSemestre/AS/projeto2/AS-Project2/server_properties/"

number_of_brokers=8

echo "Starting Zookeeper..."

eval $zookeeper_start$zookeeper_config" >/dev/null &"

echo "Zookeeper Started..."

echo "Starting Brokers..."

for (( i=1; i<=$number_of_brokers; i++ )) 
do
	eval $broker_command$broker_config"server"$i".properties >/dev/null &"
done

echo "Brokers Started..."

while : ; do
	echo "Press 'q' to exit"
	read -n 1 k <&1
	if [[ $k = q ]] ; then
		printf "\nQuitting from the program\n"
		break
	fi
done

open_servers="$(ps ax | grep -i 'kafka.Kafka' | grep -v grep | awk '{print $1}')"

for i in $open_servers
do
	eval "kill -15 "$i
done





