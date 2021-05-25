package it.polito.ezshop.acceptanceTests;

import it.polito.ezshop.classes.*;

import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CustomersTest {
	EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
	EZShopDB db = new EZShopDB();
	
	@Test
	public void defineCustomerTestCase() throws InvalidCustomerNameException, UnauthorizedException, InvalidUsernameException, InvalidPasswordException {
		db.resetDB("customers");
		
		assertThrows(UnauthorizedException.class, () -> ezShop.defineCustomer("custmerName"));

		//ezShop.login("cashier","1234567");
		//assertThrows(UnauthorizedException.class, () -> ezShop.defineCustomer("custmerName"));
		//ezShop.logout();
		
		//Given as tested
		ezShop.login("admin","strong");
		
		assertThrows(InvalidCustomerNameException.class, () -> ezShop.defineCustomer(""));
		assertThrows(InvalidCustomerNameException.class, () -> ezShop.defineCustomer(null));		
	
		
		Integer customerId = ezShop.defineCustomer("customerName");
		assert( customerId > 0);
		
		assert(ezShop.defineCustomer("customerName") == -1);
		
		ezShop.logout();
	}
	
	@Test
	public void modifyCustomerTestCase() throws InvalidCustomerNameException, InvalidCustomerCardException, InvalidCustomerIdException,
												UnauthorizedException, InvalidUsernameException, InvalidPasswordException {
		db.resetDB("customers");

		assertThrows(UnauthorizedException.class, () -> ezShop.modifyCustomer(2,"newCustomerName", "1234567890"));
		
		ezShop.login("admin","strong");
		
		assertThrows(InvalidCustomerIdException.class, () -> ezShop.modifyCustomer(-100, "newCustomerName", "1234567890"));
		assertThrows(InvalidCustomerIdException.class, () -> ezShop.modifyCustomer(null, "newCustomerName", "1234567890"));
		
		assertThrows(InvalidCustomerNameException.class, () -> ezShop.modifyCustomer(2, "", "1234567890"));
		assertThrows(InvalidCustomerNameException.class, () -> ezShop.modifyCustomer(2, null, "1234567890"));		
		
		assertThrows(InvalidCustomerCardException.class, () -> ezShop.modifyCustomer(2, "newCustomerName", "123"));
		assertThrows(InvalidCustomerCardException.class, () -> ezShop.modifyCustomer(2, "newCustomerName", "12345678901112"));
		
		
		Integer customerId = ezShop.defineCustomer("customerName");
						
		assertTrue(ezShop.modifyCustomer(customerId,"newCustomerName", "0987654321"));
	
		ezShop.logout();
	}
	
	@Test
	public void deleteCustomerTestCase() throws InvalidCustomerIdException, UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidCustomerNameException {	    
		db.resetDB("customers");
		
	    ezShop.login("admin","strong");
	    Integer customerId = ezShop.defineCustomer("customerName");
	    ezShop.logout();

	    assertThrows(UnauthorizedException.class, () -> ezShop.deleteCustomer(customerId));
		ezShop.login("admin","strong");
		
		assertThrows(InvalidCustomerIdException.class, () -> ezShop.deleteCustomer(0));
		assertThrows(InvalidCustomerIdException.class, () -> ezShop.deleteCustomer(-100));
		assertThrows(InvalidCustomerIdException.class, () -> ezShop.deleteCustomer(null));
		
		assertTrue(ezShop.deleteCustomer(customerId));
		assertFalse(ezShop.deleteCustomer(customerId));
		
		ezShop.logout();
	}
	
	@Test
	public void getCustomerTestCase() throws InvalidCustomerIdException, UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidCustomerNameException {
		db.resetDB("customers");
		
		ezShop.login("admin","strong");
		Integer customerId = ezShop.defineCustomer("customerName");
	    ezShop.logout();
	    
	    assertThrows(UnauthorizedException.class, () -> ezShop.getCustomer(customerId));
	    
	    ezShop.login("admin","strong");
	    
	    assertThrows(InvalidCustomerIdException.class, () -> ezShop.getCustomer(null));	    
	    
	    assertNull(ezShop.getCustomer(44));	    
	    assertNotNull(ezShop.getCustomer(customerId));
	    
	    ezShop.logout();
	}
	
	@Test
	public void getAllCustomersTestCase() throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidCustomerNameException {
		db.resetDB("customers");
		
		ezShop.login("admin","strong");
		ezShop.defineCustomer("customerName1");
		ezShop.defineCustomer("customerName2");
	    ezShop.logout();
	    
	    assertThrows(UnauthorizedException.class, () -> ezShop.getAllCustomers());
	    
	    ezShop.login("admin","strong");
	    assert(ezShop.getAllCustomers().size() == 2);
	    ezShop.logout();
	}
	
	@Test
	public void createCardTestCase() throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidCustomerNameException  {
		db.resetDB("cards");
		
		ezShop.login("admin","strong");
		ezShop.defineCustomer("customerName");		
	    ezShop.logout();
	    
	    assertThrows(UnauthorizedException.class, () -> ezShop.createCard());
	    
	    ezShop.login("admin","strong");
	    assert(ezShop.createCard().length() == 10);
	    ezShop.logout();
	}
	
	
	@Test
	public void attachCardToCustomerTestCase() throws InvalidCustomerIdException, InvalidCustomerCardException, UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidCustomerNameException {
		db.resetDB("cards");
		db.resetDB("customers");
		
		ezShop.login("admin","strong");
		Integer customerId = ezShop.defineCustomer("customerName");		
		String card = ezShop.createCard();
	    ezShop.logout();
	    
	    assertThrows(UnauthorizedException.class, () -> ezShop.attachCardToCustomer(card, customerId));
	    
	    ezShop.login("admin","strong");
	    
	    assertThrows(InvalidCustomerIdException.class, () -> ezShop.attachCardToCustomer(card, null));
	    assertThrows(InvalidCustomerIdException.class, () -> ezShop.attachCardToCustomer(card, -100));
	    
	    assertThrows(InvalidCustomerCardException.class, () -> ezShop.attachCardToCustomer("", customerId));
	    assertThrows(InvalidCustomerCardException.class, () -> ezShop.attachCardToCustomer(null, customerId));
	    assertThrows(InvalidCustomerCardException.class, () -> ezShop.attachCardToCustomer("123", customerId));
	    assertThrows(InvalidCustomerCardException.class, () -> ezShop.attachCardToCustomer("1234567890123", customerId));
	    
	    assertTrue(ezShop.attachCardToCustomer(card, customerId));
	    
	    ezShop.logout();
	}
	
	
	
	@Test
	public void modifyPointsOnCardTestCase() throws InvalidCustomerCardException, UnauthorizedException, InvalidUsernameException, InvalidPasswordException, InvalidCustomerNameException {
		db.resetDB("productTypes");
		db.resetDB("cards");
		
		ezShop.login("admin","strong");
		Integer customerId = ezShop.defineCustomer("customerName");		
		String card = ezShop.createCard();
	    ezShop.logout();
	    
	    assertThrows(UnauthorizedException.class, () -> ezShop.modifyPointsOnCard(card, customerId));
	    
	    ezShop.login("admin","strong");

	    assertThrows(InvalidCustomerCardException.class, () -> ezShop.modifyPointsOnCard("", 100));
	    assertThrows(InvalidCustomerCardException.class, () -> ezShop.modifyPointsOnCard(null, 100));
	    assertThrows(InvalidCustomerCardException.class, () -> ezShop.modifyPointsOnCard("123", 100));
	    assertThrows(InvalidCustomerCardException.class, () -> ezShop.modifyPointsOnCard("1234567890123", 100));
	    
	    assertFalse(ezShop.modifyPointsOnCard(card, -1 * 150));
	    
	    assertTrue(ezShop.modifyPointsOnCard(card, 750));	    
	   
	    assert(db.getCardPoints(card) == 750);
	    ezShop.logout();
	}
}
