package com.api.ewallet.util;

import com.google.common.io.CharStreams;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

@Slf4j
public class TestUtil {

    public static String getJsonFromFile(String path) throws IOException{
        InputStream inputStream = ClassLoader.getSystemResourceAsStream(path);
        if (inputStream == null) {
            throw new IOException("File not found: " + path);
        }

        try(Reader reader = new InputStreamReader(inputStream)){
            return CharStreams.toString(reader);
        }
    }
}
