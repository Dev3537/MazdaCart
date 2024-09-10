package com.ecommerce.mazdacart.payload;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddressDTO {

	private Long addressId;

	@NotBlank(message = "Street field must not be blank")
	private String street;

	@NotBlank(message = "Building Name field must not be blank")
	private String buildingName;

	@NotBlank(message = "City field must not be blank")
	private String city;

	@NotBlank(message = "State field must not be blank")
	private String state;

	@NotBlank(message = "Country field must not be blank")
	private String country;

	@NotBlank
	@Size(min = 6, max = 6, message = "Zipcode can only be exactly 6 digits")
	private String zipcode;
}
