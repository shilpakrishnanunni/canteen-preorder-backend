package org.example.TermProject.controller;

import org.example.TermProject.dto.MenuCategoryResponse;
import org.example.TermProject.dto.MenuItemResponse;
import org.example.TermProject.service.MenuService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/categories")
    public List<MenuCategoryResponse> getCategories() {
        return menuService.getCategories();
    }

}
