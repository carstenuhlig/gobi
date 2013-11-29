package data;

import java.util.HashMap;

public class Gene {

    HashMap<String, Transcript> transcripts;
<<<<<<< HEAD
    String chromosome,strand;
=======
>>>>>>> 74e87e40ab761eaf381ed6bcad1bb77fff601d7e

    public Gene() {
        transcripts = new HashMap<>();
    }

<<<<<<< HEAD
    public Gene(String transcript_id,Transcript transcript) {
        transcripts = new HashMap<>();
        transcripts.put(transcript_id,transcript);
    }

    Gene(Transcript transcript, String seqname, String transcript_id) {
        transcripts = new HashMap<>();
        transcripts.put(transcript_id, transcript);
        chromosome = seqname;
=======
    public Gene(String transcript_id, Transcript transcript) {
        transcripts = new HashMap<>();
        transcripts.put(transcript_id, transcript);
>>>>>>> 74e87e40ab761eaf381ed6bcad1bb77fff601d7e
    }

    public Transcript getTranscript(String transcript_id) {
        return transcripts.get(transcript_id);
    }

<<<<<<< HEAD
    public String getChromosome() {
        return chromosome;
    }

    public void addTranscript(String transcript_id, Transcript transcript) {
        if (transcripts.containsKey(transcript_id)) {

        } else {

=======
    public void addTranscript(String transcript_id, int start, int stop, String strand, int chromosome) {
        if (transcripts.containsKey(transcript_id)) {
            Transcript temp_transcript;
            temp_transcript = transcripts.get(transcript_id);
            int size = temp_transcript.getProtein().getNrExons();
            temp_transcript.addInformation(size, start, stop);
//            System.err.println("hier i st ein fehler: " + transcript_id);
            //TODO Exon nummer und herausfinden was da nicht stimmt
        } else {
            transcripts.put(transcript_id, new Transcript(new Protein(new Exon(new CDS(start, stop, strand))), chromosome));
        }
    }

    public void addTranscript(String transcript_id, int start, int end, String strand, int chromosome, boolean is_exon) {
        if (transcripts.containsKey(transcript_id)) {
//            Transcript temp_transcript;
//            temp_transcript = transcripts.get(transcript_id);
            System.err.println("hier ist ein fehler: " + transcript_id);
            //TODO Exon nummer und herausfinden was da nicht stimmt
        } else {
            transcripts.put(transcript_id, new Transcript(new Protein(new Exon(new CDS(strand), start, end)), chromosome));
        }
    }

    public void addTranscript(String transcript_id, int start, int end, String strand, int chromosome, boolean is_exon, boolean is_start_codon) {
        if (transcripts.containsKey(transcript_id)) {
//            Transcript temp_transcript;
//            temp_transcript = transcripts.get(transcript_id);
            System.err.println("hier ist ein fehler: " + transcript_id);
            //TODO Exon nummer und herausfinden was da nicht stimmt
        } else {
            if (is_start_codon) {
                transcripts.put(transcript_id, new Transcript(new Protein(new Exon(new CDS(strand)), start, true), chromosome));
            } else {
                transcripts.put(transcript_id, new Transcript(new Protein(new Exon(new CDS(strand)), start, false), chromosome));
            }
>>>>>>> 74e87e40ab761eaf381ed6bcad1bb77fff601d7e
        }
    }
}
