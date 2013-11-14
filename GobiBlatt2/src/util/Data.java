package util;

import java.util.Arrays;

public class Data {
	String seq;
	String srcdatabase;
	int gid;
	String addition;

	@Override
	public String toString() {
		return "Data [seq=" + seq + ", srcdatabase=" + srcdatabase + ", gid="
				+ gid + ", addition=" + addition + "]";
	}

	public Data(String seq, String srcdatabase, int gid, String addition) {
		this.seq = seq;
		this.srcdatabase = srcdatabase;
		this.gid = gid;
		this.addition = addition;
	}

	public Data(String seq, String srcdatabase, int gid) {
		this.seq = seq;
		this.srcdatabase = srcdatabase;
		this.gid = gid;
	}

	public String getSeq() {
		return seq;
	}

	public void setSeq(String seq) {
		this.seq = seq;
	}

	public String getSrcdatabase() {
		return srcdatabase;
	}

	public void setSrcdatabase(String srcdatabase) {
		this.srcdatabase = srcdatabase;
	}

	public int getGid() {
		return gid;
	}

	public void setGid(int gid) {
		this.gid = gid;
	}

	public String getAddition() {
		return addition;
	}

	public void setAddition(String addition) {
		this.addition = addition;
	}
}
