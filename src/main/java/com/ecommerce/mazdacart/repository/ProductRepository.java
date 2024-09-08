package com.ecommerce.mazdacart.repository;

import com.ecommerce.mazdacart.model.Category;
import com.ecommerce.mazdacart.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {
	List<Product> findByCategoryOrderByPriceAsc (Category category);

	Page<Product> findByProductNameContainingIgnoreCase (String keyword, Pageable pageable);

	Product findByProductName (String productName);

	Product findByProductNameIgnoreCase (String productName);

	Page<Product> findByCategory (Category category, Pageable pageable);
}
