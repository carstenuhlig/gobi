package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.LinkedList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ImportFiles {
	private static final FileSystem FS = FileSystems.getDefault();

	static String[] singles;
	static String seq;
	static String[] srcdatabase;
	static String[] addition;
	static int[] gid;
	static String[] proteinid;
	static String line;
	static String innerpattern;
	static String outerpattern;
	static String regex;

	public static LinkedList<Integer> getGiTaxIdList(int taxid, String p)
			throws IOException {
		LinkedList<Integer> liste = new LinkedList<Integer>();
		int counter = 0;
		Path path = FS.getPath(p);
		int tmp = -1;
		BufferedReader reader = Files.newBufferedReader(path,
				StandardCharsets.UTF_8);

		regex = "(\\w+)\\s+(\\w+).*";

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher("");

		line = null;
		int totalcountoflines = 73981456;
		int onepercent = (int) (totalcountoflines / 100);

		System.out.println("Processing gitaxid");
		while ((line = reader.readLine()) != null) {
			tmp = Integer.parseInt(matcher.reset(line).replaceFirst("$2"));
			// PERFORMANCE unique ids... -> linkedlist unschön
			counter++;
			if (counter % onepercent == 0)
				System.out.print(".");
			if (counter % (onepercent * 10) == 0)
				System.out.println();
			if (taxid == tmp)
				liste.add(Integer.parseInt(matcher.reset(line).replaceFirst(
						"$1")));
		}
		reader.close();
		return liste;
	}

	/**
	 * @param giIdsList
	 * @param p
	 * @return
	 * @throws IOException
	 */
	public static void getNCBIObjectsFromGiIds(LinkedList<Integer> giIdsList,
			String p, Database data) throws IOException {
		// FIXME teilweise null data objects
		Path path = FS.getPath(p);
		BufferedReader reader = Files.newBufferedReader(path,
				StandardCharsets.UTF_8);

		System.out.println("Processing NCBI_NR");

		// TODO prüfen ob gids nur aus nummern bestehen

		// pattern compile matcher
		innerpattern = "^\\|(\\d+)\\|(\\w+)\\|([\\w\\.]*)\\|\\s?(.*)\\s?$";
		Pattern pattern = Pattern.compile(innerpattern);
		Matcher matcher = pattern.matcher("");

		int outercounter = -1;

		// temporary variables
		singles = null;

		// data object
		seq = "";
		srcdatabase = null;
		addition = null;
		gid = null;
		proteinid = null;

		// bufferedreader line-by-line variable
		line = null;

		// profiling variables
		int counter = 0;
		int totalcountoflines = 56096686;
		int onepercent = (int) (totalcountoflines / 100);

		while ((line = reader.readLine()) != null) {
			counter++;
			if (line.charAt(0) == '>') {
				// save data before
				if (outercounter > -1) {
					for (int i = 0; i < srcdatabase.length; i++) {
						// PERFORMANCE delete addition
						data.addData(proteinid[i], new Data(seq,
								srcdatabase[i], gid[i]));
					}
				}
				outercounter++;

				// new object or Proteinsequence
				seq = "";
				singles = line.split(">gi");

				srcdatabase = new String[singles.length - 1];
				addition = new String[singles.length - 1];
				proteinid = new String[singles.length - 1];
				gid = new int[singles.length - 1];

				for (int i = 1; i < singles.length; i++) {
					try {
						gid[i - 1] = Integer.parseInt(matcher.reset(singles[i])
								.replaceFirst("$1"));
					} catch (NumberFormatException e) {
//						System.err.print(counter);
					}

					srcdatabase[i - 1] = matcher.reset(singles[i])
							.replaceFirst("$2");
					proteinid[i - 1] = matcher.reset(singles[i]).replaceFirst(
							"$3");
//					addition[i - 1] = matcher.reset(singles[i]).replaceFirst(
//							"$4");
					if (proteinid[i - 1] == null)
						System.err.println("proteinid: NULL");
				}
			} else {
				seq += line;
			}
			if (counter % onepercent == 0)
				System.out.print(".");
			if (counter % (onepercent * 10) == 0)
				System.out.println();
		}
		reader.close();
	}

	public static LinkedList<BLASTPiece> getMatchObjectsFromBLAST(String pdbid) {
		final String path_to_blast = "/home/proj/biosoft/PROTEINS/PDB_REP_CHAINS/BLAST";

		// TODO glob matching caseinsensitive!!
		PathMatcher matcher = FS.getPathMatcher("glob:" + pdbid + "*.blast");

		// dataobject variables
		int round;
		String unprocessed_eval;
		double eval;
		String proteinid;
		String srcdatabase;

		LinkedList<BLASTPiece> blastdata = new LinkedList<>();

		try (DirectoryStream<Path> ds = Files.newDirectoryStream(FS
				.getPath(path_to_blast))) {

			for (Path p : ds) {
				if (matcher.matches(p.getFileName())) {
					// dataobject variables
					round = -1;
					eval = -1.0;
					unprocessed_eval = null;
					proteinid = null;
					srcdatabase = null;

					// pattern
					String outerpattern = "^Results from round (\\d{1,})$";
					String innerpattern = "^(\\w{2,3})\\|([\\w\\.]*)\\|.{30,}\\s{2,}\\d+\\s+(\\S+)$";

					for (String line : Files.readAllLines(p,
							StandardCharsets.UTF_8)) {
						if (line.matches(outerpattern)) {
							round = Integer.parseInt(line.replaceFirst(
									outerpattern, "$1"));
						} else if (line.matches(innerpattern)) {
							srcdatabase = line.replaceFirst(innerpattern, "$1");
							proteinid = line.replaceFirst(innerpattern, "$2");
							unprocessed_eval = line.replaceFirst(innerpattern,
									"$4");
							if (unprocessed_eval.charAt(0) == 'e')
								unprocessed_eval = "1" + unprocessed_eval;
							eval = Double.parseDouble(unprocessed_eval);

							if (proteinid.isEmpty())
								proteinid = line.replaceFirst(innerpattern,
										"$3");

							blastdata.add(new BLASTPiece(proteinid, eval,
									round, srcdatabase));
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return blastdata;
	}

	public static LinkedList<BLASTPiece> getMatchObjectsFromBLASTFile(
			String file) throws NumberFormatException, IOException {
		final String path_to_blast = "/home/proj/biosoft/PROTEINS/PDB_REP_CHAINS/BLAST";

		Path p = FS.getPath(path_to_blast + "/" + file);

		// dataobject variables
		int round;
		String unprocessed_eval;
		double eval;
		String proteinid;
		String srcdatabase;

		LinkedList<BLASTPiece> blastdata = new LinkedList<>();

		// dataobject variables
		round = -1;
		eval = -1.0;
		unprocessed_eval = null;
		proteinid = null;
		srcdatabase = null;

		// pattern and matcher
		String outerpattern = "^Results from round (\\d{1,})$";
		Pattern opattern = Pattern.compile(outerpattern);
		String innerpattern = "^(\\w{2,3})\\|([\\w\\.]*)\\|(\\w*).{30,}\\s{2,}\\d+\\s+(\\S+)$";
		Pattern ipattern = Pattern.compile(innerpattern);
		Matcher omatcher = opattern.matcher("");
		Matcher imatcher = ipattern.matcher("");

		for (String line : Files.readAllLines(p, StandardCharsets.UTF_8)) {
			if (omatcher.reset(line).matches()) {
				round = Integer.parseInt(line.replaceFirst(outerpattern, "$1"));
			} else if (imatcher.reset(line).matches()) {
				srcdatabase = imatcher.reset(line).replaceFirst("$1");
				proteinid = imatcher.reset(line).replaceFirst("$2");
				unprocessed_eval = imatcher.reset(line).replaceFirst("$4");
				if (unprocessed_eval.charAt(0) == 'e')
					unprocessed_eval = "1" + unprocessed_eval;
				eval = Double.parseDouble(unprocessed_eval);

				if (proteinid.isEmpty())
					proteinid = imatcher.reset(line).replaceFirst("$3");

				blastdata.add(new BLASTPiece(proteinid, eval, round,
						srcdatabase));
			}
		}

		return blastdata;
	}
}