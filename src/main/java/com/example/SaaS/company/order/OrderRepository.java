package com.example.SaaS.company.order;

import com.example.SaaS.common.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository  extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.customerId.id = :customerId " +
            "AND (:status IS NULL OR o.status = :status)")
    Page<Order> findByCustomer(@Param("customerId") Long customerId,
                               @Param("status") String status,
                               Pageable pageable);

    @Query("SELECT o FROM Order o WHERE o.companyId.id = :companyId " +
            "AND (:status IS NULL OR o.status = :status)")
    Page<Order> findByCompany(@Param("companyId") Long companyId,
                              @Param("status") String status,
                              Pageable pageable);
    @Query("SELECT o FROM Order o WHERE (:status IS NULL OR o.status = :status)")
    Page<Order> findAllByStatus(@Param("status") String status, Pageable pageable);
}
