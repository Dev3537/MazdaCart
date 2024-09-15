package com.ecommerce.mazdacart.service;

import com.ecommerce.mazdacart.exceptions.APIException;
import com.ecommerce.mazdacart.exceptions.ResourceNotFoundException;
import com.ecommerce.mazdacart.model.Cart;
import com.ecommerce.mazdacart.model.CartItem;
import com.ecommerce.mazdacart.model.Category;
import com.ecommerce.mazdacart.model.Product;
import com.ecommerce.mazdacart.payload.ProductDTO;
import com.ecommerce.mazdacart.payload.ProductResponse;
import com.ecommerce.mazdacart.repository.CartItemRepository;
import com.ecommerce.mazdacart.repository.CartRepository;
import com.ecommerce.mazdacart.repository.CategoryRepository;
import com.ecommerce.mazdacart.repository.ProductRepository;
import com.ecommerce.mazdacart.util.EcomConstants;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class ProductServiceImpl implements ProductService {

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private CategoryRepository categoryRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private FileService fileService;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private CartItemRepository cartItemRepository;

	@Override
	public ProductDTO addProduct (ProductDTO productDTO, String categoryName) {

		Category category = categoryRepository.findByCategoryNameIgnoreCase(categoryName);
		if (category == null)
			throw new ResourceNotFoundException(categoryName, EcomConstants.CATEGORY, EcomConstants.CATEGORY_NAME);

		Product productExists = productRepository.findByProductNameIgnoreCase(productDTO.getProductName());

		if (productExists != null)
			throw new APIException("Product Already Present, Please use update functionality");

		Product product = modelMapper.map(productDTO, Product.class);
		product.setCategory(category);
		BigDecimal specialPrice = product.getPrice().subtract(
			product.getDiscount().multiply(BigDecimal.valueOf(EcomConstants.PERCENTAGE_CAL_BY_100))
				.multiply(product.getPrice()));
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
			throw new ResourceNotFoundException(categoryName, EcomConstants.CATEGORY, EcomConstants.CATEGORY_NAME);

		Sort sort = sortOrder.equalsIgnoreCase(EcomConstants.SORT_DIR) ? Sort.by(sortBy).ascending() :
			            Sort.by(sortBy).descending();

		Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);

		Page<Product> productList = productRepository.findByCategory(category, pageable);
		if (productList.isEmpty()) {
			throw new ResourceNotFoundException(category.getCategoryName(), EcomConstants.PRODUCT,
				EcomConstants.CATEGORY);
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
			throw new ResourceNotFoundException(keyword, EcomConstants.PRODUCT, "keyword");
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

	@Transactional
	@Override
	public ProductDTO updateProduct (ProductDTO productDTO) {

		Product checkExisitingProduct = productRepository.findByProductNameIgnoreCase(productDTO.getProductName());
		if (checkExisitingProduct == null) {
			throw new ResourceNotFoundException(productDTO.getProductName(), EcomConstants.PRODUCT,
				EcomConstants.PRODUCT_NAME);
		}

		Product product = modelMapper.map(productDTO, Product.class);
		checkExisitingProduct.setDescription(product.getDescription());
		checkExisitingProduct.setPrice(product.getPrice());
		checkExisitingProduct.setDiscount(product.getDiscount());
		BigDecimal specialPrice = product.getPrice().subtract(
			product.getDiscount().multiply(BigDecimal.valueOf(EcomConstants.PERCENTAGE_CAL_BY_100))
				.multiply(product.getPrice()));

		if (!Objects.equals(checkExisitingProduct.getSpecialPrice(), specialPrice)) {
			updateProductPricingInCart(checkExisitingProduct, specialPrice);
		}
		checkExisitingProduct.setSpecialPrice(specialPrice);
		checkExisitingProduct.setQuantity(product.getQuantity());
		checkExisitingProduct.setImage("UpdatedImage.jpg");

		return modelMapper.map(productRepository.save(checkExisitingProduct), ProductDTO.class);

	}

	/**
	 * Updates the product details in the cart including the price and discount details
	 *
	 * @param product
	 * @param specialPrice
	 */
	private void updateProductPricingInCart (Product product, BigDecimal specialPrice) {
		List<Cart> allCarts = cartRepository.findCartsByProductId(product.getProductId());

		for (Cart cart : allCarts) {
			List<CartItem> cartItemList = cart.getCartItemList();
			CartItem cartItem =
				cartItemList.stream().filter(c -> c.getProduct().getProductId().equals(product.getProductId()))
					.findFirst().orElseThrow(() -> new ResourceNotFoundException(
						"Cart Item not found with the given product, Check " + "Query"));
			BigDecimal currentDiff = specialPrice.subtract(cartItem.getProductPrice());
			cartItem.setProductPrice(specialPrice);
			cartItem.setDiscount(product.getDiscount());
			cartItemRepository.save(cartItem);
			cart.setCartItemList(cartItemList);
			cart.setTotalPrice(
				cart.getTotalPrice().add(currentDiff.multiply(BigDecimal.valueOf(cartItem.getQuantity()))));
			cartRepository.save(cart);
		}
	}

	@Transactional
	@Override
	public ProductDTO deleteProduct (String productName) {

		Product product = productRepository.findByProductName(productName);
		if (product == null)
			throw new ResourceNotFoundException(productName, EcomConstants.PRODUCT, EcomConstants.PRODUCT_NAME);

		deleteProductFromCart(product);

		/*
		  In a bidirectional relationship it is tough to delete entities. One way is to create a @PreRemove method (it
		  deletes the product from the category entity) which works on Product end but not when we try to delete
		  Category, it throws a ConcurrentModificationException exception For that we need to first decouple product
		  from category then delete product
		 */

		Category deletePrFromCat = product.getCategory();
		deletePrFromCat.getProductSet().removeIf(c -> c.getProductId().equals(product.getProductId()));
		categoryRepository.save(deletePrFromCat);

		productRepository.delete(product);
		return modelMapper.map(product, ProductDTO.class);
	}

	/**
	 * Delete the product from whichever cart it has been added to and updates the cart's total price.
	 *
	 * @param product
	 */
	@Override
	public void deleteProductFromCart (Product product) {
		List<Cart> allCarts = cartRepository.findCartsByProductId(product.getProductId());

		for (Cart cart : allCarts) {
			List<CartItem> cartItemList = cart.getCartItemList();
			CartItem cartItem =
				cartItemList.stream().filter(c -> c.getProduct().getProductId().equals(product.getProductId()))
					.findFirst().orElseThrow(() -> new ResourceNotFoundException(
						"Cart Item not found with the given product, Check " + "Query"));

			cartItemList.remove(cartItem);
			cartItemRepository.delete(cartItem);
			cart.setCartItemList(cartItemList);
			cart.setTotalPrice(cart.getTotalPrice().subtract(
				product.getSpecialPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()))));
			cartRepository.save(cart);
		}


	}

	@Override
	public ProductDTO updateImageForProduct (String productName, MultipartFile image) {

		Product product = productRepository.findByProductNameIgnoreCase(productName);
		if (product == null)
			throw new ResourceNotFoundException(productName, EcomConstants.PRODUCT, EcomConstants.PRODUCT_NAME);

		try {
			String fileName = fileService.uploadImage(image);
			product.setImage(fileName);
			return modelMapper.map(productRepository.save(product), ProductDTO.class);

		} catch (IOException | NullPointerException e) {
			throw new APIException(e.getCause() + " is thrown: " + e.getMessage());
		}

	}


}
