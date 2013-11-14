package util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.LinkedList;

public class ImportFiles {
	private static final FileSystem FS = FileSystems.getDefault();

	public static LinkedList<Integer> getGiTaxIdList(int taxid, String p)
			throws IOException {
		LinkedList<Integer> liste = new LinkedList<Integer>();
		Path path = FS.getPath(p);
		int tmp = -1;

		String pattern = "(\\w+)\\s+(\\w+).*";
		for (String line : Files.readAllLines(path, StandardCharsets.UTF_8)) {
			tmp = Integer.parseInt(line.replaceFirst(pattern, "$2"));
			// PERFORMANCE unique ids... -> linkedlist unschön
			if (taxid == tmp)
				liste.add(Integer.parseInt(line.replaceFirst(pattern, "$1")));
		}

		return liste;
	}

	/**
	 * @param giIdsList
	 * @param p
	 * @return
	 * @throws IOException
	 */
	public static Data[] getNCBIObjectsFromGiIds(LinkedList<Integer> giIdsList,
			String p) throws IOException {
		Data[] data = new Data[giIdsList.size()];
		// FIXME teilweise null data objects
		Path path = FS.getPath(p);

		// TODO prüfen ob gids nur aus nummern bestehen
		// String innerpattern =
		// "^\\|(\\d+)\\|(\\w+)\\|([\\w\\.]*)\\|\\s?(.*)\\s?$";
		String innerpattern = "^\\|(\\d+)\\|(\\w+)\\|([\\w\\.]*)\\|\\s?(.*)\\s?$";
		int outercounter = -1;

		// temporary variables
		String[] singles;

		// data object
		String seq = "";
		String[] srcdatabase = null;
		String[] addition = null;
		int[] gid = null;
		String[] proteinid = null;

		for (String line : Files.readAllLines(path, StandardCharsets.UTF_8)) {
			if (line.charAt(0) == '>') {
				// save data before
				if (outercounter > -1)
					data[outercounter] = new Data(seq, srcdatabase, gid,
							proteinid, addition);
				outercounter++;

				// new object or Proteinsequence
				seq = "";
				singles = line.split(">gi");

				srcdatabase = new String[singles.length - 1];
				addition = new String[singles.length - 1];
				proteinid = new String[singles.length - 1];
				gid = new int[singles.length - 1];

				for (int i = 1; i < singles.length; i++) {
					gid[i - 1] = Integer.parseInt(singles[i].replaceFirst(
							innerpattern, "$1"));
					srcdatabase[i - 1] = singles[i].replaceFirst(innerpattern,
							"$2");
					proteinid[i - 1] = singles[i].replaceFirst(innerpattern,
							"$3");
					addition[i - 1] = singles[i].replaceFirst(innerpattern,
							"$4");
					if (proteinid[i - 1] == null)
						System.out.println();
				}
			} else {
				seq += line;
			}
		}
		return data;
	}

	public static LinkedList<BLASTPiece> getMatchObjectsFromBLAST(String pdbid) {
		final String path_to_blast = "/home/proj/biosoft/PROTEINS/PDB_REP_CHAINS/BLAST";

		//TODO glob matching caseinsensitive!!
		PathMatcher matcher = FS.getPathMatcher("glob:" + pdbid + "*.blast");

		//dataobject variables
		int round;
		String unprocessed_eval;
		double eval;
		String proteinid;
		String srcdatabase;

		LinkedList<BLASTPiece> blastdata = new LinkedList<>();


		try (DirectoryStream<Path> ds = Files.newDirectoryStream(FS
				.getPath(path_to_blast))) {

			for (Path p : ds) {
				if (matcher.matches(p.getFileName()))
				{
					//dataobject variables
					round = -1;
					eval = -1.0;
					unprocessed_eval = null;
					proteinid = null;
					srcdatabase = null;

					//pattern
					String outerpattern = "^Results from round (\\d{1,})$";
					String innerpattern = "^(\\w{2,3})\\|([\\w\\.]*)\\|.{30,}\\s{2,}\\d+\\s+(\\S+)$";

					for (String line : Files.readAllLines(p, StandardCharsets.UTF_8)) {
						if (line.matches(outerpattern)) {
							round = Integer.parseInt(line.replaceFirst(outerpattern, "$1"));
						} else if (line.matches(innerpattern))
						{
							srcdatabase = line.replaceFirst(innerpattern, "$1");
							proteinid = line.replaceFirst(innerpattern, "$2");
							unprocessed_eval = line.replaceFirst(innerpattern, "$3");
							if (unprocessed_eval.charAt(0) == 'e')
								unprocessed_eval = "1" + unprocessed_eval;
							eval = Double.parseDouble(unprocessed_eval);

							blastdata.add(new BLASTPiece(proteinid, eval, round, srcdatabase));
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return blastdata;
	}
}
