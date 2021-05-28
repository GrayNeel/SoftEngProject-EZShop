package it.polito.ezshop.acceptanceTests;

import it.polito.ezshop.classes.*;
import it.polito.ezshop.data.*;
import it.polito.ezshop.exceptions.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.Test;

public class ResetTest {
    EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
    EZShopDB db = new EZShopDB();

    @Test
    public void resetTestCase() {
        // First save all information in database
        List<User> userlist = db.getAllUsers();
        List<ProductType> ptlist = db.getAllProductTypes();
        List<Order> orderlist = db.getAllOrders();
        List<Customer> customerlist = db.getAllCustomers();

        ezShop.reset();
        assertTrue(db.getAllUsers().isEmpty());
        assertTrue(db.getAllProductTypes().isEmpty());
        assertTrue(db.getAllOrders().isEmpty());
        assertTrue(db.getAllCustomers().isEmpty());
        // Since all db is empty, the next id should be 0 for all tables
        assert (db.getLastId("balanceOperations").equals(0));
        assert (db.getActualBalance() == 0.00);
        assert (db.getLastId("cards").equals(0));
        assert (db.getLastId("creditCards").equals(0));
        assert (db.getLastId("productEntries").equals(0));
        assert (db.getLastId("productReturns").equals(0));
        assert (db.getLastId("returnTransactions").equals(0));
        assert (db.getLastId("saleTransactions").equals(0));

        // Now we leave database how it was before

        for (User us : userlist) {
            db.addUser(us);
        }

        for (ProductType prod : ptlist) {
            db.addProductType(prod);
        }

        for (Order order : orderlist) {
            db.addAndIssueOrder(order);
        }

        for (Customer cus : customerlist) {
            CustomerClass newCus = new CustomerClass(cus.getId(), cus.getCustomerName(), cus.getCustomerCard(),
                    cus.getPoints());
            db.defineCustomer(newCus);
        }
        BalanceOperation bal = new BalanceOperationClass(15, LocalDate.parse("2021-05-26"), 150.0, "Shopping");
        db.recordBalanceOperation(bal);
        CreditCardClass cred1 = new CreditCardClass("4485123437008543", 160.0);
        CreditCardClass cred2 = new CreditCardClass("4485143337109543", 123.5);
        CreditCardClass cred3 = new CreditCardClass("4485370086510891", 170.0);
        CreditCardClass cred4 = new CreditCardClass("5303098087309156", 4999934.75);
        db.recordCreditCard(1, cred1);
        db.recordCreditCard(2, cred2);
        db.recordCreditCard(3, cred3);
        db.recordCreditCard(4, cred4);

    }

}
