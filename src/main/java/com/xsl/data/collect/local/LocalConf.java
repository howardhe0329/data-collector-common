package com.xsl.data.collect.local;

import java.util.Properties;

/**
 * Created by howard on 16/4/28.
 */
public class LocalConf {

    private String directory;
    private String fileName;
    private boolean immediateFlush;
    private int bufferSize;

    public LocalConf directory(String directory) {
        this.directory = directory;
        return this;
    }

    public LocalConf fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public LocalConf immediateFlush(boolean immediateFlush) {
        this.immediateFlush = immediateFlush;
        return this;
    }

    public LocalConf bufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
        return this;
    }

    public Properties toPreperties() {
        Properties properties = new Properties();
        properties.setProperty(LocalSenderConfiguration.FILE_DIRECTORY, directory);
        properties.setProperty(LocalSenderConfiguration.FILE_NAME, fileName);
        properties.setProperty(LocalSenderConfiguration.FILE_FLUSH, String.valueOf(immediateFlush));
        properties.setProperty(LocalSenderConfiguration.BUFFER_SIZE, String.valueOf(bufferSize));
        return properties;
    }

    public LocalConf() {
    }

    public LocalConf(String directory, String fileName) {
        this.directory = directory;
        this.fileName = fileName;
        this.immediateFlush = true;
        this.bufferSize = LocalSenderConfiguration.DEFAULT_BUFFER_SIZE;
    }

    public LocalConf(String directory, String fileName, boolean immediateFlush, int bufferSize) {
        this.directory = directory;
        this.fileName = fileName;
        this.immediateFlush = immediateFlush;
        this.bufferSize = bufferSize;
    }

    public String getDirectory() {
        return directory;
    }

    public void setDirectory(String directory) {
        this.directory = directory;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public boolean isImmediateFlush() {
        return immediateFlush;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    @Override
    public String toString() {
        return "LocalConf{" +
                "directory='" + directory + '\'' +
                ", fileName='" + fileName + '\'' +
                ", immediateFlush=" + immediateFlush +
                ", bufferSize=" + bufferSize +
                '}';
    }
}
