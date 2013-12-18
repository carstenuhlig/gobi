/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import data.Protein;
import data.Transcript;
import java.io.IOException;
import java.util.HashSet;
import java.util.TreeSet;

/**
 *
 * @author uhligc
 */
public class Isoform {

    Transcript t1, t2;
    Protein p1, p2;

    HashSet<Long> i1 = new HashSet<Long>();
    HashSet<Long> i1_aa = new HashSet<Long>();
    HashSet<Long> i2 = new HashSet<Long>();
    HashSet<Long> i2_aa = new HashSet<Long>();

    String aa1 = "";
    String aa2 = "";
    String nseq1 = "";
    String nseq2 = "";

    HashSet<Long> combined = new HashSet<Long>();
    HashSet<Long> deletions = new HashSet<Long>();
    HashSet<Long> insertions = new HashSet<Long>();
    HashSet<Long> all = new HashSet<>();
    HashSet<Long> substitutions = new HashSet<>();

    public Isoform(Transcript t1, Transcript t2) {
        this.t1 = t1;
        this.t2 = t2;
        p1 = t1.getProtein();
        p2 = t2.getProtein();
    }

    public Isoform(Protein p1, Protein p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    public void fillSets() {
        i1 = new HashSet<>();
        i2 = new HashSet<>();

        //isoform1
        int exonsanzahl = p1.getNrExons();
        for (int i = 0; i < exonsanzahl; i++) {
            int frame = p1.getExon(i).getCDS().getFrame();
            long start = p1.getExon(i).getCDS().getStart();
            long stop = p1.getExon(i).getCDS().getStop();
            if (start < stop) {
                for (long j = start + frame; j < stop + 1; j++) {
                    i1.add(j);
                }
            } else {
                System.err.print("hier ist ein Fehler " + stop + ">" + start);
            }
        }

        exonsanzahl = p2.getNrExons();
        for (int i = 0; i < exonsanzahl; i++) {
            int frame = p2.getExon(i).getCDS().getFrame();
            long start = p2.getExon(i).getCDS().getStart();
            long stop = p2.getExon(i).getCDS().getStop();
            if (start < stop) {
                for (long j = start + frame; j < stop + 1; j++) {
                    i2.add(j);
                }
            } else {
                System.err.print("hier ist ein Fehler " + stop + ">" + start);
            }
        }

        deletions.addAll(i1);
        deletions.removeAll(i2);

        insertions.addAll(i2);
        insertions.removeAll(i1);

        combined.addAll(i1);
        combined.retainAll(i2);

        all.addAll(i1);
        all.addAll(i2);
    }

//    public void getSequence() throws IOException {
//        long start = 0;
//        long stop = 0;
//        String chromosome = 
//
//        StringBuilder nucleotide_seq = new StringBuilder();
//        TreeSet<Long> positions = new TreeSet<>(i1);
//
//        for (Long pos : positions) {
//            if (pos == stop + 1) {
//                stop = pos;
//            } else {
//                //hier altes exon erst speichern
//                nucleotide_seq.append(GenomeSequenceExtractor.easySearch(nseq1, start, stop));
//                //und dann start neu setzten sowie stop ... irgendwie
//                
//            }
//        }
//    }
    public void processToAAseq() {
        int counter = 0;
        TreeSet<Long> tree = new TreeSet<>(i1);

        for (Long pos : tree) {
            if (counter % 3 == 0) {
                i1_aa.add(pos);
            }
            counter++;
        }

        counter = 0;
        tree = new TreeSet<>(i2);

        for (Long pos : tree) {
            if (counter % 3 == 0) {
                i2_aa.add(pos);
            }
            counter++;
        }
    }

    @Override
    public String toString() {
        TreeSet<Long> output = new TreeSet<Long>();
        output.addAll(all);
        TreeSet<Long> output2 = new TreeSet<Long>();
        
        output2.addAll(i1_aa);
        output2.addAll(i2_aa);
        
        StringBuilder dna_output = new StringBuilder();
        StringBuilder aa_output = new StringBuilder();
        
        for (Long pos : output) {
            if (i1.contains(pos) && i2.contains(pos)) {
                dna_output.append("*");
            } else if (i1.contains(pos)) {
                dna_output.append("D");
            } else if (i2.contains(pos)) {
                dna_output.append("I");
            } else {
                dna_output.append(" ");
            }
        }

        for (Long pos : output2) {
            if (i1_aa.contains(pos) && i2_aa.contains(pos)) {
                aa_output.append("*");
            } else if (i1_aa.contains(pos)) {
                aa_output.append("D");
            } else if (i2_aa.contains(pos)) {
                aa_output.append("I");
            } else {
                aa_output.append(" ");
            }
            aa_output.append("  ");
        }
        return dna_output.toString() + "\n" + aa_output.toString();
    }
}
