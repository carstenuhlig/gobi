package main;

import java.io.IOException;
import java.util.LinkedList;

import util.BLASTPiece;
import util.ImportFiles;

public class AufgabeC {
	public static void main(String[] args) {
		String pdbid = args[0];
		String file = args[1];

//		LinkedList<BLASTPiece> data = ImportFiles.getMatchObjectsFromBLAST(pdbid);
//
//		for (BLASTPiece blastPiece : data) {
//			System.out.println(blastPiece);
//		}

		System.out.println();
		System.out.println();
		System.out.println();

		LinkedList<BLASTPiece> data2 = null;
		try {
			data2 = ImportFiles.getMatchObjectsFromBLASTFile(file);
		} catch (NumberFormatException | IOException e) {
			e.printStackTrace();
		}

		for (BLASTPiece blastPiece : data2) {
			System.out.println(blastPiece);
		}
	}
}
