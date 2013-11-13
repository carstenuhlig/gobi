package util;

import java.util.Arrays;

public class Data {
	String seq;
	String[] srcdatabase;
	int[] gid;
	String[] addition;
	String[] proteinid;

	public Data(String seq, String[] srcdatabase, int[] gid, String[] proteinid) {
		this.seq = seq;
		this.srcdatabase = srcdatabase;
		this.gid = gid;
		this.proteinid = proteinid;
	}

	public Data(String seq, String[] srcdatabase, int[] gid,
			String[] proteinid, String[] addition) {
		this.seq = seq;
		this.srcdatabase = srcdatabase;
		this.gid = gid;
		this.proteinid = proteinid;
		this.addition = addition;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Data [seq=" + seq + "\nsrcdatabase="
				+ Arrays.toString(srcdatabase) + "\ngid="
				+ Arrays.toString(gid) + "\naddition="
				+ Arrays.toString(addition) + "\nproteinid="
				+ Arrays.toString(proteinid) + "]";
	}
}
