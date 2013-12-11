/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import cern.colt.matrix.*;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author uhligc
 */
public class Database {

    HashMap<String, DenseDoubleMatrix2D> matrices;
    HashMap<String, String> sequences;

    public Database() {
        matrices = new HashMap<String, DenseDoubleMatrix2D>();
        sequences = new HashMap<>();
    }

    public void addSequence(String id, String seq) {
        sequences.put(id, seq);
    }

    public String getSequenceByID(String id) {
        return sequences.get(id);
    }

    public void addMatrix(String id, DenseDoubleMatrix2D inputmatrix) {
        matrices.put(id, inputmatrix);
    }

    public DenseDoubleMatrix2D getMatrix(String id) {
        return matrices.get(id);
    }

    public String viewMatrix(String id) {
        DenseDoubleMatrix2D matrix = matrices.get(id);
        return matrix.toString();
    }

    public DenseDoubleMatrix2D getRandomMatrix() {
        double random = Math.random();
        int size = matrices.size();
        int nr = (int) (size * random);
        int counter = 0;
        for (Map.Entry<String, DenseDoubleMatrix2D> entry : matrices.entrySet()) {
            String string = entry.getKey();
            DenseDoubleMatrix2D denseDoubleMatrix2D = entry.getValue();
            if (counter == nr) {
                return denseDoubleMatrix2D;
            }
            counter++;
        }
        return null;
    }

    public DoubleMatrix2D getMatrixPiece(String id, int start, int length) throws NullPointerException, IndexOutOfBoundsException {
//        if (!matrices.containsKey(id)) {
//            return null;
//        }
        DoubleMatrix2D tmp = matrices.get(id).viewPart(start, 0, length, 3).copy();

        return tmp;
    }

    public int getLengthOfId(String id) {
        return matrices.get(id).rows();
    }
}
