package it.polito.ezshop.data;

import it.polito.ezshop.classes.*;
import it.polito.ezshop.exceptions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class EZShop implements EZShopInterface {
	EZShopDB db = new EZShopDB();
	
	
	
    @Override
    public void reset() {

    }

    @Override
    public Integer createUser(String username, String password, String role) throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {
    	int lastid;
    	
    	//Check that username is not empty and it does not exist
    	if(username.length() == 0 || db.checkExistingUser(username)) {
    		throw new InvalidUsernameException("Invalid username");
    	}
    	
    	//Check that password is not empty
    	if(password.length() == 0) {
    		throw new InvalidPasswordException("Invalid password");
    	}
    	
    	//Check that role is one of the admitted values
    	if(role.length()==0 || role==null || (!role.equals("Cashier") && !role.equals("ShopManager") && !role.equals("Administrator")))
    		throw new InvalidRoleException("Invalid role");
    	
    	
    	//Get the last used ID
    	lastid = db.getLastId("users"); 
    	
    	//Create User Object with newID
    	UserClass user = new UserClass(lastid+1, username, password, role);
    	
    	//Add user to the DB
    	db.addUser(user);
    	
        return lastid+1;
    }

    @Override
    public boolean deleteUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
    	
    	if(id == null || id <= 0) {
    		throw new InvalidUserIdException("Invalid id");
    	}
    	
    	User user = db.getLoggedUser();
    	
    	if(user==null || !user.getRole().equals("Administrator")) {
    		throw new UnauthorizedException();
    	}    	
    
    	
    	boolean del = db.deleteUser(id);
    	
    	if(del == true)
    		System.out.println("User with id: " + id + "deleted");
    	else
    		System.out.println("User with id: " + id + "NOT deleted");
        return del;
    }    
    

    @Override
    public List<User> getAllUsers() throws UnauthorizedException {
    	User user = db.getLoggedUser();
    	
    	if(user==null || !user.getRole().equals("Administrator")) {
    		throw new UnauthorizedException();
    		
    	}
    	
    	
        return db.getAllUsers();
    }

    @Override
    public User getUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
    	if(id<=0 || id==null) {
    		throw new InvalidUserIdException();
    	}
    	
    	User user = db.getLoggedUser();
    	
    	if(user==null || !user.getRole().equals("Administrator")) {
    		throw new UnauthorizedException();
    	}
    	
        return db.getUserById(id);
    }

    @Override
    public boolean updateUserRights(Integer id, String role) throws InvalidUserIdException, InvalidRoleException, UnauthorizedException {
    	if(id<=0 || id==null) {
    		throw new InvalidUserIdException();
    	}
    	
    	if(role.length()==0 || role==null || (!role.equals("Cashier") && !role.equals("ShopManager") && !role.equals("Administrator")))
    		throw new InvalidRoleException();
    	
    	User user = db.getLoggedUser();
    	
    	if(user==null || !user.getRole().equals("Administrator")) {
    		throw new UnauthorizedException();
    	}
    	
    	if(db.updateUserRole(id,role))
    		return true;
    	else
    		return false;
    }

    @Override
    public User login(String username, String password) throws InvalidUsernameException, InvalidPasswordException {
    	if(username.length()==0 || username==null) {
    		throw new InvalidUsernameException();
    	}
    	
    	if(password.length()==0 || password==null) {
    		throw new InvalidPasswordException();
    	}
    	
    	User user = db.getUserByCredentials(username,password);
        
    	//If user is null or DB problems
    	if(user == null || !db.loginUser(user))
    		return null;
    	
    	return user;
    }

    @Override
    public boolean logout() {
        return db.logoutUser();
    }

    @Override
    public Integer createProductType(String description, String productCode, double pricePerUnit, String note) throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
    	User user = db.getLoggedUser();
    	Integer barCode = null;
    	
    	//Verifying that the string is a number
    	try {
    		barCode = Integer.parseInt(productCode);
    	}catch(NumberFormatException e) {
    		throw new InvalidProductCodeException();    		
    	}
    	
    	if(user==null || (!user.getRole().equals("Administrator") && !user.getRole().equals("ShopManager"))) {
    		throw new UnauthorizedException();    	
    	}
    	
    	if(description.length()==0 || description == null) {
    		throw new InvalidProductDescriptionException();    		
    	}
    	
    	if(productCode == null || productCode.length() == 0 || db.checkExistingProductType(productCode)) {
    		throw new InvalidProductCodeException();    		
    	}
    	//TODO: check barcode validity (https://www.gs1.org/services/how-calculate-check-digit-manually)
    	if(pricePerUnit <= 0) {
    		throw new InvalidPricePerUnitException();    		
    	} 
    	
    	//Get the last used ID
    	int lastid = db.getLastId("productTypes");
    	
    	//Create productType Object with newID
    	//(Integer id, Integer quantity, String location, String note, String productDescription, String barCode, Double pricePerUnit)
    	ProductTypeClass productType = new ProductTypeClass(lastid+1, 0, "location", note, description, productCode, pricePerUnit);
    	
    	//Add productType to the DB
    	db.addProductType(productType);
    	
        return lastid+1;  
    }

    @Override
    public boolean updateProduct(Integer id, String newDescription, String newCode, double newPrice, String newNote) throws InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
    	/*   
         * @throws InvalidProductCodeException if the product code is null or empty, if it is not a number or if it is not a valid barcode  
         */   	
    	
    	
    	if(id<=0 || id==null) {
    		throw new InvalidProductIdException();
    	}  
    	
    	if(newDescription==null || newDescription.length() == 0) {
    		throw new InvalidProductDescriptionException();
    	}
    	
    	if(newCode == null || newCode.length() == 0  || db.checkExistingProductType(newCode)) {
    		throw new InvalidProductCodeException();    		
    	} 	  
    	//TODO: check barcode validity (https://www.gs1.org/services/how-calculate-check-digit-manually)
    	
    	if(newPrice <= 0) {
    		throw new InvalidPricePerUnitException();    		
    	} 	
    	
    	
    	User user = db.getLoggedUser();
    	
    	if(user==null || (!user.getRole().equals("Administrator") && !user.getRole().equals("ShopManager"))) {
    		throw new UnauthorizedException();
    	}
    	
    	if(db.updateProductType(id, newDescription, newCode, newPrice, newNote))
    		return true;
    	else
    		return false;
    }

    @Override
    public boolean deleteProductType(Integer id) throws InvalidProductIdException, UnauthorizedException {
    	
    	if(id == null || id <= 0) {
    		throw new InvalidProductIdException("Invalid Product id");
    	}
    	
    	User user = db.getLoggedUser();
    	
    	if(user==null || (!user.getRole().equals("Administrator") && !user.getRole().equals("ShopManager"))) {
    		throw new UnauthorizedException();
    	}    	
    
    	
    	boolean del = db.deleteProductType(id);
    	
    	if(del == true)
    		System.out.println("Product with id: " + id + "deleted");
    	else
    		System.out.println("Product with id: " + id + "NOT deleted");
        return del;
    }

    @Override
    public List<ProductType> getAllProductTypes() throws UnauthorizedException {    	
    	User user = db.getLoggedUser();
    	
    	if(user==null || !user.getRole().equals("Administrator") != !user.getRole().equals("ShopManager") != !user.getRole().equals("Cashier"))
    		throw new UnauthorizedException(); 
    	
    	return db.getAllProductTypes();
    }

    @Override
    public ProductType getProductTypeByBarCode(String barCode) throws InvalidProductCodeException, UnauthorizedException {
    	 /**
         * This method returns a product type with given barcode. It can be invoked only after a user with role "Administrator"
         * or "ShopManager" is logged in.
         *
         * @param barCode the unique barCode of a product
         *
         * @return the product type with given barCode if present, null otherwise
         *
         * @throws InvalidProductCodeException if barCode is not a valid bar code, if is it empty or if it is null
       
         */
    	
    	if(barCode == null || barCode.length() == 0  || !db.checkExistingProductType(barCode)) { //il checkExist ci vuole davvero?
    		throw new InvalidProductCodeException();    		
    	} 	  
    	//TODO: check barcode validity (https://www.gs1.org/services/how-calculate-check-digit-manually)
    	
    	User user = db.getLoggedUser();
    	
    	if(user==null || (!user.getRole().equals("Administrator") && !user.getRole().equals("ShopManager"))) {
    		throw new UnauthorizedException();
    	}        	
    	
    	 return db.getProductTypeByBarCode(barCode);
    }

    @Override
    public List<ProductType> getProductTypesByDescription(String description) throws UnauthorizedException {  	
    	
    	if(description == null) 
    		description = "";    	
    	
    	User user = db.getLoggedUser();
    	
    	if(user==null || (!user.getRole().equals("Administrator") && !user.getRole().equals("ShopManager"))) {
    		throw new UnauthorizedException();
    	} 
    	    	
        return db.getProductTypesByDescription(description);
    }

    @Override
    public boolean updateQuantity(Integer productId, int toBeAdded) throws InvalidProductIdException, UnauthorizedException {
    	
    	if(productId == null || productId <= 0) {
    		throw new InvalidProductIdException("Invalid Product id");
    	}
    	
    	User user = db.getLoggedUser();
    	
    	if(user==null || (!user.getRole().equals("Administrator") && !user.getRole().equals("ShopManager"))) {
    		throw new UnauthorizedException();
    	} 
    	
    	if(db.updateQuantity(productId, toBeAdded))
    		return true;
    	else
    		return false;  	      
    }

    @Override
    public boolean updatePosition(Integer productId, String newPos) throws InvalidProductIdException, InvalidLocationException, UnauthorizedException {
    	
    	if(productId == null || productId <= 0) {
    		throw new InvalidProductIdException("Invalid Product id");
    	}    	
    	
    	Pattern p = Pattern.compile("\\d+-\\d+-\\d+");
    	Matcher m = p.matcher(newPos);    	
    	 if (!m.matches()) {
    		 throw new InvalidLocationException("Invalid Location format");
    	 }   	
    	    	
    	User user = db.getLoggedUser();
    	
    	if(user==null || (!user.getRole().equals("Administrator") && !user.getRole().equals("ShopManager"))) {
    		throw new UnauthorizedException();
    	} 
    	return db.updateLocation(productId, newPos);
    }

    @Override
    public Integer issueOrder(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
    	
    	if(productCode == null || productCode.length() == 0) { //TO DO -> not valid barcode
    		throw new InvalidProductCodeException("Invalid Product code");
    	}
    	
    	if(db.getProductTypeByBarCode(productCode) == null)
    		return -1;
    	
    	if(quantity <= 0)
    		throw new InvalidQuantityException("Quantity can not be less than 0");
    	
    	if(pricePerUnit <= 0)
    		throw new InvalidPricePerUnitException("Price per unit can not be less than 0");
    	
    	User user = db.getLoggedUser();
    	
    	if(user==null || (!user.getRole().equals("Administrator") && !user.getRole().equals("ShopManager"))) {
    		throw new UnauthorizedException();
    	} 
    	
    	//Get the last used ID
    	Integer lastid = db.getLastId("orders"); 
    	
    	//Create Order Object with newOrderID
    	OrderClass order = new OrderClass(0, productCode, pricePerUnit, quantity, "ISSUED", lastid+1); //balanceID??**********************************
    	
    	//Add Order to the DB    	
    	if(!db.issueOrder(order))
    		return -1;
    	
        return lastid+1;
    }

    @Override
    public Integer payOrderFor(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
    	/**
        
         * This method affects the balance of the system.
         * It can be invoked only after a user with role "Administrator" or "ShopManager" is logged in.
                 *
         * @return  the id of the order (> 0)
         *          -1 if the product does not exists, if the balance is not enough to satisfy the order, if there are some ////TO DOO!! The balance
         *          problems with the db
         *      
         */
    	if(productCode == null || productCode.length() == 0) { //TO DO -> not valid barcode
    		throw new InvalidProductCodeException("Invalid Product code");
    	}
    	
    	if(db.getProductTypeByBarCode(productCode) == null) 
    		return -1;
    	
    	if(quantity <= 0)
    		throw new InvalidQuantityException("Quantity can not be less than 0");
    	
    	if(pricePerUnit <= 0)
    		throw new InvalidPricePerUnitException("Price per unit can not be less than 0");
    	
    	User user = db.getLoggedUser();
    	
    	if(user==null || (!user.getRole().equals("Administrator") && !user.getRole().equals("ShopManager"))) {
    		throw new UnauthorizedException();
    	} 
    	
    	//Get the last used ID
    	Integer lastid = db.getLastId("orders"); 
    	
    	//Create Order Object with newID
    	OrderClass order = new OrderClass(lastid+1, productCode, pricePerUnit, quantity, "PAYED", lastid+1); //balanceID??**********************************
    	
    	//Add Order to the DB    	
    	if(!db.issueAndPayOrder(order))
    		return -1;
    	
        return lastid+1;
    }

    @Override
    public boolean payOrder(Integer orderId) throws InvalidOrderIdException, UnauthorizedException {
    	/**
         
         * This method affects the balance of the system.
                
         */
    	if(orderId == null || orderId <= 0)
    		throw new InvalidOrderIdException("Order ID can not be less than 0");
    	
    	User user = db.getLoggedUser();
    	
    	if(user==null || (!user.getRole().equals("Administrator") && !user.getRole().equals("ShopManager"))) {
    		throw new UnauthorizedException();
    	}     	
    	
    	return db.payOrder(orderId);
    }

    @Override
    public boolean recordOrderArrival(Integer orderId) throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException {
    	    	
    	if(orderId == null || orderId <= 0)
    		throw new InvalidOrderIdException("Order ID can not be less than 0");
    	
    	User user = db.getLoggedUser();
    	
    	if(user==null || (!user.getRole().equals("Administrator") && !user.getRole().equals("ShopManager"))) {
    		throw new UnauthorizedException();
    	} 
    	
    	Order order = null;
    	List<Order> orderList = getAllOrders();
    	for(Order o : orderList)    	
    		if(o.getOrderId() == orderId)
    			order = o;
    	
    	ProductType prod = null;
    	List<ProductType> productList = getAllProductTypes();
    	
    	for(ProductType p : productList)    	
    		if(p.getBarCode()== order.getProductCode())
    			prod = p;
    	
    	if(prod.getLocation().length() == 0)
    		throw new InvalidLocationException("Product type of the ordered product has not an assigned location");
    		    	
    	return db.recordOrderArrival(orderId, prod.getBarCode(), prod.getQuantity() + order.getQuantity());
    }

    @Override
    public List<Order> getAllOrders() throws UnauthorizedException {
    	User user = db.getLoggedUser();
    	
    	if(user==null || (!user.getRole().equals("Administrator") && !user.getRole().equals("ShopManager"))) {
    		throw new UnauthorizedException();
    	} 
    	
    	return db.getAllOrders();
    }

    /// User case 4+
    
    @Override
    public Integer defineCustomer(String customerName) throws InvalidCustomerNameException, UnauthorizedException {   	
    	User user = db.getLoggedUser();
    	
    	if(user==null || (!user.getRole().equals("Administrator") && !user.getRole().equals("ShopManager") && !user.getRole().equals("Cashier"))) {
    		throw new UnauthorizedException();
    	}
    	   	
    	if(customerName.length() == 0)
    		throw new InvalidCustomerNameException();
    	
    	int lastid = db.getLastId("customers");
    	CustomerClass customer = new CustomerClass(lastid+1, customerName, "", 0);
    	boolean flag = db.defineCustomer(customer);
    	if(flag) {
    		return lastid+1;
    	}
    	else {
    		return -1;
    	}
    	
        
    }

    @Override
    public boolean modifyCustomer(Integer id, String newCustomerName, String newCustomerCard) throws InvalidCustomerNameException, InvalidCustomerCardException, InvalidCustomerIdException, UnauthorizedException {
    	/**
         * This method updates the data of a customer with given <id>. This method can be used to assign/delete a card to a
         * customer. If <newCustomerCard> has a numeric value than this value will be assigned as new card code, if it is an
         * empty string then any existing card code connected to the customer will be removed and, finally, it it assumes the
         * null value then the card code related to the customer should not be affected from the update. The card code should
         * be unique and should be a string of 10 digits.
         * It can be invoked only after a user with role "Administrator", "ShopManager" or "Cashier" is logged in.
         *
         * @param id the id of the customer to be updated
         * @param newCustomerName the new name to be assigned
         * @param newCustomerCard the new card code to be assigned. If it is empty it means that the card must be deleted,
         *                        if it is null then we don't want to update the cardNumber
         *
         * @return true if the update is successful
         *          false if the update fails ( cardCode assigned to another user, db unreacheable)
         *
         * @throws InvalidCustomerNameException if the customer name is empty or null
         * @throws InvalidCustomerCardException if the customer card is empty, null or if it is not in a valid format (string with 10 digits)
         * @throws UnauthorizedException if there is no logged user or if it has not the rights to perform the operation
         */
    	User user = db.getLoggedUser();
    	
    	if(user==null || (!user.getRole().equals("Administrator") && !user.getRole().equals("ShopManager") && !user.getRole().equals("Cashier"))) {
    		throw new UnauthorizedException();
    	}
    	
    	if(newCustomerName==null || newCustomerName=="") {
    		throw new InvalidCustomerNameException();
    	}
    	
    	if(newCustomerCard.length()!=10) {
    		throw new InvalidCustomerCardException();
    	}
    	
    	boolean flag = db.updateCustomer(id, newCustomerName, newCustomerCard);
    	
    	return flag;
    }

    @Override
    public boolean deleteCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
    	User user = db.getLoggedUser();
    	
    	if(user==null || (!user.getRole().equals("Administrator") && !user.getRole().equals("ShopManager") && !user.getRole().equals("Cashier"))) {
    		throw new UnauthorizedException();
    	}
    	
    	if(id==null || id<=0) {
    		throw new InvalidCustomerIdException();
    	}
    	
    	boolean flag = db.deleteCustomer(id);
    	return flag;
    }

    @Override
    public Customer getCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
    	Customer customer;
    	User user = db.getLoggedUser();
    	
    	if(user==null || (!user.getRole().equals("Administrator") && !user.getRole().equals("ShopManager") && !user.getRole().equals("Cashier"))) {
    		throw new UnauthorizedException();
    	}
    	
    	if(id==null) {
    		throw new InvalidCustomerIdException();
    	}
    	
    	customer = db.getCustomerById(id);
    	return customer;
    	
    }

    @Override
    public List<Customer> getAllCustomers() throws UnauthorizedException {
    	User user = db.getLoggedUser();
    	
    	if(user==null || (!user.getRole().equals("Administrator") && !user.getRole().equals("ShopManager") && !user.getRole().equals("Cashier"))) {
    		throw new UnauthorizedException();
    	}
    	List<Customer> customerlist = new ArrayList<>();
    	customerlist = db.getAllCustomers();
        return customerlist;
    }

    @Override
    public String createCard() throws UnauthorizedException {
    	User user = db.getLoggedUser();
    	
    	if(user==null || (!user.getRole().equals("Administrator") && !user.getRole().equals("ShopManager") && !user.getRole().equals("Cashier"))) {
    		throw new UnauthorizedException();
    	}
    	Integer lastid = db.getLastId("cards");
    	String cardId = lastid.toString();
    	String filler = "";
    	for(Integer i=0; i<(10-cardId.length()); i++) {
    		filler += "0";
    	}
    	cardId = filler + cardId;
    	boolean flag = db.createCard(cardId);
    	
    	if(flag) {
    		return cardId;
    	}
    	else {
    		return "";
    	}
    }

    @Override
    public boolean attachCardToCustomer(String customerCard, Integer customerId) throws InvalidCustomerIdException, InvalidCustomerCardException, UnauthorizedException {
    	User user = db.getLoggedUser();
    	
    	if(user==null || (!user.getRole().equals("Administrator") && !user.getRole().equals("ShopManager") && !user.getRole().equals("Cashier"))) {
    		throw new UnauthorizedException();
    	}
    	
    	if(customerId==null || customerId<=0) {
    		throw new InvalidCustomerIdException();
    	}
    	
    	if(customerCard=="" || customerCard==null || customerCard.length()!=10) {
    		throw new InvalidCustomerCardException();
    	}
    	
    	boolean flag = db.attachCardToCustomer(customerCard, customerId);
    	/**
         * This method assigns a card with given card code to a customer with given identifier. A card with given card code
         * can be assigned to one customer only.
         * It can be invoked only after a user with role "Administrator", "ShopManager" or "Cashier" is logged in.
         *
         * @param customerCard the number of the card to be attached to a customer
         * @param customerId the id of the customer the card should be assigned to
         *
         * @return true if the operation was successful
         *          false if the card is already assigned to another user, if there is no customer with given id, if the db is unreachable
         *
         * @throws InvalidCustomerIdException if the id is null, less than or equal to 0.
         * @throws InvalidCustomerCardException if the card is null, empty or in an invalid format
         * @throws UnauthorizedException if there is no logged user or if it has not the rights to perform the operation
         */
    	return flag;
    }

    @Override
    public boolean modifyPointsOnCard(String customerCard, int pointsToBeAdded) throws InvalidCustomerCardException, UnauthorizedException {
    	User user = db.getLoggedUser();
    	
    	if(user==null || (!user.getRole().equals("Administrator") && !user.getRole().equals("ShopManager") && !user.getRole().equals("Cashier"))) {
    		throw new UnauthorizedException();
    	}
    	
    	if(customerCard=="" || customerCard==null || customerCard.length()!=10) {
    		throw new InvalidCustomerCardException();
    	}
    	
    	
    	/**
         * This method updates the points on a card adding to the number of points available on the card the value assumed by
         * <pointsToBeAdded>. The points on a card should always be greater than or equal to 0.
         * It can be invoked only after a user with role "Administrator", "ShopManager" or "Cashier" is logged in.
         *
         * @param customerCard the card the points should be added to
         * @param pointsToBeAdded the points to be added or subtracted ( this could assume a negative value)
         *
         * @return true if the operation is successful
         *          false   if there is no card with given code,
         *                  if pointsToBeAdded is negative and there were not enough points on that card before this operation,
         *                  if we cannot reach the db.
         *
         * @throws InvalidCustomerCardException if the card is null, empty or in an invalid format
         * @throws UnauthorizedException if there is no logged user or if it has not the rights to perform the operation
         */
    	return false;
    }

    @Override
    public Integer startSaleTransaction() throws UnauthorizedException {
    	User user = db.getLoggedUser();
    	
    	if(user==null || (!user.getRole().equals("Administrator") && !user.getRole().equals("ShopManager") && !user.getRole().equals("Cashier"))) {
    		throw new UnauthorizedException();
    	}
    	// -------------------- FR6 ------------------- //
        // ------------------- ADMIN ------------------ //
        // --------------- SHOP MANAGER --------------- //
        // ------------------ CASHIER ----------------- //


        /**
         * This method starts a new sale transaction and returns its unique identifier.
         * It can be invoked only after a user with role "Administrator", "ShopManager" or "Cashier" is logged in.
         *
         * @return the id of the transaction (greater than or equal to 0)
         */
        return null;
    }

    @Override
    public boolean addProductToSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
    	User user = db.getLoggedUser();
    	
    	if(user==null || (!user.getRole().equals("Administrator") && !user.getRole().equals("ShopManager") && !user.getRole().equals("Cashier"))) {
    		throw new UnauthorizedException();
    	}
    	/**
         * This method adds a product to a sale transaction decreasing the temporary amount of product available on the
         * shelves for other customers.
         * It can be invoked only after a user with role "Administrator", "ShopManager" or "Cashier" is logged in.
         *
         * @param transactionId the id of the Sale transaction
         * @param productCode the barcode of the product to be added
         * @param amount the quantity of product to be added
         * @return  true if the operation is successful
         *          false   if the product code does not exist,
         *                  if the quantity of product cannot satisfy the request,
         *                  if the transaction id does not identify a started and open transaction.
         *
         * @throws InvalidTransactionIdException if the transaction id less than or equal to 0 or if it is null
         * @throws InvalidProductCodeException if the product code is empty, null or invalid
         * @throws InvalidQuantityException if the quantity is less than 0
         * @throws UnauthorizedException if there is no logged user or if it has not the rights to perform the operation
         */
    	return false;
    }

    @Override
    public boolean deleteProductFromSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
    	User user = db.getLoggedUser();
    	
    	if(user==null || (!user.getRole().equals("Administrator") && !user.getRole().equals("ShopManager") && !user.getRole().equals("Cashier"))) {
    		throw new UnauthorizedException();
    	}
    	/**
         * This method deletes a product from a sale transaction increasing the temporary amount of product available on the
         * shelves for other customers.
         * It can be invoked only after a user with role "Administrator", "ShopManager" or "Cashier" is logged in.
         *
         * @param transactionId the id of the Sale transaction
         * @param productCode the barcode of the product to be deleted
         * @param amount the quantity of product to be deleted
         *
         * @return  true if the operation is successful
         *          false   if the product code does not exist,
         *                  if the quantity of product cannot satisfy the request,
         *                  if the transaction id does not identify a started and open transaction.
         *
         * @throws InvalidTransactionIdException if the transaction id less than or equal to 0 or if it is null
         * @throws InvalidProductCodeException if the product code is empty, null or invalid
         * @throws InvalidQuantityException if the quantity is less than 0
         * @throws UnauthorizedException if there is no logged user or if it has not the rights to perform the operation
         */
    	return false;
    }

    @Override
    public boolean applyDiscountRateToProduct(Integer transactionId, String productCode, double discountRate) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidDiscountRateException, UnauthorizedException {
    	User user = db.getLoggedUser();
    	
    	if(user==null || (!user.getRole().equals("Administrator") && !user.getRole().equals("ShopManager") && !user.getRole().equals("Cashier"))) {
    		throw new UnauthorizedException();
    	}
    	/**
         * This method applies a discount rate to all units of a product type with given type in a sale transaction. The
         * discount rate should be greater than or equal to 0 and less than 1.
         * The sale transaction should be started and open.
         * It can be invoked only after a user with role "Administrator", "ShopManager" or "Cashier" is logged in.
         *
         * @param transactionId the id of the Sale transaction
         * @param productCode the barcode of the product to be discounted
         * @param discountRate the discount rate of the product
         *
         * @return  true if the operation is successful
         *          false   if the product code does not exist,
         *                  if the transaction id does not identify a started and open transaction.
         *
         * @throws InvalidTransactionIdException if the transaction id less than or equal to 0 or if it is null
         * @throws InvalidProductCodeException if the product code is empty, null or invalid
         * @throws InvalidDiscountRateException if the discount rate is less than 0 or if it greater than or equal to 1.00
         * @throws UnauthorizedException if there is no logged user or if it has not the rights to perform the operation
         */
    	return false;
    }

    @Override
    public boolean applyDiscountRateToSale(Integer transactionId, double discountRate) throws InvalidTransactionIdException, InvalidDiscountRateException, UnauthorizedException {
    	User user = db.getLoggedUser();
    	
    	if(user==null || (!user.getRole().equals("Administrator") && !user.getRole().equals("ShopManager") && !user.getRole().equals("Cashier"))) {
    		throw new UnauthorizedException();
    	}
    	/**
         * This method applies a discount rate to the whole sale transaction.
         * The discount rate should be greater than or equal to 0 and less than 1.
         * The sale transaction can be either started or closed but not already payed.
         * It can be invoked only after a user with role "Administrator", "ShopManager" or "Cashier" is logged in.
         *
         * @param transactionId the id of the Sale transaction
         * @param discountRate the discount rate of the sale
         *
         * @return  true if the operation is successful
         *          false if the transaction does not exists
         *
         * @throws InvalidTransactionIdException if the transaction id less than or equal to 0 or if it is null
         * @throws InvalidDiscountRateException if the discount rate is less than 0 or if it greater than or equal to 1.00
         * @throws UnauthorizedException if there is no logged user or if it has not the rights to perform the operation
         */
    	return false;
    }

    @Override
    public int computePointsForSale(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
    	User user = db.getLoggedUser();
    	
    	if(user==null || (!user.getRole().equals("Administrator") && !user.getRole().equals("ShopManager") && !user.getRole().equals("Cashier"))) {
    		throw new UnauthorizedException();
    	}
    	/**
         * This method returns the number of points granted by a specific sale transaction.
         * Every 10€ the number of points is increased by 1 (i.e. 19.99€ returns 1 point, 20.00€ returns 2 points).
         * If the transaction with given id does not exist then the number of points returned should be -1.
         * The transaction may be in any state (open, closed, payed).
         * It can be invoked only after a user with role "Administrator", "ShopManager" or "Cashier" is logged in.
         *
         * @param transactionId the id of the Sale transaction
         *
         * @return the points of the sale (1 point for each 10€) or -1 if the transaction does not exists
         *
         * @throws InvalidTransactionIdException if the transaction id less than or equal to 0 or if it is null
         * @throws UnauthorizedException if there is no logged user or if it has not the rights to perform the operation
         */
    	return 0;
    }

    @Override
    public boolean endSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
    	User user = db.getLoggedUser();
    	
    	if(user==null || (!user.getRole().equals("Administrator") && !user.getRole().equals("ShopManager") && !user.getRole().equals("Cashier"))) {
    		throw new UnauthorizedException();
    	}
    	/**
         * This method closes an opened transaction. After this operation the
         * transaction is persisted in the system's memory.
         * It can be invoked only after a user with role "Administrator", "ShopManager" or "Cashier" is logged in.
         *
         * @param transactionId the id of the Sale transaction
         *
         * @return  true    if the transaction was successfully closed
         *          false   if the transaction does not exist,
         *                  if it has already been closed,
         *                  if there was a problem in registering the data
         *
         * @throws InvalidTransactionIdException if the transaction id less than or equal to 0 or if it is null
         * @throws UnauthorizedException if there is no logged user or if it has not the rights to perform the operation
         */
    	return false;
    }

    //////////////////////////////////////////////////////////////////// Marco ^ , Pablo v
    
    @Override
    public boolean deleteSaleTransaction(Integer saleNumber) throws InvalidTransactionIdException, UnauthorizedException {
        return false;
    }

    @Override
    public SaleTransaction getSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        return null;
    }

    @Override
    public Integer startReturnTransaction(Integer saleNumber) throws /*InvalidTicketNumberException,*/InvalidTransactionIdException, UnauthorizedException {
        return null;
    }

    @Override
    public boolean returnProduct(Integer returnId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean endReturnTransaction(Integer returnId, boolean commit) throws InvalidTransactionIdException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean deleteReturnTransaction(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {
        return false;
    }

    @Override
    public double receiveCashPayment(Integer ticketNumber, double cash) throws InvalidTransactionIdException, InvalidPaymentException, UnauthorizedException {
        return 0;
    }

    @Override
    public boolean receiveCreditCardPayment(Integer ticketNumber, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {
        return false;
    }

    @Override
    public double returnCashPayment(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {
        return 0;
    }

    @Override
    public double returnCreditCardPayment(Integer returnId, String creditCard) throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {
        return 0;
    }

    @Override
    public boolean recordBalanceUpdate(double toBeAdded) throws UnauthorizedException {
        return false;
    }

    @Override
    public List<BalanceOperation> getCreditsAndDebits(LocalDate from, LocalDate to) throws UnauthorizedException {
    	//TEMP: REMOVE IT!! IT IS JUST FOR TESTING
    	List<BalanceOperation> bolist = new ArrayList<>();
        return bolist;
    }

    @Override
    public double computeBalance() throws UnauthorizedException {
        return 0;
    }
}
