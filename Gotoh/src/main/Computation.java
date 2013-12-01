package main;

import data.Matrix;
import util.MatrixHelper;
import util.Type;

public class Computation {

    // erste AS Sequenz
    // a = row
    private static String a;
    // zweite AS Sequenz
    // b = col
    private static String b;
    // gap_extend score (default: 12
    private static int go;
    // gap_open score (default: 1)
    private static int ge;
    // SubstitutionsMatrix
    private static int[][] smat;
    private static char[] schars;
    private static int factor;

    private static Type type;

    private static double score;

    // matrix A,D,I;0,1,2 <- dyn. Programmierungs Matrix mit Matrix A,D und I
    private static int[][][] mat;

    // matrix für das Backtracken...
    // TODO Backtracking Planung
    private static char[][] backtrack;
    private static String id_a;
    private static String id_b;

    public static void init(String as1, String as2, int[][] smatrix,
            char[] schars, double gapopen, double gapextend, Type type,
            String id1, String id2, int factor, int factor_smat) {
        Computation.a = as1;
        Computation.b = as2;
        Computation.mat = new int[3][a.length() + 1][b.length() + 1];
        Computation.ge = (int) (gapextend * (int)Math.pow(10, factor));
        Computation.go = (int) (gapopen * (int)Math.pow(10, factor));
        
        if (factor == factor_smat) {
            Computation.smat = smatrix;
        } else {
            Computation.smat = util.MatrixHelper.factor2DimInteger(smatrix, factor - factor_smat);
        }
        
        Computation.type = type;
        Computation.id_a = id1;
        Computation.id_b = id2;

        //TODO schars als hashmap -> getsmatrixscore um einiges schneller
        Computation.schars = schars;
        Computation.backtrack = null;
        Computation.score = -Double.MAX_VALUE;
        Computation.factor = factor;

        // bei local alignment mindestens wert von 0, d.h. so lassen wie bei
        // Initialisierung von Array
        if (type != Type.LOCAL || type != Type.FREESHIFT) {
            for (int row = 1; row < a.length() + 1; row++) {
                // A0,k = g(k)
                mat[0][row][0] = calcGapScore(row);
                // Di,0 = -Inf
                mat[1][row][0] = -Integer.MAX_VALUE/2;
            }
            for (int col = 1; col < b.length() + 1; col++) {
                // Ak,0 = g(k)
                mat[0][0][col] = calcGapScore(col);
                // I0,j = -Inf
                mat[2][0][col] = -Integer.MAX_VALUE/2;
            }
        }
    }

    public static void calcMatrices() {
        for (int row = 1; row < mat[2].length; row++) {
            for (int col = 1; col < mat[2][row].length; col++) {
                // Matrix I
                mat[2][row][col] = Math.max(mat[0][row - 1][col] + ge + go,
                        mat[2][row - 1][col] + ge);
                // Matrix D
                mat[1][row][col] = Math.max(mat[0][row][col - 1] + ge + go,
                        mat[1][row][col - 1] + ge);
                // Matrix A
                mat[0][row][col] = Math.max(
                        mat[0][row - 1][col - 1]
                        + getSMatrixScore(a.charAt(row - 1),
                                b.charAt(col - 1)),
                        Math.max(mat[1][row][col], mat[2][row][col]));
            }
        }
    }

    public static void calcMatricesLocal() {
        for (int row = 1; row < mat[2].length; row++) {
            for (int col = 1; col < mat[2][0].length; col++) {
                // Matrix I
                mat[2][row][col] = Math.max(Math.max(mat[0][row - 1][col] + ge
                        + go, mat[2][row - 1][col] + ge), 0);
                // Matrix D
                mat[1][row][col] = Math.max(Math.max(mat[0][row][col - 1] + ge
                        + go, mat[1][row][col - 1] + ge), 0);
                // Matrix A
                mat[0][row][col] = Math.max(
                        Math.max(
                                mat[0][row - 1][col - 1]
                                + getSMatrixScore(a.charAt(row - 1),
                                        b.charAt(col - 1)),
                                Math.max(mat[1][row][col], mat[2][row][col])),
                        0);
            }
        }
    }

    public static double backtrack() {
        char[][] alignment = new char[2][a.length() + b.length()];
        int count = 0;
        Computation.score = -Double.MAX_VALUE;
        switch (type) {
            case GLOBAL:
                int row = a.length();
                int col = b.length();
                while (row > 0 && col > 0) {
                    if (mat[0][row][col] == (mat[0][row - 1][col - 1] + getSMatrixScore(
                            a.charAt(row - 1), b.charAt(col - 1)))) {
                        alignment[0][a.length() + b.length() - 1 - count] = a
                                .charAt(row - 1);
                        alignment[1][a.length() + b.length() - 1 - count] = b
                                .charAt(col - 1);
                        count++;
                        row--;
                        col--;
                        // if (!(row > 0) || !(col > 0))
                        // break;
                    } else if (mat[0][row][col] == mat[2][row][col]) {
                        int k = 1;
                        while (mat[0][row - k][col] + calcGapScore(k) != mat[0][row][col]) {
                            k++;
                        }
                        for (int i = 1; i < k + 1; i++) {
                            alignment[0][a.length() + b.length() - 1 - count] = a
                                    .charAt(row - 1);
                            alignment[1][a.length() + b.length() - 1 - count] = '-';
                            count++;
                            row--;
                            // if (!(row > 0))
                            // break;
                        }
                    } else if (mat[0][row][col] == mat[1][row][col]) {
                        int k = 1;
                        while (mat[0][row][col - k] + calcGapScore(k) != mat[0][row][col]) {
                            k++;
                        }
                        for (int i = 1; i < k + 1; i++) {
                            alignment[0][a.length() + b.length() - 1 - count] = '-';
                            alignment[1][a.length() + b.length() - 1 - count] = b
                                    .charAt(col - 1);
                            count++;
                            col--;
                            // if (!(col > 0))
                            // break;
                        }
                    }
                }
                Computation.backtrack = alignment;
                Computation.score = mat[0][mat[0].length - 1][mat[0][0].length - 1] / (Math.pow(10, factor));
                break;
            case LOCAL:
                // get Highest score
                int[] tmpintarray = getHighestScore();
                row = tmpintarray[0];
                col = tmpintarray[1];
                score = mat[0][row][col] / (Math.pow(10,factor));

                for (int j = b.length() - 1; j >= col; j--) {
                    alignment[0][a.length() + b.length() - 1 - count] = '-';
                    alignment[1][a.length() + b.length() - 1 - count] = b.charAt(j);
                    count++;
                }

                for (int i = a.length() - 1; i >= row; i--) {
                    alignment[0][a.length() + b.length() - 1 - count] = a.charAt(i);
                    alignment[1][a.length() + b.length() - 1 - count] = '-';
                    count++;
                }

                while (row > 0 && col > 0 && mat[0][row][col] > 0) {
                    if (mat[0][row][col] == (mat[0][row - 1][col - 1] + getSMatrixScore(
                            a.charAt(row - 1), b.charAt(col - 1)))) {
                        alignment[0][a.length() + b.length() - 1 - count] = a
                                .charAt(row - 1);
                        alignment[1][a.length() + b.length() - 1 - count] = b
                                .charAt(col - 1);
                        count++;
                        row--;
                        col--;
                        // if (!(row > 0) || !(col > 0))
                        // break;
                    } else if (mat[0][row][col] == mat[2][row][col]) {
                        int k = 1;
                        while (mat[0][row - k][col] + calcGapScore(k) != mat[0][row][col]) {
                            k++;
                        }
                        for (int i = 1; i < k + 1; i++) {
                            alignment[0][a.length() + b.length() - 1 - count] = a
                                    .charAt(row - 1);
                            alignment[1][a.length() + b.length() - 1 - count] = '-';
                            count++;
                            row--;
                        }
                    } else if (mat[0][row][col] == mat[1][row][col]) {
                        int k = 1;
                        while (mat[0][row][col - k] + calcGapScore(k) != mat[0][row][col]) {
                            k++;
                        }
                        for (int i = 1; i < k + 1; i++) {
                            alignment[0][a.length() + b.length() - 1 - count] = '-';
                            alignment[1][a.length() + b.length() - 1 - count] = b
                                    .charAt(col - 1);
                            count++;
                            col--;
                        }
                    }
                }

                // restlichen AS in Alignment
                while (col > 0) {
                    alignment[0][a.length() + b.length() - 1 - count] = '-';
                    alignment[1][a.length() + b.length() - 1 - count] = b
                            .charAt(col - 1);
                    count++;
                    col--;
                }

                while (row > 0) {
                    alignment[0][a.length() + b.length() - 1 - count] = a
                            .charAt(row - 1);
                    alignment[1][a.length() + b.length() - 1 - count] = '-';
                    count++;
                    row--;
                }

                Computation.backtrack = alignment;
                break;
            case FREESHIFT:
                int[] tmpintarray2 = getHighestFreeShiftScore();
                row = tmpintarray2[0];
                col = tmpintarray2[1];

                Computation.score = mat[0][row][col] / (Math.pow(10,factor));

                // Anfang: (gaps werden hinzugefügt)
                for (int j = b.length() - 1; j >= col; j--) {
                    alignment[0][a.length() + b.length() - 1 - count] = '-';
                    alignment[1][a.length() + b.length() - 1 - count] = b.charAt(j);
                    count++;
                }

                for (int i = a.length() - 1; i >= row; i--) {
                    alignment[0][a.length() + b.length() - 1 - count] = a.charAt(i);
                    alignment[1][a.length() + b.length() - 1 - count] = '-';
                    count++;
                }

                // Hauptteil
                while (row > 0 && col > 0) {
                    if (mat[0][row][col] == (mat[0][row - 1][col - 1] + getSMatrixScore(
                            a.charAt(row - 1), b.charAt(col - 1)))) {
                        alignment[0][a.length() + b.length() - 1 - count] = a
                                .charAt(row - 1);
                        alignment[1][a.length() + b.length() - 1 - count] = b
                                .charAt(col - 1);
                        count++;
                        row--;
                        col--;
                    } else if (mat[0][row][col] == mat[2][row][col]) {
                        int k = 1;
                        while (k < row
                                && mat[0][row - k][col] + calcGapScore(k) != mat[0][row][col]) {
                            k++;
                        }
                        for (int i = 1; i < k + 1; i++) {
                            alignment[0][a.length() + b.length() - 1 - count] = a
                                    .charAt(row - 1);
                            alignment[1][a.length() + b.length() - 1 - count] = '-';
                            count++;
                            row--;
                        }
                    } else if (mat[0][row][col] == mat[1][row][col]) {
                        int k = 1;
                        while (k < col
                                && mat[0][row][col - k] + calcGapScore(k) != mat[0][row][col]) {
                            k++;
                        }
                        for (int i = 1; i < k + 1; i++) {
                            alignment[0][a.length() + b.length() - 1 - count] = '-';
                            alignment[1][a.length() + b.length() - 1 - count] = b
                                    .charAt(col - 1);
                            count++;
                            col--;
                        }
                    }

                    // Bedingung wenn Rand erreicht dann stop
                    if (row == 0 || col == 0) {
                        break;
                    }
                }

                // restlichen AS in Alignment
                while (col > 0) {
                    alignment[0][a.length() + b.length() - 1 - count] = '-';
                    alignment[1][a.length() + b.length() - 1 - count] = b
                            .charAt(col - 1);
                    count++;
                    col--;
                }

                while (row > 0) {
                    alignment[0][a.length() + b.length() - 1 - count] = a
                            .charAt(row - 1);
                    alignment[1][a.length() + b.length() - 1 - count] = '-';
                    count++;
                    row--;
                }

                Computation.backtrack = alignment;
                break;
            default:
        }
        return Computation.score;
    }

    public static boolean checkAlignment() {
        // modi definiert ... egal; 0 : S(a,b); 1 = insertion; 2 = deletion
        int modi = -1;

        switch (type) {
            case GLOBAL:
                int x1 = 0;
                double returndouble = 0;

                while (Character.getNumericValue(backtrack[0][x1]) == -1) {
                    x1++;
                }

                for (int x2 = x1; x2 < backtrack[0].length; x2++) {
                    switch (modi) {
                        case 0:
                            if (backtrack[0][x2] == '-') {
                                modi = 2;
                                returndouble += go + ge;
                            } else if (backtrack[1][x2] == '-') {
                                modi = 1;
                                returndouble += go + ge;
                            } else { // wenn S(a,b)
                                returndouble += getSMatrixScore(backtrack[0][x2],
                                        backtrack[1][x2]);
                            }
                            break;
                        case 1:
                            if (backtrack[0][x2] == '-') {
                                modi = 2;
                                returndouble += go + ge;
                            } else if (backtrack[1][x2] == '-') {
                                returndouble += ge;
                            } else { // wenn S(a,b)
                                modi = 0;
                                returndouble += getSMatrixScore(backtrack[0][x2],
                                        backtrack[1][x2]);
                            }
                            break;
                        case 2:
                            if (backtrack[0][x2] == '-') {
                                returndouble += ge;
                            } else if (backtrack[1][x2] == '-') {
                                modi = 1;
                                returndouble += go + ge;
                            } else { // wenn S(a,b)
                                modi = 0;
                                returndouble += getSMatrixScore(backtrack[0][x2],
                                        backtrack[1][x2]);
                            }
                            break;
                        default:
                            if (backtrack[0][x2] == '-') {
                                modi = 2;
                                returndouble += go + ge;
                            } else if (backtrack[1][x2] == '-') {
                                modi = 1;
                                returndouble += go + ge;
                            } else { // wenn S(a,b)
                                modi = 0;
                                returndouble += getSMatrixScore(backtrack[0][x2],
                                        backtrack[1][x2]);
                            }
                            break;
                    }
                }
                if (returndouble == score) {
                    return true;
                } else {
                    return false;
                }
            case FREESHIFT:
                int start = getStartFREESHIFT();
                int ende = getEndFREESHIFT();
                returndouble = 0;

                x1 = -1;

                for (int x2 = start; x2 < ende + 1; x2++) {
                    switch (modi) {
                        case 0:
                            if (backtrack[0][x2] == '-') {
                                modi = 2;
                                returndouble += go + ge;
                            } else if (backtrack[1][x2] == '-') {
                                modi = 1;
                                returndouble += go + ge;
                            } else { // wenn S(a,b)
                                returndouble += getSMatrixScore(backtrack[0][x2],
                                        backtrack[1][x2]);
                            }
                            break;
                        case 1:
                            if (backtrack[0][x2] == '-') {
                                modi = 2;
                                returndouble += go + ge;
                            } else if (backtrack[1][x2] == '-') {
                                returndouble += ge;
                            } else { // wenn S(a,b)
                                modi = 0;
                                returndouble += getSMatrixScore(backtrack[0][x2],
                                        backtrack[1][x2]);
                            }
                            break;
                        case 2:
                            if (backtrack[0][x2] == '-') {
                                returndouble += ge;
                            } else if (backtrack[1][x2] == '-') {
                                modi = 1;
                                returndouble += go + ge;
                            } else { // wenn S(a,b)
                                modi = 0;
                                returndouble += getSMatrixScore(backtrack[0][x2],
                                        backtrack[1][x2]);
                            }
                            break;
                        default:
                            if (backtrack[0][x2] == '-') {
                                modi = 2;
                                returndouble += go + ge;
                            } else if (backtrack[1][x2] == '-') {
                                modi = 1;
                                returndouble += go + ge;
                            } else { // wenn S(a,b)
                                modi = 0;
                                returndouble += getSMatrixScore(backtrack[0][x2],
                                        backtrack[1][x2]);
                            }
                    }
                }
                if (returndouble == score) {
                    return true;
                } else {
                    return false;
                }
            case LOCAL:
                start = getStartLOCAL();
                ende = getEndLOCAL();
                returndouble = 0;

                x1 = -1;

                for (int x2 = start; x2 < ende + 1; x2++) {
                    switch (modi) {
                        case 0:
                            if (backtrack[0][x2] == '-') {
                                modi = 2;
                                returndouble += go + ge;
                            } else if (backtrack[1][x2] == '-') {
                                modi = 1;
                                returndouble += go + ge;
                            } else { // wenn S(a,b)
                                returndouble += getSMatrixScore(backtrack[0][x2],
                                        backtrack[1][x2]);
                            }
                            break;
                        case 1:
                            if (backtrack[0][x2] == '-') {
                                modi = 2;
                                returndouble += go + ge;
                            } else if (backtrack[1][x2] == '-') {
                                returndouble += ge;
                            } else { // wenn S(a,b)
                                modi = 0;
                                returndouble += getSMatrixScore(backtrack[0][x2],
                                        backtrack[1][x2]);
                            }
                            break;
                        case 2:
                            if (backtrack[0][x2] == '-') {
                                returndouble += ge;
                            } else if (backtrack[1][x2] == '-') {
                                modi = 1;
                                returndouble += go + ge;
                            } else { // wenn S(a,b)
                                modi = 0;
                                returndouble += getSMatrixScore(backtrack[0][x2],
                                        backtrack[1][x2]);
                            }
                            break;
                        default:
                            if (backtrack[0][x2] == '-') {
                                modi = 2;
                                returndouble += go + ge;
                            } else if (backtrack[1][x2] == '-') {
                                modi = 1;
                                returndouble += go + ge;
                            } else { // wenn S(a,b)
                                modi = 0;
                                returndouble += getSMatrixScore(backtrack[0][x2],
                                        backtrack[1][x2]);
                            }
                    }
                }
                if (returndouble == score) {
                    return true;
                } else {
                    return false;
                }
            default:
                break;
        }
        // wenn true dann check score erfolgreich und richtiges ergebnis
        return false;
    }

    public static int getStartFREESHIFT() {
        if (Character.getNumericValue(backtrack[0][0]) != -1) {
            return 0;
        }
        int x = 1;
        while (backtrack[0][x] == backtrack[0][x - 1]
                || Character.getNumericValue(backtrack[0][x]) == -1) {
            x++;
        }
        return x;
    }

    public static int getEndFREESHIFT() {
        if (Character.getNumericValue(backtrack[0][backtrack[0].length - 1]) != -1) {
            return backtrack[0].length - 1;
        }
        int x = backtrack[0].length - 2;
        while (backtrack[0][x] == backtrack[0][x + 1]
                || Character.getNumericValue(backtrack[0][x]) == -1) {
            x--;
        }
        return x;
    }

    public static int getStartLOCAL() {
        int x = 0;
        while (backtrack[0][x] == '-' || backtrack[1][x] == '-'
                || Character.getNumericValue(backtrack[0][x]) == -1) {
            x++;
        }
        return x;
    }

    public static int getEndLOCAL() {
        int x = backtrack[0].length - 1;
        while (backtrack[0][x] == '-' || backtrack[1][x] == '-'
                || Character.getNumericValue(backtrack[0][x]) == -1) {
            x--;
        }
        return x;
    }

    public static void saveAlignment(Matrix m) {
        // check ob matrizen schon gespeichert sind
        if (m.getMatrices(id_a, id_b) == null) {
            saveMatrices(m);
        }
        m.addAlignment(id_a, id_b, a, b, backtrack, score, type, mat);
    }

    private static int getSMatrixScore(char a, char b) {
        // default position letzte position
        int ai = smat.length - 1;
        int bi = smat.length - 1;
        for (int i = 0; i < schars.length; i++) {
            if (schars[i] == a) {
                ai = i;
            }
            if (schars[i] == b) {
                bi = i;
            }
        }
        if (smat[0].length != smat[1].length && ai < bi) {
            return smat[bi][ai];
        } else {
            return smat[ai][bi];
        }
    }

    private static int[] getHighestScore() {
        int blah = -1;
        int[] returnint = new int[2];
        for (int row = 0; row < mat[0].length; row++) {
            for (int col = 0; col < mat[0][0].length; col++) {
                if (mat[0][row][col] > blah) {
                    blah = mat[0][row][col];
                    returnint[0] = row;
                    returnint[1] = col;
                }
            }
        }
        return returnint;
    }

    private static int calcGapScore(int n) {
        return go + ge * n;
    }

    public static void saveMatrices(Matrix m) {
        m.addMatrix(id_a, id_b, Computation.mat[0], Computation.mat[1],
                Computation.mat[2], Computation.type, Computation.a,
                Computation.b);
    }

    //TODO überprüfen ob richtiges ergebnis
    private static int[] getHighestFreeShiftScore() {
        int[] returnint = new int[2];
        double max = -Double.MAX_VALUE;
        for (int row = 1; row < a.length() + 1; row++) {
            if (mat[0][row][b.length()] > max) {
                max = mat[0][row][b.length()];
                returnint[0] = row;
                returnint[1] = b.length();
            }
        }

        for (int col = 1; col < b.length() + 1; col++) {
            if (mat[0][a.length()][col] > max) {
                max = mat[0][a.length()][col];
                returnint[0] = a.length();
                returnint[1] = col;
            }
        }

        // for (int tmp = 0; tmp < mat[0][0].length; tmp++) {
        // System.out.print(tmp + "\t");
        // }
        // System.out.println("");
        // for (int i = 0; i < mat[0].length; i++) {
        // System.out.print(i + "\t");
        // for (int j = 0; j < mat[0][0].length; j++) {
        // System.out.print(util.MatrixHelper.formatDecimal(mat[0][i][j],
        // 0, 1) + "\t");
        // }
        // System.out.println();
        // }
        return returnint;
    }
}
