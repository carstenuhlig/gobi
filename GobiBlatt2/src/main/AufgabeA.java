package main;
import util.ImportFiles;

import java.io.IOException;
import java.nio.*;
import java.util.*;

public class AufgabeA {
	public static void main(String[] args) {
		String p = "/home/proj/biosoft/PROTEINS/NR/gi_taxid_prot.dmp";
		System.out.println(p);
		LinkedList<Integer> tmp = new LinkedList<Integer>();

		try {
			tmp = ImportFiles.getGiTaxIdList(9606, p);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Datei wurde nicht gefunden");
		}

		for (Integer integer : tmp) {
			System.out.println(integer);
		}
	}
}
