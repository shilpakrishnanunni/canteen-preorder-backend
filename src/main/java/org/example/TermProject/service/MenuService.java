package org.example.TermProject.service;

import org.example.TermProject.dto.MenuCategoryResponse;
import org.example.TermProject.dto.MenuItemResponse;
import org.example.TermProject.entities.MenuCategory;
import org.example.TermProject.entities.MenuItem;
import org.example.TermProject.repository.MenuCategoryRepository;
import org.example.TermProject.repository.MenuItemRepository;
import org.springframework.stereotype.Service;

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
                        item.getImageUrl()
                ))
                .toList();
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
}
