package dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import model.Comments;

public class DBMComments extends DBManager<Comments>{

	public DBMComments(String dbhost, String dbName, String dbTable) {
		super(dbhost, dbName, dbTable);
	}

	
	@Override
	public Comments insert(Comments object) throws SQLException { 
		// TODO Auto-generated method stub
		 
		  int  lastInsertedId=-1; 
		  String strSQL = "INSERT INTO  "+ getDbTable() +" values (default, ?, ?, ?, ? , ?, ?)";
		  PreparedStatement preparedStatement=null; 
		  
			try {
				preparedStatement = getConnected()
						.prepareStatement(strSQL,Statement.RETURN_GENERATED_KEYS);
				
				 preparedStatement.setString(1,object.getMyUser());
				 preparedStatement.setString(2,object.getEmail());
				 preparedStatement.setString(3,object.getWebpage());
				 preparedStatement.setDate(4,object.getDatum());
				 preparedStatement.setString(5,object.getSummary());
				 preparedStatement.setString(6,object.getComments());
				 
		        preparedStatement.executeUpdate();	        
		        ResultSet rs = preparedStatement.getGeneratedKeys();
		        
		        if(rs.next())
		        	   lastInsertedId = rs.getInt(1);
				        
			} catch (SQLException e) {				
	            throw e;	            
			} finally{
				
				try {
					preparedStatement.close();
				} catch (Exception e1) {
					
				} 
				
			}
		
			
			
	   object.setId(lastInsertedId);
	   return object; 
	}



	/**
	 * "UPDATE tabla set myuser = ?  where id = ?";
	 * @throws SQLException 
	 */
	@Override
	public void update(Comments object) throws SQLException { 
		
		checkFormatUpdate(object); 
		
	  String strSQL = "UPDATE "+ getDbTable() + " SET " + 
			  " myuser=?, email=?, webpage = ?,datum=?, summary=?, comments=?"
			  + "  WHERE id=?";
	  PreparedStatement preparedStatement=null; 
	
		 try {
			preparedStatement = getConnected()
						.prepareStatement(strSQL);
			
			
			 preparedStatement.setString(1,object.getMyUser());
			 preparedStatement.setString(2,object.getEmail());
			 preparedStatement.setString(3,object.getWebpage());
			 preparedStatement.setDate(4,object.getDatum());
			 preparedStatement.setString(5,object.getSummary());
			 preparedStatement.setString(6,object.getComments());
			 preparedStatement.setInt(7,object.getId());
			 
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
	protected Comments mapDbToObject(ResultSet resultSet) throws SQLException {
		// lee el resultado i 
    	int id = resultSet.getInt("id");
        String user = resultSet.getString("myuser");
        String email = resultSet.getString("email");
        String webpage = resultSet.getString("webpage");
        String summary = resultSet.getString("summary");
        Date date = resultSet.getDate("datum");
        String comments = resultSet.getString("comments");
   
        Comments comment = new Comments();    
        comment.setId(id);
        comment.setEmail(email);
        comment.setDatum(date);
        comment.setMyUser(user);
        comment.setSummary(summary);
        comment.setComments(comments);
        comment.setWebpage(webpage);
        return comment; 
	}


	
	
	/**
	 * Condiciones bajo las cuales se puede actualizar un objeto. 
	 * @param object
	 */
	private static void checkFormatUpdate(Comments object) {
		if(object.getId()==-1){
			throw new RuntimeException("El objeto que trata de actualizar no tiene un id valido"); 
		}
			
	}


}
