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
		
		assertNotNull(db.getClosedSaleTransactionById(1));
		assertNull(db.getClosedSaleTransactionById(10));
		
		assertTrue(db.deleteSaleTransaction(1));
		assertFalse(db.deleteSaleTransaction(10));
	}

}
