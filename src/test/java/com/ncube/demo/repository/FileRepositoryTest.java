package com.ncube.demo.repository;

import lombok.SneakyThrows;
import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.charset.Charset;

import static junit.framework.TestCase.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
@TestPropertySource(locations = "classpath:test-application.properties")
public class FileRepositoryTest {

    @Autowired
    private FileRepository fileRepository;

    @Value("${file.path}")
    private String fileFolder;

    @SneakyThrows
    @Before
    public void createFolder() {
        FileUtils.forceMkdir(new File(fileFolder));
    }

    @SneakyThrows
    @After
    public void dropFolder() {
        FileUtils.deleteQuietly(new File(fileFolder));
    }

    @SneakyThrows
    @Test
    public void uploadFile() {
        String originalFileName = "test.img";
        String fileName = fileRepository
                .uploadFile(originalFileName, new ByteArrayInputStream(Charset.forName("UTF-16").encode("test").array()));
        assertTrue("Stored file should contain original file name", fileName.contains(originalFileName));
        assertTrue("Should save file to directory from properties", FileUtils.getFile(fileFolder + "/" + fileName).exists());
    }

    @SneakyThrows
    @Test
    public void downloadFile() {
        String originalFileName = "test.img";
        String fileName = fileRepository
                .uploadFile(originalFileName, new ByteArrayInputStream(Charset.forName("UTF-16").encode("test").array()));
        assertTrue(fileRepository.downloadFile(fileName).exists());
    }
}
