package org.example.CanteenPreorderBackend.controller;

import org.example.CanteenPreorderBackend.dto.MenuCategoryResponse;
import org.example.CanteenPreorderBackend.dto.MenuItemResponse;
import org.example.CanteenPreorderBackend.service.MenuService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import org.example.CanteenPreorderBackend.dto.CreateMenuItemRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

@RestController
@RequestMapping("/menu")
public class MenuController {
    private final MenuService menuService;

    MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping
    public List<MenuItemResponse> getMenu(
            @RequestParam(required=false) String categorySlug
    ) {
        return menuService.getMenu(categorySlug);
    }

    @GetMapping("/{id}")
    public MenuItemResponse getMenuItemById(
            @PathVariable Long id
    ){
        return menuService.getMenuItemByid(id);
    }

    @GetMapping("/categories")
    public List<MenuCategoryResponse> getCategories() {
        return menuService.getCategories();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public MenuItemResponse addMenuItem(
            @RequestBody CreateMenuItemRequest request) {
        return menuService.addMenuItem(request);
}
}
