package com.xsl.data.collect.local;

import com.xsl.data.collect.common.*;
import com.xsl.data.collect.core.AbstractSender;
import com.xsl.data.collect.event.Event;
import com.xsl.data.collect.metrics.Counter;
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
    private RandomAccessFile randomAccessFile;
    private LocalDate beforeDay;
    private boolean isImmediateFlush;
    private ByteBuffer buffer;
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
            if (name.contains(lastDay.toString())) {
                return true;
            }
            return false;
        });
        if (fileNameArray == null || fileNameArray.length == 0) {
            int pos = fileName.lastIndexOf(".");
            this.activeFile.renameTo(new File(directory, fileName.substring(0, pos + 1)
                    + lastDay.toString() + fileName.substring(pos)));
            this.activeFile = new File(this.directory, this.fileName);
        }
    }

    private void open() {
        try {
            randomAccessFile = new RandomAccessFile(this.activeFile, "rw");
            randomAccessFile.seek(randomAccessFile.length());
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
            lock.lock();
            flush();
            if (randomAccessFile != null) {
                randomAccessFile.close();
            }
        } catch (IOException e) {
            LOGGER.error("Unable to close File Channel. Exception follows.", e);
        } finally {
            lock.unlock();
            randomAccessFile = null;
        }
    }

    @Override
    public void configure(Properties properties) {
        if(counter == null) {
            counter = new Counter(Counter.Type.LOCAL);
        }
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
        if (!this.directory.exists()) {
            boolean res = this.directory.mkdirs();
            if (!res) {
                throw new ConfigurationException("mkdir error, please check directory is invalid or use chmod check" + directory);
            }
        }
        this.fileName = properties.getProperty(LocalSenderConfiguration.FILE_NAME);
        if (this.fileName == null || this.fileName.isEmpty()) {
            throw new IllegalArgumentException("File's name may not be null");
        }
        this.activeFile = new File(this.directory, this.fileName);
        this.isImmediateFlush = Boolean.valueOf(properties.getProperty(
                LocalSenderConfiguration.FILE_FLUSH, String.valueOf(LocalSenderConfiguration.DEFAULT_IMMEDIATE_FLUSH)));
        int bufferSize = Integer.valueOf(
                properties.getProperty(LocalSenderConfiguration.BUFFER_SIZE, String.valueOf(LocalSenderConfiguration.DEFAULT_BUFFER_SIZE)));
        this.buffer = ByteBuffer.allocate(bufferSize);
    }

    @Override
    public void send(Event event) {
        if (debugMode.equals(DebugMode.ON)) {
            LOGGER.info("print event: {}", new String(event.getBody()));
            return;
        }
        long startTime = System.currentTimeMillis();
        try {
            LocalDate lastDay = LocalDate.now().plusDays(-1);
            if (lastDay.isAfter(beforeDay)) {
                try {
                    lock.lock();
                    if (lastDay.isAfter(beforeDay)) {
                        close();
                        rename(lastDay);
                        open();
                        beforeDay = lastDay;
                    }
                } finally {
                    lock.unlock();
                }
            }
            byte[] bytes = new StringBuilder(new String(event.getBody())).append('\n').toString().getBytes();
            write(bytes, 0, bytes.length);
            counter.incrementSuccessCount();
        } catch (Exception e) {
            counter.incrementFailedCount();
            throw e;
        } finally {
            long endTime = System.currentTimeMillis();
            counter.addAndGetSendTime(endTime - startTime);
        }
    }

    /**
     * 将数据写入到Buffer中
     *
     * @param bytes  数据字节数组
     * @param offset 启始位置
     * @param length 数据长度
     */
    private void write(final byte[] bytes, int offset, int length) {
        try {
            lock.lock();
            int chunk;
            do {
                // 如果将要写入Buffer中的数据长度大于Buffer中剩余的大小, 则直接flush到磁盘中
                if (length > buffer.remaining()) {  // remaining方法是 limit - position
                    flush();
                }
                // 设置chunk为 数据长度与Buffer剩余的大小中的最小值. 是防止数据长度大于缓冲区中的剩余空间长度.
                chunk = Math.min(length, buffer.remaining());
                // 将数据写入到Buffer
                buffer.put(bytes, offset, chunk);
                offset += chunk;
                length -= chunk;
            } while (length > 0);
            //是否立即flush
            if (isImmediateFlush) {
                flush();
            }
        } finally {
            lock.unlock();
        }
    }

    /**
     * flush到磁盘里
     */
    private void flush() {
        try {
            lock.lock();
            buffer.flip(); // 缓冲区模式, 切换为读模式 limit = position; position = 0; mark = -1
            try {
                randomAccessFile.write(buffer.array(), 0, buffer.limit());  // limit == position
                LOGGER.debug("Buffer [ Buffer.limit: {}, Buffer.capacity: {}] flush to file [{}] success", buffer.limit(), buffer.capacity(), this.activeFile.getName());
            } catch (IOException e) {
                LOGGER.error("Unable to flush file, " + this.activeFile, e);
            }
            buffer.clear(); // 清空缓冲区中的数据 position = 0; limit = capacity; mark = -1
        } finally {
            lock.unlock();
        }
    }

    @Override
    public void send(String topic, Event event) {
        throw new UnsupportedOperationException();
    }
}
