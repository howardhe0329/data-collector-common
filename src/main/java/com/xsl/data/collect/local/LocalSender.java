package com.xsl.data.collect.local;

import com.xsl.data.collect.common.*;
import com.xsl.data.collect.core.AbstractSender;
import com.xsl.data.collect.event.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.time.LocalDate;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by howard on 16/4/14.
 */
public class LocalSender extends AbstractSender {

    private static final Logger LOGGER = LoggerFactory.getLogger(LocalSender.class);
    private DebugMode debugMode;
    private File directory;
    private File activeFile;
    private String fileName;
    private FileChannel fileChannel;
    private RandomAccessFile randomAccessFile;
    private LocalDate beforeDay;
    private Lock lock = new ReentrantLock(true);


    public LocalSender() {
        setName(SendMode.LOCAL.name());
    }

    @Override
    public void start() {
        if (debugMode.equals(DebugMode.ON)) {
            LOGGER.warn("LocalSender is debug mode");
            super.start();
            return;
        }
        LOGGER.info("Local Sender: {} starting", getName());
        try {
            lock.lock();
            beforeDay = LocalDate.now().plusDays(-1);
            rename(beforeDay);
            open();
        } finally {
            lock.unlock();
        }
        super.start();
        LOGGER.info("Local Sender: {} started", getName());
    }

    private void rename(LocalDate lastDay) {
        String[] fileNameArray = directory.list((dir, name) -> {
            if(name.contains(lastDay.toString())) {
                return true;
            }
            return false;
        });
        if(fileNameArray.length == 0) {
            int pos = fileName.lastIndexOf(".");
            this.activeFile.renameTo(new File(directory, fileName.substring(0, pos + 1)
                    + lastDay.toString() + fileName.substring(pos)));
            this.activeFile = new File(this.directory, this.fileName);
        }
    }

    private void open() {
        try {
            randomAccessFile = new RandomAccessFile(this.activeFile, "rw");
            fileChannel = randomAccessFile.getChannel();
            long position = fileChannel.position();
            long size = fileChannel.size();
            if (size != position) {
                fileChannel.position(fileChannel.size());
            }
        } catch (FileNotFoundException e) {
            LOGGER.error("Not found File: " + this.activeFile, e);
        } catch (IOException e) {
            LOGGER.error("File: " + this.activeFile, e);
        }
    }

    @Override
    public void stop() {
        LOGGER.info("Local Sender: {} stopping...", getName());
        close();
        super.stop();
        LOGGER.info("Local Sender: {} stopped. ", getName());
    }

    private void close() {
        try {
            if (fileChannel != null) {
                fileChannel.force(true);
                fileChannel.close();
            }
            if (randomAccessFile != null) {
                randomAccessFile.close();
            }
        } catch (IOException e) {
            LOGGER.error("Unable to close File Channel. Exception follows.", e);
        } finally {
            fileChannel = null;
            randomAccessFile = null;
        }
    }

    @Override
    public void configure(Properties properties) {
        String debugModeName = properties.getProperty(CommonConfiguration.DEBUG_MODE, DebugMode.OFF.name());
        debugMode = DebugMode.valueOf(debugModeName.toUpperCase());
        if (debugMode.equals(DebugMode.ON)) {
            LOGGER.warn("Warning! start debug mode");
            return;
        }
        String directory = properties.getProperty(LocalSenderConfiguration.FILE_DIRECTORY);
        if (directory == null || directory.isEmpty()) {
            throw new IllegalArgumentException("Directory may not be null");
        }
        this.directory = new File(directory);
        if(!this.directory.exists()) {
            boolean res = this.directory.mkdirs();
            if(!res) {
                throw new ConfigurationException("mkdir error, please check directory is invalid or use chmod check" + directory);
            }
        }
        this.fileName = properties.getProperty(LocalSenderConfiguration.FILE_NAME);
        if (this.fileName == null || this.fileName.isEmpty()) {
            throw new IllegalArgumentException("File's name may not be null");
        }
        this.activeFile = new File(this.directory, this.fileName);
    }

    @Override
    public void send(Event event) {
        if (debugMode.equals(DebugMode.ON)) {
            LOGGER.info("print event: {}", new String(event.getBody()));
            return;
        }
        LocalDate lastDay = LocalDate.now().plusDays(-1);
        if(lastDay.isAfter(beforeDay)) {
            try {
                lock.lock();
                if(lastDay.isAfter(beforeDay)) {
                    close();
                    rename(lastDay);
                    open();
                    beforeDay = lastDay;
                }
            } finally {
                lock.unlock();
            }
        }
        if(fileChannel == null) {
            LOGGER.error("FileChannel is null");
            return;
        }
        try {
            fileChannel.write(ByteBuffer.wrap(
                    new StringBuilder(new String(event.getBody())).append('\n').toString().getBytes()));
        } catch (IOException e) {
            LOGGER.error("Unable to write file, " + this.activeFile, e);
        }
    }

    @Override
    public void send(String topic, Event event) {
        throw new UnsupportedOperationException();
    }
}
