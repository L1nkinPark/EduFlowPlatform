package com.lms.frontend.controller;

import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.model.response.OrderHistoryResponse;
import com.lms.frontend.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/student")
public class StudentInvoiceController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/invoice")
    public String showInvoicePage(Model model) {
        ApiResponse<List<OrderHistoryResponse>> apiResponse = orderService.getOrderHistory();
        if (apiResponse != null && apiResponse.getPayload() != null) {
            model.addAttribute("orders", apiResponse.getPayload());
        } else {
            model.addAttribute("orders", Collections.emptyList());
        }
        return "student-invoice";
    }

}
