
import data.Genes;

import java.io.*;

import main.FillSequences;
import util.GTFParser;
import data.Gene;
import data.Protein;
import data.Transcript;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import util.GenomeSequenceExtractor;
import util.Isoform;
import util.SpliceEvent;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * @author uhligc
 */
public class Test1 {
    final static FileSystem FS = FileSystems.getDefault();

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        String geneid = "ENSG00000176974";

        String serdatabase = "res/gobiblatt6_test_1.ser";
        Genes g = null;
        String path = "/home/proj/biosoft/GENOMIC/HUMAN/Homo_sapiens.GRCh37.63.gtf";
        File f = new File(serdatabase);
        if (f.exists()) {

            try {
                g = deserializeDatabase(serdatabase);
            } catch (ClassNotFoundException | InvalidClassException e) {
                System.out.println("classnotfoundexception");
                g = new Genes();
                GTFParser.readFile(path, g);
                serializeDatabase(g, serdatabase);
            } catch (IOException e) {
            }
        } else {
            g = new Genes();
            GTFParser.readFile(path, g);
            serializeDatabase(g, serdatabase);
        }

        String[] bla = g.getRandomProteinIDs();
        /*while (g.getGenes().values().iterator().hasNext()) {
            Gene gene = g.getGenes().values().iterator().next();

        }*/


        g.getGenes().values().iterator().next();
        String proteinid1 = bla[0];
        String proteinid2 = bla[1];
        System.out.println(proteinid1.equals(proteinid2));

        String a = FillSequences.getProteinSequence(proteinid1, g);
        String b = FillSequences.getProteinSequence(proteinid2, g);

        SpliceEvent se = new SpliceEvent(g.getProtein(proteinid1), g.getProtein(proteinid2));
        se.main();

        System.out.println(a);
        System.out.println(b);
        System.out.println(se);
        System.out.println("Unique Exons = " + g.uniqueExons());
        System.out.println("a = " + g.getProtein(proteinid1).getLength() + " seqa = " + a.length() + " varsplic = " + se.toString().length());
        List<String> proteinseq = new ArrayList<String>();
        int totalletters = 0;
        int totalletters2 = 0;
        for (int i = 0; i < 20; i++) {
            String proteinid = g.getRandomProteinIDs()[0];
            totalletters2 += g.getProtein(proteinid).getLength();
            totalletters += FillSequences.getProteinSequence(proteinid, g).length();
        }
        System.out.println("Average Count = " + totalletters / 20 + " LengthbyCount = " + totalletters2 / 20);
//        gene.getTranscript(proteinid1);
//        HashMap<String, Transcript> map = gene.getTranscripts();
//        Protein p1 = g.getProtein(proteinid1);
//        Protein p2 = g.getProtein(proteinid2);

//        Transcript t1 = null, t2 = null;
//        for (Map.Entry<String, Transcript> entry : map.entrySet()) {
//            String transcript_id = entry.getKey();
//            Transcript transcript = entry.getValue();
//            if (transcript.getProtein().getId().equals(proteinid1)) {
//                t1 = transcript;
//            }
//            if (transcript.getProtein().getId().equals(proteinid2)) {
//                t2 = transcript;
//            }
//        }
//        Isoform isoform = new Isoform(p1, p2);
//        isoform.fillSets();
//        isoform.processToAAseq();
    }

    public static void serializeDatabase(Genes g, String stringpath) throws IOException {
        FileOutputStream fileOut = new FileOutputStream(stringpath);
        ObjectOutputStream out = new ObjectOutputStream(fileOut);
        out.writeObject(g);
        out.close();
        fileOut.close();
    }

    public static Genes deserializeDatabase(String stringpath) throws IOException, ClassNotFoundException {
        FileInputStream fileIn = new FileInputStream(stringpath);
        ObjectInputStream in = new ObjectInputStream(fileIn);
        Genes g = (Genes) in.readObject();
        in.close();
        fileIn.close();
        return g;
    }
}
