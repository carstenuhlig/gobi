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

	public void addGene() {

	}

	public void addGene(String gene_id, String transcript_id, String seqname,
			String strand, int start, int end) {
		if (genes.containsKey(gene_id)) {
			Gene temp_gene = genes.get(gene_id);
			temp_gene.addTranscript(transcript_id, new Transcript(new Protein(
					new Exon(new CDS(start, end))), seqname, strand));
		} else {
			genes.put(gene_id, new Gene(transcript_id, new Transcript(new Protein(new Exon(
					new CDS(start, end))), seqname, strand)));
		}
	}
}
