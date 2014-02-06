package util;

import data.Genes;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.util.regex.Pattern;

/**
 * Created by carsten on 06.02.14.
 */
public class IO {
    public static final FileSystem FS = FileSystems.getDefault();
    static final Pattern PATTERN_PROSITE_FASTA = Pattern.compile("^>(\\w+)\\/(\\d+)-(\\d+) : (\\w+)\\s+(\\w+).*");

    public static void importPrositePattern(String path, Genes g) throws IOException {
        BufferedReader br = Files.newBufferedReader(FS.getPath(path), StandardCharsets.UTF_8);
        String line = "";
        while ((line = br.readLine()) != null) {
            if (PATTERN_PROSITE_FASTA.matcher(line).matches()) {

            }
        }
    }
}
