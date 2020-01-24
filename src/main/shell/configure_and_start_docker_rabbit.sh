#!/bin/bash

apt-get update
apt-get install -y curl
curl -O 'https://dl.bintray.com/rabbitmq/community-plugins/3.6.x/rabbitmq_delayed_message_exchange/rabbitmq_delayed_message_exchange-20171215-3.6.x.zip'
apt-get install -y unzip
unzip ./rabbitmq_delayed_message_exchange-20171215-3.6.x.zip
cp ./rabbitmq_delayed_message_exchange-20171215-3.6.x.ez /opt/rabbitmq/plugins/
rabbitmq-plugins enable rabbitmq_delayed_message_exchange
cat >/etc/rabbitmq/rabbitmq.conf <<EOL
loopback_users.guest = false
listeners.tcp.default = 5672
management.tcp.port = 15672
EOL
/opt/rabbitmq/sbin/rabbitmq-server
