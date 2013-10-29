package data;

import java.util.ArrayList;
import util.Type;

public class Matrix {
	private ArrayList<SMatrix> substitionmatrices = new ArrayList<SMatrix>();
	private ArrayList<SMatrix> matrices = new ArrayList<SMatrix>();

	public int[][][] getMatrices(String id1, String id2) {
		// return as 3 Matrices A = [0][][], D = [1][][], I = [2][][]
		for (SMatrix sm : matrices) {
			if (sm.name == id1 + ":" + id2 || sm.name == id2 + ":" + id1) {
				int[][][] bigmatrix = new int[3][sm.matA.length][sm.matA.length];
				bigmatrix[0] = sm.matA;
				bigmatrix[1] = sm.matD;
				bigmatrix[2] = sm.matI;

				return bigmatrix;
			}
		}

		return null;
	}

	// TODO boolean return if succeeded (validate no duplicate)
	public void addMatrix(String id1, String id2, int[][] matrixA,
			int[][] matrixD, int[][] matrixI, String type) {
		matrices.add(new SMatrix(id1 + ":" + id2, matrixA, matrixD, matrixI,
				Type.valueOf(type)));
	}

	public void addSubstitutionMatrix(String name, int[][] matrix) {
		substitionmatrices.add(new SMatrix(name, matrix));
	}

	// TODO gucken ob alle substitionmatrizen gleich struktur.. bzw. gleiches
	// Alphabet und Reihenfolge haben
	public int[][] getSubstitutionMatrix(String name) {
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

	public void printSubstitutionMatrixByName(String name) {
		for (SMatrix sm : substitionmatrices) {
			if (sm.name.equals(name))
				System.out.println(sm);
		}
	}

	private class SMatrix {
		public String name; // like id or hash

		// matrices in [row|I|Y][column|J|X]-format
		public int[][] mat;
		public int[][] matA;
		public int[][] matI;
		public int[][] matD;
		public Type t;
		// convmat in [index -> char]
		public char[] convmat;

		// substitionmatrix
		public SMatrix(String name, int[][] matrix) {
			this.name = name;
			this.mat = matrix;
			t = Type.SUBSTITUTIONMATRIX;
		}

		// calculated matrix
		public SMatrix(String name, int[][] matrixA, int[][] matrixD,
				int[][] matrixI, Type type) {
			this.name = name;
			this.matA = matrixA;
			this.matD = matrixD;
			this.matI = matrixI;
			this.t = type;
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
			} else
				returnstr += "Typ anders... //TODO Änderung toString override method für LOCAL,GLOBAL und FREESHIFT\n";
			return returnstr;
		}
	}
}
