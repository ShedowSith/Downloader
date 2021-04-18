package ru.rahimyanov.downloader.utils;

import com.google.common.util.concurrent.RateLimiter;
import org.apache.commons.io.FileUtils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class FileLoaderLimit500 implements FileLoader {
    @Override
    public void loadFile(URL url, String fileName) {
        final RateLimiter throttler = RateLimiter.create(500 * FileUtils.ONE_KB);
        try(InputStream in = new ThrottlingInputStream(url.openStream(), throttler); FileOutputStream out = new FileOutputStream(fileName)) {
            byte buffer[] = new byte[1024];
            while(in.read(buffer) != -1) {
                out.write(buffer);
            }
        }catch (UnknownHostException ue){
            Logger.getLogger("ru.ruhimyanov.downloader.exception").log(Level.WARNING, "IP-адрес хоста не определен.", ue.getMessage());
        }catch (FileNotFoundException fe){
            Logger.getLogger("ru.ruhimyanov.downloader.exception").log(Level.WARNING, "Файл для загрузки не найден.", fe.getMessage());
        }
        catch (IOException e) {
            e.printStackTrace();

        }
    }
}
