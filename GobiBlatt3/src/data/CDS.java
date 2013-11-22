package data;

public class CDS {
	public CDS(int start, int end) {
		this.start = start;
		this.end = end;
	}

	int start,end;
	String seq;


	public int getStart() {
		return start;
	}


	public void setStart(int start) {
		this.start = start;
	}


	public int getEnd() {
		return end;
	}


	public void setEnd(int end) {
		this.end = end;
	}


	public String getSeq() {
		return seq;
	}


	public CDS(int start, int end, String seq) {
		this.start = start;
		this.end = end;
		this.seq = seq;
	}
}
