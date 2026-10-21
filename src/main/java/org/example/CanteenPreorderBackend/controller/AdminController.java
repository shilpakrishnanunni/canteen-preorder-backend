package org.example.CanteenPreorderBackend.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/admin")
public class AdminController {
    // TODO leave admin apis for last
    public AdminController() {

    }

    @GetMapping("/")
    public String root() {
        return "Hello, Admin!";
    }
}
