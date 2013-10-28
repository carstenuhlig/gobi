package data;

import java.util.ArrayList;

public class Matrix {
	private ArrayList<SMatrix> substitionmatrices = new ArrayList<SMatrix>();
	private ArrayList<SMatrix> matrices = new ArrayList<SMatrix>();

	private static enum Type {
		LOCAL, GLOBAL, FREESHIFT, SUBSTITUTIONMATRIX
	};

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
		matrices.add(new SMatrix(id1 + ":" + id2,matrixA,matrixD,matrixI,Matrix.Type.valueOf(type)));
	}
	
	public void addSubstitionMatrix(String name, int[][] matrix){
		substitionmatrices.add(new SMatrix(name,matrix));
	}

	// TODO gucken ob alle substitionmatrizen gleich struktur.. bzw. gleiches
	// Alphabet und Reihenfolge haben
	public int[][] getSubstitionMatrix(String name) {
		for (SMatrix sm : substitionmatrices) {
			if (sm.name == name)
				return sm.mat;
		}
		return null;
	}

	private class SMatrix {
		public String name; // like id or hash
		
		//matrices in [row][column]-format
		public int[][] mat;
		public int[][] matA;
		public int[][] matI;
		public int[][] matD;
		public Type t;

		// substitionmatrix
		public SMatrix(String name, int[][] matrix) {
			this.name = name;
			this.mat = matrix;
			t = Matrix.Type.SUBSTITUTIONMATRIX;
		}

		// calculated
		public SMatrix(String name, int[][] matrixA, int[][] matrixD,
				int[][] matrixI, Matrix.Type type) {
			this.name = name;
			this.matA = matrixA;
			this.matD = matrixD;
			this.matI = matrixI;
			this.t = type;
		}
	}
}
