package util;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class MatrixHelper {

	// Deprecated
	public static double[][] makeMatrixSymmetric(double[][] mat) {
		double[][] mat2 = new double[mat.length][mat.length];
		for (int row = mat.length - 1; row >= 0; row--) {
			for (int col = 0; col < mat.length; col++) {
				mat[row][col] = mat[col][row];
			}
		}
		return null;
	}

	public static boolean doubleEquality(double a, double b, double tolerance) {
		double d;
		if (a > b)
			d = a - b;
		else
			d = b - a;
		if (d < tolerance)
			return true;
		else
			return false;
	}
	
	public static boolean doubleEquality(double a, double b) {
		double d;
		double tolerance = 0.000000001;
		if (a > b)
			d = a - b;
		else
			d = b - a;
		if (d < tolerance)
			return true;
		else
			return false;
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
	
	public static String formatDecimal(double d) {
		NumberFormat df = DecimalFormat.getInstance();
		
		df.setMinimumFractionDigits(3);
		df.setMaximumFractionDigits(3);
		df.setRoundingMode(RoundingMode.HALF_EVEN);
		
		return df.format(d);
	}
	
	public static String formatDecimal(double d, int min, int max) {
		NumberFormat df = DecimalFormat.getInstance();
		
		df.setMinimumFractionDigits(min);
		df.setMaximumFractionDigits(max);
		df.setRoundingMode(RoundingMode.HALF_EVEN);
		
		return df.format(d);
	}
}
