package com.lms.backend.controller;

import com.lms.backend.model.request.AccountRequest;
import com.lms.backend.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//@RestController
//@RequestMapping("/api/account")
public class AccountController {

//    @Autowired
//    AccountRepository accountRepository;
//
//    @PostMapping("/validate-email")
//    public boolean validateEmail(@RequestBody AccountRequest emailRequest) {
//        return accountRepository.existsByEmail(emailRequest.getEmail());
//    }
}
