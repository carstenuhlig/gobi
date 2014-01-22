package util;

import java.util.HashMap;
import java.util.Map;

public class StringHelper {

    /**
     * Method to process a line (like import line-by-line for matrices)
     *
     * @param str   the String (line)
     * @param start usually 0 but sometimes omit directly first column (A-Z in
     *              substition matrices)
     * @return returns one-dim matrix in double values
     */
    public static double[] processStringToDoubleMatrix(String str, int start) {
        String[] strarray = str.split("\\s+");
        double[] tmp = new double[strarray.length - start];
        for (int i = start; i < strarray.length; i++) {
            tmp[i - start] = Double.parseDouble(strarray[i]);
        }
        return tmp;
    }

    public static String processDoubleArrayToString(char[] chrs) {
        StringBuilder sb = new StringBuilder();
        for (char a : chrs) {
            sb.append(a);
        }

        return sb.toString();
    }

    public static HashMap<Character, Integer> convertCharArrayToCharHashMap(char[] chars) {
        HashMap<Character, Integer> map = new HashMap<>();
        int bla = 0;
        for (char c : chars) {
            map.put(c, bla);
            bla++;
        }
        return map;
    }

    public static HashMap<Integer, Character> convertCharHashMapToIntToHashMap(HashMap<Character, Integer> charmap) {
        HashMap<Integer, Character> map = new HashMap<>();
        for (Map.Entry<Character, Integer> entry : charmap.entrySet()) {
            map.put(entry.getValue(), entry.getKey());
        }
        return map;
    }
}
