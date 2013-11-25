package data;

import java.util.HashMap;
import java.util.List;

public class Genes {
	HashMap<String,Gene> genes;

	public Genes() {
		genes = new HashMap<>();
	}

	public Gene get(String gene_id) {
		return genes.get(gene_id);
	}

	public void addGene(List<String> gene_id, List<String> transcript_id, int start, int end, int chr, String strand) {
		if (gene_id.size() == transcript_id.size()) {
			for (int i = 0; i < gene_id.size(); i++) {
				if (genes.containsKey(gene_id.get(i))) {
                                    Gene temp_gene;
                                    temp_gene = genes.get(gene_id.get(i));
                                    temp_gene.addTranscript(transcript_id.get(i));
				} else
				{
					genes.put(gene_id.get(i), new Gene());
				}
			}
		}
	}
}
