package ru.rahimyanov.downloader.utils;

import java.io.File;

public interface PoolDownloader {
    void setFileLoader(FileLoader loader);
    void setFileURL(File fileURL);
    void startLoad();
    void setFolder(String folder);
}
