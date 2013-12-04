/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kabsch;

import cern.colt.matrix.DoubleMatrix1D;
import cern.colt.matrix.DoubleMatrix2D;
import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import cern.colt.matrix.linalg.Algebra;
import cern.jet.math.Functions;

/**
 *
 * @author uhligc
 */
public class Kabsch {

    DenseDoubleMatrix2D p, q;
    DoubleMatrix2D a;
    DenseDoubleMatrix1D cP, cQ;

    double e0;

    final static Functions F = Functions.functions;

    public Kabsch(double[][] matrixA, double[][] matrixB) {
        init(matrixA, matrixB);
    }

    public Kabsch(DenseDoubleMatrix2D p, DenseDoubleMatrix2D q) {
        this.p = p;
        this.q = q;
    }

    private void translate() {
        //centroids calc
        cP = getOriginVector(p);
        cQ = getOriginVector(q);

        //p und q in 0,0,0 verschieben mit centroids
        p = applyVector(p, cP);
        q = applyVector(q, cQ);

        //e0 fehler berechnen (summe über alle koordinaten quadriert)
        e0 = calcInitError(p, q);
    }
    
    private void calcCovarianceMatrix() {
        Algebra algebra = new Algebra();
        //transpose
        DoubleMatrix2D tcP = p.viewDice();
        //multiplikation der beiden matrizen
        a = algebra.mult(tcP, q);
    }
    
    private void calcRotation() {
        
    }

    public void main() {
        //P-> cP; Q-> cQ
        translate();
        
        //A = P^T * Q
        calcCovarianceMatrix();
    }

    public static DenseDoubleMatrix2D applyVector(DenseDoubleMatrix2D matrix, DenseDoubleMatrix1D vector) {
        int rows = matrix.rows();
        int cols = matrix.columns();
        if (cols != vector.size()) {
            return null;
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                //TODO mit integrierten Funktionen machen (FactoryFunctions) --> keinen blassen schimmer wie das geht.
//                matrix.assign(F.minus.apply(matrix.get(i, j), vector.get(j)));
                matrix.set(i, j, matrix.get(i, j) - vector.get(j));
            }
        }

        return matrix;
    }

    public static double calcInitError(DenseDoubleMatrix2D p, DenseDoubleMatrix2D q) {
        return p.aggregate(F.plus, F.square) + q.aggregate(F.plus,F.square);
    }

    public void init(double[][] matrixA, double[][] matrixB) {
        this.p = new DenseDoubleMatrix2D(matrixA);
        this.q = new DenseDoubleMatrix2D(matrixB);
    }

    public static DenseDoubleMatrix1D getOriginVector(DenseDoubleMatrix2D matrix) {
        int rows = matrix.rows();
        int cols = matrix.columns();

        double[] result = new double[cols];
//        for (int i = 0; i < rows; i++) {
//            for (int j = 0; j < cols; j++) {
//                result[j] += matrix.getQuick(i, j);
//            }
//        }

        DoubleMatrix1D x = matrix.viewColumn(0);
        DoubleMatrix1D y = matrix.viewColumn(1);
        DoubleMatrix1D z = matrix.viewColumn(2);

        result[0] = x.zSum() / rows;
        result[1] = y.zSum() / rows;
        result[2] = z.zSum() / rows;

//        for (int i = 0; i < cols; i++) {
//            result[i] = result[i] / rows;
//        }
        DenseDoubleMatrix1D resultmatrix = new DenseDoubleMatrix1D(result);
        return resultmatrix;
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

    public DenseDoubleMatrix2D getP() {
        return p;
    }

    public DenseDoubleMatrix2D getQ() {
        return q;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("P: ");
        sb.append(p.toStringShort());
        sb.append("\n");
        sb.append("Q: ");
        sb.append(q.toStringShort());
        sb.append("\nOrigoVektoren:\n");
        sb.append(cP.toString());
        sb.append("\n");
        sb.append(cQ.toString());
        return sb.toString();
    }
}
