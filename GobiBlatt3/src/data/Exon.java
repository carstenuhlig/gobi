package data;

public class Exon implements Comparable<Exon> {

    CDS cds;

    // int start, stop;
    // public Exon(CDS cds, int start, int stop) {
    // this.cds = cds;
    // this.start = start;
    // this.stop = stop;
    // }
    Exon(CDS cds) {
        this.cds = cds;
    }

    public CDS getCDS() {
        return this.cds;
    }

    @Override
    public String toString() {
        return cds.toString();
    }

    public void setCds(CDS cds) {
        this.cds = cds;
    }

    @Override
    public int compareTo(Exon o) {
        return (int) (this.getCDS().getStart() - o.getCDS().getStart());
    }
}
