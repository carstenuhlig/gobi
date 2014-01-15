import data.Database;
import util.IO;

import java.io.IOException;

/**
 * Created by uhligc on 14.01.14.
 */
public class TestAufgabe1a {
    public static void main(String[] args) throws IOException {
        Database d = new Database();
        String stringcathscop = "/home/proj/biosoft/praktikum/genprakt-ws13/assignment1/cathscop.inpairs";
        String stringtmalignments = "/home/proj/biosoft/PROTEINS/CATHSCOP/TMALIGN";
        IO.importCathScop(stringcathscop, d);
        IO.importListOfPDBIds(d);
        IO.importTMAlignmentFolder(stringtmalignments, "res/tmalignments.txt", d);
    }
}
