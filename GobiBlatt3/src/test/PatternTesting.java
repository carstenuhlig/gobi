package test;


public class PatternTesting {

	public static void main(String args[]) {
		String regexdelimit = "\\s+|;\\s*";
		String gtfstring = "GL000213.1      protein_coding  exon    138767  139339  .       -       .        gene_id \"ENSG00000237375\"; transcript_id \"ENST00000327822\"; exon_number \"1\"; gene_name \"BX072566.1\"; transcript_name \"BX072566.1-201\";";

		if(gtfstring.matches(regexdelimit))
			System.out.println("win");
		else
			System.out.println("loss");

		String[] newstringarray = gtfstring.split(regexdelimit);

		System.out.println();
	}
}
