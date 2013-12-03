package util;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class MatrixHelper {

    // Deprecated
    public static double[][] makeMatrixSymmetric(double[][] mat) {
        double[][] mat2 = new double[mat.length][mat.length];
        for (int row = mat.length - 1; row >= 0; row--) {
            for (int col = 0; col < mat.length; col++) {
                mat[row][col] = mat[col][row];
            }
        }
        return null;
    }

    public static boolean doubleEquality(double a, double b, double tolerance) {
        double d;
        if (a > b) {
            d = a - b;
        } else {
            d = b - a;
        }
        if (d < tolerance) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean doubleEquality(double a, double b) {
        double d;
        double tolerance = 0.000000001;
        if (a > b) {
            d = a - b;
        } else {
            d = b - a;
        }
        if (d < tolerance) {
            return true;
        } else {
            return false;
        }
    }

    public static String matrix2DimString(double[][] mat) {
        String returnstr = "";
        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[0].length; j++) {
                returnstr += mat[i][j] + "\t";
            }
            returnstr += "\n";
        }
        return returnstr;
    }

    public static String matrix1DimString(double[] mat) {
        String returnstr = "";

        for (double d : mat) {
            returnstr += d + "\t";
        }
        return returnstr;
    }

    public static String matrix1DimString(char[] mat) {
        String returnstr = "";

        for (char c : mat) {
            returnstr += c + "\t";
        }
        return returnstr;
    }

    public static String formatDecimal(double d) {
        NumberFormat df = DecimalFormat.getInstance();

        df.setMinimumFractionDigits(3);
        df.setMaximumFractionDigits(3);
        df.setRoundingMode(RoundingMode.HALF_EVEN);

        return df.format(d);
    }

    public static String formatDecimal(double d, int min, int max) {
        NumberFormat df = DecimalFormat.getInstance();

        df.setMinimumFractionDigits(min);
        df.setMaximumFractionDigits(max);
        df.setRoundingMode(RoundingMode.HALF_EVEN);

        return df.format(d);
    }

    public static int[][] convertTo2DimInteger(double[][] matrix, int factor) {
        //TODO fehler bei sym matrizen
        int[][] returnmatrix = new int[matrix.length][matrix[0].length];
        if (matrix.length != matrix[0].length) {
            for (int i = 0; i < matrix.length; i++) {
                returnmatrix[i] = new int[i + 1];
            }
        }

        if (factor < 0) {

            //faktor herausbekommen -> max
            int max = 0;
            for (int i = 0; i < returnmatrix.length; i++) {
                for (int j = 0; j < returnmatrix[i].length; j++) {
                    String tmp = String.valueOf(matrix[i][j]);
                    if (tmp.split(".")[1].length() > max) {
                        max = tmp.split(".")[1].length();
                    }
                }
            }

            //max als faktor benutzen bzw. wenn gleich 0 einfach nur als int casten
            if (max == 0) {
                for (int i = 0; i < returnmatrix.length; i++) {
                    for (int j = 0; j < returnmatrix[i].length; j++) {
                        returnmatrix[i][j] = (int) matrix[i][j];
                    }
                }
            } else {
                for (int i = 0; i < returnmatrix.length; i++) {
                    for (int j = 0; j < returnmatrix[i].length; j++) {
                        returnmatrix[i][j] = (int) (matrix[i][j] * (int)Math.pow(10., max));
                    }
                }
            }
            for (int i = 0; i < returnmatrix.length; i++) {
                for (int j = 0; j < returnmatrix[i].length; j++) {
                    returnmatrix[i][j] = (int) matrix[i][j];
                }
            }
        } else if (factor == 0) {
            for (int i = 0; i < returnmatrix.length; i++) {
                for (int j = 0; j < returnmatrix[i].length; j++) {
                    returnmatrix[i][j] = (int) matrix[i][j];
                }
            }
        } else {
            for (int i = 0; i < returnmatrix.length; i++) {
                for (int j = 0; j < returnmatrix[i].length; j++) {
                    returnmatrix[i][j] = (int) (matrix[i][j] * (int)Math.pow(10, factor));
                }
            }
        }

        return returnmatrix;
    }

    public static int[][] factor2DimInteger(int[][] matrix, int fac) {
        int[][] mat = matrix;

        for (int i = 0; i < mat.length; i++) {
            for (int j = 0; j < mat[0].length; j++) {
                mat[i][j] = (int) (mat[i][j] * Math.pow(10, fac));
            }
        }

        return mat;
    }
}
