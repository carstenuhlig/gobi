package data;

import java.util.ArrayList;

public class Raw {
	//PERFORMANCE HashMap for Sequences
	public ArrayList<Seq> sequences = new ArrayList<Seq>();
	public ArrayList<DoubleID> pairs = new ArrayList<DoubleID>();
	
	public void addPair(String f, String s) {
		pairs.add(new DoubleID(f,s));
	}
	
	public String[] getPair(int i){
		String[] bla = {pairs.get(i).firstID,pairs.get(i).secondID};
		return bla;
	}
	
	public void addSequence(String id, String seq){
		sequences.add(new Seq(id,seq));
	}
	
	public String getSequenceById(String id) {
		for (Seq s : sequences) {
			if (s.id.equals(id))
				return s.sequence;
		}
		return null;
	}
	
	public String[] getSequenceByIndex(int i) {
		String[] str = new String[2];
		
		str[0] = sequences.get(i).id;
		str[1] = sequences.get(i).sequence;
		
		return str;
	}
	
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
