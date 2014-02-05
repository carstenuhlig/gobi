package main;

import data.Gen;
import data.Gene;
import data.Genes;
import data.Transcript;
import util.GTFParser;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GenServiceImpl implements GenService {

    //data model
    private List<Gen> genList = new LinkedList<Gen>();
    private static int id = 1;
    private String message;

    //initialize book data
    public GenServiceImpl() {
        Genes genes = new Genes();
        String path = "/Volumes/SSD/git/gobi/out/compiled/artifacts/ZKOSS_war_exploded/gtf_modified.txt";

//        message = "something";
        try {
            GTFParser.readFile(path, genes);
//            System.out.println("File was read");
            message = "Alles gut gelaufen.";
        } catch (IOException e) {
            message = path + e.toString();
            e.printStackTrace();
        }

        for (Map.Entry<String, Gene> geneEntry : genes.getGenes().entrySet()) {
            String geneid = geneEntry.getKey();
            Gene gene = geneEntry.getValue();
            for (Map.Entry<String, Transcript> transcriptEntry : gene.getTranscripts().entrySet()) {
                String transcriptid = transcriptEntry.getKey();
                Transcript transcript = transcriptEntry.getValue();
                Long start = transcript.getProtein().getStartPosition();
                Long stop = transcript.getProtein().getStopPosition();
                genList.add(new Gen(id++, geneid, transcriptid, transcript.getChromsome(), start, stop));
            }
        }
    }

    public List<Gen> findAll() {
        return genList;
    }

    public String getStatus() {
        return message;
    }

    public List<Gen> search(String keyword) {
        List<Gen> result = new LinkedList<Gen>();
        if (keyword == null || "".equals(keyword)) {
            result = genList;
        } else {
            for (Gen c : genList) {
                if (c.getGeneid().toLowerCase().contains(keyword.toLowerCase())) {
                    result.add(c);
                }
            }
        }
        return result;
    }
}
