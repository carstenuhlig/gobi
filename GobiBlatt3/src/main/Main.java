package main;

import com.sun.org.apache.xalan.internal.xsltc.runtime.BasisLibrary;
import java.io.IOException;

import data.Genes;
import util.*;

public class Main {

    public static void main(String[] args) {
        Genes g = new Genes();

//		String path = args[0];
        String path = "/home/proj/biosoft/GENOMIC/HUMAN/Homo_sapiens.GRCh37.63.gtf";

        try {
            GTFParser.readFile(path, g);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        System.out.println(g);
        
        try {
            FillSequences.writeSequences(g,"res/seqlibfile.txt","res/pairsfile.txt","res/reference_sequences.txt");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
       System.out.println(g);
    }
}
