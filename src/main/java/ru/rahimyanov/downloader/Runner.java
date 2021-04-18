package ru.rahimyanov.downloader;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.rahimyanov.downloader.utils.PoolDownloader;

import java.io.File;

@SpringBootApplication
public class Runner implements CommandLineRunner {
    final PoolDownloader downloader;

    public Runner(PoolDownloader downloader) {
        this.downloader = downloader;
    }

    public static void  main(String[] args) {
        SpringApplication.run(Runner.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        downloader.setFileURL(new File("D:\\Downloader\\file.txt"));
        downloader.setFolder("D:\\Downloader\\");
        downloader.startLoad();
    }
}
