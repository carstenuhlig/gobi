package main;

import java.io.IOException;

import controller.*;

public class AufgabeD {
	public static void main(String[] args) throws IOException {
		System.out.println("Aufgabe D");

		// args[0] ist dateipfad zu datei die ausgeben werden soll
		Mapping.makeMapping(args[0]);
	}
}
