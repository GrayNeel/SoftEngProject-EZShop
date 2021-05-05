package it.polito.ezshop.data;

import it.polito.ezshop.classes.*;
import it.polito.ezshop.exceptions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;



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
    	lastid = db.getLastId(); //prendiamo l'ultimo ID o il primo libero? Per rimpiazzare quelli cancellati
    	
    	//Create User Object with newID
    	UserClass user = new UserClass(lastid+1, username, password, role);
    	
    	//Add user to the DB
    	db.addUser(user);
    	
        return lastid+1;
    }

    @Override
    public boolean deleteUser(Integer id) throws InvalidUserIdException, UnauthorizedException {
    	////////////////////// TO DO ///////////////////////////////
    	/* @throws UnauthorizedException if there is no logged user or if it has not the rights to perform the operation*/
    	
    	//Check that username is not empty and it does not exist
    	if(id == null || id <= 0) {
    		throw new InvalidUserIdException("Invalid id");
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
    	Integer barcode = null;
    	
    	//Verifying that the string is a number
    	try {
    		barcode = Integer.parseInt(productCode);
    	}catch(NumberFormatException e) {
    		throw new InvalidProductCodeException();
    	}
    	
    	if(user==null || (!user.getRole().equals("Administrator") && !user.getRole().equals("ShopManager"))) {
    		throw new UnauthorizedException();
    	}
    	
    	if(description.length()==0 || description == null) {
    		throw new InvalidProductDescriptionException();
    	}
    	
    	if(productCode == null || productCode.length() == 0) {
    		throw new InvalidProductCodeException();
    	}
    	
    	//TODO: check barcode validity (https://www.gs1.org/services/how-calculate-check-digit-manually)
    	//and price per unit
    	
    	
    	
    	return null;
    }

    @Override
    public boolean updateProduct(Integer id, String newDescription, String newCode, double newPrice, String newNote) throws InvalidProductIdException, InvalidProductDescriptionException, InvalidProductCodeException, InvalidPricePerUnitException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean deleteProductType(Integer id) throws InvalidProductIdException, UnauthorizedException {
        return false;
    }

    @Override
    public List<ProductType> getAllProductTypes() throws UnauthorizedException {
    	//TEMP: REMOVE IT!! IT IS JUST FOR TESTING
    	List<ProductType> prodlist = new ArrayList<>();
        return prodlist;
    }

    @Override
    public ProductType getProductTypeByBarCode(String barCode) throws InvalidProductCodeException, UnauthorizedException {
        return null;
    }

    @Override
    public List<ProductType> getProductTypesByDescription(String description) throws UnauthorizedException {
    	//TEMP: REMOVE IT!! IT IS JUST FOR TESTING
    	List<ProductType> prodlist = new ArrayList<>();
        return prodlist;
    }

    @Override
    public boolean updateQuantity(Integer productId, int toBeAdded) throws InvalidProductIdException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean updatePosition(Integer productId, String newPos) throws InvalidProductIdException, InvalidLocationException, UnauthorizedException {
        return false;
    }

    @Override
    public Integer issueOrder(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
        return null;
    }

    @Override
    public Integer payOrderFor(String productCode, int quantity, double pricePerUnit) throws InvalidProductCodeException, InvalidQuantityException, InvalidPricePerUnitException, UnauthorizedException {
        return null;
    }

    @Override
    public boolean payOrder(Integer orderId) throws InvalidOrderIdException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean recordOrderArrival(Integer orderId) throws InvalidOrderIdException, UnauthorizedException, InvalidLocationException {
        return false;
    }

    @Override
    public List<Order> getAllOrders() throws UnauthorizedException {
    	//TEMP: REMOVE IT!! IT IS JUST FOR TESTING
    	List<Order> ordlist = new ArrayList<>();
        return ordlist;
    }

    @Override
    public Integer defineCustomer(String customerName) throws InvalidCustomerNameException, UnauthorizedException {
        return null;
    }

    @Override
    public boolean modifyCustomer(Integer id, String newCustomerName, String newCustomerCard) throws InvalidCustomerNameException, InvalidCustomerCardException, InvalidCustomerIdException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean deleteCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
        return false;
    }

    @Override
    public Customer getCustomer(Integer id) throws InvalidCustomerIdException, UnauthorizedException {
        return null;
    }

    @Override
    public List<Customer> getAllCustomers() throws UnauthorizedException {
    	//TEMP: REMOVE IT!! IT IS JUST FOR TESTING
    	List<Customer> customerlist = new ArrayList<>();
        return customerlist;
    }

    @Override
    public String createCard() throws UnauthorizedException {
        return null;
    }

    @Override
    public boolean attachCardToCustomer(String customerCard, Integer customerId) throws InvalidCustomerIdException, InvalidCustomerCardException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean modifyPointsOnCard(String customerCard, int pointsToBeAdded) throws InvalidCustomerCardException, UnauthorizedException {
        return false;
    }

    @Override
    public Integer startSaleTransaction() throws UnauthorizedException {
        return null;
    }

    @Override
    public boolean addProductToSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean deleteProductFromSale(Integer transactionId, String productCode, int amount) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidQuantityException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean applyDiscountRateToProduct(Integer transactionId, String productCode, double discountRate) throws InvalidTransactionIdException, InvalidProductCodeException, InvalidDiscountRateException, UnauthorizedException {
        return false;
    }

    @Override
    public boolean applyDiscountRateToSale(Integer transactionId, double discountRate) throws InvalidTransactionIdException, InvalidDiscountRateException, UnauthorizedException {
        return false;
    }

    @Override
    public int computePointsForSale(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        return 0;
    }

    @Override
    public boolean endSaleTransaction(Integer transactionId) throws InvalidTransactionIdException, UnauthorizedException {
        return false;
    }

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
