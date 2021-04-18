package ru.rahimyanov.dovnloader;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import ru.rahimyanov.downloader.spring.Config;
import ru.rahimyanov.downloader.utils.FileLoader;
import ru.rahimyanov.downloader.utils.PoolDownloader;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;


import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import static org.springframework.test.util.AssertionErrors.assertTrue;


@SpringBootTest(classes = Config.class)
public class TestsDownloader {
    @Autowired
    PoolDownloader downloader;

    @Autowired
    FileLoader fileLoader;
    @TempDir
    File tempDir;
    private static File tempFile;

    @BeforeAll
    public static void setup() throws IOException {
        List<String> lines = Arrays.asList("https://www.fotostoki.ru/blog/wp-content/uploads/2018/10/canva_ceo.jpg",
                "https://bipbap.ru/wp-content/uploads/2017/04/0_7c779_5df17311_orig.jpg",
                "https://artemmazur.ru/wp-content/uploads/2017/07/besplatnye-fotostoki-associacii.jpg");

        tempFile = File.createTempFile("list", ".txt");
        Files.write(tempFile.toPath(), lines, StandardCharsets.UTF_8);
    }

    @Test
    public void fileUploadTest() throws InterruptedException {
        downloader.setFileURL(tempFile);
        downloader.setFolder(tempDir.getAbsolutePath());
        downloader.startLoad();
        // Ждем до завершения скачивания файлов
        Thread.sleep(7000);
        assertAll(
                () -> assertTrue("Файл скачен", Files.exists(Path.of(tempDir.toPath()+"\\canva_ceo.jpg"))),
                () -> assertTrue("Файл скачен", Files.exists(Path.of(tempDir.toPath()+"\\0_7c779_5df17311_orig.jpg"))),
                () -> assertTrue("Файл скачен", Files.exists(Path.of(tempDir.toPath()+"\\besplatnye-fotostoki-associacii.jpg")))
        );
    }

    @Test
    public void exceptionsTest(){
        // Не верное имя хоста
        assertDoesNotThrow(() -> fileLoader.loadFile(new URL("https://exceptionsTest"), tempDir+File.separator+"exceptionsTest"));
        // Файл не существует
        assertDoesNotThrow(() -> fileLoader.loadFile(new URL("https://www.fotostoki.ru/blog/wp-content/uploads/2018/10/canva_ce.jpg"), tempDir+File.separator+"canva_ce.jpg"));
    }

    @Test
    public void downloadSpeedTest() throws MalformedURLException {
        /*
        * Размер файла 2613 Kb
        * Примерное время скачивания 5226 мс
        * */
        long startTime = System.currentTimeMillis();
        fileLoader.loadFile(new URL("https://bipbap.ru/wp-content/uploads/2017/04/0_7c779_5df17311_orig.jpg"), tempDir+File.separator+"exceptionsTest");
        long endTime = System.currentTimeMillis();
        assertTrue("Время скачивания больше чем 5226мс", (endTime-startTime)>5226);
    }
}
