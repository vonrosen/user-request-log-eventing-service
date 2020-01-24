package org.hunter.userrequestlogeventingservice.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.Map;

import org.hunter.model.UserView;
import org.hunter.userrequestlogeventingservice.config.AppProperties;
import org.hunter.userrequestlogeventingservice.config.RabbitConfig;
import org.hunter.userrequestlogeventingservice.model.UserRequestLog;
import org.hunter.userrequestlogeventingservice.repository.UserRequestLogRepository;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.hunter.model.UserRequestLogView;
import com.rabbitmq.client.Channel;

@Service
public class RabbitMqService {

    @Autowired
    private UserRequestLogRepository userRequestLogRepository;
    @Autowired
    private AppProperties appProperties;
    @Autowired
    private RabbitTemplate rabbitTemplate;    
    private RestTemplate restTemplate = new RestTemplate();

    @RabbitListener(queues = RabbitConfig.QUEUE)
    public void consume(UserRequestLogView userRequestLogView, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag)
            throws IOException {

        try {
            if (userRequestLogView == null || userRequestLogView.getUserId() == null || userRequestLogView.getMaxPaymentAmount() == null) {
                channel.basicReject(tag, false);
                return;
            }
            restTemplate.getForObject(
                    appProperties.getUserServiceBaseUrl() + "/" + userRequestLogView.getUserId().toString(),
                    UserView.class);
            UserRequestLog userRequestLogForSave = new UserRequestLog(null, userRequestLogView.getUserId(),
            		userRequestLogView.getMaxPaymentAmount().multiply(BigDecimal.valueOf(100)).toBigInteger());
            userRequestLogRepository.save(userRequestLogForSave);
            channel.basicAck(tag, false);
        }
        catch (Exception exc) {
            channel.basicReject(tag, true);
        }
    }
    
    @RabbitListener(queues = RabbitConfig.DEAD_LETTER_QUEUE)
    public void rePublish(Message failedMessage) {
        Map<String, Object> headers = failedMessage.getMessageProperties().getHeaders();
        Integer retriesHeader = (Integer) headers.get(RabbitConfig.X_RETRIES_HEADER);
        if (retriesHeader == null) {
            retriesHeader = Integer.valueOf(0);
        }
        if (retriesHeader < 3) {
            headers.put(RabbitConfig.X_RETRIES_HEADER, retriesHeader + 1);
            headers.put("x-delay", 5000 * retriesHeader);
            rabbitTemplate.send(RabbitConfig.DELAY_EXCHANGE, RabbitConfig.QUEUE, failedMessage);
        }
        else {
            rabbitTemplate.send(RabbitConfig.PARKING_LOT_QUEUE, failedMessage);
        }
    }    

}
