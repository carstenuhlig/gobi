package util;

import data.Database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by carsten on 23.01.14.
 */
public class BiomartParser {
    public static final FileSystem FS = FileSystems.getDefault();
    static Database d;

    public static void init(Database d) {
        BiomartParser.d = d;
    }

    public static void readGOTermProteinFile(String stringpath) throws IOException {
        Path path = FS.getPath(stringpath);
        BufferedReader r = Files.newBufferedReader(path, StandardCharsets.UTF_8);
        String line;
        String[] pieces;
        while ((line = r.readLine()) != null) {
            if (!line.contains("GO Term")) {
                pieces = line.split("\t");
                d.addNodeInformation(pieces[0], pieces[1]);
            }
        }
        r.close();
    }
}
