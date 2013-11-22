package data;

import java.util.ArrayList;
import java.util.List;

public class Protein {
	String seq,id;
	List<Exon> exons;

	public Protein() {
		exons = new ArrayList<>();
	}

	public Protein(Exon exon) {
		exons = new ArrayList<>();
		exons.add(exon);
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
}
