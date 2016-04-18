package com.xsl.data.collect.test;

import com.xsl.data.collect.util.ConfigurationProvider;
import org.junit.Test;

import java.io.File;


/**
 * Created by howard on 16/4/12.
 */
public class ConfigurationProviderTest {

    @Test
    public void testLoad() {
        ConfigurationProvider provider = new ConfigurationProvider();
        provider.loads().forEach((key, value) ->
            System.out.printf("key: %s; value: %s\n", key, value));
    }

    @Test
    public void test() {
        String fileSeparator = System.getProperty("file.separator");
        String path = ConfigurationProviderTest.class.getResource(fileSeparator).getPath();
        System.out.println(path);

        File file = new File(path);

        for (File file1 : file.listFiles((File dir, String name) -> name.endsWith(".properties"))) {
            System.out.println(file1.getName());
        }

    }
}
