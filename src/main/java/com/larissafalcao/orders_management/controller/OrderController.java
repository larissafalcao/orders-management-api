package com.larissafalcao.orders_management.controller;

import com.larissafalcao.orders_management.controller.dto.response.OrderResponse;
import com.larissafalcao.orders_management.controller.openapi.OrderOpenApi;
import com.larissafalcao.orders_management.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/v1/orders")
@AllArgsConstructor
public class OrderController implements OrderOpenApi {
    private final OrderService orderService;

    @Override
    @GetMapping("/date")
    public ResponseEntity<Page<OrderResponse>> getOrdersByDate(@RequestParam LocalDate startDate,
                                                               @RequestParam LocalDate endDate,
                                                               @RequestParam(defaultValue = "0") int page,
                                                               @RequestParam(defaultValue = "10") int size) {
        Page<OrderResponse> orders = orderService.getOrdersByDate(startDate, endDate, page, size);
        if(orders.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(orders);
    }

    @Override
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long orderId) {
        OrderResponse order = orderService.getOrder(orderId);
        if(order == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(order);
    }
}
