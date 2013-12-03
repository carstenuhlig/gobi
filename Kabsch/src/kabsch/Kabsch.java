/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kabsch;

import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;

/**
 *
 * @author uhligc
 */
public class Kabsch {

    DenseDoubleMatrix2D matrixA, matrixB;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

    }

    public void init(double[][] matrixA, double[][] matrixB) {
        this.matrixA = new DenseDoubleMatrix2D(matrixA);
        this.matrixB = new DenseDoubleMatrix2D(matrixB);
    }

    public double[] getOriginVector(DenseDoubleMatrix2D matrix) {
        int rows = matrix.rows();
        int cols = matrix.columns();
        
        double[] result = new double[cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                result[j] += matrix.getQuick(i, j);
            }
        }
        
        for (int i = 0; i < cols; i++) {
            result[i] = result[i]/rows;
        }

        return result;
    }

    public static double getDistance(double[] a, double[] b) {
        //TODO mit Exception behandeln
        if (a.length != b.length) {
            return -Double.MAX_VALUE;
        }

        double result = 0.;

        for (int i = 0; i < a.length; i++) {
            result += Math.pow(a[i] - b[i], 2);
        }

        result = Math.sqrt(result);

        return result;
    }
}
