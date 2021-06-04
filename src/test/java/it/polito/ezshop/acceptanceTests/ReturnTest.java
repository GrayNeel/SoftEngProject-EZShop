package it.polito.ezshop.acceptanceTests;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.Test;

import it.polito.ezshop.classes.*;
import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.InvalidCreditCardException;
import it.polito.ezshop.exceptions.InvalidLocationException;
import it.polito.ezshop.exceptions.InvalidPasswordException;
import it.polito.ezshop.exceptions.InvalidPaymentException;
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
    public void returnProductTestCase() throws UnauthorizedException,InvalidTransactionIdException,InvalidQuantityException,InvalidProductCodeException, InvalidUsernameException, InvalidPasswordException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductIdException, InvalidLocationException, InvalidRoleException{
    	db.resetDB("saleTransactions");
        db.resetDB("returnTransactions");
        db.resetDB("productReturns");
        db.resetDB("productTypes");
        db.resetDB("productEntries");
        Integer cashierId = ezShop.createUser("Cashier", "1234567", "Cashier");
        Integer shopManagerId = ezShop.createUser("shopManager", "1234567", "ShopManager");
    	assertThrows(UnauthorizedException.class, () -> ezShop.returnProduct(1,"232320",5));
    	ezShop.login("shopManager", "1234567");
    	assertThrows(InvalidTransactionIdException.class, () -> ezShop.returnProduct(null,"232320",5));
    	ezShop.logout();
    	ezShop.login("Cashier", "1234567");
    	assertThrows(InvalidTransactionIdException.class, () -> ezShop.returnProduct(null,"232320",5));
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
    	ezShop.endSaleTransaction(transactionId);
    	
    	Integer returnId = ezShop.startReturnTransaction(transactionId);
    	assertThrows(InvalidQuantityException.class, () -> ezShop.returnProduct(returnId,"232320",-1));
    	assertThrows(InvalidProductCodeException.class, () -> ezShop.returnProduct(returnId,"",5));
    	String productCode = null;
    	assertThrows(InvalidProductCodeException.class, () -> ezShop.returnProduct(156,"12",5));
    	assertFalse(ezShop.returnProduct(1922, "737052355054", 12));
    	assertFalse(ezShop.returnProduct(returnId, "737052355054", 200));
    	assertTrue(ezShop.returnProduct(returnId, "737052355054", 12));
    	ezShop.deleteSaleTransaction(transactionId);
    }
    
    @Test
    public void endReturnTransactionTestCase() throws UnauthorizedException,InvalidTransactionIdException, InvalidUsernameException, InvalidPasswordException, InvalidProductCodeException, InvalidQuantityException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidProductIdException, InvalidLocationException, InvalidRoleException{
    	db.resetDB("saleTransactions");
        db.resetDB("returnTransactions");
        db.resetDB("productReturns");
        db.resetDB("productTypes");	
        db.resetDB("productEntries");
        Integer cashierId = ezShop.createUser("Cashier", "1234567", "Cashier");
        Integer shopManagerId = ezShop.createUser("shopManager", "1234567", "ShopManager");
    	assertThrows(UnauthorizedException.class, () -> ezShop.endReturnTransaction(1,true));
    	ezShop.login("admin", "strong");
    	assertThrows(InvalidTransactionIdException.class, () -> ezShop.endReturnTransaction(-1,true));
    	assertThrows(InvalidTransactionIdException.class, () -> ezShop.endReturnTransaction(0,true));
    	Integer transactionId = ezShop.startSaleTransaction();
    	Integer returnId = ezShop.startReturnTransaction(transactionId);
    	assertFalse(ezShop.endReturnTransaction(returnId,false));
    	
    	Integer productId = ezShop.createProductType("Milk", "737052355054", 1.45, "A very good milk");
    	ezShop.updatePosition(productId, "1-1-1");
    	assertTrue(ezShop.updateQuantity(productId, 100));
    	assertTrue(ezShop.addProductToSale(transactionId,"737052355054",20));
    	assertTrue(ezShop.addProductToSale(transactionId,"737052355054",20));
    	ezShop.endSaleTransaction(transactionId);
    	
    	returnId = ezShop.startReturnTransaction(transactionId);
    	assertTrue(ezShop.returnProduct(returnId, "737052355054", 23));
    	assertTrue(ezShop.endReturnTransaction(returnId,true));
    	ezShop.deleteSaleTransaction(transactionId);
    }
    
    @Test
    public void deleteReturnTransactionTestCase() throws UnauthorizedException,InvalidTransactionIdException, InvalidUsernameException, InvalidPasswordException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, InvalidProductIdException, InvalidLocationException, InvalidQuantityException, InvalidRoleException{
    	db.resetDB("saleTransactions");
        db.resetDB("returnTransactions");
        db.resetDB("productReturns");
        db.resetDB("productTypes");
        db.resetDB("productEntries");
        Integer cashierId = ezShop.createUser("Cashier", "1234567", "Cashier");
        Integer shopManagerId = ezShop.createUser("shopManager", "1234567", "ShopManager");
    	assertThrows(UnauthorizedException.class, () -> ezShop.deleteReturnTransaction(1));
    	ezShop.login("admin", "strong");
    	assertThrows(InvalidTransactionIdException.class, () -> ezShop.deleteReturnTransaction(-1));
    	assertThrows(InvalidTransactionIdException.class, () -> ezShop.deleteReturnTransaction(null));

    	Integer transactionId = ezShop.startSaleTransaction();
    	Integer productId = ezShop.createProductType("Milk", "737052355054", 1.45, "A very good milk");
    	ezShop.updatePosition(productId, "1-1-1");
    	assertTrue(ezShop.updateQuantity(productId, 100));
    	assertTrue(ezShop.addProductToSale(transactionId,"737052355054",20));
    	assertTrue(ezShop.addProductToSale(transactionId,"737052355054",20));
    	ezShop.endSaleTransaction(transactionId);
    	
    	Integer returnId = ezShop.startReturnTransaction(transactionId);
    	assertFalse(ezShop.deleteReturnTransaction(2040));
    	returnId = ezShop.startReturnTransaction(transactionId);
    	assertTrue(ezShop.returnProduct(returnId, "737052355054", 12));
    	assertTrue(ezShop.endReturnTransaction(returnId,true));
    	assertTrue(ezShop.deleteReturnTransaction(returnId));
    	ezShop.deleteSaleTransaction(transactionId);
    }
    
    @Test
    public void receiveCashPaymentTestCase() throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidProductIdException, InvalidLocationException, InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidPaymentException, InvalidRoleException{
    	db.resetDB("saleTransactions");
        db.resetDB("returnTransactions");
        db.resetDB("productReturns");
        db.resetDB("productTypes");
        db.resetDB("productEntries");
    	
        Integer cashierId = ezShop.createUser("Cashier", "1234567", "Cashier");
        Integer shopManagerId = ezShop.createUser("shopManager", "1234567", "ShopManager");
    	assertThrows(UnauthorizedException.class, () -> ezShop.receiveCashPayment(2,5.5));
    	ezShop.login("shopManager", "1234567");
    	assertThrows(InvalidTransactionIdException.class, () -> ezShop.receiveCashPayment(null,5.5));
    	ezShop.logout();
    	ezShop.login("Cashier", "1234567");
    	assertThrows(InvalidTransactionIdException.class, () -> ezShop.receiveCashPayment(null,5.5));
    	ezShop.logout();
    	ezShop.login("admin", "strong");
    	assertThrows(InvalidTransactionIdException.class, () -> ezShop.receiveCashPayment(0,5.5));
    	assertThrows(InvalidPaymentException.class, () -> ezShop.receiveCashPayment(4,0));
    	
    	Integer transactionId = ezShop.startSaleTransaction();
    	Integer productId = ezShop.createProductType("Milk", "737052355054", 1.45, "A very good milk");
    	ezShop.updatePosition(productId, "1-1-1");
    	assertTrue(ezShop.updateQuantity(productId, 100));
    	assertTrue(ezShop.addProductToSale(transactionId,"737052355054",20));
    	assertTrue(ezShop.addProductToSale(transactionId,"737052355054",20));
    	ezShop.endSaleTransaction(transactionId);
    	
    	assertEquals(-1,ezShop.receiveCashPayment(19,3.0),0.01);
    	assertEquals(-1,ezShop.receiveCashPayment(transactionId,3.0),0.01);
    	assertNotEquals(-1,ezShop.receiveCashPayment(transactionId,60.0),0.01);
    }
    
    @Test
    public void receiveCreditCardPaymentTestCase() throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidProductIdException, InvalidLocationException, InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidPaymentException, InvalidCreditCardException, InvalidRoleException{
    	db.resetDB("saleTransactions");
        db.resetDB("returnTransactions");
        db.resetDB("productReturns");
        db.resetDB("productTypes");
        db.resetDB("productEntries");
        db.resetDB("creditCards");
        CreditCardClass cred1 = new CreditCardClass("4485123437008543", 160.0);
        CreditCardClass cred2 = new CreditCardClass("4485143337109543", 123.5);
        CreditCardClass cred3 = new CreditCardClass("4485370086510891", 170.0);
        CreditCardClass cred4 = new CreditCardClass("5303098087309156", 4999934.75);
        db.recordCreditCard(1, cred1);
        db.recordCreditCard(2, cred2);
        db.recordCreditCard(3, cred3);
        db.recordCreditCard(4, cred4);
    	
        Integer cashierId = ezShop.createUser("Cashier", "1234567", "Cashier");
        Integer shopManagerId = ezShop.createUser("shopManager", "1234567", "ShopManager");
    	assertThrows(UnauthorizedException.class, () -> ezShop.receiveCreditCardPayment(2,"128301928"));
    	ezShop.login("shopManager", "1234567");
    	assertThrows(InvalidTransactionIdException.class, () -> ezShop.receiveCreditCardPayment(-1,"128301928"));
    	ezShop.logout();
    	ezShop.login("Cashier", "1234567");
    	assertThrows(InvalidTransactionIdException.class, () -> ezShop.receiveCreditCardPayment(-1,"128301928"));
    	ezShop.logout();
    	ezShop.login("admin", "strong");
    	assertThrows(InvalidTransactionIdException.class, () -> ezShop.receiveCreditCardPayment(-1,"128301928"));
    	assertThrows(InvalidCreditCardException.class, () -> ezShop.receiveCreditCardPayment(4,"21"));
    	
    	Integer transactionId = ezShop.startSaleTransaction();
    	Integer productId = ezShop.createProductType("Milk", "737052355054", 1.45, "A very good milk");
    	ezShop.updatePosition(productId, "1-1-1");
    	assertTrue(ezShop.updateQuantity(productId, 100));
    	assertTrue(ezShop.addProductToSale(transactionId,"737052355054",20));
    	assertTrue(ezShop.addProductToSale(transactionId,"737052355054",20));
    	ezShop.endSaleTransaction(transactionId);
    	
    	assertFalse(ezShop.receiveCreditCardPayment(transactionId,"4532083296331932"));
    	assertFalse(ezShop.receiveCreditCardPayment(190,"5119705625622338"));
    	assertFalse(ezShop.receiveCreditCardPayment(transactionId,"5119705625622338"));
    	assertTrue(ezShop.receiveCreditCardPayment(transactionId,"5303098087309156"));

    }
    
    @Test
    public void returnCashPaymentTestCase() throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidProductIdException, InvalidLocationException, InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidPaymentException, InvalidCreditCardException, InvalidRoleException{
    	db.resetDB("saleTransactions");
        db.resetDB("returnTransactions");
        db.resetDB("productReturns");
        db.resetDB("productTypes");
        db.resetDB("productEntries");
    	
        Integer cashierId = ezShop.createUser("Cashier", "1234567", "Cashier");
        Integer shopManagerId = ezShop.createUser("shopManager", "1234567", "ShopManager");
    	assertThrows(UnauthorizedException.class, () -> ezShop.returnCashPayment(2));
    	ezShop.login("shopManager", "1234567");
    	assertThrows(InvalidTransactionIdException.class, () -> ezShop.returnCashPayment(0));
    	ezShop.logout();
    	ezShop.login("Cashier", "1234567");
    	assertThrows(InvalidTransactionIdException.class, () -> ezShop.returnCashPayment(-1));
    	ezShop.logout();
    	ezShop.login("admin", "strong");
    	assertThrows(InvalidTransactionIdException.class, () -> ezShop.returnCashPayment(null));
    	
    	Integer transactionId = ezShop.startSaleTransaction();
    	Integer productId = ezShop.createProductType("Milk", "737052355054", 1.45, "A very good milk");
    	ezShop.updatePosition(productId, "1-1-1");
    	assertTrue(ezShop.updateQuantity(productId, 100));
    	assertTrue(ezShop.addProductToSale(transactionId,"737052355054",20));
    	assertTrue(ezShop.addProductToSale(transactionId,"737052355054",20));
    	ezShop.endSaleTransaction(transactionId);
    	Integer returnId = ezShop.startReturnTransaction(transactionId);
    	assertTrue(ezShop.returnProduct(returnId, "737052355054", 23));
    	assertTrue(ezShop.endReturnTransaction(returnId,true));
    	
    	assertEquals(-1,ezShop.returnCashPayment(190),0.01);
    	assertNotEquals(-1,ezShop.returnCashPayment(returnId),0.01);
    }
    
    @Test
    public void returnCreditCardPaymentTestCase() throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidProductIdException, InvalidLocationException, InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidPaymentException, InvalidCreditCardException, InvalidRoleException{
    	db.resetDB("saleTransactions");
        db.resetDB("returnTransactions");
        db.resetDB("productReturns");
        db.resetDB("productTypes");
        db.resetDB("productEntries");
        db.resetDB("creditCards");
        CreditCardClass cred1 = new CreditCardClass("4485123437008543", 160.0);
        CreditCardClass cred2 = new CreditCardClass("4485143337109543", 123.5);
        CreditCardClass cred3 = new CreditCardClass("4485370086510891", 170.0);
        CreditCardClass cred4 = new CreditCardClass("5303098087309156", 4999934.75);
        db.recordCreditCard(1, cred1);
        db.recordCreditCard(2, cred2);
        db.recordCreditCard(3, cred3);
        db.recordCreditCard(4, cred4);
    	
        Integer cashierId = ezShop.createUser("Cashier", "1234567", "Cashier");
        Integer shopManagerId = ezShop.createUser("shopManager", "1234567", "ShopManager");
    	assertThrows(UnauthorizedException.class, () -> ezShop.returnCreditCardPayment(2,"5303098087309156"));
    	ezShop.login("shopManager", "1234567");
    	assertThrows(InvalidTransactionIdException.class, () -> ezShop.returnCreditCardPayment(0,"5303098087309156"));
    	ezShop.logout();
    	ezShop.login("Cashier", "1234567");
    	assertThrows(InvalidTransactionIdException.class, () -> ezShop.returnCreditCardPayment(-1,"5303098087309156"));
    	ezShop.logout();
    	ezShop.login("admin", "strong");
    	assertThrows(InvalidTransactionIdException.class, () -> ezShop.returnCreditCardPayment(null,"5303098087309156"));
    	assertThrows(InvalidCreditCardException.class, () -> ezShop.returnCreditCardPayment(12,""));
//    	assertThrows(InvalidCreditCardException.class, () -> ezShop.returnCreditCardPayment(11,null));
    	assertThrows(InvalidCreditCardException.class, () -> ezShop.returnCreditCardPayment(11,"11209"));
    	
    	Integer transactionId = ezShop.startSaleTransaction();
    	Integer productId = ezShop.createProductType("Milk", "737052355054", 1.45, "A very good milk");
    	ezShop.updatePosition(productId, "1-1-1");
    	assertTrue(ezShop.updateQuantity(productId, 100));
    	assertTrue(ezShop.addProductToSale(transactionId,"737052355054",20));
    	assertTrue(ezShop.addProductToSale(transactionId,"737052355054",20));
    	ezShop.endSaleTransaction(transactionId);
    	Integer returnId = ezShop.startReturnTransaction(transactionId);
    	assertTrue(ezShop.returnProduct(returnId, "737052355054", 25));
    	assertTrue(ezShop.endReturnTransaction(returnId,true));
    	
    	assertEquals(-1,ezShop.returnCreditCardPayment(190,"6011264249365616"),0.01);
    	assertEquals(-1,ezShop.returnCreditCardPayment(returnId,"6011264249365616"),0.01);
    	assertNotEquals(-1,ezShop.returnCreditCardPayment(returnId,"5303098087309156"),0.01);

    }
    
    @Test
    public void getCreditAndDebitsTestCase() throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidProductIdException, InvalidLocationException, InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidPaymentException, InvalidCreditCardException, InvalidRoleException{
    	db.resetDB("saleTransactions");
        db.resetDB("returnTransactions");
        db.resetDB("productReturns");
        db.resetDB("productTypes");
        db.resetDB("productEntries");
        Integer shopManagerId = ezShop.createUser("shopManager", "1234567", "ShopManager");
        LocalDate startDate = LocalDate.parse("2016-08-16");
        LocalDate startDate2 = LocalDate.parse("2022-08-16");
        LocalDate finalDate = LocalDate.parse("2022-10-12");
    	assertThrows(UnauthorizedException.class, () -> ezShop.getCreditsAndDebits(startDate,finalDate));
    	ezShop.login("shopManager", "1234567");
    	assert(ezShop.getCreditsAndDebits(startDate,finalDate).size()>0);
    	assert(ezShop.getCreditsAndDebits(null,finalDate).size()>0);
    	assert(ezShop.getCreditsAndDebits(startDate,null).size()>0);
    	assert(ezShop.getCreditsAndDebits(startDate2,finalDate).size()==0);
    	
    }
    
    @Test
    public void computeBalanceTestCase() throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidProductIdException, InvalidLocationException, InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidPaymentException, InvalidCreditCardException{
    	assertThrows(UnauthorizedException.class, () -> ezShop.computeBalance());
    	ezShop.login("admin", "strong");
    	
    	assert(ezShop.computeBalance()!=0);
    	
    }
    
    @Test
    public void recordBalanceTestCase() throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidProductIdException, InvalidLocationException, InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, InvalidProductDescriptionException, InvalidPricePerUnitException, InvalidPaymentException, InvalidCreditCardException{
    	assertThrows(UnauthorizedException.class, () -> ezShop.recordBalanceUpdate(10.0));
    	ezShop.login("admin", "strong");
    	
    	assertFalse(ezShop.recordBalanceUpdate(-1000000000));
    	assertTrue(ezShop.recordBalanceUpdate(10.25));
    	
    }
    
    
}
