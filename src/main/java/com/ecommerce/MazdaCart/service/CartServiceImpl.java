package com.ecommerce.MazdaCart.service;

import com.ecommerce.MazdaCart.exceptions.APIException;
import com.ecommerce.MazdaCart.exceptions.ResourceNotFoundException;
import com.ecommerce.MazdaCart.model.Cart;
import com.ecommerce.MazdaCart.model.CartItem;
import com.ecommerce.MazdaCart.model.Product;
import com.ecommerce.MazdaCart.model.Users;
import com.ecommerce.MazdaCart.payload.CartDTO;
import com.ecommerce.MazdaCart.payload.ProductDTO;
import com.ecommerce.MazdaCart.repository.CartItemRepository;
import com.ecommerce.MazdaCart.repository.CartRepository;
import com.ecommerce.MazdaCart.repository.ProductRepository;
import com.ecommerce.MazdaCart.repository.UserRepository;
import com.ecommerce.MazdaCart.util.AuthUtilHelperClass;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

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

		List<ProductDTO> productDTOs = cartItems.stream().map(item -> {
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

		List<CartDTO> responseList = allCarts.stream().map(item -> {
			CartDTO cartDTO = modelMapper.map(item, CartDTO.class);
			List<ProductDTO> products =
				item.getCartItemList().stream().map(p -> modelMapper.map(p.getProduct(), ProductDTO.class)).toList();
			cartDTO.setProductDTOS(products);
			return cartDTO;
		}).toList();

		return responseList;
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
