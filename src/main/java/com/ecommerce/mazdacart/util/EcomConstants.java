package com.ecommerce.mazdacart.util;

public class EcomConstants {

	public static final String NO_ADDRESSES_FOUND_FOR_THE_USER = "No addresses found for the user: ";

	private EcomConstants () {
		throw new IllegalStateException("Utility class");
	}

	public static final String HANDLED_BY_GENERIC_EXCEPTION_HANDLER = "Handled by Generic Exception Handler";
	public static final String PAGE_NUMBER = "0";
	public static final String PAGE_SIZE = "10";
	public static final String SORT_CATEGORIES_BY = "CategoryId";
	public static final String SORT_PRODUCTS_BY = "productId";
	public static final String SORT_DIR = "asc";

	public static final double PERCENTAGE_CAL_BY_100 = 0.01;
	public static final String PRODUCT = "Product";
	public static final String CATEGORY = "Category";
	public static final String CATEGORY_NAME = "CategoryName";
	public static final String PRODUCT_NAME = "ProductName";
}
