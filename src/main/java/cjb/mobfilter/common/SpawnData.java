package cjb.mobfilter.common;

public class SpawnData implements Comparable{
	//public int id = 0;
	public String id = "";
	public String name = "";
	
	public SpawnData(String id, String name) {
		this.id = id;
		this.name = name;
	}

	@Override
	public int compareTo(Object o) {
		return this.name.compareTo(((SpawnData)o).name);
	}
}
