package it.polito.ezshop.acceptanceTests;

import it.polito.ezshop.classes.*;
import it.polito.ezshop.data.BalanceOperation;
import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.data.Order;
import it.polito.ezshop.data.ProductType;
import it.polito.ezshop.data.User;
import it.polito.ezshop.exceptions.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.Test;

public class RFIDTest {
	EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
	EZShopDB db = new EZShopDB();

	@Test
	public void recordProductRFIDTestCase() {
		
		try {
			db.resetDB("products");
			db.resetDB("orders");
			
			assertFalse(db.recordProductRFID(new ProductClass(-5,"000004563222")));
			assertFalse(db.recordProductRFID(new ProductClass(-5,"-00004563222")));
			assertFalse(db.recordProductRFID(new ProductClass(-5,"0000045632324433")));
			assertTrue(db.recordProductRFID(new ProductClass(1741,"000004563222")));
			
			db.resetDB("products");
			db.resetDB("orders");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void getProductsRFIDbyIdTestCase() {
		try {
			db.resetDB("products");
			db.resetDB("orders");
			
			assertTrue(db.recordProductRFID(new ProductClass(1741,"000004563222")));
			assertTrue(db.recordProductRFID(new ProductClass(1741,"000004563333")));
			assert(db.getProductsRFIDbyId(23432).size() == 0);
			assert(db.getProductsRFIDbyId(1741).size() == 2);
			
			db.resetDB("products");
			db.resetDB("orders");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void getAllProductsRFIDTestCase() {
		try {
			db.resetDB("products");
			db.resetDB("orders");
			
			assertTrue(db.recordProductRFID(new ProductClass(1741,"000004563222")));
			assertTrue(db.recordProductRFID(new ProductClass(1741,"000004563333")));
			assertTrue(db.recordProductRFID(new ProductClass(1744,"000005563333")));
			assertTrue(db.recordProductRFID(new ProductClass(1747,"000006563333")));
			assert(db.getAllProductsRFID().size() == 4);
			
			db.resetDB("products");
			db.resetDB("orders");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	public void recordOrderArrivalRFIDTestCase() throws InvalidUsernameException, InvalidPasswordException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidQuantityException, InvalidProductIdException, InvalidLocationException, InvalidOrderIdException, InvalidRFIDException {
	    //public boolean recordOrderArrival(Integer orderId) throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException;
		db.resetDB("productTypes");
		db.resetDB("orders");
		db.resetDB("balanceOperations");
		db.resetDB("products");
		ezShop.login("admin","strong");
		
		Integer productId = ezShop.createProductType("descriptionTest1", "737052355054",2.50, "product note");

		BalanceOperation balOp = new BalanceOperationClass(1,LocalDate.now(),55,"CREDIT"); 
    	db.recordBalanceOperation(balOp);
    	
		Integer orderId = ezShop.payOrderFor("737052355054", 22, 2.50);
		Integer orderIssue = ezShop.issueOrder("737052355054", 13, 2.30);
	    ezShop.logout();
	    
	    String RFID = "000000234555";
	    
	    assertThrows(UnauthorizedException.class, () -> ezShop.recordOrderArrivalRFID(orderId, RFID));
	    
	    ezShop.login("admin","strong");

	    //No location assigned
	    assertThrows(InvalidLocationException.class, () -> ezShop.recordOrderArrivalRFID(orderId, RFID));
	    
	    ezShop.updatePosition(productId, "1-1-1");
	    
	    assertThrows(InvalidOrderIdException.class, () -> ezShop.recordOrderArrivalRFID(-1, RFID));
	    assertThrows(InvalidOrderIdException.class, () -> ezShop.recordOrderArrivalRFID(0, RFID));
	    assertThrows(InvalidOrderIdException.class, () -> ezShop.recordOrderArrivalRFID(null, RFID));
	    
	    assertFalse(ezShop.recordOrderArrivalRFID(55, RFID));
	    assertFalse(ezShop.recordOrderArrivalRFID(orderIssue, RFID));
	    
	    /* RFID Checks */
	    assertThrows(InvalidRFIDException.class, () -> ezShop.recordOrderArrivalRFID(orderId, "02223458922"));  // len = 11
	    assertThrows(InvalidRFIDException.class, () -> ezShop.recordOrderArrivalRFID(orderId, "0222345892222")); // len = 13
	    assertThrows(InvalidRFIDException.class, () -> ezShop.recordOrderArrivalRFID(orderId, "-22234589233")); // negative

	    assertTrue(ezShop.recordOrderArrivalRFID(orderId, RFID));
	    
	    assert(db.getProductsRFIDbyId(productId).size() == 22);
	    
	    db.resetDB("balanceOperations");
		db.resetDB("productTypes");
		db.resetDB("orders");
		db.resetDB("balanceOperations");
		db.resetDB("products");
		
	    ezShop.logout();
	   
	}
	
	@Test
	public void addProductToSaleRFIDTestCase() throws InvalidUsernameException, InvalidPasswordException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidQuantityException, InvalidProductIdException, InvalidLocationException, InvalidOrderIdException, InvalidRFIDException, InvalidTransactionIdException {
	    //public boolean recordOrderArrival(Integer orderId) throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException;
		db.resetDB("productTypes");
		db.resetDB("orders");
		db.resetDB("balanceOperations");
		db.resetDB("products");
		ezShop.login("admin","strong");
		db.resetDB("saleTransactions");
		db.resetDB("returnTransactions");
		
		Integer productId = ezShop.createProductType("descriptionTest1", "737052355054",2.50, "product note");
		ezShop.updatePosition(productId, "1-1-1");
	    String RFID = "000000234555";
	    Integer transactionId = ezShop.startSaleTransaction();
	    Integer closedTransaction = ezShop.startSaleTransaction();
	    assertTrue(ezShop.endSaleTransaction(closedTransaction));
	    
	    ezShop.logout();
	    
	    assertThrows(UnauthorizedException.class, () -> ezShop.addProductToSaleRFID(transactionId, RFID));
	    
	    ezShop.login("admin","strong");
	    
	    assertThrows(InvalidOrderIdException.class, () -> ezShop.recordOrderArrivalRFID(null, RFID));
	    
	    db.recordProductRFID(new ProductClass(productId, RFID));
	    //RFID is not valid
	    assertThrows(InvalidRFIDException.class, () -> ezShop.addProductToSaleRFID(transactionId, null));
	    assertThrows(InvalidRFIDException.class, () -> ezShop.addProductToSaleRFID(transactionId, "022234589"));
	    assertThrows(InvalidRFIDException.class, () -> ezShop.addProductToSaleRFID(transactionId, "02223458922"));
	    assertThrows(InvalidRFIDException.class, () -> ezShop.addProductToSaleRFID(transactionId, "-022234589"));
	    assertThrows(InvalidRFIDException.class, () -> ezShop.addProductToSaleRFID(transactionId, ""));
	    
	    assertThrows(InvalidTransactionIdException.class, () -> ezShop.addProductToSaleRFID(null, RFID));
	    assertThrows(InvalidTransactionIdException.class, () -> ezShop.addProductToSaleRFID(-1, RFID));
	    assertThrows(InvalidTransactionIdException.class, () -> ezShop.addProductToSaleRFID(0, RFID));
	    
	    assertFalse(ezShop.addProductToSaleRFID(transactionId, "000000234666"));
	    assertFalse(ezShop.addProductToSaleRFID(15, RFID));
	    assertTrue(ezShop.addProductToSaleRFID(transactionId, RFID));
	    assertTrue(ezShop.addProductToSaleRFID(transactionId, RFID));	    
	    
	    db.resetDB("balanceOperations");
		db.resetDB("productTypes");
		db.resetDB("orders");
		db.resetDB("balanceOperations");
		db.resetDB("products");
		db.resetDB("saleTransactions");
		db.resetDB("returnTransactions");
		
	    ezShop.logout();
	   
	}
	
	@Test
	public void deleteProductFromSaleRFIDTestCase() throws InvalidUsernameException, InvalidPasswordException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidQuantityException, InvalidProductIdException, InvalidLocationException, InvalidOrderIdException, InvalidRFIDException, InvalidTransactionIdException {
	    //public boolean recordOrderArrival(Integer orderId) throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException;
		db.resetDB("productTypes");
		db.resetDB("orders");
		db.resetDB("balanceOperations");
		db.resetDB("products");
		ezShop.login("admin","strong");
		db.resetDB("saleTransactions");
		db.resetDB("returnTransactions");
		
		Integer productId = ezShop.createProductType("descriptionTest1", "737052355054",2.50, "product note");
		ezShop.updatePosition(productId, "1-1-1");
	    String RFID = "000000234555";
	    Integer transactionId = ezShop.startSaleTransaction();
	    Integer closedTransaction = ezShop.startSaleTransaction();   
	    
	    db.recordProductRFID(new ProductClass(productId, RFID));
	    //RFID is not valid
	    
	    assertTrue(ezShop.addProductToSaleRFID(transactionId, RFID));	  
	    assertTrue(ezShop.addProductToSaleRFID(closedTransaction, RFID));
	    ezShop.endSaleTransaction(closedTransaction);
	    ezShop.logout();
	    
	    assertThrows(UnauthorizedException.class, () -> ezShop.deleteProductFromSaleRFID(transactionId, RFID));
	    
	    ezShop.login("admin","strong");
	    assertThrows(InvalidTransactionIdException.class, () -> ezShop.deleteProductFromSaleRFID(null, RFID));
	    assertThrows(InvalidTransactionIdException.class, () -> ezShop.deleteProductFromSaleRFID(0, RFID));
	    assertThrows(InvalidRFIDException.class, () -> ezShop.deleteProductFromSaleRFID(transactionId, null));
	    assertThrows(InvalidRFIDException.class, () -> ezShop.deleteProductFromSaleRFID(transactionId, ""));
	    assertThrows(InvalidRFIDException.class, () -> ezShop.deleteProductFromSaleRFID(transactionId, "022234589"));
	    assertThrows(InvalidRFIDException.class, () -> ezShop.deleteProductFromSaleRFID(transactionId, "-022234589"));
	    
	    assertFalse(ezShop.deleteProductFromSaleRFID(transactionId, "000000234666"));
	    assertFalse(ezShop.deleteProductFromSaleRFID(15, RFID));
	    assertTrue(ezShop.deleteProductFromSaleRFID(transactionId, RFID));
	    assertFalse(ezShop.deleteProductFromSaleRFID(transactionId, RFID));
	    assertFalse(ezShop.deleteProductFromSaleRFID(closedTransaction, RFID));
	    
	    db.resetDB("balanceOperations");
		db.resetDB("productTypes");
		db.resetDB("orders");
		db.resetDB("balanceOperations");
		db.resetDB("products");
		db.resetDB("saleTransactions");
		db.resetDB("returnTransactions");
		
	    ezShop.logout();
	   
	}
	
	@Test
	public void returnProductRFIDTestCase() throws InvalidUsernameException, InvalidPasswordException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidQuantityException, InvalidProductIdException, InvalidLocationException, InvalidOrderIdException, InvalidRFIDException, InvalidTransactionIdException {
	    //public boolean recordOrderArrival(Integer orderId) throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException;
		db.resetDB("productTypes");
		db.resetDB("orders");
		db.resetDB("balanceOperations");
		db.resetDB("products");
		db.resetDB("saleTransactions");
		db.resetDB("returnTransactions");
		ezShop.login("admin","strong");
		
		Integer productId = ezShop.createProductType("descriptionTest1", "737052355054",2.50, "product note");
		Integer productId2 = ezShop.createProductType("descriptionTest2", "978020137962",2.50, "super product");
		ezShop.updatePosition(productId, "1-1-1");
		ezShop.updatePosition(productId2, "1-1-2");
	    String RFID = "000000234555";
	    String RFID2 = "000000234999";
	    Integer transactionId = ezShop.startSaleTransaction();
	    Integer closedTransaction = ezShop.startSaleTransaction();   
	    
	    db.recordProductRFID(new ProductClass(productId, RFID));
	    db.recordProductRFID(new ProductClass(productId2, RFID2));
	    //RFID is not valid
	    
	    assertTrue(ezShop.addProductToSaleRFID(transactionId, RFID));	  
	    assertTrue(ezShop.addProductToSaleRFID(closedTransaction, RFID));
	    ezShop.endSaleTransaction(transactionId);
	    Integer returnId = ezShop.startReturnTransaction(transactionId);
	    ezShop.logout();
	    
	    assertThrows(UnauthorizedException.class, () -> ezShop.returnProductRFID(transactionId, RFID));
	    
	    ezShop.login("admin","strong");
	    
	    assertThrows(InvalidTransactionIdException.class, () -> ezShop.returnProductRFID(null, RFID));
	    assertThrows(InvalidTransactionIdException.class, () -> ezShop.returnProductRFID(0, RFID));
	    
	    assertThrows(InvalidRFIDException.class, () -> ezShop.returnProductRFID(returnId, null));
	    assertThrows(InvalidRFIDException.class, () -> ezShop.returnProductRFID(returnId, ""));
	    assertThrows(InvalidRFIDException.class, () -> ezShop.returnProductRFID(returnId, "022234589"));
	    assertThrows(InvalidRFIDException.class, () -> ezShop.returnProductRFID(returnId, "-022234589"));
	    
	    
	    
	    assertFalse(ezShop.returnProductRFID(15, RFID));
	    assertFalse(ezShop.returnProductRFID(returnId, RFID2));
	    assertTrue(ezShop.returnProductRFID(returnId, RFID));
	    
	    db.resetDB("balanceOperations");
		db.resetDB("productTypes");
		db.resetDB("orders");
		db.resetDB("balanceOperations");
		db.resetDB("products");
		db.resetDB("saleTransactions");
		db.resetDB("returnTransactions");
		
	    ezShop.logout();
	   
	}
	
	
}
