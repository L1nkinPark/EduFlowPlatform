package com.lms.backend;

import com.lms.backend.model.entity.Account;
import com.lms.backend.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@org.springframework.cache.annotation.EnableCaching
public class BackendApplication  implements CommandLineRunner {
//
//    @Autowired
//    private AccountService accountService;

    @Autowired
    private PasswordEncoder passwordEncoder;


//
//    @Override
//    public void run(String... args) throws Exception {
//        Account admin = accountService.findByUsername("admin");
//        if (admin == null) {
//            admin = new Account();
//            admin.setUsername("admin");
//            admin.setFullName("admin");
//            admin.setPassword(passwordEncoder.encode("admin"));
//            admin.setRole("ADMIN");
//            admin.setStatus(true);
//
//            accountService.saveOrUpdate(admin);
//        }
//
//        Account user = accountService.findByUsername("user");
//        if (user == null) {
//            user = new Account();
//            user.setUsername("user");
//            user.setFullName("user");
//            user.setPassword(passwordEncoder.encode("user"));
//            user.setRole("USER");
//            user.setStatus(true);
//
//            accountService.saveOrUpdate(user);
//        }
//    }

    public static void main(String[] args) {
        SpringApplication.run(BackendApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        System.out.println("PASSWORD ENCRYPTION: " + passwordEncoder.encode("123"));
    }
}

