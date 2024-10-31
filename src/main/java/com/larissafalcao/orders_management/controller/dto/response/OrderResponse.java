package com.larissafalcao.orders_management.controller.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderResponse {

    private Long orderId;
    private Long customerId;
    private String orderDate;
    private BigDecimal totalValue;
    private String status;

}
