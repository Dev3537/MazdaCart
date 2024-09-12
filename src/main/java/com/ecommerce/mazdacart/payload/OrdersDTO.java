package com.ecommerce.mazdacart.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrdersDTO {
	private Long orderId;

	private String emailId;

	private List<OrderItemDTO> orderItemList;

	private LocalDate orderDate;

	private PaymentDTO paymentDTO;

	private BigDecimal totalAmount;

	private String orderStatus;

	private Long addressId;
}
