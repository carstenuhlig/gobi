package data;

import java.util.ArrayList;
import util.Type;
import util.MatrixHelper;

public class Matrix {

    private ArrayList<SMatrix> substitionmatrices = new ArrayList<SMatrix>();
    private ArrayList<SMatrix> matrices = new ArrayList<SMatrix>();

    public double[][][] getMatrices(String id1, String id2) {
        // return as 3 Matrices A = [0][][], D = [1][][], I = [2][][]
        for (SMatrix sm : matrices) {
            if (sm.name.equals(id1 + ":" + id2)
                    || sm.name.equals(id2 + ":" + id1)) {
                double[][][] bigmatrix = new double[3][sm.matA.length][sm.matA.length];
                bigmatrix[0] = sm.matA;
                bigmatrix[1] = sm.matD;
                bigmatrix[2] = sm.matI;

                return bigmatrix;
            }
        }
        return null;
    }

    // TODO boolean return if succeeded (validate no duplicate)
    public void addMatrix(String id1, String id2, double[][] matrixA,
            double[][] matrixD, double[][] matrixI, Type type, String as1,
            String as2) {
        matrices.add(new SMatrix(id1 + ":" + id2, matrixA, matrixD, matrixI,
                type, as1, as2));
    }

    public void addSubstitutionMatrix(String name, double[][] matrix,
            char[] chars) {
        // double[][] new_matrix = new double[matrix.length][matrix.length];
        // char[] new_chars = new char[chars.length];
        // System.arraycopy(matrix, 0, new_matrix, 0, matrix.length);
        // System.arraycopy(chars, 0, new_chars, 0, chars.length);
        substitionmatrices.add(new SMatrix(name, matrix, chars));
    }

    // TODO gucken ob alle substitionmatrizen gleich struktur.. bzw. gleiches
    // Alphabet und Reihenfolge haben
    public double[][] getSubstitutionMatrix(String name) {
        for (SMatrix sm : substitionmatrices) {
            if (sm.name.equals(name)) {
                return sm.mat;
            }
        }
        return null;
    }

    public char[] getConvMat(String name) {
        for (SMatrix sm : substitionmatrices) {
            if (sm.name.equals(name)) {
                return sm.chars;
            }
        }
        return null;
    }

    public void printAllSubstitionMatrices() {
        for (SMatrix sm : substitionmatrices) {
            System.out.println(sm.toString());
        }
    }

    public void printAllCalculatedMatrices() {
        for (SMatrix m : matrices) {
            System.out.println(m.toString());
        }
    }

    public void addAlignment(String id1, String id2, String a, String b,
            char[][] alignment, double score, Type type, double[][][] matrix) {
        for (SMatrix sm : matrices) {
            if (sm.name.equals(id1 + ":" + id2)) {
                sm.score = score;
                sm.alignment = alignment;
                // TODO beim saveMatrices String a und b hinzufügen
                sm.a = a;
                sm.b = b;
                sm.optimiseAlignmentArray();
                return;
            }
        }
        matrices.add(new SMatrix(id1 + ":" + id2, a, b, alignment, score, type,
                matrix));
    }

    public void printSubstitutionMatrixByName(String name) {
        for (SMatrix sm : substitionmatrices) {
            if (sm.name.equals(name)) {
                System.out.println(sm);
            }
        }
    }

    public void deleteCalculatedMatrixByName(String id1, String id2) {
        for (int i = 0; i < matrices.size(); i++) {
            if (((SMatrix) matrices.get(i)).name.equals(id1 + ":" + id2)) {
                matrices.remove(i);
                return;
            }
        }
    }

    public void emptyMatrices() {
        matrices.clear();
    }

    public void printAlignment(String name) {
        for (SMatrix sm : matrices) {
            if (sm.name.equals(name)) {
                sm.printAlignment();
            }
        }
    }
    
    public String getAlignmentAsString(String name) {
        StringBuilder sb = new StringBuilder();
        
        for (SMatrix sm : matrices) {
            if (sm.name.equals(name)) {
                sb.append(sm.writeAlignmentToFile());
            }
        }
        
        return sb.toString();
    }

    // jede Matrix nur 1 Typ, d.h. entweder Substitutionsmatrix, lokal
    // Alignment, global Alignment oder Freeshift Alignment
    // TODO Performance Mehrere Typen für eine Matrix (weniger speicher
    // verbrauch wenn alle alignment arten dann 3fache menge an Daten)
    // TODO SMatrix als eigene public Klasse für alle zugreifbar ->
    // PERFORMANCE Smatrix als eigene klasse (nicht innere klasse)
    private class SMatrix {

        public String name; // like id or hash

        // matrices in [row|I|Y][column|J|X]-format
        public double[][] mat;
        public double[][] matA;
        public double[][] matI;
        public double[][] matD;
        public String a;
        public String b;
        public Type t;
        // für Substitionsmatrix wenn sie symmetrisch ist
        // sodass die eine "hälfte" der matrix unausgefüllt ist
        private boolean sym;

        // Strings mit Gaps
        public char[][] alignment;

        public double score;

        // convmat in [index -> char]
        public char[] chars;

        // substitionmatrix
        public SMatrix(String name, double[][] matrix, char[] chars) {
            this.name = name;
            this.mat = matrix;
            this.chars = chars;
            t = Type.SUBSTITUTIONMATRIX;
            if (matrix[0].length != matrix[1].length) {
                sym = true;
            } else {
                sym = false;
            }
        }

        // calculated matrix
        // TODO fehlende Werte einfügen
        public SMatrix(String name, double[][] matrixA, double[][] matrixD,
                double[][] matrixI, Type type, String as1, String as2) {
            this.name = name;
            this.matA = matrixA;
            this.matD = matrixD;
            this.matI = matrixI;
            this.t = type;
            this.a = as1;
            this.b = as2;
        }

        public SMatrix(String name, String a, String b, char[][] alignment,
                double score, Type type, double[][][] matrix) {
            this.name = name;
            this.matA = matrix[0];
            this.matD = matrix[1];
            this.matI = matrix[2];
            this.t = type;
            this.a = a;
            this.b = b;
            this.score = score;
            this.alignment = alignment;
            optimiseAlignmentArray();
        }

        private void printAlignment() {
            String a_align = util.StringHelper
                    .processDoubleArrayToString(this.alignment[0]);
            String b_align = util.StringHelper
                    .processDoubleArrayToString(this.alignment[1]);
            String[] ids = this.name.split(":");
            System.out.println(ids[0] + ": " + a_align);
            System.out.println(ids[1] + ": " + b_align);
        }

        private String writeAlignmentToFile() {
            StringBuilder sb = new StringBuilder();

            String a_align = util.StringHelper
                    .processDoubleArrayToString(this.alignment[0]);
            String b_align = util.StringHelper
                    .processDoubleArrayToString(this.alignment[1]);
            String[] ids = this.name.split(":");
            sb.append(ids[0]);
            sb.append(": ");
            sb.append(a_align);
            sb.append("\n");
            sb.append(ids[1]);
            sb.append(": ");
            sb.append(b_align);
            sb.append("\n");

            return sb.toString();
        }

        private void optimiseAlignmentArray() {
            int end = -1;
            for (int i = 0; i < this.alignment[0].length; i++) {
                if (Character.getNumericValue(alignment[0][i]) == -1) {
                    end = i;
                }
                // TODO mit while schleife schneller
                if (Character.getNumericValue(alignment[0][i]) > -1) {
                    break;
                }
            }

            char[][] new_alignment = new char[2][alignment[0].length - end - 1];

            System.arraycopy(alignment[0], end + 1, new_alignment[0], 0,
                    alignment[0].length - end - 1);
            System.arraycopy(alignment[1], end + 1, new_alignment[1], 0,
                    alignment[1].length - end - 1);
            this.alignment = new_alignment;
        }

        public void setChars(char[] chr) {
            chars = new char[chr.length];
            System.arraycopy(chr, 0, chars, 0, chr.length);
        }

        @Override
        public String toString() {
            String returnstr = "";
            returnstr += "Name:\t" + name + "\n";
            returnstr += "Type:\t" + t + "\n";
            if (t == Type.SUBSTITUTIONMATRIX) {
                if (sym == false) {
                    returnstr += "\t";
                    for (int xtmp = 0; xtmp < mat.length; xtmp++) {
                        returnstr += (xtmp + 1) + "\t";
                    }
                    returnstr += "\n";
                    for (int y = 0; y < mat.length; y++) {
                        returnstr += (y + 1) + "\t";
                        for (int x = 0; x < mat.length; x++) {
                            returnstr += util.MatrixHelper.formatDecimal(
                                    mat[y][x], 1, 3) + "\t";
                        }
                        returnstr += "\n";
                    }
                } else {
                    for (int col = 0; col < chars.length; col++) {
                        returnstr += "\t" + chars[col];
                    }
                    returnstr += "\n";
                    for (int row = 0; row < chars.length; row++) {
                        returnstr += chars[row];
                        for (double d : mat[row]) {
                            returnstr += "\t"
                                    + util.MatrixHelper.formatDecimal(d, 1, 3);
                        }
                        returnstr += "\n";
                    }
                }
            } else {
                // TODO Performance: tab vor xtmp, etc.
                // TODO Check
                returnstr += "Matrix:\tA\n\t";
                for (int xtmp = 0; xtmp < matA[0].length; xtmp++) {
                    if (xtmp > 0) {
                        returnstr += b.charAt(xtmp - 1) + "\t";
                    } else {
                        returnstr += " \t";
                    }
                }
                returnstr += "\n";
                for (int y = 0; y < matA.length; y++) {
                    if (y > 0) {
                        returnstr += a.charAt(y - 1) + "\t";
                    } else {
                        returnstr += " \t";
                    }
                    for (int x = 0; x < matA[0].length; x++) {
                        returnstr += util.MatrixHelper.formatDecimal(
                                matA[y][x], 1, 3) + "\t";
                    }
                    returnstr += "\n\n";
                }

                returnstr += "Matrix:\tD\n\t";
                for (int xtmp = 0; xtmp < matD[0].length; xtmp++) {
                    if (xtmp > 0) {
                        returnstr += b.charAt(xtmp - 1) + "\t";
                    } else {
                        returnstr += " \t";
                    }
                }
                returnstr += "\n";
                for (int y = 0; y < matD.length; y++) {
                    if (y > 0) {
                        returnstr += a.charAt(y - 1) + "\t";
                    } else {
                        returnstr += " \t";
                    }
                    for (int x = 0; x < matD[0].length; x++) {
                        if (matD[y][x] == -Double.MAX_VALUE) {
                            returnstr += "-Inf\t";
                        } else {
                            returnstr += util.MatrixHelper.formatDecimal(
                                    matD[y][x], 1, 3) + "\t";
                        }
                    }
                    returnstr += "\n\n";
                }

                returnstr += "Matrix:\tI\n\t";
                for (int xtmp = 0; xtmp < matI[0].length; xtmp++) {
                    if (xtmp > 0) {
                        returnstr += b.charAt(xtmp - 1) + "\t";
                    } else {
                        returnstr += " \t";
                    }
                }
                returnstr += "\n";
                for (int y = 0; y < matI.length; y++) {
                    if (y > 0) {
                        returnstr += a.charAt(y - 1) + "\t";
                    } else {
                        returnstr += " \t";
                    }
                    for (int x = 0; x < matI[0].length; x++) {
                        if (matI[y][x] == -Double.MAX_VALUE) {
                            returnstr += "-Inf\t";
                        } else {
                            returnstr += util.MatrixHelper.formatDecimal(
                                    matI[y][x], 1, 3) + "\t";
                        }
                    }
                    returnstr += "\n\n";
                }
            }
            return returnstr;
        }
    }
}
