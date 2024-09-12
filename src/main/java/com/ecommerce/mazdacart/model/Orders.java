package com.ecommerce.mazdacart.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Orders {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long orderId;

	@Email
	@Column(nullable = false)
	private String emailId;


	@OneToMany(mappedBy = "order", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private List<OrderItem> orderItemList;

	private LocalDate orderDate;

	@OneToOne
	@JoinColumn(name = "payment_id")
	private Payment payment;

	private BigDecimal totalAmount;

	private String orderStatus;

	@ManyToOne
	@JoinColumn(name = "address")
	private Address address;

}
