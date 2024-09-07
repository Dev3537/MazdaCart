package com.ecommerce.MazdaCart.service;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

	/**
	 * Uploads Image into the server
	 *
	 * @param image
	 * @return
	 * @throws IOException
	 * @throws NullPointerException
	 */
	String uploadImage (@NotNull MultipartFile image) throws IOException, NullPointerException;
}
