package it.polito.ezshop.data;

import java.util.Date;

import it.polito.ezshop.classes.*;
import it.polito.ezshop.exceptions.*;

//import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EZShop implements EZShopInterface {
	EZShopDB db = new EZShopDB();
	User loggedUser = null;
	// List<TicketEntry> ticket = ArrayList<>();
	// SaleTransactionClass transaction = null;
	Map<Integer,List<TicketEntry>> tickets = new HashMap<>();
	List<ProductReturnClass> returns = new ArrayList<>();
	
	
	
    @Override
    public void reset() {
    	//TODO: reset all the application (delete entries in DB and reset local variables)
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
    	
    	
    	//Get the last used ID from users table
    	lastid = db.getLastId("users"); 
    	
    	//Create User Object with newID
    	User user = new UserClass(lastid+1, username, password, role);
    	
    	//Add user to the DB
    	db.addUser(user);
    	
        return lastid+1;
    }

    @Override
    public boolean deleteUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
    	
    	if(id == null || id <= 0) {
    		throw new InvalidUserIdException("Invalid id");
    	}
    	
    	User user = this.loggedUser;
    	
    	if(user==null || !user.getRole().equals("Administrator")) {
    		throw new UnauthorizedException();
    	}    	    
   
        return db.deleteUser(id);
    }    
    

    @Override
    public List<User> getAllUsers() throws UnauthorizedException {
    	User user = this.loggedUser;
    	
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
    	
    	User user = this.loggedUser;
    	
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
    	
    	User user = this.loggedUser;
    	
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
        
    	//If user is null
    	if(user == null)
    		return null;
    	
    	//Login user
    	loggedUser = user;
    	
    	return user;
    }

    @Override
    public boolean logout() {
    	if(loggedUser != null) {
    		loggedUser = null;
    		return true;
    	} else
    		return false;
    }

    @Override
    public Integer createProductType(String description, String productCode, double pricePerUnit, String note) throws InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
    	User user = this.loggedUser;
    	
    	if(user==null || (!user.getRole().equals("Administrator") && !user.getRole().equals("ShopManager"))) {
    		throw new UnauthorizedException();    	
    	}
    	
    	if(description.length()==0 || description == null) {
    		throw new InvalidProductDescriptionException();    		
    	}
    	
    	if(productCode == null || productCode.length() == 0 || db.checkExistingProductType(productCode) || !ProductTypeClass.validateProductCode(productCode)) {
    		throw new InvalidProductCodeException();    		
    	}

    	if(pricePerUnit <= 0) {
    		throw new InvalidPricePerUnitException();    		
    	} 
    	
    	//Get the last used ID
    	int lastid = db.getLastId("productTypes");
    	
    	//Create productType Object with newID
    	//(Integer id, Integer quantity, String location, String note, String productDescription, String barCode, Double pricePerUnit)
    	ProductType productType = new ProductTypeClass(lastid+1, 0, "location", note, description, productCode, pricePerUnit);
    	
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
    	
    	if(newCode == null || newCode.length() == 0  || db.checkExistingProductType(newCode) || !ProductTypeClass.validateProductCode(newCode)) {
    		throw new InvalidProductCodeException();    		
    	} 	  

    	if(newPrice <= 0) {
    		throw new InvalidPricePerUnitException();    		
    	} 	
    	
    	
    	User user = this.loggedUser;
    	
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
    	
    	User user = this.loggedUser;
    	
    	if(user==null || (!user.getRole().equals("Administrator") && !user.getRole().equals("ShopManager"))) {
    		throw new UnauthorizedException();
    	}    	
    
        return db.deleteProductType(id);
    }

    @Override
    public List<ProductType> getAllProductTypes() throws UnauthorizedException {    	
    	User user = this.loggedUser;
    	
    	if(user==null || (!user.getRole().equals("Administrator") && !user.getRole().equals("ShopManager") && !user.getRole().equals("Cashier")))
    		throw new UnauthorizedException(); 
    	
    	return db.getAllProductTypes();
    }

    @Override
    public ProductType getProductTypeByBarCode(String barCode) throws InvalidProductCodeException, UnauthorizedException {

    	
    	if(barCode == null || barCode.length() == 0  || !db.checkExistingProductType(barCode) || !ProductTypeClass.validateProductCode(barCode)) {
    		throw new InvalidProductCodeException();    		
    	} 	  
    	
    	User user = this.loggedUser;
    	
    	if(user==null || (!user.getRole().equals("Administrator") && !user.getRole().equals("ShopManager"))) {
    		throw new UnauthorizedException();
    	}        	
    	
    	 return db.getProductTypeByBarCode(barCode);
    }

    @Override
    public List<ProductType> getProductTypesByDescription(String description) throws UnauthorizedException {  	
    	
    	if(description == null) 
    		description = "";    	
    	
    	User user = this.loggedUser;
    	
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
    	
    	User user = this.loggedUser;
    	
    	if(user==null || (!user.getRole().equals("Administrator") && !user.getRole().equals("ShopManager"))) {
    		throw new UnauthorizedException();
    	} 
    	
    	//Get product quantity
    	Integer quantity = db.getQuantityByProductTypeId(productId);
    	
    	if(quantity + toBeAdded < 0)
    		return false;
    	
    	//update product quantity
    	if(db.updateQuantityByProductTypeId(productId, quantity + toBeAdded))
    		return true;
    	else
    		return false;  	      
    }

    @Override
    public boolean updatePosition(Integer productId, String newPos) throws InvalidProductIdException, InvalidLocationException, UnauthorizedException {
    	
    	if(productId == null || productId <= 0) {
    		throw new InvalidProductIdException("Invalid Product id");
    	}   
    	
    	if(newPos == null || newPos.length() == 0) {
    		newPos = "";
    	}
    	else
    	{
    		Pattern p = Pattern.compile("\\d+-\\d+-\\d+");
        	Matcher m = p.matcher(newPos);    	
        	 if (!m.matches()) {
        		 throw new InvalidLocationException("Invalid Location format");
        	 } 
        	 
        	 if(db.isLocationUsed(newPos))
         		return false;
         	
    	}
    	
    	  	
    	    	
    	User user = this.loggedUser;
    	
    	if(user==null || (!user.getRole().equals("Administrator") && !user.getRole().equals("ShopManager"))) {
    		throw new UnauthorizedException();
    	} 
    	
    	   	
    		
    	return db.updateProductTypeLocation(productId,newPos);
    }

    @Override
    public Integer issueOrder(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
    	
    	if(productCode == null || productCode.length() == 0 || !ProductTypeClass.validateProductCode(productCode)) { 
    		throw new InvalidProductCodeException("Invalid Product code");
    	}
    	
    	if(db.getProductTypeByBarCode(productCode) == null)
    		return -1;
    	
    	if(quantity <= 0)
    		throw new InvalidQuantityException("Quantity can not be less than 0");
    	
    	if(pricePerUnit <= 0)
    		throw new InvalidPricePerUnitException("Price per unit can not be less than 0");
    	
    	User user = this.loggedUser;
    	
    	if(user==null || (!user.getRole().equals("Administrator") && !user.getRole().equals("ShopManager"))) {
    		throw new UnauthorizedException();
    	} 
    	
    	//Get the last used ID
    	Integer lastid = db.getLastId("orders"); 
    	System.out.println(lastid);
    	//Create Order Object with newOrderID
    	Order order = new OrderClass(lastid+1,0, productCode, pricePerUnit, quantity, "ISSUED"); //balanceID??**********************************
    	
    	//Add Order to the DB    	
    	if(!db.addAndIssueOrder(order))
    		return -1;
    	
        return lastid+1;
    }

    @Override
    public Integer payOrderFor(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
    	if(productCode == null || productCode.length() == 0 || !ProductTypeClass.validateProductCode(productCode)) { 
    		throw new InvalidProductCodeException("Invalid Product code");
    	}
    	
    	if(db.getProductTypeByBarCode(productCode) == null) 
    		return -1;
    	
    	if(quantity <= 0)
    		throw new InvalidQuantityException("Quantity can not be less than 0");
    	
    	if(pricePerUnit <= 0)
    		throw new InvalidPricePerUnitException("Price per unit can not be less than 0");
    	
    	User user = this.loggedUser;
    	
    	if(user==null || (!user.getRole().equals("Administrator") && !user.getRole().equals("ShopManager"))) {
    		throw new UnauthorizedException();
    	} 
    	
    	//Get the last used ID
    	Integer lastid = db.getLastId("orders"); 
    	
        /** 
         * This method affects the balance of the system.
         * @return  the id of the order (> 0)
         *          -1 if the product does not exists, if the balance is not enough to satisfy the order, if there are some
         *          problems with the db
    	 */
    	
    	//TODO: manage balance
    	
    	//Create Order Object with newID
    	Order order = new OrderClass(lastid+1, lastid+1, productCode, pricePerUnit, quantity, "PAYED"); //balanceID??**********************************
    	
    	//Add Order to the DB    	
    	if(!db.addAndIssueOrder(order))
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
    	
    	User user = this.loggedUser;
    	
    	if(user==null || (!user.getRole().equals("Administrator") && !user.getRole().equals("ShopManager"))) {
    		throw new UnauthorizedException();
    	}     	
    	
    	return db.payOrder(orderId);
    }

    @Override
    public boolean recordOrderArrival(Integer orderId) throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException {
    	    
    	
    	if(orderId == null || orderId <= 0)
    		throw new InvalidOrderIdException("Order ID can not be less than 0");
    	
    	User user = this.loggedUser;
    	
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
    	{    		
    		if(p.getBarCode().equals(order.getProductCode()))
    		{    			
    			prod = p;
    			break;
    		}
    			
    	}
    	
    	if(prod == null) //Non existing product Type anymore, maybe deleted. Cannot update status
    	{
    		System.err.println("Product does not exist anymore. Cannot change the status!");
    		return false;
    	}
    		
    	
    	if(prod.getLocation().length() == 0)
    		throw new InvalidLocationException("Product type of the ordered product has not an assigned location");
    	
    	
    	return db.recordOrderArrival(orderId, prod.getBarCode(), prod.getQuantity() + order.getQuantity());
    }

    @Override
    public List<Order> getAllOrders() throws UnauthorizedException {
    	User user = this.loggedUser;
    	
    	if(user==null || (!user.getRole().equals("Administrator") && !user.getRole().equals("ShopManager"))) {
    		throw new UnauthorizedException();
    	} 
    	
    	return db.getAllOrders();
    }

    /// User case 4+
    
    @Override
    public Integer defineCustomer(String customerName) throws InvalidCustomerNameException, UnauthorizedException {   	
    	User user = this.loggedUser;
    	
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
    	User user = this.loggedUser;
    	
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
    	User user = this.loggedUser;
    	
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
    	User user = this.loggedUser;
    	
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
    	User user = this.loggedUser;
    	
    	if(user==null || (!user.getRole().equals("Administrator") && !user.getRole().equals("ShopManager") && !user.getRole().equals("Cashier"))) {
    		throw new UnauthorizedException();
    	}
    	List<Customer> customerlist = new ArrayList<>();
    	customerlist = db.getAllCustomers();
        return customerlist;
    }

    @Override
    public String createCard() throws UnauthorizedException {
    	User user = this.loggedUser;
    	
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
    	User user = this.loggedUser;
    	
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

    	return flag;
    }

    @Override
    public boolean modifyPointsOnCard(String customerCard, int pointsToBeAdded) throws InvalidCustomerCardException, UnauthorizedException {
    	User user = this.loggedUser;
    	
    	if(user==null || (!user.getRole().equals("Administrator") && !user.getRole().equals("ShopManager") && !user.getRole().equals("Cashier"))) {
    		throw new UnauthorizedException();
    	}
    	
    	if(customerCard=="" || customerCard==null || customerCard.length()!=10) {
    		throw new InvalidCustomerCardException();
    	}
    	
		Integer currentPoints = db.getCardPoints(customerCard);
		if(pointsToBeAdded*(-1)>currentPoints || currentPoints==-1){
			return false;
		}
    	boolean flag = db.updateCardPoints(customerCard, currentPoints+pointsToBeAdded);
    	
    	return flag;
    }

    @Override
    public Integer startSaleTransaction() throws UnauthorizedException {
    	User user = this.loggedUser;
    	
    	if(user==null || (!user.getRole().equals("Administrator") && !user.getRole().equals("ShopManager") && !user.getRole().equals("Cashier"))) {
    		throw new UnauthorizedException();
    	}
		Integer lastId = db.getLastId("saleTransactions");
		lastId = lastId + 1;
		Date date = new Date();
		String[] datesplit = date.toString().split(" ");
		List<TicketEntry> entries = new ArrayList<>();
		SaleTransactionClass transaction = new SaleTransactionClass(lastId,datesplit[0],datesplit[1],0.0,"",0.0,entries,"OPEN");
		lastId = db.startSaleTransaction(transaction);
    	tickets.put(lastId,entries);
        return lastId;
    }

    @Override
    public boolean addProductToSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
    	User user = this.loggedUser;
    	
    	if(user==null || (!user.getRole().equals("Administrator") && !user.getRole().equals("ShopManager") && !user.getRole().equals("Cashier"))) {
    		throw new UnauthorizedException();
    	}
		
		if(transactionId<0 || transactionId==null){
			throw new InvalidTransactionIdException();
		}

		
		if(productCode==null || productCode==""){
			throw new InvalidProductCodeException();
		}

		if(amount <= 0){
			throw new InvalidQuantityException();
		}
		
		ProductType product = getProductTypeByBarCode(productCode);

		if(product==null){
			return false;
		}

		if(product.getQuantity()<amount){
			return false;
		}

		List<TicketEntry> entries = tickets.get(transactionId);
		SaleTransactionClass transaction = db.getSaleTransactionById(transactionId);
		if(transaction==null || entries==null){
			return false;
		}
		
		
		boolean flag = false;
		for(TicketEntry entry : entries){
			if(entry.getBarCode().equals(productCode)){
				flag = true;
				entry.setAmount(entry.getAmount()+amount);
				db.updateQuantityByBarCode(productCode, product.getQuantity()-(entry.getAmount()+amount));
			}
		}
		if(flag==false) {
			db.updateQuantityByBarCode(productCode,product.getQuantity()-amount);
			TicketEntryClass entry = new TicketEntryClass(0,productCode,product.getProductDescription(),amount,product.getPricePerUnit(),0.0);
			entries.add(entry);			
		}

		tickets.put(transactionId,entries);

    	return flag;
    }

    @Override
    public boolean deleteProductFromSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
    	User user = this.loggedUser;
    	
    	if(user==null || (!user.getRole().equals("Administrator") && !user.getRole().equals("ShopManager") && !user.getRole().equals("Cashier"))) {
    		throw new UnauthorizedException();
    	}
		
		if(transactionId<0 || transactionId==null){
			throw new InvalidTransactionIdException();
		}

		
		if(productCode==null || productCode==""){
			throw new InvalidProductCodeException();
		}

		if(amount <= 0){
			throw new InvalidQuantityException();
		}


		ProductType product = getProductTypeByBarCode(productCode);

		if(product==null){
			return false;
		}

		List<TicketEntry> entries = tickets.get(transactionId);
		SaleTransactionClass transaction = db.getSaleTransactionById(transactionId);
		if(transaction==null || entries==null){
			return false;
		}

		if(transaction.getState().equals("PAYED") || transaction.getState().equals("CLOSED")){
			return false;
		}
		
		for(TicketEntry entry: entries){
			if(entry.getBarCode().equals(productCode)){
				Integer curramount = entry.getAmount();
				if(amount>curramount){
					return false;
				}
				entry.setAmount(curramount-amount);
				boolean flag = db.updateQuantityByBarCode(productCode, product.getQuantity()+newQuantity);
			}
		}

		tickets.put(transactionId,entries);

		return flag;
    }

    @Override
    public boolean applyDiscountRateToProduct(Integer transactionId, String productCode, double discountRate) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidDiscountRateException, UnauthorizedException {
    	User user = this.loggedUser;
    	
    	if(user==null || (!user.getRole().equals("Administrator") && !user.getRole().equals("ShopManager") && !user.getRole().equals("Cashier"))) {
    		throw new UnauthorizedException();
    	}
		
		if(transactionId<0 || transactionId==null){
			throw new InvalidTransactionIdException();
		}

		
		if(productCode==null || productCode=="" || ProductTypeClass.validateProductCode(productCode)==false){
			throw new InvalidProductCodeException();
		}

		if(discountRate <= 0 || discountRate > 1){
			throw new InvalidDiscountRateException();
		}

		ProductType product = getProductTypeByBarCode(productCode);

		if(product==null){
			return false;
		}
		
		List<TicketEntry> entries = tickets.get(transactionId);
		if(entries==null){
			return false;
		}
			
		for(TicketEntry entry : entries){
			if(entry.getBarCode()==productCode){
				entry.setDiscountRate(discountRate);
			}
		}
		tickets.put(transactionId, entries);

    	return true;
    }

    @Override
    public boolean applyDiscountRateToSale(Integer transactionId, double discountRate) throws InvalidTransactionIdException, InvalidDiscountRateException, UnauthorizedException {
    	User user = this.loggedUser;
    	
    	if(user==null || (!user.getRole().equals("Administrator") && !user.getRole().equals("ShopManager") && !user.getRole().equals("Cashier"))) {
    		throw new UnauthorizedException();
    	}
    	
    	if(transactionId<0 || transactionId==null){
			throw new InvalidTransactionIdException();
		}

		SaleTransactionClass transaction = db.getSaleTransactionById(transactionId);
		if(transaction==null || tickets.get(transactionId)==null){
			return false;
		}

		if(transaction.getState().equals("PAYED")){
			return false;
		}

		boolean flag = db.applyDiscountRate(transactionId, discountRate);
    	return flag;
    }

    @Override
    public int computePointsForSale(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
    	User user = this.loggedUser;
    	
    	if(user==null || (!user.getRole().equals("Administrator") && !user.getRole().equals("ShopManager") && !user.getRole().equals("Cashier"))) {
    		throw new UnauthorizedException();
    	}
    	
    	if(transactionId<0 || transactionId==null){
			throw new InvalidTransactionIdException();
		}
		
		SaleTransactionClass transaction = db.getSaleTransactionById(transactionId);
		if(transaction==null){
			return -1;
		}

		List<TicketEntry> entries = tickets.get(transactionId);
		if(transaction.getState().equals("OPEN")){
			if(entries==null){
				return -1;
			}
			Double total = 0.0;
			Double prodTotal = 0.0;
			for(TicketEntry entry: entries){
				prodTotal = entry.getAmount() * entry.getPricePerUnit();
				total = total + prodTotal*(1-entry.getDiscountRate());
			}
			total = total*(1-transaction.getDiscountRate());
		}
		else{
			SaleTransactionClass transactionClosed = getClosedSaleTransactionById(transactionId);
			entries = transactionClosed.getEntries();
			Double total = 0.0;
			Double prodTotal = 0.0;
			for(TicketEntry entry: entries){
				prodTotal = entry.getAmount() * entry.getPricePerUnit();
				total = total + prodTotal*(1-entry.getDiscountRate());
			}
			total = total*(1-transactionClosed.getDiscountRate());
		}

		Integer points = Integer.parseInt(total);
    	return points;
    }

    @Override
    public boolean endSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
    	User user = this.loggedUser;
    	
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
    // chiedere se si può cambiare il nome dil parameter
    public boolean deleteSaleTransaction(Integer saleNumber)
            throws InvalidTransactionIdException, UnauthorizedException {
        User user = this.loggedUser;

        if (user == null || (!user.getRole().equals("Administrator") && !user.getRole().equals("ShopManager")
                && !user.getRole().equals("Cashier"))) {
            throw new UnauthorizedException();
        }

        if (saleNumber == null || saleNumber <= 0) {
            throw new InvalidTransactionIdException();
        }

        boolean flag = db.deleteSaleTransaction(saleNumber);
        return flag;
    }

    @Override
    public SaleTransaction getSaleTransaction(Integer transactionId)
            throws InvalidTransactionIdException, UnauthorizedException {

        User user = this.loggedUser;

        if (user == null || !user.getRole().equals("Administrator") && !user.getRole().equals("ShopManager")
                && !user.getRole().equals("Cashier")) {
            throw new UnauthorizedException();
        }

        if (transactionId <= 0 || transactionId == null) {
            throw new InvalidTransactionIdException();
        }
        List<TicketEntry> products = db.getProductEntriesByTransactionId(transactionId);
        return db.getClosedSaleTransactionById(transactionId, products);
    }

    @Override
    public Integer startReturnTransaction(Integer saleNumber)
            throws /* InvalidTicketNumberException, */InvalidTransactionIdException, UnauthorizedException {
        User user = this.loggedUser;

        if (user == null || (!user.getRole().equals("Administrator") && !user.getRole().equals("ShopManager")
                && !user.getRole().equals("Cashier"))) {
            throw new UnauthorizedException();
        }

        if (saleNumber == null || saleNumber <= 0)
            throw new InvalidTransactionIdException();

        int lastid = db.getLastId("returnTransactions");
        ReturnTransactionClass returnTransaction = new ReturnTransactionClass(lastid + 1, saleNumber, 0, 0, "ACTIVE");
        Integer result = db.startReturnTransaction(returnTransaction);

        return result;
    }

    @Override
    public boolean returnProduct(Integer returnId, String productCode, int amount) throws InvalidTransactionIdException,
            InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        User user = this.loggedUser;

        if (user == null || (!user.getRole().equals("Administrator") && !user.getRole().equals("ShopManager")
                && !user.getRole().equals("Cashier"))) {
            throw new UnauthorizedException();
        }

        if (returnId == null || returnId <= 0)
            throw new InvalidTransactionIdException();

        if (amount <= 0)
            throw new InvalidQuantityException();
        boolean productValid = ProductTypeClass.validateProductCode(productCode);
        if (productCode == "" || productCode == null || !productValid)
            throw new InvalidProductCodeException();

        ReturnTransactionClass returnTransaction = db.getReturnTransactionById(returnId);
        if (returnTransaction == null)
            return false;
        boolean existProduct = db.checkExistingProductType(productCode);
        boolean existProductInSale = db.checkProductInSaleTransaction(returnTransaction.getTransactionId(), productCode);
        
        int entryAmount = db.getAmountonEntry(returnTransaction.getTransactionId(), productCode);
        //The amount of units of product to be returned should not exceed the amount originally sold.
        // if it was not in the transaction
        if (entryAmount < amount || !existProductInSale || !existProduct)
        	return false;
        double returnValue = db.getPricePerUnit(productCode)*amount;
        ProductReturnClass tempReturn = new ProductReturnClass(0, returnId, productCode, amount, returnValue);
        returns.add(tempReturn);
//        boolean flag = db.returnProduct(returnTransaction.getId(), returnTransaction.getTransactionId(), productCode,
//                amount, returnValue);

        return true;
    }

    /**
     * This method closes a return transaction. A closed return transaction can be
     * committed (i.e. <commit> = true) thus it increases the product quantity
     * available on the shelves or not (i.e. <commit> = false) thus the whole
     * transaction is undone. This method updates the transaction status (decreasing
     * the number of units sold by the number of returned one and decreasing the
     * final price). If committed, the return transaction must be persisted in the
     * system's memory. It can be invoked only after a user with role
     * "Administrator", "ShopManager" or "Cashier" is logged in.
     *
     * @param returnId the id of the transaction
     * @param commit   whether we want to commit (True) or rollback(false) the
     *                 transaction
     *
     * @return true if the operation is successful false if the returnId does not
     *         correspond to an active return transaction, if there is some problem
     *         with the db
     *
     * @throws InvalidTransactionIdException if returnId is less than or equal to 0
     *                                       or if it is null
     * @throws UnauthorizedException         if there is no logged user or if it has
     *                                       not the rights to perform the operation
     */
    @Override
    public boolean endReturnTransaction(Integer returnId, boolean commit)
            throws InvalidTransactionIdException, UnauthorizedException {
        User user = this.loggedUser;

        if (user == null || (!user.getRole().equals("Administrator") && !user.getRole().equals("ShopManager")
                && !user.getRole().equals("Cashier"))) {
            throw new UnauthorizedException();
        }
        if (returnId==null || returnId<=0) {
        	throw new InvalidTransactionIdException();
        }

        if (commit) {
			ReturnTransactionClass returnTransaction = db.getReturnTransactionById(returnId);
			int totalAmountReturned = returnTransaction.getQuantity();
			double totalReturnValue = returnTransaction.getReturnValue();
			double totalSold = 0;
        	for(ProductReturnClass productReturn : returns){
        		int lastId = db.getLastId("productReturns"); 
        		boolean addedReturnEntry = db.returnProduct(lastId+1, productReturn.getReturnId(), productReturn.getProductCode(),
        				productReturn.getQuantity(), productReturn.getReturnValue());
        		if(addedReturnEntry) {
            		int actualAmount = db.getQuantityByProductTypeBarCode(productReturn.getProductCode());
        			int newAmount = actualAmount+productReturn.getQuantity();
    				db.updateQuantityByBarCode(productReturn.getProductCode(), newAmount);
    				
    				totalAmountReturned += productReturn.getQuantity();
    				totalReturnValue += productReturn.getReturnValue();
    				
    				int actualAmountSold = db.getAmountonEntry(returnTransaction.getTransactionId(), productReturn.getProductCode());
    				int newAmountSold = actualAmountSold - productReturn.getQuantity();
    				
    				double actualTotalSold = db.getTotalOnEntry(returnTransaction.getTransactionId(), productReturn.getProductCode());
    				double newTotalSold = actualTotalSold - productReturn.getReturnValue();
    				totalSold += newTotalSold;
    				db.updateEntryAfterCommit(returnTransaction.getTransactionId(), productReturn.getProductCode(), newAmountSold, newTotalSold);
        		}
    		}
			db.updateReturnTransaction(returnId, totalAmountReturned, totalReturnValue);
			db.updateSaleTransactionAfterCommit(returnTransaction.getTransactionId(), totalSold);
        	returns.clear();
            return true;
        }
        //Close the return transaction
		db.updateReturnTransaction(returnId, 0, 0.0);
		returns.clear();
        return false;
    }

    @Override
    public boolean deleteReturnTransaction(Integer returnId)
            throws InvalidTransactionIdException, UnauthorizedException {
        User user = this.loggedUser;

        if (user == null || (!user.getRole().equals("Administrator") && !user.getRole().equals("ShopManager")
                && !user.getRole().equals("Cashier"))) {
            throw new UnauthorizedException();
        }

        if (returnId == null || returnId <= 0) {
            throw new InvalidTransactionIdException();
        }
        //Update first entries and products
		ReturnTransactionClass returnTransaction = db.getReturnTransactionById(returnId);
        List<ProductReturnClass> returnProducts = db.getAllProductReturnsById(returnId);
        double newTotal = 0;
        for(ProductReturnClass productReturn : returnProducts){
    		int actualAmount = db.getQuantityByProductTypeBarCode(productReturn.getProductCode());
			int newAmount = actualAmount-productReturn.getQuantity();
			db.updateQuantityByBarCode(productReturn.getProductCode(), newAmount);
			
			int actualAmountSold = db.getAmountonEntry(returnTransaction.getTransactionId(), productReturn.getProductCode());
			int newAmountSold = actualAmountSold - productReturn.getQuantity();
			
			double actualTotalSold = db.getTotalOnEntry(returnTransaction.getTransactionId(), productReturn.getProductCode());
			double newTotalSold = actualTotalSold - productReturn.getReturnValue();
			newTotal += newTotalSold;			
			db.updateEntryAfterCommit(returnTransaction.getTransactionId(), productReturn.getProductCode(), newAmountSold, newTotalSold);
		}
        // Update sale transaction
		db.updateSaleTransactionAfterCommit(returnTransaction.getTransactionId(), newTotal);
		// delete first return products since it has foreign key
		boolean deletedProductReturns = db.deleteProductReturnsByReturnId(returnId);
        boolean deletedReturnTransaction = db.deleteReturnTransaction(returnId);
        if (deletedProductReturns && deletedReturnTransaction) { 
        	return true;
        }
        return false;
    }
    /**
     * This method record the payment of a sale transaction with cash and returns the change (if present).
     * This method affects the balance of the system.
     * It can be invoked only after a user with role "Administrator", "ShopManager" or "Cashier" is logged in.
     *
     * @param transactionId the number of the transaction that the customer wants to pay
     * @param cash the cash received by the cashier
     *
     * @return the change (cash - sale price)
     *         -1   if the sale does not exists,
     *              if the cash is not enough,
     *              if there is some problemi with the db
     *
     * @throws InvalidTransactionIdException if the  number is less than or equal to 0 or if it is null
     * @throws UnauthorizedException if there is no logged user or if it has not the rights to perform the operation
     * @throws InvalidPaymentException if the cash is less than or equal to 0
     */
    @Override
    public double receiveCashPayment(Integer transactionId, double cash)
            throws InvalidTransactionIdException, InvalidPaymentException, UnauthorizedException {
    	User user = this.loggedUser;

        if (user == null || (!user.getRole().equals("Administrator") && !user.getRole().equals("ShopManager")
                && !user.getRole().equals("Cashier"))) {
            throw new UnauthorizedException();
        }

        if (transactionId == null || transactionId <= 0) {
            throw new InvalidTransactionIdException();
        }
        
        if (cash <= 0) {
            throw new InvalidPaymentException();
        }
        double change = -1;
        SaleTransaction saleTransaction = db.getSaleTransactionById(transactionId);
        if (saleTransaction == null){
        	return change;
        }
        double salePrice = saleTransaction.getPrice();
        if (salePrice > cash) {
        	return change;
        }
        boolean updatedSaleTransaction = db.updateStateSaleTransaction(transactionId, "PAYED");
    	boolean updatedBalanceOperation = recordBalanceUpdate(salePrice);
    	
    	//Only return cash if no problems in db and recorded
    	if (updatedSaleTransaction && updatedBalanceOperation) {
    		change = cash-salePrice;
    	}
      
        return change;
    }

    @Override
    public boolean receiveCreditCardPayment(Integer ticketNumber, String creditCard)
            throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {
        return false;
    }

    @Override
    public double returnCashPayment(Integer returnId) throws InvalidTransactionIdException, UnauthorizedException {
        return 0;
    }

    @Override
    public double returnCreditCardPayment(Integer returnId, String creditCard)
            throws InvalidTransactionIdException, InvalidCreditCardException, UnauthorizedException {
        return 0;
    }

    @Override
    public boolean recordBalanceUpdate(double toBeAdded) throws UnauthorizedException {
    	User user = this.loggedUser;

        if (user == null || (!user.getRole().equals("Administrator") && !user.getRole().equals("ShopManager"))) {
            throw new UnauthorizedException();
        }
        
        double actualBalance = db.getActualBalance();
        
        if (actualBalance + toBeAdded < 0) {
        	return false;
        }
       
        String type = "";
        if (toBeAdded >=0) {
        	type = "CREDIT";
        } else {
        	type = "DEBIT";
        }
        
        int newId = db.getLastId("balanceOperations");
    	LocalDate date = LocalDate.now();
    	BalanceOperation balanceOperation = new BalanceOperationClass(newId+1,date, toBeAdded, type);
    	boolean recordedBalanceOperation = db.recordBalanceOperation(balanceOperation);
        return recordedBalanceOperation;
    }
    /**
     * This method returns a list of all the balance operations (CREDIT,DEBIT,ORDER,SALE,RETURN) performed between two
     * given dates.
     * This method should understand if a user exchanges the order of the dates and act consequently to correct
     * them.
     * Both <from> and <to> are included in the range of dates and might be null. This means the absence of one (or
     * both) temporal constraints.
     *
     *
     * @param from the start date : if null it means that there should be no constraint on the start date
     * @param to the end date : if null it means that there should be no constraint on the end date
     *
     * @return All the operations on the balance whose date is <= to and >= from
     *
     * @throws UnauthorizedException if there is no logged user or if it has not the rights to perform the operation
     */
    @Override
    public List<BalanceOperation> getCreditsAndDebits(LocalDate from, LocalDate to) throws UnauthorizedException {
    	User user = this.loggedUser;
    	String fromString = "";
    	String toString = "";
        if (user == null || (!user.getRole().equals("Administrator") && !user.getRole().equals("ShopManager"))) {
            throw new UnauthorizedException();
        }
        if (from!=null) {
        	fromString = from.toString();
        }
        if (to!=null) {
        	toString = to.toString();
        }
        
        List<BalanceOperation> bolist = db.getBalanceOperations(fromString, toString);
        return bolist;
    }
    
    @Override
    public double computeBalance() throws UnauthorizedException {
    	User user = this.loggedUser;

        if (user == null || (!user.getRole().equals("Administrator") && !user.getRole().equals("ShopManager"))) {
            throw new UnauthorizedException();
        }
        double actualBalance = db.getActualBalance();
        return actualBalance;
    }
}
