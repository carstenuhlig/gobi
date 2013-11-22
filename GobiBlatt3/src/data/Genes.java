package data;

import java.util.HashMap;

public class Genes {
	HashMap<String,Gene> genes;

	public Genes() {
		genes = new HashMap<>();
	}

	public Gene get(String gene_id) {
		return genes.get(gene_id);
	}

	public void addGene() {

	}
}
