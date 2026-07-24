package com.lms.frontend.controller;

import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.model.response.InstructorOrderResponse;
import com.lms.frontend.service.InstructorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/instructor")
public class InstructorOrderController {

    @Autowired
    private InstructorService instructorService;

    @GetMapping("/order")
    public String showOrderPage(Model model) {
        ApiResponse<List<InstructorOrderResponse>> apiResponse = instructorService.getOrders();
        List<InstructorOrderResponse> orders = (apiResponse != null && apiResponse.getPayload() != null)
                ? apiResponse.getPayload()
                : Collections.emptyList();

        model.addAttribute("orders", orders);
        return "instructor-orders";
    }

}
