# Design Document 


Authors: 

Date: 24/04/2021

Version: 01


# Contents

- [High level design](#package-diagram)
- [Low level design](#class-diagram)
- [Verification traceability matrix](#verification-traceability-matrix)
- [Verification sequence diagrams](#verification-sequence-diagrams)

# Instructions

The design must satisfy the Official Requirements document, notably functional and non functional requirements

# High level design 

_Architecture_:
* EZShop is a **stand-alone** application.

_Architectural patterns_:
* MVC (because there is a GUI)
* layered - 3 tiered

```plantuml
@startuml

package "EzShop" as ez {}
package "EzShop Data" as mt {}
package "EzShop Application Logic" as at {}
package "EzShop GUI" as GUI {}

mt -down-|> ez
at -down-|> ez
GUI -down-|> ez

@enduml
```

# Low level design

```plantuml
@startuml

left to right direction
class Shop {
    +ProductTypeList
    +UsersList
    +CustomersList
    +TicketsList

    +login(string,string)
    +logout()
    +addProductType()
    +getProductTypeByBarCode(integer)
    +createUser(string, string, string)
    +createCustomer(string)
    +updateCustomerData(integer, string, string)
    +deleteUser(integer)
    +getUser(integer)
    +createOrder(string,int,double)
    +searchOrder(integer)
    +payOrderFor(string,int,double)
    +recordOrderArrival(integer)
    +createCard()
    +attachCardToCustomer(string, integer)
    +detachCardFromCustomer(integer)
    +startSaleTransaction()
    +closeSaleTransaction(integer)
    +deleteSaleTicket(integer)
    +getTicketByNumber(integer)
    +startReturnTransaction(integer)
    +endReturnTransaction(integer, boolean)
    +deleteReturnTransaction(integer)
    +returnProduct(integer, integer, integer)
    +getTransactions(string, string)
}
class AccountBook 

AccountBook - Shop

class FinancialTransaction {
 +description
 +amount
 +date
}

AccountBook -- "*" FinancialTransaction

class Credit 
class Debit

Credit --|> FinancialTransaction
Debit --|> FinancialTransaction

class Order
class Sale
class Return

Order --|> Debit
Sale --|> Credit
Return --|> Debit


class ProductType{
    +barCode
    +description
    +sellPrice
    +quantity
    +discountRate
    +notes

    +addPosition()
    +updatePosition()
    +updatePrice()
    +updateQuantity()
}

Shop - "*" ProductType

EzShop - "*" SaleTransaction

class SaleTransaction {
    +ID 
    +date
    +time
    +cost
    +paymentType
    +discount rate
    +ProductsMap

    +addProductToSale(integer,string,int)
    +deleteProductFromSale(integer,string,int)
    +applyDiscountRateToProduct(integer,string,double)
    +applyDiscountRateToSale(integer,double)
    +computePointsForSale(integer)
    +modifyPointsOnCard(string,int)
    +printTicket()
    +getBalance()
}
SaleTransaction - "*" ProductType

class Quantity {
    +quantity
}
(SaleTransaction, ProductType)  .. Quantity

class LoyaltyCard {
    +ID
    +points
}

class Customer {
    +name
    +surname
    +LoyaltyCardID
}

LoyaltyCard "0..1" - Customer

SaleTransaction "*" -- "0..1" LoyaltyCard

class Position {
    +aisleID
    +rackID
    +levelID
}

ProductType - "0..1" Position

class Order {
  +ID
  +supplier
  +pricePerUnit
  +quantity
  +status

  +updateStatus()
}

Order "*" - ProductType

class ReturnTransaction {
  +quantity
  +returnedValue
}

ReturnTransaction "*" - SaleTransaction
ReturnTransaction "*" - ProductType

class User {
    +ID
    +username
    +password
    +accessRights

    +getAccessRight()
    +updateUserRight(integer,string)    
}

EzShop -- "1..*" User

@enduml
```







# Verification traceability matrix

\<for each functional requirement from the requirement document, list which classes concur to implement it>











# Verification sequence diagrams 
\<select key scenarios from the requirement document. For each of them define a sequence diagram showing that the scenario can be implemented by the classes and methods in the design>

