package test;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import org.junit.Assert;
import org.junit.Test;

import dao.DBManager;
import model.Table;

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
		protected Table mapDbToObject(ResultSet resultSet) throws SQLException {
			// TODO Auto-generated method stub
			return null;
		}


		@Override
		protected HashMap mapObjectToDb(Table object) {
			// TODO Auto-generated method stub
			return null;
		}


	}

		
}
