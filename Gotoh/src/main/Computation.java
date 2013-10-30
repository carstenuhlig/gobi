package main;

import data.Matrix;
import util.Type;

public class Computation {
	// erste AS Sequenz
	// a = row
	private static String a;
	// zweite AS Sequenz
	// b = col
	private static String b;
	// Länge des AS Alphabets (in SubstitionsMatrix)
	// TODO Länge aus SubstitutionsMatrix beziehen
	private static int as_alpha_length;

	// gap_extend score (default: 12
	private static double go;
	// gap_open score (default: 1)
	private static double ge;
	// SubstitutionsMatrix
	private static double[][] smat;

	private static Type type;

	// matrix A,D,I;0,1,2 <- dyn. Programmierungs Matrix mit Matrix A,D und I
	private static double[][][] mat;

	// matrix für das Backtracken...
	// TODO Backtracking Planung
	private static double[][] backtrack;
	private static String id_a;
	private static String id_b;

	public static void init(String as1, String as2, int l, double[][] smatrix,
			double gapopen, double gapextend, Type type, String id1, String id2) {
		Computation.a = as1;
		Computation.b = as2;
		Computation.as_alpha_length = l;
		Computation.mat = new double[3][a.length()+1][b.length()+1];
		Computation.ge = gapextend;
		Computation.go = gapopen;
		Computation.smat = smatrix;
		Computation.type = type;
		Computation.id_a = id1;
		Computation.id_b = id2; 

		for (int row = 1; row < a.length()+1; row++) {
			// A0,k = g(k)
			mat[0][row][0] = calcGapScore(row);
			// Di,0 = -Inf
			mat[1][row][0] = -Double.MAX_VALUE;
		}
		for (int col = 1; col < b.length()+1; col++) {
			// Ak,0 = g(k)
			mat[0][0][col] = calcGapScore(col);
			// I0,j = -Inf
			mat[2][0][col] = -Double.MAX_VALUE;
		}
		System.out.println("finished");
	}

	public static void calcMatrices() {
		// Matrix I
		// row fängt bei 1 an col bei 0 wegen Matrix I --> Unterschied zu Matrix
		// D und A sowieso
		for (int row = 1; row < mat[2].length; row++) {
			for (int col = 1; col < mat[2][0].length; col++) {
				mat[2][row][col] = Math.max(mat[0][row - 1][col] + ge + go,
						mat[2][row - 1][col] + ge);
			}
		}
		// Matrix D
		for (int row = 1; row < mat[2].length; row++) {
			for (int col = 1; col < mat[2][0].length; col++) {
				mat[1][row][col] = Math.max(mat[0][row][col - 1] + ge + go,
						mat[1][row][col - 1] + ge);
			}
		}
		// Matrix A
		//TODO SubstitionMatrix nach char -> in int etc.
		for (int row = 1; row < mat[2].length; row++) {
			for (int col = 1; col < mat[2][0].length; col++) {
				mat[0][row][col] = Math.max(mat[0][row - 1][col - 1]
						+ getSMatrixScore(row, col),
						Math.max(mat[1][row][col], mat[2][row][col]));
			}
		}
	}
	
	
	//TODO Matrices for local alignment... braucht man das so?
	public static void calcMatricesLocal() {
		// Matrix I
		// row fängt bei 1 an col bei 0 wegen Matrix I --> Unterschied zu Matrix
		// D und A sowieso
		for (int row = 1; row < mat[2].length; row++) {
			for (int col = 1; col < mat[2][0].length; col++) {
				mat[2][row][col] = Math.max(mat[0][row - 1][col] + ge + go,
						mat[2][row - 1][col] + ge);
			}
		}
		// Matrix D
		for (int row = 1; row < mat[2].length; row++) {
			for (int col = 1; col < mat[2][0].length; col++) {
				mat[1][row][col] = Math.max(mat[0][row][col - 1] + ge + go,
						mat[1][row][col - 1] + ge);
			}
		}
		// Matrix A
		for (int row = 1; row < mat[2].length; row++) {
			for (int col = 1; col < mat[2][0].length; col++) {
				mat[0][row][col] = Math.max(mat[0][row - 1][col - 1]
						+ getSMatrixScore(row, col),
						Math.max(mat[1][row][col], mat[2][row][col]));
			}
		}
	}

	private static double getSMatrixScore(int row, int col) {
		return Computation.smat[row][col];
	}

	private static double calcGapScore(int n) {
		return go + ge * n;
	}

	public static void saveMatrices(Matrix m) {
		m.addMatrix(id_a, id_b, Computation.mat[0], Computation.mat[1],
				Computation.mat[2], Computation.type, Computation.a,
				Computation.b);
	}
}
