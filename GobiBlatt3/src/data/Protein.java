package data;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Protein {

    String seq, id;
    List<Exon> exons;
    int start, stop;

    public Protein() {
        exons = new ArrayList<>();
    }

    public Protein(String protein_id, Exon exon) {
        exons = new ArrayList<>();
        exons.add(exon);
        this.id = protein_id;
    }

    public Protein(Exon exon, int start, boolean is_start_codon) {
        exons = new ArrayList<>();
        exons.add(exon);
        if (is_start_codon) {
            this.start = start;
        } else {
            this.stop = stop;
        }
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
