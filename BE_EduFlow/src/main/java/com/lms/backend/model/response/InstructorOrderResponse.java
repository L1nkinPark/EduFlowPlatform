package com.lms.backend.model.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;

@Getter
@Setter
public class InstructorOrderResponse implements Serializable {
    private Long orderId;
    private String courseId;
    private String courseName;
    private String studentName;
    private String studentEmail;
    private double price;
    private Date orderDate;
}
