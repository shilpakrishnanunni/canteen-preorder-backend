package org.example.TermProject.controller;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.Valid;
import org.example.TermProject.dto.*;
import org.example.TermProject.service.OrderService;
import org.example.TermProject.service.StaffService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequestMapping("/staff")
public class StaffController {
    public StaffController(
            StaffService staffService,
            OrderService orderService
    ) {
        this.staffService = staffService;
        this.orderService = orderService;
    }

    private final StaffService staffService;
    private final OrderService orderService;

    @GetMapping("/")
    public String root() {
        return "Hello, Staff!";
    }

    @GetMapping("/orders")
    public List<StaffOrderResponse> getOrders(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return orderService.getOrdersByDate(startDate, endDate);
    }

    @PatchMapping("/orderStatus")
    public ResponseEntity<Void> updateOrderStatus(
            @Valid @RequestBody UpdateOrderStatusRequest request
    ) {
        orderService.updateOrderStatus(request.orderToken(), request.status());
        return ResponseEntity.noContent().build();
    }

    @GetMapping ("/menu")
    public List<MenuItemResponse> getStaffMenu(){
        return staffService.getStaffMenu();
    }

    @PatchMapping("/updateMenuItems")
    public MenuItemResponse updateMenuItem(
            @RequestBody UpdateMenuItemRequest request) {
        return staffService.updateMenuItem(request);
    }

    @DeleteMapping("/deleteMenuItem/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMenuItem(@PathVariable Long id) {
        staffService.deleteMenuItem(id);
    }

    @PostMapping("/addMenuItems")
    @ResponseStatus(HttpStatus.CREATED)
    public MenuItemResponse addMenuItem(
            @RequestBody CreateMenuItemRequest request) {
        return staffService.addMenuItem(request);
    }
        // TODO create/update/delete menu items and categories
}
