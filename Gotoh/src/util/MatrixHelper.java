package util;

public class MatrixHelper {
	
	//Deprecated
	public static double[][] makeMatrixSymmetric(double[][] mat) {
		double[][] mat2 = new double[mat.length][mat.length];
		for (int row = mat.length - 1; row >= 0; row--) {
			for (int col = 0; col < mat.length; col++) {
				mat[row][col] = mat[col][row];
			}
		}
		return null;
	}

	public static String matrix2DimString(double[][] mat) {
		String returnstr = "";
		for (int i = 0; i < mat.length; i++) {
			for (int j = 0; j < mat[0].length; j++) {
				returnstr += mat[i][j] + "\t";
			}
			returnstr += "\n";
		}
		return returnstr;
	}

	public static String matrix1DimString(double[] mat) {
		String returnstr = "";

		for (double d : mat) {
			returnstr += d + "\t";
		}
		return returnstr;
	}

	public static String matrix1DimString(char[] mat) {
		String returnstr = "";

		for (char c : mat) {
			returnstr += c + "\t";
		}
		return returnstr;
	}
}
