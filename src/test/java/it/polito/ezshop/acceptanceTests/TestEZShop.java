package it.polito.ezshop.acceptanceTests;

import it.polito.ezshop.classes.*;

import it.polito.ezshop.data.Customer;
import it.polito.ezshop.data.*;
import it.polito.ezshop.data.Order;
import it.polito.ezshop.data.ProductType;

import static org.junit.Assert.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

public class TestEZShop {
	EZShopDB db = new EZShopDB();
	
/////////////////////////////////////////// Testing Functions
	
/////////////////////////////////////// Pablo
	@Test
	public void resetDBTestCase() {
		// Test for tables with getAll method
		List<User> userlist = db.getAllUsers();
		assertNotNull(userlist);
		
		assertTrue(db.resetDB("users"));
		
		List<User> emptyList = db.getAllUsers();
		assertTrue(emptyList.isEmpty());
		
		for(User us : userlist) {
			db.addUser(us);
		}
		
		List<ProductType> ptlist = db.getAllProductTypes();
		assertNotNull(ptlist);
		
		assertTrue(db.resetDB("productTypes"));
		
		List<ProductType> emptyptlist = db.getAllProductTypes();
		assertTrue(emptyptlist.isEmpty());
		
//		for(ProductType prod : ptlist) {
//			db.addProductType(prod);
//		}
		
		List<Order> orderlist = db.getAllOrders();
		assertNotNull(orderlist);
		
		assertTrue(db.resetDB("orders"));
		
		List<Order> emptyOrderList = db.getAllOrders();
		assertTrue(emptyOrderList.isEmpty());
		
		for(Order order : orderlist) {
			db.addAndIssueOrder(order);
		}
		
		assertFalse(db.resetDB("nonexistingtable"));

		
	}
	
	@Test
	public void getterAndSetterUserTestCase() {
		User user = new UserClass(1, "andrea.admin", "1234567", "testrole");
		assertNotNull(user);
		
		user.setId(2);
		Integer id = user.getId();
		assert(id == 2);
		
		user.setUsername("andrea.rossi");
		String username = user.getUsername();
		assert(username == "andrea.rossi");
		
		user.setPassword("andrea123");
		String password = user.getPassword();
		assert(password == "andrea123");
		
		user.setRole("Administrator");
		String role = user.getRole();
		assert(role == "Administrator");
	}
	
	@Test
	public void getAllUsersTestCase() {
		User user = new UserClass(10, "andrea.admin", "1234567", "testrole");
		
		boolean flag = db.addUser(user);
		System.out.print(flag);
		List<User> userlist = db.getAllUsers();
		assertNotNull(userlist);
		
		db.resetDB("users");
		
		assertTrue(db.getAllUsers().isEmpty());
		
		for(User us : userlist) {
			db.addUser(us);
		}
		db.deleteUser(10);
	}
	
	@Test
	public void addAndDeleteUserTestCase() {
		User user1 = new UserClass(7, "newAdmin", "1234567", "Administrator");
		User usernull = null;
		// Adding a new user
		assertTrue(db.addUser(user1));
		User user2 = db.getUserById(7);
		assertNotNull(user2);
		assertFalse(db.addUser(usernull));
		assert(user2.getId() == 7);
		assert(user2.getUsername().equals("newAdmin"));
		assert(user2.getPassword().equals("1234567"));
		assert(user2.getRole().equals("Administrator"));
	
		// Repeating an Id in database
		User failedUser= new UserClass(7, "failedUsername", "failedPassword", "failedRole");
		assertFalse(db.addUser(failedUser));
		User testFailUser = db.getUserById(7);
		assert(testFailUser.getUsername() != "failedUsername");
		assert(testFailUser.getPassword() != "failedPassword");
		assert(testFailUser.getRole() != "failedRole");
		assert(testFailUser.getUsername().equals("newAdmin"));
		assert(testFailUser.getPassword().equals("1234567"));
		assert(testFailUser.getRole().equals("Administrator"));

		assertTrue(db.deleteUser(7));
		assertFalse(db.deleteUser(100));
		assertFalse(db.checkExistingUser("newAdmin"));
	}
	
	@Test
	public void checkExistingUserTestCase() {
		User user = new UserClass(20, "Alberto1", "12345678", "Cashier");
		
		db.addUser(user);
		assertTrue(db.checkExistingUser("Alberto1"));
		db.deleteUser(20);
		assertFalse(db.checkExistingUser("Alberto1"));
	}
	
	@Test
	public void getUserByIdTestCase() {
		User user = new UserClass(20, "Alberto1", "12345678", "Cashier");
		db.addUser(user);
		
		User response = db.getUserById(20);
		assertNotNull(response);
	
		assert(response.getId() == 20);
		assert(response.getUsername().equals("Alberto1"));
		assert(response.getPassword().equals("12345678"));
		assert(response.getRole().equals("Cashier"));
		
		User nullResponse = db.getUserById(100);
		assertNull(nullResponse);
		
		db.deleteUser(20);
	}
	
	@Test
	public void getUserByCredentialsTestCase() {
		User user = new UserClass(20, "Alberto1", "12345678", "Cashier");
		db.addUser(user);
		
		User response = db.getUserByCredentials("Alberto1", "12345678");
		assertNotNull(response);
		assert(response.getUsername().equals("Alberto1"));
		assert(response.getPassword().equals("12345678"));
		
		User nullResponse = db.getUserByCredentials("falseusername", "falsepassword");
		assertNull(nullResponse);
		User badPassword = db.getUserByCredentials("Alberto1", "falsepassword");
		assertNull(badPassword);
		User badUsername = db.getUserByCredentials("falseusername", "12345678");
		assertNull(badUsername);
		db.deleteUser(20);
	}
	
	@Test
	public void getLastIdTestCase() {
		User user = new UserClass(30, "Test1", "12345678", "Cashier");
		db.addUser(user);
		
		Integer lastIdUser = db.getLastId("users");
		assert(lastIdUser == 30);
		assertFalse(lastIdUser.equals(31));
		
		ProductType pt = new ProductTypeClass(1741, 2, "location", "test", "this is a test", "22345212", 3.22);
		db.addProductType(pt);
		
		Integer lastIPT = db.getLastId("productTypes");
		assert(lastIPT == 1741);
		assertFalse(lastIPT.equals(1742));
		
		CustomerClass c = new CustomerClass(12,  "Carlo", "1423673214", 122);
		db.defineCustomer(c);
		
		Integer lastCustomer = db.getLastId("customers");
		assert(lastCustomer == 12);
		assertFalse(lastCustomer.equals(13));
		
    	Order o = new OrderClass(5853,-1, "1741", 8.5, 5, "ISSUED");
		db.addAndIssueOrder(o);
		
		Integer lastOrder = db.getLastId("orders");
		assert(lastOrder == 5853);
		assertFalse(lastOrder.equals(5854));
		
		db.deleteUser(30);
		db.deleteProductType(1741);
		db.deleteCustomer(12);
		db.deleteOrder(5853);
	}
	
	
	@Test
	public void updateUserRoleTestCase() {
		User user = new UserClass(20, "Alberto1", "12345678", "Cashier");
		db.addUser(user);
		
		assertTrue(db.updateUserRole(20, "Administrator"));
		assertFalse(db.updateUserRole(100, "Role"));

		User updatedUser = db.getUserById(20);
		assert(updatedUser.getRole() != "Cashier");
		assert(updatedUser.getRole().equals("Administrator"));
		
		db.deleteUser(20);
	}
	
/////////////////////////////////////// Marco S.
	@Test
	public void loopCoverageTestCase() {
		db.resetDB("productTypes");
		ProductType pt1 = new ProductTypeClass(1741, 2, "location", "test", "this is a test", "22345212", 3.22);
		ProductType pt2 = new ProductTypeClass(1742, 3, "location", "test", "this is a test", "223434232", 3.22);
		
		List<ProductType> ptlist = db.getAllProductTypes();
		assert(ptlist.size() == 0);
		
		db.addProductType(pt1);
		ptlist = db.getAllProductTypes();
		assert(ptlist.size() == 1);

		db.addProductType(pt2);
		ptlist = db.getAllProductTypes();
		assert(ptlist.size() == 2);

		db.resetDB("productTypes");
	}
	
	@Test
	public void getterAndSetterProductTypeTestCase() {
		ProductType pt = new ProductTypeClass(1, 0, "location", "test", "this is a test", "2222222", 3.22);
		assertNotNull(pt);
		
		pt.setId(5);
		Integer id = pt.getId();
		assert(id == 5);
		
		pt.setQuantity(2);
		Integer quantity = pt.getQuantity();
		assert(quantity == 2);
		
		pt.setLocation("testLoc");
		String location = pt.getLocation();
		assert(location == "testLoc");
		
		pt.setNote("note");
		String note = pt.getNote();
		assert(note == "note");
		
		pt.setProductDescription("desc");
		String productDesc = pt.getProductDescription();
		assert(productDesc == "desc");
		
		pt.setBarCode("03030");
		String barCode = pt.getBarCode();
		assert(barCode == "03030");
		
		pt.setPricePerUnit(1.10);
		Double price = pt.getPricePerUnit();
		assert(price == 1.10);
	}
	
	@Test
	public void getterAndSetterOrderTestCase() {
		//Integer orderId, Integer balanceId, String productCode, Double pricePerUnit, Integer quantity, String status
		Order o = new OrderClass(1,-1, "333", 2.01, 5, "ISSUED");
		assertNotNull(o);
		
		o.setOrderId(5);
		Integer id = o.getOrderId();
		assert(id == 5);
		
		o.setBalanceId(4);
		Integer bid = o.getBalanceId();
		assert(bid == 4);
		
		o.setProductCode("20202");
		String pc = o.getProductCode();
		assert(pc == "20202");
		
		o.setPricePerUnit(2.01);
		Double price = o.getPricePerUnit();
		assert(price == 2.01);
		
		o.setQuantity(22);
		Integer qty = o.getQuantity();
		assert (qty == 22);
		
		o.setStatus("PAYED");
		String status = o.getStatus();
		assert (status == "PAYED");
	}
	
	@Test
	public void validateProductCodeTestCase() {
		assertFalse(ProductTypeClass.validateProductCode("df"));
		assertFalse(ProductTypeClass.validateProductCode(""));
		assertFalse(ProductTypeClass.validateProductCode("1234567"));
		assertFalse(ProductTypeClass.validateProductCode("1234567890"));
		assertFalse(ProductTypeClass.validateProductCode("333333333333333"));
		assertFalse(ProductTypeClass.validateProductCode("44444444444444444444"));
		assertFalse(ProductTypeClass.validateProductCode("12345678"));
		assertTrue(ProductTypeClass.validateProductCode("12345670"));
		assertTrue(ProductTypeClass.validateProductCode("123456756328"));
		assertFalse(ProductTypeClass.validateProductCode("123456756324"));
		assertFalse(ProductTypeClass.validateProductCode("8717163994254"));
		assertTrue(ProductTypeClass.validateProductCode("8717163994250"));
		assertFalse(ProductTypeClass.validateProductCode("12344674332822"));
		assertTrue(ProductTypeClass.validateProductCode("12344674332827"));
		assertFalse(ProductTypeClass.validateProductCode("12344674332827777"));
		assertTrue(ProductTypeClass.validateProductCode("12344674332827772"));
		assertFalse(ProductTypeClass.validateProductCode("123446743328277775"));
		assertTrue(ProductTypeClass.validateProductCode("123446743328277771"));
	}
	
	@Test
	public void addAndDeleteProductTypeTestCase() {
		ProductType pt = new ProductTypeClass(1741, 2, "location", "test", "this is a test", "22345212", 3.22);
		
		assertTrue(db.addProductType(pt));
		assertFalse(db.addProductType(null));
		
		assertTrue(db.deleteProductType(1741));
		assertFalse(db.deleteProductType(-1));
	}
	
	@Test
	public void checkExistingProductTypeTestCase() {
		ProductType pt = new ProductTypeClass(1741, 2, "location", "test", "this is a test", "22345212", 3.22);
		
		db.addProductType(pt);
		assertTrue(db.checkExistingProductType("22345212"));
		db.deleteProductType(1741);
		assertFalse(db.checkExistingProductType("22345212"));
	}
	
	@Test
	public void updateProductTypeTestCase() {
		ProductType pt = new ProductTypeClass(1741, 2, "location", "test", "this is a test", "22345212", 3.22);
		
		db.addProductType(pt);
		assertFalse(db.updateProductType(-1, "ok", "333", 4.18, "good"));
		//assertFalse(db.updateProductType(1741, 3, "333", 4.18, "good"));
		assertTrue(db.updateProductType(1741, "ok", "333", 4.18, "good"));
		db.deleteProductType(1741);
	}
	
	@Test
	public void getAllProductTypesTestCase() {
		ProductType pt = new ProductTypeClass(1741, 2, "location", "test", "this is a test", "22345212", 3.22);
		
		db.addProductType(pt);
		
		List<ProductType> ptlist = db.getAllProductTypes();
		assertNotNull(ptlist);
		
		ptlist.remove(pt);
		db.deleteProductType(1741);
		
		db.resetDB("productTypes");
		
		assertTrue(db.getAllProductTypes().isEmpty());
		
		for(ProductType prod : ptlist) {
			db.addProductType(prod);
		}
	}
	
	@Test
	public void getProductTypeByBarCodeTestCase() {
		ProductType pt = new ProductTypeClass(1741, 2, "location", "test", "this is a test", "22345212", 3.22);
		db.addProductType(pt);
		
		assertNotNull(db.getProductTypeByBarCode("22345212"));
		assertNull(db.getProductTypeByBarCode("222"));
		db.deleteProductType(1741);
	}
	
	@Test
	public void getProductTypesByDescriptionTestCase() {
		ProductType pt = new ProductTypeClass(17441, 23, "locationthis", "test", "nice", "223452212", 3.22);
		db.addProductType(pt);
		
		ProductType pt2 = new ProductTypeClass(17442, 3, "locationals", "test2", "nicest", "223345214", 4.22);
		db.addProductType(pt2);
		
		//NOT WORKING:
		List<ProductType> ptlist = db.getProductTypesByDescription("nic");
		assert(ptlist.size() >= 2);
		//GIVES 0 AS RESULT
		
		assertNotNull(db.getProductTypesByDescription("nicest"));
		assertNotNull(db.getProductTypesByDescription("nice"));

		assertTrue(db.getProductTypesByDescription("222dasdas12134").isEmpty());
		
		db.deleteProductType(1741);
		db.deleteProductType(1742);
	}
	
	@Test
	public void getQuantityByBarCodeTestCase() {
		ProductType pt = new ProductTypeClass(1741, 2, "location", "test", "nice", "22345212", 3.22);
		db.addProductType(pt);
		
		assert(db.getQuantityByProductTypeBarCode("22345212") == 2);
		assertNull(db.getQuantityByProductTypeBarCode(null));
		
		db.deleteProductType(1741);
	}
	
	@Test
	public void updateQuantityByBarCodeTestCase() {
		db.resetDB("productTypes");
		ProductType pt = new ProductTypeClass(1741, 2, "location", "test", "nice", "22345212", 3.22);
		db.addProductType(pt);
		
		assertTrue(db.updateQuantityByBarCode("22345212", 5));
		
		assert(db.getQuantityByProductTypeBarCode("22345212") == 5);
		
		assertFalse(db.updateQuantityByBarCode(null, -1));
		
		db.deleteProductType(1741);
	}
	
	@Test
	public void getQuantityByProductTypeIdTestCase() {
		db.resetDB("productTypes");
		ProductType pt = new ProductTypeClass(1741, 2, "location", "test", "this is a test", "22345212", 3.22);
		db.addProductType(pt);
		
		assert(db.getQuantityByProductTypeId(1741) == 2);
		assertNull(db.getQuantityByProductTypeId(-1));
		
		db.deleteProductType(1741);
	}
	
	@Test
	public void updateQuantityByProductTypeIdTestCase() {
		db.resetDB("productTypes");
		ProductType pt = new ProductTypeClass(1741, 2, "location", "test", "this is a test", "22345212", 3.22);
		db.addProductType(pt);
		
		assertFalse(db.updateQuantityByProductTypeId(-1, 4));
		//assertFalse(db.updateQuantityByProductTypeId(1741, "434");
		assertTrue(db.updateQuantityByProductTypeId(1741, 4));
		assert(db.getQuantityByProductTypeId(1741) == 4);
		
		db.deleteProductType(1741);
	}
	
	@Test
	public void isLocationUsedTestCase() {
		ProductType pt = new ProductTypeClass(1741, 2, "4-4-4", "test", "this is a test", "22345212", 3.22);
		db.addProductType(pt);
		
		assertTrue(db.isLocationUsed("4-4-4"));
		
		db.deleteProductType(1741);
		
		assertFalse(db.isLocationUsed("4-4-4"));
	}
	
	@Test
	public void updateProductTypeLocationTestCase() {
		ProductType pt = new ProductTypeClass(1741, 2, "4-4-4", "test", "this is a test", "22345212", 3.22);
		db.addProductType(pt);
		
		assertTrue(db.updateProductTypeLocation(1741,"5-5-5"));
		assertFalse(db.updateProductTypeLocation(-1,"5-5-5"));
		
		db.deleteProductType(1741);
	}
	
	@Test
	public void addAndIssueOrderThenDeleteTestCase() {
    	Order o = new OrderClass(5853,-1, "1741", 8.5, 5, "ISSUED");
    	
    	assertTrue(db.addAndIssueOrder(o));
    	assertFalse(db.addAndIssueOrder(null));
    	assertTrue(db.deleteOrder(5853));
    	assertFalse(db.deleteOrder(-1));
	}
	
	@Test
	public void setBalanceIdInOrderTestCase() {
    	Order o = new OrderClass(5853,-1, "1741", 8.5, 5, "ISSUED");
    	
    	db.addAndIssueOrder(o);
    	
    	assertFalse(db.setBalanceIdInOrder(-1, 500));
    	assertFalse(db.setBalanceIdInOrder(5853, null));
    	assertTrue(db.setBalanceIdInOrder(5853, 500));
    	
    	db.deleteOrder(5853);
	}
	
	@Test
	public void payOrderByIdTestCase() {
    	Order o = new OrderClass(5853,-1, "1741", 8.5, 5, "ISSUED");
    	
    	db.addAndIssueOrder(o);
    	
    	assertFalse(db.payOrderById(null));
    	assertTrue(db.payOrderById(5853));
    	
    	assert(db.getOrderById(5853).getStatus().equals("PAYED"));
    	
    	db.deleteOrder(5853);
	}
	
	@Test
	public void recordOrderArrivalByIdTestCase() {
    	Order o = new OrderClass(5853,-1, "1741", 8.5, 5, "ISSUED");
    	
    	db.addAndIssueOrder(o);
    	db.payOrderById(5853);
    	
    	assertFalse(db.recordOrderArrivalById(null));
    	assertFalse(db.recordOrderArrivalById(-1));
    	assertTrue(db.recordOrderArrivalById(5853));
    	
    	assert(db.getOrderById(5853).getStatus().equals("COMPLETED"));
    	
    	db.deleteOrder(5853);
	}
	
	@Test
	public void getAllOrdersTestCase() {
		Order o = new OrderClass(5853,-1, "1741", 8.5, 5, "ISSUED");
		
		db.addAndIssueOrder(o);
		
		List<Order> orlist = db.getAllOrders();
		assertNotNull(orlist);
		
		orlist.remove(o);
		db.deleteOrder(5853);
		
		db.resetDB("orders");
		
		assertTrue(db.getAllOrders().isEmpty());
		
		for(Order or : orlist) {
			db.addAndIssueOrder(or);
		}
	}

/////////////////////////////////////// Francesco
	@Test
	public void getterAndSetterCustomerTestCase() {
		CustomerClass c = new CustomerClass(1,  "Carlo", "1423673214", 122);
		assertNotNull(c);
		
		c.setId(5);
		Integer id = c.getId();
		assert(id == 5);
		
		c.setCustomerName("Alberto");
		String customerName = c.getCustomerName();
		assert(customerName == "Alberto");
		
		c.setCustomerCard("4562342134");
		String customerCard = c.getCustomerCard();
		assert(customerCard == "4562342134");
		
		c.setPoints(444);
		Integer points = c.getPoints();
		assert(points == 444);	
		
	}
		

	@Test
	public void addAndDeleteCustomerTestCase() {
		db.resetDB("customers");
		CustomerClass c = new CustomerClass(18,  "Stefano", "1478973214", 178);
		CustomerClass cnull = null;
		assertTrue(db.defineCustomer(c));		
		assertFalse(db.defineCustomer(c));
		assertFalse(db.defineCustomer(cnull));
		
		assertTrue(db.deleteCustomer(18));		
		assertFalse(db.deleteCustomer(440));
	}
	
	@Test
	public void modifyCustomerTestCase() {
		db.resetDB("customers");
		CustomerClass c = new CustomerClass(12,  "Giacomo", "1423673214", 122);
		db.defineCustomer(c);
	
		assertTrue(db.updateCustomer(12, "Carlo", "1423373228"));
		assertTrue(db.updateCustomer(12, "Giovanni", "1423373228"));		
		assertTrue(db.updateCustomer(12, "Giovanni", ""));
		assertTrue(db.updateCustomer(12, "Giovanni", null));
		
	}
	
	@Test
	public void getCustomerByIdTestCase() {
		db.resetDB("customers");
		CustomerClass c = new CustomerClass(12,  "Carlo", "1423673214", 122);
		db.defineCustomer(c);
		
		assertNotNull(db.getCustomerById(12));
		assertNull(db.getCustomerById(-1));
		
	}
	
	@Test
	public void getAllCustomerTestCase() {
		db.resetDB("customers");
		CustomerClass c = new CustomerClass(12,  "Carlo", "1423673214", 122);
		
		db.defineCustomer(c);
		
		List<Customer> clist = db.getAllCustomers();
		assertNotNull(clist);
		
		clist.remove(c);
		db.deleteCustomer(12);
		
		db.resetDB("customers");
		
		assertTrue(db.getAllCustomers().isEmpty());
		
		for(Customer c2 : clist) {
			db.defineCustomer((CustomerClass) c2); 
		}
		
	}
	
	@Test
	public void createCardTestCase() {		
		db.resetDB("cards");
		assertTrue(db.createCard("2400000000"));
		assertFalse(db.createCard("2400000000"));		
	}
	
	@Test
	public void attachCardToCustomerTestCase() {
		db.resetDB("customers");
		db.resetDB("cards");
		CustomerClass c = new CustomerClass(12,  "Carlo", "1423673214", 122);
		db.defineCustomer(c);
		db.createCard("2000000000");
		
		
		assertTrue(db.attachCardToCustomer("2000000000", 12));		
		assertFalse(db.attachCardToCustomer("8000000000", 12));
		db.deleteCustomer(12);
		assertFalse(db.attachCardToCustomer("2000000000", 14));	
	}
	
	@Test
	public void updateAndgetCardPointsTestCase() {	
		db.resetDB("cards");
		db.createCard("1234567890");		
		db.updateCardPoints("1234567890", 122);
		assertFalse(db.updateCardPoints("123", 122));		
		assertTrue(db.updateCardPoints("1234567890", 122));			
				
		assert(db.getCardPoints("1234567890") == 122);
		assertNull(db.getCardPoints(""));			
	}	
	
	@Test
	public void createTicketEntryTestCase() {
		db.resetDB("productEntries");		
		TicketEntry te = new TicketEntryClass(132,"22345212","test description",10,1.50,170,0.0);		
		assertTrue(db.createTicketEntry(te,170));		
		assertFalse(db.createTicketEntry(null,170));	
	}	

	
	@Test
	public void getterAndSetterBalanceOperationTestCase() {
		db.resetDB("balanceOperations");
		BalanceOperation bo = new BalanceOperationClass(10, LocalDate.of(2020, 1, 8), 32.5, "type");
		assertNotNull(bo);
		
		bo.setBalanceId(2);
		Integer id = bo.getBalanceId();
		assert(id == 2);
		
		bo.setMoney(20.5);
		Double money = bo.getMoney();
		assert(money == 20.5);
		
		bo.setType("Shopping");
		String type = bo.getType();
		assert(type == "Shopping");
		
		LocalDate date = LocalDate.of(2021, 4, 9); 
		bo.setDate(date);
		assert(date == bo.getDate());
		
	}
	
	@Test
	public void getterAndSetterCreditCardTestCase() {	
		CreditCardClass cc = new CreditCardClass("4485370086510891", 440.5);
		assertNotNull(cc);
		
		cc.setCardNumber("4485123437008543");
		String cn = cc.getCardNumber();
		assert(cn == "4485123437008543");
		
		cc.setBalance(220.5);
		Double b = cc.getBalance();
		assert(b == 220.5);					
	}
	
	@Test
	public void getterAndSetterSaleTransactionTestCase() {
		db.resetDB("saleTransactions");
		db.resetDB("productEntries");
		List<TicketEntry> productList = new ArrayList<>();
		String[] date = (new Date()).toString().split(" ");
		SaleTransactionClass st = new SaleTransactionClass(170,date[0],date[1],0.0,"",0.0,productList,"OPEN");
		assertNotNull(st);
		
		st.setTicketNumber(2);
		Integer tn = st.getTicketNumber();
		assert(tn == 2);
		
		st.setDate("date");
		String d = st.getDate();
		assert(d == "date");
		
		st.setTime("time");
		String tm = st.getTime();
		assert(tm == "time");
		
		st.setPaymentType("payType");		
		String pt = st.getPaymentType();
		assert(pt == "payType");
		
		st.setPrice(2);		
		Double p = st.getPrice();
		assert(p == 2);
		
		st.setDiscountRate(0.2);		
		Double dr = st.getDiscountRate();
		assert(dr == 0.2);
		
		productList.add(new TicketEntryClass(2, "barcode", "desc", 32, 20.0, 3, 0.2));
		st.setEntries(productList);
		List<TicketEntry> pl = st.getEntries();
		assert(pl == productList);
		
		st.setState("state");		
		String s = st.getState();
		assert(s == "state");
		
	}
	
	
	@Test
	public void getterAndSetterticketEntryTestCase() {	
		db.resetDB("productEntries");
		TicketEntryClass te = new TicketEntryClass(2, "barcode", "desc", 32, 20.0, 3, 0.2);
		assertNotNull(te);
		
		te.setId(4);
		Integer id = te.getId();
		assert(id == 4);
		
		te.setBarCode("bcode");
		String bc = te.getBarCode();
		assert(bc == "bcode");
		
		te.setProductDescription("Caramelle");
		String pd = te.getProductDescription();
		assert(pd == "Caramelle");
		
		te.setAmount(44);		
		Integer a = te.getAmount();
		assert(a == 44);
		
		te.setPricePerUnit(2.2);		
		Double ppu = te.getPricePerUnit();
		assert(ppu == 2.2);
		
		te.setTransactionId(90);		
		Integer tid = te.getTransactionId();
		assert(tid == 90);			
		
		te.setDiscountRate(0.5);		
		Double dr = te.getDiscountRate();
		assert(dr == 0.5);
		
	}
	
	
	@Test
	public void getterAndSetterReturnTransactionTestCase() {
		db.resetDB("returnTransactions");
		List<ProductType> ptlist = new ArrayList<>();
		
		ReturnTransactionClass rt = new ReturnTransactionClass(12, 23, 44, 3.5, "state");
		assertNotNull(rt);
		
		rt.setId(2);
		Integer id = rt.getId();
		assert(id == 2);
		
		rt.setState("st");
		String s = rt.getState();
		assert(s == "st");
		
		rt.setQuantity(5);
		Integer q = rt.getQuantity();
		assert(q == 5);
		
		rt.setTransactionId(4);		
		Integer tid = rt.getTransactionId();
		assert(tid == 4);
		
		rt.setReturnValue(2.4);		
		Double rv = rt.getReturnValue();
		assert(rv == 2.4);
		
		ptlist.add(new ProductTypeClass(3, 4, "location", "note", "desc", "barcode", 33.2));
		rt.setProductTypeList(ptlist);
		List<ProductType> pl = rt.getProductTypeList();
		assert(pl == ptlist);		
	}
	
	@Test
	public void getterAndSetterProductReturnTestCase() {
		db.resetDB("productReturns");
		ProductReturnClass rt = new ProductReturnClass(4, 6, "barcode", 44, 2.2);
		assertNotNull(rt);
		
		rt.setId(2);
		Integer id = rt.getId();
		assert(id == 2);
		
		rt.setReturnId(5);
		Integer rid = rt.getReturnId();
		assert(rid == 5);
		
		rt.setProductCode("Code");
		String pc = rt.getProductCode();
		assert(pc == "Code");
		
		rt.setQuantity(400);		
		Integer q = rt.getQuantity();
		assert(q == 400);
		
		rt.setReturnValue(2.4);		
		Double rv = rt.getReturnValue();
		assert(rv == 2.4);
	}
	
	
	@Test
	public void checkValidCardTestCase() {
		assertFalse(CreditCardClass.checkValidCard("df"));				
		assertTrue(CreditCardClass.checkValidCard("4485370086510891"));
		assertFalse(CreditCardClass.checkValidCard("123456756324"));				
	}
	
///////////////////////////////////// Marco C.
	
	@Test
	public void startDeleteSaleTransactionTestCase() {
		db.resetDB("saleTransactions");
		List<TicketEntry> productList = new ArrayList<>();
		String[] date = (new Date()).toString().split(" ");
		SaleTransactionClass saleTransaction = new SaleTransactionClass(170,date[0],date[1],0.0,"",0.0,productList,"OPEN");
		
		assertEquals(170,db.startSaleTransaction(saleTransaction)+0);
		assertEquals(-1,db.startSaleTransaction(saleTransaction)+0);
		
		assertTrue(db.deleteSaleTransaction(170));
		assertFalse(db.deleteSaleTransaction(44));		
	}

	@Test
	public void getTransactionByIdTestCase() {
		db.resetDB("saleTransactions");
		List<TicketEntry> productList = new ArrayList<>();
		String[] date = (new Date()).toString().split(" ");
		SaleTransactionClass saleTransaction = new SaleTransactionClass(170,date[0],date[1],0.0,"",0.0,productList,"OPEN");
		
		db.startSaleTransaction(saleTransaction);		
				
		assertNotNull(db.getSaleTransactionById(170));
		assertNull(db.getSaleTransactionById(180));	
		
		db.resetDB("saleTransactions");
	}
//	
	@Test
	public void applyDiscountToTransactionTestCase() {
		db.resetDB("saleTransactions");
		List<TicketEntry> productList = new ArrayList<>();
		String[] date = (new Date()).toString().split(" ");
		SaleTransactionClass saleTransaction = new SaleTransactionClass(170,date[0],date[1],0.0,"",0.0,productList,"OPEN");
		
		db.startSaleTransaction(saleTransaction);
		db.startSaleTransaction(saleTransaction);
				
		assertTrue(db.applyDiscountRate(170,0.2));
		assertFalse(db.applyDiscountRate(170,-1.5));
		
		db.deleteSaleTransaction(170);
	}	
	
	
	@Test
	public void applyDiscountToProductTestCase() {
		db.resetDB("productEntries");
		TicketEntry te = new TicketEntryClass(132,"22345212","test description",10,1.50,170,0.0);		
		db.createTicketEntry(te,170);
		
		assertTrue(db.applyDiscountRateToProduct(170, "22345212", 0.2));
		assertFalse(db.applyDiscountRateToProduct(170, "22345212",-1.5));
					
	}
	
	
	@Test
	public void updateTransactionStateTestCase() {
		db.resetDB("saleTransactions");
		List<TicketEntry> productList = new ArrayList<>();
		String[] date = (new Date()).toString().split(" ");
		SaleTransactionClass saleTransaction = new SaleTransactionClass(170,date[0],date[1],0.0,"",0.0,productList,"OPEN");
		db.startSaleTransaction(saleTransaction);
		assertFalse(db.updateTransactionState(180, "CLOSED"));
		assertFalse(db.updateTransactionState(180, null));
		assertTrue(db.updateTransactionState(170, "CLOSED"));
		SaleTransactionClass transaction = db.getSaleTransactionById(170);
		assertNotNull(transaction);
		assertEquals("CLOSED",transaction.getState());
	}
	
	@Test
	public void getClosedSaleTransactionTestCase() {
		db.resetDB("saleTransactions");
		db.resetDB("productTypes");
		ProductType pt = new ProductTypeClass(1741, 20, "4-4-4", "test", "this is a test", "22345212", 3.22);
		db.addProductType(pt);
		List<TicketEntry> productList = new ArrayList<>();
		String[] date = (new Date()).toString().split(" ");
		SaleTransactionClass saleTransaction = new SaleTransactionClass(170,date[0],date[1],0.0,"",0.0,productList,"OPEN");
		
		db.resetDB("productEntries");
		TicketEntry te = new TicketEntryClass(132,"22345212","test description",10,1.50,170,0.0);
		
		assertEquals(170,db.startSaleTransaction(saleTransaction)+0);
		assertTrue(db.createTicketEntry(te,170));
		assertTrue(db.updateTransactionState(170, "PAYED"));
		assertNull(db.getClosedSaleTransactionById(171));
		assertNotNull(db.getClosedSaleTransactionById(170));
		
	}
	
	@Test
	public void getProductEntriesByTransactionIdTestCase() {
		db.resetDB("productEntries");
		db.resetDB("productTypes");
		ProductType pt = new ProductTypeClass(1741, 20, "4-4-4", "test", "this is a test", "22345212", 3.22);
		db.addProductType(pt);
		TicketEntry te = new TicketEntryClass(132,"22345212","test description",10,1.50,170,0.0);
		assertTrue(db.createTicketEntry(te,170));
		assertNotEquals(0,db.getProductEntriesByTransactionId(170).size());
		assertEquals(0,db.getProductEntriesByTransactionId(172).size());
	}
	
	@Test
	public void returnTransactionTestCase() {
		db.resetDB("saleTransactions");
		db.resetDB("returnTransactions");
		List<TicketEntry> productList = new ArrayList<>();
		String[] date = (new Date()).toString().split(" ");
		SaleTransactionClass saleTransaction = new SaleTransactionClass(170,date[0],date[1],0.0,"",0.0,productList,"OPEN");
		TicketEntry te = new TicketEntryClass(132,"22345212","test description",10,1.50,170,0.0);
		
		
		db.createTicketEntry(te,170);
		assertEquals(170,db.startSaleTransaction(saleTransaction)+0);
		db.updateTransactionState(170, "CLOSED");
		
		ReturnTransactionClass returnTransaction = new ReturnTransactionClass(12,170,10,0.0,"OPEN");
		ReturnTransactionClass wrongReturnTransaction = new ReturnTransactionClass(13,180,10,0.0,"OPEN");
		
		assertNotEquals(-1,db.startReturnTransaction(returnTransaction)+0);
		assertEquals(-1,db.startReturnTransaction(wrongReturnTransaction)+0);
		
		assertNotNull(db.getReturnTransactionById(12));
		assertNull(db.getReturnTransactionById(13));
		
		assertTrue(db.checkProductInSaleTransaction(170, "22345212"));
		assertFalse(db.checkProductInSaleTransaction(170, "22345215"));
		assertFalse(db.checkProductInSaleTransaction(171, "22345215"));
		
		assertTrue(db.updateReturnTransaction(12, 12, 10.15));
		
		assertTrue(db.deleteReturnTransaction(12));
	}
	
	@Test
	public void getPriceUnitTestCase() {
		db.resetDB("productTypes");
		ProductType pt = new ProductTypeClass(1741, 20, "4-4-4", "test", "this is a test", "22345212", 3.22);
		db.addProductType(pt);
		assertNotEquals(0,db.getPricePerUnit("22345212"),0.01);
		assertEquals(0,db.getPricePerUnit("22342212"),0.01);
	}
	
	@Test
	public void returnProductTestCase() {
		db.resetDB("productReturns");
		
		assertTrue(db.returnProduct(12, 23, "22345212", 12, 14.50));
	}
	
	@Test
	public void getAmountOnEntryTestCase() {
		db.resetDB("productEntries");
		TicketEntry te = new TicketEntryClass(132,"22345212","test description",10,1.50,170,0.0);
		assertTrue(db.createTicketEntry(te,170));
		assertNotEquals(-1,db.getAmountonEntry(170, "22345212")+0);
		assertEquals(-1,db.getAmountonEntry(171, "22345212")+0);
			
	}
	
	@Test
	public void getTotalOnEntryTestCase() {
		db.resetDB("productEntries");
		TicketEntry te = new TicketEntryClass(132,"22345212","test description",10,1.50,170,0.2);
		assertTrue(db.createTicketEntry(te,170));
		assertNotEquals(0.0,db.getTotalOnEntry(170, "22345212"),0.01);
		assertEquals(0.0,db.getTotalOnEntry(171, "22345212"),0.01);
			
	}
	
	@Test
	public void updateSaleTransactionAfterCommitTestCase() {
		db.resetDB("saleTransactions");

		String[] date = (new Date()).toString().split(" ");
		List<TicketEntry> productList = new ArrayList<>();
		SaleTransactionClass saleTransaction = new SaleTransactionClass(170,date[0],date[1],0.0,"",0.0,productList,"OPEN");

		assertEquals(170,db.startSaleTransaction(saleTransaction)+0);
		assertTrue(db.updateSaleTransactionAfterCommit(170,12.50));
	}
	
	@Test
	public void updateEntryAfterCommitTestCase() {
		db.resetDB("productEntries");
		
		TicketEntry te = new TicketEntryClass(132,"22345212","test description",10,1.50,170,0.0);
		assertTrue(db.createTicketEntry(te,170));
		
		assertTrue(db.updateEntryAfterCommit(170, "22345212", 10, 17.50));
	}
	
	@Test
	public void getAllProductReturnsTestCase() {
		db.resetDB("productReturns");
		
		assertTrue(db.returnProduct(12, 23, "22345212", 12, 14.50));
		assertTrue(db.returnProduct(15, 23, "22845212", 15, 15.50));
		
		assertNotEquals(0,db.getAllProductReturnsById(23).size());
		assertEquals(0,db.getAllProductReturnsById(21).size());
	}
	
	@Test
	public void deleteProductReturnsTestCase() {
		db.resetDB("productReturns");
		
		assertTrue(db.returnProduct(12, 23, "22345212", 12, 14.50));
		assertTrue(db.returnProduct(15, 23, "22845212", 15, 15.50));
		
		assertTrue(db.deleteProductReturnsByReturnId(23));
	}
	
	@Test
	public void updatePaymentSaleTransactionTestCase() {
		db.resetDB("saleTransactions");
		
		List<TicketEntry> productList = new ArrayList<>();
		String[] date = (new Date()).toString().split(" ");
		SaleTransactionClass saleTransaction = new SaleTransactionClass(170,date[0],date[1],0.0,"",0.0,productList,"OPEN");

		assertEquals(170,db.startSaleTransaction(saleTransaction)+0);
		
		assertTrue(db.updatePaymentSaleTransaction(170, "CARD", "PAYED"));
	}
	
	@Test
	public void recordBalanceOperationTestCase() {
		db.resetDB("balanceOperations");
		
		LocalDate date = LocalDate.now();
		BalanceOperationClass balance = new BalanceOperationClass(15,date,150.0,"Shopping");

		assertTrue(db.recordBalanceOperation(balance));
	}
	
	@Test
	public void getActualBalanceTestCase() {
		db.resetDB("balanceOperations");
		
		LocalDate date = LocalDate.now();
		BalanceOperationClass balance = new BalanceOperationClass(15,date,150.0,"Shopping");
		assertEquals(0,db.getActualBalance(),0.01);
		assertTrue(db.recordBalanceOperation(balance));
		
		assertNotEquals(0,db.getActualBalance(),0.01);
	}
	
	@Test
	public void getBalanceOperationsTestCase() {
		db.resetDB("balanceOperations");
		
		LocalDate date = LocalDate.now();
		BalanceOperationClass balance = new BalanceOperationClass(15,date,150.0,"Shopping");
		BalanceOperationClass balance2 = new BalanceOperationClass(17,date,150.0,"Shopping");
		
		assert(db.getBalanceOperations(date.toString(),date.toString()).size() == 0);
		
		assertTrue(db.recordBalanceOperation(balance));
		assertTrue(db.recordBalanceOperation(balance2));
		
		assert(db.getBalanceOperations(date.toString(),date.toString()).size() > 1);
	}
	
	@Test
	public void getCreditCardTestCase() {
		
		assertNotNull(db.getCreditCardByCardNumber("4485370086510891"));
		assertNull(db.getCreditCardByCardNumber("1209219201241"));	
		
	}
	
	@Test
	public void updateBalanceTestCase() {
		
		assertTrue(db.updateBalanceInCreditCard("4485370086510891",170.0));
		assertNull(db.getCreditCardByCardNumber("1209219201241"));	
		
	}
	
	
	
}
