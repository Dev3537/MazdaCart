package com.ecommerce.mazdacart.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Product {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
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


	@ManyToOne
	@JsonIgnore
	@JoinColumn(name = "category_id")
	private Category category;

	/**
	 * In a bidirectional relationship it is tough to delete entities. One is to create a @PreRemove method (it
	 * deletes the product from the category entity) which works on Product end but not when we try to delete
	 * Category, it throws a ConcurrentModificationException exception For that we need to first decouple product
	 * from category then delete product
	 *
	 * @PreRemove public void deleteProductFromCategories () {
	 * category.getProductSet().remove(this);
	 * }
	 **/

	@ManyToOne
	private Users users;

	@OneToMany(mappedBy = "product", cascade = {CascadeType.MERGE, CascadeType.PERSIST, CascadeType.REMOVE})
	private List<CartItem> cartItemList = new ArrayList<>();

}
