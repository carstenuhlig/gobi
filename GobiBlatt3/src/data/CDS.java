package data;

public class CDS {

    int start, stop;
    String seq;
    String strand;

    public CDS(int start, int end) {
        this.start = start;
        this.stop = end;
    }

    public CDS(int start, int end, String strand) {
        this.start = start;
        this.stop = end;
        this.strand = strand;
    }

    public CDS(String strand) {
        this.strand = strand;
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

    public void setStop(int end) {
        this.stop = end;
    }

    public String getSeq() {
        return seq;
    }

    public CDS(int start, int end, String strand, String seq) {
        this.start = start;
        this.stop = end;
        this.seq = seq;
        this.strand = strand;
    }
    
    public void addInformation(String strand) {
        if (this.strand.isEmpty())
            this.strand = strand;
    }
    
    public void addInformation(int start, int stop) {
        this.start = start;
        this.stop = stop;
    }

    public void setStrand(String strand) {
        this.strand = strand;
    }
}
