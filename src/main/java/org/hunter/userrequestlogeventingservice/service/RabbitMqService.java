package org.hunter.userrequestlogeventingservice.service;

import java.io.IOException;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;

import com.rabbitmq.client.Channel;

@Service
public class RabbitMqService {

    @RabbitListener(queues = "userrequestlog")
    public void consume(String payload, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag)
            throws IOException {
        System.out.println("payload " + payload);
        channel.basicAck(tag, false);
    }

}
