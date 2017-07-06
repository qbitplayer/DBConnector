package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import model.Table;

/*
 * <T extends Table> garantiza que todo T debe extender de Table
 */

public abstract class DBManager<T extends Table> implements DBAccess<T>  { 
	
	private String dbName;
	private final String dbTable;
	private String dbUri;
	private Connection connect;
	
	// TODO ojo no sera miembreo
	private ResultSet resultSet;
 
	
	
	public DBManager(String dbhost,String dbName, String dbTable){
	
		 this.dbTable = dbTable; 
		 this.dbName = dbName;
		 this.dbUri = "jdbc:mysql://host/dbName?user=root&password=12345";		 
		 dbUri = dbUri.replace("host",dbhost).
				 	   replace("dbName",dbName);
	 }
	
	
	@Override
	public void connect(String user, String password) 
			throws SQLException, ClassNotFoundException {
		 try {
	        	String uri =  dbUri.replace("root",user)
	        			.replaceAll("12345", password); 
	        	
	            // Cargar el driver MYSQL
	            Class.forName("com.mysql.jdbc.Driver");
	            // jdbc:mysql://ip database // database ? 
	            connect = DriverManager
	                    .getConnection(uri);

	            // Statements allow to issue SQL queries to the database
	            //statement = connect.createStatement();
	          
		    }catch (ClassNotFoundException e){
		    	System.err.println("Verifique que el driver este se ha incluido");
		    	close(); 
		    	throw e;  
	        } catch (SQLException  e) {
	        	close(); 
	            throw e;
	        } 
		
	}
	
	
	@Override
	 public void deleteAll() throws SQLException{
		PreparedStatement preparedStatement=null; 
			try{
				preparedStatement = connect
				        .prepareStatement("truncate "+ dbTable);		        
				preparedStatement.executeUpdate();	
		         			
		    } catch (SQLException e) {
	            throw e;
		    }finally{
				try {
					preparedStatement.close();
				} catch (Exception e1) {} 
			}         
	}
	 


	@Override
	public void delete(int id) throws SQLException{	
		PreparedStatement preparedStatement=null; 
		try {
			preparedStatement = getConnected()
			        .prepareStatement("delete from "+getDbTable()+"  where id= ? ; ");
			 preparedStatement.setInt(1, id);
	         preparedStatement.executeUpdate();
		
	    } catch (SQLException e) {	    	
            throw e;
		}finally{
			try {
				preparedStatement.close();
			} catch (Exception e1) {} 
		}
	}
	
	

	
	
	

	@Override
	public T select(int id) throws SQLException { 
		String strSQL = "SELECT * FROM "+
				getDbTable() +" WHERE id = ?";

		PreparedStatement preparedStatement=null; 
		T generic = null; 
		try {			
			preparedStatement = getConnected()
					.prepareStatement(strSQL);			
			preparedStatement.setInt(1,id);			
			ResultSet resultSet = preparedStatement.executeQuery(); 
			
			ArrayList<T> list = resultSetToGeneric(resultSet);
			generic = list.get(0); 			 
		} catch (SQLException e) {			
			throw e;
		}finally{
			try {
				preparedStatement.close();
			} catch (Exception e1) {} 
		}
		return generic; 
	}

	
	/**
	 * recupera todos los tegistros con la condicion que: 
	 * 
	 * la columna column operador value, donde operador puede ser: 
	 *    =	'value'
	 *    !='value'
	 *    >	'value'
	 *    <	'value'
	 *    >='value'
	 *    <='value'
	 *    BETWEEN	 ? 
	 *    LIKE	'value%'    // use el % para indicar cualquier cosa
	 *    IN	  ? 
	 * @param string 
	 * @throws SQLException 
	 * 
	 */

	@Override
	public ArrayList<T> select(String column, String operator, String value)  throws SQLException { 

		
		checkOperator(operator);  
		String strSQL = "SELECT * FROM "+
				getDbTable() +" WHERE "+ column +" "+ operator +" "+ value;

		PreparedStatement preparedStatement=null; 
		ArrayList<T> list=null; 
		try {
			
			preparedStatement = getConnected()
					.prepareStatement(strSQL);
			ResultSet resultSet = preparedStatement.executeQuery(); 	
			
			list = resultSetToGeneric(resultSet);
			
		} catch (SQLException e) {			
			throw e;
		}finally{
			try {
				preparedStatement.close();
			} catch (Exception e1) {} 
		}
		
		return list; 
	}
	
	
	/**
	 * Transforma el resultado de una consulta resultSet en un objeto de tipo 
	 * T
	 * @param resultSet
	 * @return lista de objetos de tipo T 
	 * @throws SQLException
	 */
	protected  ArrayList<T> resultSetToGeneric(ResultSet resultSet)
			throws SQLException{
		
		ArrayList<T> list = new ArrayList<>(); 		
		 while (resultSet.next()) {
			 
			    T generic= mapDbToObject(resultSet); 
	            list.add(generic);  
	        		 
		 }		 
		 return list; 		
	}
	
	
	
	
	protected abstract T mapDbToObject(ResultSet resultSet) throws SQLException;  


	protected abstract HashMap<String,Object> mapObjectToDb(T object); 

	
	

	
	/** Verifica que la operacion sea valida  
	 * 
	 * @param operator
	 */
	private void checkOperator(String operator) {
		final ArrayList<String> columns = new ArrayList<String>(
				Arrays.asList("=", "!=", "<>","<=",">=","<",">","LIKE","BETWEEN", "IN"));
		if(!columns.contains(operator))
			throw new RuntimeException("Error el operando " 
					+  operator + "no es valido. "); 
	}

	/**
	 * Cerra la conexion 
	 */
	@Override
	public void close() {
	        try {
	            if (resultSet != null) {
	                resultSet.close();
	                resultSet=null; 
	            }	     
	            if (connect != null) {
	                connect.close();
	                connect = null; 
	            }  
	        } catch (Exception e) {

	        }
	 }
	
	/**  getters y setteres osea, metodos accesorios */ 
	
	public String getDbName() {
		return dbName;
	}


	public String getDbTable() {
		return dbTable;
	}
	
	protected Connection getConnected(){  
		return connect; 
	}
	


	@Override
	public int insert(T object) throws SQLException { 	
		int lastInsertedId = -1; 
		
		HashMap<String,Object> mapColumn = mapObjectToDb(object); 		
		String strSQL = getAnSQLInsert(mapColumn);  		
		     // try con algumentos cierra automaticamente al finalizar
		
			 try(PreparedStatement preparedStatement =  getConnected()
						.prepareStatement(strSQL,Statement.RETURN_GENERATED_KEYS)) {
				 int i=1; 
				 for (String column :mapColumn.keySet())						 
					preparedStatement.setObject(i++,mapColumn.get(column));  
 				  
				 preparedStatement.executeUpdate();	        
			        ResultSet rs = preparedStatement.getGeneratedKeys();
			        
			        if(rs.next())
			        	   lastInsertedId = rs.getInt(1);
	
			        object.setId(lastInsertedId); 
			        
			} catch (SQLException e) {
		        throw e;	        
			}
			 
	 return lastInsertedId; 
	}
	
	
	@Override
	public void update(T object) throws SQLException { 		
		HashMap<String,Object> mapColumn = mapObjectToDb(object); 		
		String strSQL = getAnSQLUpdate(mapColumn); 		
		     // try con algumentos cierra automaticamente al finalizar
			 try(PreparedStatement preparedStatement = getConnected()
						.prepareStatement(strSQL)) {
				 int i=1; 
				 for (String column :mapColumn.keySet())						 
					preparedStatement.setObject(i++,mapColumn.get(column));  
 				  
				 preparedStatement.setInt(i,object.getId());
				 preparedStatement.executeUpdate();	     
				
			} catch (SQLException e) {
		        throw e;	        
			}			 		
	}
	
	/**
	 *  " myuser=?, email=?, webpage = ?,datum=?, summary=?, comments=?  WHERE id=?"
	 * @param mapObject
	 * @return
	 */
	
	private String getAnSQLUpdate(HashMap<String,Object> mapObject) {
		  StringBuilder strSQL = new StringBuilder("UPDATE "+ getDbTable() + " SET "); 
		  
		  int index =0;
		  int size =  mapObject.keySet().size(); 
		  for (String column : mapObject.keySet()) { 
			  strSQL.append(column);
			  if(index++<size-1)
				  strSQL.append("=?, "); 
			  else
			      strSQL.append("=? "); 
		   }
		  
		  strSQL.append(" WHERE id=? "); 
		  
		return strSQL.toString();	
	}
	
	/**
	 * 
	 * INSERT INTO getDbTable() (id, column2, column3, column4, column5, column6)
	 * VALUES (default, ?, ?, ?, ?, ?);
	 * 
	 * 
	 * @param mapObject
	 * @return
	 */
	private String getAnSQLInsert(HashMap<String,Object> mapObject) {
		  StringBuilder strSQL = new StringBuilder("INSERT INTO "+ getDbTable()); 
		  StringBuilder columns = new StringBuilder(" ( id, " );
		  StringBuilder values = new StringBuilder(" values (default, " );
		  int index =0;
		  int size =  mapObject.keySet().size();
		  
		  for (String column : mapObject.keySet()){ 		
			  if(index++<size-1){
				 values.append(" ?, "); 
			     columns.append(column + ", "); 
			  }else{
				  values.append(" ?)");
				  columns.append(column + ")");
			  }
		   }
		  
		  strSQL.append(columns); 
		  strSQL.append(values); 
		  
	  return strSQL.toString();	
	}
	
	
	
}
