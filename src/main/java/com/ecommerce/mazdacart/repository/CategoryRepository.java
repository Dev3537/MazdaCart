package com.ecommerce.mazdacart.repository;

import com.ecommerce.mazdacart.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface CategoryRepository extends JpaRepository<Category,Long> {
    Category findByCategoryName(String categoryName);

    Category findByCategoryNameIgnoreCase (String categoryName);

    @Modifying
    @Query(nativeQuery = true,value = "update category set category_name=:categoryName where category_id=:categoryId")
    void updateCategoryName (String categoryName, Long categoryId);
}

