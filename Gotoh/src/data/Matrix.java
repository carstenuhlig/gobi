package data;

import java.util.ArrayList;
import util.Type;

public class Matrix {
	private ArrayList<SMatrix> substitionmatrices = new ArrayList<SMatrix>();
	private ArrayList<SMatrix> matrices = new ArrayList<SMatrix>();

	public double[][][] getMatrices(String id1, String id2) {
		// return as 3 Matrices A = [0][][], D = [1][][], I = [2][][]
		for (SMatrix sm : matrices) {
			if (sm.name == id1 + ":" + id2 || sm.name == id2 + ":" + id1) {
				double[][][] bigmatrix = new double[3][sm.matA.length][sm.matA.length];
				bigmatrix[0] = sm.matA;
				bigmatrix[1] = sm.matD;
				bigmatrix[2] = sm.matI;

				return bigmatrix;
			}
		}
		return null;
	}

	// TODO boolean return if succeeded (validate no duplicate)
	public void addMatrix(String id1, String id2, double[][] matrixA,
			double[][] matrixD, double[][] matrixI, Type type, String as1,
			String as2) {
		matrices.add(new SMatrix(id1 + ":" + id2, matrixA, matrixD, matrixI,
				type, as1, as2));
	}

	public void addSubstitutionMatrix(String name, double[][] matrix) {
		substitionmatrices.add(new SMatrix(name, matrix));
	}

	// TODO gucken ob alle substitionmatrizen gleich struktur.. bzw. gleiches
	// Alphabet und Reihenfolge haben
	public double[][] getSubstitutionMatrix(String name) {
		for (SMatrix sm : substitionmatrices) {
			if (sm.name.equals(name))
				return sm.mat;
		}
		return null;
	}

	public void printAllSubstitionMatrices() {
		for (SMatrix sm : substitionmatrices) {
			System.out.println(sm.toString());
		}
	}

	public void printAllCalculatedMatrices() {
		for (SMatrix m : matrices) {
			System.out.println(m.toString());
		}
	}

	public void printSubstitutionMatrixByName(String name) {
		for (SMatrix sm : substitionmatrices) {
			if (sm.name.equals(name))
				System.out.println(sm);
		}
	}

	// jede Matrix nur 1 Typ, d.h. entweder Substitutionsmatrix, lokal
	// Alignment, global Alignment oder Freeshift Alignment
	// TODO Performance Mehrere Typen für eine Matrix
	private class SMatrix {
		public String name; // like id or hash

		// matrices in [row|I|Y][column|J|X]-format
		public double[][] mat;
		public double[][] matA;
		public double[][] matI;
		public double[][] matD;
		public String a;
		public String b;
		public Type t;

		// Strings mit Gaps
		public String calc_a;
		public String calc_b;

		public double score;

		// convmat in [index -> char]
		public char[] convmat;

		// substitionmatrix
		public SMatrix(String name, double[][] matrix) {
			this.name = name;
			this.mat = matrix;
			t = Type.SUBSTITUTIONMATRIX;
		}

		// calculated matrix
		// TODO fehlende Werte einfügen
		public SMatrix(String name, double[][] matrixA, double[][] matrixD,
				double[][] matrixI, Type type, String as1, String as2) {
			this.name = name;
			this.matA = matrixA;
			this.matD = matrixD;
			this.matI = matrixI;
			this.t = type;
			this.a = as1;
			this.b = as2;
		}

		public void setChars(char[] chr) {
			convmat = new char[chr.length];
			System.arraycopy(chr, 0, convmat, 0, chr.length);
		}

		@Override
		public String toString() {
			String returnstr = "";
			returnstr += "Name:\t" + name + "\n";
			returnstr += "Type:\t" + t + "\n";
			if (t == Type.SUBSTITUTIONMATRIX) {
				returnstr += "\t";
				for (int xtmp = 0; xtmp < mat.length; xtmp++) {
					returnstr += (xtmp + 1) + "\t";
				}
				returnstr += "\n";
				for (int y = 0; y < mat.length; y++) {
					returnstr += (y + 1) + "\t";
					for (int x = 0; x < mat.length; x++) {
						returnstr += mat[y][x] + "\t";
					}
					returnstr += "\n";
				}
			} else {
				// TODO Performance: tab vor xtmp, etc.
				//TODO Check
				returnstr += "Matrix:\tA\n\t";
				for (int xtmp = 0; xtmp < matA[0].length; xtmp++) {
					returnstr += xtmp + "\t";
				}
				returnstr += "\n";
				for (int y = 0; y < matA.length; y++) {
					returnstr += y + "\t";
					for (int x = 0; x < matA[0].length; x++) {
						returnstr += matA[y][x] + "\t";
					}
					returnstr += "\n\n";
				}

				returnstr += "Matrix:\tD\n\t";
				for (int xtmp = 0; xtmp < matD[0].length; xtmp++) {
					returnstr += xtmp + "\t";
				}
				returnstr += "\n";
				for (int y = 0; y < matD.length; y++) {
					returnstr += y + "\t";
					for (int x = 0; x < matD[0].length; x++) {
						if (matD[y][x] == -Double.MAX_VALUE)
							returnstr += "-Inf\t";
						else
							returnstr += matD[y][x] + "\t";
					}
					returnstr += "\n\n";
				}

				returnstr += "Matrix:\tI\n\t";
				for (int xtmp = 0; xtmp < matI[0].length; xtmp++) {
					returnstr += xtmp + "\t";
				}
				returnstr += "\n";
				for (int y = 0; y < matI.length; y++) {
					returnstr += y + "\t";
					for (int x = 0; x < matI[0].length; x++) {
						if (matI[y][x] == -Double.MAX_VALUE)
							returnstr += "-Inf\t";
						else
							returnstr += matI[y][x] + "\t";
					}
					returnstr += "\n\n";
				}
			}
			return returnstr;
		}
	}
}
