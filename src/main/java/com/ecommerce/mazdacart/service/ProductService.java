package com.ecommerce.mazdacart.service;

import com.ecommerce.mazdacart.model.Product;
import com.ecommerce.mazdacart.payload.ProductDTO;
import com.ecommerce.mazdacart.payload.ProductResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {

	/**
	 * Adds a Product, Throws exception if product already present or No Category found for the product
	 *
	 * @param product
	 * @param categoryName
	 * @return
	 */
	ProductDTO addProduct (ProductDTO product, String categoryName);

	/**
	 * Gets all the products, throws exception if no Products found
	 *
	 * @param pageNumber
	 * @param pageSize
	 * @param sortBy
	 * @param sortOrder
	 * @return
	 */
	ProductResponse getAllProducts (Integer pageNumber, Integer pageSize, String sortBy, String sortOrder);

	/**
	 * Get all the Products by Category, throws exception if no Products or matching Category found
	 *
	 * @param categoryName
	 * @param pageNumber
	 * @param pageSize
	 * @param sortBy
	 * @param sortOrder
	 * @return
	 */
	ProductResponse getProductsByCategory (String categoryName, Integer pageNumber, Integer pageSize, String sortBy,
	                                       String sortOrder);

	/**
	 * Get all Products with Keyword, throws exception if no matching Products found
	 *
	 * @param keyword
	 * @param pageNumber
	 * @param pageSize
	 * @param sortBy
	 * @param sortOrder
	 * @return
	 */
	ProductResponse getProductsByKeyword (String keyword, Integer pageNumber, Integer pageSize, String sortBy,
	                                      String sortOrder);

	/**
	 * Update an existing product, throws exception if product doesn't exist
	 *
	 * @param productDTO
	 * @param productName
	 * @return
	 */
	ProductDTO updateProduct (ProductDTO productDTO, String productName);

	/**
	 * Delete products from DB, throws exception if given product not found
	 *
	 * @param productName
	 * @return
	 */
	ProductDTO deleteProduct (String productName);

	void deleteProductFromCart (Product product);

	/**
	 * Updates the image for the given product, throws IO Exception, throws exception if Product not found
	 *
	 * @param productName
	 * @param image
	 * @return
	 */
	ProductDTO updateImageForProduct (String productName, MultipartFile image);
}
