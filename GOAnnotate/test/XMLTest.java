import data.Database;
import util.BiomartParser;
import util.XMLParser;

import java.io.IOException;
import java.nio.file.NoSuchFileException;

/**
 * Created by uhligc on 22.01.14.
 */
public class XMLTest {

    public static void main(String[] args) throws IOException {
        Database d = new Database();
        XMLParser.init(d);

        String path_to_xml = "/home/proj/biosoft/GO/go_daily-termdb.obo-xml";
        String path_to_xml_private = "res/go_daily-termdb.obo-xml";
        String path_to_biomart = "res/mart_export.txt";

        try {
            XMLParser.parseXML(path_to_xml);
        } catch (NoSuchFileException e) {
            XMLParser.parseXML(path_to_xml_private);
        }

        BiomartParser.init(d);
        BiomartParser.readGOTermProteinFile(path_to_biomart);

//        System.out.println(d);
//        System.out.println(d.getIncompleteNodes());
        System.out.println(d.getAllChildrenFromID("GO:0016301"));
        System.out.println(d.getAllChildrenFromID("GO:0016301").size());
        System.out.println(d.hasNoNames());
        System.out.println(d.getBalanceChildrenParents());
    }
}
