package com.ecommerce.mazdacart.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Category {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long categoryId;

	@NotBlank(message = "Category Name must not be blank")
	@Size(min = 3, message = "Category Name could not be lesser than 3 characters")
	private String categoryName;

	@OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<Product> productSet;

}