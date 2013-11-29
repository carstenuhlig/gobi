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
        String source;
        String feature;
        int start;
        int end;
        int score;
        String strand;
        int frame;
        String gene_id = null;
        // transcript_id = id für sequenz
        String transcript_id;

        while ((line = reader.readLine()) != null) {
            // split
            pieces = line.split(regexdelimit);
            //falls nicht chromosome enthält.. egal
            if (!pieces[0].matches(regexchromosome))
                continue;
            // save pieces
            seqname = pieces[0];
            source = pieces[1];
            feature = pieces[2];
            start = Integer.parseInt(pieces[3]);
            end = Integer.parseInt(pieces[4]);
            // TODO check for available strand inputs -> evtl als Character oder
            // Integer oder boolean
            strand = pieces[6];
            
            transcript_id = "";
            // TODO check for available strand inputs -> evtl als Character oder
            // Integer oder boolean
            strand = pieces[6];
            try {
                frame = Integer.parseInt(pieces[7]);
            } catch (NumberFormatException e) {
                frame = -Integer.MAX_VALUE;
            }
            for (int i = 8; i < pieces.length; i += 2) {
                switch (pieces[i]) {
                    case "gene_id":
                        gene_id = pieces[i + 1];
                        break;
                    case "transcript_id":
                        transcript_id = pieces[i + 1];
                        break;
                    default:
                        System.err.println("fehlende Eigenschaft gefunden. Bitte beheben!");
                        break;
                }
            }
            
            genes.addGene(gene_id, transcript_id, seqname, strand, start, end);
        }
        reader.close();

            // save in database
            // TODO
            if (!gene_id.isEmpty() && !transcript_id.isEmpty()) {
                switch (pieces[2]) {
                    case "CDS":
                        genes.addGene(gene_id, transcript_id, start, end, chromosome, strand);
                        break;
                    case "exon":
                        genes.addGene(gene_id, transcript_id, start, end, chromosome, strand, true);
                        break;
                    case "start_codon":
                        genes.addGene(gene_id, transcript_id, start, end, chromosome, strand, true, true);
                        break;
                    case "stop_codon":
                        genes.addGene(gene_id, transcript_id, start, end, chromosome, strand, true, false);
                        break;
                    default:
                        System.err.print("Found undefined feature: " + pieces[2]);
                        break;
                }

            }

//            System.out.print("");
            // reset values
            frame = -Integer.MAX_VALUE;
            score = -Integer.MAX_VALUE;
        }
    }
}
