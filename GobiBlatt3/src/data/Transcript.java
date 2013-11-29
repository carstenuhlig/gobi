package data;

public class Transcript {

    Protein protein;
    String chromosome,strand;

    public Transcript(Protein protein, String chromosome, String strand) {
    	this.protein = protein;
    	this.chromosome = chromosome;
    	this.strand = strand;
    }

	public Protein getProtein() {
		return protein;
	}

    public Protein getChromsome() {
        return protein;
    }

    public void addInformation(int i, String strand) {
        CDS temp_cds = this.getProtein().getExon(i).getCDS();
        temp_cds.addInformation(strand);
    }

    public void addInformation(int i, int start, int stop) {
        CDS temp_cds = this.getProtein().getExon(i).getCDS();
        temp_cds.setStart(start);
        temp_cds.setStop(stop);
    }
}
