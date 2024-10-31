package com.larissafalcao.orders_management.consumer;

import com.larissafalcao.orders_management.controller.dto.request.OrderRequest;
import com.larissafalcao.orders_management.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

import static com.larissafalcao.orders_management.config.OrdersManagementConfiguration.QUEUE;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderConsumer {
    private final OrderService orderService;

    @RabbitListener(
            queues = QUEUE,
            concurrency = "${rabbitmq.queue.orders.return.concurrency}"
    )
    public void consume(OrderRequest request) {
        log.info("Order received for orderId [{}]", request.getOrderId());
        orderService.calculateTotalOrder(request);
    }
}
