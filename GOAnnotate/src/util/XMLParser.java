package util;

import data.Database;
import data.Node;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by uhligc on 22.01.14.
 */
public class XMLParser {
    public static final FileSystem FS = FileSystems.getDefault();
    public static Database d = null;

    public static void init(Database d) {
        XMLParser.d = d;
    }

//    public static String getRootFolder() {
//        return ExecuteShellCommand.executeCommand("ls");
//    }

    public static void parseXML(String stringpath) throws IOException {
        Path p = FS.getPath(stringpath);
        BufferedReader r = Files.newBufferedReader(p, StandardCharsets.UTF_8);

        //vars
        String line;

        String id = null;
        String name = null;
        HashSet<String> subclasses = new HashSet<>();

        String reg_id = "^\\s*<id>(\\S+)</id>$";
        String reg_name = "^\\s*<name>(.+)</name>$";
        String reg_subclass = "^\\s*<is_a>(\\S+)</is_a>$";

        Pattern p_id = Pattern.compile(reg_id);
        Pattern p_name = Pattern.compile(reg_name);
        Pattern p_subclass = Pattern.compile(reg_subclass);

        Matcher m_id = p_id.matcher("");
        Matcher m_name = p_name.matcher("");
        Matcher m_subclass = p_subclass.matcher("");

        while ((line = r.readLine()) != null) {
            // read lines here
            if (line.contains("</term>")) {

                if (id != null)
                    addKnode(id, name, subclasses);
                id = null;
                name = null;
                subclasses = new HashSet<>();
            } else {
                if (m_id.reset(line).matches()) {
                    id = m_id.replaceAll("$1");
                } else if (m_name.reset(line).matches()) {
                    name = m_name.replaceAll("$1");
                } else if (m_subclass.reset(line).matches()) {
                    subclasses.add(m_subclass.replaceAll("$1"));
                }
            }
        }

        r.close();
    }

    public static void addKnode(String id, String name, HashSet<String> parentids) {
        LinkedList<Node> parentnodes = new LinkedList<>();

        Node thisnode = d.getNode(id);
        if (thisnode == null) {
            thisnode = new Node(id, name);
        } else
            thisnode.setName(name);
        for (String parentid : parentids) {
            Node tmp = d.getNode(parentid);
            if (tmp == null) {
                //wenn parent noch nicht vorhanden
                tmp = new Node(parentid, name, thisnode);
                parentnodes.add(tmp);
                d.addNode(tmp);
            } else {
                //wenn parent schon vorhanden
                parentnodes.add(tmp);
                d.addChild(parentid, thisnode);
            }
        }
        thisnode.addParents(parentnodes);
        d.addNode(thisnode);
    }
}
