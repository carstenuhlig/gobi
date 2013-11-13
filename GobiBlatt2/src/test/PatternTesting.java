package test;

public class PatternTesting {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		String a = "gi|15674171|ref|NP_268346.1| 30S ribosomal protein S18 [Lactococcus lactis subsp. lactis Il1403] ";
		String pattern = "^gi\\|(\\d+)\\|(\\w+)\\|(\\S+)\\|\\s?(.*)\\s?$";
		String a1 = a.replaceFirst(pattern, "$1");
		String a2 = a.replaceFirst(pattern, "$2");
		String a3 = a.replaceFirst(pattern, "$3");
		String a4 = a.replaceFirst(pattern, "$4");
	}

}
