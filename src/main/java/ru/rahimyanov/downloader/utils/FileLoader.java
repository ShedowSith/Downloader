package ru.rahimyanov.downloader.utils;

import java.net.URL;

public interface FileLoader {
    void loadFile(URL url, String fileName);
}
