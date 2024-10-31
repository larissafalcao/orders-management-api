package com.larissafalcao.orders_management.persistence.repository;

import com.larissafalcao.orders_management.persistence.domain.OrderProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProductEntity, Long> {
}
