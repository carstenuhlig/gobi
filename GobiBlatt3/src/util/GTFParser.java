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
<<<<<<< HEAD
        String regexchromosome = "^(X|Y|\\d+).*";
=======
        String regexchr = "^.*chromosome\\.(\\d+).*$";

        int chromosome = Integer.parseInt(path.getFileName().toString().replaceFirst(regexchr, "$1"));
>>>>>>> 74e87e40ab761eaf381ed6bcad1bb77fff601d7e

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
<<<<<<< HEAD
        String gene_id = null;
        // transcript_id = id für sequenz
        String transcript_id;
=======
        List<String> gene_id = new ArrayList<String>();
        List<String> transcript_id = new ArrayList<String>();
        List<Integer> exonnr = new ArrayList<Integer>();
>>>>>>> 74e87e40ab761eaf381ed6bcad1bb77fff601d7e

        while ((line = reader.readLine()) != null) {
            // split
            pieces = line.split(regexdelimit);
<<<<<<< HEAD
            
            //falls nicht chromosome enthält.. egal
            if (!pieces[0].matches(regexchromosome))
                continue;
            
=======

>>>>>>> 74e87e40ab761eaf381ed6bcad1bb77fff601d7e
            // save pieces
            seqname = pieces[0];
            source = pieces[1];
            feature = pieces[2];
            start = Integer.parseInt(pieces[3]);
            end = Integer.parseInt(pieces[4]);
<<<<<<< HEAD
            score = Integer.parseInt(pieces[5]);
            // TODO check for available strand inputs -> evtl als Character oder
            // Integer oder boolean
            strand = pieces[6];
            
            transcript_id = "";
            
            
=======
            try {
                score = Integer.parseInt(pieces[5]);
            } catch (NumberFormatException e) {
                score = -Integer.MAX_VALUE;
            }
            // TODO check for available strand inputs -> evtl als Character oder
            // Integer oder boolean
            strand = pieces[6];
>>>>>>> 74e87e40ab761eaf381ed6bcad1bb77fff601d7e
            try {
                frame = Integer.parseInt(pieces[7]);
            } catch (NumberFormatException e) {
                frame = -Integer.MAX_VALUE;
            }
            for (int i = 8; i < pieces.length; i += 2) {
                switch (pieces[i]) {
<<<<<<< HEAD
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
=======
                    case "gene_name":
                    case "gene_id":
                        gene_id.add(pieces[i + 1].replace("\"", ""));
                        break;
                    case "protein_id":
                    case "transcript_name":
                    case "transcript_id":
                        transcript_id.add(pieces[i + 1].replace("\"", ""));
                        break;
                    case "exon_number":
                        exonnr.add(Integer.parseInt(pieces[i + 1].replace("\"", "")));
                        break;
                    default:
                        System.err
                                .println("fehlende Eigenschaft gefunden. Bitte beheben!:" + pieces[i] + " " + pieces[i + 1]);
                        break;
                }
            }

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
            gene_id.clear();
            transcript_id.clear();
            exonnr.clear();

        }
>>>>>>> 74e87e40ab761eaf381ed6bcad1bb77fff601d7e
    }
}
