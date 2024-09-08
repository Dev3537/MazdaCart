package com.ecommerce.mazdacart.service;

import com.ecommerce.mazdacart.exceptions.APIException;
import com.ecommerce.mazdacart.exceptions.ResourceNotFoundException;
import com.ecommerce.mazdacart.model.Cart;
import com.ecommerce.mazdacart.model.CartItem;
import com.ecommerce.mazdacart.model.Product;
import com.ecommerce.mazdacart.model.Users;
import com.ecommerce.mazdacart.payload.CartDTO;
import com.ecommerce.mazdacart.payload.ProductDTO;
import com.ecommerce.mazdacart.repository.CartItemRepository;
import com.ecommerce.mazdacart.repository.CartRepository;
import com.ecommerce.mazdacart.repository.ProductRepository;
import com.ecommerce.mazdacart.repository.UserRepository;
import com.ecommerce.mazdacart.util.AuthUtilHelperClass;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Service
public class CartServiceImpl implements CartService {

	@Autowired
	AuthUtilHelperClass authUtilHelperClass;


	@Autowired
	UserRepository userRepository;

	@Autowired
	CartRepository cartRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	CartItemRepository cartItemRepository;

	@Autowired
	ModelMapper modelMapper;

	@Override
	public CartDTO addProductToCart (String productName, Integer quantity) {

		Cart cart = getCart();

		Product product = productRepository.findByProductNameIgnoreCase(productName);
		if (product == null) {
			throw new ResourceNotFoundException("Product does not exist");
		}

		if (product.getQuantity() == 0 || product.getQuantity() < quantity) {
			throw new APIException("Quantity specified is not left in stock");
		}

		if (cartItemRepository.findByProductIdAndCartId(product.getProductId(), cart.getCartId()).isPresent()) {
			throw new APIException("Product Already exists in cart");
		}

		CartItem cartItem = new CartItem();
		cartItem.setCart(cart);
		cartItem.setProduct(product);
		cartItem.setProductPrice(product.getSpecialPrice());
		cartItem.setQuantity(quantity);
		cartItem.setDiscount(product.getDiscount());
		cartItemRepository.save(cartItem);

		cart.setTotalPrice(
			cart.getTotalPrice().add(cartItem.getProductPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()))));
		cart.getCartItemList().add(cartItem);
		cartRepository.save(cart);

		List<CartItem> cartItems = cart.getCartItemList();

		List<ProductDTO> productDTOs =
			cartItems.stream().filter(p -> p.getProduct().getProductId().equals(product.getProductId())).map(item -> {
				ProductDTO productDTO = modelMapper.map(item.getProduct(), ProductDTO.class);
				productDTO.setQuantity(quantity);
				return productDTO;
			}).toList();

		CartDTO response = modelMapper.map(cart, CartDTO.class);

		response.setProductDTOS(productDTOs);
		return response;

	}

	@Override
	public List<CartDTO> getAllCarts () {
		List<Cart> allCarts = cartRepository.findAll();

		if (allCarts.isEmpty())
			throw new APIException("No carts found");

		List<CartDTO> responseList = allCarts.stream().map(cart -> {
			CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
			List<ProductDTO> products = cart.getCartItemList().stream().map(cartProduct -> {
				ProductDTO productDTO = modelMapper.map(cartProduct.getProduct(), ProductDTO.class);
				productDTO.setQuantity(cartProduct.getQuantity());
				return productDTO;
			}).toList();
			cartDTO.setProductDTOS(products);
			return cartDTO;
		}).toList();

		return responseList;
	}

	@Override
	public CartDTO getCartsByUser () {
		String emailId = authUtilHelperClass.getCurrentUserEmail();
		Users user = userRepository.findByEmailId(emailId)
			             .orElseThrow(() -> new ResourceNotFoundException("User not found with email Id:" + emailId));
		if (user.getCart() == null) {
			throw new ResourceNotFoundException("No Carts present for the user");
		}

		List<ProductDTO> productDTOS = user.getCart().getCartItemList().stream().map(item -> {
			ProductDTO productDTO = modelMapper.map(item.getProduct(), ProductDTO.class);
			productDTO.setQuantity(item.getQuantity());
			return productDTO;
		}).toList();
		CartDTO cartDTO = modelMapper.map(user.getCart(), CartDTO.class);
		cartDTO.setProductDTOS(productDTOS);

		return cartDTO;


	}

	@Transactional
	@Override
	public CartDTO updateProductToCart (String productName, Integer newQuantity) {
		Users user = authUtilHelperClass.getCurrentUser();
		Cart cart = cartRepository.findByUsers(user);
		if (cart == null) {
			throw new APIException("No Cart found for the User: " + user.getUserName());
		}

		List<CartItem> cartItemList = cart.getCartItemList();
		Product product =
			cartItemList.stream().filter(p -> p.getProduct().getProductName().equalsIgnoreCase(productName)).findFirst()
				.map(CartItem::getProduct).orElse(null);

		if (product == null) {
			throw new ResourceNotFoundException("No product found in the cart with the given name");
		}
		CartItem cartItem = cartItemRepository.findByProduct(product);

		if (newQuantity == -1) {
			cart.setTotalPrice(cart.getTotalPrice().subtract(cartItem.getProductPrice()));
			cartItem.setQuantity(cartItem.getQuantity() + newQuantity);
			if (cartItem.getQuantity() == 0) {
				cartItemList.remove(cartItem);
				cartItemRepository.delete(cartItem);
			}
		} else {
			if (Objects.equals(cartItem.getQuantity(), product.getQuantity())) {
				throw new APIException("Additional Quantity is not present in stock");
			}
			cartItem.setQuantity(cartItem.getQuantity() + newQuantity);
			cart.setTotalPrice(cart.getTotalPrice().add(cartItem.getProductPrice()));
			cartItemRepository.save(cartItem);
		}

		cart.setCartItemList(cartItemList);
		cartRepository.save(cart);
		CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
		List<ProductDTO> productDTOS = cartItemList.stream().map(item -> {
			ProductDTO productDTO = modelMapper.map(item.getProduct(), ProductDTO.class);
			productDTO.setQuantity(cartItem.getQuantity());
			return productDTO;
		}).toList();
		cartDTO.setProductDTOS(productDTOS);

		return cartDTO;
	}

	@Transactional
	@Override
	public CartDTO deleteProductFromCart (String productName) {
		Users user = authUtilHelperClass.getCurrentUser();
		Cart cart = cartRepository.findByUsers(user);
		if (cart == null) {
			throw new APIException("No Cart found for the User: " + user.getUserName());
		}

		List<CartItem> cartItemList = cart.getCartItemList();
		Product product =
			cartItemList.stream().filter(p -> p.getProduct().getProductName().equalsIgnoreCase(productName)).findFirst()
				.map(CartItem::getProduct).orElse(null);

		if (product == null) {
			throw new ResourceNotFoundException("No product found in the cart with the given name");
		}
		CartItem cartItem =
			cartItemList.stream().filter(c -> c.getProduct().getProductId().equals(product.getProductId())).findFirst()
				.orElseThrow(() -> new ResourceNotFoundException("Cart Item not found with the given product Name"));

		cartItemList.remove(cartItem);
		cartItemRepository.delete(cartItem);
		cart.setCartItemList(cartItemList);
		cart.setTotalPrice(cart.getTotalPrice().subtract(
			product.getSpecialPrice().multiply(BigDecimal.valueOf(cartItem.getQuantity()))));
		cartRepository.save(cart);
		CartDTO cartDTO = modelMapper.map(cart, CartDTO.class);
		List<ProductDTO> productDTOS = cartItemList.stream().map(item -> {
			ProductDTO productDTO = modelMapper.map(item.getProduct(), ProductDTO.class);
			productDTO.setQuantity(cartItem.getQuantity());
			return productDTO;
		}).toList();
		cartDTO.setProductDTOS(productDTOS);

		return cartDTO;

	}

	private Cart getCart () {
		Users user = authUtilHelperClass.getCurrentUser();
		Cart cart = cartRepository.findByUsers(user);
		if (cart == null) {
			cart = new Cart();
			cart.setUsers(user);
			cart.setTotalPrice(BigDecimal.ZERO);
			cartRepository.save(cart);
		}
		return cart;
	}


}
