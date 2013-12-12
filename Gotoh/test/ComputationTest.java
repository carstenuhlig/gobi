
import data.Matrix;
import data.Raw;
import main.Computation;
import static main.Main.m;
import util.ImportFile;
import util.Type;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author Carsten
 */
public class ComputationTest extends Init{

    public static void main(String[] args) {
        ImportFile.readDir("res\\matrices", m, r);
        Computation.init("WTHA", "WTHGQA", m.getSubstitutionMatrix("BLOSUM50"),
                m.getCharMap("BLOSUM50"), -10.0, -2.0, Type.GLOBAL,
                "Gobiepraesentation1", "Gobiepraesentation2", 3, 3);
//        m.printSubstitutionMatrixByName("BLOSUM50");
        Computation.calcMatrices();
        Computation.backtrack();
        System.out.println(Computation.toStringDebug());
        // m.printAllSubstitionMatrices();

        System.out.println("nun großer Test");

        System.out.println();

        Computation.saveMatrices(m);
        Computation.saveAlignment(m);
        m.printAlignment("Gobiepraesentation1:Gobiepraesentation2");
        m.printAllCalculatedMatrices();
    }
}
