package it.polito.ezshop.acceptanceTests;

import it.polito.ezshop.classes.*;
import it.polito.ezshop.data.BalanceOperation;
import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.data.TicketEntry;
import it.polito.ezshop.data.User;
import it.polito.ezshop.exceptions.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.Test;

public class SaleTransactionsTest {
	EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
	EZShopDB db = new EZShopDB();
	
	@Test
	public void startSaleTransactionTestCase() throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidCustomerNameException {
		db.resetDB("customers");
		db.resetDB("saleTransactions");
		
		assertThrows(UnauthorizedException.class, () -> ezShop.startSaleTransaction());
	    
	    ezShop.login("admin","strong");
	    
	    Integer saleTransactionId = ezShop.startSaleTransaction();
	    assert( saleTransactionId > 0); 
	    ezShop.logout();		
	}
	
	@Test
	public void addProductToSaleTestCase() throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException,	UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductIdException, InvalidLocationException{
		db.resetDB("customers");
		db.resetDB("saleTransactions");
		db.resetDB("productTypes");

		assertThrows(UnauthorizedException.class, () -> ezShop.addProductToSale(2,"productCode", 100));
		
		ezShop.login("admin","strong");
		
		Integer saleTransactionId = ezShop.startSaleTransaction();
		
		Integer productId = ezShop.createProductType("descriptionTest", "737052355054",2.50, "product note");
		
		assertThrows(InvalidTransactionIdException.class, () -> ezShop.addProductToSale(-10, "737052355054", 100));
		//assertThrows(InvalidTransactionIdException.class, () -> ezShop.addProductToSale(null, "737052355054", 100));		
		
		assertThrows(InvalidProductCodeException.class, () -> ezShop.addProductToSale(saleTransactionId, "", 100));
		assertThrows(InvalidProductCodeException.class, () -> ezShop.addProductToSale(saleTransactionId, null, 100));
		assertThrows(InvalidProductCodeException.class, () -> ezShop.addProductToSale(saleTransactionId, "123", 100)); //737052355059
		
		assertThrows(InvalidQuantityException.class, () -> ezShop.addProductToSale(saleTransactionId, "737052355054", -10));
		
						
		//assertFalse (ezShop.addProductToSale(saleTransactionId, "737052355059", 100)); //if (product == null) return false;
		
	    assertFalse (ezShop.addProductToSale(saleTransactionId, "737052355054", 100));
	    
	    //aumento la quantità del prodotto
	    ezShop.updatePosition(productId,"1-1-1");
	    ezShop.updateQuantity(productId, 400);
	    
	    assertTrue(ezShop.addProductToSale(saleTransactionId, "737052355054", 100)); //not yet in ticket entries		    
	    assertTrue(ezShop.addProductToSale(saleTransactionId, "737052355054", 200)); //already in ticket entry
		ezShop.logout();
	}
	
	@Test
	public void deleteProductFromSaleTestCase() throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException,
	UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductIdException, InvalidLocationException {	    
		db.resetDB("customers");
		db.resetDB("saleTransactions");
		db.resetDB("productTypes");
		
		assertThrows(UnauthorizedException.class, () -> ezShop.deleteProductFromSale(2,"productCode", 100));
		
		ezShop.login("admin","strong");
		
		Integer saleTransactionId = ezShop.startSaleTransaction();
		Integer productId = ezShop.createProductType("descriptionTest", "737052355054",2.50, "product note");
		
		assertThrows(InvalidTransactionIdException.class, () -> ezShop.deleteProductFromSale(-10, "737052355054", 100));
		//assertThrows(InvalidTransactionIdException.class, () -> ezShop.deleteProductFromSale(null, "737052355054", 100));		
		
		assertThrows(InvalidProductCodeException.class, () -> ezShop.deleteProductFromSale(saleTransactionId, "", 100));
		assertThrows(InvalidProductCodeException.class, () -> ezShop.deleteProductFromSale(saleTransactionId, null, 100));
		assertThrows(InvalidProductCodeException.class, () -> ezShop.deleteProductFromSale(saleTransactionId, "123", 100)); //737052355059
		
		assertThrows(InvalidQuantityException.class, () -> ezShop.deleteProductFromSale(saleTransactionId, "737052355054", -10));
				
		db.updateTransactionState(saleTransactionId, "PAYED");		
		assertFalse(ezShop.deleteProductFromSale(saleTransactionId, "737052355054", 10));
		db.updateTransactionState(saleTransactionId, "SARTED");
		 //aumento la quantità del prodotto
	    ezShop.updatePosition(productId,"1-1-1");
	    ezShop.updateQuantity(productId, 400);
	    
	    ezShop.addProductToSale(saleTransactionId, "737052355054", 100); //not yet in ticket entries	
	    assertFalse(ezShop.deleteProductFromSale(saleTransactionId, "737052355054", 900));
		assertTrue(ezShop.deleteProductFromSale(saleTransactionId, "737052355054", 100));	
		
		
		assertFalse (ezShop.getProductTypeByBarCode("737052355054").getQuantity() < 100);
		ezShop.logout();
	}
	
	@Test
	public void applyDiscountRateToProductTestCase() throws InvalidTransactionIdException, InvalidProductCodeException, InvalidDiscountRateException,
	UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidCustomerNameException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidQuantityException, InvalidProductIdException, InvalidLocationException {
		db.resetDB("customers");
		db.resetDB("saleTransactions");
		db.resetDB("productTypes");
		
		assertThrows(UnauthorizedException.class, () -> ezShop.applyDiscountRateToProduct(2,"productCode", 0.4));
		
		ezShop.login("admin","strong");
		
		Integer saleTransactionId = ezShop.startSaleTransaction();
		Integer productId =  ezShop.createProductType("descriptionTest", "737052355054",2.50, "product note");
		
		assertThrows(InvalidTransactionIdException.class, () -> ezShop.applyDiscountRateToProduct(-10, "737052355054", 0.4));
		//assertThrows(InvalidTransactionIdException.class, () -> ezShop.applyDiscountRateToProduct(null, "737052355054", 0.4));		
		
		assertThrows(InvalidProductCodeException.class, () -> ezShop.applyDiscountRateToProduct(saleTransactionId, "", 0.4));
		assertThrows(InvalidProductCodeException.class, () -> ezShop.applyDiscountRateToProduct(saleTransactionId, null, 0.4));
		assertThrows(InvalidProductCodeException.class, () -> ezShop.applyDiscountRateToProduct(saleTransactionId, "123", 0.4)); //737052355059
		
		assertThrows(InvalidDiscountRateException.class, () -> ezShop.applyDiscountRateToProduct(saleTransactionId, "737052355054", -0.4));
		assertThrows(InvalidDiscountRateException.class, () -> ezShop.applyDiscountRateToProduct(saleTransactionId, "737052355054", 1.4));
		
		ezShop.createProductType("descriptionTest1", "737052355054",2.50, "product note");
		
		//aumento la quantità del prodotto
	    ezShop.updatePosition(productId,"1-1-1");
	    ezShop.updateQuantity(productId, 400);
		
		ezShop.addProductToSale(saleTransactionId, "737052355054", 20);				
	
	    assertTrue(ezShop.applyDiscountRateToProduct(saleTransactionId, "737052355054", 0.2));
		ezShop.logout();		
	}
	
	@Test
	public void applyDiscountRateToSaleTestCase() throws InvalidTransactionIdException, InvalidDiscountRateException, UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidCustomerNameException {
		db.resetDB("customers");
		db.resetDB("saleTransactions");
		
		
		assertThrows(UnauthorizedException.class, () -> ezShop.applyDiscountRateToSale(2, 0.4));
		
		ezShop.login("admin","strong");
		
		Integer saleTransactionId = ezShop.startSaleTransaction();		
		
		
		assertThrows(InvalidTransactionIdException.class, () -> ezShop.applyDiscountRateToSale(-10, 0.4));
		//assertThrows(InvalidTransactionIdException.class, () -> ezShop.applyDiscountRateToSale(null, 0.4));		
		
		assertThrows(InvalidDiscountRateException.class, () -> ezShop.applyDiscountRateToSale(saleTransactionId, -0.4));
		assertThrows(InvalidDiscountRateException.class, () -> ezShop.applyDiscountRateToSale(saleTransactionId, 1.4));
		
		db.updateTransactionState(saleTransactionId, "PAYED");		
		assertFalse(ezShop.applyDiscountRateToSale(saleTransactionId, 0.2));
		db.updateTransactionState(saleTransactionId, "SARTED");		
		
		
	
	    assertTrue(ezShop.applyDiscountRateToSale(saleTransactionId, 0.2));	    
	    
	    SaleTransactionClass st = db.getSaleTransactionById(saleTransactionId);
	    assert(st.getDiscountRate() == 0.2);		
		
		ezShop.logout();
	}
	
	@Test
	public void computePointsForSaleTestCase() throws InvalidTransactionIdException, UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidCustomerNameException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidQuantityException, InvalidProductIdException, InvalidLocationException  {
		db.resetDB("customers");
		db.resetDB("saleTransactions");
		db.resetDB("productTypes");
		db.resetDB("productEntries");
		assertThrows(UnauthorizedException.class, () -> ezShop.computePointsForSale(2));
		
		ezShop.login("admin","strong");			
		
		assertThrows(InvalidTransactionIdException.class, () -> ezShop.computePointsForSale(-10));
		//assertThrows(InvalidTransactionIdException.class, () -> ezShop.computePointsForSale(null));		
		
		assert(ezShop.computePointsForSale(999) == -1);
		
		Integer saleTransactionId = ezShop.startSaleTransaction();	
		Integer prod1 = ezShop.createProductType("descriptionTest1", "737052355054",2.50, "product note");
		Integer prod2 = ezShop.createProductType("descriptionTest2", "12344674332827",1.50, "productnote");
		
		 ezShop.updatePosition(prod1,"1-1-1");
		 ezShop.updateQuantity(prod1, 150);
		    
		 ezShop.updatePosition(prod2,"2-1-1");
		 ezShop.updateQuantity(prod2, 400);
		
		db.updateTransactionState(saleTransactionId, "OPEN");	
		assert(ezShop.computePointsForSale(saleTransactionId) == -1); // no entries
		
		ezShop.addProductToSale(saleTransactionId, "737052355054", 100); //not yet in ticket entries
		ezShop.addProductToSale(saleTransactionId, "12344674332827", 200); //not yet in ticket entries
		ezShop.addProductToSale(saleTransactionId, "737052355054", 10); //already in ticket entries
		
		assert(ezShop.computePointsForSale(saleTransactionId) == 575.0);
		
		ezShop.addProductToSale(saleTransactionId, "737052355054", 100); //not yet in ticket entries
		ezShop.addProductToSale(saleTransactionId, "12344674332827", 200); //not yet in ticket entries
		ezShop.addProductToSale(saleTransactionId, "737052355054", 10); //already in ticket entries
		db.updateTransactionState(saleTransactionId, "PAYED");	
		ezShop.endSaleTransaction(saleTransactionId);	
		
		
		//aggiungere entries o è nullo
		assert(ezShop.computePointsForSale(saleTransactionId) == 900.0);	//serve aggiungere entries per i pt
		
		ezShop.logout();
	}
	
	
	@Test
	public void endSaleTransactionTestCase() throws InvalidTransactionIdException, UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidCustomerNameException, InvalidProductCodeException, InvalidQuantityException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductIdException, InvalidLocationException {
		db.resetDB("customers");
		db.resetDB("saleTransactions");
		db.resetDB("productTypes");
		db.resetDB("productEntries");
		
		assertThrows(UnauthorizedException.class, () -> ezShop.endSaleTransaction(2));
		
		ezShop.login("admin","strong");
		
				
		
		assertThrows(InvalidTransactionIdException.class, () -> ezShop.endSaleTransaction(-10));
		//assertThrows(InvalidTransactionIdException.class, () -> ezShop.endSaleTransaction(null));	
		
		Integer saleTransactionId = ezShop.startSaleTransaction();
		Integer prod1 = ezShop.createProductType("descriptionTest1", "737052355054",2.50, "product note");
		Integer prod2 = ezShop.createProductType("descriptionTest2", "12344674332827",1.50, "productnote");
		
		 ezShop.updatePosition(prod1,"1-1-1");
		 ezShop.updateQuantity(prod1, 150);
		    
		 ezShop.updatePosition(prod2,"2-1-1");
		 ezShop.updateQuantity(prod2, 400);
		
		
		ezShop.addProductToSale(saleTransactionId, "737052355054", 100); //not yet in ticket entries
		ezShop.addProductToSale(saleTransactionId, "12344674332827", 200); //not yet in ticket entries
		
		db.updateTransactionState(saleTransactionId, "PAYED");		
		assertFalse(ezShop.endSaleTransaction(saleTransactionId));
		db.updateTransactionState(saleTransactionId, "STARTED");	
		
		
	
	    assertTrue(ezShop.endSaleTransaction(saleTransactionId));	    
	    
	    //check if ticketEntries è vuoto?
	   
		ezShop.logout();
	}
	
	@Test
    public void getAndDeleteSaleTransactionTestCase() throws UnauthorizedException,InvalidTransactionIdException,InvalidQuantityException,InvalidProductCodeException, InvalidUsernameException, InvalidPasswordException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductIdException, InvalidLocationException{
    	db.resetDB("saleTransactions");
        db.resetDB("returnTransactions");
        db.resetDB("productReturns");
        db.resetDB("productTypes");
        db.resetDB("productEntries");
        
        try {
        	Integer userId = ezShop.createUser("shopManager", "1234567", "ShopManager");
        	Integer userId2 = ezShop.createUser("Cashier", "1234567", "Cashier");
        } catch (Exception e) {
        	e.printStackTrace();
        }
        
    	assertThrows(UnauthorizedException.class, () -> ezShop.getSaleTransaction(1));
    	assertThrows(UnauthorizedException.class, () -> ezShop.deleteSaleTransaction(1));
    	ezShop.login("shopManager", "1234567");
    	assertThrows(InvalidTransactionIdException.class, () -> ezShop.getSaleTransaction(null));
    	assertThrows(InvalidTransactionIdException.class, () -> ezShop.deleteSaleTransaction(null));
    	ezShop.logout();
    	ezShop.login("Cashier", "1234567");
    	assertThrows(InvalidTransactionIdException.class, () -> ezShop.getSaleTransaction(0));
    	assertThrows(InvalidTransactionIdException.class, () -> ezShop.deleteSaleTransaction(0));
    	ezShop.logout();
    	ezShop.login("admin", "strong");
    	
    	
    	assertThrows(InvalidTransactionIdException.class, () -> ezShop.returnProduct(null,"232320",5));
    	assertThrows(InvalidTransactionIdException.class, () -> ezShop.returnProduct(-1,"232320",5));
    	
    	Integer transactionId = ezShop.startSaleTransaction();
    	Integer productId = ezShop.createProductType("Milk", "737052355054", 1.45, "A very good milk");
    	ezShop.updatePosition(productId, "1-1-1");
    	assertTrue(ezShop.updateQuantity(productId, 100));
    	assertTrue(ezShop.addProductToSale(transactionId,"737052355054",20));
    	assertTrue(ezShop.addProductToSale(transactionId,"737052355054",20));
    	assertTrue(ezShop.endSaleTransaction(transactionId));
    	db.updateTransactionState(transactionId, "PAYED");
    	assertNotNull(ezShop.getSaleTransaction(transactionId));
    	ezShop.endSaleTransaction(transactionId);
//    	ezShop.deleteSaleTransaction(transactionId);
    }
	
    @Test
    public void getSaleTransactionTestCase() throws UnauthorizedException,InvalidTransactionIdException,InvalidQuantityException,InvalidProductCodeException, InvalidUsernameException, InvalidPasswordException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductIdException, InvalidLocationException{
    	db.resetDB("saleTransactions");
        db.resetDB("returnTransactions");
        db.resetDB("productReturns");
        db.resetDB("productTypes");
        db.resetDB("productEntries");
    	
        try {
        	Integer userId = ezShop.createUser("shopManager", "1234567", "ShopManager");
        	Integer userId2 = ezShop.createUser("Cashier", "1234567", "Cashier");
        } catch (Exception e) {
        	e.printStackTrace();
        }
        
        assertThrows(UnauthorizedException.class, () -> ezShop.getSaleTransaction(1));    	
    	ezShop.login("shopManager", "1234567");
    	assertThrows(InvalidTransactionIdException.class, () -> ezShop.getSaleTransaction(null));    	
    	ezShop.logout();
    	ezShop.login("Cashier", "1234567");
    	assertThrows(InvalidTransactionIdException.class, () -> ezShop.getSaleTransaction(0));    	
    	ezShop.logout();
    	ezShop.login("admin", "strong");
    	
    	
    	assertThrows(InvalidTransactionIdException.class, () -> ezShop.returnProduct(null,"232320",5));
    	assertThrows(InvalidTransactionIdException.class, () -> ezShop.returnProduct(-1,"232320",5));
    	
    	Integer transactionId = ezShop.startSaleTransaction();
    	Integer productId = ezShop.createProductType("Milk", "737052355054", 1.45, "A very good milk");
    	ezShop.updatePosition(productId, "1-1-1");
    	assertTrue(ezShop.updateQuantity(productId, 100));
    	assertTrue(ezShop.addProductToSale(transactionId,"737052355054",20));
    	assertTrue(ezShop.addProductToSale(transactionId,"737052355054",20));
    	assertTrue(ezShop.endSaleTransaction(transactionId));
    	db.updateTransactionState(transactionId, "PAYED");
    	assertNotNull(ezShop.getSaleTransaction(transactionId));
    	ezShop.endSaleTransaction(transactionId);    	
    
    }
    
    @Test
    public void deleteSaleTransactionTestCase() throws UnauthorizedException,InvalidTransactionIdException,InvalidQuantityException,InvalidProductCodeException, InvalidUsernameException, InvalidPasswordException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductIdException, InvalidLocationException{
    	db.resetDB("saleTransactions");
        db.resetDB("returnTransactions");
        db.resetDB("productReturns");
        db.resetDB("productTypes");
        db.resetDB("productEntries");
        try {
        	Integer userId = ezShop.createUser("shopManager", "1234567", "ShopManager");
        	Integer userId2 = ezShop.createUser("Cashier", "1234567", "Cashier");
        } catch (Exception e) {
        	e.printStackTrace();
        }
    	assertThrows(UnauthorizedException.class, () -> ezShop.deleteSaleTransaction(1));
    	ezShop.login("shopManager", "1234567");    	
    	assertThrows(InvalidTransactionIdException.class, () -> ezShop.deleteSaleTransaction(null));
    	ezShop.logout();
    	ezShop.login("Cashier", "1234567");    	
    	assertThrows(InvalidTransactionIdException.class, () -> ezShop.deleteSaleTransaction(0));
    	ezShop.logout();
    	ezShop.login("admin", "strong");
    	
    	
    	assertThrows(InvalidTransactionIdException.class, () -> ezShop.returnProduct(null,"232320",5));
    	assertThrows(InvalidTransactionIdException.class, () -> ezShop.returnProduct(-1,"232320",5));
    	
    	Integer transactionId = ezShop.startSaleTransaction();
    	Integer productId = ezShop.createProductType("Milk", "737052355054", 1.45, "A very good milk");
    	ezShop.updatePosition(productId, "1-1-1");
    	assertTrue(ezShop.updateQuantity(productId, 100));
    	assertTrue(ezShop.addProductToSale(transactionId,"737052355054",20));
    	assertTrue(ezShop.addProductToSale(transactionId,"737052355054",20));
    	assertTrue(ezShop.endSaleTransaction(transactionId));
    	db.updateTransactionState(transactionId, "PAYED");
    	assertNotNull(ezShop.getSaleTransaction(transactionId));
    	ezShop.endSaleTransaction(transactionId);
    	ezShop.deleteSaleTransaction(transactionId);
    }
	
	
}
