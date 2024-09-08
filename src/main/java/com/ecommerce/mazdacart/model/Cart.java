package com.ecommerce.mazdacart.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Cart {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long cartId;

	@OneToOne
	@JoinColumn(name = "user_id")
	private Users users;

	@OneToMany(mappedBy = "cart", cascade = {CascadeType.PERSIST, CascadeType.MERGE,
		CascadeType.REMOVE}, orphanRemoval = true)
	private List<CartItem> cartItemList = new ArrayList<>();

	private BigDecimal totalPrice = BigDecimal.valueOf(0);
}
