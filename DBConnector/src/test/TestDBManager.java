package test;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import dao.DBManager;
import model.Comments;

public class TestDBManager {

	
	@Test
	public void testConnection(){
		boolean result = true;
		DBManager dbManager = new MockManager();
		
		try {
			
			dbManager.connect("root","poodb");
			
	
			
		} catch (Exception e) {
			e.printStackTrace();
			result = false; 
		}finally {
			dbManager.close();
		}
		
		
		Assert.assertEquals(true,result); 
	}
	
	
	@Test
	public void testDeleteAall(){
		boolean result = true;
		DBManager dbManager = 
				new MockManager();
		
		try{
			dbManager.connect("root","poodb");
			dbManager.deleteAll();
			
		} catch (Exception e) {
			e.printStackTrace();
			result = false; 
		}finally {
			dbManager.close();
		}
		
		Assert.assertEquals(true,result); 
	}
	
	
	
	
	public static class MockManager extends DBManager{
		public MockManager() {
			super("localhost", "dbtest","comments"); 
	    }

		@Override
		public Object insert(Object object) {
			return null;
		}

		@Override
		public void update(Object object) {
		}

		@Override
		protected void checkColumn(String column) {
			// TODO Auto-generated method stub
			
		}

		@Override
		protected ArrayList<?> resultSetToGeneric(ResultSet resultSet) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}
	}

		
}
