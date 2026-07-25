package com.lms.frontend.model.response;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class OrderHistoryResponse {
    private Long orderId;
    private double totalAmount;
    private Date orderDate;
    private List<String> courseNames;
}
