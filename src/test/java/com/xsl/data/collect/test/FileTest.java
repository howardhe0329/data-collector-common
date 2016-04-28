package com.xsl.data.collect.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * Created by howard on 16/4/28.
 */
public class FileTest {

    public static void main(String[] args) {
        String fileName = "log/data.log";
        RandomAccessFile randomAccessFile = null;
        FileChannel fileChannel = null;
        try {
            randomAccessFile = new RandomAccessFile(fileName, "rw");
            fileChannel = randomAccessFile.getChannel();
            fileChannel.write(ByteBuffer.wrap("hello\n".getBytes()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileChannel.close();
                randomAccessFile.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
