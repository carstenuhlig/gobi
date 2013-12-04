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

    public Database() {
        matrices = new HashMap<String, DenseDoubleMatrix2D>();
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
        int nr = (int)(size*random);
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
}
