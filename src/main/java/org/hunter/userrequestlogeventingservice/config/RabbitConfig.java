package org.hunter.userrequestlogeventingservice.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

	public static final String EXCHANGE = "loan";
	public static final String DELAY_EXCHANGE = "dlqReRouter";
	public static final String QUEUE = "userrequestlog";
	public static final String DEAD_LETTER_QUEUE = QUEUE + ".dlq";
	public static final String PARKING_LOT_QUEUE = QUEUE + ".parkinglot";
	public static final String X_RETRIES_HEADER = "x-retries";

	@Bean
	public TopicExchange loanExchange() {
		return new TopicExchange(EXCHANGE);
	}

	@Bean
	public Queue queue() {
		return QueueBuilder.durable(QUEUE).withArgument("x-dead-letter-exchange", "")
				.withArgument("x-dead-letter-routing-key", DEAD_LETTER_QUEUE).withArgument("x-message-ttl", 300000)
				.build();
	}

	@Bean
	public Queue dlQueue() {
		return QueueBuilder.durable(DEAD_LETTER_QUEUE).withArgument("x-message-ttl", 10000).build();
	}

	@Bean
	public Queue parkingLotQueue() {
		return QueueBuilder.durable(PARKING_LOT_QUEUE).build();
	}

	@Bean
	public DirectExchange delayExchange() {
		DirectExchange exchange = new DirectExchange(DELAY_EXCHANGE);
		exchange.setDelayed(true);
		return exchange;
	}

	@Bean
	public Binding bindOriginalToDelay() {
		return BindingBuilder.bind(new Queue(QUEUE)).to(delayExchange()).with(QUEUE);
	}

	@Bean
	public Binding bindOriginalToTopic() {
		return BindingBuilder.bind(new Queue(QUEUE)).to(loanExchange()).with(QUEUE);
	}

	@Bean
	public RabbitTemplate rabbitTemplate(final ConnectionFactory connectionFactory) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(producerJackson2MessageConverter());
		return rabbitTemplate;
	}

	@Bean
	public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
		return new Jackson2JsonMessageConverter();
	}
}
