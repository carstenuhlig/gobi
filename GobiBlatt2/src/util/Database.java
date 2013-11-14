package util;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class Database {
	String name;
	HashMap<String, Data> database;
	static String key;
	static String value;

	public Database(String name) {
		this.name = name;
		database = new HashMap<>();
	}

	private String databaseToString() {
		String returnstr = "";
		Iterator it = database.keySet().iterator();
		while (it.hasNext()){
			key = it.next().toString();
			value = database.get(key).toString();
			returnstr += key + "=" + value + "\n";
		}
		return returnstr;
	}

	@Override
	public String toString() {
		return "Database [" + name + "=" + databaseToString() + "]";
	}

	public void addData(String proteinid, Data piece) {
		database.put(proteinid, piece);
//		System.out.println("proteinid=" + proteinid + ", " + piece);
	}

	public Data getDataFromProteinID(String proteinid) {
		Data d = database.get(proteinid);
		return d;
	}
}
