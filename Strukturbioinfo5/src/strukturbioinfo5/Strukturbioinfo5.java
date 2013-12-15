/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package strukturbioinfo5;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.jet.math.Functions;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils.IO;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author uhligc
 */
public class Strukturbioinfo5 {

    final static FileSystem FS = FileSystems.getDefault();
    final static Functions F = Functions.functions;

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

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        DenseDoubleMatrix2D[] matrices = new DenseDoubleMatrix2D[4];
        matrices[0] = readPDBFile("structure1.pdb", "1", 'A');
        matrices[1] = readPDBFile("structure2.pdb", "2", 'A');
        matrices[2] = readPDBFile("structure3.pdb", "3", 'A');
        matrices[3] = readPDBFile("structure4.pdb", "4", 'B');
        ArrayList<int[][]> contactmaps = new ArrayList<>();
        
//        System.out.println(matrices[0]);
//        System.out.println(matrices[1]);
//        System.out.println(matrices[2]);
//        System.out.println(matrices[3]);
        
        contactmaps.add(createContactMap(matrices[0]));
        contactmaps.add(createContactMap(matrices[1]));
        contactmaps.add(createContactMap(matrices[2]));
        contactmaps.add(createContactMap(matrices[3]));
        
        System.out.println(contactMapToString(contactmaps.get(0)));
        System.out.println(contactMapToString(contactmaps.get(1)));
        System.out.println(contactMapToString(contactmaps.get(2)));
        System.out.println(contactMapToString(contactmaps.get(3)));
    }

    public static DenseDoubleMatrix2D readPDBFile(String stringpath, String pdbid, char chain) {
        Path path = FS.getPath(stringpath);
        String along, xx, yy, zz, nr;
        char ashort;
        double x, y, z;
        int i = 0, max = 0;
        try {
            List<String> lines = Files.readAllLines(path, StandardCharsets.UTF_8);
            StringBuilder aa = new StringBuilder();
            double[][] result = new double[lines.size() / 4][3]; //durch 4 da min 4 atomarten gibt (glycin)
            for (String line : lines) {
                if (line.length() > 3) { //damit keine Exception geworfen wird mit stringindex out of range
                    if (line.substring(0, 4).equals("ATOM")) {
                        if (line.substring(13, 15).equals("CA")) {
                            if (line.charAt(21) == chain) {
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
                                result[max][0] = x;
                                result[max][1] = y;
                                result[max][2] = z;

                                //aminosequence
                                aa.append(ashort);
                                max++;
                            }
                        }
                    }
                }
            }
            //trimtosize doublematrix
            DenseDoubleMatrix2D resultmatrix = new DenseDoubleMatrix2D(result);
            resultmatrix = (DenseDoubleMatrix2D) resultmatrix.viewPart(0, 0, max, 3);
            resultmatrix.trimToSize();
            return resultmatrix;
        } catch (IOException ex) {
            Logger.getLogger(IO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static double getDistance(DoubleMatrix1D a, DoubleMatrix1D b) {
        DoubleMatrix1D tmp = a.copy();
        tmp.assign(b, F.minus);
        return Math.sqrt(tmp.aggregate(F.plus, F.square));
    }

    public static int[][] createContactMap(DenseDoubleMatrix2D a) {
        int[][] matrix = new int[a.rows()][a.rows()];
        for (int i = 0; i < a.rows(); i++) {
            for (int j = 0; j < a.rows(); j++) {
                if (getDistance(a.viewRow(i), a.viewRow(j)) <= 12) {
                    matrix[i][j] = 1;
                } else {
                    matrix[i][j] = 0;
                }
            }
        }

        return matrix;
    }

    private static String contactMapToString(int[][] matrix) {
        StringBuilder sb = new StringBuilder();
        for ( int i = 0; i<matrix.length;i++) {
            for (int j = 0; j<matrix[i].length;j++) {
                sb.append(matrix[i][j]);
                sb.append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
