package util;

import java.io.IOException;
import java.nio.file.*;
import java.nio.charset.*;

import data.Raw;

import java.net.URISyntaxException;
import java.nio.file.attribute.BasicFileAttributes;

public class ImportFile {

    private static final FileSystem FS = FileSystems.getDefault();

    private static Path path;
    private static Type type;
    private static double[][] matrix;
    private static String name;
    private static int rows;
    private static int cols;
    // as count variable for which line in file of "MATRIX\t\d\t..."
    private static int matCnt;
    private static char[] matChrs;
    // if matrix is symmetric and other half has to be filled in

    private static boolean initialized;

    // to reset values
    private static void reset() {
        matrix = null;
        rows = 0;
        cols = 0;
        matCnt = 0;
        matChrs = null;
        initialized = false;
    }

    /**
     * ReadFile for smatrices, pairfiles and seqlibfiles.
     *
     * @param p     Path provided as String (relative to working directory or
     *              absolute)
     * @param aType Type of File that has to be imported provided as util.Type
     * @return if succeeded true
     * @throws IOException not readable File, etc.
     */
    public static boolean readFile(String p, Type aType, data.Matrix m,
                                   data.Raw r) throws IOException {
        // Variable setting
        ImportFile.path = Paths.get(p);
        ImportFile.type = aType;

        // TODO änderung wobei nur für einlesen von pairfile und seqlibfile
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
                m.addSubstitutionMatrix(ImportFile.name, ImportFile.matrix,
                        ImportFile.matChrs);
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * ReadFile for smatrices, pairfiles and seqlibfiles.
     *
     * @param p Path provided as Path Object (relative to working directory or
     * absolute)
     * @param aType Type of File that has to be imported provided as util.Type
     * @return if succeeded true
     * @throws IOException not readable File, etc.
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
        reset();
        // Variable setting
        ImportFile.path = p;
        ImportFile.type = aType;

        // ImportFile.matrix = new double[25][25];
        ImportFile.name = "";

        // main Method
        int lineCnt = 0;
        for (String line : Files.readAllLines(path, StandardCharsets.UTF_8)) {
            processLines(line, lineCnt, m, r);

            // wenn anzahl an cols bekannt matChrs init
            if (cols > 0 && !initialized) {
                matChrs = new char[rows];
                matrix = new double[rows][cols];
                initialized = true;
            }
            lineCnt++;
        }
        if (lineCnt > 2) {
            // TODO Import Chars
            if (type == Type.SUBSTITUTIONMATRIX) {
                m.addSubstitutionMatrix(ImportFile.name, ImportFile.matrix,
                        ImportFile.matChrs);
            }
            return true;
        } else {
            return false;
        }
    }

    private static void processLines(String line, int lineCnt, data.Matrix m,
                                     Raw r) {
        switch (type) {
            case PAIRFILE:
                String pattern1 = "(\\S+)(\\s+)(\\S+).*";
                // push to database
                r.addPair(line.replaceFirst(pattern1, "$1"),
                        line.replaceFirst(pattern1, "$3"));
                break;
            case SUBSTITUTIONMATRIX:
                // TODO handles more formats for substitutionmatrix-name
                String pattern2 = "(\\w+)(\\s+)(\\S+).*";
                switch (line.replaceFirst(pattern2, "$1")) {
                    case "NAME":
                        ImportFile.name = line.replaceFirst(pattern2, "$3");
                        break;
                    case "NUMROW":
                        ImportFile.rows = Integer.parseInt(line.replaceFirst(pattern2,
                                "$3"));
                        break;
                    case "NUMCOL":
                        ImportFile.cols = Integer.parseInt(line.replaceFirst(pattern2,
                                "$3"));
                        break;
                    case "ROWINDEX":
                        matChrs = line.replaceFirst(pattern2, "$3").toCharArray();
                        break;
                    case "MATRIX":
                        // System.out.println(util.MatrixHelper.matrix1DimString(matChrs));
                        double[] tmp1 = util.StringHelper.processStringToDoubleMatrix(
                                line, 1);
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
                if (!readFile(p, Type.SUBSTITUTIONMATRIX, m, r)) {
                    return false;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static String returnMatricesFolder() {
        if (System.getProperty("os.name").toLowerCase().indexOf("win") >= 0) {
            return "res\\matrices";
        } else {
            return "res/matrices";
        }
    }

    public static void readMatricesFromResources(final data.Matrix m, final data.Raw r) throws URISyntaxException, IOException {
        System.out.println(System.getProperty("os.name").toLowerCase().indexOf("win") >= 0);
        Path jarpath = Paths.get(util.ImportFile.class.getProtectionDomain().getCodeSource().getLocation().toURI());
        try (FileSystem fs = FileSystems.newFileSystem(jarpath, null)) {
            Files.walkFileTree(fs.getPath(returnMatricesFolder()), new SimpleFileVisitor<Path>() {

                @Override
                public FileVisitResult visitFile(Path t, BasicFileAttributes bfa) throws IOException {
                    boolean bla = readFile(t, Type.SUBSTITUTIONMATRIX, m, r);
                    return FileVisitResult.CONTINUE;
                }
            });
        }
    }
}
