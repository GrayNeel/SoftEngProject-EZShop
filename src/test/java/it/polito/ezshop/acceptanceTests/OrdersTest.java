package it.polito.ezshop.acceptanceTests;

import it.polito.ezshop.classes.*;
import it.polito.ezshop.data.BalanceOperation;
import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.junit.Test;

public class OrdersTest {
	EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
	EZShopDB db = new EZShopDB();
	
	@Test
	public void issueOrderTestCase() throws InvalidUsernameException, InvalidPasswordException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidQuantityException {
	    /**
	     * This method issues an order of <quantity> units of product with given <productCode>, each unit will be payed
	     * <pricePerUnit> to the supplier. <pricePerUnit> can differ from the re-selling price of the same product. The
	     * product might have no location assigned in this step.
	     * It can be invoked only after a user with role "Administrator" or "ShopManager" is logged in.
	     *
	     * @param productCode the code of the product that we should order as soon as possible
	     * @param quantity the quantity of product that we should order
	     * @param pricePerUnit the price to correspond to the supplier (!= than the resale price of the shop) per unit of
	     *                     product
	     *
	     * @return  the id of the order (> 0)
	     *          -1 if the product does not exists, if there are problems with the db
	     *
	     */
	    //public Integer issueOrder(String productCode, int quantity, double pricePerUnit)
	    //        throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException;
		
		db.resetDB("productTypes");
		db.resetDB("orders");
		ezShop.login("admin","strong");
		ezShop.createProductType("descriptionTest1", "12345670",2.50, "product note");
	    ezShop.logout();
	    
	    assertThrows(UnauthorizedException.class, () -> ezShop.issueOrder("12345670", 50, 2.50));
	    
	    ezShop.login("admin","strong");

	    assertThrows(InvalidPricePerUnitException.class, () -> ezShop.issueOrder("12345670", 50, -2.50));
	    assertThrows(InvalidPricePerUnitException.class, () -> ezShop.issueOrder("12345670", 50, 0.00));
	    
	    assertThrows(InvalidQuantityException.class, () -> ezShop.issueOrder("12345670", -50, 2.50));
	    assertThrows(InvalidQuantityException.class, () -> ezShop.issueOrder("12345670", 0, 2.50));
	    
	    assertThrows(InvalidProductCodeException.class, () -> ezShop.issueOrder("12345679", 50, 2.50));
	    assertThrows(InvalidProductCodeException.class, () -> ezShop.issueOrder("", 50, 2.50));
	    assertThrows(InvalidProductCodeException.class, () -> ezShop.issueOrder(null, 50, 2.50));
	    
	    assert(ezShop.issueOrder("12345670", 50, 2.50) == 1);
	    assert(ezShop.issueOrder("860004804109", 50, 2.50) == -1);
	 
	    ezShop.logout();
	}
	
	@Test
	public void payOrderForTestCase() throws InvalidUsernameException, InvalidPasswordException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidQuantityException {
		db.resetDB("productTypes");
		db.resetDB("orders");
		db.resetDB("balanceOperations");
		ezShop.login("admin","strong");
		ezShop.createProductType("descriptionTest1", "12345670",2.50, "product note");
		//Integer orderId = ezShop.issueOrder("12345670", 50, 2.50);
	    ezShop.logout();
	    
	    assertThrows(UnauthorizedException.class, () -> ezShop.payOrderFor("12345670", 50, 2.50));
	    
	    ezShop.login("admin","strong");

	    assertThrows(InvalidPricePerUnitException.class, () -> ezShop.payOrderFor("12345670", 50, -2.50));
	    assertThrows(InvalidPricePerUnitException.class, () -> ezShop.payOrderFor("12345670", 50, 0.00));
	   
	    assertThrows(InvalidQuantityException.class, () -> ezShop.payOrderFor("12345670", -50, 2.50));
	    assertThrows(InvalidQuantityException.class, () -> ezShop.payOrderFor("12345670", 0, 2.50));
	    
	    assertThrows(InvalidProductCodeException.class, () -> ezShop.payOrderFor("12345679", 50, 2.50));
	    assertThrows(InvalidProductCodeException.class, () -> ezShop.payOrderFor("", 50, 2.50));
	    assertThrows(InvalidProductCodeException.class, () -> ezShop.payOrderFor(null, 50, 2.50));
	 
	    assert(ezShop.payOrderFor("860004804109", 50, 2.50) == -1);
	    
	    //Balance is 0
	    assert(ezShop.payOrderFor("12345670", 50, 2.50) == -1);
	    
    	BalanceOperation balOp = new BalanceOperationClass(1,LocalDate.now(),50,"CREDIT"); 
    	db.recordBalanceOperation(balOp);
    	
    	//Balance is 50
    	assert(ezShop.payOrderFor("12345670", 50, 2.50) == -1);
    	
    	BalanceOperation balOp2 = new BalanceOperationClass(2,LocalDate.now(),150,"CREDIT"); 
    	db.recordBalanceOperation(balOp2);
    	
    	//balance is 200
    	assert(ezShop.payOrderFor("12345670", 50, 2.50) == 1);
	    
    	db.resetDB("balanceOperations");
	    ezShop.logout();
	}
	
	@Test
	public void payOrderTestCase() {
	    /**
	     * This method change the status the order with given <orderId> into the "PAYED" state. The order should be either
	     * issued (in this case the status changes) or payed (in this case the method has no effect).
	     * This method affects the balance of the system.
	     * It can be invoked only after a user with role "Administrator" or "ShopManager" is logged in.
	     *
	     * @param orderId the id of the order to be ORDERED
	     *
	     * @return  true if the order has been successfully ordered
	     *          false if the order does not exist or if it was not in an ISSUED/ORDERED state
	     *
	     * @throws InvalidOrderIdException if the order id is less than or equal to 0 or if it is null.
	     * @throws UnauthorizedException if there is no logged user or if it has not the rights to perform the operation
	     */
	    //public boolean payOrder(Integer orderId) throws InvalidOrderIdException, UnauthorizedException;
	}
	
	@Test
	public void recordOrderArrivalTestCase() {
	    /**
	     * This method records the arrival of an order with given <orderId>. This method changes the quantity of available product.
	     * The product type affected must have a location registered. The order should be either in the PAYED state (in this
	     * case the state will change to the COMPLETED one and the quantity of product type will be updated) or in the
	     * COMPLETED one (in this case this method will have no effect at all).
	     * It can be invoked only after a user with role "Administrator" or "ShopManager" is logged in.
	     *
	     * @param orderId the id of the order that has arrived
	     *
	     * @return  true if the operation was successful
	     *          false if the order does not exist or if it was not in an ORDERED/COMPLETED state
	     *
	     * @throws InvalidOrderIdException if the order id is less than or equal to 0 or if it is null.
	     * @throws InvalidLocationException if the ordered product type has not an assigned location.
	     * @throws UnauthorizedException if there is no logged user or if it has not the rights to perform the operation
	     */
	    //public boolean recordOrderArrival(Integer orderId) throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException;

	   
	}
	
	@Test
	public void getAllOrdersTestCase() {
		 /**
	     * This method return the list of all orders ISSUED, ORDERED and COMLPETED.
	     * It can be invoked only after a user with role "Administrator" or "ShopManager" is logged in.
	     *
	     * @return a list containing all orders
	     *
	     * @throws UnauthorizedException if there is no logged user or if it has not the rights to perform the operation
	     */
	    //public List<Order> getAllOrders() throws UnauthorizedException;
	}
}
