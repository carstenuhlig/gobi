package data;

import java.util.HashMap;

public class Gene {
	HashMap<String,Transcript> transcripts;

	public Gene () {
		transcripts = new HashMap<>();
	}

	public Transcript getTranscript (String transcript_id) {
		return transcripts.get(transcript_id);
	}
}
