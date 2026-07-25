package com.lms.backend.model.response;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
public class OrderHistoryResponse implements Serializable {
    private Long orderId;
    private double totalAmount;
    private Date orderDate;
    private List<String> courseNames;
}
