package data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import util.GenomeSequenceExtractor;

public class Protein implements Serializable {

    String seq, id, chromosome;
    List<Exon> exons;

    public Protein() {
        exons = new ArrayList<>();
    }

    public void setChromosome(String chromosome) {
        this.chromosome = chromosome;
    }

    public String getChromosome() {
        return chromosome;
    }

    public List<Exon> getExons() {
        return exons;
    }

    public Protein(String protein_id, Exon exon) {
        exons = new ArrayList<>();
        exons.add(exon);
        this.id = protein_id;
    }

    public int getNrExons() {
        return exons.size();
    }

    public Protein(String id, String seq, Exon exon) {
        exons = new ArrayList<>();
        this.seq = seq;
        this.id = id;
        exons.add(exon);
    }

    public Exon getExon(int i) {
        return exons.get(i);
    }

    public void addExon(Exon exon) {
        exons.add(exon);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Iterator<Exon> it = exons.iterator(); it.hasNext(); ) {
            Exon exon = it.next();
            sb.append("Protein: ");
            sb.append(id);
            sb.append("\n");
            sb.append(exon.toString());
        }

        return sb.toString();
    }

    public String getId() {
        return id;
    }

    public int getLength() {
        int tmp = 0;
        for (Exon exon : exons) {
            tmp += exon.getLength();
        }
        return tmp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Protein protein = (Protein) o;

        if (!id.equals(protein.id)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    public boolean uniqueExons() {
        HashSet<Exon> set = new HashSet<>(exons);
        if (set.size() == exons.size()) {
            return true;
        } else
            return false;
    }
}
