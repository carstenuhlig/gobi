import data.Database;
import util.XMLParser;

import java.io.IOException;

/**
 * Created by uhligc on 22.01.14.
 */
public class XMLTest {

    public static void main(String[] args) throws IOException {
        Database d = new Database();
        XMLParser.init(d);

        String path_to_xml = "/home/proj/biosoft/GO/go_daily-termdb.obo-xml";
        XMLParser.parseXML(path_to_xml);

        System.out.println(d.getAllChildrenFromID("GO:0016301").size());
    }
}
