package util;

public class BLASTPiece {
	String proteinid;
	double evalue;
	int round;
	String srcdatabase;

	public BLASTPiece(String proteinid, double evalue, int round,
			String srcdatabase) {
		this.proteinid = proteinid;
		this.evalue = evalue;
		this.round = round;
		this.srcdatabase = srcdatabase;
	}

	@Override
	public String toString() {
		return "BLASTPiece [proteinid=" + proteinid + ", evalue=" + evalue
				+ ", round=" + round + ", srcdatabase=" + srcdatabase + "]";
	}

	public String getProteinid() {
		return proteinid;
	}

	public void setProteinid(String proteinid) {
		this.proteinid = proteinid;
	}

	public double getEvalue() {
		return evalue;
	}

	public void setEvalue(double evalue) {
		this.evalue = evalue;
	}

	public int getRound() {
		return round;
	}

	public void setRound(int round) {
		this.round = round;
	}

	public String getSrcdatabase() {
		return srcdatabase;
	}

	public void setSrcdatabase(String srcdatabase) {
		this.srcdatabase = srcdatabase;
	}
}
