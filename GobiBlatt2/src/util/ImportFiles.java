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
	
	public static LinkedList<Integer> getGiTaxIdList(int taxid, String p) throws IOException {
		LinkedList<Integer> liste = new LinkedList<Integer>();
		Path path = FS.getPath(p);
		
		String pattern = "(\\w+)\\s+(\\w+).*";
		for (String line : Files.readAllLines(path, StandardCharsets.UTF_8)) {
			liste.add(Integer.parseInt(line.replaceFirst(pattern, "$1")));
		}
		
		return liste;
	}
}
