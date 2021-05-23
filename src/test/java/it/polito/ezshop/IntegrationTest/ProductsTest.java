package it.polito.ezshop.IntegrationTest;

import it.polito.ezshop.classes.*;

import it.polito.ezshop.data.EZShop;
import it.polito.ezshop.data.EZShopInterface;
import it.polito.ezshop.exceptions.*;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;

public class ProductsTest {
	EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
	EZShopDB db = new EZShopDB();
	
	@Test
	public void createProductTypeTestCase() throws InvalidUsernameException, InvalidPasswordException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
		db.resetDB("productTypes");
		
		assertThrows(UnauthorizedException.class, () -> ezShop.createProductType("description", "12345670",5.23, "product note"));

		//Given as tested
		ezShop.login("admin","strong");
		
		assertThrows(InvalidProductDescriptionException.class, () -> ezShop.createProductType("", "12345670",5.23, "product note"));
		assertThrows(InvalidProductDescriptionException.class, () -> ezShop.createProductType(null, "12345670",5.23, "product note"));
		
		assertThrows(InvalidProductCodeException.class, () -> ezShop.createProductType("description", null,5.23, "product note"));
		assertThrows(InvalidProductCodeException.class, () -> ezShop.createProductType("description", "",5.23, "product note"));
		assertThrows(InvalidProductCodeException.class, () -> ezShop.createProductType("description", "12345679",5.23, "product note"));
		
		assertThrows(InvalidPricePerUnitException.class, () -> ezShop.createProductType("description", "12345670",-1.50, "product note"));
		assertThrows(InvalidPricePerUnitException.class, () -> ezShop.createProductType("description", "12345670",0.00, "product note"));
		
		Integer productId = ezShop.createProductType("descriptionTest", "12345670",2.50, "product note");
		assert( productId > 0);
		
		assert(ezShop.createProductType("sameBarCodeTest", "12345670",2.70, "product note") == -1);
		
		ezShop.logout();
		
	}
	
	@Test
	public void updateProductTestCase() throws InvalidUsernameException, InvalidPasswordException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException, InvalidProductIdException {
		db.resetDB("productTypes");

		assertThrows(UnauthorizedException.class, () -> ezShop.updateProduct(11,"New description", "860004804109",8.88, "newNote"));
		ezShop.login("admin","strong");
		
		Integer productId = ezShop.createProductType("descriptionTest", "12345670",2.50, "product note");
		
		assertThrows(InvalidProductIdException.class, () -> ezShop.updateProduct(-11,"New description", "860004804109",8.88, "newNote"));
		assertThrows(InvalidProductIdException.class, () -> ezShop.updateProduct(0,"New description", "860004804109",8.88, "newNote"));
		assertThrows(InvalidProductIdException.class, () -> ezShop.updateProduct(null,"New description", "860004804109",8.88, "newNote"));
		
		assertThrows(InvalidProductDescriptionException.class, () -> ezShop.updateProduct(productId,"", "860004804109",8.88, "newNote"));
		assertThrows(InvalidProductDescriptionException.class, () -> ezShop.updateProduct(productId,null, "860004804109",8.88, "newNote"));
		
		assertThrows(InvalidProductCodeException.class, () -> ezShop.updateProduct(productId,"New description", "",8.88, "newNote"));
		assertThrows(InvalidProductCodeException.class, () -> ezShop.updateProduct(productId,"New description", null,8.88, "newNote"));
		assertThrows(InvalidProductCodeException.class, () -> ezShop.updateProduct(productId,"New description", "12345679",8.88, "newNote"));
		assertThrows(InvalidProductCodeException.class, () -> ezShop.updateProduct(productId,"New description", "NaN",8.88, "newNote"));
		
		assertThrows(InvalidPricePerUnitException.class, () -> ezShop.updateProduct(11,"New description", "860004804109",-8.88, "newNote"));
		assertThrows(InvalidPricePerUnitException.class, () -> ezShop.updateProduct(11,"New description", "860004804109",0.00, "newNote"));
		
		assertTrue(ezShop.updateProduct(productId,"New description", "860004804109",8.88, "newNote"));
		assertFalse(ezShop.updateProduct(9999, "new desc", "12345670", 8.88, "new"));
		
		ezShop.createProductType("descriptionTest", "12345670",2.50, "product note");
		
		assertFalse(ezShop.updateProduct(productId,"New description", "12345670",8.88, "newNote"));
	
		ezShop.logout();
	}
}
