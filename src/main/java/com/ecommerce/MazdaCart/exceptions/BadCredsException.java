package com.ecommerce.MazdaCart.exceptions;

import org.springframework.security.core.AuthenticationException;

public class BadCredsException extends AuthenticationException {
	/**
	 * Constructs an {@code AuthenticationException} with the specified message and root
	 * cause.
	 *
	 * @param msg the detail message
	 * @param cause the root cause
	 */
	public BadCredsException (String msg, Throwable cause) {
		super(msg, cause);
	}

	/**
	 * Constructs an {@code AuthenticationException} with the specified message and no
	 * root cause.
	 *
	 * @param msg the detail message
	 */
	public BadCredsException (String msg) {
		super(msg);
	}
}
