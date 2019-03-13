package com.ncube.demo.service;


import com.ncube.demo.repository.FileRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static org.mockito.Mockito.verify;


@RunWith(SpringRunner.class)
@SpringBootTest
public class FileServiceTest {

    @MockBean
    private FileRepository fileRepository;

    @Autowired
    private FileService fileService;


    @Test
    public void download() {
        String fileName = "temp.img";
        fileService.download(fileName);
        verify(fileRepository).downloadFile(fileName);
    }
}