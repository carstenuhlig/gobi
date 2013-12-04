/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kabsch;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import static kabsch.Kabsch.F;

/**
 *
 * @author uhligc
 */
public class Scores {

    public static double calcGDT(double[][] matrixa, double[][] matrixb, int sizea, int sizeb) {
        if (matrixa.length != matrixb.length) {
            return -Double.MAX_VALUE;
        }

        //matrix enthält 4 spalten --> 1.spalte = wenn 1 dann aligniert wenn 0 dann nix
        int p1 = 0;
        int p2 = 0;
        int p4 = 0;
        int p8 = 0;

        int size = matrixa.length;
        double tmp = 0;

        for (int i = 0; i < size; i++) {
            if (matrixa[i][0] == 0) {
                continue;
            }
            if (matrixb[i][0] == 0) {
                continue;
            }

            tmp = getDistance(matrixa[i], matrixb[i]);
            if (tmp <= 1) {
                p1++;
                p2++;
                p4++;
                p8++;
            } else if (tmp <= 2) {
                p2++;
                p4++;
                p8++;
            } else if (tmp <= 4) {
                p4++;
                p8++;
            } else if (tmp <= 8) {
                p8++;
            } else {
                //nix...
            }
        }

        return (p1 + p2 + p4 + p8 / (4 * ((sizea + sizeb) / 2.)));
    }
    
    public static double getRMSD(DenseDoubleMatrix2D a, DenseDoubleMatrix2D b) {
        DenseDoubleMatrix2D tmp = (DenseDoubleMatrix2D)a.clone();
        tmp.assign(b, F.minus);
        tmp.assign(F.square);
        int rows = tmp.rows();
                
        double result = tmp.zSum();
        
        result = Math.sqrt(result/rows);
        
        return result;
    }

    public static double getDistance(double[] a, double[] b) {
        //TODO mit Exception behandeln
        if (a.length != b.length) {
            return -Double.MAX_VALUE;
        }

        double result = 0.;

        for (int i = 1; i < a.length; i++) {
            result += Math.pow(a[i] - b[i], 2);
        }

        result = Math.sqrt(result);

        return result;
    }
}
