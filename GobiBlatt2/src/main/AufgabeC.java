package main;

import java.util.LinkedList;

import util.BLASTPiece;
import util.ImportFiles;

public class AufgabeC {
	public static void main(String[] args) {
		String pdbid = args[0];

		LinkedList<BLASTPiece> data = ImportFiles.getMatchObjectsFromBLAST(pdbid);

		for (BLASTPiece blastPiece : data) {
			System.out.println(blastPiece);
			System.out.println();
		}
	}
}
