package org.example.TermProject.controller;

import java.util.List;
import org.example.TermProject.dto.StaffOrderResponse;
import org.example.TermProject.entities.OrderStatus;
import org.example.TermProject.service.StaffService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/staff")
public class StaffController {
    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    private final StaffService staffService;

    @GetMapping("/orders")
    public List<StaffOrderResponse> getOrders(
            @RequestParam(required = false) List<OrderStatus> status) {
        return staffService.getOrdersByStatus(status);
    }

    @GetMapping("/")
    public String root() {
        return "Hello, Staff!";
    }

    // TODO create/update/delete menu items and categories
}
