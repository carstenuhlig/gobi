package main;

import java.io.IOException;
import java.util.LinkedList;

import util.Data;
import util.Database;
import util.ImportFiles;

public class AufgabeB {
	public static void main(String[] args) {
		String p = "/home/u/uhligc/aufgaben_gobi/assignment2/gi_taxid_prot_testfile.dmp";
		LinkedList<Integer> tmp = new LinkedList<Integer>();
		System.out.println("anfang");
		try {
			tmp = ImportFiles.getGiTaxIdList(9606, p);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Datei wurde nicht gefunden");
		}


		System.out.println("bllblblblb");
		String p2 = "/home/u/uhligc/aufgaben_gobi/assignment2/nrdump_testfile.fasta";
		Database database = new Database("Human GIDs");

		try {
			ImportFiles.getNCBIObjectsFromGiIds(tmp, p2, database);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Datei wurde nicht gefunden");
		}
	}
}
