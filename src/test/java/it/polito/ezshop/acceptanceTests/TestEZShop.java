package it.polito.ezshop.acceptanceTests;

import it.polito.ezshop.classes.*;

import it.polito.ezshop.data.Customer;
import it.polito.ezshop.data.*;
import it.polito.ezshop.data.Order;
import it.polito.ezshop.data.ProductType;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

public class TestEZShop {
	EZShopDB db = new EZShopDB();
	
	@Test
	public void test() {
		
		
/////////////////////////////////////// Pablo
		
/////////////////////////////////////// Marco S.
//		validateProductCodeTestCase();
//		getterAndSetterProductTypeTestCase();
//		getterAndSetterOrderTestCase();
//		addAndDeleteProductTypeTestCase();
//		checkExistingProductTypeTestCase();
//		updateProductTypeTestCase();
//		getAllProductTypesTestCase();
//		getProductTypeByBarCodeTestCase();
//		getProductTypesByDescriptionTestCase();
//		getQuantityByProductTypeIdTestCase();
//		updateQuantityByProductTypeIdTestCase();
//		isLocationUsedTestCase();
//		updateProductTypeLocationTestCase();
//		
//		addAndIssueOrderThenDeleteTestCase();
//		setBalanceIdInOrderTestCase();
//		payOrderByIdTestCase();
//		recordOrderArrivalByIdTestCase();
//		getAllOrdersTestCase();

/////////////////////////////////////// Francesco
//		getterAndSetterCustomerTestCase();		
//		
//		addAndDeleteCustomerTestCase();
//		modifyCustomerTestCase();
//		getCustomerByIdTestCase();
//		createCardTestCase();
//		attachCardToCustomerTestCase();
//		updateAndgetCardPointsTestCase();
		
/////////////////////////////////////// Marco C.
//		validateClosedSaleTransaction();
//		validateGetProductEntries();
//		validateDeleteSaleTransaction();
//		validateDeleteReturnTransaction();
//		validateStartReturnTransaction();
//		validateGetReturnTransaction();
		

		
		
		
		
		
	}
	
	
	
/////////////////////////////////////////// Testing Functions
	
/////////////////////////////////////// Pablo
	@Test
	public void resetDBTestCase() {
		// Test for tables with getAll method
		List<User> userlist = db.getAllUsers();
		assertNotNull(userlist);
		
		db.resetDB("users");
		
		List<User> emptyList = db.getAllUsers();
		assertTrue(emptyList.isEmpty());
		
		for(User us : userlist) {
			db.addUser(us);
		}
		
		List<ProductType> ptlist = db.getAllProductTypes();
		assertNotNull(ptlist);
		
		db.resetDB("productTypes");
		
		List<ProductType> emptyptlist = db.getAllProductTypes();
		assertTrue(emptyptlist.isEmpty());
		
		for(ProductType prod : ptlist) {
			db.addProductType(prod);
		}
		
		List<Order> orderlist = db.getAllOrders();
		assertNotNull(orderlist);
		
		db.resetDB("orders");
		
		List<Order> emptyOrderList = db.getAllOrders();
		assertTrue(emptyOrderList.isEmpty());
		
		for(Order order : orderlist) {
			db.addAndIssueOrder(order);
		}
		
		
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
		
		// Adding a new user
		assertTrue(db.addUser(user1));
		User user2 = db.getUserById(7);
		assertNotNull(user2);
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
		
		db.deleteUser(20);
	}
	
	@Test
	public void getLastIdTestCase() {
		User user = new UserClass(30, "Test1", "12345678", "Cashier");
		db.addUser(user);
		
		Integer lastIdUser = db.getLastId("users");
		assert(lastIdUser == 30);
		
		ProductType pt = new ProductTypeClass(1741, 2, "location", "test", "this is a test", "22345212", 3.22);
		db.addProductType(pt);
		
		Integer lastIPT = db.getLastId("productTypes");
		assert(lastIPT == 1741);
		
		CustomerClass c = new CustomerClass(12,  "Carlo", "1423673214", 122);
		db.defineCustomer(c);
		
		Integer lastCustomer = db.getLastId("customers");
		assert(lastCustomer == 12);
		
    	Order o = new OrderClass(5853,-1, "1741", 8.5, 5, "ISSUED");
		db.addAndIssueOrder(o);
		
		Integer lastOrder = db.getLastId("orders");
		assert(lastOrder == 5853);
		
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
		
		User updatedUser = db.getUserById(20);
		assert(updatedUser.getRole() != "Cashier");
		assert(updatedUser.getRole().equals("Administrator"));
		
		db.deleteUser(20);
	}
	
/////////////////////////////////////// Marco S.
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
		ProductType pt = new ProductTypeClass(1741, 2, "location", "test", "nice", "22345212", 3.22);
		db.addProductType(pt);
		
		ProductType pt2 = new ProductTypeClass(1742, 2, "locational", "test", "nicest", "22345212", 3.22);
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
	public void getQuantityByProductTypeIdTestCase() {
		ProductType pt = new ProductTypeClass(1741, 2, "location", "test", "this is a test", "22345212", 3.22);
		db.addProductType(pt);
		
		assert(db.getQuantityByProductTypeId(1741) == 2);
		assertNull(db.getQuantityByProductTypeId(-1));
		
		db.deleteProductType(1741);
	}
	
	@Test
	public void updateQuantityByProductTypeIdTestCase() {
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
		CustomerClass c = new CustomerClass(18,  "Stefano", "1478973214", 178);
		assertTrue(db.defineCustomer(c));		
		assertFalse(db.defineCustomer(c));
		assertTrue(db.deleteCustomer(18));
		//assertFalse(db.deleteCustomer(-1));	
	}
	
	@Test
	public void modifyCustomerTestCase() {
		CustomerClass c = new CustomerClass(12,  "Carlo", "1423673214", 122);
		db.defineCustomer(c);
		//assertFalse(db.updateCustomer(-1, "Giovanni", "1423373228"));		
		assertTrue(db.updateCustomer(12, "Giovanni", "1423373228"));
		db.deleteCustomer(12);	
	}
	
	@Test
	public void getCustomerByIdTestCase() {
		CustomerClass c = new CustomerClass(12,  "Carlo", "1423673214", 122);
		db.defineCustomer(c);
		
		assertNotNull(db.getCustomerById(12));
		assertNull(db.getCustomerById(-1));
		db.deleteCustomer(12);
	}
	
	@Test
	public void getAllCustomerTestCase() {
		CustomerClass c = new CustomerClass(12,  "Carlo", "1423673214", 122);
		
		db.defineCustomer(c);
		
		List<Customer> clist = db.getAllCustomers();
		assertNotNull(clist);
		
		clist.remove(c);
		db.deleteCustomer(12);
		
		db.resetDB("customers");
		
		assertTrue(db.getAllCustomers().isEmpty());
		
		for(Customer c2 : clist) {
			db.defineCustomer((CustomerClass) c2); //potrei dover cambiare parametro defineCustomer
		}
		db.resetDB("customers");
	}
	
	@Test
	public void createCardTestCase() {		
		assertTrue(db.createCard("2400000000"));
		assertFalse(db.createCard("2400000000"));
		db.resetDB("cards");
	}
	
	@Test
	public void attachCardToCustomerTestCase() {
		CustomerClass c = new CustomerClass(12,  "Carlo", "1423673214", 122);
		db.defineCustomer(c);
		db.createCard("2000000000");
		
		
		assertTrue(db.attachCardToCustomer("2000000000", 12));
		db.deleteCustomer(12);
		assertFalse(db.attachCardToCustomer("2000000000", 14));
		//assertFalse(db.attachCardToCustomer("", -1));
		
	}
	
	@Test
	public void updateAndgetCardPointsTestCase() {		
		db.createCard("1234567890");		
		db.updateCardPoints("1234567890", 122);
		
		
		assertFalse(db.getCardPoints("1234567890") == 1022);
		assertTrue(db.getCardPoints("1234567890") == 122);
		
		db.resetDB("cards");		
	}	
	
	
/////////////////////////////////////// Marco C.
	
	@Test
	public void saleTransactionTestCase() {
//		Integer transactionId = 180;
//		Integer wrongTransactionId = 200;
//		String productCode = "22345212";
//		
//		ProductType pt = new ProductTypeClass(1741, 2, "4-4-4", "test", "this is a test", productCode, 3.22);
//		db.addProductType(pt);
//		
//		
//		TicketEntry te = new TicketEntryClass(132,productCode,"test description",10,1.50,transactionId,0.0);
//		
////		assertNotEquals(-1,nextId+0);
//		String[] date = (new Date()).toString().split(" ");
//		List<TicketEntry> productList = new ArrayList<>();
//		productList.add(te);
//		
//		SaleTransactionClass saleTransaction = new SaleTransactionClass(transactionId,date[0],date[1],0.0,"",0.0,productList,"OPEN");
//		assertEquals(transactionId+0,db.startSaleTransaction(saleTransaction)+0);
//		assertEquals(-1,db.startSaleTransaction(saleTransaction)+0);
//		
//		assertNotNull(db.getSaleTransactionById(transactionId));
//		assertNull(db.getSaleTransactionById(wrongTransactionId));
//		
//		assertTrue(db.applyDiscountRate(transactionId,0.2));
//		assertTrue(db.applyDiscountRate(wrongTransactionId,0.2));
//		
//		assertTrue(db.createTicketEntry(te,transactionId));
////		assertFalse(db.createTicketEntry(te,transactionId));
//		
//		assertTrue(db.applyDiscountRateToProduct(transactionId, productCode, 0.2));
//		assertFalse(db.applyDiscountRateToProduct(wrongTransactionId, productCode, 0.2));
//		
//		assertTrue(db.updateTransactionState(transactionId, "CLOSED"));
//		assertFalse(db.updateTransactionState(wrongTransactionId, "CLOSED"));
//		
//		assertNotNull(db.getClosedSaleTransactionById(transactionId));
//		assertNull(db.getClosedSaleTransactionById(wrongTransactionId));
//		
//		assertNotNull(db.getProductEntriesByTransactionId(transactionId));
//		assertNull(db.getProductEntriesByTransactionId(wrongTransactionId));
//		
//		assertTrue(db.deleteSaleTransaction(transactionId));
//		assertFalse(db.deleteSaleTransaction(wrongTransactionId));
//		
//		db.deleteProductType(1741);
	}
	
	
//	public void returnTransactionTestCase() {
//		Integer returnId = 100;
//		Integer wrongReturnId = 105;
//		Integer transactionId = 180;
//		Integer wrongTransactionId = 200;
//		String productCode = "22345212";
//		String wrongProductCode = "24345212";
//		
//		ProductType pt = new ProductTypeClass(1741, 20, "4-4-4", "test", "this is a test", productCode, 3.22);
//		db.addProductType(pt);
//		
//		
//		TicketEntry te = new TicketEntryClass(132,productCode,"test description",10,1.50,transactionId,0.0);
//		
////		assertNotEquals(-1,nextId+0);
//		String[] date = (new Date()).toString().split(" ");
//		List<TicketEntry> productList = new ArrayList<>();
//		productList.add(te);
//		
//		SaleTransactionClass saleTransaction = new SaleTransactionClass(transactionId,date[0],date[1],0.0,"",0.0,productList,"OPEN");
//		db.startSaleTransaction(saleTransaction);
//		db.createTicketEntry(te,transactionId);
//		db.updateTransactionState(transactionId, "CLOSED");
//		
//		ReturnTransactionClass returnTransaction = new ReturnTransactionClass(returnId,transactionId,10,0.0,"OPEN");
//		ReturnTransactionClass wrongReturnTransaction = new ReturnTransactionClass(wrongReturnId,wrongTransactionId,10,0.0,"OPEN");
//		
//		assertNotEquals(-1,db.startReturnTransaction(returnTransaction)+0);
//		assertEquals(-1,db.startReturnTransaction(wrongReturnTransaction)+0);
//		
//		assertNotNull(db.getReturnTransactionById(returnId));
//		assertNull(db.getReturnTransactionById(wrongReturnId));
//		
//		assertEquals(3.22,db.getPricePerUnit(productCode),0.01);
//		assertEquals(0,db.getPricePerUnit(wrongProductCode),0.01);
//		
////		assertEquals(20,db.getAmountonEntry(transactionId, productCode)+0);
////		assertEquals(-1,db.getAmountonEntry(wrongTransactionId, productCode)+0);
//		
//		assertEquals(15.0,db.getTotalOnEntry(transactionId, productCode),0.01);
//		assertEquals(0.0,db.getTotalOnEntry(wrongTransactionId, productCode),0.01);
//		
//		assertTrue(db.returnProduct(150, returnId, productCode, 10, 32.20));
//		assertFalse(db.returnProduct(150, returnId, productCode, 10, 32.20));
//		
//		assertTrue(db.deleteSaleTransaction(transactionId));
//		assertFalse(db.deleteSaleTransaction(wrongTransactionId));
//		
//		assertTrue(db.deleteReturnTransaction(returnId));
//		assertFalse(db.deleteReturnTransaction(wrongReturnId));
//		
//		db.deleteProductType(1741);
		
	
	
	
	@Test
	public void startDeleteSaleTransactionTestCase() {
		db.resetDB("saleTransactions");
		List<TicketEntry> productList = new ArrayList<>();
		String[] date = (new Date()).toString().split(" ");
		SaleTransactionClass saleTransaction = new SaleTransactionClass(170,date[0],date[1],0.0,"",0.0,productList,"OPEN");
		
		assertEquals(170,db.startSaleTransaction(saleTransaction)+0);
		assertEquals(-1,db.startSaleTransaction(saleTransaction)+0);
		
		assertTrue(db.deleteSaleTransaction(170));
//		assertFalse(db.deleteSaleTransaction());
		
		
	}
//	
	@Test
	public void getTransactionByIdTestCase() {
		db.resetDB("saleTransactions");
		List<TicketEntry> productList = new ArrayList<>();
		String[] date = (new Date()).toString().split(" ");
		SaleTransactionClass saleTransaction = new SaleTransactionClass(170,date[0],date[1],0.0,"",0.0,productList,"OPEN");
		
		assertEquals(170,db.startSaleTransaction(saleTransaction)+0);
		assertEquals(-1,db.startSaleTransaction(saleTransaction)+0);
				
		assertNotNull(db.getSaleTransactionById(170));
		assertNull(db.getSaleTransactionById(180));
		
		assertTrue(db.deleteSaleTransaction(170));

		
		db.resetDB("saleTransactions");
	}
//	
	@Test
	public void applyDiscountToTransactionTestCase() {
		db.resetDB("saleTransactions");
		List<TicketEntry> productList = new ArrayList<>();
		String[] date = (new Date()).toString().split(" ");
		SaleTransactionClass saleTransaction = new SaleTransactionClass(170,date[0],date[1],0.0,"",0.0,productList,"OPEN");
		
		assertEquals(170,db.startSaleTransaction(saleTransaction)+0);
		assertEquals(-1,db.startSaleTransaction(saleTransaction)+0);
				
		assertTrue(db.applyDiscountRate(170,0.2));
//		assertFalse(db.applyDiscountRate(180,0.2));
		
		assertTrue(db.deleteSaleTransaction(170));
	}
	
	@Test
	public void applyDiscountToProductTestCase() {
		db.resetDB("productEntries");
		TicketEntry te = new TicketEntryClass(132,"22345212","test description",10,1.50,170,0.0);
		
		assertTrue(db.createTicketEntry(te,170));
		assertTrue(db.applyDiscountRateToProduct(170, "22345212", 0.2));
					
	}
	
	@Test
	public void updateTransactionStateTestCase() {
		db.resetDB("saleTransactions");
		List<TicketEntry> productList = new ArrayList<>();
		String[] date = (new Date()).toString().split(" ");
		SaleTransactionClass saleTransaction = new SaleTransactionClass(170,date[0],date[1],0.0,"",0.0,productList,"OPEN");
		
		assertTrue(db.updateTransactionState(170, "CLOSED"));
//		assertFalse(db.updateTransactionState(180, "CLOSED"));
	}
//	
	@Test
	public void getClosedSaleTransactionTestCase() {
		db.resetDB("saleTransactions");
		List<TicketEntry> productList = new ArrayList<>();
		String[] date = (new Date()).toString().split(" ");
		SaleTransactionClass saleTransaction = new SaleTransactionClass(170,date[0],date[1],0.0,"",0.0,productList,"OPEN");
		
		db.resetDB("productEntries");
		TicketEntry te = new TicketEntryClass(132,"22345212","test description",10,1.50,170,0.0);
		
		assertEquals(170,db.startSaleTransaction(saleTransaction)+0);
		assertTrue(db.createTicketEntry(te,170));
		assertTrue(db.updateTransactionState(170, "PAYED"));
		assertNotNull(db.getClosedSaleTransactionById(170));
		assertNull(db.getClosedSaleTransactionById(171));
	}
	
	@Test
	public void returnTransactionTestCase() {
		db.resetDB("saleTransactions");
		List<TicketEntry> productList = new ArrayList<>();
		String[] date = (new Date()).toString().split(" ");
		SaleTransactionClass saleTransaction = new SaleTransactionClass(170,date[0],date[1],0.0,"",0.0,productList,"OPEN");
		TicketEntry te = new TicketEntryClass(132,"22345212","test description",10,1.50,170,0.0);
//		
		
		db.createTicketEntry(te,170);
		assertEquals(170,db.startSaleTransaction(saleTransaction)+0);
		db.updateTransactionState(170, "CLOSED");
		
		ReturnTransactionClass returnTransaction = new ReturnTransactionClass(12,170,10,0.0,"OPEN");
		ReturnTransactionClass wrongReturnTransaction = new ReturnTransactionClass(13,180,10,0.0,"OPEN");
		
		assertNotEquals(-1,db.startReturnTransaction(returnTransaction)+0);
		assertEquals(-1,db.startReturnTransaction(wrongReturnTransaction)+0);
	}
//	
//	@Test
//	public void validateGetPricePerUnit() {
//		assertNotEquals(0,db.getPricePerUnit("12345670"),0.01);
//		assertEquals(0,db.getPricePerUnit("1234567"),0.01);
//	}
//	
//	@Test
//	public void validateReturnProduct() {
//		
//	}
//	
//	@Test
//	public void validateGetAmountEntry() {
//
//	}
//	
//	@Test
//	public void validateGetTotalOnEntry() {
//
//	}
//	
//	@Test
//	public void validateCheckProductInSaleTransaction() {
//
//	}
//	
//	@Test
//	public void validateUpdateReturnTransaction() {
//
//	}
//	
//	@Test
//	public void validateUpdateSaleTransactionAfterCommit() {
//
//	}
//	
//	@Test
//	public void validateUpdateEntryAfterCommit() {
//
//	}
//	
//	@Test
//	public void validateGetAllProductReturnsById() {
//
//	}
//	
//	@Test
//	public void validateDeleteAllProductReturnsByReturnId() {
//
//	}
//	
//	@Test
//	public void validateUpdatePaymentSaleTransaction() {
//
//	}
//	
//	@Test
//	public void validateRecordBalanceOperation() {
//
//	}
//	
//	@Test
//	public void validateGetActualBalance() {
//
//	}
//	
//	@Test
//	public void validateGetBalanceOperations() {
//
//	}
//	
//	@Test
//	public void validateGetCreditCardByCardNumber() {
//
//	}
//	
//	@Test
//	public void validateUpdateBalanceInCreditCard() {
//
//	}
	
	
}
