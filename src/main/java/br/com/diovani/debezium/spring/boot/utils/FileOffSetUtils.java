package br.com.diovani.debezium.spring.boot.utils;

import lombok.experimental.UtilityClass;

import java.io.File;
import java.io.IOException;

@UtilityClass
public class FileOffSetUtils {

    public String create(String fileName) throws IOException {
        String tempDir = System.getProperty("java.io.tmpdir");
        var storageTempFile = new File(tempDir, fileName);
        if (!storageTempFile.exists()) {
            storageTempFile.createNewFile();
        }
        return storageTempFile.getAbsolutePath();
    }

}
