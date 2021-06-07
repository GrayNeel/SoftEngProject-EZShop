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
			/*
			 * connection.addUpdateListener(new SQLiteUpdateListener() {
			 * 
			 * @Override public void onUpdate(Type type, String db, String table, long
			 * rowId) { System.out.println("OnUpdate: " + type + " " + db + " " + table +
			 * " " + rowId); // Update data here } });
			 */
		} catch (SQLException e) {
		}
	}

	public EZShopDB() {
		createConnection();
	    User user = new UserClass(3, "admin", "strong", "Administrator");

		// Add user to the DB
	    addUser(user);
	}

	public boolean resetDB(String table) {
		String sql = "DELETE FROM " + table;
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.executeUpdate();
		} catch (SQLException e) {
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
			// If table is empty, gives id = 0
			id = rs.getInt("tot");
		} catch (SQLException e) {
		}

		return id;
	}

	/**
	 * This method adds a new user to the database "users"
	 * 
	 * @param user the UserClass containing parameters to add
	 */
	public boolean addUser(User user) {
		String sql = "INSERT INTO users(id,username,password,role) VALUES(?,?,?,?)";
		boolean success = false;
		if (user == null)
			return success;
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, user.getId());
			pstmt.setString(2, user.getUsername());
			pstmt.setString(3, user.getPassword());
			pstmt.setString(4, user.getRole());

			pstmt.executeUpdate();
			success = true;
		} catch (SQLException e) {
			success = false;
		}
		return success;
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
		}

		return exists;
	}

	public boolean deleteUser(Integer id) {
		String sql = "SELECT COUNT(*) AS tot FROM users WHERE id=?";
		boolean exists = false;

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();

			if (rs.getInt("tot") > 0)
				exists = true;

		} catch (SQLException e) {
			return false;
		}

		if (!exists)
			return false;

		String sql2 = "DELETE FROM users WHERE id=?";
		boolean success = false;
		try (PreparedStatement pstmt = connection.prepareStatement(sql2)) {
			pstmt.setInt(1, id);
			pstmt.executeUpdate();
			success = true;
		} catch (SQLException e) {
		}
		return success;
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
		}

		return user;
	}

	public boolean updateUserRole(Integer id, String role) {
		String existance = "SELECT COUNT(*) AS tot FROM users WHERE id=?";
		boolean exists = false;

		try (PreparedStatement pstmt = connection.prepareStatement(existance)) {
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();

			if (rs.getInt("tot") > 0)
				exists = true;

		} catch (SQLException e) {
			return false;
		}

		if (!exists)
			return false;

		String sql = "UPDATE users SET role=? WHERE id=?";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, role);
			pstmt.setInt(2, id);
			pstmt.executeUpdate();
		} catch (SQLException e) {
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

	// public User getLoggedUser() {
	// String sql = "SELECT id,username,password,role FROM loggedusers";
	// User user = null;
	//
	// try (Statement stmt = connection.createStatement(); ResultSet rs =
	// stmt.executeQuery(sql)) {
	//
	// user = new UserClass(rs.getInt("id"), rs.getString("username"),
	// rs.getString("password"),
	// rs.getString("role"));
	//
	// } catch (SQLException e) {
	// return null;
	// }
	//
	// return user;
	// }

	/**
	 * This method adds a new productType to the database "productTypes"
	 * 
	 * @param productType the ProductTypeClass containing parameters to add
	 */
	public boolean addProductType(ProductType productType) {
		String sql = "INSERT INTO productTypes(id, quantity, location, note, productDescription, barCode, pricePerUnit) VALUES(?,?,?,?,?,?,?)";

		if (productType == null)
			return false;

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, productType.getId());
			pstmt.setInt(2, productType.getQuantity());
			pstmt.setString(3, productType.getLocation());
			pstmt.setString(4, productType.getNote());
			pstmt.setString(5, productType.getProductDescription());
			pstmt.setString(6, productType.getBarCode());
			pstmt.setDouble(7, productType.getPricePerUnit());

			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			return false;
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
		}

		return exists;
	}

	public boolean updateProductType(Integer id, String newDescription, String newCode, double newPrice,
			String newNote) {
		String sql = "SELECT COUNT(*) AS tot FROM productTypes WHERE id=?";
		boolean exists = false;

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();

			if (rs.getInt("tot") > 0)
				exists = true;

		} catch (SQLException e) {
			// System.err.println(e.getMessage());
			return false;
		}

		if (!exists)
			return false;

		String sql2 = "UPDATE productTypes SET productDescription=?, barCode=?, pricePerUnit=?, note=? WHERE id=?";

		if (id < 0)
			return false;

		try (PreparedStatement pstmt = connection.prepareStatement(sql2)) {
			pstmt.setString(1, newDescription);
			pstmt.setString(2, newCode);
			pstmt.setDouble(3, newPrice);
			pstmt.setString(4, newNote);

			pstmt.setInt(5, id);
			pstmt.executeUpdate();
		} catch (SQLException e) {
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
		}

		return productTypeList;
	}

	public boolean deleteProductType(Integer id) {
		String sql = "SELECT COUNT(*) AS tot FROM productTypes WHERE id=?";
		boolean exists = false;

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();

			if (rs.getInt("tot") > 0)
				exists = true;

		} catch (SQLException e) {
			// System.err.println(e.getMessage());
			return false;
		}

		if (!exists)
			return false;

		String sql2 = "DELETE FROM productTypes WHERE id=?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql2)) {
			pstmt.setInt(1, id);
			pstmt.executeUpdate();
		} catch (SQLException e) {
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
		}

		return product;
	}

	public List<ProductType> getProductTypesByDescription(String description) {
		String sql = "SELECT * FROM productTypes WHERE productDescription LIKE (?)";
		List<ProductType> productTypeList = new ArrayList<>();

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			String toSend = "%" + description + "%";
			pstmt.setString(1, toSend);
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
		}

		return qty;
	}

	public String getPositionByProductTypeId(Integer id) {
		String sql = "SELECT location FROM productTypes WHERE id=?";
		String pos = "";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();
			pos = rs.getString("location");
		} catch (SQLException e) {
		}

		return pos;
	}

	public Integer getQuantityByProductTypeBarCode(String barCode) {
		String sql = "SELECT quantity FROM productTypes WHERE barCode=?";
		Integer qty = null;

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, barCode);
			ResultSet rs = pstmt.executeQuery();
			qty = rs.getInt("quantity");
		} catch (SQLException e) {
			// System.err.println(e.getMessage());
		}

		return qty;
	}
	
	public String getBarCodeByProductTypeId(Integer id) {
		String sql = "SELECT barCode FROM productTypes WHERE id=?";
		String brc = null;

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();
			brc = rs.getString("barCode");
		} catch (SQLException e) {
			// System.err.println(e.getMessage());
		}

		return brc;
	}

	public boolean updateQuantityByProductTypeId(Integer id, int newQuantity) {
		String sql = "SELECT COUNT(*) AS tot FROM productTypes WHERE id=?";
		boolean exists = false;

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();

			if (rs.getInt("tot") > 0)
				exists = true;

		} catch (SQLException e) {
			return false;
		}

		if (!exists)
			return false;

		String sql2 = "UPDATE productTypes SET quantity=? WHERE id=?";

		try (PreparedStatement pstmt = connection.prepareStatement(sql2)) {
			pstmt.setInt(1, newQuantity);
			pstmt.setInt(2, id);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			return false;
		}

		return true;
	}

	public boolean updateQuantityByBarCode(String productCode, int newQuantity) {
		if (productCode == null)
			return false;

		String sql = "UPDATE productTypes SET quantity=? WHERE barCode=?";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, newQuantity);
			pstmt.setString(2, productCode);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// System.err.println(e.getMessage());
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
			// System.err.println(e.getMessage());
		}

		if (res == 0)
			return false;
		else
			return true;
	}

	public boolean updateProductTypeLocation(Integer productId, String newPos) {
		String sql = "SELECT COUNT(*) AS tot FROM productTypes WHERE id=?";
		boolean exists = false;

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, productId);
			ResultSet rs = pstmt.executeQuery();

			if (rs.getInt("tot") > 0)
				exists = true;

		} catch (SQLException e) {
			// System.err.println(e.getMessage());
			return false;
		}

		if (!exists)
			return false;

		String sqlUpd = "UPDATE productTypes SET location=? WHERE id=?";

		try (PreparedStatement pstmt = connection.prepareStatement(sqlUpd)) {
			pstmt.setString(1, newPos);
			pstmt.setInt(2, productId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// System.err.println(e.getMessage());
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

		if (order == null)
			return false;

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, order.getOrderId());
			pstmt.setInt(2, order.getBalanceId());
			pstmt.setString(3, order.getProductCode());
			pstmt.setDouble(4, order.getPricePerUnit());
			pstmt.setInt(5, order.getQuantity());
			pstmt.setString(6, order.getStatus());

			pstmt.executeUpdate();
		} catch (SQLException e) {
			// System.err.println(e.getMessage());
			return false;
		}
		return true;
	}

	public boolean deleteOrder(Integer id) {
		String sql = "SELECT COUNT(*) AS tot FROM orders WHERE id=?";
		boolean exists = false;

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();

			if (rs.getInt("tot") > 0)
				exists = true;

		} catch (SQLException e) {
			// System.err.println(e.getMessage());
			return false;
		}

		if (!exists)
			return false;

		String sql2 = "DELETE FROM orders WHERE id=?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql2)) {
			pstmt.setInt(1, id);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			// System.err.println(e.getMessage()); // non serve checkare se esiste. Se non
			// esiste non viene cancellato
			// nulla
			return false;
		}
		return true;
	}

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
			// System.err.println(e.getMessage());
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
			// System.err.println(e.getMessage());
		}

		return orderList;
	}

	public boolean payOrderById(Integer orderId) {
		if (orderId == null)
			return false;

		String sql = "SELECT COUNT(*) AS tot FROM orders WHERE id=?";
		boolean exists = false;

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, orderId);
			ResultSet rs = pstmt.executeQuery();

			if (rs.getInt("tot") > 0)
				exists = true;

		} catch (SQLException e) {
			// System.err.println(e.getMessage());
			return false;
		}

		if (!exists)
			return false;

		// UPDATE status into ORDERED
		String sql2 = "UPDATE orders SET status=? WHERE id=?";

		try (PreparedStatement pstmt = connection.prepareStatement(sql2)) {
			pstmt.setString(1, "PAYED");
			pstmt.setInt(2, orderId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			return false;
		}
		return true;
	}

	public boolean setBalanceIdInOrder(Integer orderId, Integer balanceId) {
		String sql = "SELECT COUNT(*) AS tot FROM orders WHERE id=?";
		boolean exists = false;

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, orderId);
			ResultSet rs = pstmt.executeQuery();

			if (rs.getInt("tot") > 0)
				exists = true;

		} catch (SQLException e) {
			return false;
		}

		if (!exists)
			return false;

		// UPDATE status into ORDERED
		String sql2 = "UPDATE orders SET balanceId=? WHERE id=?";

		if (balanceId == null)
			return false;

		try (PreparedStatement pstmt = connection.prepareStatement(sql2)) {
			pstmt.setInt(1, balanceId);
			pstmt.setInt(2, orderId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			return false;
		}
		return true;
	}

	public boolean recordOrderArrivalById(Integer orderId) {
		if (orderId == null)
			return false;

		String sql = "SELECT COUNT(*) AS tot FROM orders WHERE id=?";
		boolean exists = false;

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, orderId);
			ResultSet rs = pstmt.executeQuery();

			if (rs.getInt("tot") > 0)
				exists = true;

		} catch (SQLException e) {
			return false;
		}

		if (!exists)
			return false;
		// UPDATE status into COMPLETED
		String sql2 = "UPDATE orders SET status=? WHERE id=?";

		try (PreparedStatement pstmt = connection.prepareStatement(sql2)) {
			pstmt.setString(1, "COMPLETED");
			pstmt.setInt(2, orderId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			return false;
		}
		return true;
	}

	public boolean defineCustomer(CustomerClass customer) {
		if (customer == null)
			return false;
		String sql = "INSERT INTO customers(id,customerName,customerCard,points) VALUES(?,?,?,?)";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, customer.getId());
			pstmt.setString(2, customer.getCustomerName());
			pstmt.setString(3, customer.getCustomerCard());
			pstmt.setInt(4, customer.getPoints());

			pstmt.executeUpdate();
		} catch (SQLException e) {
			return false;
		}
		return true;
	}

	public boolean deleteCustomer(Integer id) {
		String sql0 = "SELECT COUNT(*) AS tot FROM customers WHERE id=?";
		boolean exists = false;

		try (PreparedStatement pstmt = connection.prepareStatement(sql0)) {
			pstmt.setInt(1, id);
			ResultSet rs = pstmt.executeQuery();

			if (rs.getInt("tot") > 0)
				exists = true;

		} catch (SQLException e) {
			return false;
		}

		if (!exists)
			return false;

		String sql = "DELETE FROM customers WHERE id=?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, id);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			return false;
		}

		return true;
	}

	public boolean updateCustomer(Integer id, String newCustomerName, String newCustomerCard) {
		// Check if card exists
		String sql1 = "SELECT COUNT(*) AS c FROM cards WHERE id=?";

		try (PreparedStatement pstmt = connection.prepareStatement(sql1)) {
			pstmt.setString(1, newCustomerCard);
			ResultSet rs = pstmt.executeQuery();

			if (rs.getInt("c") == 0 && newCustomerCard.length() != 0)
				return false;

		} catch (SQLException e) {
			return false;
		}		
		
		if (newCustomerCard == null) 			
				return false;
			
		if (newCustomerCard.length() == 0) {
			String sql = "UPDATE customers SET customerName=?, customerCard='' WHERE id=?";
			try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
				pstmt.setString(1, newCustomerName);
				pstmt.setInt(2, id);
				pstmt.executeUpdate();
			} catch (SQLException e) {
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
		}

		return customerlist;
	}

	public boolean attachCardToCustomer(String customerCard, Integer customerId) {
		// Check if user is related
		String sql0 = "SELECT COUNT(*) AS n FROM customers WHERE id=?";

		try (PreparedStatement pstmt = connection.prepareStatement(sql0)) {
			pstmt.setInt(1, customerId);
			ResultSet rs = pstmt.executeQuery();

			if (rs.getInt("n") == 0)
				return false;

		} catch (SQLException e) {
			return false;
		}

		// Check if card exists
		String sql1 = "SELECT COUNT(*) AS c FROM cards WHERE id=?";

		try (PreparedStatement pstmt = connection.prepareStatement(sql1)) {
			pstmt.setString(1, customerCard);
			ResultSet rs = pstmt.executeQuery();

			if (rs.getInt("c") == 0)
				return false;

		} catch (SQLException e) {
			return false;
		}

		String sql = "UPDATE customers SET customerCard=? WHERE id=?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, customerCard);
			pstmt.setInt(2, customerId);			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			return false;
		}
		
		sql = "UPDATE cards SET assigned=1 WHERE id=? and assigned=0";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, customerCard);						
			pstmt.executeUpdate();
		} catch (SQLException e) {
			return false;
		}

		return true;
	}

	public Integer getCardPoints(String customerCard) {
		String sql1 = "SELECT COUNT(*) AS c FROM cards WHERE id=?";

		try (PreparedStatement pstmt = connection.prepareStatement(sql1)) {
			pstmt.setString(1, customerCard);
			ResultSet rs = pstmt.executeQuery();
			if (rs.getInt("c") == 0)
				return null;
		} catch (SQLException e) {
			return null;
		}
		
		String sql = "SELECT points from cards WHERE id=?";
		Integer points = null;
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, customerCard);
			ResultSet rs = pstmt.executeQuery();
			points = rs.getInt("points");

		} catch (SQLException e) {
			return null;
		}
		return points;
	}

	public boolean updateCardPoints(String customerCard, Integer points) {
		// Check if card exists
		String sql1 = "SELECT COUNT(*) AS c FROM cards WHERE id=?";

		try (PreparedStatement pstmt = connection.prepareStatement(sql1)) {
			pstmt.setString(1, customerCard);
			ResultSet rs = pstmt.executeQuery();
			if (rs.getInt("c") == 0)
				return false;
		} catch (SQLException e) {
			return false;
		}

		String sql = "UPDATE cards SET points=? WHERE id=?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, points);
			pstmt.setString(2, customerCard);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			return false;
		}
		return true;
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
		}

		return transaction;
	}

	public boolean applyDiscountRateToProduct(Integer transactionId, String productCode, Double discountRate) {
		boolean flag = false;

		if (discountRate < 0 || discountRate > 1)
			return false;

		String sql = "UPDATE productEntries SET discountRate=? WHERE transactionId=? and productCode=?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setDouble(1, discountRate);
			pstmt.setInt(2, transactionId);
			pstmt.setString(3, productCode);
			pstmt.executeUpdate();
			flag = true;
		} catch (SQLException e) {
		}

		return flag;
	}

	public boolean applyDiscountRate(Integer transactionId, Double discountRate) {
		boolean flag = false;
		if (discountRate < 0 || discountRate > 1)
			return false;

		String sql = "UPDATE saleTransactions SET discountRate=? WHERE id=?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setDouble(1, discountRate);
			pstmt.setInt(2, transactionId);
			pstmt.executeUpdate();
			flag = true;
		} catch (SQLException e) {
		}

		return flag;
	}

	public boolean createTicketEntry(TicketEntry ticketEntry, Integer transactionId) {
		if (ticketEntry == null || ticketEntry.getDiscountRate() < 0 || ticketEntry.getDiscountRate() >= 1)
			return false;
		Integer id = getLastId("productEntries");
		String sql = "INSERT INTO productEntries(id, productCode, amount, total, transactionId, unitPrice, discountRate) VALUES(?,?,?,?,?,?,?)";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, id + 1);
			pstmt.setString(2, ticketEntry.getBarCode());
			pstmt.setInt(3, ticketEntry.getAmount());
			pstmt.setDouble(4,
					ticketEntry.getAmount() * ticketEntry.getPricePerUnit() * (1 - ticketEntry.getDiscountRate()));
			pstmt.setInt(5, transactionId);
			pstmt.setDouble(6, ticketEntry.getPricePerUnit());
			pstmt.setDouble(7, ticketEntry.getDiscountRate());

			pstmt.executeUpdate();
		} catch (SQLException e) {
			return false;
		}

		return true;
	}

	public boolean updateTransactionState(Integer transactionId, String state) {
		if (state == null)
			return false;
		// Check if transaction exists
		String sql1 = "SELECT COUNT(*) AS st FROM saleTransactions WHERE id=?";

		try (PreparedStatement pstmt = connection.prepareStatement(sql1)) {
			pstmt.setInt(1, transactionId);
			ResultSet rs = pstmt.executeQuery();
			if (rs.getInt("st") == 0)
				return false;
		} catch (SQLException e) {
			return false;
		}

		String sql = "UPDATE saleTransactions SET state=? WHERE id=?";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, state);
			pstmt.setInt(2, transactionId);
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			return false;
		}
	}

	///////////////// Pablo write methods after this point

	public boolean deleteSaleTransaction(Integer transactionId) {
		// Check if card exists
		String sql1 = "SELECT COUNT(*) AS sn FROM saleTransactions WHERE id=?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql1)) {
			pstmt.setInt(1, transactionId);
			ResultSet rs = pstmt.executeQuery();
			if (rs.getInt("sn") == 0)
				return false;
		} catch (SQLException e) {
			return false;
		}

		String sql = "DELETE FROM saleTransactions WHERE state != 'PAYED' AND id=?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, transactionId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			return false;
		}

		sql = "DELETE FROM productEntries WHERE transactionId=?";
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, transactionId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			return false;
		}

		return true;
	}

	public SaleTransaction getClosedSaleTransactionById(Integer transactionId, List<TicketEntry> p) {
		String sql = "SELECT id,discountRate,date,time,price,paymentType,state FROM saleTransactions WHERE (state = 'PAYED' OR state = 'CLOSED') AND id=?";
		SaleTransaction saletransaction = null;
		ResultSet rs = null;
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, transactionId);
			rs = pstmt.executeQuery();
			saletransaction = new SaleTransactionClass(rs.getInt("id"), rs.getString("date"), rs.getString("time"),
					rs.getDouble("price"), rs.getString("paymentType"), rs.getDouble("discountRate"), p,
					rs.getString("state"));
		} catch (SQLException e) {
			return null;
		}

		return saletransaction;
	}

	public List<TicketEntry> getProductEntriesByTransactionId(Integer transactionId) {
		String sql = "SELECT productEntries.id AS id,productEntries.productCode as productCode,productTypes.productDescription AS productDescription,productEntries.amount AS amount,productTypes.pricePerUnit AS pricePerUnit"
				+ " FROM productTypes JOIN productEntries ON productTypes.barCode=productEntries.productCode WHERE productEntries.transactionId = ?";
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
		}

		return returntransaction;
	}

	public double getPricePerUnit(String productCode) {
		String sql = "SELECT pricePerUnit FROM productTypes WHERE barCode=?";
		double result = 0;
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, productCode);
			ResultSet rs = pstmt.executeQuery();
			result = rs.getDouble("pricePerUnit");
		} catch (SQLException e) {
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
			result = rs.getDouble("total");
		} catch (SQLException e) {
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
			if (rs.getInt("tot") > 0)
				exists = true;

		} catch (SQLException e) {
		}
		return exists;
	}

	public boolean updateReturnTransaction(Integer returnId, Integer newAmount, Double newReturnValue) {
		String sql = "UPDATE returnTransactions SET quantity=? , returnValue=? , state=? WHERE id=?";

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, newAmount);
			pstmt.setDouble(2, newReturnValue);
			pstmt.setString(3, "CLOSED");
			pstmt.setInt(4, returnId);
			pstmt.executeUpdate();
		} catch (SQLException e) {
			return false;
		}

		return true;
	}

	public boolean updateSaleTransactionAfterCommit(Integer transactionId, Double newReturnValue) {
		String sql = "UPDATE saleTransactions SET price=?, state='CLOSED' WHERE id=?";
		boolean success = false;

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setDouble(1, newReturnValue);
			pstmt.setInt(2, transactionId);
			pstmt.executeUpdate();
			success = true;
		} catch (SQLException e) {
		}

		return success;
	}

	public boolean updateEntryAfterCommit(Integer transactionId, String productCode, int newAmountSold,
			double newTotalSold) {
		String sql = "UPDATE productEntries SET amount=?, total=? WHERE transactionId=? AND productCode=?";
		boolean flag = false;

		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, newAmountSold);
			pstmt.setDouble(2, newTotalSold);
			pstmt.setInt(3, transactionId);
			pstmt.setString(4, productCode);
			pstmt.executeUpdate();
			flag = true;
		} catch (SQLException e) {
		}
		return flag;
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

				ProductReturnClass returnEntry = new ProductReturnClass(id, returnId, productCode, quantity,
						returnValue);
				returnlist.add(returnEntry);
			}
		} catch (SQLException e) {
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
		}

		return success;
	}

	public boolean updatePaymentSaleTransaction(Integer transactionId, String paymentMethod, String state) {
		String sql = "UPDATE saleTransactions SET state=?, paymentType=? WHERE id=?";
		boolean success = false;
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, state);
			pstmt.setString(2, paymentMethod);
			pstmt.setInt(3, transactionId);
			pstmt.executeUpdate();
			success = true;
		} catch (SQLException e) {
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
		}

		return success;
	}

	public double getActualBalance() {
		String sql = "SELECT SUM(money) as total FROM balanceOperations";
		double total = 0;
		try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
			total = rs.getDouble("total");
		} catch (SQLException e) {
		}
		return total;
	}

	public List<BalanceOperation> getBalanceOperations(String from, String to) {
		String sql = "SELECT id,date,money,type FROM balanceOperations WHERE date >=? AND date <=?";
		List<BalanceOperation> operations = new ArrayList<>();
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
		}
		return operations;
	}

	public boolean recordCreditCard(Integer id, CreditCardClass creditCard) {
		String sql = "INSERT INTO creditCards(id,creditCardNumber,balance) VALUES(?,?,?)";
		boolean success = false;
		if (creditCard == null) {
			return success;
		}
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, id);
			pstmt.setString(2, creditCard.getCardNumber());
			pstmt.setDouble(3, creditCard.getBalance());

			pstmt.executeUpdate();
			success = true;
		} catch (SQLException e) {
		}

		return success;
	}

	public boolean deleteCreditCard(Integer id) {
		String sql = "DELETE FROM creditCards WHERE id=?";
		boolean success = false;
		if (id <0) {
			return success;
		}
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, id);
			pstmt.executeUpdate();
			success = true;
		} catch (SQLException e) {
		}

		return success;
	}

	public CreditCardClass getCreditCardByCardNumber(String cardNumber) {
		String sql = "SELECT * FROM creditCards WHERE creditCardNumber=?";
		CreditCardClass creditCard = null;
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, cardNumber);
			ResultSet rs = pstmt.executeQuery();
			creditCard = new CreditCardClass(rs.getString("creditCardNumber"), rs.getDouble("balance"));
		} catch (SQLException e) {
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
		}
		return success;
	}

	public boolean deleteCustomerCard(String customerCard) {
		String sql = "DELETE FROM cards WHERE id=?";
		boolean success = false;
		
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, customerCard);
			pstmt.executeUpdate();
			success = true;
		} catch (SQLException e) {
		}

		return success;		
	}

	
	/**
	 * The following methods are here to handle RFID new change
	 */
	public boolean recordProductRFID(ProductClass prod) {
		String sql = "INSERT INTO products(RFID, id) VALUES(?,?)";

		if(prod.getId() < 0 || prod.getRFID().length() != 10 || Double.parseDouble(prod.getRFID()) < 0)
			return false;
		
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setString(1, prod.getRFID());
			pstmt.setInt(2, prod.getId());
			
			pstmt.executeUpdate();
		} catch (SQLException e) {
			return false;
		}

		return true;
	}
	
	public List<ProductClass> getAllProductsRFID() {
		String sql = "SELECT RFID, id FROM products";
		List<ProductClass> prodlist = new ArrayList<>();

		try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {

			while (rs.next()) {
				Integer id = rs.getInt("id");
				String RFID = rs.getString("RFID");

				ProductClass prod = new ProductClass(id, RFID);
				prodlist.add(prod);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return prodlist;
	}
	
	public List<ProductClass> getProductsRFIDbyId(Integer id) {
		String sql = "SELECT RFID, id FROM products WHERE id=?";
		List<ProductClass> prodlist = new ArrayList<>();
		
		try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
			pstmt.setInt(1, id);
			
			ResultSet rs = pstmt.executeQuery();
			
			while (rs.next()) {
				Integer pid = rs.getInt("id");
				String RFID = rs.getString("RFID");

				ProductClass prod = new ProductClass(pid, RFID);
				prodlist.add(prod);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return prodlist;
	}
}
