/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author uhligc
 */
public class GenomeSequenceExtractor {

    static FileSystem FS = FileSystems.getDefault();
    HashMap<String, FileDouble> files = new HashMap<String, FileDouble>();
    final static String default_prefix = "/home/proj/biosoft/GENOMIC/HUMAN/HUMAN_GENOME_FASTA/Homo_sapiens.GRCh37.63.dna.chromosome.";

    public void init(String prefix_path) throws FileNotFoundException, IOException {
        files.put("X", new FileDouble(new RandomAccessFile(prefix_path + "X.fa", "r")));
        files.put("Y", new FileDouble(new RandomAccessFile(prefix_path + "Y.fa", "r")));
        for (int i = 1; i < 23; i++) {
            files.put("" + i, new FileDouble(new RandomAccessFile(prefix_path + i + ".fa", "r")));
        }
    }

    public void close() throws IOException {
        for (Map.Entry<String, FileDouble> entry : files.entrySet()) {
            FileDouble raf = entry.getValue();
            raf.getRaf().close();
        }
    }

    public String getSequence(String chromosome, long start, long stop) throws IOException {
        FileDouble fb = files.get(chromosome);
        start += (fb.getHeaderoffset() + start / 60);
        stop += (fb.getHeaderoffset() + start / 60);
        byte[] b = new byte[(int) (stop - start + 1)];
        fb.getRaf().seek(start - 1);
        fb.getRaf().read(b);
        String tmp = new String(b);
        String[] tmp2 = tmp.split("\n");
        StringBuilder sb = new StringBuilder();
        for (String str : tmp2) {
            sb.append(str);
        }
        return sb.toString();
    }
    
    public static String easySearch(String chromosome, long start, long stop) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(default_prefix + chromosome + ".fa", "r");
        raf.seek(0);
        int headeroffset = raf.readLine().length();
        start += (headeroffset + start / 60);
        stop += (headeroffset + stop / 60);
        byte[] b = new byte[(int) (stop - start + 1)];
        raf.seek(start);
        raf.read(b);
        raf.close();
        String tmp = new String(b);
        String[] tmp2 = tmp.split("\n");
        StringBuilder sb = new StringBuilder();
        for (String str : tmp2) {
            sb.append(str);
        }
        return sb.toString();
    }

    class FileDouble {

        RandomAccessFile raf;
        int headeroffset;

        FileDouble(RandomAccessFile raf) throws IOException {
            this.raf = raf;
            raf.seek(0);
            String tmp = raf.readLine();
            headeroffset = tmp.length();
        }

        public int getHeaderoffset() {
            return headeroffset;
        }

        public RandomAccessFile getRaf() {
            return raf;
        }
    }
}
