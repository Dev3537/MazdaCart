package com.ecommerce.mazdacart.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {

	private Long productId;

	@NotBlank(message = "Product Name cannot be blank")
	@Size(min = 3, message = "Product Name cannot be lesser than 3")
	private String productName;

	@NotBlank(message = "Description cannot be blank")
	private String description;

	private String image;

	@NotNull
	private Integer quantity;

	@NotNull(message = "Price must be present")
	private BigDecimal price;

	@NotNull(message = "Discount must be present")
	private BigDecimal discount;

	private BigDecimal specialPrice;

}
