# Unit Testing Documentation

Authors: Group 38

Date: 12/05/2021

Version: 01

# Contents

- [Black Box Unit Tests](#black-box-unit-tests)




- [White Box Unit Tests](#white-box-unit-tests)


# Black Box Unit Tests

    <Define here criteria, predicates and the combination of predicates for each function of each class.
    Define test cases to cover all equivalence classes and boundary conditions.
    In the table, report the description of the black box test case and (traceability) the correspondence with the JUnit test case writing the 
    class and method name that contains the test case>
    <JUnit test classes must be in src/test/java/it/polito/ezshop   You find here, and you can use,  class TestEzShops.java that is executed  
    to start tests
    >

 ### **Class *ProductTypeClass* - method *validateProductCode***

**Criteria for method *name*:**
	

 - Validity of the string
 - Length of the string
 - Last number is the check of the previous ones

**Predicates for method *name*:**

| Criteria                                      | Predicate                                   |
| --------------------------------------------- | ------------------------------------------- |
| String contains a number                      | Yes                                         |
|                                               | No                                          |
| Length of the string                          | 8, 12, 13, 14, 17, 18                       |
|                                               | [0, 7] , [9, 11], [15, 16], [19, maxDouble] |
| Last number is the check of the previous ones | Yes                                         |
|                                               | No                                          |

**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |

**Combination of predicates**:


| String contains a number | Length of the string | Last number is the check of the previous ones | Valid / Invalid | Description of the test case | JUnit test case |
|-------|-------|-------|-------|-------|-------|
|No|*|*|Invalid|T1("df") -> false <br />T2("") -> false|validateProductCodeTestCase()|
|*|[0,7]|*|Invalid|T3("1234567") -> false|''|
|*|[9, 11]|*|Invalid|T4("1234567890") -> false|''|
|*|[15, 16]|*|Invalid|T5("333333333333333") -> false|''|
|*|[19, maxDouble]|*|Invalid|T5("44444444444444444444") -> false|''|
|Yes|8|No|Invalid|T6("12345678") -> false|''|
|Yes|8|Yes|Valid|T7("12345670") -> true|''|
|Yes|12|No|Invalid|T8("123456756328") -> true|''|
|Yes|12|Yes|Valid|T9("123456756324") -> false|''|
|Yes|13|No|Invalid|T10("8717163994254") -> false|''|
|Yes|13|Yes|Valid|T11("8717163994250") -> true|''|
|Yes|14|No|Invalid|T12("12344674332822") -> false|''|
|Yes|14|Yes|Valid|T13("12344674332827") -> true|''|
|Yes|17|No|Invalid|T14("12344674332827777") -> false|''|
|Yes|17|Yes|Valid|T15("12344674332827772") -> true|''|
|Yes|18|No|Invalid|T16("123446743328277775") -> false|''|
|Yes|18|Yes|Valid|T17("123446743328277771") -> true|''|

 ### **Class *EZShopDB* - method *checkExistingProductType***

**Criteria for method *name*:**

 - The string is contained in the database

**Predicates for method *name*:**

| Criteria                                | Predicate |
| --------------------------------------- | --------- |
| The string is contained in the database | Yes       |
|                                         | No        |

**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |

**Combination of predicates**:


| The string is contained in the database | Valid / Invalid | Description of the test case | JUnit test case                    |
| --------------------------------------- | --------------- | ---------------------------- | ---------------------------------- |
| Yes                                     | Valid           | T1("8859392701093") -> true  | checkExistingProductTypeTestCase() |
| No                                      | Invalid         | T2("blabla") -> false        | ''                                 |

 ### **Class *EZShopDB* - method *addProductType***

**Criteria for method *name*:**

 - Validity of the object ProductType

**Predicates for method *name*:**

| Criteria                           | Predicate |
| ---------------------------------- | --------- |
| Validity of the object ProductType | Yes       |
|                                    | No        |



**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |



**Combination of predicates**:


| Validity of the object ProductType | Valid / Invalid | Description of the test case | JUnit test case                   |
| ---------------------------------- | --------------- | ---------------------------- | --------------------------------- |
| Yes                                | Valid           | T1(valid PT) -> true         | addAndDeleteProductTypeTestCase() |
| No                                 | Invalid         | T2(NULL) -> false            | ''                                |

### **Class *EZShopDB* - method *updateProductType***

**Criteria for method \*name\*:** 

- Validity of the id parameter
- Validity of the newDescription parameter
- Validity of the newCode parameter
- Validity of the newPrice parameter
- Validity of the newNote parameter

**Predicates for method \*name\*:**

| Criteria                                 | Predicate |
| ---------------------------------------- | --------- |
| Validity of the id parameter             | Yes       |
|                                          | No        |
| Validity of the newDescription parameter | Yes       |
|                                          | No        |
| Validity of the newCode parameter        | Yes       |
|                                          | No        |
| Validity of the newPrice parameter       | Yes       |
|                                          | No        |
| Validity of the newNote parameter        | Yes       |
|                                          | No        |

**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |

**Combination of predicates**:

| Validity of the id parameter | Validity of the newDescription parameter | Validity of the newCode parameter | Validity of the newPrice parameter | Validity of the newNote parameter | Valid / Invalid | Description of the test case           | JUnit test case             |
| ---------------------------- | ---------------------------------------- | --------------------------------- | ---------------------------------- | --------------------------------- | --------------- | -------------------------------------- | --------------------------- |
| No                           | *                                        | *                                 | *                                  | *                                 | Invalid         | T1("d","ok","333","2","good") -> false | updateProductTypeTestCase() |
| *                            | No                                       | *                                 | *                                  | *                                 | Invalid         | T2(100,3,"333","2","good") -> error    | ''                          |
| *                            | *                                        | No                                | *                                  | *                                 | Invalid         | T3(100,"ok",3,"2","good") -> error     | ''                          |
| *                            | *                                        | *                                 | No                                 | *                                 | Invalid         | T4(100,"ok","333",3,"good") -> error   | ''                          |
| *                            | *                                        | *                                 | *                                  | No                                | Invalid         | T5(100,"ok","333","2",3) -> error      | ''                          |
| Yes                          | Yes                                      | Yes                               | Yes                                | Yes                               | Valid           | T6(100,"ok","333","2","good") -> true  | ''                          |

 ### **Class *EZShopDB* - method *deleteProductType***

**Criteria for method *name*:**

 - The given ID is in the database

**Predicates for method *name*:**

| Criteria                        | Predicate |
| ------------------------------- | --------- |
| The given ID is in the database | Yes       |
|                                 | No        |

**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |



**Combination of predicates**:


| The given ID is in the database | Valid / Invalid | Description of the test case           | JUnit test case                   |
| ------------------------------- | --------------- | -------------------------------------- | --------------------------------- |
| Yes                             | Valid           | T1(an ID in the database) -> true      | addAndDeleteProductTypeTestCase() |
| No                              | Invalid         | T2(an ID not in the database) -> false | ''                                |

### **Class *EZShopDB* - method *getAllProductTypes***

**Criteria for method \*name\*:** 

- There are ProductTypes in the database

**Predicates for method \*name\*:**

| Criteria                               | Predicate |
| -------------------------------------- | --------- |
| There are ProductTypes in the database | Yes       |
|                                        | No        |



**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |



**Combination of predicates**:

| There are ProductTypes in the database | Valid / Invalid | Description of the test case | JUnit test case              |
| -------------------------------------- | --------------- | ---------------------------- | ---------------------------- |
| Yes                                    | Valid           | T1() -> List(ProductTypes)   | getAllProductTypesTestCase() |
| No                                     | Invalid         | T2() -> Emptylist            | ''                           |

### **Class *class_name* - method *getProductTypeByBarCode***

**Criteria for method \*name\*:** 

- Validity of the barCode string

**Predicates for method \*name\*:**

| Criteria                       | Predicate |
| ------------------------------ | --------- |
| Validity of the barCode string | Yes       |
|                                | No        |



**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |



**Combination of predicates**:

| Validity of the barCode string | Valid / Invalid | Description of the test case           | JUnit test case                   |
| ------------------------------ | --------------- | -------------------------------------- | --------------------------------- |
| Yes                            | Valid           | T1(a barCode in the DB) -> ProductType | getProductTypeByBarCodeTestCase() |
| No                             | Invalid         | T2(a barCode not in the DB) -> NULL    | ''                                |

### **Class *EZShopDB* - method *getProductTypesByDescription***

**Criteria for method \*name\*:** 

- Validity of the description string

**Predicates for method \*name\*:**

| Criteria                           | Predicate |
| ---------------------------------- | --------- |
| Validity of the description string | Yes       |
|                                    | No        |



**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |



**Combination of predicates**:

| Validity of the description string | Valid / Invalid | Description of the test case                   | JUnit test case                       |
| ---------------------------------- | --------------- | ---------------------------------------------- | ------------------------------------- |
| Yes                                | Valid           | T1(a description in the DB) -> ProductTypeList | getProductTypeByDescriptionTestCase() |
| No                                 | Invalid         | T2(a description not in the DB) -> EmptyList   | ''                                    |

### **Class *EZShopDB* - method *getQuantityByProductTypeId***

**Criteria for method \*name\*:** 

- Validity of the id parameter

**Predicates for method \*name\*:**

| Criteria                     | Predicate |
| ---------------------------- | --------- |
| Validity of the id parameter | Yes       |
|                              | No        |



**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |



**Combination of predicates**:

| Validity of the description string | Valid / Invalid | Description of the test case    | JUnit test case                      |
| ---------------------------------- | --------------- | ------------------------------- | ------------------------------------ |
| Yes                                | Valid           | T1(an ID in the DB) -> quantity | getQuantityByProductTypeIdTestCase() |
| No                                 | Invalid         | T2(an ID not in the DB) -> null | ''                                   |

### **Class *EZShopDB* - method *updateQuantityByProductTypeId***

**Criteria for method \*name\*:** 

- Validity of the id parameter
- Validity of the newQuantity parameter

**Predicates for method \*name\*:**

| Criteria                              | Predicate |
| ------------------------------------- | --------- |
| Validity of the id parameter          | Yes       |
|                                       | No        |
| Validity of the newQuantity parameter | Yes       |
|                                       | No        |



**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |



**Combination of predicates**:

| Validity of the description string | Validity of the newQuantity parameter | NoValid / Invalid | Description of the test case       | JUnit test case                         |
| ---------------------------------- | ------------------------------------- | ----------------- | ---------------------------------- | --------------------------------------- |
| No                                 | *                                     | Invalid           | T1(an ID not in the DB,4) -> false | updateQuantityByProductTypeIdTestCase() |
| *                                  | No                                    | Invalid           | T2(an ID in the DB, "ss") -> error | ''                                      |
| Yes                                | Yes                                   | Valid             | T3(an ID in the DB, 4) -> true     | ''                                      |

### **Class *EZShopDB* - method *isLocationUsed***

**Criteria for method \*name\*:** 

- Parameter pos found in the DB

**Predicates for method \*name\*:**

| Criteria                      | Predicate |
| ----------------------------- | --------- |
| Parameter pos found in the DB | Yes       |
|                               | No        |



**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |



**Combination of predicates**:

| Parameter pos found in the DB | NoValid / Invalid | Description of the test case        | JUnit test case          |
| ----------------------------- | ----------------- | ----------------------------------- | ------------------------ |
| No                            | Invalid           | T1(position not in the db) -> false | isLocationUsedTestCase() |
| Yes                           | Valid             | T2(position in the DB) -> true      | ''                       |

### **Class *EZShopDB* - method *updateProductTypeLocation***

**Criteria for method \*name\*:** 

- Validity of the productId parameter
- Validity of the newPos parameter

**Predicates for method \*name\*:**

| Criteria                            | Predicate |
| ----------------------------------- | --------- |
| Validity of the productId parameter | Yes       |
|                                     | No        |
| Validity of the newPos parameter    | Yes       |
|                                     | No        |



**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |



**Combination of predicates**:

| Validity of the productId parameter | Validity of the newPos parameter | NoValid / Invalid | Description of the test case             | JUnit test case                     |
| ----------------------------------- | -------------------------------- | ----------------- | ---------------------------------------- | ----------------------------------- |
| No                                  | *                                | Invalid           | T1(an ID not in the DB,"4-4-4") -> false | updateProductTypeLocationTestCase() |
| *                                   | No                               | Invalid           | T2(an ID in the DB, 4) -> false          | ''                                  |
| Yes                                 | Yes                              | Valid             | T3(an ID in the DB, "4-4-4") -> true     | ''                                  |

### **Class *EZShopDB* - method *addAndIssueOrder***

**Criteria for method \*name\*:** 

- Validity of the Order object

**Predicates for method \*name\*:**

| Criteria                     | Predicate |
| ---------------------------- | --------- |
| Validity of the Order object | Yes       |
|                              | No        |



**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |



**Combination of predicates**:

| Validity of the Order object | Valid / Invalid | Description of the test case | JUnit test case                      |
| ---------------------------- | --------------- | ---------------------------- | ------------------------------------ |
| Yes                          | Valid           | T1(valid Order) -> true      | addAndIssueOrderThenDeleteTestCase() |
| No                           | Invalid         | T2(NULL) -> false            | ''                                   |

### **Class *EZShopDB* - method *setBalanceIdInOrder***

**Criteria for method \*name\*:** 

- Validity of the orderId parameter
- Validity of the balanceId parameter

**Predicates for method \*name\*:**

| Criteria                            | Predicate |
| ----------------------------------- | --------- |
| Validity of the orderId parameter   | Yes       |
|                                     | No        |
| Validity of the balanceId parameter | Yes       |
|                                     | No        |



**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |



**Combination of predicates**:

| Validity of the orderId parameter | Validity of the balanceId parameter | NoValid / Invalid | Description of the test case       | JUnit test case               |
| --------------------------------- | ----------------------------------- | ----------------- | ---------------------------------- | ----------------------------- |
| No                                | *                                   | Invalid           | T1(an ID not in the DB,4) -> false | setBalanceIdInOrderTestCase() |
| *                                 | No                                  | Invalid           | T2(an ID in the DB, NULL) -> false | ''                            |
| Yes                               | Yes                                 | Valid             | T3(an ID in the DB, 4) -> true     | ''                            |

### **Class *EZShopDB* - method *payOrderById***

**Criteria for method \*name\*:** 

- Validity of the orderId parameter

**Predicates for method \*name\*:**

| Criteria                          | Predicate |
| --------------------------------- | --------- |
| Validity of the orderId parameter | Yes       |
|                                   | No        |



**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |



**Combination of predicates**:

| Validity of the orderId parameter | Valid / Invalid | Description of the test case      | JUnit test case        |
| --------------------------------- | --------------- | --------------------------------- | ---------------------- |
| Yes                               | Valid           | T1(an ID in the database) -> true | payOrderByIdTestCase() |
| No                                | Invalid         | T2(NULL) -> false                 | ''                     |

### **Class *EZShopDB* - method *recordOrderArrivalById***

**Criteria for method \*name\*:** 

- Validity of the orderId parameter

**Predicates for method \*name\*:**

| Criteria                          | Predicate |
| --------------------------------- | --------- |
| Validity of the orderId parameter | Yes       |
|                                   | No        |



**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |



**Combination of predicates**:

| Validity of the orderId parameter | Valid / Invalid | Description of the test case      | JUnit test case                  |
| --------------------------------- | --------------- | --------------------------------- | -------------------------------- |
| Yes                               | Valid           | T1(an ID in the database) -> true | recordOrderArrivalByIdTestCase() |
| No                                | Invalid         | T2(NULL) -> false                 | ''                               |

### **Class *EZShopDB* - method *getAllOrders***

**Criteria for method \*name\*:** 

- There are Orders in the database

**Predicates for method \*name\*:**

| Criteria                         | Predicate |
| -------------------------------- | --------- |
| There are Orders in the database | Yes       |
|                                  | No        |



**Boundaries**:

| Criteria | Boundary values |
| -------- | --------------- |
|          |                 |



**Combination of predicates**:

| There are Orders in the database | Valid / Invalid | Description of the test case | JUnit test case        |
| -------------------------------- | --------------- | ---------------------------- | ---------------------- |
| Yes                              | Valid           | T1() -> List(Order)          | getAllOrdersTestCase() |
| No                               | Invalid         | T2() -> Emptylist            | ''                     |

# White Box Unit Tests

### Test cases definition

    <JUnit test classes must be in src/test/java/it/polito/ezshop>
    <Report here all the created JUnit test cases, and the units/classes under test >
    <For traceability write the class and method name that contains the test case>


| Unit name | JUnit test case |
|--|--|
|ProductType|getterAndSetterProductTypeTestCase()|
|Order|getterAndSetterOrderTestCase()|
|User|getterAndSetterUserTestCase()|
|Customer|getterAndSetterCustomerTestCase()|
|BalanceOperation|getterAndSetterBalanceOperationTestCase()|
|ReturnTransaction|getterAndSetterReturnTransactionTestCase()|
|ProductReturn|getterAndSetterProductReturnTestCase()|
|TicketEntry|getterAndSetterTicketEntryTestCase()|
|SaleTransaction|getterAndSetterSaleTransactionTestCase()|
|CreditCard|getterAndSetterCreditCardTestCase()|

### Code coverage report

    <Add here the screenshot report of the statement and branch coverage obtained using
    the Eclemma tool. >


### Loop coverage analysis

    <Identify significant loops in the units and reports the test cases
    developed to cover zero, one or multiple iterations >

|Unit name | Loop rows | Number of iterations | JUnit test case |
|---|---|---|---|
|||||
|||||
||||||



