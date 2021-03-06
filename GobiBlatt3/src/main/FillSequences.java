/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import data.*;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import static main.FillSequences.FS;
import util.GenomeSequenceExtractor;
import util.GenomicUtils;

/**
 *
 * @author uhligc
 */
public class FillSequences {

    Genes g;
    final static String default_prefix = "/home/proj/biosoft/GENOMIC/HUMAN/HUMAN_GENOME_FASTA/Homo_sapiens.GRCh37.63.dna.chromosome.";
    final static FileSystem FS = FileSystems.getDefault();
    static HashMap<String, Boolean> map;

    public static void fillSequences(Genes g) throws IOException {
        GenomeSequenceExtractor gse = new GenomeSequenceExtractor();

        gse.init(default_prefix);

        HashMap<String, Gene> genes = g.getGenes();
        int size = g.getHashMapSize();
        System.out.println(size);
        int counter = 0;
        for (Map.Entry<String, Gene> entry : genes.entrySet()) {
            String gene_id = entry.getKey();
            Gene gene = entry.getValue();
            HashMap<String, Transcript> transcripts = gene.getTranscripts();
            for (Map.Entry<String, Transcript> entry1 : transcripts.entrySet()) {
                String transcript_id = entry1.getKey();
                Transcript transcript = entry1.getValue();
                for (int i = 0; i < transcript.getProtein().getNrExons(); i++) {
                    CDS tmp = transcript.getProtein().getExon(i).getCDS();
                    tmp.setSeq(GenomeSequenceExtractor.easySearch(transcript.getChromsome(), tmp.getStart(), tmp.getStop()));
                    transcript.getProtein().getExon(i).setCds(tmp);
                }
            }
            System.out.print(counter / size);
            if (size % (size / 1000) == 0) {
                System.out.println();
            }
        }
        gse.close();
    }

    public static String getProteinSequence(String proteinid, Genes g) throws IOException {
        StringBuilder sb = new StringBuilder();
        Protein protein = g.getProtein(proteinid);
        for (Exon exon : protein.getExons()) {
            CDS tmp = exon.getCDS();
            if ((tmp.getStop() - tmp.getStart() - tmp.getFrame()) >= 3) {
                String seq = GenomeSequenceExtractor.easySearch(protein.getChromosome(), tmp.getStart(), tmp.getStop());
                sb.append(GenomicUtils.convertToAA(seq, tmp.getStrand(), tmp.getFrame()));
            }
        }
        return sb.toString();
    }

    //TODO in aminosäuresequenz
    public static void writeSequences(Genes g, String path_seqlibfile, String path_pairsfile, String path_reference_seqlibfile) throws IOException {
//        GenomeSequenceExtractor gse = new GenomeSequenceExtractor();

//        gse.init(default_prefix);
        Path seqlibfile = FS.getPath(path_seqlibfile);
        Path pairsfile = FS.getPath(path_pairsfile);
        Path reference = FS.getPath(path_reference_seqlibfile);

        BufferedWriter writer1 = Files.newBufferedWriter(seqlibfile, StandardCharsets.UTF_8);
        BufferedWriter writer2 = Files.newBufferedWriter(pairsfile, StandardCharsets.UTF_8);

        initHashMap(path_reference_seqlibfile);
//        BufferedReader reader1 = Files.newBufferedReader(reference, StandardCharsets.UTF_8);

        HashMap<String, Gene> genes = g.getGenes();
        int size = g.getHashMapSize();
        System.out.println(size);
        int counter = 0;
        for (Map.Entry<String, Gene> entry : genes.entrySet()) {
            String gene_id = entry.getKey();
            Gene gene = entry.getValue();
            HashMap<String, Transcript> transcripts = gene.getTranscripts();
            for (Map.Entry<String, Transcript> entry1 : transcripts.entrySet()) {
                String transcript_id = entry1.getKey();
                Transcript transcript = entry1.getValue();
                String protein_id = transcript.getProtein().getId();

                if (findGene(protein_id)) {
                    StringBuilder sb = new StringBuilder();

                    int size_exons = transcript.getProtein().getNrExons();
                    for (int i = 0; i < size_exons; i++) {
                        CDS tmp = transcript.getProtein().getExon(i).getCDS();
                        if ((tmp.getStop() - tmp.getStart() - tmp.getFrame()) >= 3) {
                            String seq = GenomeSequenceExtractor.easySearch(transcript.getChromsome(), tmp.getStart(), tmp.getStop());
                            sb.append(GenomicUtils.convertToAA(seq, tmp.getStrand(), tmp.getFrame()));
                        }
                    }

                    if (sb.length() > 0) {
                        if (transcript.getProtein().getExon(0).getCDS().getStrand().equals("-")) {
                            writer1.write(protein_id + "-:" + sb.toString() + "\n");
                            writer2.write(protein_id + " " + protein_id + "-\n");
                        } else {
                            writer1.write(protein_id + "D:" + sb.toString() + "\n");
                            writer2.write(protein_id + " " + protein_id + "D\n");
                        }
                    }
                }
            }

            if (counter % (size
                    / 1000) == 0) {
                System.out.print((counter * 100) / size);
            }

            counter++;
        }

//        gse.close();
        writer1.close();

        writer2.close();
//        reader1.close();
    }

    public static void getGenesForHashMap(String path) throws IOException {
        RandomAccessFile raf = new RandomAccessFile(path, "r");
        String line;
        raf.seek(0);
        while ((line = raf.readLine()) != null) {
            map.put(line.split(":")[0], true);
        }
        raf.close();
    }

    public static void initHashMap(String path) throws IOException {
        map = new HashMap<String, Boolean>();
        getGenesForHashMap(path);
    }

    public static Boolean findGene(String protein_id) {
        if (map.containsKey(protein_id)) {
            return true;
        } else {
            return false;
        }
    }
}
