package test;

import static org.junit.Assert.*;
import data.Raw;
import data.Matrix;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import util.ImportFile;
import util.Type;

public class ImportTest {

	Matrix m;
	Raw r;

	@Before
	public void init() {
		m = new Matrix();
		r = new Raw();
	}

	@Test
	public void test() {
		try {
			assertTrue(ImportFile.readFile("res/pfile.txt", Type.PAIRFILE, m, r));
			assertTrue(ImportFile
					.readFile("res/sample.txt", Type.SEQLIBFILE, m, r));
			assertTrue(ImportFile.readFile("res/matrix.txt",
					Type.SUBSTITUTIONMATRIX, m, r));
			double[][] blahdouble = m.getSubstitutionMatrix("BLOSUM50");
			
			assertTrue(ImportFile.readDir("res/matrices", m, r));
			
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("IOException bei Einlesen von Dateien");
		}

		System.out.println("pairs");
		for (int i = 0; i < r.pairs.size(); i++) {
			System.out.print("|" + r.getPair(i)[0] + "+" + r.getPair(i)[1]);
			System.out.println("|");
		}

		System.out.println();

		System.out.println("seqlib");
		for (int j = 0; j < r.sequences.size(); j++) {
			System.out.println("id = " + r.getSequenceByIndex(j)[0]
					+ " AND sequence = " + r.getSequenceByIndex(j)[1]);
		}

		// String blah =
		// "W -3 -3 -4 -5 -5 -1 -3 -3 -3 -3 -2 -3 -1  1 -4 -4 -3 15  2 -3 -5 -2 -2 -1 -5";
		// String[] new_blah = blah.split(" ");
		// for (String string : new_blah) {
		// System.out.print("|" + string + "|");
		// }
		// System.out.println();

		//TODO test toString SMatrix
		m.printAllSubstitionMatrices();
	}
}
