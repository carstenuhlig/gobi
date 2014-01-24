package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by carsten on 24.01.14.
 */
public class IO {
    public static final FileSystem FS = FileSystems.getDefault();
    static BufferedWriter wr;

    public static void startBufferedWriter(String stringpathtofile) throws IOException {
        Path path = FS.getPath(stringpathtofile);
        wr = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
    }

    public static void closeBufferedWriter() throws IOException {
        wr.close();
    }

    public static void writeBufferedWriter(String content) throws IOException {
        wr.write(content);
    }
}
