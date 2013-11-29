package util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import data.Genes;

public class GTFParser {

    private static final FileSystem FS = FileSystems.getDefault();

    public static void readFile(String p, Genes genes) throws IOException {
        Path path = FS.getPath(p);

        BufferedReader reader = Files.newBufferedReader(path,
                StandardCharsets.UTF_8);

        String regexdelimit = "\\s+|;\\s*";
        String regexchromosome = "^(X|Y|\\d+).*";
        String regexchr = "^.*chromosome\\.(\\d+).*$";

        String line;
        String[] pieces;
        String seqname;
        String protein_id = null;
        String source;
        String feature;
        int start;
        int end;
        int frame;
        String strand;
        String gene_id = null;
        // transcript_id = id für sequenz
        String transcript_id = null;

        while ((line = reader.readLine()) != null) {
            // split
            pieces = line.split(regexdelimit);
            //falls nicht chromosome enthält.. egal
            if (!pieces[0].matches(regexchromosome)) {
                continue;
            }
            
            if (!pieces[2].equals("CDS")) {
                continue;
            }
            
            // save pieces
            seqname = pieces[0];
            source = pieces[1];
            feature = pieces[2];
            start = Integer.parseInt(pieces[3]);
            end = Integer.parseInt(pieces[4]);
            // TODO check for available strand inputs -> evtl als Character oder
            // Integer oder boolean
            strand = pieces[6];

            // TODO check for available strand inputs -> evtl als Character oder
            // Integer oder boolean
            strand = pieces[6];

            frame = Integer.parseInt(pieces[7]);
            
            for (int i = 8; i < pieces.length; i += 2) {
                switch (pieces[i]) {
                    case "gene_id":
                        gene_id = pieces[i + 1].replace("\"", "");
                        break;
                    case "transcript_id":
                        transcript_id = pieces[i + 1].replace("\"", "");
                        break;
                    case "protein_id":
                        protein_id = pieces[i + 1].replace("\"", "");
                        break;
                    default:
//                      System.err.println("fehlende Eigenschaft gefunden. Bitte beheben!");
                        break;
                }
            }

            genes.addGene(protein_id, gene_id, transcript_id, seqname, strand, start, end, frame);
        }
        reader.close();

        // save in database
        // TODO
//            System.out.print("");
        // reset values
//        frame = -Integer.MAX_VALUE;
//        score = -Integer.MAX_VALUE;
    }
}
