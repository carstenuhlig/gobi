package test;

public class PatternTesting {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String a = "gi|15674171|ref|NP_268346.1| 30S ribosomal protein S18 [Lactococcus lactis subsp. lactis Il1403] ";
		String patterna = "^gi\\|(\\d+)\\|(\\w+)\\|(\\S+)\\|\\s?(.*)\\s?$";
		String a1 = a.replaceFirst(patterna, "$1");
		String a2 = a.replaceFirst(patterna, "$2");
		String a3 = a.replaceFirst(patterna, "$3");
		String a4 = a.replaceFirst(patterna, "$4");

		String b = "ref|YP_001868102.1| photosystem II manganese-stabilizing protein...   322   1e-86";
		String patternb = "^(\\w{2,3})\\|([\\w\\.]*)\\|.{30,}\\s{2,}\\d+\\s+(\\S+)$";
		String b1 = b.replaceFirst(patternb, "$1");
		String b2 = b.replaceFirst(patternb, "$2");
		String b3 = b.replaceFirst(patternb, "$3");
		System.out.println(b);
		System.out.println();
		System.out.println(b1 + "||" + b2 + "||" + b3 + "||");
		System.out.println(b.matches(patternb));

		System.out.println(Double.parseDouble(b3));

		String c = "Results from round 1";
		String patternc = "^Results from round (\\d+)$";

		System.out.println(c.matches(patternc));
	}

}
