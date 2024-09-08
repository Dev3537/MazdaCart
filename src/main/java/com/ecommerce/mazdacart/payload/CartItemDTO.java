package com.ecommerce.mazdacart.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemDTO {

	private Long cartItemId;

	private CartDTO cartDTO;

	private ProductDTO productDTO;

	private Integer quantity;

	private BigDecimal discount;

	private BigDecimal productPrice;
}
