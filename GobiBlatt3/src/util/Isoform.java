/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import data.Protein;
import data.Transcript;
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
    HashSet<Long> i2 = new HashSet<Long>();
    String aa = "";

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
            int frame = p1.getExon(i).getCDS().getFrame();
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

    @Override
    public String toString() {
        TreeSet<Long> output = new TreeSet<Long>();
        output.addAll(all);
        StringBuilder dna_output = new StringBuilder();
        StringBuilder aa_output = new StringBuilder();
        for (Long pos : output) {
            if (combined.contains(pos)) {
                dna_output.append("*");
            } else if (insertions.contains(pos)) {
                dna_output.append("I");
            } else if (deletions.contains(pos)) {
                dna_output.append("D");
            } else {
                dna_output.append(" ");
            }
        }
        return dna_output.toString() + "\n" + aa_output.toString();
    }
}
