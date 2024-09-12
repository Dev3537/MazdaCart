package com.ecommerce.mazdacart.payload;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDTO {
	private Long orderItemId;

	private String productName;

	private String description;

	private String image;

	private Integer quantity;

	private BigDecimal discount;

	private BigDecimal orderedProductPrice;
}
