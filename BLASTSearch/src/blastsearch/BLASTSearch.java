/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blastsearch;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author carsten
 */
public class BLASTSearch {

    public static FileSystem FS = FileSystems.getDefault();
    static int[][] substitutionmatrix;
    static char[] charmatrix;
    static char[] sequence;
    static String seq;
    static int wordlength;
    static int threshold;
    static Path outputfile;
    static Path fastafile;
    static Path substitionfile;
    static LinkedList<String> substrings;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        //parameter:
        // 0 = Wortlänge l
        // 1 = Threshold t
        // 2 = Sequenz-Datei in Fasta Format
        // 3 = Scoring-Matrix
        // 4 = Output-File

        wordlength = Integer.parseInt(args[0]);
        threshold = Integer.parseInt(args[1]);
        fastafile = FS.getPath(args[2]);
        substitionfile = FS.getPath(args[3]);
        outputfile = FS.getPath(args[4]);

        importSubstitionMatrix();
        importFastaFile();
        makeSubstrings();

    }

    public static void importSubstitionMatrix() throws IOException {
        //temp vars
        int row = 0;

        List<String> lines = Files.readAllLines(substitionfile, StandardCharsets.UTF_8);
        for (String line : lines) {
            if (!line.startsWith("#")) {
                if (line.startsWith(" ")) {
                    String[] bla = line.split("\\s+");

                    //matrizen initialisieren
                    charmatrix = new char[bla.length];
                    substitutionmatrix = new int[bla.length][bla.length];
                    //TODO überprüfung ob bei split mit leerzeichen vorher auch kein leeres feld in String array entsteht

                    for (int i = 0; i < bla.length; i++) {
                        charmatrix[i] = bla[i].charAt(0);
                    }
                } else {
                    String[] bla = line.split("\\s+");
                    for (int i = 1; i < bla.length; i++) {
                        substitutionmatrix[row][i - 1] = Integer.parseInt(bla[i]);
                    }
                    row++;
                }
            }
        }
    }

    public static void importFastaFile() throws IOException {
        List<String> lines = Files.readAllLines(fastafile, StandardCharsets.UTF_8);
        StringBuilder sb = new StringBuilder();

        for (String line : lines) {
            if (!line.startsWith(">")) {
                sb.append(line);
            }
        }
        seq = sb.toString();
    }

    public static void makeSubstrings() {
        for (int i = 0; i < seq.length() - 1 - wordlength; i++) {
            substrings.add(seq.substring(i, i + wordlength));
        }
    }
}
