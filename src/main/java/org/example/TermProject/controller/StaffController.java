package org.example.TermProject.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/staff")
public class StaffController {
    public StaffController() {

    }

    @GetMapping("/")
    public String root() {
        return "Hello, Staff!";
    }

    // TODO create/update/delete menu items and categories
}
