package data;

import java.util.HashMap;
import java.util.Map;

public class Genes {

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
}
