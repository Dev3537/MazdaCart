package com.ecommerce.mazdacart.service;

import com.ecommerce.mazdacart.exceptions.APIException;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileServiceImpl implements FileService {

	@Value("${property.image.uri}")
	private String imagePath;

	@Override
	public String uploadImage (@NotNull MultipartFile image) throws IOException, NullPointerException {

		String fileName = image.getOriginalFilename();

		if (fileName == null) {
			throw new APIException("fileName fetched from the multipart file is null");
		}

		String uniqueName = UUID.randomUUID().toString();

		String newFileName = uniqueName.concat(fileName.substring(fileName.indexOf('.')));

		String newFilePath = imagePath + File.separator + newFileName;

		//Check if folder exists
		File folder = new File(imagePath);
		if (!folder.exists()) {
			folder.mkdir();
		}

		//Copy the input file stream from the file and paste into the new path
		Files.copy(image.getInputStream(), Paths.get(newFilePath));

		folder.deleteOnExit();

		return newFileName;

	}
}
