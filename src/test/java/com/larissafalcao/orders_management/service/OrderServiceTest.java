package com.larissafalcao.orders_management.service;

import com.larissafalcao.orders_management.controller.dto.request.OrderRequest;
import com.larissafalcao.orders_management.controller.dto.response.OrderResponse;
import com.larissafalcao.orders_management.exception.EntityNotFoundException;
import com.larissafalcao.orders_management.persistence.domain.OrderEntity;
import com.larissafalcao.orders_management.persistence.domain.OrderProductEntity;
import com.larissafalcao.orders_management.persistence.repository.OrderProductRepository;
import com.larissafalcao.orders_management.persistence.repository.OrderRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

class OrderServiceTest {
    @Mock
    OrderRepository orderRepository;
    @Mock
    OrderProductRepository orderProductEntityRepository;
    @Mock
    ModelMapper modelMapper;
    @InjectMocks
    OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getOrdersByDate_returnsOrders_whenOrdersExist() {
        Page<OrderEntity> mockPage = new PageImpl<>(List.of(
                OrderEntity.builder()
                .orderId(1L)
                .customerId(12345L)
                .orderDate(LocalDate.of(2023, 10, 1))
                .calculateDate(LocalDate.now())
                .totalValue(new BigDecimal("100.00"))
                .status("PENDING")
                .build()));
        when(orderRepository.findAllByCalculateDateBetween(any(LocalDate.class), any(LocalDate.class), any(Pageable.class))).thenReturn(mockPage);


        Page<OrderResponse> result = orderService.getOrdersByDate(LocalDate.of(2024, Month.OCTOBER, 1), LocalDate.of(2024, Month.OCTOBER, 31), 0, 10);
        Assertions.assertFalse(result.isEmpty());
    }

    @Test
    void getOrdersByDate_returnsNoOrders_whenNoOrdersExist() {
        Page<OrderEntity> emptyPage = new PageImpl<>(Collections.emptyList());
        when(orderRepository.findAllByCalculateDateBetween(any(LocalDate.class), any(LocalDate.class), any(Pageable.class))).thenReturn(emptyPage);

        Page<OrderResponse> result = orderService.getOrdersByDate(LocalDate.of(2024, Month.OCTOBER, 1), LocalDate.of(2024, Month.OCTOBER, 31), 0, 10);
        Assertions.assertTrue(result.isEmpty());
    }

    @Test
    void getOrder_returnsOrder_whenOrderExists() {
        OrderEntity mockOrder = new OrderEntity();
        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(mockOrder));
        when(modelMapper.map(any(OrderEntity.class), eq(OrderResponse.class))).thenReturn(new OrderResponse());

        OrderResponse result = orderService.getOrder(1L);
        Assertions.assertNotNull(result);
    }

    @Test
    void getOrder_throwsException_whenOrderDoesNotExist() {
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        Assertions.assertThrows(EntityNotFoundException.class, () -> orderService.getOrder(1L));
    }

    @Test
    void calculateTotalOrder_savesOrder_whenOrderDoesNotExist() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderId(1L);
        orderRequest.setItems(List.of(OrderRequest.OrderProduct.builder().productId(1L).unitPrice(new BigDecimal("10.00")).quantity(2).build()));
        when(orderRepository.findById(anyLong())).thenReturn(Optional.empty());

        orderService.calculateTotalOrder(orderRequest);

        verify(orderRepository, times(1)).save(any(OrderEntity.class));
        verify(orderProductEntityRepository, times(1)).save(any(OrderProductEntity.class));
    }

    @Test
    void calculateTotalOrder_doesNotSaveOrder_whenOrderExists() {
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setOrderId(1L);

        when(orderRepository.findById(anyLong())).thenReturn(Optional.of(new OrderEntity()));

        orderService.calculateTotalOrder(orderRequest);

        verify(orderRepository, never()).save(any(OrderEntity.class));
        verify(orderProductEntityRepository, never()).save(any(OrderProductEntity.class));
    }

}
