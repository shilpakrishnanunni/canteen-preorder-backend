package org.example.CanteenPreorderBackend.service;

import org.example.CanteenPreorderBackend.dto.MenuCategoryResponse;
import org.example.CanteenPreorderBackend.dto.MenuItemResponse;
import org.example.CanteenPreorderBackend.entities.MenuCategory;
import org.example.CanteenPreorderBackend.entities.MenuItem;
import org.example.CanteenPreorderBackend.repository.MenuCategoryRepository;
import org.example.CanteenPreorderBackend.repository.MenuItemRepository;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import org.example.CanteenPreorderBackend.dto.CreateMenuItemRequest;
import java.math.BigDecimal;

import java.util.List;

@Service
public class MenuService {
    private final MenuItemRepository menuItemRepository;
    private final MenuCategoryRepository menuCategoryRepository;

    public MenuService(
            MenuItemRepository menuItemRepository,
            MenuCategoryRepository menuCategoryRepository
    ) {
        this.menuItemRepository = menuItemRepository;
        this.menuCategoryRepository = menuCategoryRepository;
    }

    public List<MenuItemResponse> getMenu(String categorySlug) {
        List<MenuItem> items;

        if (categorySlug==null) {
            items = menuItemRepository.findByAvailableTrue();
        } else {
            items = menuItemRepository.findByCategorySlugAndAvailableTrue(categorySlug);
        }
        return items.stream()
                .map(item -> new MenuItemResponse(
                        item.getCategory().getId(),
                        item.getCategory().getName(),
                        item.getId(),
                        item.getName(),
                        item.getDescription(),
                        item.getPrice(),
                        item.getImageUrl(),
                        item.getAvailable()
                ))
                .toList();
    }

    public MenuItemResponse getMenuItemByid(Long id){
        MenuItem item = menuItemRepository.findById(id)
                .filter(MenuItem::getAvailable)
                .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.NOT_FOUND,
                    "Menu Item not found"
                ));

                return new MenuItemResponse(
                    item.getCategory().getId(),
                    item.getCategory().getName(),
                    item.getId(),
                    item.getName(),
                    item.getDescription(),
                    item.getPrice(),
                    item.getImageUrl(),
                        item.getAvailable()
                );
    }
    public List<MenuCategoryResponse> getCategories() {
        return menuCategoryRepository.findAll()
                .stream()
                .filter(MenuCategory::getStatus)
                .map(category -> new MenuCategoryResponse(
                        category.getId(),
                        category.getName(),
                        category.getSlug()
                ))
                .toList();
    }


    //Add Menu ITem
    public MenuItemResponse addMenuItem(CreateMenuItemRequest request) {
    MenuCategory category = menuCategoryRepository.findById(request.categoryId())
            .filter(MenuCategory::getStatus)
            .orElseThrow(() -> new ResponseStatusException(
                    HttpStatus.BAD_REQUEST,
                    "Invalid or inactive category"
            ));

    if (request.name() == null || request.name().isBlank()
            || request.price() == null
            || request.price().compareTo(BigDecimal.ZERO) <= 0) {
        throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST,
                "Name and a positive price are required"
        );
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
            saved.getImageUrl(),
            saved.getAvailable()
    );
}
}
