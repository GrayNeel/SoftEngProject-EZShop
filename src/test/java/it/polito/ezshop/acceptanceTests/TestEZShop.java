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
		
		String productCode = "";
		String wrongProductCode = "";
		
		
		
		
		
		assertTrue(db.deleteSaleTransaction(1));
		assertFalse(db.deleteSaleTransaction(10));
		
//		assertEqual(1,db.startReturnTransaction(returnTransaction));
		
		assertTrue(db.deleteReturnTransaction(1));
		assertFalse(db.deleteReturnTransaction(10));
		
		assertNotNull(db.getReturnTransactionById(1));
		assertNull(db.getReturnTransactionById(10));
		
		assertNotEquals(0,db.getPricePerUnit(productCode),0.01);
		assertEquals(0,db.getPricePerUnit(wrongProductCode),0.01);
		
		
		
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
	
	
}
