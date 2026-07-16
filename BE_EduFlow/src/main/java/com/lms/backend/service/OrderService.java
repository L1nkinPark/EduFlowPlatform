package com.lms.backend.service;

import com.lms.backend.model.entity.Account;
import com.lms.backend.model.entity.Course;
import com.lms.backend.model.entity.Order;

import java.util.List;

public interface OrderService {
    Order createOrder(Account user, String courseId);
    List<Course> getPurchasedCourses(Account user);
    boolean hasPurchasedCourse(Account user, String courseId);
}
