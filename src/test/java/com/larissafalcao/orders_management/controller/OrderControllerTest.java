package com.larissafalcao.orders_management.controller;

import com.larissafalcao.orders_management.controller.dto.response.OrderResponse;
import com.larissafalcao.orders_management.service.OrderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;

import static org.mockito.Mockito.*;

class OrderControllerTest {
    @Mock
    OrderService orderService;
    @InjectMocks
    OrderController orderController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getOrdersByDate_returnsOrders_whenOrdersExist() {
        Page<OrderResponse> mockPage = mock(Page.class);
        when(mockPage.isEmpty()).thenReturn(false);
        when(orderService.getOrdersByDate(any(LocalDate.class), any(LocalDate.class), anyInt(), anyInt())).thenReturn(mockPage);

        ResponseEntity<Page<OrderResponse>> result = orderController.getOrdersByDate(LocalDate.of(2024, Month.OCTOBER, 1), LocalDate.of(2024, Month.OCTOBER, 31), 0, 10);
        Assertions.assertEquals(ResponseEntity.ok(mockPage), result);
    }

    @Test
    void getOrdersByDate_returnsNoContent_whenNoOrdersExist() {
        Page<OrderResponse> mockPage = mock(Page.class);
        when(mockPage.isEmpty()).thenReturn(true);
        when(orderService.getOrdersByDate(any(LocalDate.class), any(LocalDate.class), anyInt(), anyInt())).thenReturn(mockPage);

        ResponseEntity<Page<OrderResponse>> result = orderController.getOrdersByDate(LocalDate.of(2024, Month.OCTOBER, 1), LocalDate.of(2024, Month.OCTOBER, 31), 0, 10);
        Assertions.assertEquals(ResponseEntity.noContent().build(), result);
    }

    @Test
    void getOrder_returnsOrder_whenOrderExists() {
        OrderResponse mockOrder = new OrderResponse(1L, 1L, "2024-10-01", new BigDecimal("100.00"), "COMPLETED");
        when(orderService.getOrder(anyLong())).thenReturn(mockOrder);

        ResponseEntity<OrderResponse> result = orderController.getOrder(1L);
        Assertions.assertEquals(ResponseEntity.ok(mockOrder), result);
    }

    @Test
    void getOrder_returnsNotFound_whenOrderDoesNotExist() {
        when(orderService.getOrder(anyLong())).thenReturn(null);

        ResponseEntity<OrderResponse> result = orderController.getOrder(1111L);

        Assertions.assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
    }
}

