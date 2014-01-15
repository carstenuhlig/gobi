package main;

import cern.colt.matrix.impl.DenseDoubleMatrix1D;
import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import data.Alignment;
import data.Database;
import kabsch.Kabsch;
import util.IO;
import util.Matrix;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by uhligc on 15.01.14.
 */
public class MultipleKabsch {
    String template_pdbid;
    DenseDoubleMatrix2D template;
    LinkedList<String> pdbids;
    HashMap<String, Alignment> alignments;
    HashMap<String, Kabsch> kabsches;
    String string_inception;

    Database d;

    public MultipleKabsch(String template_pdbid, List<String> pdbids, HashMap<String, Alignment> alis, Database d) {
        this.template_pdbid = template_pdbid;
        this.pdbids = (LinkedList) pdbids;
        this.d = d;
        this.alignments = alis;
        init();
    }

    private void init() {
        kabsches = new HashMap<>();
        importStructures();
        calcKabsch();
    }

    private void importStructures() {
        if (!d.hasMatrix(template_pdbid)) {
            IO.readPDBFileWhole(template_pdbid, d);
            IO.readPDBFile(template_pdbid, d);
        }

        for (String pdbid : pdbids) {
            if (!d.hasMatrix(pdbid)) {
                IO.readPDBFile(pdbid, d);
                IO.readPDBFileWhole(pdbid, d);
            }
        }
    }

    private void calcKabsch() {
        int sizeTemplateMatrix = d.getMatrix(template_pdbid).rows();
        for (String pdbid : pdbids) {
            DenseDoubleMatrix2D tmptemplatematrix = d.getMatrix(template_pdbid);
            DenseDoubleMatrix2D tmppdbidmatrix = d.getMatrix(pdbid);
            int size_q = tmppdbidmatrix.rows();
            Alignment ali = d.getAlignment(template_pdbid + " " + pdbid);
            DenseDoubleMatrix2D[] reducedMatrices = Matrix.processMatrices(tmptemplatematrix, tmppdbidmatrix, alignments.get(pdbid).getOne(), alignments.get(pdbid).getTwo(), d, template_pdbid, pdbid);
            Kabsch k = new Kabsch(reducedMatrices[0], reducedMatrices[1]);
            k.main();
            //k.getGDTProtein(sizeTemplateMatrix, size_q);
            kabsches.put(pdbid, k);
        }
    }

    public void exportToPdbFile(String pdbid, String stringpath) {
        if (pdbid.equals(template_pdbid)) {
            IO.exportToPDB(d, template_pdbid, stringpath);
            return;
        }
        Kabsch tmp = kabsches.get(pdbid);
        DenseDoubleMatrix2D dingdong = tmp.processWholeStructure(d.getBigMatrix(pdbid));
        IO.exportToPDB(d, pdbid, stringpath, dingdong, template_pdbid + " " + pdbid, 2);
    }
}
