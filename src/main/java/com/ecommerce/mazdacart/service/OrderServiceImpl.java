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
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements OrderService {

	@Autowired
	private AuthUtilHelperClass authUtilHelperClass;

	@Autowired
	private ModelMapper modelMapper;

	@Autowired
	private CartRepository cartRepository;

	@Autowired
	private AddressRepository addressRepository;

	@Autowired
	private PaymentRepository paymentRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private OrdersRepository ordersRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private RestClient restClient;


	@Transactional
	@Override
	public OrdersDTO placeOrder (String paymentMethod, Long addressId) {

		String url = String.format("http://localhost:8081/api/payment/gateway-proceed/%s", paymentMethod);

		OrderRequestDTO orderRequestDTO = restClient.get().uri(url).retrieve().body(OrderRequestDTO.class);

		if (orderRequestDTO == null) {
			throw new APIException("Payment Gateway failed");
		}

		String emailId = authUtilHelperClass.getCurrentUserEmail();

		Cart currentCart = cartRepository.findCartByEmailId(emailId)
			                   .orElseThrow(() -> new ResourceNotFoundException("No Carts found for the user"));

		Address address = addressRepository.findById(addressId)
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
