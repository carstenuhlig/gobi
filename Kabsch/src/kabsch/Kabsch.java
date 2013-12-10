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
import cern.colt.matrix.linalg.SingularValueDecomposition;
import cern.jet.math.Functions;

/**
 *
 * @author uhligc
 */
public class Kabsch {

    //p und q origin stehen für unberührte koordinaten für spätere rmsd berechnung
    DenseDoubleMatrix2D p, q, pDasOriginal, qDasOriginal;
    DoubleMatrix2D a, s, v, u, r;
    DoubleMatrix1D t;
    DenseDoubleMatrix1D cP, cQ;

    double e0, esvd, rmsd, ermsd;

    final static Functions F = Functions.functions;
    final static Algebra A = new Algebra();

    public Kabsch(double[][] matrixA, double[][] matrixB) {
        init(matrixA, matrixB);
    }

    public Kabsch(DenseDoubleMatrix2D p, DenseDoubleMatrix2D q) {
        this.p = p;
        this.q = q;
        pDasOriginal = (DenseDoubleMatrix2D) this.p.copy();
        qDasOriginal = (DenseDoubleMatrix2D) this.q.copy();
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
        //transpose
        DoubleMatrix2D tcP = p.viewDice();
        //multiplikation der beiden matrizen
        a = A.mult(tcP, q);
    }

    private void calcRotation() {
        //svd calculate
        SingularValueDecomposition svd = new SingularValueDecomposition(a);
        s = svd.getS();
        v = svd.getV();
        u = svd.getU();

        //check right handed coord system
        double d = A.det(v) * A.det(u);
        double[][] dmatrix = {{1, 0, 0}, {0, 1, 0}, {0, 0, d}};
        DoubleMatrix2D dmat = new DenseDoubleMatrix2D(dmatrix);

        //s und u multipliziert mit 1,0,0 0,1,0 0,0,d
        s = A.mult(s, dmat);
        u = A.mult(u, dmat);

        //error von s ausrechnen... keine ahnung was das sein soll.
        esvd = calcSVDError(s);
        calcRMSDError();

        //rotation matrix berechnen
        r = A.mult(u, A.transpose(v));
    }

    public void main() {
        //P-> cP; Q-> cQ
        translate();

        //A = P^T * Q
        calcCovarianceMatrix();

        //rotationsmatrix bestimmen
        calcRotation();

        //proteinstrukturen rotieren
//        rotateStructures();
        manualRotateStructures();

        //rmsd berechnen
        calcRMSD();
    }

    public static DenseDoubleMatrix2D applyVector(DenseDoubleMatrix2D matrix, DenseDoubleMatrix1D vector) {
        int rows = matrix.rows();
        int cols = matrix.columns();
        if (cols != vector.size()) {
            return null;
        }

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix.set(i, j, matrix.get(i, j) - vector.get(j));
            }
        }

        return matrix;
    }

    public static void applyVectorFast(DenseDoubleMatrix2D matrix, DenseDoubleMatrix1D vector) {
        int cols = matrix.columns();
        if (cols != vector.size()) {
            return;
        }

        for (int i = 0; i < vector.size(); i++) {
            matrix.viewColumn(i).assign(F.minus(vector.get(i)));
        }

//        return matrix;
    }

    private void calcRMSDError() {
        ermsd = Math.sqrt((Math.abs(e0 - 2 * esvd) / ((p.rows() + q.rows()) / 2)));
    }

    private void rotateStructures() {
        cQ.assign(F.neg);
        t = (A.mult(r.viewDice(), cQ)).assign(cP, F.plus);
        int rows = q.rows();

        for (int i = 0; i < rows; i++) {
            qDasOriginal.viewRow(i).assign((A.mult(r, qDasOriginal.viewRow(i))).assign(t, F.plus));
        }
    }

    private void manualRotateStructures() {
        //TODO implement easy version ( rotateStructures())
        DoubleMatrix1D tmp = A.mult(r, cQ);
        
        tmp.assign(F.neg);
        
        tmp.assign(cP,F.plus);
        
//        cP.assign(t, F.min);
        int rows = qDasOriginal.rows();

        r = r.viewDice();

        for (int i = 0; i < rows; i++) {
            double x = qDasOriginal.get(i, 0);
            double y = qDasOriginal.get(i, 1);
            double z = qDasOriginal.get(i, 2);

            double tx = x * r.get(0, 0) + y * r.get(1, 0) + z * r.get(2, 0) + tmp.get(0);
            double ty = x * r.get(0, 1) + y * r.get(1, 1) + z * r.get(2, 1) + tmp.get(1);
            double tz = x * r.get(0, 2) + y * r.get(1, 2) + z * r.get(2, 2) + tmp.get(2);

            qDasOriginal.set(i, 0, tx);
            qDasOriginal.set(i, 1, ty);
            qDasOriginal.set(i, 2, tz);
        }
    }

    public static double calcInitError(DenseDoubleMatrix2D p, DenseDoubleMatrix2D q) {
        return p.aggregate(F.plus, F.square) + q.aggregate(F.plus, F.square);
    }

    public static double calcSVDError(DoubleMatrix2D s) {
        double result = s.get(0, 0);
        result += s.get(1, 1);
        return result + s.get(2, 2);
    }

    public void init(double[][] matrixA, double[][] matrixB) {
        this.p = new DenseDoubleMatrix2D(matrixA);
        this.q = new DenseDoubleMatrix2D(matrixB);
    }

//    private void calcRMSD() {
//        rmsd = Scores.getRMSD(p, q);
//    }
    private void calcRMSD() {
        rmsd = Scores.getRMSD(pDasOriginal, qDasOriginal);
    }

    public static DenseDoubleMatrix1D getOriginVector(DenseDoubleMatrix2D matrix) {
        int rows = matrix.rows();
        int cols = matrix.columns();

        double[] result = new double[cols];

        DoubleMatrix1D x = matrix.viewColumn(0);
        DoubleMatrix1D y = matrix.viewColumn(1);
        DoubleMatrix1D z = matrix.viewColumn(2);

        result[0] = x.zSum() / rows;
        result[1] = y.zSum() / rows;
        result[2] = z.zSum() / rows;

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

    public double getRmsd() {
        return rmsd;
    }

    public double getErmsd() {
        return ermsd;
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
