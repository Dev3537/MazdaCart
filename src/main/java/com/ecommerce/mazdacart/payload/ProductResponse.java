package com.ecommerce.mazdacart.payload;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {

	List<ProductDTO> content;

	private Integer pageNumber;

	private Integer pageSize;

	private Long totalElements;

	private Integer totalPages;

	private boolean lastPage;
}
