package it.polito.ezshop.classes;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.sqlite.SQLiteConnection;
import org.sqlite.SQLiteUpdateListener;

import it.polito.ezshop.data.User;

public class EZShopDB {
	private SQLiteConnection connection = null;
	private String dbUrl = "jdbc:sqlite:ezshop.db";
	
	private void createConnection() {
		try {
			// create database connection
			this.connection = (SQLiteConnection) DriverManager.getConnection(dbUrl);
			
			//This method will create a listener that will monitor updates in the db in order to 
			//automatically update the data structures 
			connection.addUpdateListener(new SQLiteUpdateListener() {
				@Override
				public void onUpdate(Type type, String db, String table, long rowId) {
					System.out.println("OnUpdate: " + type + " " + db + " " + table + " " + rowId);
					//Update data here
				}
			});
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		} 
	}
	
	public EZShopDB() {
		createConnection();
	}
	
	/**
	 * This method gives back the number of registered user, which corresponds to the last added ID of a user
	 * @return integer 
	 */
	public Integer getLastId() {
		String sql = "SELECT COUNT(*) AS tot FROM users";
        Integer id = -1;
        
        try (Statement stmt  = connection.createStatement();
             ResultSet rs    = stmt.executeQuery(sql)){
            
            id = rs.getInt("tot");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        
        return id;
	}
	
	/**
	 * This method adds a new user to the database "users"
	 * @param user the UserClass containing parameters to add
	 */
	public void addUser(UserClass user) {
		String sql = "INSERT INTO users(id,username,password,role) VALUES(?,?,?,?)";

	    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	            pstmt.setInt(1, user.getId());
	            pstmt.setString(2, user.getUsername());
	            pstmt.setString(3, user.getPassword());
	            pstmt.setString(4, user.getRole());
	            
	            pstmt.executeUpdate();
	        } catch (SQLException e) {
	            System.err.println(e.getMessage());
	        }
	}
	
	/**
	 * This method gives back the UserClass with the given username, if present
	 * @return
	 */
	public boolean checkExistingUser(String username) {
		String sql = "SELECT COUNT(*) AS tot FROM users WHERE username=?";
        boolean exists = false;
        //CHECK IF USER ALREADY EXISTS
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        	pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            
            if(rs.getInt("tot")>0)
            	exists = true;
            
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        
        return exists;
	}
	
	public List<User> getAllUsers() {
		String sql = "SELECT id,username,password,role FROM users";
		List<User> userlist = new ArrayList<>();
		
		try (Statement stmt  = connection.createStatement();
	         ResultSet rs = stmt.executeQuery(sql)){
	            
			 while (rs.next()) {
				 Integer id = rs.getInt("id");
				 String name = rs.getString("username");
				 String password = rs.getString("password");
				 String role = rs.getString("role");
				 
				 User user = new UserClass(id,name,password,role);
				 userlist.add(user);
	         }
	    } catch (SQLException e) {
	    	System.err.println(e.getMessage());
	    }
		
		return userlist;
	}
	
	public User getUserById(Integer id) {
		String sql = "SELECT id,username,password,role FROM users WHERE id=?";
		User user = null;
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        	pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            
            user = new UserClass(rs.getInt("id"),rs.getString("username"),rs.getString("password"),rs.getString("role"));
            
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        
        return user;
	}
	
	public boolean updateUserRole(Integer id, String role) {
		String sql = "UPDATE users SET role=? WHERE id=?";
		
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        	pstmt.setInt(1, id);
        	pstmt.setString(2, role);
            pstmt.executeQuery();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
		
		return true;
	}
	
	public User getUserByCredentials(String username, String password) {
		String sql = "SELECT id,username,password,role FROM users WHERE username=? AND password=?";
		User user = null;
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        	pstmt.setString(1,username);
        	pstmt.setString(2, password);
        	
            ResultSet rs = pstmt.executeQuery();
            
            user = new UserClass(rs.getInt("id"),rs.getString("username"),rs.getString("password"),rs.getString("role"));
            
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        
        return user;
	}
	
	public boolean loginUser(User user) {
		String sql = "INSERT INTO loggedusers(id,username,password,role) VALUES(?,?,?,?)";

	    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	            pstmt.setInt(1, user.getId());
	            pstmt.setString(2, user.getUsername());
	            pstmt.setString(3, user.getPassword());
	            pstmt.setString(4, user.getRole());
	            
	            pstmt.executeUpdate();
	        } catch (SQLException e) {
	            return false;
	        }
	    
	    return true;
	}
	
	public boolean logoutUser() {
		String sql = "DELETE FROM loggedusers WHERE 1";

		try (Statement stmt  = connection.createStatement();
		     ResultSet rs = stmt.executeQuery(sql)){
	            
	        } catch (SQLException e) {
	            return false;
	        }
	    
	    return true;
	}
	
	public User getLoggedUser() {
		String sql = "SELECT id,username,password,role FROM users LIMIT 1";
		User user = null;
        
		try (Statement stmt  = connection.createStatement();
			 ResultSet rs = stmt.executeQuery(sql)){
            
             user = new UserClass(rs.getInt("id"),rs.getString("username"),rs.getString("password"),rs.getString("role"));
            
        } catch (SQLException e) {
            return null;
        }
        
        return user;
	}
}
