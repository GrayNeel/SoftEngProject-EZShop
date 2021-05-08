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
import org.sqlite.util.StringUtils;

import it.polito.ezshop.data.Order;
import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.data.SaleTransaction;
import it.polito.ezshop.data.TicketEntry;
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
	public Integer getLastId(String table) {
		String sql = "SELECT COUNT(*) AS tot FROM "+table;
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


	public boolean deleteUser(Integer id) {
		String sql = "DELETE FROM users WHERE id=?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        	pstmt.setInt(1, id);
        	pstmt.executeUpdate();            
        } catch (SQLException e) {
            System.err.println(e.getMessage()); //non serve checkare se esiste. Se non esiste non viene cancellato nulla
            return false;
        }		
		
		return true;
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
        	pstmt.setString(1, role);
        	pstmt.setInt(2, id);
            pstmt.executeUpdate();
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
	        	System.err.println(e.getMessage());
	            return false;
	        }
	    
	    return true;
	}
	
	public boolean logoutUser() {
		String sql = "DELETE FROM loggedusers";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)){
			pstmt.executeUpdate();
	    } catch (SQLException e) {
	            return false;
	        }
	    
	    return true;
	}
	
	public User getLoggedUser() {
		String sql = "SELECT id,username,password,role FROM loggedusers";
		User user = null;
        
		try (Statement stmt  = connection.createStatement();
			 ResultSet rs = stmt.executeQuery(sql)){
            
             user = new UserClass(rs.getInt("id"),rs.getString("username"),rs.getString("password"),rs.getString("role"));
            
        } catch (SQLException e) {
            return null;
        }
        
        return user;
	}
	
	/**
	 * This method adds a new productType to the database "productTypes"
	 * @param productType the ProductTypeClass containing parameters to add
	 */
	public void addProductType(ProductTypeClass productType) {
		String sql = "INSERT INTO productTypes(id, quantity, location, note, productDescription, barCode, pricePerUnit) VALUES(?,?,?,?,?,?,?)";

	    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	            pstmt.setInt(1, productType.getId());
	            pstmt.setInt(2, productType.getQuantity());
	            pstmt.setString(3, productType.getLocation());
	            pstmt.setString(4, productType.getNote());
	            pstmt.setString(5, productType.getProductDescription());
	            pstmt.setString(6, productType.getBarCode());
	            pstmt.setDouble(7, productType.getPricePerUnit());
	            
	            pstmt.executeUpdate();
	        } catch (SQLException e) {
	            System.err.println(e.getMessage());
	        }	  
	}
	
	public boolean checkExistingProductType(String barCode) {
		String sql = "SELECT COUNT(*) AS tot FROM productTypes WHERE barCode=?";
        boolean exists = false;
        //CHECK IF USER ALREADY EXISTS
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        	pstmt.setString(1, barCode);
            ResultSet rs = pstmt.executeQuery();
            
            if(rs.getInt("tot")>0)
            	exists = true;
            
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        
        return exists;
	}
	
	public boolean updateProductType(Integer id, String newDescription, String newCode, double newPrice, String newNote) {
		String sql = "UPDATE productTypes SET productDescription=?, barCode=?, pricePerUnit=?, note=? WHERE id=?";
		
		
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        	pstmt.setString(1, newDescription);
        	pstmt.setString(2, newCode);
        	pstmt.setDouble(3, newPrice);
        	pstmt.setString(4, newNote);        	
        	
        	pstmt.setInt(5, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
		
		return true;
	}
	
	public List<ProductType> getAllProductTypes() {
		String sql = "SELECT * FROM productTypes";
		List<ProductType> productTypeList = new ArrayList<>();
		
		try (Statement stmt  = connection.createStatement();
	         ResultSet rs = stmt.executeQuery(sql)){
	            
			 while (rs.next()) {
				
				 Integer id = rs.getInt("id");
				 Integer quantity = rs.getInt("quantity");
				 String location = rs.getString("location");
				 String note = rs.getString("note");
				 String productDescription = rs.getString("productDescription");
				 String barCode = rs.getString("barCode");
				 Double pricePerUnit = rs.getDouble("pricePerUnit");
				 
				 ProductType product = new ProductTypeClass(id,quantity,location,note, productDescription, barCode, pricePerUnit);
				 productTypeList.add(product);
	         }
	    } catch (SQLException e) {
	    	System.err.println(e.getMessage());
	    }
		
		return productTypeList;
	}
	
	
	public boolean deleteProductType(Integer id) {
		String sql = "DELETE FROM productTypes WHERE id=?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        	pstmt.setInt(1, id);
        	pstmt.executeUpdate();            
        } catch (SQLException e) {
            System.err.println(e.getMessage()); //non serve checkare se esiste. Se non esiste non viene cancellato nulla
            return false;
        }				
		return true;
	}

	public ProductType getProductTypeByBarCode(String barCode) {
		
		String sql = "SELECT * FROM productTypes WHERE barCode=?";
		ProductType product = null;
        
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        	pstmt.setString(1, barCode);
            ResultSet rs = pstmt.executeQuery();
            product = new ProductTypeClass(rs.getInt("id"), rs.getInt("quantity"), rs.getString("location"), rs.getString("note"),rs.getString("productDescription"), rs.getString("barCode"), rs.getDouble("pricePerUnit"));
            
        } catch (SQLException e) {
            System.err.println(e.getMessage());         
		}
        
        return product;
	}
	
	

	public List<ProductType> getProductTypesByDescription(String description) {
		String sql = "SELECT * FROM productTypes WHERE productDescription=?";
		List<ProductType> productTypeList = new ArrayList<>();
		
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        	pstmt.setString(1, description);
            ResultSet rs = pstmt.executeQuery();			
			
			 while (rs.next()) {
				 
				 Integer id = rs.getInt("id");
				 Integer quantity = rs.getInt("quantity");
				 String location = rs.getString("location");
				 String note = rs.getString("note");
				 String productDescription = rs.getString("productDescription");
				 String barCode = rs.getString("barCode");
				 Double pricePerUnit = rs.getDouble("pricePerUnit");
				 
				 ProductType product = new ProductTypeClass(id,quantity,location,note, productDescription, barCode, pricePerUnit);
				 productTypeList.add(product);
	         }
	    } catch (SQLException e) {
	    	System.err.println(e.getMessage());
	    }	
		
		return productTypeList;
	}

	public boolean updateQuantity(Integer id, int toBeAdded) {
		String sqlRead = "SELECT * FROM productTypes WHERE id=?";
		String sql = "UPDATE productTypes SET quantity=? WHERE id=?";
		Integer qty = 0;
		try (PreparedStatement pstmt = connection.prepareStatement(sqlRead)) {
        	pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            qty = rs.getInt("quantity");
            
        } catch (SQLException e) {
            System.err.println(e.getMessage());         
		}
		
		if(qty + toBeAdded < 0)
		{
			System.err.println("Qty can not be less than 0");
            return false;
		}		
		
		
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			
        	pstmt.setInt(1, qty + toBeAdded);        	       	
        	
        	pstmt.setInt(2, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
		
		return true;
	}
	
	public boolean updateLocation(Integer productId, String newPos) {
		
		String sql = "SELECT COUNT(*) AS tot FROM productTypes WHERE location=?";
       
        //CHECK IF Position is ALREADY used
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        	pstmt.setString(1, newPos);
            ResultSet rs = pstmt.executeQuery();
            
            if(rs.getInt("tot")>0)
            {
            	System.err.println("Location Already Used");
            	return false;
            }            	
            
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }  
		
		String sqlUpd = "UPDATE productTypes SET location=? WHERE id=?";		
		
		try (PreparedStatement pstmt = connection.prepareStatement(sqlUpd)) {
        	pstmt.setString(1, newPos);  
        	pstmt.setInt(2, productId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }		
		return true;
	}
	
	/**
	 * This method adds a new order to the database "orders"
	 * @param user the OrderClass containing parameters to add
	 * @return 
	 */
	public boolean issueOrder (OrderClass order) {
		String sql = "INSERT INTO orders(balanceId,productCode,pricePerUnit,quantity,status,orderId) VALUES(?,?,?,?,?,?)";

	    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	            pstmt.setInt(1, order.getBalanceId());
	            pstmt.setString(2, order.getProductCode());
	            pstmt.setDouble(3, order.getPricePerUnit());
	            pstmt.setInt(4, order.getQuantity());
	            pstmt.setString(5, order.getStatus());
	            pstmt.setInt(6, order.getOrderId());
	            
	            pstmt.executeUpdate();
	        } catch (SQLException e) {
	            System.err.println(e.getMessage());
	            return false;
	        }	  
	    return true;
	}
	
	public boolean issueAndPayOrder (OrderClass order) {
		String sql = "SELECT COUNT(*) AS tot FROM orders WHERE orderId=?";
       
        //CHECK IF Order ALREADY EXISTS
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        	pstmt.setInt(1, order.getOrderId());
            ResultSet rs = pstmt.executeQuery();
            
            if(rs.getInt("tot")>0)
            	return false;            
            
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        
		sql = "INSERT INTO orders(balanceId,productCode,pricePerUnit,quantity,status,orderId) VALUES(?,?,?,?,?,?)";

	    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	            pstmt.setInt(1, order.getBalanceId());
	            pstmt.setString(2, order.getProductCode());
	            pstmt.setDouble(3, order.getPricePerUnit());
	            pstmt.setInt(4, order.getQuantity());
	            pstmt.setString(5, order.getStatus());
	            pstmt.setInt(6, order.getOrderId());
	            
	            pstmt.executeUpdate();
	        } catch (SQLException e) {
	            System.err.println(e.getMessage());
	            return false;
	        }	  
	    return true;
	}

	public List<Order> getAllOrders() {
		String sql = "SELECT * FROM orders";
		List<Order> orderList = new ArrayList<>();
		
		try (Statement stmt  = connection.createStatement();
	         ResultSet rs = stmt.executeQuery(sql)){
	            
			 while (rs.next()) {
				
				 Integer balanceId = rs.getInt("balanceId");
				 String productCode = rs.getString("productCode");
				 Double pricePerUnit = rs.getDouble("pricePerUnit");
				 Integer quantity = rs.getInt("quantity");
				 String status = rs.getString("status");
				 Integer orderId = rs.getInt("orderId");
				
				 
				 Order order = new OrderClass(balanceId,productCode,pricePerUnit, quantity, status, orderId);
				 orderList.add(order);
	         }
	    } catch (SQLException e) {
	    	System.err.println(e.getMessage());
	    }
		
		return orderList;
	}

	public boolean payOrder(Integer orderId) {
		
		String sql = "SELECT COUNT(*) AS tot FROM orders WHERE orderId=?";
	       
        //CHECK IF Order ALREADY EXISTS
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        	pstmt.setInt(1, orderId);
            ResultSet rs = pstmt.executeQuery();
            
            if(rs.getInt("tot")==0)
            	return false;            
            
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        
      //CHECK Order status
        sql = "SELECT status FROM orders WHERE orderId=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        	pstmt.setInt(1, orderId);
            ResultSet rs = pstmt.executeQuery();
            
            if(rs.getString("status")!= "ISSUED" || rs.getString("status")!= "ORDERED" )
            	return false;            
            
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        
        //UPDATE status into ORDERED		
        sql = "UPDATE orders SET status=? WHERE orderId=?";		
		
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        	pstmt.setString(1, "ORDERED");  
        	pstmt.setInt(2, orderId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }		
		return true;
	}

	public boolean recordOrderArrival(Integer orderId, String barCode, Integer newQty) {
		 		
		String sql = "SELECT COUNT(*) AS tot FROM orders WHERE orderId=?";
	       
        //CHECK IF Order ALREADY EXISTS
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        	pstmt.setInt(1, orderId);
            ResultSet rs = pstmt.executeQuery();
            
            if(rs.getInt("tot")==0)
            	return false;            
            
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
                    
      //CHECK Order status
        sql = "SELECT status FROM orders WHERE orderId=?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        	pstmt.setInt(1, orderId);
            ResultSet rs = pstmt.executeQuery();            
            if(!rs.getString("status").equals("PAYED"))
            	return false;   
            if(rs.getString("status").equals("COMPLETED"))
            	return true; 
            
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        
      
      //UPDATE status into COMPLETED		
        sql = "UPDATE orders SET status=? WHERE orderId=?";		
		
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        	pstmt.setString(1, "COMPLETED");  
        	pstmt.setInt(2, orderId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }
	
		//UPDATE quantity into productType
		sql = "UPDATE productTypes SET quantity=? WHERE barCode=?";		
		
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        	pstmt.setInt(1, newQty);  
        	pstmt.setString(2, barCode);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }	
		return true;
	}

	
	public boolean defineCustomer(CustomerClass customer) {
		String sql = "INSERT INTO customers(id,customerName,customerCard,points) VALUES(?,?,?,?)";

	    try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	            pstmt.setInt(1, customer.getId());
	            pstmt.setString(2, customer.getCustomerName());
	            pstmt.setString(3, customer.getCustomerCard());
	            pstmt.setInt(4, customer.getPoints());
	            
	            pstmt.executeUpdate();
	        } catch (SQLException e) {
	            System.err.println(e.getMessage());
	        }	
	    return true;
	}
	
	public boolean deleteCustomer(Integer id) {
		String sql = "DELETE FROM customers WHERE id=?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        	pstmt.setInt(1, id);
        	pstmt.executeUpdate();            
        } catch (SQLException e) {
            System.err.println(e.getMessage()); //non serve checkare se esiste. Se non esiste non viene cancellato nulla
            return false;
        }		
		
		return true;
	}
	
	public boolean updateCustomer(Integer id, String newCustomerName, String newCustomerCard) {
		if(newCustomerCard==null) {
			String sql = "UPDATE customers SET customerName=? WHERE id=?";
			try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	        	pstmt.setString(1, newCustomerName);
	        	pstmt.setInt(2, id);
	            pstmt.executeUpdate();
	        } catch (SQLException e) {
	            System.err.println(e.getMessage());
	            return false;
	        }
		}
		else if(newCustomerCard=="") {
			String sql = "UPDATE customers SET customerName=?, customerCard='' WHERE id=?";
			try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	        	pstmt.setString(1, newCustomerName);
	        	pstmt.setInt(2, id);
	            pstmt.executeUpdate();
	        } catch (SQLException e) {
	            System.err.println(e.getMessage());
	            return false;
	        }
		}
		else {
			String sql = "UPDATE customers SET customerName=?, customerCard=? WHERE id=?";
			try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	        	pstmt.setString(1, newCustomerName);
	        	pstmt.setString(2, newCustomerCard);
	        	pstmt.setInt(3, id);
	            pstmt.executeUpdate();
	        } catch (SQLException e) {
	            System.err.println(e.getMessage());
	            return false;
	        }
		}
		
		
		
		return true;
	}
	
	///////////////// Pablo write methods after this point

	public boolean deleteSaleTransaction(Integer transactionId) {
		String sql = "DELETE FROM saleTransactions WHERE state != 'PAYED' AND transactionId=?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        	pstmt.setInt(1, transactionId);
        	pstmt.executeUpdate();            
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }		
		
		return true;
	}
	
	public SaleTransaction getSaleTransactionById(Integer transactionId) {
		String sql = "SELECT transactionId,date,time,price,paymentType,state FROM saleTransactions "
				+ "WHERE state == 'CLOSED' AND transactionId=?";
		SaleTransaction saletransaction = null;
		List<TicketEntry> ticketList = null; // Change after implementing ticketentries
        
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        	pstmt.setInt(1, transactionId);
            ResultSet rs = pstmt.executeQuery();
            
            saletransaction = new SaleTransactionClass(rs.getInt("transactionId"), rs.getString("date"),
            		rs.getString("time"), rs.getDouble("price"), rs.getString("paymentType"), rs.getDouble("discountRate"), 
            		ticketList, rs.getString("state"));
            
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        
        return saletransaction;
	}
	
	public Integer startReturnTransaction(ReturnTransactionClass returnTransaction) {
		String sql = "INSERT INTO returnTransactions(id, transactionId, quantity, returnedValue, state) VALUES(?,?,?,?,?)";
		SaleTransaction transaction = getSaleTransactionById(returnTransaction.getTransactionId());
		Integer idReturn = -1;
		if (transaction!=null) {
			try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	            pstmt.setInt(1, returnTransaction.getId());
	            pstmt.setInt(2, returnTransaction.getTransactionId());
	            pstmt.setInt(3, returnTransaction.getQuantity());
	            pstmt.setDouble(4, returnTransaction.getReturnedValue());
	            pstmt.setString(5, returnTransaction.getState());
	            
	            pstmt.executeUpdate();
	        } catch (SQLException e) {
	            System.err.println(e.getMessage());
	        }
			idReturn = returnTransaction.getId();
			return idReturn;
		}
		
	    return idReturn;
	}
	
	public boolean deleteReturnTransaction(Integer returnId) {
		String sql = "DELETE FROM returnTransactions WHERE state != 'CLOSED' AND id=?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
        	pstmt.setInt(1, returnId);
        	pstmt.executeUpdate();            
        } catch (SQLException e) {
            System.err.println(e.getMessage());
            return false;
        }		
		
		return true;
	}

	
	
	
}
