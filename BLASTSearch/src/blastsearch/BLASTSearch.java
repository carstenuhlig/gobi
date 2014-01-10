/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package blastsearch;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author carsten
 */
public class BLASTSearch {

    public static FileSystem FS = FileSystems.getDefault();
    static int[][] substitutionmatrix;
    static char[] alphabet;
    static HashMap<Character, Integer> alphabet_hashmap;
    static char[] sequence;
    static String seq;
    static int wordlength;
    static int threshold;
    static Path outputfile;
    static Path fastafile;
    static Path substitionfile;
    static LinkedList<String> substrings;
    static LinkedList<String> generatedStrings;

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

        alphabet_hashmap = new HashMap<>();

        importSubstitionMatrix();
        importFastaFile();
        makeSubstrings();
        genWords(wordlength);
        exportToFile();
    }

    public static List<String> genWords(int length) {
        generatedStrings = new LinkedList<>();
        int iterations = (int) Math.pow(alphabet.length, length);
        int[] intWord = new int[length];
        int[] subSequence = null;
        for (int i = 0; i < iterations; i++) {
            for (String seq : substrings) {
                subSequence = getIntWordFromString(seq);
                if (calcDistance(intWord, subSequence) > threshold) {
                    generatedStrings.add(getStringFromIntWord(intWord));
                }
            }
            nextIntWord(intWord, alphabet.length);
        }
        return generatedStrings;
    }

    public static int[] getIntWordFromString(String seq) {
        int[] returnintarray = new int[seq.length()];
        int counter = 0;
        for (char c : seq.toCharArray()) {
            try {
                returnintarray[counter] = alphabet_hashmap.get(c);
            } catch (NullPointerException e) {
                returnintarray[counter] = alphabet.length-1;
            }
            counter++;
        }
        return returnintarray;
    }

    public static int calcDistance(int[] wordA, int[] wordB) {
        int sum = 0;
        for (int i = 0; i < wordA.length; i++) {
            sum += substitutionmatrix[wordA[i]][wordB[i]];
        }
        return sum;
    }

    //TODO Testen
    public static void nextIntWord(int[] currentWord, int alphabet_length) {
        boolean inc = true;
        int i = 0;
        while (inc == true && wordlength > i) {
            currentWord[i]++;
            if (currentWord[i] == alphabet_length) {
                currentWord[i] = 0;
            } else {
                inc = false;
            }
            i++;
        }
    }

    public static char[] getCharFromIntWord(int[] intWord) {
        char[] charWord = new char[intWord.length];
        for (int i = 0; i < intWord.length; i++) {
            charWord[i] = alphabet[intWord[i]];
        }
        return charWord;
    }

    public static String getStringFromIntWord(int[] intWord) {
        StringBuilder sb = new StringBuilder();
        for (int i : intWord) {
            sb.append(alphabet[i]);
        }
        return sb.toString();
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
                    alphabet = new char[bla.length];
                    substitutionmatrix = new int[bla.length][bla.length];
                    //TODO überprüfung ob bei split mit leerzeichen vorher auch kein leeres feld in String array entsteht

                    for (int i = 1; i < bla.length; i++) {
                        alphabet[i] = bla[i].charAt(0);
                        alphabet_hashmap.put(bla[i].charAt(0), i);
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
        substrings = new LinkedList<>();
        for (int i = 0; i < seq.length() - 1 - wordlength; i++) {
            substrings.add(seq.substring(i, i + wordlength));
        }
    }

    public static void exportToFile() throws IOException {
        BufferedWriter bla = Files.newBufferedWriter(outputfile, StandardCharsets.UTF_8 );
        for (String string : generatedStrings) {
            bla.write(string + "\n");
        }
        bla.close();
    }
}
