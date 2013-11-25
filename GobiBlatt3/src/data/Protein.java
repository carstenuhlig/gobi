package data;

import java.util.ArrayList;
import java.util.List;

public class Protein {

    String seq, id;
    List<Exon> exons;
    int start, stop;

    public Protein() {
        exons = new ArrayList<>();
    }

    public Protein(Exon exon) {
        exons = new ArrayList<>();
        exons.add(exon);
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
}
