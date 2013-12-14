/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data;

import cern.colt.matrix.*;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author uhligc
 */
public class Database {

    HashMap<String, DenseDoubleMatrix2D> matrices;
    HashMap<String, DenseDoubleMatrix2D> bigmatrices; //sollen matrizen mit anderen zusätzlichen Atomen darstellen
    HashMap<String, String> sequences;
    LinkedList<String> pairs;
    LinkedList<String> pdbids;

    public Database() {
        matrices = new HashMap<>();
        sequences = new HashMap<>();
        bigmatrices = new HashMap<>();
//        pdbids = new HashSet<>(); //kommt bei setten durch import von cathscop file
//        pairs = new LinkedList<>(); //kommt bei setten durch import von cathscop file
    }

    public void addSequence(String id, String seq) {
        
        sequences.put(id, seq);
    }
    
    public void addBigMatrix(String pdbid, DenseDoubleMatrix2D a) {
        bigmatrices.put(pdbid, a);
    }
    
    public DenseDoubleMatrix2D getBigMatrix(String pdbid) {
        DenseDoubleMatrix2D bla = bigmatrices.get(pdbid);
        return (DenseDoubleMatrix2D) bla.copy();
    }

    public HashMap<String, String> getSequences() {
        return sequences;
    }

    public LinkedList<String> getPdbids() {
        return pdbids;
    }

    public void addPair(String pair) {
        pairs.add(pair);
    }

    //performance iterator über pairs mit linkedlist
    public LinkedList<String> getPairs() {
        return pairs;
    }

    public String getSequenceByID(String id) {
        return sequences.get(id);
    }

    public void setPairs(List<String> strings) {
        pairs = new LinkedList(strings);
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

    public void setPdbids(Set<String> list) {
        pdbids = new LinkedList<>(list);
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
        DoubleMatrix2D tmp = matrices.get(id).viewPart(start, 0, length, 3).copy();

        return tmp;
    }

    public int getLengthOfId(String id) {
        return matrices.get(id).rows();
    }
}
