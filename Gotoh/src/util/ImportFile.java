package util;

import java.io.IOException;
import java.nio.file.*;
import java.nio.charset.*;

import data.Raw;

public class ImportFile {
	private static final FileSystem FS = FileSystems.getDefault();
//	private static String type;
	public static enum Type { SUBSTITIONMATRICES,PAIRFILE,SEQLIBFILE }; 
	private static Path path;
	private static Type type;
	
	//TODO ImportFile
	//TODO ImportFile Testing
	
	/**
	 * ReadFile for smatrices, pairfiles and seqlibfiles.
	 * @param p Path provided as String (relative to working directory or absolute)
	 * @param t Type of File that has to be imported provided as String (will be converted to util.ImportFile.Type)
	 * @return if succeeded true
	 * @throws IOException not readable File, etc.
	 */
	public static boolean readFile(String p, String t, data.Matrix m,data.Raw r) throws IOException {
		//Variable setting
		ImportFile.path = Paths.get(p);
		ImportFile.type = Type.valueOf(t);
		
		//main Method
		int lineCnt = 1;
		for (String line : Files.readAllLines(path, StandardCharsets.UTF_8)) {
			processLines(line,lineCnt,m,r);
			lineCnt++;
		}
		if (lineCnt > 2)
			return true;
		else
			return false;
	}

	private static void processLines(String line, int lineCnt,data.Matrix m, Raw r) {
		switch (type) {
		case PAIRFILE:
			//" " as seperator for PAIRFILE
			//TODO tab as seperator for Import Pairfile
			
			//split String into 2 ints
			String[] strp = line.split(":");		
			String f = strp[0];
			String s = strp[1];
			
			//push to database
			r.addPair(f, s);
			break;
		case SUBSTITIONMATRICES:
			//TODO SubsMatrices process
			if (lineCnt > 1) {
				String[] tmp1 = line.split(" ");
				int[] tmp2 = new int[25];
				
				int counter = 0;
				int i = 1;
				while ( counter < 25) {
					if (tmp1[i] != "")
						tmp2[counter] = Integer.parseInt(tmp1[i]);
					counter++;
				}
			}
			break;
		case SEQLIBFILE:
			//TODO Valdiation of ImportData
			String[] strs = line.split(":");
			String id = strs[0];
			String seq = strs[1];
			
			//push to database
			r.addSequence(id, seq);
			break;
		}
	}
}