package util;

import java.io.IOException;
import java.nio.file.*;
import java.nio.charset.*;

import data.Raw;

public class ImportFile {
	private static final FileSystem FS = FileSystems.getDefault();

	// private static String type;
	public static enum Type {
		SUBSTITUTIONMATRICES, PAIRFILE, SEQLIBFILE
	};

	private static Path path;
	private static Type type;
	private static int[][] matrix;
	private static String name;

	// TODO ImportFile
	// TODO ImportFile Testing

	/**
	 * ReadFile for smatrices, pairfiles and seqlibfiles.
	 * 
	 * @param p
	 *            Path provided as String (relative to working directory or
	 *            absolute)
	 * @param t
	 *            Type of File that has to be imported provided as String (will
	 *            be converted to util.ImportFile.Type)
	 * @return if succeeded true
	 * @throws IOException
	 *             not readable File, etc.
	 */
	public static boolean readFile(String p, String t, data.Matrix m, data.Raw r)
			throws IOException {
		// Variable setting
		ImportFile.path = Paths.get(p);
		ImportFile.type = Type.valueOf(t);

		ImportFile.matrix = new int[25][25];

		ImportFile.name = "";

		// main Method
		int lineCnt = 0;
		for (String line : Files.readAllLines(path, StandardCharsets.UTF_8)) {
			processLines(line, lineCnt, m, r);
			lineCnt++;
		}
		if (lineCnt > 2) {
			if (t.contains("SUB")) {
				m.addSubstitionMatrix(ImportFile.name, ImportFile.matrix);
			}
			return true;
		} else
			return false;
	}

	private static void processLines(String line, int lineCnt, data.Matrix m,
			Raw r) {
		switch (type) {
		case PAIRFILE:
			// " " as seperator for PAIRFILE
			// TODO tab as seperator for Import Pairfile

			// split String into 2 ints
			String[] strp = line.split(":");
			String f = strp[0];
			String s = strp[1];

			// push to database
			r.addPair(f, s);
			break;
		case SUBSTITUTIONMATRICES:
			// TODO handles more formats for substitutionmatrix-name
			if (lineCnt == 0)
				ImportFile.name = line.split(" ")[1];
			if (lineCnt > 1) {
				int[] tmp1 = util.StringHelper.processStringToIntMatrix(line, 1, 25);
				if (tmp1[24] != 0)
					ImportFile.matrix[lineCnt - 2] = tmp1;
			}
			break;
		case SEQLIBFILE:
			// TODO Valdiation of ImportData
			String[] strs = line.split(":");
			String id = strs[0];
			String seq = strs[1];

			// push to database
			r.addSequence(id, seq);
			break;
		}
	}
}