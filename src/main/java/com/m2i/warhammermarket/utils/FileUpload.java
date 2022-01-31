package com.m2i.warhammermarket.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

public class FileUpload {

	private final static String uploadDir = "./src/main/resources/static/upload/profilePictures/";

	/*
	 * filename HashMD5
	 * 
	 * @return MD5Hash of inputStreamFile IOException - if an I/O error occurs when
	 * reading or writing
	 * 
	 * @throws NullPointerException - if inputStream is null
	 **/
	public static String getMD5Name(InputStream inputStream) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();

		inputStream.transferTo(baos);

		return DigestUtils.md5DigestAsHex(new ByteArrayInputStream(baos.toByteArray()));

	}

	/*
	 * Save the bitmap in upload directory
	 * 
	 * 
	 **/
	public static boolean saveFile(String fileName, MultipartFile multipartFile) {
		Path uploadPath = Paths.get(uploadDir);
		Boolean success = false;
		try {
			if (!Files.exists(uploadPath)) {
				Files.createDirectories(uploadPath);
			}

			InputStream inputStream = multipartFile.getInputStream();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			inputStream.transferTo(baos);
			InputStream bais = new ByteArrayInputStream(baos.toByteArray()); 
			Path filePath = uploadPath.resolve(fileName);
			Files.copy(bais, filePath, StandardCopyOption.REPLACE_EXISTING);

			success = true;
		} catch (Exception e) {
			success = false;
			e.printStackTrace();
		}

		return success;
	}

	public static void removefile(String fileName) {
		Path uploadPath = Paths.get(uploadDir);
		try {
			Files.deleteIfExists(uploadPath.resolve(fileName));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
