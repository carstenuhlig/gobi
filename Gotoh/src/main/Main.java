package main;

import java.io.File;
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
import java.io.BufferedWriter;
import java.nio.charset.Charset;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.Iterator;

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
    public static String outputfile;
    final static FileSystem FS = FileSystems.getDefault();
    public static BufferedWriter writer;
    public static Boolean is_timer_on;
    public static long time_start;
    private static String tmp_time;

    /**
     * Main method
     *
     * @param args
     * @throws ParseException when parsing of commandline arguments failed
     */
    public static void main(String[] args) throws ParseException {
        Options opt = new Options();
        opt.addOption("", "seqlib", true, "<seqlibfile>");
        opt.addOption("", "pairs", true, "<pairfile>");
        opt.addOption("m", true, "substitutionmatrix");
        opt.addOption("", "go", true, "gap open score (default:12)");
        opt.addOption("", "ge", true, "gap extend score (default:1)");
        opt.addOption("", "mode", true, "mode (local|global|freeshift)");
        opt.addOption("", "printali", false, "print every alignment");
        opt.addOption("", "printmatrices", false, "print all matrices");
        opt.addOption("", "check", false, "validation (checkscores)");
        opt.addOption("", "output", true, "file to write in");
        opt.addOption("", "time", false, "time");

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
        } else {
            matrixname = "dayhoff";
        }

        if (cmd.getOptionValues("go") != null) {
            gapopen = Integer.parseInt(cmd.getOptionValues("go")[0]);
        } else {
            gapopen = -12;
        }

        if (cmd.getOptionValues("ge") != null) {
            gapextend = Integer.parseInt(cmd.getOptionValues("ge")[0]);
        } else {
            gapextend = -1;
        }

        if (cmd.getOptionValues("mode") != null) {
            mode = Type.valueOf(cmd.getOptionValues("mode")[0].toUpperCase());
        } else {
            mode = Type.FREESHIFT;
        }

        if (cmd.hasOption("printali")) {
            printalignment = true;
        }

        if (cmd.hasOption("printmatrices")) {
            printmatrices = true;
        }

        if (cmd.hasOption("check")) {
            checkscores = true;
        }

        if (cmd.getOptionValues("output") != null) {
            outputfile = cmd.getOptionValues("output")[0];
        } else {
            outputfile = "";
        }

        if (cmd.hasOption("time")) {
            is_timer_on = true;
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
            int[][] smatrix = m.getSubstitutionMatrix(matrixname);
            char[] schars = m.getConvMat(matrixname);
            Computation.init(as1, as2, smatrix, schars, gapopen, gapextend,
                    mode, id1[i], id2[i], 3, m.getFactorOfSubstitionMatrix(matrixname));
            if (mode == Type.GLOBAL) {
                Computation.calcMatrices();
            } else {
                Computation.calcMatricesLocal();
            }
            Computation.saveMatrices(m);
            m.printAllCalculatedMatrices();
            m.deleteCalculatedMatrixByName(id1[i], id2[i]);
        }
    }

    public static void scoreOnePairByIds(String id1, String id2, Type modus) {
        String as1 = r.getSequenceById(id1);
        String as2 = r.getSequenceById(id2);
        int[][] smatrix = m.getSubstitutionMatrix(matrixname);
        char[] schars = m.getConvMat(matrixname);
        Computation.init(as1, as2, smatrix, schars, gapopen, gapextend, modus, id1, id2, 3, m.getFactorOfSubstitionMatrix(matrixname));
        if (mode == Type.LOCAL) {
            Computation.calcMatricesLocal();
        } else {
            Computation.calcMatrices();
        }
        Computation.saveMatrices(m);
        System.out.println(Computation.backtrack());
        // m.printAllCalculatedMatrices();
        m.deleteCalculatedMatrixByName(id1, id2);
    }

    // default mit pairfile
    public static void doMatrices() throws IOException {
        if (is_timer_on) {
            time_start = System.currentTimeMillis();
            tmp_time = "";
        }

        DecimalFormat f = new DecimalFormat("#0.00");

        int size = r.pairs.size();
        int counter = 0;
        
        if (!outputfile.isEmpty()) {
            prepareOutputFile();
        }
        
        for (Iterator<String[]> it = r.pairs.iterator(); it.hasNext();) {
            String[] ids = it.next();
        
            String as1 = r.getSequenceById(ids[0]);
            String as2 = r.getSequenceById(ids[1]);

            if (as1.length() > 8000 || as2.length() > 8000) {
                continue;
            }

            String name = ids[0] + ":" + ids[1];
            int[][] smatrix = m.getSubstitutionMatrix(matrixname);
            if (smatrix == null) {
                System.err.println("No Substition Matrix found");
                System.exit(1);
            }
            char[] schars = m.getConvMat(matrixname);

            if (as1 == null || as2 == null) {
                System.err.println("Sequenz leer.");
                continue;
            }

            Computation.init(as1, as2, smatrix, schars, gapopen, gapextend, mode, ids[0], ids[1], 3, m.getFactorOfSubstitionMatrix(matrixname));
            if (mode == Type.LOCAL || mode == Type.FREESHIFT) {
                Computation.calcMatricesLocal();
            } else {
                Computation.calcMatrices();
            }

            if (!printalignment) {
                System.out.println(ids[0]
                        + " "
                        + ids[1]
                        + " "
                        + util.MatrixHelper.formatDecimal(Computation
                                .backtrack()));
            }
            if (checkscores) {
                Computation.backtrack();
                // falls falsch durch check score fehler -> printAlignment wird
                // durchgeführt
                // PERFORMANCE Aufräumen...
                if (!Computation.checkAlignment()) {
                    Computation.saveAlignment(m);
                    m.printAlignment(name);
                    m.emptyMatrices();
                }
            } else {
                if (outputfile.isEmpty()) {
                    System.out.println(">"
                            + ids[0]
                            + " "
                            + ids[1]
                            + " "
                            + util.MatrixHelper.formatDecimal(Computation
                                    .backtrack()));
                    Computation.saveAlignment(m);
                    m.printAlignment(name);
                } else {
                    Computation.backtrack();
                    Computation.saveAlignment(m);
                    writer.write("> \n" + m.getAlignmentAsString(name));
                }
                if (!printmatrices) {
                    m.emptyMatrices();
                }
            }

            if (printmatrices && !printalignment) {
                Computation.saveMatrices(m);
                m.printAllCalculatedMatrices();
                m.emptyMatrices();
            } else if (printmatrices && printalignment) {
                m.printAllCalculatedMatrices();
            }
            // System.out.print(". ");
//            if (i % 1200 == 0 && i > 0) {
//                System.out.println(i + " von " + r.pairs.size());
//            }
            // m.deleteCalculatedMatrixByName(ids[0], ids[1]);
            if (!outputfile.isEmpty() && counter % (size / 300) == 0) {
                StringBuilder bla = new StringBuilder();
                for (int j = 0; j < tmp_time.length() + 1; j++) {
                    bla.append("\b");
                }
                System.out.print(bla.toString());
                if (is_timer_on && counter > 0) {
                    long time_now = System.currentTimeMillis();
                    long time_diff = time_now - time_start;

                    double time = (time_diff / counter) * (size - counter) / 1000.0;
                    tmp_time = (int) time / 60 + ":" + (int) (time % 60) + " min to go\t";
                }
                tmp_time += f.format(counter / (size / 100.0)) + "%";
                System.out.print(tmp_time);
                
            }
            counter++;
        }
        if (!outputfile.isEmpty()) {
            closeOutputFile();
        }
    }

    public static void prepareOutputFile() throws IOException {
        Path p = FS.getPath(outputfile);
        writer = Files.newBufferedWriter(p, Charset.defaultCharset());
    }

    public static void closeOutputFile() throws IOException {
        writer.close();
    }

    public static void writeLinesToFile(String str) throws IOException {
        writer.write(str);
    }

    public static void importFiles() throws IOException {
//        ImportFile.readDir(getCurrentFolder() + "/res/matrices", m, r);
        ImportFile.readDir("res\\matrices", m, r);
//        ImportFile.readDir("/home/proj/biocluster/praktikum/genprakt-ws13/abgaben/assignment1/uhligc/res/matrices", m, r);
        ImportFile.readFile(pairfile, Type.PAIRFILE, m, r);
        ImportFile.readFile(seqlibfile, Type.SEQLIBFILE, m, r);
    }

    public static String getCurrentFolder() {
        File f = new File(System.getProperty("java.class.path"));
        File dir = f.getAbsoluteFile().getParentFile();
        String dirString = dir.toString();
        String[] strings = dirString.split(":");
        return strings[0];
    }
}
