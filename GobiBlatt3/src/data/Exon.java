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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Exon exon = (Exon) o;

        if (cds != null ? !cds.equals(exon.cds) : exon.cds != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return cds != null ? cds.hashCode() : 0;
    }
}
