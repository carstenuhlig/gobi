package data;

import java.util.HashMap;

public class Gene {

    HashMap<String, Transcript> transcripts;

    public Gene() {
        transcripts = new HashMap<>();
    }

    public Gene(String transcript_id, Transcript transcript) {
        transcripts = new HashMap<>();
        transcripts.put(transcript_id, transcript);
    }

    public Transcript getTranscript(String transcript_id) {
        return transcripts.get(transcript_id);
    }

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
        }
    }
}
