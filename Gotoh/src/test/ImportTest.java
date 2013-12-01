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
			assertTrue(ImportFile
					.readFile("res/sanity.pairs", Type.PAIRFILE, m, r));
			assertTrue(ImportFile.readFile("res/domains.seqlib", Type.SEQLIBFILE,
					m, r));
//			assertTrue(ImportFile.readFile("res/matrix.txt",
//					Type.SUBSTITUTIONMATRIX, m, r));
			int[][] blahdouble = m.getSubstitutionMatrix("BLOSUM50");

			assertTrue(ImportFile.readDir("res/matrices", m, r));

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("IOException bei Einlesen von Dateien");
		}
	}
	
	private void printPairs() {
		System.out.println("pairs");
		for (int i = 0; i < r.pairs.size(); i++) {
			System.out.println("|" + r.getPair(i)[0] + "\t" + r.getPair(i)[1]
					+ "|");
		}

	}
	
	private void printSeqLib() {
		System.out.println("seqlib");
		for (int j = 0; j < r.sequences.size(); j++) {
			System.out.println("id = " + r.getSequenceByIndex(j)[0]
					+ " AND sequence = " + r.getSequenceByIndex(j)[1]);
		}
	}
	
	private void printSubstitionMatrices() {
		m.printAllSubstitionMatrices();
	}
}
