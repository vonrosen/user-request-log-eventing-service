#!/bin/bash

apt-get update
apt-get install -y curl
curl -O 'https://github.com/rabbitmq/rabbitmq-delayed-message-exchange/releases/download/v3.8.0/rabbitmq_delayed_message_exchange-3.8.0.ez' -L
apt-get install -y unzip
cp ./rabbitmq_delayed_message_exchange-3.8.0.ez /opt/rabbitmq/plugins/
rabbitmq-plugins enable rabbitmq_delayed_message_exchange
cat >/etc/rabbitmq/rabbitmq.conf <<EOL
loopback_users.guest = false
listeners.tcp.default = 5672
management.tcp.port = 15672
EOL
/opt/rabbitmq/sbin/rabbitmq-server
