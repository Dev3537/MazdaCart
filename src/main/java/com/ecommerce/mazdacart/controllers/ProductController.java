package com.ecommerce.mazdacart.controllers;

import com.ecommerce.mazdacart.payload.ProductDTO;
import com.ecommerce.mazdacart.payload.ProductResponse;
import com.ecommerce.mazdacart.service.ProductService;
import com.ecommerce.mazdacart.util.EcomConstants;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api")
@SuppressWarnings("unused")
public class ProductController {


	@Autowired
	ProductService productService;

	@PreAuthorize("hasAnyRole('ROLE_SELLER','ROLE_ADMIN')")
	@PostMapping("/admin/categories/{categoryName}/product")
	public ResponseEntity<ProductDTO> createProduct (@Valid @RequestBody ProductDTO productDTO,
	                                                 @PathVariable String categoryName) {
		ProductDTO createdProductDTO = productService.addProduct(productDTO, categoryName);
		return new ResponseEntity<>(createdProductDTO, HttpStatus.CREATED);

	}

	@PreAuthorize("hasAnyRole('ROLE_USER')")
	@GetMapping("/public/products")
	public ResponseEntity<ProductResponse> getAllProducts (
		@RequestParam(defaultValue = EcomConstants.PAGE_NUMBER) Integer pageNumber,
		@RequestParam(defaultValue = EcomConstants.PAGE_SIZE) Integer pageSize,
		@RequestParam(defaultValue = EcomConstants.SORT_PRODUCTS_BY) String sortBy,
		@RequestParam(defaultValue = EcomConstants.SORT_DIR) String sortOrder) {

		ProductResponse productResponse = productService.getAllProducts(pageNumber, pageSize, sortBy, sortOrder);
		return new ResponseEntity<>(productResponse, HttpStatus.OK);

	}

	@PreAuthorize("hasAnyRole('ROLE_USER')")
	@GetMapping("/public/categories/{categoryName}/products")
	public ResponseEntity<ProductResponse> getProductsByCategory (@PathVariable String categoryName,
	                                                              @RequestParam(defaultValue =
		                                                                            EcomConstants.PAGE_NUMBER) Integer pageNumber,
	                                                              @RequestParam(defaultValue = EcomConstants.PAGE_SIZE) Integer pageSize,
	                                                              @RequestParam(defaultValue =
		                                                                            EcomConstants.SORT_PRODUCTS_BY) String sortBy,
	                                                              @RequestParam(defaultValue = EcomConstants.SORT_DIR) String sortOrder) {
		ProductResponse productResponse =
			productService.getProductsByCategory(categoryName, pageNumber, pageSize, sortBy, sortOrder);
		return new ResponseEntity<>(productResponse, HttpStatus.OK);
	}

	@PreAuthorize("hasAnyRole('ROLE_USER')")
	@GetMapping("/public/products/keyword/{keyword}")
	public ResponseEntity<ProductResponse> getProductsByKeyword (@PathVariable String keyword,
	                                                             @RequestParam(defaultValue =
		                                                                           EcomConstants.PAGE_NUMBER) Integer pageNumber,
	                                                             @RequestParam(defaultValue = EcomConstants.PAGE_SIZE) Integer pageSize,
	                                                             @RequestParam(defaultValue =
		                                                                           EcomConstants.SORT_PRODUCTS_BY) String sortBy,
	                                                             @RequestParam(defaultValue = EcomConstants.SORT_DIR) String sortOrder) {
		ProductResponse productResponse =
			productService.getProductsByKeyword(keyword, pageNumber, pageSize, sortBy, sortOrder);
		return new ResponseEntity<>(productResponse, HttpStatus.FOUND);
	}

	@PreAuthorize("hasAnyRole('ROLE_SELLER','ROLE_ADMIN')")
	@PutMapping("/admin/products/{productName}")
	public ResponseEntity<ProductDTO> updateProduct (@Valid @RequestBody ProductDTO productDTO,
	                                                 @PathVariable String productName) {
		ProductDTO responseDTO = productService.updateProduct(productDTO, productName);
		return new ResponseEntity<>(responseDTO, HttpStatus.OK);
	}


	@PreAuthorize("hasAnyRole('ROLE_ADMIN')")
	@DeleteMapping("/admin/products/{productName}")
	public ResponseEntity<ProductDTO> deleteProduct (@PathVariable String productName) {

		ProductDTO productDTO = productService.deleteProduct(productName);
		return new ResponseEntity<>(productDTO, HttpStatus.OK);

	}

	@PreAuthorize("hasAnyRole('ROLE_SELLER','ROLE_ADMIN')")
	@PutMapping("/admin/products/{productName}/image")
	public ResponseEntity<ProductDTO> updateImage (@PathVariable String productName,
	                                               @RequestBody MultipartFile image) {
		ProductDTO productDTO = productService.updateImageForProduct(productName, image);
		return new ResponseEntity<>(productDTO, HttpStatus.OK);
	}
}
