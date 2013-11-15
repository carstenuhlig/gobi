package util;

public class GeneDouble {
	String geneid;
	String proteinid;

	public GeneDouble(String geneid, String proteinid) {
		this.geneid = geneid;
		this.proteinid = proteinid;
	}
	@Override
	public String toString() {
		return "GeneDouble [geneid=" + geneid + ", proteinid=" + proteinid
				+ "]";
	}
}
