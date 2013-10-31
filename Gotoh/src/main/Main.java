package main;

import java.io.IOException;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import util.ImportFile;
import util.Type;

import data.Matrix;
import data.Raw;

public class Main {
	// Options
	public static String seqlibfile;
	public static String pairfile;
	public static String matrixname;
	public static int gapopen;
	public static int gapextend;
	public static Type mode;
	public static boolean printalignment;
	public static boolean printmatrices;
	public static boolean checkscores;
	public static Matrix m;
	public static Raw r;

	/**
	 * Main method
	 * 
	 * @param args
	 * @throws ParseException
	 *             when parsing of commandline arguments failed
	 */
	public static void main(String[] args) throws ParseException {
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
		} else
			matrixname = "dayhoff";

		if (cmd.getOptionValues("go") != null) {
			gapopen = Integer.parseInt(cmd.getOptionValues("go")[0]);
		} else
			gapopen = -12;

		if (cmd.getOptionValues("ge") != null) {
			gapextend = Integer.parseInt(cmd.getOptionValues("ge")[0]);
		} else
			gapextend = -1;

		if (cmd.getOptionValues("mode") != null) {
			mode = Type.valueOf(cmd.getOptionValues("mode")[0].toUpperCase());
		} else
			mode = Type.FREESHIFT;

		if (cmd.hasOption("printali")) {
			printalignment = true;
		}

		if (cmd.hasOption("printmatrices")) {
			printmatrices = true;
		}

		if (cmd.hasOption("check")) {
			checkscores = true;
		}

		try {
			init();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void init() throws IOException {
		// Daten init
		r = new Raw();
		m = new Matrix();

		// Import
		importFiles();
		doMatrices();
		// scoreOnePairByIds("1j2xA00", "1wq2B00", Type.GLOBAL);

	}

	public static void doMatrices(String[] id1, String[] id2) {

		for (int i = 0; i < id1.length; i++) {
			String as1 = r.getSequenceById(id1[i]);
			String as2 = r.getSequenceById(id2[i]);
			double[][] smatrix = m.getSubstitutionMatrix(matrixname);
			char[] schars = m.getConvMat(matrixname);
			Computation.init(as1, as2, smatrix, schars, gapopen, gapextend,
					mode, id1[i], id2[i]);
			if (mode == Type.LOCAL)
				Computation.calcMatricesLocal();
			else
				Computation.calcMatrices();
			Computation.saveMatrices(m);
			m.printAllCalculatedMatrices();
			m.deleteCalculatedMatrixByName(id1[i], id2[i]);
		}
	}

	public static void scoreOnePairByIds(String id1, String id2, Type modus) {
		String as1 = r.getSequenceById(id1);
		String as2 = r.getSequenceById(id2);
		double[][] smatrix = m.getSubstitutionMatrix(matrixname);
		char[] schars = m.getConvMat(matrixname);
		Computation.init(as1, as2, smatrix, schars, gapopen, gapextend, modus,
				id1, id2);
		if (mode == Type.LOCAL)
			Computation.calcMatricesLocal();
		else
			Computation.calcMatrices();
		Computation.saveMatrices(m);
		System.out.println(Computation.backtrack());
		// m.printAllCalculatedMatrices();
		m.deleteCalculatedMatrixByName(id1, id2);
	}

	// default mit pairfile
	public static void doMatrices() {
		for (int i = 0; i < r.pairs.size(); i++) {
			String[] ids = r.getPair(i);
			String as1 = r.getSequenceById(ids[0]);
			String as2 = r.getSequenceById(ids[1]);
			String name = ids[0] + ":" + ids[1];
			double[][] smatrix = m.getSubstitutionMatrix(matrixname);
			char[] schars = m.getConvMat(matrixname);
			Computation.init(as1, as2, smatrix, schars, gapopen, gapextend,
					mode, ids[0], ids[1]);
			if (mode == Type.LOCAL)
				Computation.calcMatricesLocal();
			else
				Computation.calcMatrices();

			if (!printalignment)
				System.out.println(ids[0] + " " + ids[1] + " "
						+ Computation.backtrack());
			else {
				System.out.println(">" + ids[0] + " " + ids[1] + " "
						+ Computation.backtrack());
				Computation.saveAlignment(m);
				m.printAlignment(name);
				if (!printmatrices)
					m.emptyMatrices();
			}
			
			if (printmatrices && !printalignment) {
				Computation.saveMatrices(m);
				m.printAllCalculatedMatrices();
				m.emptyMatrices();
			} else if (printmatrices && printalignment) {
				m.printAllCalculatedMatrices();
			}
			// System.out.print(". ");
			// if (i % 80 == 0 && i > 0)
			// System.out.println(i + " von " + r.pairs.size());
			// m.deleteCalculatedMatrixByName(ids[0], ids[1]);
		}
	}

	public static void importFiles() throws IOException {
		ImportFile.readDir("res/matrices", m, r);
		ImportFile.readFile(pairfile, Type.PAIRFILE, m, r);
		ImportFile.readFile(seqlibfile, Type.SEQLIBFILE, m, r);
	}
}
