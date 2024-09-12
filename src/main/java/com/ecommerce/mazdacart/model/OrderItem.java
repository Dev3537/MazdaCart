package com.ecommerce.mazdacart.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderItemId;

	@ManyToOne
	private Product product;

	@ManyToOne
	private Orders order;

	private Integer quantity;

	private BigDecimal discount;

	private BigDecimal orderedProductPrice;

}
