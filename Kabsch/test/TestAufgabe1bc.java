import cern.colt.matrix.impl.DenseDoubleMatrix2D;
import data.Alignment;
import data.Database;
import kabsch.Kabsch;
import util.IO;
import util.Matrix;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Carsten on 16.01.14.
 */
public class TestAufgabe1bc {
    final static String path_to_pdblist = "/home/proj/biosoft/PROTEINS/PDB_REP_CHAINS/TMSIM/1bj4.A.simlist";
    final static String outputfile0 = "res/pdb.pdb";
    final static String outputfile1 = "res/pdbone.pdb";
    final static String outputfile2 = "res/pdbtwo.pdb";

    public static void main(String[] args) throws IOException {
        Database d = new Database();
        List<String> mylist = IO.readSimList(path_to_pdblist);
        HashMap<String, Alignment> alignmentHashMap = new HashMap<>();
        String template_pdbid = "1bj4.A";
        Alignment tmp = null;
        Alignment first = null;
        Alignment second = null;
        double firstscore = 0.;
        double secondscore = 0.;

        for (String pdbid : mylist) {
            tmp = IO.readManualTMalignment(template_pdbid, pdbid);
            d.addAlignment(template_pdbid + " " + pdbid, tmp);
            if (tmp.getTmscore() > firstscore) {
                secondscore = firstscore;
                second = first;
                firstscore = tmp.getTmscore();
                first = tmp;
            } else if (tmp.getTmscore() > secondscore) {
                second = tmp;
                secondscore = second.getTmscore();
            }
            alignmentHashMap.put(pdbid, tmp);
            //IO.readPDBFile(pdbid, d, true); //true steht für nicht cathscop structures
        }

        //nun ersten beiden pairs importen
        //template pdb
        IO.readPDBFile(template_pdbid, d, true);
        IO.readPDBFileWhole(template_pdbid, d, true);
        //first score pdb
        IO.readPDBFile(first.getId2(), d, true);
        IO.readPDBFileWhole(first.getId2(), d, true);
        //second score pdb
        IO.readPDBFile(second.getId2(), d, true);
        IO.readPDBFileWhole(second.getId2(), d, true);

        //Kabsch
        DenseDoubleMatrix2D[] red_mat = Matrix.processMatrices(template_pdbid, first.getId2(), d);
        Kabsch k = new Kabsch(red_mat);
        k.main();
        DenseDoubleMatrix2D wholeprocessed = k.processWholeStructure(d.getBigMatrix(first.getId2()));
        IO.exportToPDB(d, first.getId2(), outputfile0, wholeprocessed, template_pdbid + " " + first.getId2(), 2);

        red_mat = Matrix.processMatrices(template_pdbid, second.getId2(), d);
        k = new Kabsch(red_mat);
        k.main();
        wholeprocessed = k.processWholeStructure(d.getBigMatrix(second.getId2()));
        IO.exportToPDB(d, second.getId2(), outputfile2, wholeprocessed, template_pdbid + " " + second.getId2(), 2);
    }

}
