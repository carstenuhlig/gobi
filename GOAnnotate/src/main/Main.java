package main;

import data.Database;
import data.Node;
import util.BiomartParser;
import util.ExecuteShellCommand;
import util.IO;
import util.XMLParser;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.List;
import java.util.Set;

/**
 * Created by carsten on 24.01.14.
 */
public class Main {
    public static final String SUBCLASS = "subclass";
    public static final String PARENTCLASS = "parentclass";
    //TODO implement method to get a random number of protein ids ... for testing purposes
    public static final String RANDOM = "random";
    public static final String NUMBER = "nr";
    public static final String PROTEIN = "protein";
    public static final String OUTPUT = "output";
    public static final String PROSITE = "prosite";
    public static final String PATH_SUFFIX = ".prosite";
    static String outputfile;
    static Database data;

    public static void main(String[] args) {
        if (args.length < 2) {
            System.out.println("Usage: java -jar <jar> <file-go-annotate-database> <file-biomart-database> <options>...");
            System.out.println("\t\t<options>:\n\t\t\tsubclass|parentclass\n\t\t\tprotein\n\t\t\t<go:id>\n\t\t\toutput\n\t\t\t<output-file>");
            System.exit(1);
        }

        long timestart = System.currentTimeMillis();

        String path_to_goannotate = args[0];
        String path_to_biomart = args[1];

        data = new Database();
        XMLParser.init(data);
        try {
            XMLParser.parseXML(path_to_goannotate);
        } catch (NoSuchFileException e) {
            System.err.println("Error: wrong goannotate database file");
            System.err.println("Usage: java -jar <jar> <file-go-annotate-database> <file-biomart-database> <options>...");
        } catch (IOException e) {
            System.err.println("IO-error (but found file)");
        }

        BiomartParser.init(data);
        try {
            BiomartParser.readGOTermProteinFile(path_to_biomart);
        } catch (NoSuchFileException e) {
            System.err.println("Error: wrong biomart database file");
            System.err.println("Usage: java -jar <jar> <file-go-annotate-database> <file-biomart-database>");
        } catch (IOException e) {
            System.err.println("IO-error (but found file)");
        }

        //options: decision-tree

        if (args.length > 2) {
            for (int i = 2; i < args.length; i++) {
                String s = args[i];
                if (s.equals(OUTPUT)) {
                    outputfile = args[i + 1];
                }
            }
        } else {
            System.out.println("The database contains: " + data.getNumberOfNodes() + " of nodes.");
            System.out.println("Took " + (System.currentTimeMillis() - timestart) / 1000 + " seconds.");
            System.out.println("Nothing to do.");
            System.out.println("Exit.");
            //nun exit da keine weiteren Optionen angegeben wurden.
            System.exit(0);
        }

        //args 2 - 4 for options
        switch (args[2]) {
            case SUBCLASS:
                if (args[3].equals(PROTEIN)) {
                    if (outputfile != null) {
                        try {
                            writeAllChildrenProteinsInList(args[4]);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else
                        printAllChildrenProteinsInList(args[4]);
                }
                break;
            case PARENTCLASS:
                if (args[3].equals(PROTEIN))
                    if (outputfile != null) {
                        try {
                            writeAllParentsProteinsInList(args[4]);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } else
                        printAllParentsProteinsInList(args[4]);
                break;
            default:
                System.out.println("Took " + (System.currentTimeMillis() - timestart) / 1000 + " seconds.");
                System.out.println("Nothing to do.");
        }

        if (outputfile != null) {
            System.out.println("Took " + (System.currentTimeMillis() - timestart) / 1000 + " seconds.");
            System.out.println("Exit.");
        }
    }

    /**
     * Method gets all subclasses from specified goterm and prints their protein ids to stdout
     *
     * @param goid
     */
    public static void printAllChildrenProteinsInList(String goid) {
        Set<Node> nodes = data.getAllChildrenFromID(goid);
        for (Node n : nodes) {
            if (n.hasProteins()) {
                for (String p : n.getProteins()) {
                    System.out.println(p);
                }
            }
        }
    }

    /**
     * Method get all parentclasses from specified goterm and prints their protein ids to stdout
     *
     * @param goid
     */
    public static void printAllParentsProteinsInList(String goid) {
        Set<Node> nodes = data.getAllParentsFromID(goid);
        for (Node n : nodes) {
            if (n.hasProteins()) {
                for (String p : n.getProteins()) {
                    System.out.println(p);
                }
            }
        }
    }

    /**
     * Method gets all subclasses from specified goterm and writes it to a file
     *
     * @param goid
     */
    public static void writeAllChildrenProteinsInList(String goid) throws IOException {
        IO.startBufferedWriter(outputfile);
        Set<Node> nodes = data.getAllChildrenFromID(goid);
        for (Node n : nodes) {
            if (n.hasProteins()) {
                StringBuilder sb = new StringBuilder();
                for (String p : n.getProteins()) {
                    sb.append(p + "\n");
                }
                IO.writeBufferedWriter(sb.toString());
            }
        }
        IO.closeBufferedWriter();
    }

    /**
     * Method get all parentclasses from specified goterm and writes it to a file
     *
     * @param goid
     */
    public static void writeAllParentsProteinsInList(String goid) throws IOException {
        Set<Node> nodes = data.getAllParentsFromID(goid);
        for (Node n : nodes) {
            if (n.hasProteins()) {
                StringBuilder sb = new StringBuilder();
                for (String p : n.getProteins()) {
                    sb.append(p + "\n");
                }
                IO.writeBufferedWriter(sb.toString());
            }
        }
        IO.closeBufferedWriter();
    }

    public static void convertToFasta(String filein) {
        ExecuteShellCommand.executeCommand("python scripts/converttofasta.py " + filein + " " + filein + ".fasta");
        ExecuteShellCommand.executeCommand("rm " + filein);
    }

    public static void executeProsite(String prosite_executable, String prosite_database, String sequencefile) {
        // -s parameter for only complicated patterns
        ExecuteShellCommand.executeCommand(prosite_executable + " -s -d " + prosite_database + " " + sequencefile + " > " + sequencefile + PATH_SUFFIX);
    }

    public static void processPrositeFile(String prosite_prefix_file) {
        String file = prosite_prefix_file + PATH_SUFFIX;
        //TODO processprositefile:
        //1. name von nodes mit prosite vergleichen
        //2. wenn gleich dann in liste speichern
        //3. ...
    }
}
