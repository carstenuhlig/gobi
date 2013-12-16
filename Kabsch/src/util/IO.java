/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import data.Database;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

/**
 *
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

    final static String path_to_pdbfiles = "/home/proj/biosoft/PROTEINS/CATHSCOP/STRUCTURES/";

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
        Path path = FS.getPath(path_to_pdbfiles + pdbid + ".pdb");
        String xx, yy, zz, nr, atom_type;
        char chain;
        double x, y, z;
        int i = 0;
        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            double[][] result = new double[lines.size()][3]; //durch 4 da min 4 atomarten gibt (glycin)
            LinkedList<String> atom_types = new LinkedList<>();
            LinkedList<Character> chains = new LinkedList<>();
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
        for (Iterator<String> it = list.iterator(); it.hasNext();) {
            StringBuilder sb = new StringBuilder(path_to_pdbfiles);
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
            BufferedWriter bw = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
            bw.write("REMARK BLABLABLABLA ( unwichtiges Zeugs steht hier )\n");
            bw.write(sb.toString());
            bw.write("TER\n");
        } catch (IOException ex) {
            Logger.getLogger(IO.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public static void exportToPDB(Database d, String pdbid, String stringpath, DenseDoubleMatrix2D tmp) {
        Path path = FS.getPath(stringpath);
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

        try {
            BufferedWriter bw = Files.newBufferedWriter(path, StandardCharsets.UTF_8);
            bw.write("REMARK BLABLABLABLA ( unwichtiges Zeugs steht hier )\n");
            while (itatom_types.hasNext()) {
                String atom_type = itatom_types.next();
                char chain = itchains.next();
                
                StringBuilder sb = new StringBuilder();
                
                sb.append("ATOM  ");
                sb.append(fixedLength(String.valueOf(counter), 5));
                sb.append("  ");
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
                
                bw.write(sb.toString());
                if (atom_type.equals("CA ") && counterCalpha < aa.length() - 1) {
                    counterCalpha++;
                }
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
        BufferedWriter pairs = new BufferedWriter(Files.newBufferedWriter(pairsp, StandardCharsets.UTF_8));
        BufferedWriter seqlib = new BufferedWriter(Files.newBufferedWriter(seqlibp, StandardCharsets.UTF_8));

        //Hauptteil
        LinkedList<String> liste = d.getPairs();
        HashMap<String, String> sequences = d.getSequences();

        //Pairs write
        for (Iterator<String> it = liste.iterator(); it.hasNext();) {
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

    public static void processAlignmentFile(String stringpathin, String stringpathout, Database d) throws IOException {
        Path path = FS.getPath(stringpathin);
        Path pathout = FS.getPath(stringpathout);

        BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8);
        BufferedWriter writer = Files.newBufferedWriter(pathout, StandardCharsets.UTF_8);

        DecimalFormatSymbols dfs = new DecimalFormatSymbols();
        dfs.setDecimalSeparator('.');
        DecimalFormat df = new DecimalFormat("#0.00000000000000000", dfs);

        String regex = "^(\\S+)\\:\\s(\\S+)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher("");

        String pdbid1 = null, pdbid2 = null, seq1 = null, seq2 = null;
        double rmsd, gdt, identity;

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
                    DenseDoubleMatrix2D[] reducedMatrices = Matrix.processMatrices(a, b, seq1, seq2);

                    Kabsch k = new Kabsch(reducedMatrices[0], reducedMatrices[1]);
                    k.main();
                    rmsd = k.getErmsd();
                    gdt = k.getGdt();
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
}
