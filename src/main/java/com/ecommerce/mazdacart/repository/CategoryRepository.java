package com.ecommerce.mazdacart.repository;

import com.ecommerce.mazdacart.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    Category findByCategoryName(String categoryName);

    Category findByCategoryNameIgnoreCase (String categoryName);
}

