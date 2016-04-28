package com.xsl.data.collect.test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Locale;

/**
 * Created by howard on 16/4/28.
 */
public class FileLoopTest {

    public static void main(String[] args) {
        String directory = "log/test";
        File file = new File(directory);
        File activeFile = new File(directory, "test.log");
        if (!file.exists()) {
            file.mkdirs();
        }
        LocalDate current = LocalDate.now();
        for (int i = 0; i < 10; i++) {
            try {
                new File(directory, "test." + current.plusDays(i - 10).toString() + ".log").createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        RandomAccessFile randomAccessFile = null;
        FileChannel fileChannel = null;
        try {
            randomAccessFile = new RandomAccessFile(activeFile, "rw");
            fileChannel = randomAccessFile.getChannel();
            fileChannel.write(ByteBuffer.wrap("hello".getBytes()));
            fileChannel.write(ByteBuffer.wrap("\n".getBytes()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
//            try {
//                fileChannel.close();
//                randomAccessFile.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        }
        File[] files = file.listFiles();
        boolean hasFile = false;
        String dateStr = current.toString();
        for (int i = 0; i < files.length; i++) {
            String fileName = files[i].getName();
            if (fileName.contains(dateStr)) {
                System.out.println(fileName);
                hasFile = true;
                break;
            }
        }

        String[] fileNameArray = file.list((dir, name) -> {
            if(name.contains(current.plusDays(-1).toString())) {
                return true;
            }
            return false;
        });
        System.out.println(Arrays.toString(fileNameArray));

        if (!hasFile) {
            activeFile.renameTo(new File(directory, "test." + dateStr + ".log"));
        }

        try {
            fileChannel.write(ByteBuffer.wrap("end\n".getBytes()));
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
