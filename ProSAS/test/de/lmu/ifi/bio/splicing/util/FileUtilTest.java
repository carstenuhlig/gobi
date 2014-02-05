package de.lmu.ifi.bio.splicing.util;

import java.io.File;
import java.io.IOException;

import org.junit.Test;

import de.lmu.ifi.bio.splicing.genome.Gene;

public class FileUtilTest {

    @Test
    public void testReadGene() throws IOException {
        //55934; end: 56141
        File gtfFile = new File("/home/proj/biosoft/GENOMIC/HUMAN/Homo_sapiens.GRCh37.73.gtf");

        Gene gene = new FileUtil.GTFReader(gtfFile).setStartOffset(388612708l).setEndOffset(388661825l).getGene();
        System.out.println(gene.getTranscripts().size());
        System.out.println(gene.getTranscripts().get(0) + " " + gene.getTranscripts().get(0).getProtein().getCodingExons());
    }

}
