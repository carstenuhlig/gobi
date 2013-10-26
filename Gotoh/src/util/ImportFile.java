package util;

import java.io.IOException;
import java.nio.file.*;
import java.nio.charset.*;

public class ImportFile {
	private static final FileSystem FS = FileSystems.getDefault();
//	private static String type;
	private static enum Type { SUBSTITIONMATRICES,PAIRFILE,SEQLIBFILE }; 
	private static Path path;
	private static Type type;
	
	//TODO ImportFile
	//TODO ImportFile Testing
	public ImportFile(String p, Type t) {
		this.path = FS.getPath(p);
		this.type = t;
	}
	
	public static boolean readFile() throws IOException {
		int lineCnt = 1;
		for (String line : Files.readAllLines(path, StandardCharsets.UTF_8)) {
			processLines(line,lineCnt);
			lineCnt++;
		}
		if (lineCnt > 2)
			return true;
		else
			return false;
	}

	private static void processLines(String line, int lineCnt) {
		switch (type) {
		case PAIRFILE:
			//TODO Pairfile process
			System.out.println(line + " " + lineCnt);
		case SUBSTITIONMATRICES:
			//TODO SubsMatrices process
		case SEQLIBFILE:
			//TODO SeqLibFile process
		default:
			System.out.println("Fehler");
			//TODO log Fehler
		}
	}
}