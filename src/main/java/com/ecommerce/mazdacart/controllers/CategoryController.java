package com.ecommerce.mazdacart.controllers;

import com.ecommerce.mazdacart.payload.CategoryDTO;
import com.ecommerce.mazdacart.payload.CategoryResponse;
import com.ecommerce.mazdacart.service.CategoryService;
import com.ecommerce.mazdacart.util.EcomConstants;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/")
@SuppressWarnings("unused")
public class CategoryController {

	@Autowired
	CategoryService categoryService;

	@PreAuthorize("hasAnyRole('ROLE_USER')")
	@GetMapping("/public/categories")
	public ResponseEntity<CategoryResponse> getAllCategories (
		@RequestParam(defaultValue = EcomConstants.PAGE_NUMBER) Integer pageNumber,
		@RequestParam(defaultValue = EcomConstants.PAGE_SIZE) Integer pageSize,
		@RequestParam(defaultValue = EcomConstants.SORT_CATEGORIES_BY) String sortBy,
		@RequestParam(defaultValue = EcomConstants.SORT_DIR) String sortOrder) {

		CategoryResponse categories = categoryService.getAllCategories(pageNumber, pageSize, sortBy, sortOrder);
		return new ResponseEntity<>(categories, HttpStatus.OK);

	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@PostMapping("/admin/categories")
	public ResponseEntity<CategoryDTO> createCategories (@Valid @RequestBody CategoryDTO categoryDTO) {
		CategoryDTO category = categoryService.createCategory(categoryDTO);
		return new ResponseEntity<>(category, HttpStatus.CREATED);

	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@DeleteMapping("/admin/categories/{categoryName}")
	public ResponseEntity<CategoryDTO> deleteCategory (@PathVariable String categoryName) {
		CategoryDTO category = categoryService.deleteCategory(categoryName);
		return new ResponseEntity<>(category, HttpStatus.OK);

	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@PutMapping("/admin/categories/{categoryName}")
	public ResponseEntity<CategoryDTO> updateCategory (@Valid @RequestBody CategoryDTO categoryDTO,
	                                                   @PathVariable String categoryName) {
		CategoryDTO updatedCategory = categoryService.updateCategory(categoryDTO, categoryName);
		return new ResponseEntity<>(updatedCategory, HttpStatus.OK);
	}
}
