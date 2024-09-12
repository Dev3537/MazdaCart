package com.ecommerce.mazdacart.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(uniqueConstraints = @UniqueConstraint(columnNames = "paymentProviderId"))
public class Payment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long paymentId;

	@OneToOne(mappedBy = "payment", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
	private Orders order;

	@NotBlank
	@Size(min = 3, message = "Payment Method definition cannot be lesser than 4 characters")
	private String paymentMethod;

	@NotNull
	private String paymentProviderId;

	private String paymentProviderStatus;

	private String paymentProviderName;

}
