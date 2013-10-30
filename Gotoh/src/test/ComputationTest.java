package test;

import static org.junit.Assert.fail;

import java.io.IOException;

import main.Computation;

import org.junit.Before;
import org.junit.Test;

import util.ImportFile;
import util.Type;

import data.Matrix;
import data.Raw;

public class ComputationTest {
	Matrix m;
	Raw r;

	@Before
	public void init() {
		m = new Matrix();
		r = new Raw();

		try {
			ImportFile.readFile("res/matrix.txt", Type.SUBSTITUTIONMATRIX, m, r);
		} catch (IOException e) {
			fail("error reading file in init-phase");
		}
	}

	@Test
	public void test() {
		Computation.init("WTHA", "WTHGQA", 25,
				m.getSubstitutionMatrix("BLOSUM50"), -10.0, -2.0, Type.FREESHIFT, "IDT1", "IDT2");
		m.printSubstitutionMatrixByName("BLOSUM50");
		Computation.calcMatrices();
//		m.printAllSubstitionMatrices();

		System.out.println("nun großer Test");

		System.out.println();

		Computation.saveMatrices(m);

		m.printAllCalculatedMatrices();
		//test commit
	}
}
