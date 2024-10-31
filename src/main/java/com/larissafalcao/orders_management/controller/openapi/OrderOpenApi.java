package com.larissafalcao.orders_management.controller.openapi;

import com.larissafalcao.orders_management.controller.dto.response.OrderResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;

public interface OrderOpenApi {

    @Operation(summary = "Get Orders", description = "Get Orders.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders successfully found"),
            @ApiResponse(responseCode = "204", description = "No content"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "400", description = "Invalid data provided"),
    })
    ResponseEntity<Page<OrderResponse>> getOrdersByDate(LocalDate startDate, LocalDate endDate, int page, int size);

    @Operation(summary = "Get Order by ID", description = "Get Order by ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order successfully found"),
            @ApiResponse(responseCode = "500", description = "Internal server error"),
            @ApiResponse(responseCode = "400", description = "Invalid data provided"),
    })
    ResponseEntity<OrderResponse> getOrder(Long orderId);
}
