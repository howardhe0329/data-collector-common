package com.xsl.data.collect.util;

import com.xsl.data.collect.common.ConfigurationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.Properties;

/**
 * Created by howard on 16/4/12.
 */
public class ConfigurationProvider {

    private static final String FILE_SEP = System.getProperty("file.separator");

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private File file;

    public ConfigurationProvider() {
        this.file = new File(getClassPath() + FILE_SEP);
    }
    
    public Properties loads() {
        if(!file.isDirectory()) {
            throw new ConfigurationException("Unable to load file: " + file + " is not directory");
        }
        File[] files = file.listFiles((File dir, String name) -> name.endsWith("data-collect.properties"));
        if(files != null && files.length == 1) {
            return load(files[0]);
        } else {
            files = file.listFiles((File dir, String name) -> name.endsWith(".properties"));
            Properties p = new Properties();
            for (File file1 : files) {
                p.putAll(load(file1));
            }
            return p;
        }
    }

    public Properties load(String fileName) {
        File file = new File(getClassPath() + FILE_SEP + fileName);
        return load(file);
    }

    private String getClassPath() {
        String path = ConfigurationProvider.class.getResource(FILE_SEP).getPath();
        return path;
    }

    private Properties load(File file) {
        BufferedReader reader = null;
        Properties properties = new Properties();
        try {
            reader = new BufferedReader(new FileReader(file));
            properties.load(reader);
        } catch (IOException e) {
            LOGGER.error("Unable to load file: " + file + " (I/O failure) - Exception follows.", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    LOGGER.warn("Unable close file reader for file: " + file, e);
                }
            }
        }
        return properties;
    }
}
