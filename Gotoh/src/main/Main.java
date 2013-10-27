package main;

import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import util.ImportFile;

import data.Matrix;
import data.Raw;

public class Main {
	// Options
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
	 * 
	 * @param args
	 * @throws ParseException
	 *             when parsing of commandline arguments failed
	 */
	public static void main(String[] args) throws ParseException {
		// TODO
		Options opt = new Options();
		opt.addOption("seqlib", true, "<seqlibfile>");
		opt.addOption("pairs", true, "<pairfile>");
		opt.addOption("m", true, "substitutionmatrix");
		opt.addOption("go", true, "gap open score (default:12)");
		opt.addOption("ge", true, "gap extend score (default:1)");
		opt.addOption("mode", true, "mode (local|global|freeshift)");
		opt.addOption("printali", false, "print every alignment");
		opt.addOption("printmatrices", false, "print all matrices");
		opt.addOption("check", false, "validation (checkscores)");

		CommandLineParser parser = new GnuParser();
		CommandLine cmd = parser.parse(opt, args);

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

		if (cmd.getOptionValues("m") != null) {
			matrixname = cmd.getOptionValues("m")[0];
		}

		if (cmd.getOptionValues("go") != null) {
			gapopen = Integer.parseInt(cmd.getOptionValues("go")[0]);
		}

		if (cmd.getOptionValues("ge") != null) {
			gapextend = Integer.parseInt(cmd.getOptionValues("ge")[0]);
		}

		if (cmd.getOptionValues("mode") != null) {
			mode = cmd.getOptionValues("mode")[0];
		}

		if (cmd.hasOption("printlali")) {
			printalignment = true;
		}

		if (cmd.hasOption("printmatrices")) {
			printmatrices = true;
		}

		if (cmd.hasOption("check")) {
			checkscores = true;
		}
	}

	public void init() throws IOException {
		// Daten init
		Raw r = new Raw();
		Matrix m = new Matrix();

		// Import
		if (!ImportFile.readFile(pairfile, "PAIRFILE", m, r))
			System.exit(1);
		if (!ImportFile.readFile(seqlibfile, "SEQLIBFILE", m, r))
			System.exit(1);
		if (!ImportFile.readFile("res/substitionmatrices.txt",
				"SUBSTITIONMATRICES", m, r))
			System.exit(1);
	}
}
