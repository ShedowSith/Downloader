package ru.rahimyanov.downloader.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.rahimyanov.downloader.utils.FileLoader;
import ru.rahimyanov.downloader.utils.FileLoaderLimit500;
import ru.rahimyanov.downloader.utils.PoolDownloader;
import ru.rahimyanov.downloader.utils.PoolDownloaderImpl;

@Configuration
public class Config {
    @Bean
    public FileLoader fileLoader(){
        return new FileLoaderLimit500();
    }

    @Bean
    public PoolDownloader poolDownloader(FileLoader fileLoader){
        PoolDownloader downloader = new PoolDownloaderImpl();
        downloader.setFileLoader(fileLoader);
        return downloader;
    }
}
