package com.first.task_manager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.aayushatharva.brotli4j.decoder.BrotliInputStream;

public class BrotliUtils {

    public static byte[] BrotliDecode(byte[] compressedData) throws IOException {
        try (InputStream inputStream = new BrotliInputStream(new ByteArrayInputStream(compressedData))) {
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

			byte[] buffer = new byte[1024];
			int bytesRead;
			while ((bytesRead = inputStream.read(buffer)) != -1) {
			    outputStream.write(buffer, 0, bytesRead);
			}

			return outputStream.toByteArray();
		}
    }
}
