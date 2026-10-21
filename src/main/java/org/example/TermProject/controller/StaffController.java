package org.example.TermProject.controller;

import java.time.LocalDate;
import java.util.List;

import org.example.TermProject.dto.StaffOrderResponse;
import org.example.TermProject.entities.OrderStatus;
import org.example.TermProject.service.StaffService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.example.TermProject.dto.MenuItemResponse;
import org.example.TermProject.dto.UpdateMenuItemRequest;
import org.example.TermProject.dto.MenuItemResponse;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.example.TermProject.dto.CreateMenuItemRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@RestController
@RequestMapping("/staff")
public class StaffController {
    public StaffController(StaffService staffService) {
        this.staffService = staffService;
    }

    private final StaffService staffService;

    @GetMapping("/orders")
    public List<StaffOrderResponse> getOrders(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate
    ) {
        return staffService.getOrdersByDate(startDate, endDate);
    }

    @GetMapping("/")
    public String root() {
        return "Hello, Staff!";
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
