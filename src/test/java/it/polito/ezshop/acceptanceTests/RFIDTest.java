package it.polito.ezshop.acceptanceTests;

import it.polito.ezshop.classes.*;
import it.polito.ezshop.data.BalanceOperation;
import it.polito.ezshop.data.EZShopInterface;
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
	    
	    String RFID = "0000002345";
	    
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
	    assertThrows(InvalidRFIDException.class, () -> ezShop.recordOrderArrivalRFID(orderId, "022234589"));  // len = 9
	    assertThrows(InvalidRFIDException.class, () -> ezShop.recordOrderArrivalRFID(orderId, "02223458922")); // len = 11
	    assertThrows(InvalidRFIDException.class, () -> ezShop.recordOrderArrivalRFID(orderId, "-222345892")); // negative
	    assertTrue(ezShop.recordOrderArrivalRFID(orderId, RFID));
	    
	    db.resetDB("balanceOperations");
	    ezShop.logout();
	   
	}
}
