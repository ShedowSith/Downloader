package ru.rahimyanov.downloader.utils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;



public class PoolDownloaderImpl implements PoolDownloader {
    private FileLoader fileLoader;
    private File fileURL;
    private String folder;

    @Override
    public void setFileLoader(FileLoader loader) {
        this.fileLoader = loader;
    }

    @Override
    public void setFileURL(File fileURL) {
        this.fileURL = fileURL;
    }

    @Override
    public void startLoad(){
        String[] list;
        ExecutorService executor = Executors.newFixedThreadPool(3);
        try (Stream<String> linesStream = Files.lines(fileURL.toPath())) {
            list = linesStream.toArray(String[]::new);
            Arrays.stream(list).forEach(line -> executor.execute(() -> {
                try {
                    String[] name = line.split("/");
                    fileLoader.loadFile(new URL(line), folder+File.separator+name[name.length-1]);
                } catch (MalformedURLException e) {
                    Logger.getLogger("ru.ruhimyanov.downloader.exception").log(Level.WARNING, "Неверная URL ссылка.", e.getMessage());
                }
            }));
        } catch (IOException e) {
            e.printStackTrace();
        }
        executor.shutdown();

    }

    @Override
    public void setFolder(String folder) {
        this.folder = folder;
    }
}
