package util;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
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

	public static Data[] getNCBIObjectsFromGiIds(LinkedList<Integer> giIdsList,
			String p) throws IOException {
		Data[] data = new Data[giIdsList.size()];
		LinkedList<Integer> liste = new LinkedList<Integer>();
		Path path = FS.getPath(p);

		// TODO prüfen ob gids nur aus nummern bestehen
		String innerpattern = "^gi\\|(\\d+)\\|ref\\|(\\S+)\\|\\s?(.*)\\s?$";
		int outercounter = 0;

		// temporary variables
		String[] singles;
		int innercounter;

		// data object
		String seq = "";
		String[] srcdatabase = null;
		String[] addition = null;
		int[] gid = null;

		for (String line : Files.readAllLines(path, StandardCharsets.UTF_8)) {
			if (line.charAt(0) == '>') {
				//save data before
				data[innercounter] = new Data(seq, srcdatabase, gid, addition);
				
				
				//new object or Proteinsequence
				seq = "";
				singles = line.split(">");

				srcdatabase = new String[singles.length];
				addition = new String[singles.length];
				gid = new int[singles.length];

				innercounter = 0;

				for (String str : singles) {
					gid[innercounter] = Integer.parseInt(str.replaceFirst(
							innerpattern, "$1"));
					srcdatabase[innercounter] = str.replaceFirst(innerpattern, "$2");
					addition[innercounter] = str.replaceFirst(innerpattern, "$3");

					innercounter++;
				}
			} else
			{
				seq += line;
			}

		}

		// String outerpattern = "(>gi[^>]*)+";
		return null;
	}
}
