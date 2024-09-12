package com.ecommerce.mazdacart.service;

import com.ecommerce.mazdacart.exceptions.APIException;
import com.ecommerce.mazdacart.exceptions.ResourceNotFoundException;
import com.ecommerce.mazdacart.model.*;
import com.ecommerce.mazdacart.payload.*;
import com.ecommerce.mazdacart.repository.*;
import com.ecommerce.mazdacart.util.AuthUtilHelperClass;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	AuthUtilHelperClass authUtilHelperClass;

	@Autowired
	ModelMapper modelMapper;

	@Autowired
	CartRepository cartRepository;

	@Autowired
	AddressRepository addressRepository;

	@Autowired
	PaymentRepository paymentRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	CartRepository cartItemRepository;

	@Autowired
	CartService cartService;

	@Autowired
	OrdersRepository ordersRepository;

	@Autowired
	OrderItemRepository orderItemRepository;


	@Transactional
	@Override
	public OrdersDTO placeOrder (OrderRequestDTO orderRequestDTO) {
		String emailId = authUtilHelperClass.getCurrentUserEmail();

		Cart currentCart = cartRepository.findCartByEmailId(emailId)
			                   .orElseThrow(() -> new ResourceNotFoundException("No Carts found for the user"));

		Address address = addressRepository.findById(orderRequestDTO.getAddressId())
			                  .orElseThrow(() -> new APIException("No Address found for the given address ID"));

		Orders orders = new Orders();
		orders.setOrderDate(LocalDate.now());
		orders.setAddress(address);
		orders.setEmailId(emailId);
		orders.setOrderStatus("Order Accepted!");
		orders.setTotalAmount(currentCart.getTotalPrice());

		Payment payment = new Payment();
		payment.setPaymentMethod(orderRequestDTO.getPaymentMethod());
		payment.setPaymentProviderId(orderRequestDTO.getPaymentProviderId());
		payment.setPaymentProviderName(orderRequestDTO.getPaymentProviderName());
		payment.setPaymentProviderStatus(orderRequestDTO.getPaymentProviderStatus());
		payment.setOrder(orders);
		paymentRepository.save(payment);
		orders.setPayment(payment);

		List<CartItem> cartItemList = currentCart.getCartItemList();
		if (cartItemList.isEmpty())
			throw new APIException("Cart is empty");

		List<OrderItem> orderItemList = cartItemList.stream().map(cartItem -> {
			OrderItem orderItem = new OrderItem();
			orderItem.setOrderedProductPrice(cartItem.getProductPrice());
			orderItem.setDiscount(cartItem.getDiscount());
			orderItem.setQuantity(cartItem.getQuantity());
			orderItem.setProduct(cartItem.getProduct());
			orderItem.setOrder(orders);
			return orderItem;
		}).collect(Collectors.toCollection(ArrayList::new));
		orderItemRepository.saveAll(orderItemList);

		orders.setOrderItemList(orderItemList);

		Orders savedOrder = ordersRepository.save(orders);

		OrdersDTO responseDTO = modelMapper.map(savedOrder, OrdersDTO.class);
		responseDTO.setPaymentDTO(modelMapper.map(payment, PaymentDTO.class));
		List<OrderItemDTO> orderItemDTOS = orderItemList.stream().map(orderItem -> {
			OrderItemDTO itemDTO = modelMapper.map(orderItem, OrderItemDTO.class);
			itemDTO.setProductName(orderItem.getProduct().getProductName());
			itemDTO.setDescription(orderItem.getProduct().getDescription());
			itemDTO.setImage(orderItem.getProduct().getImage());
			return itemDTO;

		}).toList();
		responseDTO.setOrderItemList(orderItemDTOS);

		currentCart.getCartItemList().forEach(item -> {
			int quantity = item.getQuantity();
			Product product = item.getProduct();
			product.setQuantity(product.getQuantity() - quantity);
			productRepository.save(product);
		});
		cartRepository.delete(currentCart);

		return responseDTO;


	}
}
