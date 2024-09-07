package com.ecommerce.MazdaCart.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartDTO {

	private String cartId;

	private BigDecimal totalPrice;

	private List<ProductDTO> productDTOS = new ArrayList<>();


}
