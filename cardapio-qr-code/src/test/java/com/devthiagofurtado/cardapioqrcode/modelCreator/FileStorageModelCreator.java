package com.devthiagofurtado.cardapioqrcode.modelCreator;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class FileStorageModelCreator {

    public static MultipartFile arquivo(String origninalFileName){
        return new MultipartFile() {
            @Override
            public String getName() {
                return "teste";
            }

            @Override
            public String getOriginalFilename() {
                return origninalFileName;
            }

            @Override
            public String getContentType() {
                return "application/pdf";
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @Override
            public long getSize() {
                return 1;
            }

            @Override
            public byte[] getBytes() throws IOException {
                return new byte[1234];
            }

            @Override
            public InputStream getInputStream() throws IOException {
                return null;
            }

            @Override
            public void transferTo(File dest) throws IOException, IllegalStateException {

            }
        };
    }
}
