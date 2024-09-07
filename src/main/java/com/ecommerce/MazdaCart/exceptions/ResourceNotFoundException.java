package com.ecommerce.MazdaCart.exceptions;

public class ResourceNotFoundException extends RuntimeException {


	public ResourceNotFoundException () {
		super();
	}

	private String fieldName;
	private Long fieldId;
	private String resourceName;
	private String field;

	/**
	 * Value passed, Entity for which resource is missing, Field associated with passed value
	 *
	 * @param fieldName
	 * @param resourceName
	 * @param field
	 */
	public ResourceNotFoundException (String fieldName, String resourceName, String field) {
		super(String.format("%s not found with %s: %s", resourceName, field, fieldName));
		this.fieldName = fieldName;
		this.resourceName = resourceName;
		this.field = field;
	}

	/**
	 * Value passed, Entity for which resource is missing, Field associated with passed value
	 *
	 * @param fieldId
	 * @param resourceName
	 * @param field
	 */
	public ResourceNotFoundException (Long fieldId, String resourceName, String field) {
		super(String.format("%s not found with %s: %d", resourceName, field, fieldId));
		this.fieldId = fieldId;
		this.resourceName = resourceName;
		this.field = field;
	}

	/**
	 * Constructs a new runtime exception with the specified detail message.
	 * The cause is not initialized, and may subsequently be initialized by a
	 * call to {@link #initCause}.
	 *
	 * @param message the detail message. The detail message is saved for
	 * later retrieval by the {@link #getMessage()} method.
	 */
	public ResourceNotFoundException (String message) {
		super(message);
	}
}
