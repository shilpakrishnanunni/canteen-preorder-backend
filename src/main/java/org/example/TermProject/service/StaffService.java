package org.example.TermProject.service;


import org.springframework.stereotype.Service;


import org.springframework.transaction.annotation.Transactional;
//import org.example.TermProject.dto.OrderResponse;
import org.example.TermProject.dto.StaffOrderResponse;
import org.example.TermProject.dto.StaffOrderItemResponse;
import org.example.TermProject.entities.Order;
import org.example.TermProject.entities.OrderStatus;
import org.example.TermProject.repository.OrderRepository;
import org.example.TermProject.repository.OrderItemRepository;
import org.example.TermProject.dto.MenuItemResponse;
import org.example.TermProject.service.MenuService;
import org.example.TermProject.dto.MenuItemResponse;
import org.example.TermProject.dto.UpdateMenuItemRequest;
import org.example.TermProject.entities.MenuItem;
import org.example.TermProject.repository.MenuItemRepository;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import org.example.TermProject.dto.CreateMenuItemRequest;
import org.example.TermProject.entities.MenuCategory;
import org.example.TermProject.repository.MenuCategoryRepository;

import java.util.List;

@Service 
public class StaffService {
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final MenuService menuService;
    private final MenuItemRepository menuItemRepository;
    private final MenuCategoryRepository menuCategoryRepository;

    public StaffService(
        OrderItemRepository orderItemRepository,
        OrderRepository orderRepository,
        MenuService menuService,
        MenuItemRepository menuItemRepository,
        MenuCategoryRepository menuCategoryRepository
    ){
    this.orderItemRepository = orderItemRepository;
    this.orderRepository = orderRepository;
    this.menuService = menuService;
    this.menuItemRepository = menuItemRepository;
    this.menuCategoryRepository = menuCategoryRepository;
    }



@Transactional(readOnly = true)
 public List<StaffOrderResponse> getOrdersByStatus(List<OrderStatus> statuses){

        List<Order> orders;

        if (statuses == null || statuses.isEmpty()) {
            orders = orderRepository.findAll();
        } else {
            orders = orderRepository.findByStatusIn(statuses);
        }

        return orders.stream()
        .map(order-> new StaffOrderResponse(
            order.getId(),
            order.getOrderToken(),
            order.getStatus(),
            order.getTotalAmount(),
            order.getCreatedAt(),
            order.getItems().stream()
                .map(item -> new StaffOrderItemResponse(
                    item.getMenuItem().getName(),
                    item.getQuantity(),
                    item.getUnitPrice()
                ))
                .toList()
        ))
        .toList();
    }
@Transactional(readOnly = true)
public List<MenuItemResponse>getStaffMenu(){
    return menuService.getMenu(null);
}

    private StaffOrderResponse toStaffOrderResponse(Order order) {
        List<StaffOrderItemResponse> items = order.getItems()
                .stream()
                .map(item -> new StaffOrderItemResponse(
                        item.getMenuItem().getName(),
                        item.getQuantity(),
                        item.getUnitPrice()
                ))
                .toList();

        return new StaffOrderResponse(
                order.getId(),
                order.getOrderToken(),
                order.getStatus(),
                order.getTotalAmount(),
                order.getCreatedAt(),
                items
        );
    }

    @Transactional
public MenuItemResponse updateMenuItem(UpdateMenuItemRequest request) {
    MenuItem item = menuItemRepository.findById(request.id())
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Menu item not found"));

    if (request.name() != null && !request.name().isBlank()) {
        item.setName(request.name().trim());
    }
    if (request.description() != null) {
        item.setDescription(request.description());
    }
    if (request.price() != null && request.price().signum() > 0) {
        item.setPrice(request.price());
    }
    if (request.imageUrl() != null) {
        item.setImageUrl(request.imageUrl());
    }
    if (request.available() != null) {
        item.setAvailable(request.available());
    }

    MenuItem saved = menuItemRepository.save(item);

    return new MenuItemResponse(
            saved.getCategory().getId(),
            saved.getCategory().getName(),
            saved.getId(),
            saved.getName(),
            saved.getDescription(),
            saved.getPrice(),
            saved.getImageUrl()
    );
}

@Transactional
public void deleteMenuItem(Long id) {
    MenuItem item = menuItemRepository.findById(id)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Menu item not found"));

    item.setAvailable(false);
    menuItemRepository.save(item);
}
@Transactional
public MenuItemResponse addMenuItem(CreateMenuItemRequest request) {
    MenuCategory category = menuCategoryRepository.findById(request.categoryId())
            .filter(MenuCategory::getStatus)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Invalid or inactive category"));

    if (request.name() == null || request.name().isBlank()
            || request.price() == null || request.price().signum() <= 0) {
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Name and positive price are required");
    }

    MenuItem item = new MenuItem(
            category,
            request.name().trim(),
            request.description(),
            request.price(),
            request.imageUrl()
    );

    MenuItem saved = menuItemRepository.save(item);

    return new MenuItemResponse(
            saved.getCategory().getId(),
            saved.getCategory().getName(),
            saved.getId(),
            saved.getName(),
            saved.getDescription(),
            saved.getPrice(),
            saved.getImageUrl()
    );
}
}
