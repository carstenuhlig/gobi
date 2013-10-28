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
	 * @param end
	 *            number of values to save into the new matrix means (in
	 *            BLOSUM50 25)
	 * @return returns one-dim matrix
	 */
	public static int[] processStringToIntMatrix(String str, int start, int end) {
		String[] tmp1 = str.split(" ");
		int[] tmp2 = new int[end];

		int counter = 0;
		int i = start;
		while (counter < end) {
			if (!tmp1[i].isEmpty()) {
				tmp2[counter] = Integer.parseInt(tmp1[i]);
				counter++;
			}
			i++;
		}
		return tmp2;
	}
}