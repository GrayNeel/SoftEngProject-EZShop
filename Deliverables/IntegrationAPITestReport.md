# Integration and API Test Documentation

Authors: Group 38

Date:

Version:

# Contents

- [Dependency graph](#dependency-graph)

- [Integration approach](#integration)

- [Tests](#tests)

- [Scenarios](#scenarios)

- [Coverage of scenarios and FR](#scenario-coverage)
- [Coverage of non-functional requirements](#nfr-coverage)



# Dependency graph 

     <report the here the dependency graph of the classes in EzShop, using plantuml>

```plantuml
@startuml
digraph dependencyGraph {
 GUI -> API ;
 API -> EZShopDB ;
 API -> ReturnTransactionClass;
 API -> SaleTransactionClass ;
 API -> UserClass ;
 API -> OrderClass ;
 API -> ProductTypeClass ;
 API -> CustomerClass;
 API -> CreditCardClass;
 API -> ProductReturnClass;
 API -> BalanceOperationClass;
 API -> TicketEntryClass;

 EZShopDB -> ReturnTransactionClass;
 EZShopDB -> SaleTransactionClass;
 EZShopDB -> UserClass;
 EZShopDB -> OrderClass;
 EZShopDB -> ProductTypeClass;
 EZShopDB -> CustomerClass;
 EZShopDB -> CreditCardClass;
 EZShopDB -> ProductReturnClass;
 EZShopDB -> BalanceOperationClass;
 EZShopDB -> TicketEntryClass;

 ReturnTransactionClass -> ReturnTransaction;
 SaleTransactionClass -> SaleTransaction;
 SaleTransactionClass -> TicketEntry;
 UserClass -> User;
 OrderClass -> Order;
 ProductTypeClass -> ProductType;
 CustomerClass -> Customer;
 BalanceOperationClass -> BalanceOperation;
 TicketEntryClass -> TicketEntry;
 
}
@enduml
```
     
# Integration approach

    <Write here the integration sequence you adopted, in general terms (top down, bottom up, mixed) and as sequence
    (ex: step1: class A, step 2: class A+B, step 3: class A+B+C, etc)> 
    <Some steps may  correspond to unit testing (ex step1 in ex above), presented in other document UnitTestReport.md>
    <One step will  correspond to API testing>
    
     Bottom Up

#  Tests

   <define below a table for each integration step. For each integration step report the group of classes under test, and the names of
     JUnit test cases applied to them> JUnit test classes should be here src/test/java/it/polito/ezshop

## Step 1 - Unit test of leaf classes
| Classes                | JUnit test cases                          |
| ---------------------- | ----------------------------------------- |
| UserClass              | getterAndSetterUserTestCase()             |
| BalanceOperationClass  | getterAndSetterBalanceOperationTestCase() |
| ReturnTransactionClass | getterAndSetterReturnTransactionTestCase()|
| SaleTransactionClass   | getterAndSetterSaleTransactionTestCase()  |
| UserClass              | getterAndSetterUserTestCase()             |
| OrderClass             | getterAndSetterOrderTestCase()            |
| ProductTypeClass       | getterAndSetterProductTypeTestCase()      |
| CustomerClass          | getterAndSetterCustomerTestCase()         |
| CreditCardClass        | getterAndSetterCreditCardTestCase()       |
| ProductReturnClass     | getterAndSetterProductReturnTestCase()    |
| BalanceOperationClass  | getterAndSetterBalanceOperationTestCase() |
| TicketEntryClass       | getterAndSetterticketEntryTestCase()      |


## Step 2 - DB Test (+ Leaf classes)
| Classes  | JUnit test cases                   |
| -------- | ---------------------------------- |
| EZShopDB | All the methods in TestEZShop.java |

## Step 3 - API test (+ DB + Leaf classes)

| Classes                | JUnit test cases                          |
| ---------------------- | ----------------------------------------- |
| EZShop, ProductType    | All the methods in ProductsTest.java      |  
| EZShop, Order          | All the methods in OrdersTest.java        |  

## Step n 

| Classes  | JUnit test cases |
|--|--|
|||


# Scenarios


<If needed, define here additional scenarios for the application. Scenarios should be named
 referring the UC in the OfficialRequirements that they detail>

## Scenario UCx.y

| Scenario |  name |
| ------------- |:-------------:| 
|  Precondition     |  |
|  Post condition     |   |
| Step#        | Description  |
|  1     |  ... |  
|  2     |  ... |



# Coverage of Scenarios and FR


<Report in the following table the coverage of  scenarios (from official requirements and from above) vs FR. 
Report also for each of the scenarios the (one or more) API JUnit tests that cover it. >




| Scenario ID | Functional Requirements covered | JUnit  Test(s) | 
| ----------- | ------------------------------- | ----------- | 
|  ..         | FRx                             |             |             
|  ..         | FRy                             |             |             
| ...         |                                 |             |             
| ...         |                                 |             |             
| ...         |                                 |             |             
| ...         |                                 |             |             



# Coverage of Non Functional Requirements


<Report in the following table the coverage of the Non Functional Requirements of the application - only those that can be tested with automated testing frameworks.>


### 

| Non Functional Requirement | Test name |
| -------------------------- | --------- |
|                            |           |


