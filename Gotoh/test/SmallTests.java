import util.MatrixHelper;

import java.util.Arrays;

/**
 * Created by carsten on 21.01.14.
 */
public class SmallTests {
    public static void main(String[] args) {
        char[] chars = new char[]{'-', 'C', 'A'};
        int[] bla = MatrixHelper.convertCharsToInts(chars);
        for (int i : bla) System.out.println(i);
        if (Arrays.equals(MatrixHelper.convertIntsToChars(bla), chars)) System.out.println("geht");
        else System.out.println("geht nicht");
    }
}
