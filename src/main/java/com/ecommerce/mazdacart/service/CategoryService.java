package com.ecommerce.mazdacart.service;

import com.ecommerce.mazdacart.payload.CategoryDTO;
import com.ecommerce.mazdacart.payload.CategoryResponse;

/**
 * Interface for all Category business logic methods
 */
public interface CategoryService {

	/**
	 * Get all categories with Pagination with Sort Order by Criteria, throws exception if no categories found
	 *
	 * @param pageNumber
	 * @param pageSize
	 * @param sortBy
	 * @param sortOrder
	 * @return
	 */
	CategoryResponse getAllCategories (Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

	/**
	 * Create new Category, throws exception if given category already exists
	 *
	 * @param category
	 * @return
	 */
	CategoryDTO createCategory (CategoryDTO category);

	/**
	 * Update existing category, throws exception if to be updated Category already exists or to be updated category
	 * doesn't exist
	 *
	 * @param category
	 * @param categoryName
	 * @return
	 */
	CategoryDTO updateCategory (CategoryDTO category, String categoryName);

	/**
	 * Delete existing category, throws exception if given category name doesn't exist
	 *
	 * @param categoryName
	 * @return
	 */
	CategoryDTO deleteCategory (String categoryName);

}
