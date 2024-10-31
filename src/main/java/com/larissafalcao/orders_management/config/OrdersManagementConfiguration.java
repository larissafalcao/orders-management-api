package com.larissafalcao.orders_management.config;

import lombok.NoArgsConstructor;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.ExchangeBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Component
@NoArgsConstructor
public class OrdersManagementConfiguration {
    public static final String QUEUE = "orders-queue";
    public static final String QUEUE_DLQ = "orders-queue-dlq";
    public static final String EXCHANGE = "orders.exchange";


    @Bean
    @Qualifier("ordersExchange")
    public FanoutExchange ordersExchange() {
        return (FanoutExchange)ExchangeBuilder.fanoutExchange(EXCHANGE).durable(true).build();
    }

    @Bean
    @Qualifier("ordersQueue")
    public Queue ordersQueue() {
        return QueueBuilder.durable(QUEUE).withArgument("x-dead-letter-exchange", "").withArgument("x-dead-letter-routing-key", QUEUE_DLQ).build();
    }

    @Bean
    public Binding bindingOrdersImport(@Qualifier("ordersExchange") FanoutExchange ordersExchange, @Qualifier("ordersQueue") Queue ordersQueue) {
        return BindingBuilder.bind(ordersQueue).to(ordersExchange);
    }

    @Bean
    public Queue ordersQueueDLQ() {
        return new Queue(QUEUE_DLQ, true);
    }
}
