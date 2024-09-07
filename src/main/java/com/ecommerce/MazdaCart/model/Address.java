package com.ecommerce.MazdaCart.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
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

	@ManyToMany(mappedBy = "addresses")
	private List<Users> users = new ArrayList<>();


}



