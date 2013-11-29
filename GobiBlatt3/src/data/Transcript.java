package data;

public class Transcript {

    Protein protein;
    int chromosome;

<<<<<<< HEAD
    Transcript(Protein protein) {
        this.protein = protein;
    }

	public Protein getProtein() {
		return protein;
	}
=======
//	public Transcript(Protein protein) {
//		this.protein = protein;
//	}
    public Transcript(Protein protein, int chromosome) {
        this.protein = protein;
        this.chromosome = chromosome;
    }

    public Protein getProtein() {
        return protein;
    }

    public Protein getChromsome() {
        return protein;
    }

    public void addInformation(int chromsome) {
        this.chromosome = chromsome;
    }

    public void addInformation(int i, String strand) {
        CDS temp_cds = this.getProtein().getExon(i).getCDS();
        temp_cds.addInformation(strand);
    }

    public void addInformation(int i, int start, int stop, boolean is_exon) {
        Exon temp_exon = this.getProtein().getExon(i);
        temp_exon.setStart(start);
        temp_exon.setStop(stop);
    }
    
    public void addInformation(int i, int start, int stop) {
        CDS temp_cds = this.getProtein().getExon(i).getCDS();
        temp_cds.setStart(start);
        temp_cds.setStop(stop);
    }
>>>>>>> 74e87e40ab761eaf381ed6bcad1bb77fff601d7e
}
