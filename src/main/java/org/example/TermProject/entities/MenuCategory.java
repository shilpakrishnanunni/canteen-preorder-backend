package org.example.TermProject.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="menu_categories")
public class MenuCategory {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique=true, length=100)
    private String name;

    @Column(nullable = false, unique=true, length=100)
    private String slug;

    @Column(nullable = false)
    private Boolean status;

    protected MenuCategory() {
    }

    protected MenuCategory(
            String name,
            String slug
    ) {
        this.name = name;
        this.slug = slug;
        this.status = true;
    }
}
