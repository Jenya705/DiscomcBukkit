package com.github.jenya705.util;

import com.github.jenya705.Discomc;
import lombok.experimental.UtilityClass;

import java.io.*;

@UtilityClass
public class FilesUtil {

    public static void saveFromInputStream(File file, InputStream inputStream) throws IOException {
        OutputStream fileOutputStream = new FileOutputStream(file);
        byte[] bytes = new byte[1024];
        int len;
        do {
            len = inputStream.read(bytes);
            if (len < 1) break;
            fileOutputStream.write(bytes, 0, len);
        } while (true);
        fileOutputStream.flush();
        inputStream.close();
        fileOutputStream.close();
    }

    public static File getRecursivelyFile(String... destination) {
        File file = Discomc.getPlugin().getDataFolder();
        for (String directory: destination) {
            file = new File(file, directory);
        }
        return file;
    }

}
