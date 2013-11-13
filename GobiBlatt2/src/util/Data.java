package util;

public class Data {
	String seq;
	String[] srcdatabase;
	int[] gid;
	String[] addition;
	
	public Data(String seq, String[] srcdatabase, int[] gid) {
		this.seq = seq;
		this.srcdatabase = srcdatabase;
		this.gid = gid;
	}

	public Data(String seq, String[] srcdatabase, int[] gid, String[] addition) {
		this.seq = seq;
		this.srcdatabase = srcdatabase;
		this.gid = gid;
		this.addition = addition;
	}
}
