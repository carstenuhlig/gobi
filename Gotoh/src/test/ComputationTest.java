package test;

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

		ImportFile.readDir("res/matrices", m, r);
	}

	@Test
	public void test() {
		Computation.init("WTHA", "WTHGQA", 25,
				m.getSubstitutionMatrix("BLOSUM50"), m.getConvMat("BLOSUM50"),
				-10.0, -2.0, Type.GLOBAL, "Gobiepraesentation1", "Gobiepraesentation2");
		m.printSubstitutionMatrixByName("BLOSUM50");
		Computation.calcMatrices();
		// m.printAllSubstitionMatrices();

		System.out.println("nun großer Test");

		System.out.println();

		Computation.saveMatrices(m);

		m.printAllCalculatedMatrices();
		// test commit
	}
}
