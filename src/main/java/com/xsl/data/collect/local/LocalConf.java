package com.xsl.data.collect.local;

import java.util.Properties;

/**
 * Created by howard on 16/4/28.
 */
public class LocalConf {

    private String directory;
    private String fileName;

    public LocalConf directory(String directory) {
        this.directory = directory;
        return this;
    }

    public LocalConf fileName(String fileName) {
        this.fileName = fileName;
        return this;
    }

    public Properties toPreperties() {
        Properties properties = new Properties();
        properties.setProperty(LocalSenderConfiguration.FILE_DIRECTORY, directory);
        properties.setProperty(LocalSenderConfiguration.FILE_NAME, fileName);
        return properties;
    }

    public LocalConf() {
    }

    public LocalConf(String directory, String fileName) {
        this.directory = directory;
        this.fileName = fileName;
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

    @Override
    public String toString() {
        return "LocalConf{" +
                "directory='" + directory + '\'' +
                ", fileName='" + fileName + '\'' +
                '}';
    }
}
