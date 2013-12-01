package data;

import java.util.HashMap;
import java.util.LinkedList;

public class Raw {
	//PERFORMANCE HashMap for Sequences
//	public ArrayList<Seq> sequences = new ArrayList<Seq>();
	//PERFORMANCE LinkedList
//	public ArrayList<DoubleID> pairs =	 new ArrayList<DoubleID>();
        public HashMap<String,String> sequences = new HashMap<String, String>();
        public LinkedList<String[]> pairs = new LinkedList<String[]>();

	public void addPair(String f, String s) {
		pairs.add(new String[]{f,s});
	}

//	public String[] getPair(int i){
//		return bla;
//	}

	public void addSequence(String id, String seq){
//		sequences.add(new Seq(id,seq));
            sequences.put(id, seq);
	}

	public String getSequenceById(String id) {
//		for (Seq s : sequences) {
//			if (s.id.equals(id))
//				return s.sequence;
//		}
		return sequences.get(id);
	}

//	public String[] getSequenceByIndex(int i) {
//		String[] str = new String[2];
//
//		str[0] = sequences.get(i).id;
//		str[1] = sequences.get(i).sequence;
//
//		return str;
//	}

	private class DoubleID {
		public String firstID;
		public String secondID;

		public DoubleID(String f,String s){
			this.firstID = f;
			this.secondID = s;
		}
	}

	private class Seq {
		public String id;
		public String sequence;

		public Seq(String id, String seq) {
			this.id = id;
			this.sequence = seq;
		}
	}
}
