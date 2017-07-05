package test;

import java.sql.Date;
import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import dao.DBMComments;
import model.Comments;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TestDBMComments {

	

	@Test
	public void testInsert(){
		boolean result = true;
		DBMComments dbManager =   new DBMComments("localhost", "dbTest", "comments"); 		
		Comments comments1 =getMockDBMComments(); 
		
		try {
			dbManager.connect("root","poodb"); 
			dbManager.insert(comments1);

			
			
		} catch (Exception e) {
			result = false; 
			e.printStackTrace();
			
		}finally{
			dbManager.close(); 
		}
		
		Assert.assertEquals(true,result);
		Assert.assertEquals(true,comments1.getId()!=-1); 
	}
	
	

	
	@Test
	public void testUpdate(){
		boolean result = true;
		DBMComments dbManager =   new DBMComments("localhost", "dbTest", "comments"); 
		Comments comments1 =getMockDBMComments();
		Comments commentsUpdated=null; 
		
		try {
			dbManager.connect("root","poodb"); 
			dbManager.insert(comments1);
				comments1.setMyUser("Don update"); 
				comments1.setComments("Me han actualizado"); 
				comments1.setDatum(new Date(124563)); 
				
			dbManager.update(comments1); 			
			commentsUpdated = dbManager.select(comments1.getId()); 
			
			
		} catch (Exception e) {
			result = false; 
			e.printStackTrace();	
		}finally{
			dbManager.close(); 
		}
		
		Assert.assertEquals(true,result);
		
	
		Assert.assertEquals("Don update",commentsUpdated.getMyUser());
		Assert.assertEquals("Me han actualizado",commentsUpdated.getComments());
		Assert.assertEquals(true,commentsUpdated.getId()!=-1); 
		Assert.assertEquals(new Date(124563).toString(),
				comments1.getDatum().toString());  
	}

	
	
	
	
	@Test
	public void testGet(){
		boolean result = true;
		DBMComments dbManager =   new DBMComments("localhost", "dbTest", "comments"); 		
		Comments comments1 =getMockDBMComments(); 
		Comments results = null; 
		try {
			dbManager.connect("root","poodb"); 
			
			int id = dbManager.insert(comments1);
			comments1.setId(id); 
			results = dbManager.select(comments1.getId()); 
			
		} catch (Exception e) {
			result = false; 
			e.printStackTrace();
		}finally{
			dbManager.close(); 
		}
		
		Assert.assertEquals(true,result);
		Assert.assertEquals(comments1.getMyUser(),results.getMyUser()); 
		Assert.assertEquals(comments1.getComments(),results.getComments()); 
	}
	
	
	@Test
	public void testSelect(){
		boolean result = true;
		DBMComments dbManager =   new DBMComments("localhost", "dbTest", "comments"); 		
		
		Comments comments1 =getMockDBMComments("user1","user1@poo.com");
		Comments comments2 =getMockDBMComments("user2","user2@poo.com");
		Comments comments3 =getMockDBMComments("user3","user3@poo.com");
		Comments comments4 =getMockDBMComments("user1","user4@poo.com");
		Comments comments5 =getMockDBMComments("xuser","user4@poo.com");
		
		ArrayList<Comments> results1 = null;  
		ArrayList<Comments> results2 = null;
		ArrayList<Comments> results3 = null;
		try {
			dbManager.connect("root","poodb"); 
			
			dbManager.deleteAll();
			
			dbManager.insert(comments1);
			dbManager.insert(comments2);
			dbManager.insert(comments3);
			dbManager.insert(comments4);
			dbManager.insert(comments5);
			
			results1 = dbManager.select("myuser","LIKE", "'user%'");
			results2 = dbManager.select("myuser","=","'user1'");
			results3 = dbManager.select("id","BETWEEN","2 AND 5"); 
			
		} catch (Exception e) {
			result = false; 
			e.printStackTrace();
		}finally{
			dbManager.close(); 
		}
		
		Assert.assertEquals(true,result);
		Assert.assertEquals(4,results1.size());
		Assert.assertEquals(2,results2.size());
		Assert.assertEquals(4,results3.size());
	}
	

	
	private Comments getMockDBMComments() { 
		return getMockDBMComments("root", "root@root"); 
	}

	private Comments getMockDBMComments(String myUser, String email) { 
		Comments comments1 = new Comments();
		comments1.setMyUser(myUser);
		comments1.setEmail(email); 
		comments1.setSummary("Esto es un resumen"); 
		comments1.setComments("Esto es un comentario"); 
		comments1.setDatum(new Date(System.currentTimeMillis())); 
		comments1.setWebpage("poo.cifo"); 
		return comments1;
	}
	
	
	
	
	
	
	
}
