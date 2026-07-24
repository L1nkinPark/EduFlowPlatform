package com.lms.frontend.controller;

import com.lms.frontend.model.response.ApiResponse;
import com.lms.frontend.model.response.InstructorStudentResponse;
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
public class InstructorStudentController {

    @Autowired
    private InstructorService instructorService;

    @GetMapping("/student")
    public String showStudentPage(Model model) {
        ApiResponse<List<InstructorStudentResponse>> apiResponse = instructorService.getStudents();
        List<InstructorStudentResponse> students = (apiResponse != null && apiResponse.getPayload() != null)
                ? apiResponse.getPayload()
                : Collections.emptyList();

        model.addAttribute("students", students);
        return "instructor-students";
    }
}
