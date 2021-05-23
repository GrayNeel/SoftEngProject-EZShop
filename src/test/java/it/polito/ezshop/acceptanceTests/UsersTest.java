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

public class UsersTest {
    EZShopInterface ezShop = new it.polito.ezshop.data.EZShop();
    EZShopDB db = new EZShopDB();

    @Test
    public void createUserTestCase() throws InvalidUsernameException, InvalidPasswordException, InvalidRoleException {

        assertThrows(InvalidUsernameException.class, () -> ezShop.createUser("", "1234567", "Cashier"));
        String nullUsername = null;
        assertThrows(InvalidUsernameException.class, () -> ezShop.createUser(nullUsername, "1234567", "Cashier"));

        assertThrows(InvalidPasswordException.class, () -> ezShop.createUser("newUser", "", "Cashier"));
        String nullPassword = null;
        assertThrows(InvalidPasswordException.class, () -> ezShop.createUser("newUser", nullPassword, "Cashier"));

        assertThrows(InvalidRoleException.class, () -> ezShop.createUser("newUser", "1234567", ""));
        String nullRole = null;
        assertThrows(InvalidRoleException.class, () -> ezShop.createUser("newUser", "1234567", nullRole));
        assertThrows(InvalidRoleException.class, () -> ezShop.createUser("newUser", "1234567", "Owner"));
        // Given as tested

        Integer userId = ezShop.createUser("testUser", "1234567", "Cashier");
        assert (userId > 0);

        // Confirming it was added
        Integer lastUserId = db.getLastId("users");
        assert (lastUserId == userId);
        ezShop.login("admin", "strong");
        db.deleteUser(userId);
        ezShop.logout();
        // Creating an existing user in database
        assert (ezShop.createUser("admin", "12345670", "Administrator") == -1);
    }

    @Test
    public void deleteUserTestCase() throws InvalidUserIdException, UnauthorizedException, InvalidUsernameException,
            InvalidPasswordException, InvalidRoleException {
        ezShop.login("admin", "strong");
        Integer userId = ezShop.createUser("userTest", "12345670", "Cashier");
        ezShop.logout();

        assertThrows(UnauthorizedException.class, () -> ezShop.deleteUser(userId));
        ezShop.login("admin", "strong");

        assertThrows(InvalidUserIdException.class, () -> ezShop.deleteUser(0));
        assertThrows(InvalidUserIdException.class, () -> ezShop.deleteUser(-5));
        Integer nullId = null;
        assertThrows(InvalidUserIdException.class, () -> ezShop.deleteUser(nullId));

        assertTrue(ezShop.deleteUser(userId));
        assertFalse(ezShop.deleteUser(userId));

        ezShop.logout();
    }

    @Test
    public void getAllUsersTestCase() throws UnauthorizedException, InvalidUsernameException, InvalidPasswordException,
            InvalidRoleException, InvalidUserIdException {

        assertThrows(UnauthorizedException.class, () -> ezShop.getAllUsers());
        // Trying with an existing user but with no permissions
        ezShop.login("cashier", "1234567");
        assertThrows(UnauthorizedException.class, () -> ezShop.getAllUsers());
        ezShop.logout();

        ezShop.login("admin", "strong");
        assertFalse(ezShop.getAllUsers().isEmpty());
        Integer qtyUsers = ezShop.getAllUsers().size();
        Integer newId = ezShop.createUser("testUser", "12345678", "Cashier");
        assert (ezShop.getAllUsers().size() == qtyUsers + 1);
        ezShop.deleteUser(newId);
        ezShop.logout();
    }

    @Test
    public void getUserTestCase() throws InvalidUserIdException, UnauthorizedException, InvalidUsernameException,
            InvalidPasswordException, InvalidRoleException {

        assertThrows(InvalidUserIdException.class, () -> ezShop.getUser(0));
        assertThrows(InvalidUserIdException.class, () -> ezShop.getUser(-5));
        Integer nullInteger = null;
        assertThrows(InvalidUserIdException.class, () -> ezShop.getUser(nullInteger));

        // Not logged in user
        assertThrows(UnauthorizedException.class, () -> ezShop.getUser(4));
        // Logged user but with no permissions
        ezShop.login("cashier", "1234567");
        assertThrows(UnauthorizedException.class, () -> ezShop.getUser(4));
        ezShop.logout();

        ezShop.login("admin", "strong");
        Integer newUser = ezShop.createUser("testUser2", "1234567", "Cashier");
        assertNotNull(ezShop.getUser(newUser));
        assert (ezShop.getUser(newUser).getUsername().equals("testUser2"));
        assert (ezShop.getUser(newUser).getPassword().equals("1234567"));
        assert (ezShop.getUser(newUser).getRole().equals("Cashier"));
        ezShop.logout();

        assertThrows(UnauthorizedException.class, () -> ezShop.getUser(newUser));
        ezShop.login("admin", "strong");
        ezShop.deleteUser(newUser);
        ezShop.logout();
    }

    @Test
    public void updateUserRightsTestCase() throws InvalidUserIdException, InvalidRoleException, UnauthorizedException,
            InvalidUsernameException, InvalidPasswordException {

        assertThrows(InvalidUserIdException.class, () -> ezShop.updateUserRights(0, "Cashier"));
        assertThrows(InvalidUserIdException.class, () -> ezShop.updateUserRights(-5, "Cashier"));
        Integer nullInteger = null;
        assertThrows(InvalidUserIdException.class, () -> ezShop.updateUserRights(nullInteger, "Cashier"));

        assertThrows(InvalidRoleException.class, () -> ezShop.updateUserRights(10, ""));
        String nullRole = null;
        assertThrows(InvalidRoleException.class, () -> ezShop.updateUserRights(10, nullRole));
        assertThrows(InvalidRoleException.class, () -> ezShop.updateUserRights(10, "Owner"));

        // Non authorized user trying to update role
        assertThrows(UnauthorizedException.class, () -> ezShop.updateUserRights(5, "Administrator"));

        // Logged user trying to update with no permissions
        ezShop.login("cashier", "1234567");
        assertThrows(UnauthorizedException.class, () -> ezShop.updateUserRights(5, "Administrator"));
        ezShop.logout();

        ezShop.login("admin", "strong");
        Integer userId = ezShop.createUser("workerOfMonth", "12345670", "Cashier");
        assertThrows(InvalidRoleException.class, () -> ezShop.updateUserRights(userId, "Owner"));
        assertThrows(InvalidRoleException.class, () -> ezShop.updateUserRights(userId, ""));

        assertTrue(ezShop.updateUserRights(userId, "ShopManager"));
        // update a non-existing user
        assertFalse(ezShop.updateUserRights(100, "ShopManager"));
        assert (ezShop.getUser(userId).getRole().equals("ShopManager"));
        ezShop.deleteUser(userId);
        ezShop.logout();
    }

}
