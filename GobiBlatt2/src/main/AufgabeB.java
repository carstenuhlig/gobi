package main;

import java.io.IOException;
import java.util.HashMap;

import util.Database;
import util.ImportFiles;

public class AufgabeB {
	public static void main(String[] args) {
//		String p = "/home/u/uhligc/aufgaben_gobi/assignment2/gi_taxid_prot_testfile.dmp";
		String p = "/home/proj/biosoft/PROTEINS/NR/gi_taxid_prot.dmp";
//		if (!args[0].isEmpty())
//			p = args[0];
		HashMap<Integer,Boolean> tmp = new HashMap<Integer,Boolean>();
		System.out.println("Aufgabe B");
		try {
			tmp = ImportFiles.getGiTaxIdListViaHashMap(9606, p);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Datei wurde nicht gefunden");
		}

//		String p2 = "/home/u/uhligc/aufgaben_gobi/assignment2/nrdump_testfile.fasta";
		String p2 = "/home/proj/biosoft/PROTEINS/NR/nrdump.fasta";

		//falls doch andere Datenbank dann als zweites Argument
//		if (!args[1].isEmpty())
//			p2 = args[1];
		Database database = new Database("Human GIDs");

		try {
			ImportFiles.getNCBIObjectsFromGiIds(tmp, p2, database);
			System.out.println("finished processing");
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Datei wurde nicht gefunden");
		}

		database.printDatabase();
	}
}
