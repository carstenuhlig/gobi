/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import data.CDS;
import data.Gene;
import data.Genes;
import data.Transcript;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import util.GenomeSequenceExtractor;

/**
 *
 * @author uhligc
 */
public class FillSequences {

    Genes g;
    final static String default_prefix = "/home/proj/biosoft/GENOMIC/HUMAN/HUMAN_GENOME_FASTA/Homo_sapiens.GRCh37.63.dna.chromosome.";

    public static void fillSequences(Genes g) throws IOException {
        GenomeSequenceExtractor gse = new GenomeSequenceExtractor();

        gse.init(default_prefix);

        HashMap<String, Gene> genes = g.getGenes();

        for (Map.Entry<String, Gene> entry : genes.entrySet()) {
            String gene_id = entry.getKey();
            Gene gene = entry.getValue();
            HashMap<String, Transcript> transcripts = gene.getTranscripts();
            for (Map.Entry<String, Transcript> entry1 : transcripts.entrySet()) {
                String transcript_id = entry1.getKey();
                Transcript transcript = entry1.getValue();
                for (int i = 0; i < transcript.getProtein().getNrExons(); i++) {
                    CDS tmp = transcript.getProtein().getExon(i).getCDS();
                    tmp.setSeq(gse.easySearch(transcript.getChromsome(), tmp.getStart(), tmp.getStop()));
//                    System.out.println(".");
                }
            }
        }

        gse.close();
    }
}
