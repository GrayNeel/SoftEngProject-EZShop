package it.polito.ezshop.acceptanceTests;

import it.polito.ezshop.classes.*;
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
		validateProductCodeTestCase();
		getterAndSetterProductTypeTestCase();
		getterAndSetterOrderTestCase();
		addAndDeleteProductTypeTestCase();
		checkExistingProductTypeTestCase();
		updateProductTypeTestCase();
		getAllProductTypesTestCase();
		getProductTypeByBarCodeTestCase();
		getProductTypesByDescriptionTestCase();
		getQuantityByProductTypeIdTestCase();
		updateQuantityByProductTypeIdTestCase();
		isLocationUsedTestCase();
		updateProductTypeLocationTestCase();
		
		addAndIssueOrderThenDeleteTestCase();
		setBalanceIdInOrderTestCase();
		payOrderByIdTestCase();
		recordOrderArrivalByIdTestCase();
		getAllOrdersTestCase();

/////////////////////////////////////// Francesco

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

/////////////////////////////////////// Marco C.
	
	@Test
	public void saleTransactionTestCase() {
		ProductType pt = new ProductTypeClass(1741, 2, "4-4-4", "test", "this is a test", "22345212", 3.22);
		db.addProductType(pt);
		
		Integer nextId = db.getLastId("saleTransactions");
//		assertNotEquals(-1,nextId+0);
		String[] date = (new Date()).toString().split(" ");
		List<TicketEntry> productList = new ArrayList<>();
		SaleTransactionClass saleTransaction = new SaleTransactionClass(nextId+1,date[0],date[1],0.0,"",0.0,productList,"OPEN");
		assertEquals(nextId+1,db.startSaleTransaction(saleTransaction)+0);
		
		
		assertNotNull(db.getClosedSaleTransactionById(1));
		assertNull(db.getClosedSaleTransactionById(10));
	}
	
	@Test
	public void validateClosedSaleTransaction() {
		assertNotNull(db.getClosedSaleTransactionById(1));
		assertNull(db.getClosedSaleTransactionById(10));
	}
	
	@Test
	public void validateGetProductEntries() {
		assertFalse(db.getProductEntriesByTransactionId(1).isEmpty());
		assertTrue(db.getProductEntriesByTransactionId(10).isEmpty());
	}
	
	@Test
	public void validateDeleteSaleTransaction() {
		assertTrue(db.deleteSaleTransaction(1));
		assertFalse(db.deleteSaleTransaction(10));
	}
	
	@Test
	public void validateStartReturnTransaction() {
		assertNotEquals(-1,db.startReturnTransaction(returnTransaction)+0);
		assertEquals(-1,db.startReturnTransaction(wrongTransaction)+0);
	}
	
	@Test
	public void validateDeleteReturnTransaction() {
		assertTrue(db.deleteReturnTransaction(1));
		assertFalse(db.deleteReturnTransaction(10));
	}
	
	@Test
	public void validateGetReturnTransaction() {
		assertNotNull(db.getReturnTransactionById(1));
		assertNull(db.getReturnTransactionById(10));
	}
	
	@Test
	public void validateGetPricePerUnit() {
		assertNotEquals(0,db.getPricePerUnit("12345670"),0.01);
		assertEquals(0,db.getPricePerUnit("1234567"),0.01);
	}
	
	@Test
	public void validateReturnProduct() {
		
	}
	
	@Test
	public void validateGetAmountEntry() {

	}
	
	@Test
	public void validateGetTotalOnEntry() {

	}
	
	@Test
	public void validateCheckProductInSaleTransaction() {

	}
	
	@Test
	public void validateUpdateReturnTransaction() {

	}
	
	@Test
	public void validateUpdateSaleTransactionAfterCommit() {

	}
	
	@Test
	public void validateUpdateEntryAfterCommit() {

	}
	
	@Test
	public void validateGetAllProductReturnsById() {

	}
	
	@Test
	public void validateDeleteAllProductReturnsByReturnId() {

	}
	
	@Test
	public void validateUpdatePaymentSaleTransaction() {

	}
	
	@Test
	public void validateRecordBalanceOperation() {

	}
	
	@Test
	public void validateGetActualBalance() {

	}
	
	@Test
	public void validateGetBalanceOperations() {

	}
	
	@Test
	public void validateGetCreditCardByCardNumber() {

	}
	
	@Test
	public void validateUpdateBalanceInCreditCard() {

	}
	
	
}
