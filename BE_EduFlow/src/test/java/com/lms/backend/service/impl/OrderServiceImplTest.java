package com.lms.backend.service.impl;

import com.lms.backend.model.entity.Account;
import com.lms.backend.model.entity.Course;
import com.lms.backend.model.entity.Order;
import com.lms.backend.model.entity.OrderItem;
import com.lms.backend.repository.CourseRepository;
import com.lms.backend.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private CourseRepository courseRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Account user;
    private Course course;

    @BeforeEach
    void setUp() {
        user = new Account();
        user.setAccountId(1L);
        user.setUsername("student@test.com");

        course = new Course();
        course.setCourseId("course123");
        course.setCourseName("Test Course");
        course.setPrice(99.0);
    }

    @Test
    void testCreateOrder_Success() {
        when(courseRepository.findById("course123")).thenReturn(Optional.of(course));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> {
            Order o = invocation.getArgument(0);
            o.setId(10L);
            return o;
        });

        Order result = orderService.createOrder(user, "course123");

        assertNotNull(result);
        assertEquals(10L, result.getId());
        assertEquals(user, result.getUser());
        assertEquals(99.0, result.getTotalAmount());
        assertEquals(1, result.getOrderItems().size());

        OrderItem item = result.getOrderItems().get(0);
        assertEquals(course, item.getCourse());
        assertEquals(99.0, item.getPrice());
        assertEquals(1, item.getQuantity());

        verify(courseRepository, times(1)).findById("course123");
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void testCreateOrder_CourseNotFound() {
        when(courseRepository.findById("invalid")).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> orderService.createOrder(user, "invalid"));

        verify(courseRepository, times(1)).findById("invalid");
        verify(orderRepository, never()).save(any(Order.class));
    }

    @Test
    void testGetPurchasedCourses() {
        Order order = new Order();
        OrderItem item = new OrderItem();
        item.setCourse(course);
        List<OrderItem> items = new ArrayList<>();
        items.add(item);
        order.setOrderItems(items);

        List<Order> orders = new ArrayList<>();
        orders.add(order);

        when(orderRepository.findByUser(user)).thenReturn(orders);

        List<Course> result = orderService.getPurchasedCourses(user);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(course, result.get(0));

        verify(orderRepository, times(1)).findByUser(user);
    }

    @Test
    void testHasPurchasedCourse_True() {
        Order order = new Order();
        OrderItem item = new OrderItem();
        item.setCourse(course);
        List<OrderItem> items = new ArrayList<>();
        items.add(item);
        order.setOrderItems(items);

        List<Order> orders = new ArrayList<>();
        orders.add(order);

        when(orderRepository.findByUser(user)).thenReturn(orders);

        boolean result = orderService.hasPurchasedCourse(user, "course123");

        assertTrue(result);
        verify(orderRepository, times(1)).findByUser(user);
    }

    @Test
    void testHasPurchasedCourse_False() {
        when(orderRepository.findByUser(user)).thenReturn(new ArrayList<>());

        boolean result = orderService.hasPurchasedCourse(user, "course123");

        assertFalse(result);
        verify(orderRepository, times(1)).findByUser(user);
    }
}
