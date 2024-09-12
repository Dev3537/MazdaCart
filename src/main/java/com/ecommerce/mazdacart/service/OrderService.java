package com.ecommerce.mazdacart.service;

import com.ecommerce.mazdacart.payload.OrderRequestDTO;
import com.ecommerce.mazdacart.payload.OrdersDTO;

public interface OrderService {
	OrdersDTO placeOrder (OrderRequestDTO ordersDTO);
}
