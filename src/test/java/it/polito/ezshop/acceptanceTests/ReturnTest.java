package it.polito.ezshop.acceptanceTests;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import it.polito.ezshop.classes.*;
import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.InvalidLocationException;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidPricePerUnitException;
import it.polito.ezshop.exceptions.InvalidProductCodeException;
import it.polito.ezshop.exceptions.InvalidProductDescriptionException;
import it.polito.ezshop.exceptions.InvalidProductIdException;
import it.polito.ezshop.exceptions.InvalidQuantityException;
import it.polito.ezshop.exceptions.InvalidRoleException;
import it.polito.ezshop.exceptions.InvalidTransactionIdException;
import it.polito.ezshop.exceptions.InvalidUsernameException;
import it.polito.ezshop.exceptions.UnauthorizedException;

public class ReturnTest {
	EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
    EZShopDB db = new EZShopDB();
    
    
    @Test
    public void startReturnTransactionTestCase() throws InvalidTransactionIdException,UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidRoleException{
    	db.resetDB("saleTransactions");
        db.resetDB("returnTransactions");
        Integer cashierId = ezShop.createUser("Cashier", "1234567", "Cashier");
        Integer shopManagerId = ezShop.createUser("shopManager", "1234567", "ShopManager");
        
    	assertThrows(UnauthorizedException.class, () -> ezShop.startReturnTransaction(1));
    	ezShop.login("admin", "strong");
    	assertThrows(InvalidTransactionIdException.class, () -> ezShop.startReturnTransaction(null));
    	ezShop.logout();
    	ezShop.login("Cashier", "1234567");
    	assertThrows(InvalidTransactionIdException.class, () -> ezShop.startReturnTransaction(null));
    	ezShop.logout();
    	ezShop.login("shopManager", "1234567");
    	assertThrows(InvalidTransactionIdException.class, () -> ezShop.startReturnTransaction(null));
    	
    	assertThrows(InvalidTransactionIdException.class, () -> ezShop.startReturnTransaction(null));
    	assertThrows(InvalidTransactionIdException.class, () -> ezShop.startReturnTransaction(0));
    	Integer transactionId = ezShop.startSaleTransaction();
    	assert(ezShop.startReturnTransaction(transactionId) != -1);
    	ezShop.deleteSaleTransaction(transactionId);
    	ezShop.logout();
    }
    
    @Test
    public void returnProductTestCase() throws UnauthorizedException,InvalidTransactionIdException,InvalidQuantityException,InvalidProductCodeException, InvalidUsernameException, InvalidPasswordException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductIdException, InvalidLocationException{
    	db.resetDB("saleTransactions");
        db.resetDB("returnTransactions");
        db.resetDB("productReturns");
        db.resetDB("productTypes");
    	assertThrows(UnauthorizedException.class, () -> ezShop.returnProduct(1,"232320",5));
    	ezShop.login("admin", "strong");
    	assertThrows(InvalidTransactionIdException.class, () -> ezShop.returnProduct(null,"232320",5));
    	ezShop.logout();
    	ezShop.login("Cashier", "1234567");
    	assertThrows(InvalidTransactionIdException.class, () -> ezShop.returnProduct(null,"232320",5));
    	ezShop.logout();
    	ezShop.login("shopManager", "1234567");
    	
    	assertThrows(InvalidTransactionIdException.class, () -> ezShop.returnProduct(null,"232320",5));
    	assertThrows(InvalidTransactionIdException.class, () -> ezShop.returnProduct(-1,"232320",5));
    	
    	Integer transactionId = ezShop.startSaleTransaction();
    	Integer productId = ezShop.createProductType("Milk", "12345670", 1.45, "A very good milk");
    	ezShop.updateQuantity(productId, 100);
    	ezShop.updatePosition(productId, "1-1-1");
    	assertTrue(ezShop.addProductToSale(transactionId,"12345670",20));
    	ezShop.endSaleTransaction(transactionId);
    	
    	Integer returnId = ezShop.startReturnTransaction(transactionId);
    	assertThrows(InvalidQuantityException.class, () -> ezShop.returnProduct(returnId,"232320",-1));
    	assertThrows(InvalidProductCodeException.class, () -> ezShop.returnProduct(returnId,"",5));
    	String productCode = null;
    	assertThrows(InvalidProductCodeException.class, () -> ezShop.returnProduct(156,"12",5));
    	assertFalse(ezShop.returnProduct(1922, "12345670", 12));
    	assertTrue(ezShop.returnProduct(returnId, "12345670", 12));
    	ezShop.deleteSaleTransaction(transactionId);
    }
    
//    @Test
//    public void endReturnTransactionTestCase() throws UnauthorizedException,InvalidTransactionIdException, InvalidUsernameException, InvalidPasswordException, InvalidProductCodeException, InvalidQuantityException, InvalidProductDescriptionException, InvalidPricePerUnitException{
//    	db.resetDB("saleTransactions");
//        db.resetDB("returnTransactions");
//        db.resetDB("productTypes");	
//    	assertThrows(UnauthorizedException.class, () -> ezShop.endReturnTransaction(1,true));
//    	ezShop.login("admin", "strong");
//    	assertThrows(InvalidTransactionIdException.class, () -> ezShop.endReturnTransaction(-1,true));
//    	assertThrows(InvalidTransactionIdException.class, () -> ezShop.endReturnTransaction(0,true));
//    	Integer transactionId = ezShop.startSaleTransaction();
//    	Integer returnId = ezShop.startReturnTransaction(transactionId);
//    	assertFalse(ezShop.endReturnTransaction(returnId,false));
//    	
//    	ezShop.createProductType("Milk", "12345670", 1.45, "A very good milk");
//    	ezShop.addProductToSale(transactionId,"12345670",50);
//    	
//    	returnId = ezShop.startReturnTransaction(transactionId);
//    	ezShop.returnProduct(returnId, "12345670", 23);
//    	assertTrue(ezShop.endReturnTransaction(returnId,true));
//    	ezShop.deleteSaleTransaction(transactionId);
//    }
//    
//    @Test
//    public void deleteReturnTransactionTestCase() throws UnauthorizedException,InvalidTransactionIdException, InvalidUsernameException, InvalidPasswordException{
//    	db.resetDB("saleTransactions");
//        db.resetDB("returnTransactions");
//    	assertThrows(UnauthorizedException.class, () -> ezShop.deleteReturnTransaction(1));
//    	ezShop.login("admin", "strong");
//    	assertThrows(InvalidTransactionIdException.class, () -> ezShop.deleteReturnTransaction(-1));
//    	assertThrows(InvalidTransactionIdException.class, () -> ezShop.deleteReturnTransaction(null));
//    	Integer transactionId = ezShop.startSaleTransaction();
//    	Integer returnId = ezShop.startReturnTransaction(transactionId);
//    	assertFalse(ezShop.deleteReturnTransaction(2040));
//    	returnId = ezShop.startReturnTransaction(transactionId);
//    	assertTrue(ezShop.deleteReturnTransaction(returnId));
//    	ezShop.deleteSaleTransaction(transactionId);
//    }
}
