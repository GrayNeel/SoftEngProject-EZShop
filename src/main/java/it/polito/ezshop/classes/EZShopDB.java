package it.polito.ezshop.classes;

import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.sqlite.SQLiteConnection;
import org.sqlite.SQLiteUpdateListener;
import org.sqlite.util.StringUtils;

import it.polito.ezshop.data.BalanceOperation;
import it.polito.ezshop.data.Customer;
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

			// This method will create a listener that will monitor updates in the db in
			// order to
			// automatically update the data structures
			connection.addUpdateListener(new SQLiteUpdateListener() {
				@Override
				public void onUpdate(Type type, String db, String table, long rowId) {
					System.out.println("OnUpdate: " + type + " " + db + " " + table + " " + rowId);
					// Update data here
				}
			});
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
	}

	public EZShopDB() {
		createConnection();
	}
	
	public boolean resetDB(String table) {		
		
		String sql = "DELETE FROM " + table;
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e.getMessage()); // non serve checkare se esiste. Se non esiste non viene cancellato
												// nulla
			return false;
		}

		return true;
	}
	
	/**
	 * This method gives back the number of registered user, which corresponds to
	 * the last added ID of a user
	 * 
	 * @return integer
	 */
	public Integer getLastId(String table) {
		String sql = "SELECT MAX(id) AS tot FROM " + table;
		Integer id = -1;
		
		try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
			//If table is empty, gives id = 0
			id = rs.getInt("tot");
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}

		return id;
	}

	/**
	 * This method adds a new user to the database "users"
	 * 
	 * @param user the UserClass containing parameters to add
	 */
	public void addUser(User user) {
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
	 * 
	 * @return
	 */
	public boolean checkExistingUser(String username) {
		String sql = "SELECT COUNT(*) AS tot FROM users WHERE username=?";
		boolean exists = false;
		// CHECK IF USER ALREADY EXISTS
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, username);
			ResultSet rs = pstmt.executeQuery();

			if (rs.getInt("tot") > 0)
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
			System.err.println(e.getMessage()); // non serve checkare se esiste. Se non esiste non viene cancellato
												// nulla
			return false;
		}

		return true;
	}

	public List<User> getAllUsers() {
		String sql = "SELECT id,username,password,role FROM users";
		List<User> userlist = new ArrayList<>();

		try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {
				Integer id = rs.getInt("id");
				String name = rs.getString("username");
				String password = rs.getString("password");
				String role = rs.getString("role");

				User user = new UserClass(id, name, password, role);
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

			user = new UserClass(rs.getInt("id"), rs.getString("username"), rs.getString("password"),
					rs.getString("role"));

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
			pstmt.setString(1, username);
			pstmt.setString(2, password);

			ResultSet rs = pstmt.executeQuery();

			user = new UserClass(rs.getInt("id"), rs.getString("username"), rs.getString("password"),
					rs.getString("role"));

		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}

		return user;
	}

	/**
	 * Deprecated DB functions that managed loggedUser. It is better to use a
	 * variable in java to avoid problems when user exit program without logging out
	 */
	// public boolean loginUser(User user) {
	// String sql = "INSERT INTO loggedusers(id,username,password,role)
	// VALUES(?,?,?,?)";
	//
	// try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
	// pstmt.setInt(1, user.getId());
	// pstmt.setString(2, user.getUsername());
	// pstmt.setString(3, user.getPassword());
	// pstmt.setString(4, user.getRole());
	//
	// pstmt.executeUpdate();
	// } catch (SQLException e) {
	// System.err.println(e.getMessage());
	// return false;
	// }
	//
	// return true;
	// }
	//
	// public boolean logoutUser() {
	// String sql = "DELETE FROM loggedusers";
	//
	// try (PreparedStatement pstmt = connection.prepareStatement(sql)){
	// pstmt.executeUpdate();
	// } catch (SQLException e) {
	// return false;
	// }
	//
	// return true;
	// }

//	public User getLoggedUser() {
//		String sql = "SELECT id,username,password,role FROM loggedusers";
//		User user = null;
//
//		try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
//
//			user = new UserClass(rs.getInt("id"), rs.getString("username"), rs.getString("password"),
//					rs.getString("role"));
//
//		} catch (SQLException e) {
//			return null;
//		}
//
//		return user;
//	}

	/**
	 * This method adds a new productType to the database "productTypes"
	 * 
	 * @param productType the ProductTypeClass containing parameters to add
	 */
	public void addProductType(ProductType productType) {
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
		// CHECK IF USER ALREADY EXISTS
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, barCode);
			ResultSet rs = pstmt.executeQuery();

			if (rs.getInt("tot") > 0)
				exists = true;

		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}

		return exists;
	}

	public boolean updateProductType(Integer id, String newDescription, String newCode, double newPrice,
			String newNote) {
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

		try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {

				Integer id = rs.getInt("id");
				Integer quantity = rs.getInt("quantity");
				String location = rs.getString("location");
				String note = rs.getString("note");
				String productDescription = rs.getString("productDescription");
				String barCode = rs.getString("barCode");
				Double pricePerUnit = rs.getDouble("pricePerUnit");

				ProductType product = new ProductTypeClass(id, quantity, location, note, productDescription, barCode,
						pricePerUnit);
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
			System.err.println(e.getMessage()); // non serve checkare se esiste. Se non esiste non viene cancellato
												// nulla
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
			product = new ProductTypeClass(rs.getInt("id"), rs.getInt("quantity"), rs.getString("location"),
					rs.getString("note"), rs.getString("productDescription"), rs.getString("barCode"),
					rs.getDouble("pricePerUnit"));

		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}

		return product;
	}

	public List<ProductType> getProductTypesByDescription(String description) {
		String sql = "SELECT * FROM productTypes WHERE productDescription LIKE ?";
		List<ProductType> productTypeList = new ArrayList<>();

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, "%"+description+"%");
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {

				Integer id = rs.getInt("id");
				Integer quantity = rs.getInt("quantity");
				String location = rs.getString("location");
				String note = rs.getString("note");
				String productDescription = rs.getString("productDescription");
				String barCode = rs.getString("barCode");
				Double pricePerUnit = rs.getDouble("pricePerUnit");

				ProductType product = new ProductTypeClass(id, quantity, location, note, productDescription, barCode,
						pricePerUnit);
				productTypeList.add(product);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}

		return productTypeList;
	}

	public Integer getQuantityByProductTypeId(Integer id) {
		String sql = "SELECT quantity FROM productTypes WHERE id=?";
		Integer qty = null;
		
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();
			qty = rs.getInt("quantity");
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		
		return qty;
	}
	
	public Integer getQuantityByProductTypeBarCode(String barCode) {
		String sql = "SELECT quantity FROM productTypes WHERE barCode=?";
		Integer qty = null;
		
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, barCode);
			ResultSet rs = pstmt.executeQuery();
			qty = rs.getInt("quantity");
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		
		return qty;
	}
	
	public boolean updateQuantityByProductTypeId(Integer id, int newQuantity) {
		String sql = "UPDATE productTypes SET quantity=? WHERE id=?";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, newQuantity);
			pstmt.setInt(2, id);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			return false;
		}

		return true;
	}

	public boolean updateQuantityByBarCode(String productCode, int newQuantity) {
		String sql = "UPDATE productTypes SET quantity=? WHERE barCode=?";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, newQuantity);
			pstmt.setString(2, productCode);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			return false;
		}

		return true;
	}

	public boolean isLocationUsed(String pos) {
		String sql = "SELECT COUNT(*) AS tot FROM productTypes WHERE location=?";
		Integer res = null;
		// CHECK IF Position is ALREADY used
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, pos);
			ResultSet rs = pstmt.executeQuery();
			res = rs.getInt("tot");
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		
		if(res==0)
			return false;
		else 
			return true;
	}
	
	public boolean updateProductTypeLocation(Integer productId, String newPos) {
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
	 * 
	 * @param user the OrderClass containing parameters to add
	 * @return
	 */
	public boolean addAndIssueOrder(Order order) {
		String sql = "INSERT INTO orders(id,balanceId,productCode,pricePerUnit,quantity,status) VALUES(?,?,?,?,?,?)";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, order.getOrderId());
			pstmt.setInt(2, order.getBalanceId());
			pstmt.setString(3, order.getProductCode());
			pstmt.setDouble(4, order.getPricePerUnit());
			pstmt.setInt(5, order.getQuantity());
			pstmt.setString(6, order.getStatus());

			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			return false;
		}
		return true;
	}

	// It is totally wrong
//	public boolean issueAndPayOrder(OrderClass order) {
//		String sql = "SELECT COUNT(*) AS tot FROM orders WHERE orderId=?";
//
//		// CHECK IF Order ALREADY EXISTS
//		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
//			pstmt.setInt(1, order.getOrderId());
//			ResultSet rs = pstmt.executeQuery();
//
//			if (rs.getInt("tot") > 0)
//				return false;
//
//		} catch (SQLException e) {
//			System.err.println(e.getMessage());
//		}
//
//		sql = "INSERT INTO orders(balanceId,productCode,pricePerUnit,quantity,status,orderId) VALUES(?,?,?,?,?,?)";
//
//		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
//			pstmt.setInt(1, order.getBalanceId());
//			pstmt.setString(2, order.getProductCode());
//			pstmt.setDouble(3, order.getPricePerUnit());
//			pstmt.setInt(4, order.getQuantity());
//			pstmt.setString(5, order.getStatus());
//			pstmt.setInt(6, order.getOrderId());
//
//			pstmt.executeUpdate();
//		} catch (SQLException e) {
//			System.err.println(e.getMessage());
//			return false;
//		}
//		return true;
//	}

	public Order getOrderById(Integer orderId) {
		String sql = "SELECT * FROM orders WHERE id=?";
		Order order;
		
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, orderId);
			ResultSet rs = pstmt.executeQuery();

			Integer id = rs.getInt("id");
			Integer balanceId = rs.getInt("balanceId");
			String productCode = rs.getString("productCode");
			Double pricePerUnit = rs.getDouble("pricePerUnit");
			Integer quantity = rs.getInt("quantity");
			String status = rs.getString("status");
			
			order = new OrderClass(id, balanceId, productCode, pricePerUnit, quantity, status);
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			order = null;
		}

		return order;
	}
	
	public List<Order> getAllOrders() {
		String sql = "SELECT * FROM orders";
		List<Order> orderList = new ArrayList<>();

		try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {

				Integer balanceId = rs.getInt("balanceId");
				String productCode = rs.getString("productCode");
				Double pricePerUnit = rs.getDouble("pricePerUnit");
				Integer quantity = rs.getInt("quantity");
				String status = rs.getString("status");
				Integer orderId = rs.getInt("id");

				Order order = new OrderClass(orderId, balanceId, productCode, pricePerUnit, quantity, status);
				orderList.add(order);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}

		return orderList;
	}

	public boolean payOrderById(Integer orderId) {
		
		// UPDATE status into ORDERED
		String sql = "UPDATE orders SET status=? WHERE id=?";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, "PAYED");
			pstmt.setInt(2, orderId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			return false;
		}
		return true;
	}
	
	public boolean setBalanceIdInOrder(Integer orderId, Integer balanceId) {
		
		// UPDATE status into ORDERED
		String sql = "UPDATE orders SET balanceId=? WHERE id=?";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, balanceId);
			pstmt.setInt(2, orderId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			return false;
		}
		return true;
	}

	public boolean recordOrderArrivalById(Integer orderId) {

		// UPDATE status into COMPLETED
		String sql = "UPDATE orders SET status=? WHERE id=?";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, "COMPLETED");
			pstmt.setInt(2, orderId);
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
			return false;
		}
		return true;
	}

	public boolean deleteCustomer(Integer id) {
		String sql = "DELETE FROM customers WHERE id=?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, id);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e.getMessage()); // non serve checkare se esiste. Se non esiste non viene cancellato
												// nulla
			return false;
		}

		return true;
	}

	public boolean updateCustomer(Integer id, String newCustomerName, String newCustomerCard) {
		if (newCustomerCard == null) {
			String sql = "UPDATE customers SET customerName=? WHERE id=?";
			try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
				pstmt.setString(1, newCustomerName);
				pstmt.setInt(2, id);
				pstmt.executeUpdate();
			} catch (SQLException e) {
				System.err.println(e.getMessage());
				return false;
			}
		} else if (newCustomerCard == "") {
			String sql = "UPDATE customers SET customerName=?, customerCard='' WHERE id=?";
			try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
				pstmt.setString(1, newCustomerName);
				pstmt.setInt(2, id);
				pstmt.executeUpdate();
			} catch (SQLException e) {
				System.err.println(e.getMessage());
				return false;
			}
		} else {
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

	public boolean createCard(String cardId) {
		String sql = "INSERT INTO cards(id,assigned) VALUES(?,0)";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, cardId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			return false;
		}
		return true;
	}

	public Customer getCustomerById(Integer id) {
		String sql = "SELECT id,customerName,customerCard,points FROM customers WHERE id=?";
		Customer customer = null;

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();

			customer = new CustomerClass(rs.getInt("id"), rs.getString("customerName"), rs.getString("customerCard"),
					rs.getInt("points"));

		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}

		return customer;
	}

	public List<Customer> getAllCustomers() {
		String sql = "SELECT id,customerName,customerCard,points FROM customers";
		List<Customer> customerlist = new ArrayList<>();

		try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {
				Integer id = rs.getInt("id");
				String customerName = rs.getString("customerName");
				String customerCard = rs.getString("customerCard");
				Integer points = rs.getInt("points");

				Customer customer = new CustomerClass(id, customerName, customerCard, points);
				customerlist.add(customer);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}

		return customerlist;
	}

	public boolean attachCardToCustomer(String customerCard, Integer customerId) {
		boolean flag;
		String sql = "UPDATE customers SET customerCard=? WHERE id=?; UPDATE cards SET assigned=1 WHERE id=? and assigned=0";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, customerCard);
			pstmt.setInt(2, customerId);
			pstmt.executeUpdate();
			flag = true;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			flag = false;
		}
		
		return flag;
	}

	public Integer getCardPoints(String customerCard) {
		String sql = "SELECT points from cards WHERE id=?";
		Integer points = -1;
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, customerCard);
			ResultSet rs = pstmt.executeQuery();
			points = rs.getInt("points");

		} catch (SQLException e) {
			System.err.println(e.getMessage());
			return -1;
		}
		return points;
	}
	
	public boolean updateCardPoints(String customerCard, Integer points) {
		boolean flag;

		String sql = "UPDATE cards SET points=? WHERE id=?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, points);
			pstmt.executeUpdate();
			flag = true;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			flag = false;
		}

		return flag;
	}

	public Integer startSaleTransaction(SaleTransactionClass saleTransaction) {
		Date curdate = new Date();
		String[] datesplit = curdate.toString().split(" ");
		String sql = "INSERT INTO saleTransactions(id, price, discountRate, date, time, paymentType, state) VALUES(?,?,?,?,?,'CASH','STARTED')";
		
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, saleTransaction.getTicketNumber());
			pstmt.setDouble(2, saleTransaction.getPrice());
			pstmt.setDouble(3, saleTransaction.getDiscountRate());
			pstmt.setString(4, datesplit[0]);
			pstmt.setString(5, datesplit[1]);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			return -1;
		}


		return saleTransaction.getTicketNumber();
	}
	
	public SaleTransactionClass getSaleTransactionById(Integer transactionId) {
    	String sql = "SELECT * FROM saleTransactions WHERE id=?";
		SaleTransactionClass transaction = null;
		List<TicketEntry> entries = new ArrayList<>();

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, transactionId);
			ResultSet rs = pstmt.executeQuery();
			transaction = new SaleTransactionClass(rs.getInt("id"), rs.getString("date"), rs.getString("time"),
					rs.getDouble("price"), rs.getString("paymentType"), rs.getDouble("discountRate"), entries,
					rs.getString("state"));

		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}

		return transaction;
	}

	public boolean applyDiscountRateToProduct(Integer transactionId, String productCode, Double discountRate) {
		boolean flag;

		String sql = "UPDATE p SET discountRate=? WHERE id=?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setDouble(1, discountRate);
			pstmt.setInt(2, transactionId);
			pstmt.executeUpdate();
			flag = true;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			flag = false;
		}

		return flag;
	}

	public boolean applyDiscountRate(Integer transactionId, Double discountRate) {
		boolean flag;

		String sql = "UPDATE saleTransactions SET discountRate=? WHERE id=?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setDouble(1, discountRate);
			pstmt.setInt(2, transactionId);
			pstmt.executeUpdate();
			flag = true;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			flag = false;
		}

		return flag;
	}
	
	public boolean createTicketEntry(TicketEntry ticketEntry, Integer transactionId) {		
		String sql = "INSERT INTO productEntries(productCode, amount, total, transactionId, unitPrice, discountRate) VALUES(?,?,0.0,?,?,?,?)";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, ticketEntry.getBarCode());
			pstmt.setInt(2, ticketEntry.getAmount());
			pstmt.setInt(3, transactionId);
			pstmt.setDouble(4, ticketEntry.getPricePerUnit());
			pstmt.setDouble(5, ticketEntry.getDiscountRate());

			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			return false;
		}


		return true;
	}
	
	public boolean updateTransactionState(Integer transactionId, String state) {
    	String sql = "UPDATE saleTransactions SET state=? WHERE id=?";
    		
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, transactionId);
			pstmt.setString(2, state);
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			return false;
    	}
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
    

    public SaleTransaction getClosedSaleTransactionById(Integer transactionId) {
        String sql = "SELECT id,discountRate,date,time,price,paymentType,state FROM saleTransactions "
                + "WHERE state == 'PAYED' AND id=?";
        List<TicketEntry> products = getProductEntriesByTransactionId(transactionId);
        SaleTransaction saletransaction = null;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, transactionId);
            ResultSet rs = pstmt.executeQuery();

            saletransaction = new SaleTransactionClass(rs.getInt("id"), rs.getString("date"),
                    rs.getString("time"), rs.getDouble("price"), rs.getString("paymentType"),
                    rs.getDouble("discountRate"), products , rs.getString("state"));
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return saletransaction;
    }
    
    public List<TicketEntry> getProductEntriesByTransactionId(Integer transactionId) {
    	String sql = "SELECT productEntries.id AS id,productEntries.productCode as productCode,productTypes.productDescription AS productDescription,productEntries.amount AS amount,productTypes.pricePerUnit AS pricePerUnit"
                + " FROM productTypes JOIN productEntries ON productTypes.barCode=productEntries.productCode"
                + " WHERE productEntries.transactionId=?";
        List<TicketEntry> productslist = new ArrayList<>();

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, transactionId);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Integer id = rs.getInt("id");
                String productCode = rs.getString("productCode");
                String productDescription = rs.getString("productDescription");
                Integer amount = rs.getInt("amount");
                double pricePerUnit = rs.getDouble("pricePerUnit");

                TicketEntry productEntry = new TicketEntryClass(id, productCode, productDescription, amount,
                        pricePerUnit, transactionId, 0.0);
                productslist.add(productEntry);
            }
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        
        return productslist;
    
    }


    public Integer startReturnTransaction(ReturnTransactionClass returnTransaction) {
        String sql = "INSERT INTO returnTransactions(id, transactionId, quantity, returnValue, state) VALUES(?,?,?,?,?)";
        SaleTransaction transaction = getSaleTransactionById(returnTransaction.getTransactionId());
        Integer idReturn = -1;
        if (transaction != null) {
            try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
                pstmt.setInt(1, returnTransaction.getId());
                pstmt.setInt(2, returnTransaction.getTransactionId());
                pstmt.setInt(3, returnTransaction.getQuantity());
                pstmt.setDouble(4, returnTransaction.getReturnValue());
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
        String sql = "DELETE FROM returnTransactions WHERE state = 'CLOSED' AND id=?";
        boolean success = false;
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, returnId);
            pstmt.executeUpdate();
            success = true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return success;
    }

    public ReturnTransactionClass getReturnTransactionById(Integer returnId) {
        String sql = "SELECT * FROM returnTransactions WHERE id=?";
        ReturnTransactionClass returntransaction = null;

        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, returnId);
            ResultSet rs = pstmt.executeQuery();

            returntransaction = new ReturnTransactionClass(rs.getInt("id"), rs.getInt("transactionId"),
                    rs.getInt("quantity"), rs.getDouble("returnValue"), rs.getString("state"));

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return returntransaction;
    }

    public double getPricePerUnit(String productCode) {
        String sql = "SELECT pricePerUnit FROM productTypes WHERE barCode=?";
        double result = 0;
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, productCode);
            ResultSet rs = pstmt.executeQuery();
            result= rs.getDouble("pricePerUnit");
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return result;
    }
    
    public boolean returnProduct(int id, int returnId, String productCode, int amount, double returnValue) {
        String sql = "INSERT INTO productReturns(id,returnId,productCode,quantity,returnValue) VALUES(?,?,?,?,?)";
        boolean success = false;
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.setInt(2, returnId);
            pstmt.setString(3, productCode);
            pstmt.setInt(4, amount);
            pstmt.setDouble(5, returnValue);
            pstmt.executeUpdate();
            success = true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        // UPDATE state of returnTransaction

        return success;
    }
    
    public Integer getAmountonEntry(Integer transactionId, String productCode) {
    	String sql = "SELECT amount FROM productEntries WHERE transactionId=? AND productCode=?";
        Integer result = -1;
    	try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, transactionId);
            pstmt.setString(2, productCode);
            ResultSet rs = pstmt.executeQuery();
            result = rs.getInt("amount");

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    	return result;
    }
    
    public double getTotalOnEntry(Integer transactionId, String productCode) {
    	String sql = "SELECT total FROM productEntries WHERE transactionId=? AND productCode=?";
        double result = 0.0;
    	try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, transactionId);
            pstmt.setString(2, productCode);
            ResultSet rs = pstmt.executeQuery();
            result = rs.getInt("total");

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    	return result;
    }
    
    public boolean checkProductInSaleTransaction(Integer transactionId, String productCode) {
    	String sql = "SELECT COUNT(*) as tot FROM productEntries WHERE transactionId=? AND productCode=?";
    	boolean exists = false;
    	try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, transactionId);
            pstmt.setString(2, productCode);
            ResultSet rs = pstmt.executeQuery();
            if (rs.getInt("tot")==1)
            	exists = true;

        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
    	return exists;
    }
    
    public void updateReturnTransaction(Integer returnId, Integer newAmount, Double newReturnValue) {
    	String sql = "UPDATE returnTransactions SET quantity=? , returnValue=? , state=? WHERE id=?";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, newAmount);
			pstmt.setDouble(2, newReturnValue);
			pstmt.setString(3, "CLOSED");
			pstmt.setInt(4, returnId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
    }
    
    public void updateSaleTransactionAfterCommit(Integer transactionId, Double newReturnValue) {
    	String sql = "UPDATE saleTransactions SET price=? WHERE id=?";
    		
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setDouble(1, newReturnValue);
			pstmt.setInt(2, transactionId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
    	}
    }
    
    public void updateEntryAfterCommit(Integer transactionId, String productCode, int newAmountSold, double newTotalSold){
    	String sql = "UPDATE productEntries SET amount=?, total=? WHERE transactionId=? AND productCode=?";
		
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, newAmountSold);
			pstmt.setDouble(2, newTotalSold);
			pstmt.setInt(3, transactionId);
			pstmt.setString(4, productCode);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			System.err.println(e.getMessage());
    	}
    }
    
    public List<ProductReturnClass> getAllProductReturnsById(Integer returnId) {
		String sql = "SELECT * FROM productReturns WHERE returnId=?";
		List<ProductReturnClass> returnlist = new ArrayList<>();

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, returnId);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Integer id = rs.getInt("id");
				String productCode = rs.getString("productCode");
				Integer quantity = rs.getInt("quantity");
				double returnValue = rs.getDouble("returnValue");

				ProductReturnClass returnEntry = new ProductReturnClass(id, returnId, productCode, quantity, returnValue);
				returnlist.add(returnEntry);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}

		return returnlist;
	}
    
    public boolean deleteProductReturnsByReturnId(Integer returnId) {
    	String sql = "DELETE FROM productReturns WHERE returnId=?";
        boolean success = false;
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, returnId);
            pstmt.executeUpdate();
            success = true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return success;
    }
    
    public boolean updatePaymentSaleTransaction(Integer transactionId, String paymentMethod, String state) {
    	String sql = "UPDATE saleTransactions SET state=?, paymentMethod=? WHERE id=?";
        boolean success = false;
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, state);
            pstmt.setString(2, paymentMethod);
            pstmt.setInt(3, transactionId);
            pstmt.executeUpdate();
            success = true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }

        return success;
    }
    
    public boolean recordBalanceOperation(BalanceOperation balanceOperation) {
    	String sql = "INSERT INTO balanceOperations(id,date,money,type) VALUES(?,?,?,?)";
    	String date = balanceOperation.getDate().toString();
    	boolean success = false;
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, balanceOperation.getBalanceId());
			pstmt.setString(2, date);
			pstmt.setDouble(3, balanceOperation.getMoney());
			pstmt.setString(4, balanceOperation.getType());

			pstmt.executeUpdate();
			success = true;
		} catch (SQLException e) {
			System.err.println(e.getMessage());
			success = false;
		}

        return success;
    }
    
    public double getActualBalance() {
    	String sql = "SELECT SUM(money) as total FROM balanceOperations";
    	double total = 0;
    	try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
				total = rs.getDouble("total");
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
    	return total;
    }
    
    public List<BalanceOperation> getBalanceOperations(String from, String to) {
    	String sql = "SELECT id,date,money,type FROM balanceOperations WHERE date >=? AND date <=?";
    	List<BalanceOperation> operations =  new ArrayList<>();
    	try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, from);
			pstmt.setString(2, to);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				Integer balanceId = rs.getInt("id");
				String dateString = rs.getString("date");
				double money = rs.getDouble("money");
				String type = rs.getString("type");
				
				LocalDate date = LocalDate.parse(dateString);
				BalanceOperation operation = new BalanceOperationClass(balanceId, date, money, type);
				operations.add(operation);
			}
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
    	return operations;
    }
    
    public CreditCardClass getCreditCardByCardNumber(String cardNumber) {
		String sql = "SELECT * FROM creditCards WHERE creditCardNumber=?";
		CreditCardClass creditCard = null;
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, cardNumber);
			ResultSet rs = pstmt.executeQuery();
			creditCard = new CreditCardClass(rs.getString("creditCardNumber"), rs.getDouble("balance"));
		} catch (SQLException e) {
			System.err.println(e.getMessage());
		}
		return creditCard;
	}
    
    public boolean updateBalanceInCreditCard(String cardNumber, double newBalance) {
    	String sql = "UPDATE creditCards SET balance=? WHERE creditCardNumber=?";
        boolean success = false;
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setDouble(1, newBalance);
            pstmt.setString(2, cardNumber);
            pstmt.executeUpdate();
            success = true;
        } catch (SQLException e) {
            System.err.println(e.getMessage());
        }
        return success;
	}

}
