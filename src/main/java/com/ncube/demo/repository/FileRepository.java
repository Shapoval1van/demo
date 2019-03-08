package com.ncube.demo.repository;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

@Component
public class FileRepository {

    @Value("${file.path}")
    private String filePath;

    public String uploadFile(String originalFileName, InputStream fileInputStream) throws IOException {
        String filePrefix = UUID.randomUUID().toString();

        String fileName = filePrefix + originalFileName;
        String filePath = this.filePath + "/" + fileName;
        File targetFile = new File(filePath);
        FileUtils.copyInputStreamToFile(fileInputStream, targetFile);
        return  fileName;
    }

    public File downloadFile(String filename) {
        return  FileUtils.getFile(filePath + "/" + filename);
    }
}
