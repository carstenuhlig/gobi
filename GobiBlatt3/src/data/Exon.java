package data;

public class Exon {

	CDS cds;
	int start, stop;

	public Exon(CDS cds, int start, int stop) {
		this.cds = cds;
		this.start = start;
		this.stop = stop;
	}
        
        public Exon(CDS cds) {
		this.cds = cds;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getStop() {
		return stop;
	}

	public void setStop(int stop) {
		this.stop = stop;
	}
        
        public CDS getCDS() {
            return this.cds;
        }
}
