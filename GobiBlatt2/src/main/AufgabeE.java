package main;

import java.io.IOException;

import controller.Mapping;

public class AufgabeE {
	public static void main(String[] args) {
		System.out.println("Aufgabe E");

		try {
			// args[0] ist dateipfad zu datei die ausgeben werden soll
			Mapping.makeMapping(args[0], true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
