package main;

import java.io.IOException;

import data.Genes;
import util.*;

public class Main {
	public static void main(String[] args) {
		Genes g = new Genes();
		
//		String path = args[0];
		String path = "res\\homo_sapiens.chromosome.1.gtf";
		
		try {
			GTFParser.readFile(path, g);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
