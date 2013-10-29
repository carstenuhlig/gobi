package main;

import util.Type;

public class Computation {
	//erste AS Sequenz
	//a = row
	private static String a;
	//zweite AS Sequenz
	//b = col
	private static String b;
	//Länge des AS Alphabets (in SubstitionsMatrix)
	//TODO Länge aus SubstitutionsMatrix beziehen
	private static int as_alpha_length;

	//gap_extend score (default: 12
	private static double go;
	//gap_open score (default: 1)
	private static double ge;
	//SubstitutionsMatrix
	private static int[][] smat;

	private static Type type;

	//matrix A,D,I;0,1,2 <- dyn. Programmierungs Matrix mit Matrix A,D und I
	private static double[][][] mat;

	//matrix für das Backtracken...
	//TODO Backtracking Planung
	private static double[][] backtrack;

	public static void init(String as1, String as2, int l, int[][] smatrix, double gapopen, double gapextend, Type type){
		Computation.a = as1;
		Computation.b = as2;
		Computation.as_alpha_length = l;
		Computation.mat = new double[3][a.length()][b.length()];
		Computation.ge = gapextend;
		Computation.go = gapopen;

		//TODO smat wird nicht übergeben
		Computation.smat = smatrix;
		Computation.type = type;


		for (int row = 0; row<a.length();row++){
			//A0,k = g(k)
			mat[0][row][0] = calcGapScore(row);
			//Di,0 = -Inf
			mat[1][row][0] = Double.MIN_VALUE;
		}
		for (int col = 0; col<b.length();col++){
			//Ak,0 = g(k)
			mat[0][0][col] = calcGapScore(col);
			//I0,j = -Inf
			mat[2][0][col] = Double.MIN_VALUE;
		}
		System.out.println("finished");
	}

	private static double calcGapScore(int n) {
		return go+ge*n;
	}
}
