package it.polito.ezshop.acceptanceTests;

import it.polito.ezshop.classes.*;
import static org.junit.Assert.*;

import org.junit.Test;

public class TestEZShop {
	
	@Test
	public void test() {
		EZShopDB db = new EZShopDB();
		
/////////////////////////////////////// Pablo
		
/////////////////////////////////////// Marco S.

/////////////////////////////////////// Francesco

/////////////////////////////////////// Marco C.
		
		String productCode = "";
		String wrongProductCode = "";
		
		assertNotNull(db.getClosedSaleTransactionById(1));
		assertNull(db.getClosedSaleTransactionById(10));
		
		assertFalse(db.getProductEntriesByTransactionId(1).isEmpty());
		assertTrue(db.getProductEntriesByTransactionId(10).isEmpty());
		
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

}
