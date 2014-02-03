package data;

import java.io.IOException;
import java.io.Serializable;

import util.GenomeSequenceExtractor;

public class Transcript implements Serializable {

    Protein protein;
    String chromosome;

    public Transcript(Protein protein, String chromosome) {
        this.protein = protein;
        this.chromosome = chromosome;
    }

    public Protein getProtein() {
        return protein;
    }

    public String getChromsome() {
        return chromosome;
    }

//    public void addInformation(int i, String strand) {
//        CDS temp_cds = this.getProtein().getExon(i).getCDS();
//        temp_cds.addInformation(strand);
//    }
    public void addInformation(int i, int start, int stop) {
        CDS temp_cds = this.getProtein().getExon(i).getCDS();
        temp_cds.setStart(start);
        temp_cds.setStop(stop);
    }

    public void addInformation(Exon exon) {
        protein.addExon(exon);
    }

    public boolean uniqueExons() {
        return protein.uniqueExons();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Chromosom: ");
        sb.append(chromosome);
        sb.append("\n");
        sb.append(protein);
        return sb.toString();
    }

    public String getSequenceDirectly() throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < this.getProtein().getNrExons(); i++) {
            CDS tmp = this.getProtein().getExon(i).getCDS();
            sb.append(GenomeSequenceExtractor.easySearch(this.getChromsome(), tmp.getStart(), tmp.getStop()));
        }
        return sb.toString();
    }
}
