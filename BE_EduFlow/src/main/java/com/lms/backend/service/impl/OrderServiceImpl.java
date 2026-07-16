package com.lms.backend.service.impl;

import com.lms.backend.model.entity.Account;
import com.lms.backend.model.entity.Course;
import com.lms.backend.model.entity.Order;
import com.lms.backend.model.entity.OrderItem;
import com.lms.backend.repository.CourseRepository;
import com.lms.backend.repository.OrderRepository;
import com.lms.backend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public Order createOrder(Account user, String courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found: " + courseId));

        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(new Date());
        order.setTotalAmount(course.getPrice());

        OrderItem item = new OrderItem();
        item.setOrder(order);
        item.setCourse(course);
        item.setQuantity(1);
        item.setPrice(course.getPrice());

        List<OrderItem> items = new ArrayList<>();
        items.add(item);
        order.setOrderItems(items);

        return orderRepository.save(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Course> getPurchasedCourses(Account user) {
        List<Order> orders = orderRepository.findByUser(user);
        List<Course> courses = new ArrayList<>();
        for (Order order : orders) {
            if (order.getOrderItems() != null) {
                for (OrderItem item : order.getOrderItems()) {
                    if (item.getCourse() != null) {
                        courses.add(item.getCourse());
                    }
                }
            }
        }
        return courses;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean hasPurchasedCourse(Account user, String courseId) {
        List<Order> orders = orderRepository.findByUser(user);
        for (Order order : orders) {
            if (order.getOrderItems() != null) {
                for (OrderItem item : order.getOrderItems()) {
                    if (item.getCourse() != null && courseId.equals(item.getCourse().getCourseId())) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}
