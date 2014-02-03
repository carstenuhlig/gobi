package data;

import com.sun.jndi.url.iiopname.iiopnameURLContextFactory;

public class CDS {

    long start, stop;
    int frame;
    String seq;
    String strand;

    //TODO int durch long ersetzten für genes.addGene etc.
    public CDS(int start, int end) {
        this.start = start;
        this.stop = end;
    }

    public CDS(long start, long end) {
        this.start = start;
        this.stop = end;
    }

    public CDS(int start, int end, String strand) {
        this.start = start;
        this.stop = end;
        this.strand = strand;
    }

    public CDS(int start, int end, String strand, int frame) {
        this.start = start;
        this.stop = end;
        this.strand = strand;
        this.frame = frame;
    }

    public int getFrame() {
        return frame;
    }

    public String getStrand() {
        return strand;
    }

    public CDS(String strand) {
        this.strand = strand;
    }

    public long getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public long getStop() {
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

    public void setSeq(String seq) {
        this.seq = seq;
    }

    public void setStrand(String strand) {
        this.strand = strand;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Start: ");
        sb.append(start);
        sb.append(" | Ende: ");
        sb.append(stop);
        sb.append(" | Strand: ");
        sb.append(strand);
        sb.append(" | Frame: ");
        sb.append(frame);

        if (seq != null) {
            sb.append("\n");
            sb.append("Sequenz = ");
            sb.append(seq);
        }
        sb.append("\n");

        return sb.toString(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean equals(Object obj) {
        CDS tmp = (CDS) obj;
        if (start == tmp.getStart()) {
            if (stop == tmp.getStop()) {
                if (frame == tmp.getFrame()) {
                    if (strand.equals(tmp.getStrand())) {
                        if (seq == null && tmp.getSeq() == null)
                            return true;
                        else if (seq.equals(tmp.getSeq()))
                            return true;
                    }
                }
            }
        }
        return false;
    }
}
