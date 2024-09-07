package com.ecommerce.MazdaCart.service;

import com.ecommerce.MazdaCart.exceptions.APIException;
import com.ecommerce.MazdaCart.exceptions.ResourceNotFoundException;
import com.ecommerce.MazdaCart.model.Category;
import com.ecommerce.MazdaCart.payload.CategoryDTO;
import com.ecommerce.MazdaCart.payload.CategoryResponse;
import com.ecommerce.MazdaCart.repository.CategoryRepository;
import com.ecommerce.MazdaCart.util.EcomConstants;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	ModelMapper modelMapper;

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

		Category newCategory = modelMapper.map(category, Category.class);
		existingCategory.setCategoryName(newCategory.getCategoryName());
		return createCategory(modelMapper.map(existingCategory, CategoryDTO.class));

	}

	@Override
	public CategoryDTO deleteCategory (String categoryName) {
		Category existingCategory = categoryRepository.findByCategoryNameIgnoreCase(categoryName);
		if (existingCategory == null)
			throw new ResourceNotFoundException(categoryName, "Category", "CategoryName");
		categoryRepository.delete(existingCategory);
		return modelMapper.map(existingCategory, CategoryDTO.class);

	}
}
