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
}
