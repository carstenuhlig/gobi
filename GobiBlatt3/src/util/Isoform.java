/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import data.Exon;
import data.Protein;
import data.Transcript;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.TreeSet;

/**
 * @author uhligc
 */
public class Isoform {

    //TODO umbenennen denn ist nicht wirklich einen isoform sondern schon das spliceevent
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
    HashMap<Integer, HashSet<Long>> i1_exons = new HashMap<>();
    HashMap<Integer, HashSet<Long>> i2_exons = new HashMap<>();
    TreeSet<Exon> i1_exons_hashset = new TreeSet<>();
    TreeSet<Exon> i2_exons_hashset = new TreeSet<>();

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

    public void dnaAlignment() {
        //Temporäre Exons
        Exon last1 = null;
        Exon last2 = null;

        //StringBuilder
        StringBuilder sb = new StringBuilder();

        //Temporary Counter
        long begin = 0;
        long end = 0;
        long middle = 0;

        //Iterator
        Iterator<Exon> i1_iterator = i1_exons_hashset.iterator();
        Iterator<Exon> i2_iterator = i2_exons_hashset.iterator();

        while (i1_iterator.hasNext() || i2_iterator.hasNext()) {
            if (last1 != null && i1_iterator.hasNext()) {
                last1 = i1_iterator.next();
            }
            if (last2 != null && i2_iterator.hasNext()) {
                last2 = i2_iterator.next();
            }
            if (last1 != null && last2 != null) {
                if (checkCut(last1, last2)) {
                    if (checkSameFrame(last1, last2)) {
                        begin = Math.abs(last1.getCDS().getStart() - last2.getCDS().getStart());
                    }
                }
            } else if (last1 != null) {
                addCharsTimes(sb, 'D', last1.getCDS().getStop() - last1.getCDS().getStart());
            } else {
                addCharsTimes(sb, 'I', last2.getCDS().getStop() - last2.getCDS().getStart());
            }
        }
    }

    public void addCharsTimes(StringBuilder sb, char c, long times) {
        for (int i = 0; i < times; i++) {
            sb.append(c);
        }
    }

    public void addStringTimes(StringBuilder sb, String str, long times) {
        for (int i = 0; i < times; i++) {
            sb.append(str);
        }
    }

    public void fillSets() {
        i1 = new HashSet<>();
        i2 = new HashSet<>();

        i1_exons_hashset = new TreeSet<>();
        i2_exons_hashset = new TreeSet<>();

        //isoform1
        int exonanzahl = p1.getNrExons();
        for (int i = 0; i < exonanzahl; i++) {
            i1_exons_hashset.add(p1.getExon(i));
        }

        //isoform2
        exonanzahl = p2.getNrExons();
        for (int i = 0; i < exonanzahl; i++) {
            i2_exons_hashset.add(p2.getExon(i));
        }
    }

    //        //isoform1
//        int exonanzahl = p1.getNrExons();
//        for (int i = 0; i < exonanzahl; i++) {
//            int frame = p1.getExon(i).getCDS().getFrame();
//            long start = p1.getExon(i).getCDS().getStart();
//            long stop = p1.getExon(i).getCDS().getStop();
//            if (start < stop) {
//                HashSet<Long> tmp_exon = new HashSet<Long>();
//                for (long j = start + frame; j < stop + 1; j++) {
//                    i1.add(j);
//                }
//                i1_exons.put(i, tmp_exon);
//            } else {
//                System.err.print("hier ist ein Fehler " + stop + ">" + start);
//            }
//        }
//
//        //isoform2
//        exonanzahl = p2.getNrExons();
//        for (int i = 0; i < exonanzahl; i++) {
//            int frame = p2.getExon(i).getCDS().getFrame();
//            long start = p2.getExon(i).getCDS().getStart();
//            long stop = p2.getExon(i).getCDS().getStop();
//            if (start < stop) {
//                HashSet<Long> tmp_exon = new HashSet<Long>();
//                for (long j = start + frame; j < stop + 1; j++) {
//                    i2.add(j);
//                }
//                i2_exons.put(i, tmp_exon);
//            } else {
//                System.err.print("hier ist ein Fehler " + stop + ">" + start);
//            }
//        }
//        deletions.addAll(i1);
//        deletions.removeAll(i2);
//
//        insertions.addAll(i2);
//        insertions.removeAll(i1);
//
//        combined.addAll(i1);
//        combined.retainAll(i2);
//
//        all.addAll(i1);
//        all.addAll(i2);
    public boolean checkSameFrame(int firstexon, int secondexon) {
        if (p1.getExon(firstexon).getCDS().getFrame() == p2.getExon(secondexon).getCDS().getFrame()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkSameFrame(Exon firstexon, Exon secondexon) {
        if (firstexon.getCDS().getFrame() == secondexon.getCDS().getFrame()) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkCut(HashSet<Long> firstexon, HashSet<Long> secondexon) {
        TreeSet<Long> tmp1 = new TreeSet<Long>(firstexon);
        TreeSet<Long> tmp2 = new TreeSet<Long>(secondexon);

        long start = Math.max(tmp1.first(), tmp2.first());
        long stop = Math.min(tmp1.last(), tmp2.last());

        if (stop - start > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkCut(Exon firstexon, Exon secondexon) {
        long start = Math.max(firstexon.getCDS().getStart(), secondexon.getCDS().getStart());
        long stop = Math.min(firstexon.getCDS().getStop(), secondexon.getCDS().getStop());

        if (stop > start) {
            return true;
        } else {
            return false;
        }
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
    //deprecated
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
//    public void processToAAseq() {
//        int counter = 0;
//        TreeSet<Long> tree = new TreeSet<>(i1);
//
//        for (Long pos : tree) {
//            if (counter % 3 == 0) {
//                i1_aa.add(pos);
//            }
//            counter++;
//        }
//
//        counter = 0;
//        tree = new TreeSet<>(i2);
//
//        for (Long pos : tree) {
//            if (counter % 3 == 0) {
//                i2_aa.add(pos);
//            }
//            counter++;
//        }
//    }
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

//        for (Long pos : output2) {
//            if (i1_aa.contains(pos) && i2_aa.contains(pos)) {
//                aa_output.append("*");
//            } else if (i1_aa.contains(pos)) {
//                aa_output.append("D");
//            } else if (i2_aa.contains(pos)) {
//                aa_output.append("I");
//            } else {
//                aa_output.append(" ");
//            }
//            aa_output.append("  ");
//        }
        return dna_output.toString();
    }
}
