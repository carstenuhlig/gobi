package main;

import data.Gen;
import data.Gene;
import data.Genes;
import util.GTFParser;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class GenServiceImpl implements GenService {

    //data model
    private List<Gen> genList = new LinkedList<Gen>();
    private static int id = 1;

    //initialize book data
    public GenServiceImpl() {
        Genes genes = new Genes();
        String path = "res/gtf_modified.txt";

        try {
            GTFParser.readFile(path, genes);
        } catch (IOException e) {
            e.printStackTrace();
        }

        HashMap<String, Gene> hashmap = genes.getGenes();

        for (Map.Entry<String, Gene> geneEntry : hashmap.entrySet()) {
            String geneid = geneEntry.getKey();
            Gene geneValue = geneEntry.getValue();
            genList.add(new Gen(id++, geneid, geneValue.getTranscripts().size()));
        }
    }

    public List<Gen> findAll() {
        return genList;
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
