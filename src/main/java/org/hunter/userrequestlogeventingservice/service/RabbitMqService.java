package org.hunter.userrequestlogeventingservice.service;

import java.io.IOException;

import org.hunter.model.UserView;
import org.hunter.userrequestlogeventingservice.config.AppProperties;
import org.hunter.userrequestlogeventingservice.model.UserRequestLog;
import org.hunter.userrequestlogeventingservice.repository.UserRequestLogRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;
import org.springframework.web.client.RestTemplate;

import com.hunter.model.UserRequestLogView;
import com.rabbitmq.client.Channel;

@Service
public class RabbitMqService {

    @Autowired
    private UserRequestLogRepository userRequestLogRepository;
    @Autowired
    private AppProperties appProperties;
    private RestTemplate restTemplate = new RestTemplate();

    @RabbitListener(queues = "userrequestlog")
    public void consume(UserRequestLogView userRequestLog, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag)
            throws IOException {

        try {
            restTemplate.getForObject(
                    appProperties.getUserServiceBaseUrl() + "/" + userRequestLog.getUserId().toString(),
                    UserView.class);

            UserRequestLog userRequestLogForSave = new UserRequestLog(null, userRequestLog.getUserId(),
                    userRequestLog.getMaxPaymentAmountCents());
            userRequestLogRepository.save(userRequestLogForSave);
            channel.basicAck(tag, false);
        }
        catch (RestClientResponseException re) {
            if (re.getRawStatusCode() == 404) {
                // if user not exist no retry
                channel.basicReject(tag, false);
            }
            else {
                channel.basicReject(tag, true);
            }
        }
        catch (Exception exc) {
            channel.basicReject(tag, true);
        }
    }

}
