package model;

public class Table {

	private int id;
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getColumns(){
		return 0;
	}
	
	public String getName(){
		return getClass().getSimpleName(); 
	}
}
