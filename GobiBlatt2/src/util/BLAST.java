package util;

import java.util.LinkedList;

public class BLAST {
	String pdbid;
	LinkedList<BLASTPiece> data;
	public BLAST(String pdbid, LinkedList<BLASTPiece> data) {
		this.pdbid = pdbid;
		this.data = data;
	}
	@Override
	public String toString() {
		String returnstr = "";

		for (BLASTPiece piece : data) {
			returnstr += piece.toString() + "\n";
		}

		return "BLAST [pdbid=" + pdbid + ", data=" + returnstr + "]";
	}
}
