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

    public static double calcGDT(DenseDoubleMatrix2D matrixa, DenseDoubleMatrix2D matrixb) {
        int p1 = 0;
        int p2 = 0;
        int p4 = 0;
        int p8 = 0;

        int size = matrixa.rows();
        double tmp;

        for (int i = 0; i < size; i++) {
            tmp = getDistance(matrixa.viewRow(i), matrixb.viewRow(i));
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
            }
        }

        return ((p1 + p2 + p4 + p8) / (4. * size));
    }

    public static double getRMSD(DenseDoubleMatrix2D a, DenseDoubleMatrix2D b) {
        //copy schritt kann weggelassen werden falls keine nachstehenden operation an a mehr gibt
        
    	//tmp ist a nur nochmal kopiert
    	//a - b
        DenseDoubleMatrix2D tmp = (DenseDoubleMatrix2D) a.copy();
        //2 2dimensionale matrizen
        //a und b
        //enthalten je
        tmp.assign(b, F.minus);
        tmp.assign(F.square); //hoch 2
        int rows = tmp.rows();
        
        double result = tmp.zSum(); //summe über alle elemente in matrix

        result = Math.sqrt(result / rows); //wurzel von (summe über alle elemente / anzahl aminosäuren)

        return result;
    }

    public static double getDistance(DoubleMatrix1D a, DoubleMatrix1D b) {
        DoubleMatrix1D tmp = a.copy();
        tmp.assign(b, F.minus);
        return Math.sqrt(tmp.aggregate(F.plus, F.square));
    }
}
