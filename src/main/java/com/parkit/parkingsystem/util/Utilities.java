package com.parkit.parkingsystem.util;

import com.parkit.parkingsystem.config.DataBaseConfig;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class Utilities {
    public static String getResourceFileAsString(String fileName) throws IOException {
        InputStream is = getResourceFileAsInputStream(fileName);
        String line;
        if (is != null) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            line = (String)reader.lines().collect(Collectors.joining(System.lineSeparator()));
            reader.close();
            return line;
        } else {
            throw new RuntimeException("resource not found");
        }

    }
    public static InputStream getResourceFileAsInputStream(String fileName) {
        ClassLoader classLoader = DataBaseConfig.class.getClassLoader();
        return classLoader.getResourceAsStream(fileName);
    }
}
