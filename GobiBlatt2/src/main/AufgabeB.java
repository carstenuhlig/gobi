package main;

import java.io.IOException;
import java.util.LinkedList;

import util.Data;
import util.ImportFiles;

public class AufgabeB {
	public static void main(String[] args) {
		String p = args[0];
		LinkedList<Integer> tmp = new LinkedList<Integer>();
		
		try {
			tmp = ImportFiles.getGiTaxIdList(9606, p);
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Datei wurde nicht gefunden");
		}
		
		String p2 = args[1];
		try {
			Data[] data = ImportFiles.getNCBIObjectsFromGiIds(tmp, p2);
			for (Data data2 : data) {
				System.out.println(data2);
			}
		} catch (IOException e) {
			e.printStackTrace();
			System.err.println("Datei wurde nicht gefunden");
		}
	}
}
