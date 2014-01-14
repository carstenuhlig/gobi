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
        IO.importCathScop(stringcathscop, d);
        IO.importListOfPDBIds(d);
        IO.processAlignmentFile(args[0], args[1], d);
    }
}
