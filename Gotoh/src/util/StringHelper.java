package util;

public class StringHelper {

	/**
	 * Method to process a line (like import line-by-line for matrices)
	 *
	 * @param str
	 *            the String (line)
	 * @param start
	 *            usually 0 but sometimes omit directly first column (A-Z in
	 *            substition matrices)
	 * @return returns one-dim matrix in double values
	 */
	public static double[] processStringToDoubleMatrix(String str, int start) {
		String[] strarray = str.split("\\s+");
		double[] tmp = new double[strarray.length-start];
		for (int i = start; i<strarray.length;i++){
			tmp[i-start] = Double.parseDouble(strarray[i]);
		}
		return tmp;
	}
}
