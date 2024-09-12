package com.ecommerce.mazdacart.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentDTO {

	private Long paymentId;

	private String paymentMethod;

	private String paymentProviderId;

	private String paymentProviderStatus;

	private String paymentProviderName;
}
