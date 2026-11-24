package com.example.SaaS.superAdmin.dashboard.repository;

import com.example.SaaS.common.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.time.LocalDateTime;

public interface DashboardRepository extends JpaRepository <Company, Long>{

    @Query("""
            SELECT COUNT(c)
            FROM Company c
            """)
    Long totalCompanies();

    @Query("""
            SELECT COUNT(c)
            FROM Company c
            WHERE c.createdAt >= :today
            """)
    Long newCompaniesToday(Instant today);

    @Query("""
            SELECT COUNT(s)
            FROM Subscription s
            WHERE s.status='ACTIVE'
            """)
    Long activeSubscriptions();

    @Query("""
            SELECT COUNT(s)
            FROM Subscription s
            WHERE s.endDate < :now
            """)
    Long expiredSubscriptions(LocalDateTime now);

    @Query("""
            SELECT COUNT(o)
            FROM Order o
            """)
    Long totalOrders();

    @Query("""
            SELECT COUNT(o)
            FROM Order o
            WHERE o.status='PENDING'
            """)
    Long pendingOrders();

    @Query("""
            SELECT COUNT(o)
            FROM Order o
            WHERE o.status='COMPLETED'
            """)
    Long completedOrders();

}
