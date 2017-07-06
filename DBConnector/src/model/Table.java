package model;

/**
 * Clase base que representara un objeto mapeable a tabla de bd. 
 * Como condicion la tabla debe tener una columna id que respresenta 
 * su clave primaria. 
 * @author campino
 */

public class Table {

	private int id;
	
	public Table(){
		setId(-1);
	}
	
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
