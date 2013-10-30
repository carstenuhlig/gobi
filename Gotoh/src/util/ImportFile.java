package util;

import java.io.IOException;
import util.Type;
import java.nio.file.*;
import java.nio.charset.*;

import data.Raw;

public class ImportFile {
	private static final FileSystem FS = FileSystems.getDefault();

	private static Path path;
	private static Type type;
	private static double[][] matrix;
	private static double[][] new_matrix;
	private static String name;
	private static int rows;
	private static int cols;
	//as count variable for which line in file of "MATRIX\t\d\t..."
	private static int matCnt;
	private static char[] matChrs;
	//if matrix is symmetric and other half has to be filled in
	private static boolean sym;

	// TODO ImportFile Testing

	/**
	 * ReadFile for smatrices, pairfiles and seqlibfiles.
	 * 
	 * @param p
	 *            Path provided as String (relative to working directory or
	 *            absolute)
	 * @param aType
	 *            Type of File that has to be imported provided as util.Type
	 * @return if succeeded true
	 * @throws IOException
	 *             not readable File, etc.
	 */
	public static boolean readFile(String p, Type aType, data.Matrix m,
			data.Raw r) throws IOException {
		// Variable setting
		ImportFile.path = Paths.get(p);
		ImportFile.type = aType;

		ImportFile.matrix = new double[25][25];

		ImportFile.name = "";

		// main Method
		int lineCnt = 0;
		for (String line : Files.readAllLines(path, StandardCharsets.UTF_8)) {
			processLines(line, lineCnt, m, r);
			lineCnt++;
		}
		if (lineCnt > 2) {
			// TODO Import Chars
			if (type == Type.SUBSTITUTIONMATRIX) {
				m.addSubstitutionMatrix(ImportFile.name, ImportFile.matrix);
			}
			return true;
		} else
			return false;
	}

	/**
	 * ReadFile for smatrices, pairfiles and seqlibfiles.
	 * 
	 * @param p
	 *            Path provided as Path Object (relative to working directory or
	 *            absolute)
	 * @param aType
	 *            Type of File that has to be imported provided as util.Type
	 * @return if succeeded true
	 * @throws IOException
	 *             not readable File, etc.
	 */
	/**
	 * @param p
	 * @param aType
	 * @param m
	 * @param r
	 * @return
	 * @throws IOException
	 */
	public static boolean readFile(Path p, Type aType, data.Matrix m, data.Raw r)
			throws IOException {
		// Variable setting
		ImportFile.path = p;
		ImportFile.type = aType;

		// ImportFile.matrix = new double[25][25];

		ImportFile.name = "";

		// main Method
		int lineCnt = 0;
		for (String line : Files.readAllLines(path, StandardCharsets.UTF_8)) {
			processLines(line, lineCnt, m, r);
			
			//wenn anzahl an cols bekannt matChrs init
			if (cols > 0) {
				matChrs = new char[rows];
				matrix = new double[rows][cols];
			}
			
			lineCnt++;
		}
		if (lineCnt > 2) {
			// TODO Import Chars
			if (type == Type.SUBSTITUTIONMATRIX) {
				if (sym) {
					new_matrix = util.MatrixHelper.makeMatrixSymmetric(ImportFile.matrix);
					m.addSubstitutionMatrix(ImportFile.name, new_matrix);
				}
				m.addSubstitutionMatrix(ImportFile.name, ImportFile.matrix);
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
		case SUBSTITUTIONMATRIX:
			// TODO handles more formats for substitutionmatrix-name
			String pattern = "(\\w+)(\\s+)(\\w+)";
			switch (line.replaceFirst(pattern, "$1")) {
			case "NAME":
				ImportFile.name = line.replaceFirst(pattern, "$3");
				break;
			case "NUMROW":
				ImportFile.rows = Integer.parseInt(line.replaceFirst(pattern, "$3"));
				break;
			case "NUMCOL":
				ImportFile.cols = Integer.parseInt(line.replaceFirst(pattern, "$3"));
				break;
			case "ROWINDEX":
				matChrs = line.replaceFirst(pattern, "$3").toCharArray();
				break;
			case "MATRIX":
				System.out.println(util.MatrixHelper.matrix1DimString(matChrs));
				double[]  tmp1 = util.StringHelper.processStringToDoubleMatrix(line, 1);
				if (matCnt == 0 && tmp1.length == 1)
					sym = true;
				matrix[matCnt] = tmp1;
				matCnt++;
				break;
			default:
				break;
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

	public static boolean readDir(String dir, data.Matrix m, data.Raw r) {
		try (DirectoryStream<Path> ds = Files.newDirectoryStream(FS
				.getPath(dir))) {
			for (Path p : ds) {
				System.out.println("Datei: \"" + p.getFileName()
						+ "\" wird prozessiert.");
				if (!readFile(p, Type.SUBSTITUTIONMATRIX, m, r))
					return false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return true;
	}
}