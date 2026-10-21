package org.example.CanteenPreorderBackend.controller;

import java.time.LocalDate;
import java.util.List;

import jakarta.validation.Valid;
import org.example.CanteenPreorderBackend.dto.*;
import org.example.CanteenPreorderBackend.service.OrderService;
import org.example.CanteenPreorderBackend.service.StaffService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

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

    @PutMapping("/updateMenuItem")
    public MenuItemResponse updateMenuItem(
            @RequestBody UpdateMenuItemRequest request) {
        return staffService.updateMenuItem(request);
    }

    @DeleteMapping("/deleteMenuItem/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMenuItem(@PathVariable Long id) {
        staffService.deleteMenuItem(id);
    }

    @PostMapping("/addMenuItem")
    @ResponseStatus(HttpStatus.CREATED)
    public MenuItemResponse addMenuItem(
            @RequestBody CreateMenuItemRequest request) {
        return staffService.addMenuItem(request);
    }
}
