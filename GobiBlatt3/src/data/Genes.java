package data;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Genes implements Serializable {

    HashMap<String, Gene> genes;

    public Genes() {
        genes = new HashMap<>();
    }


    public Gene get(String gene_id) {
        return genes.get(gene_id);
    }

    public void addGene(String protein_id, String gene_id, String transcript_id, String seqname,
            String strand, int start, int end, int frame) {
        if (genes.containsKey(gene_id)) {
            Gene temp_gene = genes.get(gene_id);
            temp_gene.addTranscript(transcript_id, new Transcript(new Protein(protein_id,
                    new Exon(new CDS(start, end, strand, frame))), seqname));
        } else {
            genes.put(gene_id, new Gene(transcript_id, new Transcript(new Protein(protein_id, new Exon(
                    new CDS(start, end, strand, frame))), seqname)));
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Map.Entry<String, Gene> entry : genes.entrySet()) {
            String gene_id = entry.getKey();
            Gene gene = entry.getValue();
            sb.append("GeneID = ");
            sb.append(gene_id);
            sb.append("\n");
            sb.append(gene.toString());
        }

        return sb.toString();
    }

    public HashMap<String, Gene> getGenes() {
        return genes;
    }

    public int getHashMapSize() {
        return genes.size();
    }

    public Transcript getTranscript(String transcript_id) {
        for (Map.Entry<String, Gene> entry : genes.entrySet()) {
            String gene_id = entry.getKey();
            Gene gene = entry.getValue();
            Transcript temp;
            if ((temp = gene.getTranscript(transcript_id)) != null) {
                return temp;
            }
        }
        return null;
    }

    public String[] getRandomProteinIDs() {
        Random rnd = new Random();
        String[] proteins = new String[2];
        for (Map.Entry<String, Gene> geneEntry : genes.entrySet()) {
            String key = geneEntry.getKey();
            Gene g = geneEntry.getValue();

            if (g.getTranscripts().size() > 1) {
                if (rnd.nextBoolean()) {
                    proteins[0] = g.getRandomTranscript().getProtein().getId();
                    proteins[1] = g.getRandomTranscript().getProtein().getId();
                    while (proteins[0].equals(proteins[1])) {
                        proteins[1] = g.getRandomTranscript().getProtein().getId();
                    }
                }
            }
        }
        return proteins;
    }

    public boolean uniqueExons() {
        for (Gene gene : genes.values()) {
            if (!gene.uniqueExons()) {
                return false;
            }
        }
        return true;
    }

    public Protein getProtein(String protein_id) {
        for (Map.Entry<String, Gene> entry : genes.entrySet()) {
            String gene_id = entry.getKey();
            Gene gene = entry.getValue();
            Protein temp;
            HashMap<String, Transcript> transcripts = gene.getTranscripts();
            for (Map.Entry<String, Transcript> entry1 : transcripts.entrySet()) {
                String string = entry1.getKey();
                Transcript transcript = entry1.getValue();
                Protein protein = transcript.getProtein();
                String protein_id2 = protein.getId();
                if (protein_id2.equals(protein_id)) {
                    protein.setChromosome(transcript.getChromsome());
                    return protein;
                }
            }
        }
        return null;
    }


}
