import data.Genes;
import util.GTFParser;
import util.SpliceEvent;

import java.io.IOException;

/**
 * Created by carsten on 02.02.14.
 */
public class SpliceTest {
    public static void main(String[] args) throws IOException {
        Genes g = new Genes();
        String path = "res/gtf_modified.txt";
        String en1 = "ENSP00000383125";
        String en2 = "ENSP00000373083";

        GTFParser.readFile(path, g);

        SpliceEvent se = new SpliceEvent(g.getProtein(en1), g.getProtein(en2));
        System.out.println(se.toString());
    }
}
