package data;

import java.io.Serializable;
import java.util.*;

public class Gene implements Serializable {

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

    public Transcript getRandomTranscript() {
        Random rnd = new Random();
        int toget = rnd.nextInt(transcripts.size());
        int i = 0;
        for (Transcript transcript : transcripts.values()) {
            if (i == toget)
                return transcript;
            i++;
        }
        return transcripts.values().iterator().next();
    }

    public boolean uniqueExons() {
        for (Transcript transcript : transcripts.values()) {
            if (!transcript.uniqueExons())
                return false;
        }
        return true;
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
