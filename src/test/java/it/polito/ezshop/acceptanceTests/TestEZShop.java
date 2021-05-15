package it.polito.ezshop.acceptanceTests;

import it.polito.ezshop.classes.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class TestEZShop {
	EZShopDB db = new EZShopDB();
	
	@Test
	public void test() {
		
		
/////////////////////////////////////// Pablo
		
/////////////////////////////////////// Marco S.
		validateProductCodeTestCase();


/////////////////////////////////////// Francesco

/////////////////////////////////////// Marco C.
		validateClosedSaleTransaction();
		
		validateGetProductEntries();
		
		validateDeleteSaleTransaction();
		
		validateDeleteReturnTransaction();
		
//		validateStartReturnTransaction();
		
		validateGetReturnTransaction();
		

		
		
		
		
		
	}
	
	
	
/////////////////////////////////////////// Testing Functions
	
/////////////////////////////////////// Pablo
	
/////////////////////////////////////// Marco S.
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

/////////////////////////////////////// Francesco

/////////////////////////////////////// Marco C.
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
//		assertEqual(1,db.startReturnTransaction(returnTransaction));
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
