/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import cern.colt.matrix.impl.DenseDoubleMatrix2D;

/**
 *
 * @author Carsten
 */
public class Matrix {

    public static int[][] getPositionArray(char[][] alignment) {
        int counter = 0;
        int counter1 = 0;
        int counter2 = 0;
        int[][] rawmatrix = new int[2][alignment[0].length];
        for (int i = 0; i < alignment[0].length; i++) {
            if (alignment[0][i] != '-' && alignment[1][i] != '-') {
                rawmatrix[0][counter] = counter1;
                rawmatrix[1][counter] = counter2;
                counter1++;
                counter2++;
                counter++;
            } else if (alignment[0][i] != '-') {
                counter1++;
            } else if (alignment[1][i] != '-') {
                counter2++;
            }
        }

        int[][] returnmatrix = new int[2][counter]; //TODO anders lösen
        System.arraycopy(rawmatrix[0], 0, returnmatrix[0], 0, counter);
        System.arraycopy(rawmatrix[1], 0, returnmatrix[1], 0, counter);
        return returnmatrix;
    }

    public static DenseDoubleMatrix2D[] getReducedMatrices(DenseDoubleMatrix2D a, DenseDoubleMatrix2D b, int[][] pos_array) {
        DenseDoubleMatrix2D[] new_matrices = new DenseDoubleMatrix2D[2];
        DenseDoubleMatrix2D acopy = (DenseDoubleMatrix2D) a.copy();
        DenseDoubleMatrix2D bcopy = (DenseDoubleMatrix2D) b.copy();
        
        for (int i = 0; i < pos_array[0].length; i++) {
            new_matrices[0].viewRow(i).assign(acopy.viewColumn(pos_array[0][i]));
            new_matrices[1].viewRow(i).assign(bcopy.viewColumn(pos_array[1][i]));
        }
        
        return new_matrices;
    }
    
    public static DenseDoubleMatrix2D[] processMatrices(DenseDoubleMatrix2D a, DenseDoubleMatrix2D b, char[][] alignment) {
        int[][] tmpint = getPositionArray(alignment);
        DenseDoubleMatrix2D[] tmpdmatrix = getReducedMatrices(a, b, tmpint);
        return tmpdmatrix;
    }
}
