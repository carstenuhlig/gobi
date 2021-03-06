/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import data.Alignment;
import data.Database;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kabsch.Kabsch;

import static java.nio.file.Files.newBufferedWriter;

/**
 * @author uhligc
 */
public class IO {

    //HashMaps für Übersetzung der 3 letter codes von Aminosäuren
    final static HashMap<String, Character> lts = new HashMap<String, Character>(); // abk für: longToShort

    static {
        lts.put("ALA", 'A');
        lts.put("ARG", 'R');
        lts.put("ASN", 'N');
        lts.put("ASP", 'D');
        lts.put("CYS", 'C');
        lts.put("GLN", 'Q');
        lts.put("GLU", 'E');
        lts.put("GLY", 'G');
        lts.put("HIS", 'H');
        lts.put("ILE", 'I');
        lts.put("LEU", 'L');
        lts.put("LYS", 'K');
        lts.put("MET", 'M');
        lts.put("PHE", 'F');
        lts.put("PRO", 'P');
        lts.put("SER", 'S');
        lts.put("THR", 'T');
        lts.put("TRP", 'W');
        lts.put("TYR", 'Y');
        lts.put("VAL", 'V');
    }

    final static String path_to_pdbcathfiles = "/home/proj/biosoft/PROTEINS/CATHSCOP/STRUCTURES/";
    final static String path_to_pdbfiles = "/home/proj/biosoft/PROTEINS/PDB_REP_CHAINS/STRUCTURES/";
    final static String path_to_tmalign = "/home/proj/biosoft/PROTEINS/software/TMalign";
    final static HashMap<Character, String> stl = new HashMap<Character, String>(); //abk für shortToLong

    static {
        stl.put('A', "ALA");
        stl.put('R', "ARG");
        stl.put('N', "ASN");
        stl.put('D', "ASP");
        stl.put('C', "CYS");
        stl.put('Q', "GLN");
        stl.put('E', "GLU");
        stl.put('G', "GLY");
        stl.put('H', "HIS");
        stl.put('I', "ILE");
        stl.put('L', "LEU");
        stl.put('K', "LYS");
        stl.put('M', "MET");
        stl.put('F', "PHE");
        stl.put('P', "PRO");
        stl.put('S', "SER");
        stl.put('T', "THR");
        stl.put('W', "TRP");
        stl.put('Y', "TYR");
        stl.put('V', "VAL");
    }

    final static FileSystem FS = FileSystems.getDefault();

    public static DenseDoubleMatrix2D readSampleFile(String stringpath) throws IOException {
        Path p = Paths.get(stringpath);

        List<String> strings = Files.readAllLines(p, Charset.defaultCharset());
        double[][] result = new double[strings.size()][3];

        String regex = "^(\\S+)\\s+(\\S+)\\s+(\\S+)\\s+(\\S+).*";

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher("");

        for (String str : strings) {
            if (matcher.reset(str).matches()) {
                int pos = Integer.parseInt(matcher.replaceFirst("$1"));
                double x1 = Double.parseDouble(matcher.replaceAll("$2"));
                double x2 = Double.parseDouble(matcher.replaceAll("$3"));
                double x3 = Double.parseDouble(matcher.replaceAll("$4"));

                result[pos][0] = x1;
                result[pos][1] = x2;
                result[pos][2] = x3;
            }
        }
        DenseDoubleMatrix2D resultmatrix = new DenseDoubleMatrix2D(result);
        resultmatrix.trimToSize();
        return resultmatrix;
    }

    public static void readPDBFile(String stringpath, String pdbid, Database d) {
        Path path = FS.getPath(stringpath);
        String along, xx, yy, zz, nr;
        char ashort;
        double x, y, z;
        int i, max = 0;
        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            StringBuilder aa = new StringBuilder();
            double[][] result = new double[lines.size() / 4][3]; //durch 4 da min 4 atomarten gibt (glycin)
            for (String line : lines) {
                if (line.length() > 3) { //damit keine Exception geworfen wird mit stringindex out of range
                    if (line.substring(0, 4).equals("ATOM")) {
                        if (line.substring(13, 15).equals("CA")) {
                            //einlesen nach content format pdf files v3.3
                            along = line.substring(17, 20);
                            xx = line.substring(30, 38).replace(" ", "");
                            yy = line.substring(38, 46).replace(" ", "");
                            zz = line.substring(46, Math.min(55, line.length())).replace(" ", "");
                            nr = line.substring(23, 26).replace(" ", "");

                            //parse und valueof
                            x = Double.parseDouble(xx);
                            y = Double.parseDouble(yy);
                            z = Double.parseDouble(zz);
                            i = Integer.parseInt(nr);

                            //aminosäure konvertieren
                            ashort = lts.get(along);

                            //einspeichern
                            //rotationmatrix
                            result[i][0] = x;
                            result[i][1] = y;
                            result[i][2] = z;

                            //aminosequence
                            aa.append(ashort);
                            max++;
                        }
                    }
                }
            }
            //trimtosize doublematrix
            DenseDoubleMatrix2D resultmatrix = new DenseDoubleMatrix2D(result);
            resultmatrix = (DenseDoubleMatrix2D) resultmatrix.viewPart(0, 0, max, 3);
            resultmatrix.trimToSize();

            //TODO überprüfung ob wirklich fortlaufende residues in pdbfile sonst inkosistente daten
            //save to database
            d.addMatrix(pdbid, resultmatrix);
            d.addSequence(pdbid, aa.toString());
        } catch (IOException ex) {
            Logger.getLogger(IO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void readPDBFile(String pdbid, Database d) {
        Path path = FS.getPath(path_to_pdbcathfiles + pdbid + ".pdb");
        String along, xx, yy, zz, nr;
        char ashort;
        double x, y, z;
        int i, max = 0;
        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            StringBuilder aa = new StringBuilder();
            double[][] result = new double[lines.size() / 4][3]; //durch 4 da min 4 atomarten gibt (glycin)
            for (String line : lines) {
                if (line.length() > 3) { //damit keine Exception geworfen wird mit stringindex out of range
                    if (line.substring(0, 4).equals("ATOM")) {
                        if (line.substring(13, 15).equals("CA")) {
                            //einlesen nach content format pdf files v3.3
                            along = line.substring(17, 20);
                            xx = line.substring(30, 38).replace(" ", "");
                            yy = line.substring(38, 46).replace(" ", "");
                            zz = line.substring(46, Math.min(55, line.length())).replace(" ", "");
                            nr = line.substring(23, 26).replace(" ", "");

                            //parse und valueof
                            x = Double.parseDouble(xx);
                            y = Double.parseDouble(yy);
                            z = Double.parseDouble(zz);
                            i = Integer.parseInt(nr);

                            //aminosäure konvertieren
                            ashort = lts.get(along);

                            //einspeichern
                            //rotationmatrix
                            result[i][0] = x;
                            result[i][1] = y;
                            result[i][2] = z;

                            //aminosequence
                            aa.append(ashort);
                            max++;
                        }
                    }
                }
            }
            //trimtosize doublematrix
            DenseDoubleMatrix2D resultmatrix = new DenseDoubleMatrix2D(result);
            resultmatrix = (DenseDoubleMatrix2D) resultmatrix.viewPart(0, 0, max, 3);
            resultmatrix.trimToSize();

            //TODO überprüfung ob wirklich fortlaufende residues in pdbfile sonst inkosistente daten
            //save to database
            d.addMatrix(pdbid, resultmatrix);
            d.addSequence(pdbid, aa.toString());
        } catch (IOException ex) {
            Logger.getLogger(IO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void readPDBFile(String pdbid, Database d, boolean is_pdbnormal_flag) {
        Path path = FS.getPath(path_to_pdbfiles + pdbid + ".pdb");
        String along, xx, yy, zz, nr;
        char ashort;
        double x, y, z;
        int i, max = 0;
        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            StringBuilder aa = new StringBuilder();
            double[][] result = new double[lines.size() / 4][3]; //durch 4 da min 4 atomarten gibt (glycin)
            for (String line : lines) {
                if (line.length() > 3) { //damit keine Exception geworfen wird mit stringindex out of range
                    if (line.substring(0, 4).equals("ATOM")) {
                        if (line.substring(13, 15).equals("CA")) {
                            //einlesen nach content format pdf files v3.3
                            along = line.substring(17, 20);
                            xx = line.substring(30, 38).replace(" ", "");
                            yy = line.substring(38, 46).replace(" ", "");
                            zz = line.substring(46, Math.min(55, line.length())).replace(" ", "");
                            nr = line.substring(23, 26).replace(" ", "");

                            //parse und valueof
                            x = Double.parseDouble(xx);
                            y = Double.parseDouble(yy);
                            z = Double.parseDouble(zz);
                            i = Integer.parseInt(nr);

                            //aminosäure konvertieren
                            ashort = lts.get(along);

                            //einspeichern
                            //rotationmatrix
                            result[i][0] = x;
                            result[i][1] = y;
                            result[i][2] = z;

                            //aminosequence
                            aa.append(ashort);
                            max++;
                        }
                    }
                }
            }
            //trimtosize doublematrix
            DenseDoubleMatrix2D resultmatrix = new DenseDoubleMatrix2D(result);
            resultmatrix = (DenseDoubleMatrix2D) resultmatrix.viewPart(0, 0, max, 3);
            resultmatrix.trimToSize();

            //TODO überprüfung ob wirklich fortlaufende residues in pdbfile sonst inkosistente daten
            //save to database
            d.addMatrix(pdbid, resultmatrix);
            d.addSequence(pdbid, aa.toString());
        } catch (IOException ex) {
            Logger.getLogger(IO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void readPDBFileWhole(String pdbid, Database d) {
        Path path = FS.getPath(path_to_pdbcathfiles + pdbid + ".pdb");
        String xx, yy, zz, nr, atom_type;
        char chain;
        double x, y, z;
        int i = 0, position, residue;
        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            double[][] result = new double[lines.size()][3]; //durch 4 da min 4 atomarten gibt (glycin)
            LinkedList<String> atom_types = new LinkedList<>();
            LinkedList<Character> chains = new LinkedList<>();
            LinkedList<Integer> positions = new LinkedList<>();
            LinkedList<Integer> residues = new LinkedList<>();
            for (String line : lines) {
                if (line.length() > 3) { //damit keine Exception geworfen wird mit stringindex out of range
                    if (line.substring(0, 4).equals("ATOM")) {
                        //einlesen nach content format pdf files v3.3
                        xx = line.substring(30, 38).replace(" ", "");
                        yy = line.substring(38, 46).replace(" ", "");
                        zz = line.substring(46, Math.min(55, line.length())).replace(" ", "");
                        nr = line.substring(23, 26).replace(" ", "");
                        atom_type = line.substring(13, 16);
                        chain = line.charAt(21);
                        position = Integer.parseInt(line.substring(6, 11).replace(" ", ""));
                        residue = Integer.parseInt(line.substring(22, 26).replace(" ", ""));

                        //parse und valueof
                        x = Double.parseDouble(xx);
                        y = Double.parseDouble(yy);
                        z = Double.parseDouble(zz);

                        //einspeichern
                        //rotationmatrix
                        result[i][0] = x;
                        result[i][1] = y;
                        result[i][2] = z;
                        atom_types.add(atom_type);
                        chains.add(chain);
                        positions.add(position);
                        residues.add(residue);

                        //aminosequence
                        i++;
                    }
                }
            }
            //trimtosize doublematrix
            DenseDoubleMatrix2D resultmatrix = new DenseDoubleMatrix2D(result);
            resultmatrix = (DenseDoubleMatrix2D) resultmatrix.viewPart(0, 0, i, 3);
            resultmatrix.trimToSize();

            //TODO überprüfung ob wirklich fortlaufende residues in pdbfile sonst inkosistente daten
            //save to database
            d.addBigMatrix(pdbid, resultmatrix);
            d.addAtomTypeList(pdbid, atom_types);
            d.addChain(pdbid, chains);
            d.addAtomPositionList(pdbid, positions);
            d.addResiduePosList(pdbid, residues);

        } catch (IOException ex) {
            Logger.getLogger(IO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void readPDBFileWhole(String pdbid, Database d, boolean is_pdb_folder_normal) {
        Path path = FS.getPath(path_to_pdbfiles + pdbid + ".pdb");
        String xx, yy, zz, nr, atom_type;
        char chain;
        double x, y, z;
        int i = 0, position, residue;
        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            double[][] result = new double[lines.size()][3]; //durch 4 da min 4 atomarten gibt (glycin)
            LinkedList<String> atom_types = new LinkedList<>();
            LinkedList<Character> chains = new LinkedList<>();
            LinkedList<Integer> positions = new LinkedList<>();
            LinkedList<Integer> residues = new LinkedList<>();
            for (String line : lines) {
                if (line.length() > 3) { //damit keine Exception geworfen wird mit stringindex out of range
                    if (line.substring(0, 4).equals("ATOM")) {
                        //einlesen nach content format pdf files v3.3
                        xx = line.substring(30, 38).replace(" ", "");
                        yy = line.substring(38, 46).replace(" ", "");
                        zz = line.substring(46, Math.min(55, line.length())).replace(" ", "");
                        nr = line.substring(23, 26).replace(" ", "");
                        atom_type = line.substring(13, 16);
                        chain = line.charAt(21);
                        position = Integer.parseInt(line.substring(6, 11).replace(" ", ""));
                        residue = Integer.parseInt(line.substring(22, 26).replace(" ", ""));

                        //parse und valueof
                        x = Double.parseDouble(xx);
                        y = Double.parseDouble(yy);
                        z = Double.parseDouble(zz);

                        //einspeichern
                        //rotationmatrix
                        result[i][0] = x;
                        result[i][1] = y;
                        result[i][2] = z;
                        atom_types.add(atom_type);
                        chains.add(chain);
                        positions.add(position);
                        residues.add(residue);

                        //aminosequence
                        i++;
                    }
                }
            }
            //trimtosize doublematrix
            DenseDoubleMatrix2D resultmatrix = new DenseDoubleMatrix2D(result);
            resultmatrix = (DenseDoubleMatrix2D) resultmatrix.viewPart(0, 0, i, 3);
            resultmatrix.trimToSize();

            //TODO überprüfung ob wirklich fortlaufende residues in pdbfile sonst inkosistente daten
            //save to database
            d.addBigMatrix(pdbid, resultmatrix);
            d.addAtomTypeList(pdbid, atom_types);
            d.addChain(pdbid, chains);
            d.addAtomPositionList(pdbid, positions);
            d.addResiduePosList(pdbid, residues);

        } catch (IOException ex) {
            Logger.getLogger(IO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void importCathScop(String stringpath, Database d) {
        Path path = FS.getPath(stringpath);
        List<String> raw = new LinkedList<>();
        List<String> pairs = new LinkedList<>();
        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            for (String line : lines) {
                String[] dang = line.split(" ");
                //wenn spalte 3 gleich 4 dann speichere paar von pdbids und die pdbids einzeln in liste
                if (dang[2].equals(dang[3])) {
                    raw.add(dang[0]);
                    raw.add(dang[1]);
                    pairs.add(dang[0] + " " + dang[1]);
                }
            }

            //liste von einzelnen pdbids wird in set umgewandelt -> unique pdbids
            Set<String> unique = new HashSet<String>(raw);
            d.setPairs(pairs);
            d.setPdbids(unique);

        } catch (IOException ex) {
            Logger.getLogger(IO.class
                    .getName()).log(Level.SEVERE, null, ex);
//            ex.printStackTrace();
        }
    }

    public static void importListOfPDBIds(Database d) {
        List<String> list = d.getPdbids();
        for (Iterator<String> it = list.iterator(); it.hasNext(); ) {
            StringBuilder sb = new StringBuilder(path_to_pdbcathfiles);
            String pdbid = it.next();
            sb.append(pdbid);
            sb.append(".pdb");
            readPDBFile(sb.toString(), pdbid, d);
        }
    }

    public static void exportToPDB(Database d, String pdbid, String stringpath) {
        Path path = FS.getPath(stringpath);
        DenseDoubleMatrix2D tmp = d.getBigMatrix(pdbid);
        int size = tmp.rows();
        LinkedList<String> atom_types = d.getAtomListBypdbid(pdbid);
        LinkedList<Character> chains = d.getChainFromPDBDID(pdbid);
        String aa = d.getSequenceByID(pdbid);
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("0.000", dfs);

        int counter = 0;
        int counterCalpha = 0;
        Iterator<String> itatom_types = atom_types.iterator();
        Iterator<Character> itchains = chains.iterator();

        StringBuilder sb = new StringBuilder();

        while (itatom_types.hasNext()) {
            String atom_type = itatom_types.next();
            char chain = itchains.next();
            sb.append("ATOM  ");
            sb.append(fixedLength(String.valueOf(counter), 5));
            sb.append(atom_type);
            sb.append(" ");
            sb.append(stl.get(aa.charAt(counterCalpha))); //AMINOACID
            sb.append(" ");
            sb.append(chain);
            sb.append(fixedLength(String.valueOf(counterCalpha), 4));
            sb.append("    ");
            sb.append(fixedLength(df.format(tmp.get(counter, 0)), 8));
            sb.append(fixedLength(df.format(tmp.get(counter, 1)), 8));
            sb.append(fixedLength(df.format(tmp.get(counter, 2)), 8));
            sb.append("\n");

            if (atom_type.equals("CA ") && counterCalpha < aa.length() - 1) {
                counterCalpha++;
            }
            counter++;
        }

        try {
            BufferedWriter bw = newBufferedWriter(path, StandardCharsets.UTF_8);
            bw.write("REMARK BLABLABLABLA ( unwichtiges Zeugs steht hier )\n");
            bw.write(sb.toString());
            bw.write("TER\n");
        } catch (IOException ex) {
            Logger.getLogger(IO.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void exportToPDB(Database d, String pdbid, String stringpath, DenseDoubleMatrix2D tmp, String pdbid_pair, int pdbidcount) {
        Path path = FS.getPath(stringpath);
        int size = tmp.rows();
        LinkedList<String> atom_types = d.getAtomListBypdbid(pdbid);
        LinkedList<Character> chains = d.getChainFromPDBDID(pdbid);
        LinkedList<Integer> positions = d.getAtomPositionList(pdbid);
        LinkedList<Integer> residues = d.getResiduePosList(pdbid);
        int[][] dbl_pos_array = d.getPositionalArray(pdbid_pair);
        int[] pos_array;
        if (pdbidcount == 1) {
            pos_array = dbl_pos_array[0];
        } else {
            pos_array = dbl_pos_array[1];
        }
        String aa = d.getSequenceByID(pdbid);
        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("0.000", dfs);

        int counter = 0;
        int counterCalpha = -1;
//        int counterReduced = -1;
        Iterator<String> itatom_types = atom_types.iterator();
        Iterator<Character> itchains = chains.iterator();

        try {
            BufferedWriter bw = newBufferedWriter(path, StandardCharsets.UTF_8);
//            bw.write("REMARK BLABLABLABLA ( unwichtiges Zeugs steht hier )\n");
            bw.write("MODEL\n");
            while (itatom_types.hasNext()) {

                String atom_type = itatom_types.next();
                char chain = itchains.next();

                StringBuilder sb = new StringBuilder();

                if (atom_type.equals("N  ") && counterCalpha < aa.length() - 1) {
                    counterCalpha++;
//                    if (counterReduced < pos_array.length) {
//                        if (pos_array[counterReduced] == counterCalpha) {
//                            counterReduced++;
//                        }
//                    }
                }
                sb.append("ATOM  ");
                sb.append(fixedLength(String.valueOf(positions.get(counter)), 5));
                sb.append("  ");
                sb.append(atom_type);
                sb.append(" ");
                sb.append(stl.get(aa.charAt(counterCalpha))); //AMINOACID
                sb.append(" ");
//                if (counterReduced < pos_array.length) {
                if (Matrix.checkContainsInteger(pos_array, counterCalpha)) {
                    sb.append('Z');
                } else {
                    sb.append(chain);
                }
//                } else {
//                    sb.append(chain);
//                }

                sb.append(fixedLength(String.valueOf(residues.get(counter)), 4));
                sb.append("    ");
                sb.append(fixedLength(df.format(tmp.get(counter, 0)), 8));
                sb.append(fixedLength(df.format(tmp.get(counter, 1)), 8));
                sb.append(fixedLength(df.format(tmp.get(counter, 2)), 8));
                sb.append("\n");

                bw.write(sb.toString());

                counter++;
            }

            bw.write("ENDMODEL\n");
            bw.close();
        } catch (IOException ex) {
            Logger.getLogger(IO.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static String fixedLength(String string, int length) {
        return String.format("%1$" + length + "s", string);
    }

    public static void exportForGotoh(String stringpathpairs, String stringpathseqlib, Database d) throws IOException {
        //init path und kram
        Path pairsp = FS.getPath(stringpathpairs);
        Path seqlibp = FS.getPath(stringpathseqlib);
        BufferedWriter pairs = new BufferedWriter(newBufferedWriter(pairsp, StandardCharsets.UTF_8));
        BufferedWriter seqlib = new BufferedWriter(newBufferedWriter(seqlibp, StandardCharsets.UTF_8));

        //Hauptteil
        LinkedList<String> liste = d.getPairs();
        HashMap<String, String> sequences = d.getSequences();

        //Pairs write
        for (Iterator<String> it = liste.iterator(); it.hasNext(); ) {
            pairs.write(it.next() + "\n");
        }
        pairs.close();

        for (Map.Entry<String, String> entry : sequences.entrySet()) {
            String pdbid = entry.getKey();
            String seq = entry.getValue();
            seqlib.write(pdbid + ":" + seq + "\n");
        }
        seqlib.close();
    }

    public static String[] importTMAlignment(Path file) throws IOException {
        List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
        boolean is_nextline = false;
        int counter = 0;
        String string1 = "";
        String string2 = "";
        for (String line : lines) {
            if (line.startsWith("(\":\"")) {
                is_nextline = true;
            } else if (is_nextline) {
                if (counter == 0) {
                    string1 = line;
                } else if (counter == 2) {
                    string2 = line;
                } else if (counter > 2)
                    break;
                counter++;
            }
        }
        return new String[]{string1, string2};
    }

    public static void importTMAlignmentFolder(String folder, String stringoutput, Database database) throws IOException {
        Path path_to_tmalignments = FS.getPath(folder);
        Path output = FS.getPath(stringoutput);
        BufferedWriter wr = Files.newBufferedWriter(output, StandardCharsets.UTF_8);


        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("#0.00000000000000000", dfs);

        // temp variables
        String filename = "";
        String pdb1, pdb2;
        double rmsd, identity, gdt;
        String[] alignments;

        //TODO nur gleiche CATHS SCOP implementieren
        //sind alle schon vorsortiert. deswegen nicht nötig. (wenn zeit noch ändern)

        try (DirectoryStream<Path> ds = Files.newDirectoryStream(path_to_tmalignments)) {
            for (Path p : ds) {
                StringBuilder sb = new StringBuilder();

                //aus filename pdbids ableiten
                filename = p.getFileName().toString();
                pdb1 = filename.split("-")[0];
                pdb2 = filename.split("-")[1];

                DenseDoubleMatrix2D[] reduced_matrices = null;

                alignments = importTMAlignment(p);
                try {
                    reduced_matrices = Matrix.processMatrices(database.getMatrix(pdb1), database.getMatrix(pdb2), alignments[0], alignments[1], database, pdb1, pdb2);
                    // Kabsch
                    Kabsch k = new Kabsch(reduced_matrices[0], reduced_matrices[1]);
                    k.main();
                    rmsd = k.getErmsd();
                    gdt = k.getGDTProtein(database.getMatrix(pdb1).rows(), database.getMatrix(pdb2).rows());
                    identity = Matrix.calcSequenceIdentity(alignments[0], alignments[1]);

                    sb.append(pdb1 + "\t" + pdb2 + "\t" + df.format(identity) + "\t" + df.format(rmsd) + "\t" + df.format(gdt) + "\n");
                    wr.write(sb.toString());
                } catch (NullPointerException e) {

                }

            }
            wr.close();
        }
    }

    public static void processAlignmentFile(String stringpathin, String stringpathout, Database d) throws IOException {
        Path pathin = FS.getPath(stringpathin);
        Path pathout = FS.getPath(stringpathout);

        BufferedReader reader = Files.newBufferedReader(pathin, StandardCharsets.UTF_8);
        BufferedWriter writer = newBufferedWriter(pathout, StandardCharsets.UTF_8);

        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("#0.00000000000000000", dfs);

        String regex = "^(\\S+)\\:\\s(\\S+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher("");

        String pdbid1 = null, pdbid2 = null, seq1 = null, seq2 = null;
        double rmsd, gdt, identity;

        writer.write("pdbid1\tpdbid2\tidentity\t\trmsd\t\t\tgdt\n");

        String line = "";
        while ((line = reader.readLine()) != null) {
            if (!line.startsWith(">")) {
                matcher.reset(line);
                if (pdbid1 == null) {
                    pdbid1 = matcher.replaceAll("$1");
                    seq1 = matcher.replaceAll("$2");
                } else {
                    pdbid2 = matcher.replaceAll("$1");
                    seq2 = matcher.replaceAll("$2");
                }
            } else {
                if (pdbid1 != null) {
                    DenseDoubleMatrix2D a = d.getMatrix(pdbid1);
                    DenseDoubleMatrix2D b = d.getMatrix(pdbid2);

                    //get reduced matrices
                    DenseDoubleMatrix2D[] reducedMatrices = Matrix.processMatrices(a, b, seq1, seq2, d, pdbid1, pdbid2);

                    Kabsch k = new Kabsch(reducedMatrices[0], reducedMatrices[1]);
                    k.main();
                    rmsd = k.getErmsd();
                    gdt = k.getGDTProtein(a.rows(), b.rows());
                    identity = Matrix.calcSequenceIdentity(seq1, seq2);

                    StringBuilder sb = new StringBuilder();
                    sb.append(pdbid1);
                    sb.append("\t");
                    sb.append(pdbid2);
                    sb.append("\t");
                    sb.append(df.format(identity));
                    sb.append("\t");
                    sb.append(df.format(rmsd));
                    sb.append("\t");
                    sb.append(df.format(gdt));
                    sb.append("\n");

                    writer.write(sb.toString());

                    pdbid1 = null;
                }
            }
        }
        reader.close();
        writer.close();
    }

    public static Alignment readManualTMalignment(String pdbid1, String pdbid2) {
        String fileoutput = "temp_tmalign";
        String one = null, two = null;
        String command = path_to_tmalign + " " + path_to_pdbfiles + pdbid1 + ".pdb " + path_to_pdbfiles + pdbid2 + ".pdb";
        String output = ExecuteShellCommand.executeCommand(command);
        String[] outputinlines = output.split("\n");
        int counter = -1;
        double tmscore = 0.;

        for (String line : outputinlines) {
            if (line.contains("TM-score=")) {
                //tmscore = Double.parseDouble(line.replaceAll("^.+TM-score=(\\S+).*$", "$1"));
                tmscore = Double.parseDouble(line.replaceAll("^.+TM-score=(\\d+\\.\\d+).*$", "$1"));
            }
            if (line.startsWith("(\"") || counter > -1) {
                if (counter == 0)
                    one = line;
                else if (counter == 2)
                    two = line;
                counter++;
                if (counter > 2)
                    break;
            }
        }

        Alignment tmp = null;
        if (one != null && two != null) {
            tmp = new Alignment(one, two, pdbid1, pdbid2, tmscore);
        } else
            return null;

        //clean
        //ExecuteShellCommand.executeCommand("rm " + fileoutput);
        return tmp;
    }

    public static List<String> readSimList(String path_to_simlist) throws IOException {
        Path simlist = FS.getPath(path_to_simlist);
        Set<String> set = new HashSet<>();
        BufferedReader r = Files.newBufferedReader(simlist, StandardCharsets.UTF_8);
        String line = "";
        String regex = "^(\\S+)\\s+(\\S+).+tmscore:\\s(\\S+).*$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher("");
        String pdb, pdb1st, pdb2nd;
        double tmscore = 0.0;
        double fstmscore = 0.;
        double scndtmscore = 0.;
        while ((line = r.readLine()) != null) {
            m.reset(line);
            pdb = m.replaceAll("$2");
            set.add(pdb);
        }
        r.close();
        return (new LinkedList<String>(set));
    }

    public static String[] readSimList(String path_to_simlist, boolean flag) throws IOException {
        Path simlist = FS.getPath(path_to_simlist);
        String[] strs = new String[2];

        BufferedReader r = Files.newBufferedReader(simlist, StandardCharsets.UTF_8);
        String line = "";
        String regex = "^(\\S+)\\s+(\\S+).+tmscore:\\s(\\S+).*$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher("");
        String pdb;
        strs[0] = "";
        strs[1] = "";
        double tmscore = 0.0;
        double fstmscore = 0.;
        double scndtmscore = 0.;
        while ((line = r.readLine()) != null) {
            m.reset(line);
            pdb = m.replaceAll("$2");
            tmscore = Double.parseDouble(m.replaceAll("$3"));
            if (m.replaceAll("$1").equals(pdb))
                continue;
            if (tmscore > fstmscore) {
                scndtmscore = fstmscore;
                strs[1] = strs[0];
                fstmscore = tmscore;
                strs[0] = pdb;
            } else if (tmscore > scndtmscore) {
                strs[1] = pdb;
                scndtmscore = tmscore;
            }
        }
        r.close();
        return strs;
    }
}
