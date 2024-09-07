package com.ecommerce.MazdaCart.service;

import com.ecommerce.MazdaCart.exceptions.APIException;
import com.ecommerce.MazdaCart.exceptions.ResourceNotFoundException;
import com.ecommerce.MazdaCart.model.Category;
import com.ecommerce.MazdaCart.model.Product;
import com.ecommerce.MazdaCart.payload.ProductDTO;
import com.ecommerce.MazdaCart.payload.ProductResponse;
import com.ecommerce.MazdaCart.repository.CategoryRepository;
import com.ecommerce.MazdaCart.repository.ProductRepository;
import com.ecommerce.MazdaCart.util.EcomConstants;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

	public static final double PERCENTAGE_CAL_BY_100 = 0.01;
	@Autowired
	ModelMapper modelMapper;

	@Autowired
	CategoryRepository categoryRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	FileService fileService;

	@Override
	public ProductDTO addProduct (ProductDTO productDTO, String categoryName) {

		Category category = categoryRepository.findByCategoryNameIgnoreCase(categoryName);
		if (category == null)
			throw new ResourceNotFoundException(categoryName, "Category", "CategoryName");

		boolean productExists = category.getProductSet().stream().anyMatch(
			product -> product.getProductName().equalsIgnoreCase(productDTO.getProductName()));

		if (productExists)
			throw new APIException("Product Already Present, Please use update functionality");

		Product product = modelMapper.map(productDTO, Product.class);
		product.setCategory(category);
		BigDecimal specialPrice = product.getPrice().subtract(
			product.getDiscount().multiply(BigDecimal.valueOf(PERCENTAGE_CAL_BY_100)).multiply(product.getPrice()));
		product.setSpecialPrice(specialPrice);
		product.setImage("default.png");
		Product savedProduct = productRepository.save(product);
		return modelMapper.map(savedProduct, ProductDTO.class);

	}


	@Override
	public ProductResponse getAllProducts (Integer pageNumber, Integer pageSize, String sortBy, String sortOrder) {

		Sort sort = sortOrder.equalsIgnoreCase(EcomConstants.SORT_DIR) ? Sort.by(sortBy).ascending() :
			            Sort.by(sortBy).descending();

		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

		Page<Product> productList = productRepository.findAll(pageable);
		if (productList.isEmpty()) {
			throw new ResourceNotFoundException("No Products Found");
		}

		boolean vegetables = productList.stream().anyMatch(pr -> pr.getProductName().equals("Vegetables"));

		List<ProductDTO> productDTOList =
			productList.stream().map(pr -> modelMapper.map(pr, ProductDTO.class)).toList();


		ProductResponse response = new ProductResponse();
		response.setContent(productDTOList);
		response.setPageNumber(productList.getNumber());
		response.setPageSize(productList.getSize());
		response.setTotalElements(productList.getTotalElements());
		response.setTotalPages(productList.getTotalPages());
		response.setLastPage(productList.isLast());
		return response;

	}

	@Override
	public ProductResponse getProductsByCategory (String categoryName, Integer pageNumber, Integer pageSize,
	                                              String sortBy, String sortOrder) {

		Category category = categoryRepository.findByCategoryNameIgnoreCase(categoryName);
		if (category == null)
			throw new ResourceNotFoundException(categoryName, "Category", "CategoryName");

		Sort sort = sortOrder.equalsIgnoreCase(EcomConstants.SORT_DIR) ? Sort.by(sortBy).ascending() :
			            Sort.by(sortBy).descending();

		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

		Page<Product> productList = productRepository.findByCategory(category, pageable);
		if (productList.isEmpty()) {
			throw new ResourceNotFoundException(category.getCategoryName(), "Product", "Category");
		}

		List<ProductDTO> productDTOList =
			productList.stream().map(pr -> modelMapper.map(pr, ProductDTO.class)).toList();
		ProductResponse response = new ProductResponse();
		response.setContent(productDTOList);
		response.setPageNumber(productList.getNumber());
		response.setPageSize(productList.getSize());
		response.setTotalElements(productList.getTotalElements());
		response.setTotalPages(productList.getTotalPages());
		response.setLastPage(productList.isLast());
		return response;

	}

	@Override
	public ProductResponse getProductsByKeyword (String keyword, Integer pageNumber, Integer pageSize, String sortBy,
	                                             String sortOrder) {
		Sort sort = sortOrder.equalsIgnoreCase(EcomConstants.SORT_DIR) ? Sort.by(sortBy).ascending() :
			            Sort.by(sortBy).descending();

		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

		Page<Product> productList = productRepository.findByProductNameContainingIgnoreCase(keyword, pageable);
		if (productList.isEmpty()) {
			throw new ResourceNotFoundException(keyword, "Product", "keyword");
		}

		List<ProductDTO> productDTOList =
			productList.stream().map(pr -> modelMapper.map(pr, ProductDTO.class)).toList();

		ProductResponse response = new ProductResponse();
		response.setContent(productDTOList);
		response.setPageNumber(productList.getNumber());
		response.setPageSize(productList.getSize());
		response.setTotalElements(productList.getTotalElements());
		response.setTotalPages(productList.getTotalPages());
		response.setLastPage(productList.isLast());
		return response;
	}

	@Override
	public ProductDTO updateProduct (ProductDTO productDTO, String productName) {

		Product checkExisitingProduct = productRepository.findByProductNameIgnoreCase(productName);
		if (checkExisitingProduct == null) {
			throw new ResourceNotFoundException(productName, "Product", "ProductName");
		}

		Product product = modelMapper.map(productDTO, Product.class);
		checkExisitingProduct.setDescription(product.getDescription());
		checkExisitingProduct.setPrice(product.getPrice());
		BigDecimal specialPrice = product.getPrice().subtract(
			product.getDiscount().multiply(BigDecimal.valueOf(PERCENTAGE_CAL_BY_100)).multiply(product.getPrice()));
		checkExisitingProduct.setSpecialPrice(specialPrice);
		checkExisitingProduct.setDiscount(product.getDiscount());
		checkExisitingProduct.setQuantity(product.getQuantity());
		checkExisitingProduct.setImage("UpdatedImage.jpg");
		return modelMapper.map(productRepository.save(checkExisitingProduct), ProductDTO.class);

	}

	@Override
	public ProductDTO deleteProduct (String productName) {

		Product product = productRepository.findByProductName(productName);
		if (product == null)
			throw new ResourceNotFoundException(productName, "Product", "ProductName");

		/**
		 * In a bidirectional relationship it is tough to delete entities. One way is to create a @PreRemove method (it
		 * deletes the product from the category entity) which works on Product end but not when we try to delete
		 * Category, it throws a ConcurrentModificationException exception For that we need to first decouple product
		 * from category then delete product
		 */

		Category deletePrFromCat = product.getCategory();
		deletePrFromCat.getProductSet().removeIf(c -> c.getProductId().equals(product.getProductId()));
		categoryRepository.save(deletePrFromCat);


		productRepository.delete(product);
		return modelMapper.map(product, ProductDTO.class);
	}

	@Override
	public ProductDTO updateImageForProduct (String productName, MultipartFile image) {

		Product product = productRepository.findByProductNameIgnoreCase(productName);
		if (product == null)
			throw new ResourceNotFoundException(productName, "Product", "ProductName");

		try {
			String fileName = fileService.uploadImage(image);
			product.setImage(fileName);
			return modelMapper.map(productRepository.save(product), ProductDTO.class);

		} catch (IOException | NullPointerException e) {
			throw new APIException(e.getCause() + " is thrown: " + e.getMessage());
		}

	}


}
