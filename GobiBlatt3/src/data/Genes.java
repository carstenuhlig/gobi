package data;

import java.util.HashMap;
import java.util.List;

public class Genes {

    HashMap<String, Gene> genes;

    public Genes() {
        genes = new HashMap<>();
    }

    public Gene get(String gene_id) {
        return genes.get(gene_id);
    }

<<<<<<< HEAD
    public void addGene() {

    }

    public void addGene(String gene_id, String transcript_id, String seqname, String strand, int start, int end) {
        if (genes.containsKey(gene_id)) {
            Gene temp_gene = genes.get(gene_id);
            temp_gene.addTranscript(transcript_id, new Transcript(new Protein(new Exon(new CDS(start, end))), seqname, transcript_id));
        } else {
            genes.put(gene_id, new Gene(new Transcript(new Protein(new Exon(new CDS(start, end)))), seqname, transcript_id, strand));
=======
    public void addGene(List<String> gene_id, List<String> transcript_id, int start, int end, int chr, String strand) {
        if (gene_id.size() == transcript_id.size()) {
            for (int i = 0; i < gene_id.size(); i++) {
                if (genes.containsKey(gene_id.get(i))) {
                    Gene temp_gene;
                    temp_gene = genes.get(gene_id.get(i));
                    temp_gene.addTranscript(transcript_id.get(i), start, end, strand, chr);
                } else {
                    genes.put(gene_id.get(i), new Gene(transcript_id.get(i), new Transcript(new Protein(new Exon(new CDS(start, end, strand))), chr)));
                }
            }
        }
    }

    public void addGene(List<String> gene_id, List<String> transcript_id, int start, int end, int chr, String strand, boolean is_exon) {
        if (gene_id.size() == transcript_id.size()) {
            for (int i = 0; i < gene_id.size(); i++) {
                if (genes.containsKey(gene_id.get(i))) {
                    Gene temp_gene;
                    temp_gene = genes.get(gene_id.get(i));
                    temp_gene.addTranscript(transcript_id.get(i), start, end, strand, chr);
                } else {
                    genes.put(gene_id.get(i), new Gene(transcript_id.get(i), new Transcript(new Protein(new Exon(new CDS(strand), start, end)), chr)));
                }
            }
        }
    }

    public void addGene(List<String> gene_id, List<String> transcript_id, int start, int end, int chr, String strand, boolean is_exon, boolean is_start_codon) {
        if (gene_id.size() == transcript_id.size()) {
            for (int i = 0; i < gene_id.size(); i++) {
                if (genes.containsKey(gene_id.get(i))) {
                    Gene temp_gene;
                    temp_gene = genes.get(gene_id.get(i));
                    if (is_start_codon) {
                        temp_gene.addTranscript(transcript_id.get(i), start, end, strand, chr, true);
                    } else {
                        temp_gene.addTranscript(transcript_id.get(i), start, end, strand, chr, false);
                    }
                } else {
                    if (is_start_codon) {
                        genes.put(gene_id.get(i), new Gene(transcript_id.get(i), new Transcript(new Protein(new Exon(new CDS(strand), start, end), start, true), chr)));
                    } else {
                        genes.put(gene_id.get(i), new Gene(transcript_id.get(i), new Transcript(new Protein(new Exon(new CDS(strand), start, end), start, false), chr)));
                    }

                }
            }
>>>>>>> 74e87e40ab761eaf381ed6bcad1bb77fff601d7e
        }
    }
}
