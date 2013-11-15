package util;

import java.util.Arrays;

public class Data {
	String srcdatabase;
	int gid;
	String geneid;

	@Override
	public String toString() {
		return "Data [srcdatabase=" + srcdatabase + ", gid=" + gid + "]";
	}

	public Data(String srcdatabase, int gid) {
		this.srcdatabase = srcdatabase;
		this.gid = gid;
	}

	public Data(String srcdatabase, int gid, String geneid) {
		this.srcdatabase = srcdatabase;
		this.gid = gid;
		this.geneid = geneid;
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

	public String getGeneid() {
		return geneid;
	}

	public void setGeneid(String geneid) {
		this.geneid = geneid;
	}
}
