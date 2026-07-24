package com.lms.backend.repository;

import com.lms.backend.model.entity.Account;
import com.lms.backend.model.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(Account user);

    @Query("SELECT COALESCE(SUM(o.totalAmount), 0) FROM Order o")
    double sumTotalRevenue();

    List<Order> findTop10ByOrderByOrderDateDesc();
}
