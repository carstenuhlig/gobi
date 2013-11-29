package data;

import java.util.HashMap;

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

		} else {
			
		}
	}
}
