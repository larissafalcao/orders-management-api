package com.larissafalcao.orders_management.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequest {

    @Data
    @Builder
    public static class OrderProduct {
        private Long productId;
        private BigDecimal unitPrice;
        private Integer quantity;
    }

    private Long customerId;
    private Long orderId;
    private LocalDate orderDate;
    private String status;
    private List<OrderProduct> items;
}
