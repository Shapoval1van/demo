package com.ncube.demo.service;

import com.ncube.demo.repository.FileRepository;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class FileService {

    private final FileRepository fileRepository;

    public FileService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public File download(String fileName) {
        return fileRepository.downloadFile(fileName);
    }
}
