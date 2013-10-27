package test;

import static org.junit.Assert.*;
import data.Raw;
import data.Matrix;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import util.ImportFile;

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
			assertTrue(ImportFile.readFile("res/pfile.txt", "PAIRFILE", m, r));
			assertTrue(ImportFile
					.readFile("res/sample.txt", "SEQLIBFILE", m, r));
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
	}
}
