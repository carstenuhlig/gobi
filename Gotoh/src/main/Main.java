package main;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class Main {
	//Options
	public static String seqlibfile;
	public static String pairfile;
	public static String matrixname;
	public static int gapopen;
	public static int gapextend;
	public static String mode;
	public static boolean printalignment;
	public static boolean printmatrices;
	public static boolean checkscores;
	
	
	/**
	 * Main method
	 * @param args	
	 * @throws ParseException when parsing of commandline arguments failed
	 */
	public static void main (String[] args) throws ParseException{
		//TODO
		Options opt = new Options();
		opt.addOption("seqlib", true, "<seqlibfile>");
		opt.addOption("pairs", true, "<pairfile>");
		
		CommandLineParser parser = new GnuParser();
		CommandLine cmd = parser.parse(opt,args);
		
		if (cmd.getOptionValues("seqlib") == null) {
			System.exit(1);
		} else {
			seqlibfile = cmd.getOptionValues("seqlib")[0];
		}
		
		if (cmd.getOptionValues("pairs") == null) {
			System.exit(1);
		} else {
			pairfile = cmd.getOptionValues("pairs")[0];
		}
		
		System.out.println(seqlibfile + " " + pairfile);
		
		
	}
}
