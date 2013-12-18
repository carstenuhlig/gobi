
import data.Genes;
import java.io.IOException;
import util.GTFParser;
import data.Gene;
import data.Protein;
import data.Transcript;
import java.util.HashMap;
import util.Isoform;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author uhligc
 */
public class Test1 {

    public static void main(String[] args) throws IOException {
        String geneid = "ENSG00000176974";
        String proteinid1 = "ENSP00000318868";
        String proteinid2 = "ENSP00000318805";

        Genes g = new Genes();
        String path = "/home/proj/biosoft/GENOMIC/HUMAN/Homo_sapiens.GRCh37.63.gtf";

        GTFParser.readFile(path, g);

        Gene gene = g.get(geneid);
//        gene.getTranscript(proteinid1);
        HashMap<String, Transcript> map = gene.getTranscripts();
        Protein p1 = g.getProtein(proteinid1);
        Protein p2 = g.getProtein(proteinid2);

//        Transcript t1 = null, t2 = null;
//        for (Map.Entry<String, Transcript> entry : map.entrySet()) {
//            String transcript_id = entry.getKey();
//            Transcript transcript = entry.getValue();
//            if (transcript.getProtein().getId().equals(proteinid1)) {
//                t1 = transcript;
//            }
//            if (transcript.getProtein().getId().equals(proteinid2)) {
//                t2 = transcript;
//            }
//        }
        Isoform isoform = new Isoform(p1, p2);
        isoform.fillSets();
        System.out.println(isoform);
    }
}
