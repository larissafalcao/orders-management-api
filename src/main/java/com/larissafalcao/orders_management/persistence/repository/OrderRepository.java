package com.larissafalcao.orders_management.persistence.repository;

import com.larissafalcao.orders_management.persistence.domain.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {
    Page<OrderEntity> findAllByCalculateDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
}
