package dao;

import java.sql.SQLException;
import java.util.ArrayList;

public interface DBAccess <T>{

	/** Conexion a la base de datos 
	 * 
	 * @param user            usuario de la base de datos 
	 * @param password        palabra secreta de la base de datos 
	 * @throws SQLException   si no se puede abrir la base de datos, no autorizado.. etc  
	 * @throws ClassNotFoundException  Si el driver no esta instalado
	 */
	public void connect(String user, String password) 
			throws SQLException, ClassNotFoundException;  
	 
	/**
	 * Inserta un objeto T en la tabla
	 * @param object objeto de tipo T
	 * @return
	 * @throws SQLException 
	 */
	public int insert(T object) throws SQLException;  
	
	/**
	 * Actualiza un objeto de tipo T, donde T contiene las columnas de la labla a actualizar
	 * @param object
	 * @throws SQLException 
	 */
	public void update(T object) throws SQLException; 
	
	/**
	 * Recupera un objeto de tipo T segun el id, los miembros de T contienen los valores  de las 
	 * columnas de la tabla. 
	 * @param id
	 * @return
	 * @throws SQLException 
	 */
	public T select(int id) throws SQLException; 
	
	/** Ejecuta un selec segun strSQL, y retorna un ArrayList con todos los objetos 
	 * recuperados.  
	 * @param column
	 * @param operator TODO
	 * @param value TODO
	 * @return
	 * @throws SQLException 
	 */
	public ArrayList<T> select(String column, String operator, String value) throws SQLException;
	
	
	/**
	 * Elimina el registro segun el id
	 * @param id
	 * @throws SQLException 
	 */
	public void delete(int id) throws SQLException; 
	
	/**
	 * Elimina todos los objetos de la tabla, reinicia el contador de id
	 * @throws SQLException  
	 */
	public void deleteAll() throws SQLException;  
	
	/**
	 * Cierra una conexion
	 */
	public void close(); 
	
}
