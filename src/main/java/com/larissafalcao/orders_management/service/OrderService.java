package com.larissafalcao.orders_management.service;

import com.larissafalcao.orders_management.controller.dto.request.OrderRequest;
import com.larissafalcao.orders_management.controller.dto.response.OrderResponse;
import com.larissafalcao.orders_management.exception.EntityNotFoundException;
import com.larissafalcao.orders_management.persistence.domain.OrderEntity;
import com.larissafalcao.orders_management.persistence.domain.OrderProductEntity;
import com.larissafalcao.orders_management.persistence.repository.OrderProductRepository;
import com.larissafalcao.orders_management.persistence.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static com.larissafalcao.orders_management.OrderStatusEnum.CALCULATED;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderProductRepository orderProductEntityRepository;
    private final ModelMapper modelMapper;

    public Page<OrderResponse> getOrdersByDate(LocalDate startDate, LocalDate endDate, int page, int size) {
        Pageable pageable = createPageable(page, size);
        Page<OrderEntity> orders = orderRepository.findAllByCalculateDateBetween(startDate, endDate, pageable);
        return orders.map(order -> modelMapper.map(order, OrderResponse.class));
    }

    public static Pageable createPageable(int page, int size) {
        int adjustedPage = (page > 0) ? page - 1 : 0;
        return PageRequest.of(adjustedPage, size);
    }

    public OrderResponse getOrder(Long orderId) {
        OrderEntity order = orderRepository.findById(orderId).orElseThrow(() -> new EntityNotFoundException("Order not found"));
        return modelMapper.map(order, OrderResponse.class);
    }

    @Transactional
    public void calculateTotalOrder(OrderRequest orderRequest) {
        Optional<OrderEntity> orderExistent = orderRepository.findById(orderRequest.getOrderId());
        if(orderExistent.isPresent()) {
            return;
        }

        BigDecimal totalValue = orderRequest.getItems().stream()
                .map(item -> item.getUnitPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        OrderEntity order = OrderEntity.builder()
                .orderId(orderRequest.getOrderId())
                .calculateDate(LocalDate.now())
                .totalValue(totalValue)
                .status(CALCULATED.name())
                .customerId(orderRequest.getCustomerId())
                .orderDate(orderRequest.getOrderDate())
                .totalValue(totalValue)
                .build();
        orderRepository.save(order);
        orderRequest.getItems().stream().parallel()
                .map(item -> OrderProductEntity.builder()
                        .order(order)
                        .productId(item.getProductId())
                        .productUnitPrice(item.getUnitPrice())
                        .quantity(item.getQuantity())
                        .build())
                .forEach(orderProductEntityRepository::save);
    }
}
