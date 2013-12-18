package data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import util.GenomeSequenceExtractor;

public class Protein {

    String seq, id;
    List<Exon> exons;

    public Protein() {
        exons = new ArrayList<>();
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
        for (Iterator<Exon> it = exons.iterator(); it.hasNext();) {
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

}
