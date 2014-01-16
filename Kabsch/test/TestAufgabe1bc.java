import data.Alignment;
import data.Database;
import util.IO;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Carsten on 16.01.14.
 */
public class TestAufgabe1bc {
    final static String path_to_pdblist = "/home/proj/biosoft/PROTEINS/PDB_REP_CHAINS/TMSIM/1bj4.A.simlist";

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

    }

}
