package ru.rahimyanov.downloader.utils;

import com.google.common.util.concurrent.RateLimiter;

import java.io.IOException;
import java.io.InputStream;

public class ThrottlingInputStream extends InputStream {
    private final InputStream target;
    private final RateLimiter maxBytesPerSecond;

    public ThrottlingInputStream(InputStream target, RateLimiter maxBytesPerSecond) {
        this.target = target;
        this.maxBytesPerSecond = maxBytesPerSecond;
    }

    @Override
    public int read() throws IOException {
        maxBytesPerSecond.acquire(1);
        return target.read();
    }

    @Override
    public int read(byte[] b) throws IOException {
        maxBytesPerSecond.acquire(b.length);
        return target.read(b);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {
        maxBytesPerSecond.acquire(len);
        return target.read(b, off, len);
    }
}
