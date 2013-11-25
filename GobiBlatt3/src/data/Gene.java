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

    public void addTranscript(String transcript_id, int start, int end, String strand) {
        if (transcripts.containsKey(transcript_id)) {
            Transcript temp_transcript;
            temp_transcript = transcripts.get(transcript_id);
            System.err.println("hier ist ein fehler: " + transcript_id);
        } else {
            transcripts.put(transcript_id, new Transcript(new Protein(new Exon(new CDS(), start, end))));
        }
    }
}
