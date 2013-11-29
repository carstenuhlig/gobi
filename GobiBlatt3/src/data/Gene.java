package data;

import java.util.HashMap;
import java.util.Map;

public class Gene {

    HashMap<String, Transcript> transcripts;

    public Gene(String transcript_id, Transcript transcript) {
        transcripts = new HashMap<>();
        transcripts.put(transcript_id, transcript);
    }

    public Transcript getTranscript(String transcript_id) {
        return transcripts.get(transcript_id);
    }

    public void addTranscript(String transcript_id, Transcript transcript) {
        if (transcripts.containsKey(transcript_id)) {
            Transcript tmp = transcripts.get(transcript_id);
            tmp.addInformation(transcript.getProtein().getExon(0));
        } else {
            transcripts.put(transcript_id, transcript);
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, Transcript> entry : transcripts.entrySet()) {
            String transcript_id = entry.getKey();
            Transcript transcript = entry.getValue();
            sb.append("TranscriptID = ");
            sb.append(transcript_id);
            sb.append("\n");
            sb.append(transcript.toString());
            sb.append("\n");
        }
        return sb.toString();
    }

    public HashMap<String, Transcript> getTranscripts() {
        return transcripts;
    }

    
}
