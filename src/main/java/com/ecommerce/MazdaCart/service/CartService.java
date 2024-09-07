package com.ecommerce.MazdaCart.service;

import com.ecommerce.MazdaCart.payload.CartDTO;

import java.util.List;

public interface CartService {

	CartDTO addProductToCart (String productName, Integer quantity);

	List<CartDTO> getAllCarts ();

}
