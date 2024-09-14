package com.ecommerce.mazdacart.service;

import com.ecommerce.mazdacart.exceptions.APIException;
import com.ecommerce.mazdacart.exceptions.ResourceNotFoundException;
import com.ecommerce.mazdacart.model.Category;
import com.ecommerce.mazdacart.model.Product;
import com.ecommerce.mazdacart.payload.CategoryDTO;
import com.ecommerce.mazdacart.payload.CategoryResponse;
import com.ecommerce.mazdacart.repository.CategoryRepository;
import com.ecommerce.mazdacart.util.EcomConstants;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ProductService productService;

	@Autowired
	private ModelMapper modelMapper;

	@Override
	public CategoryResponse getAllCategories (Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {
		Sort sort = sortOrder.equalsIgnoreCase(EcomConstants.SORT_DIR) ? Sort.by(sortBy).ascending() :
			            Sort.by(sortBy).descending();

		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

		Page<Category> fetchAll = categoryRepository.findAll(pageable);

		if (fetchAll.isEmpty()) {
			throw new ResourceNotFoundException("No Categories found");
		}
		List<CategoryDTO> mappedDTO = fetchAll.stream().map(c -> modelMapper.map(c, CategoryDTO.class)).toList();
		CategoryResponse response = new CategoryResponse();
		response.setContent(mappedDTO);
		response.setPageNumber(fetchAll.getNumber());
		response.setPageSize(fetchAll.getSize());
		response.setTotalElements(fetchAll.getTotalElements());
		response.setTotalPages(fetchAll.getTotalPages());
		response.setLastPage(fetchAll.isLast());
		return response;

	}

	@Override
	public CategoryDTO createCategory (CategoryDTO category) {
		Category existingCategory = categoryRepository.findByCategoryName(category.getCategoryName());
		if (existingCategory != null) {
			throw new APIException("A Category exists with the given Name");
		}
		Category savedCategory = categoryRepository.save(modelMapper.map(category, Category.class));
		return modelMapper.map(savedCategory, CategoryDTO.class);

	}

	@Transactional
	@Override
	public CategoryDTO updateCategory (CategoryDTO category, String categoryName) {

		Category toBeUpdatedCategory = categoryRepository.findByCategoryNameIgnoreCase(category.getCategoryName());
		if (toBeUpdatedCategory != null) {
			throw new APIException("A Category exists with the given Name");
		}

		Category existingCategory = categoryRepository.findByCategoryNameIgnoreCase(categoryName);
		if (existingCategory == null) {
			throw new ResourceNotFoundException(categoryName, "Category", "CategoryName");
		}
		existingCategory.setCategoryName(category.getCategoryName());
		categoryRepository.updateCategoryName(category.getCategoryName(),existingCategory.getCategoryId());
		return modelMapper.map(existingCategory, CategoryDTO.class);

	}

	@Transactional
	@Override
	public CategoryDTO deleteCategory (String categoryName) {
		Category existingCategory = categoryRepository.findByCategoryNameIgnoreCase(categoryName);
		if (existingCategory == null)
			throw new ResourceNotFoundException(categoryName, "Category", "CategoryName");
		List<Product> productSet = existingCategory.getProductSet();
		for(Product product:productSet){
			productService.deleteProductFromCart(product);
		}
		categoryRepository.delete(existingCategory);
		return modelMapper.map(existingCategory, CategoryDTO.class);

	}
}
