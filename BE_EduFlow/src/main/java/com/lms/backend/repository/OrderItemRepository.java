package com.lms.backend.repository;

import com.lms.backend.model.entity.Account;
import com.lms.backend.model.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

    // Tất cả order item thuộc các course do 1 instructor sở hữu (dùng cho trang Orders/Earnings của instructor).
    @Query("SELECT oi FROM OrderItem oi WHERE oi.course.account = :instructor ORDER BY oi.order.orderDate DESC")
    List<OrderItem> findByInstructor(@Param("instructor") Account instructor);

    @Query("SELECT COALESCE(SUM(oi.price), 0) FROM OrderItem oi WHERE oi.course.account = :instructor")
    double sumRevenueByInstructor(@Param("instructor") Account instructor);

    @Query("SELECT COUNT(oi) FROM OrderItem oi WHERE oi.course.account = :instructor")
    long countSalesByInstructor(@Param("instructor") Account instructor);

    // Danh sách học viên (không trùng) đã mua ít nhất 1 khóa học của instructor.
    @Query("SELECT DISTINCT oi.order.user FROM OrderItem oi WHERE oi.course.account = :instructor")
    List<Account> findDistinctStudentsByInstructor(@Param("instructor") Account instructor);
}
